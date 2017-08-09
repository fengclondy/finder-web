/*
 * $RCSfile: Less.java,v $
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title: Less</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Less {
    private static final byte[] LF = new byte[]{0x0A};

    /**
     * less
     */
    private Less() {
    }

    /**
     * @param raf
     * @param position
     * @param rows
     * @param charset
     * @return FileRange
     * @throws IOException
     */
    public static FileRange prev(RandomAccessFile raf, long position, int rows, String charset) throws IOException {
        long length = raf.length();

        if(position < 0 || position >= length) {
            FileRange range = new FileRange();
            range.setStart(position);
            range.setEnd(position);
            range.setCount(0L);
            range.setLength(length);
            range.setRows(0);
            range.setCharset(charset);
            return range;
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
        range.setCount(readBytes);
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
    public static FileRange next(RandomAccessFile raf, long position, int rows, String charset) throws IOException {
        long start = position;
        long length = raf.length();

        if((start + 1) >= length) {
            start = Math.max(length - 1, 0);
            FileRange range = new FileRange();
            range.setStart(start);
            range.setEnd(start);
            range.setCount(0L);
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
                range.setCount(0L);
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

        if(bytes.length > 0 && bytes[bytes.length - 1] != LF) {
            count++;
        }

        FileRange range = new FileRange();
        range.setStart(start);
        range.setEnd(start + bytes.length - 1);
        range.setCount(bytes.length);
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
    public static FileRange tail(RandomAccessFile raf, long position, int rows, String charset) throws IOException {
        if(position <= 0) {
            long length = raf.length();
            return prev(raf, length - 1, rows, charset);
        }
        else {
            return read(raf, position, rows, charset);
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
    public static FileRange read(RandomAccessFile raf, long position, int rows, String charset) throws IOException {
        long start = position;
        long length = raf.length();

        /**
         * read与next的实现有些不同
         * 1. next总是从当前位置的下一行开始读取, read从当前位置开始读取
         * 2. next返回的是[start, end], 闭区间, 包含start和end位置的数据
         *    read返回的是[start, end), 左闭右开, 包含start但不包含end
         *    这是为了客户端的代码一致
         * tail功能有可能上次读取到的是非换行符结束的位置, 为了下次接着上次的读取, 所以按照客户端传过来position读取
         * 服务端返回给客户端的结束位置是下次客户端请求的开始位置
         */
        if((start + 1) >= length) {
            FileRange range = new FileRange();
            range.setStart(length);
            range.setEnd(length);
            range.setCount(0L);
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

        if(bytes.length > 0 && bytes[bytes.length - 1] != LF) {
            count++;
        }

        FileRange range = new FileRange();
        range.setStart(start);
        range.setEnd(start + bytes.length);
        range.setCount(bytes.length);
        range.setLength(length);
        range.setRows(count);
        range.setBuffer(bytes);
        range.setCharset(charset);
        return range;
    }

    /**
     * @param request
     * @param response
     * @param charset
     * @param status
     * @param message
     * @param range
     * @throws IOException
     */
    public static void callback(HttpServletRequest request, HttpServletResponse response, String charset, int status, String message, FileRange range) throws IOException {
        String content = getReturnValue(status, message, range);
        response.setContentType("text/plain; charset=" + charset);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(content.getBytes(charset));

        if(range != null) {
            outputStream.write(LF, 0, 1);
            range.write(outputStream);
        }
        outputStream.flush();
    }

    /**
     * @param status
     * @param message
     * @param range
     * @return String
     */
    public static String getReturnValue(int status, String message, FileRange range) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("{\"status\": ");
        buffer.append(status);
        buffer.append(", \"message\": \"");
        buffer.append(escape(message));
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
            buffer.append("}");
        }
        buffer.append("}");
        return buffer.toString();
    }

    /**
     * @param source
     * @return String
     */
    public static String escape(String source) {
        if(source == null) {
            return "";
        }

        char c;
        StringBuilder buffer = new StringBuilder();

        for(int i = 0, length = source.length(); i < length; i++) {
            c = source.charAt(i);

            switch (c) {
                case '"': {
                    buffer.append("\\\"");
                    break;
                }
                case '\r': {
                    buffer.append("\\r");
                    break;
                }
                case '\n': {
                    buffer.append("\\n");
                    break;
                }
                case '\t': {
                    buffer.append("\\t");
                    break;
                }
                case '\b': {
                    buffer.append("\\b");
                    break;
                }
                case '\f': {
                    buffer.append("\\f");
                    break;
                }
                case '\\': {
                    buffer.append("\\\\");
                    break;
                }
                default : {
                    buffer.append(c);
                    break;
                }
            }
        }
        return buffer.toString();
    }
}
