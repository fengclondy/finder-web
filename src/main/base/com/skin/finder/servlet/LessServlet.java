/*
 * $RCSfile: LessServlet.java,v $$
 * $Revision: 1.1 $
 * $Date: 2016-10-02 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.FileRange;
import com.skin.util.IO;

/**
 * <p>Title: LessServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class LessServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(LessServlet.class);

    /**
     * default
     */
    public LessServlet() {
    }

    /**
     * @param request
     * @param response
     * @param file
     * @throws ServletException
     * @throws IOException
     */
    public void getRange(HttpServletRequest request, HttpServletResponse response, File file) throws ServletException, IOException {
        long position = this.getLong(request, "position", 0L);
        int type = this.getInteger(request, "type", 0);
        int rows = this.getInteger(request, "rows", 10);
        String charset = this.getTrimString(request, "charset");

        /**
         * 不允许请求太多内容
         */
        if(rows > 2000) {
            rows = 2000;
        }

        /**
         * 默认为utf-8
         */
        if(charset.length() < 1) {
            charset = "utf-8";
        }

        RandomAccessFile raf = null;

        try {
            raf = new RandomAccessFile(file, "r");

            if(type == 1) {
                FileRange range = this.next(raf, position, rows, charset);
                this.callback(request, response, 200, "success", range);
                return;
            }
            else {
                FileRange range = this.prev(raf, position, rows, charset);
                this.callback(request, response, 200, "success", range);
                return;
            }
        }
        catch(IOException e) {
            logger.error(e.getMessage(), e);
            this.callback(request, response, 500, "error", (FileRange)null);
            return;
        }
        finally {
            IO.close(raf);
        }
    }

    /**
     * @param request
     * @param response
     * @param file
     * @throws ServletException
     * @throws IOException
     */
    public void getTail(HttpServletRequest request, HttpServletResponse response, File file) throws ServletException, IOException {
        int rows = this.getInteger(request, "rows", 10);
        long position = this.getLong(request, "position", 0);
        String charset = this.getTrimString(request, "charset");

        /**
         * 不允许请求太多内容
         */
        if(rows > 2000) {
            rows = 2000;
        }

        /**
         * 默认为utf-8
         */
        if(charset.length() < 1) {
            charset = "utf-8";
        }

        RandomAccessFile raf = null;

        try {
            raf = new RandomAccessFile(file, "r");
            FileRange range = this.tail(raf, position, rows, charset);
            this.callback(request, response, 200, "success", range);
        }
        catch(IOException e) {
            logger.error(e.getMessage(), e);
            this.callback(request, response, 500, "error", (FileRange)null);
            return;
        }
        finally {
            IO.close(raf);
        }
    }

    /**
     * @param raf
     * @param position
     * @param rows
     * @param charset
     * @return FileRange
     * @throws IOException 
     */
    public FileRange prev(RandomAccessFile raf, long position, int rows, String charset) throws IOException {
        long length = raf.length();

        if(position < 0 || position >= length) {
            return null;
        }

        int bufferSize = 4096;
        byte[] buffer = new byte[bufferSize];

        if(position > 0 && position < length) {
            raf.seek(position);
        }

        int count = 0;
        int readBytes = 0;
        long start = position;
        long end = position;

        while(true) {
            end = start;
            start = Math.max(start - bufferSize + 1, 0L);

            raf.seek(start);
            readBytes = raf.read(buffer, 0, (int)(end - start + 1));
            raf.seek(start);

            for(int i = readBytes - 1; i > -1; i--) {
                if(buffer[i] == '\n') {
                    count++;

                    if(count >= rows) {
                        start = (start + i + 1);
                        raf.seek(start);
                        break;
                    }
                }
            }

            if(start == 0L || count >= rows) {
                break;
            }
        }

        readBytes = (int)(position - start + 1);
        byte[] bytes = new byte[readBytes];
        readBytes = raf.read(bytes, 0, readBytes);

        if(count < 1 && readBytes > 0) {
            count = 1;
        }

        FileRange range = new FileRange();
        range.setStart(start);
        range.setEnd(start + readBytes - 1);
        range.setLength(length);
        range.setRows(count);
        range.setBuffer(bytes);
        range.setCharset(charset);
        return range;
    }

    /**
     * @param raf
     * @param position
     * @param rows
     * @param charset
     * @return FileRange
     * @throws IOException 
     */
    public FileRange next(RandomAccessFile raf, long position, int rows, String charset) throws IOException {
        long start = position;
        long length = raf.length();

        if((start + 1) >= length) {
            FileRange range = new FileRange();
            range.setStart(length - 1);
            range.setEnd(length - 1);
            range.setLength(length);
            range.setRows(0);
            range.setCharset(charset);
            return range;
        }

        if(start < 0) {
            start = 0;
        }

        byte LF = 0x0A;
        int readBytes = 0;
        int bufferSize = (int)(Math.min(length - start, 4L * 1024L));
        byte[] buffer = new byte[bufferSize];
        raf.seek(start);

        /**
         * 该方法总是从position处先查找第一个换行符的位置, 然后才开始读取数据, 并从第一个换行符以后的位置开始计算有效行数
         * 客户端允许从进度条点击一个文件位置, 此时传过来的position可能是文件的任意位置
         * 所以此处根据position查找出当前位置所在行的结束位置, 也就是说总是从请求位置的下一行开始获取内容.
         * 一般情况下, 服务端返回的end位置总是位于换行符的位置, 下次请求时传过来的start是上次返回的end位置
         * end总是处于换行符的位置的好处是刚好兼容此处的处理, 否则就需要实现两套逻辑.
         */
        if(start > 0) {
            boolean flag = false;

            while((readBytes = raf.read(buffer, 0, bufferSize)) > 0) {
                for(int i = 0; i < readBytes; i++) {
                    if(buffer[i] == LF) {
                        start = start + i + 1;
                        flag = true;
                        break;
                    }
                }

                if(flag) {
                    break;
                }
                else {
                    start += readBytes;
                }
            }

            if(flag) {
                raf.seek(start);
            }
            else {
                FileRange range = new FileRange();
                range.setStart(length - 1);
                range.setEnd(length - 1);
                range.setLength(length);
                range.setRows(0);
                range.setCharset(charset);
                return range;
            }
        }

        int count = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        while((readBytes = raf.read(buffer, 0, bufferSize)) > 0) {
            for(int i = 0; i < readBytes; i++) {
                if(buffer[i] == LF) {
                    count++;

                    if(count >= rows) {
                        readBytes = i + 1;
                        break;
                    }
                }
            }

            if(readBytes > 0) {
                bos.write(buffer, 0, readBytes);
            }

            if(count >= rows) {
                break;
            }
        }

        byte[] bytes = bos.toByteArray();

        if(count < 1 && bytes.length > 0) {
            count = 1;
        }

        FileRange range = new FileRange();
        range.setStart(start);
        range.setEnd(start + bytes.length - 1);
        range.setLength(length);
        range.setRows(count);
        range.setBuffer(bytes);
        range.setCharset(charset);
        return range;
    }

    /**
     * @param raf
     * @param position
     * @param rows
     * @param charset
     * @return FileRange
     * @throws IOException
     */
    public FileRange tail(RandomAccessFile raf, long position, int rows, String charset) throws IOException {
        if(position <= 0) {
            long length = raf.length();
            return this.prev(raf, length - 1, rows, charset);
        }
        else {
            return this.next(raf, position, rows, charset);
        }
    }

    /**
     * @param status
     * @param message
     * @param range
     * @throws IOException 
     */
    protected void callback(HttpServletRequest request, HttpServletResponse response, int status, String message, FileRange range) throws IOException {
        String content = this.getReturnValue(status, message, range);
        response.setContentType("text/javascript; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println(content);
        out.flush();
    }

    /**
     * @param request
     * @param response
     * @param contentType
     * @param content
     * @throws IOException
     */
    protected void write(HttpServletRequest request, HttpServletResponse response, String contentType, String content) throws IOException {
        byte[] buffer = content.getBytes("utf-8");
        response.setContentType(contentType);
        response.setContentLength(buffer.length);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(buffer);
        outputStream.flush();
    }

    /**
     * @param status
     * @param message
     * @param range
     * @return String
     */
    public String getReturnValue(int status, String message, FileRange range) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("{\"status\": ");
        buffer.append(status);
        buffer.append(", \"message\": \"");
        buffer.append(this.escape(message));
        buffer.append("\"");

        if(range != null) {
            buffer.append(", \"value\": {\"start\": ");
            buffer.append(range.getStart());
            buffer.append(", \"end\": ");
            buffer.append(range.getEnd());
            buffer.append(", \"length\": ");
            buffer.append(range.getLength());
            buffer.append(", \"rows\": ");
            buffer.append(range.getRows());
            buffer.append(", \"content\": \"");
            buffer.append(this.escape(range.getContent()));
            buffer.append("\"}");
        }
        buffer.append("}");
        return buffer.toString();
    }

    /**
     * @param request
     * @param name
     * @param defaultValue
     * @return int
     */
    protected int getInteger(HttpServletRequest request, String name, int defaultValue) {
        String value = request.getParameter(name);

        try {
            return Integer.parseInt(value);
        }
        catch(NumberFormatException e) {
        }
        return defaultValue;
    }

    /**
     * @param request
     * @param name
     * @return String
     */
    public String getTrimString(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return (value != null ? value.trim() : "");
    }

    /**
     * @param request
     * @param name
     * @param defaultValue
     * @return long
     */
    protected long getLong(HttpServletRequest request, String name, long defaultValue) {
        String value = request.getParameter(name);

        try {
            return Long.parseLong(value);
        }
        catch(NumberFormatException e) {
        }
        return defaultValue;
    }

    /**
     * @param source
     * @return String
     */
    private String escape(String source) {
        if(source == null) {
            return "";
        }

        char c;
        StringBuilder buffer = new StringBuilder();

        for(int i = 0, length = source.length(); i < length; i++) {
            c = source.charAt(i);

            switch (c) {
                case '"': {
                    buffer.append("\\\"");break;
                }
                case '\r': {
                    buffer.append("\\r");break;
                }
                case '\n': {
                    buffer.append("\\n");break;
                }
                case '\t': {
                    buffer.append("\\t");break;
                }
                case '\b': {
                    buffer.append("\\b");break;
                }
                case '\f': {
                    buffer.append("\\f");break;
                }
                case '\\': {
                    buffer.append("\\\\");break;
                }
                default : {
                    buffer.append(c);break;
                }
            }
        }
        return buffer.toString();
    }
}
