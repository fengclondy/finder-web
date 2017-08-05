/*
 * $RCSfile: Multipart.java,v $
 * $Revision: 1.1 $
 * $Date: 2014-01-03 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.upload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title: Multipart</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Multipart {
    private String repository;
    private int maxFileSize;
    private int maxBodySize;
    private PushbackInputStream inputStream;
    private static final byte[] LF = new byte[]{0x0A};
    private static final byte[] CRLF = new byte[]{0x0D, 0x0A};
    private static final byte[] END = new byte[]{'-', '-'};

    /**
     * @param args
     */
    public static void main(String[] args) {
        int count = 0;

        while(count++ < 1) {
            InputStream inputStream = null;
    
            try {
                inputStream = new FileInputStream("request.data");
                Multipart multipart = new Multipart();
                multipart.setMaxFileSize(20 * 1024 * 1024);
                multipart.setMaxBodySize(20 * 1024 * 1024);
                multipart.setRepository(".");
                List<Part> partList = multipart.parse(inputStream, "------WebKitFormBoundaryXMRUbYUKUWCAAaBi", "utf-8");

                for(Part part : partList) {
                    System.out.println("------------------------");
                    if(part.isFileField()) {
                        System.out.println(part.getFileName());
                    }
                    print(part);
                }
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            finally {
                close(inputStream);
            }
        }
    }

    /**
     *
     */
    public Multipart() {
        this.maxFileSize = 2 * 1024 * 1024;
        this.maxBodySize = 2 * 1024 * 1024;
    }

    /**
     * @param request
     * @return Map<String, Part>
     * @throws IOException
     */
    public List<Part> parse(HttpServletRequest request) throws IOException {
        String method = request.getMethod();
        String contentType = request.getContentType();
        String boundary = null;

        if(!method.equalsIgnoreCase("post")) {
            throw new IOException("BadHttpMethodException: " + method);
        }

        int k = contentType.indexOf(";");

        if(k > -1) {
            boundary = contentType.substring(k + 1).trim();
            contentType = contentType.substring(0, k + 1).toLowerCase();
        }
        else {
            throw new RuntimeException("BadContentTypeException: " + method);
        }

        if(!contentType.equals("multipart/form-data;")) {
            throw new RuntimeException("BadContentTypeException: " + contentType);
        }

        if(boundary.startsWith("boundary=")) {
            boundary = boundary.substring(9).trim();
        }
        else {
            throw new RuntimeException("BadContentTypeException: " + contentType);
        }
        return this.parse(request.getInputStream(), "--" + boundary, request.getCharacterEncoding());
    }

    /**
     * @param inputStream
     * @param seperator
     * @param charset
     * @return List<Part>
     * @throws IOException
     */
    public List<Part> parse(InputStream inputStream, String seperator, String charset) throws IOException {
        long bodyBytes = 0L;
        byte[] bytes = this.getAsciiBytes(seperator);
        this.inputStream = new PushbackInputStream(inputStream, 4096);
        List<Part> items = new ArrayList<Part>();

        try {
            while(this.readBoundary(bytes)) {
                HttpHeader httpHeader = this.readHeaders(charset);
                String content = httpHeader.getHeader("Content-Disposition");
                String contentType = httpHeader.getHeader("Content-Type");
                ContentDisposition disposition = ContentDisposition.parse(content);
                String name = disposition.getProperty("name");
                String fileName = disposition.getProperty("filename");
                String boundary = disposition.getProperty("boundary");

                if(boundary != null) {
                    bytes = this.getAsciiBytes(boundary);
                }

                Part part = new Part();
                part.setName(name);
                part.setFileName(fileName);
                part.setContentType(contentType);
                part.setCharset(charset);
                part.setHttpHeader(httpHeader);
                items.add(part);

                if(fileName != null) {
                    if(this.repository == null) {
                        this.repository = System.getProperty("java.io.tmpdir");
                    }

                    OutputStream outputStream = null;
                    File file = this.getTempFile(this.repository);

                    try {
                        outputStream = new FileOutputStream(file);
                        long length = this.readBody(bytes, outputStream);
                        part.setLength(length);
                        part.setFile(file);
                    }
                    finally {
                        close(outputStream);
                    }
                }
                else {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    long length = this.readBody(bytes, bos);
                    part.setLength(length);
                    part.setInputStream(new ByteArrayInputStream(bos.toByteArray()));
                }

                bodyBytes += part.length();

                if(bodyBytes >= this.maxBodySize) {
                    throw new IOException("stream body too large: " + bodyBytes + ", maxBodySize: " + this.maxBodySize);
                }
            }
            return items;
        }
        catch(Exception e) {
            for(Part part : items) {
                part.delete();
            }

            if(e instanceof IOException) {
                throw (IOException)e;
            }
            else {
                throw new IOException(e);
            }
        }
    }

    /**
     * @return Map<String, String>
     * @throws IOException
     */
    protected HttpHeader readHeaders(String charset) throws IOException {
        HttpHeader httpHeader = new HttpHeader();

        while(true) {
            byte[] bytes = this.readLine(this.inputStream);

            if(Arrays.equals(bytes, CRLF) || Arrays.equals(bytes, LF)) {
                break;
            }

            String header = new String(bytes, charset);
            int k = header.indexOf(":");

            if(k > -1) {
                String name = header.substring(0, k).trim();
                String value = header.substring(k + 1).trim();
                httpHeader.addHeader(name, value);
            }
        }
        return httpHeader;
    }

    /**
     * @param boundary
     * @param outputStream
     * @throws IOException
     */
    protected long readBody(byte[] boundary, OutputStream outputStream) throws IOException {
        byte[] bytes = new byte[boundary.length + 2];
        bytes[0] = 0x0D;
        bytes[1] = 0x0A;
        System.arraycopy(boundary, 0, bytes, 2, boundary.length);
        long readBytes = this.copy(this.inputStream, outputStream, bytes, this.maxFileSize);
        this.readCRLF();
        return readBytes;
    }

    /**
     * @return boolean
     * @throws IOException
     */
    protected boolean readBoundary(byte[] boundary) throws IOException {
        byte[] buffer = new byte[boundary.length];
        int length = this.inputStream.read(buffer, 0, buffer.length);

        if(!equals(buffer, boundary, 0)) {
            throw new IOException("bad boundary: " + new String(buffer, 0, length, "ascii"));
        }

        this.inputStream.read(buffer, 0, 2);

        if(equals(buffer, CRLF, 0)) {
            return true;
        }

        if(equals(buffer, END, 0)) {
            return false;
        }
        throw new IOException("bad boundary.");
    }

    /**
     * @throws IOException
     */
    protected void readCRLF() throws IOException {
        byte[] buf = new byte[2];
        this.inputStream.read(buf, 0, 2);

        if(buf[0] != 0x0D || buf[1] != 0x0A) {
            throw new IOException("CRLF expected at end of boundary. [" + (char)buf[0] + "] - [" + (char)buf[1] + "]");
        }
    }

    /**
     * @param stream
     * @return byte[]
     * @throws IOException
     */
    protected byte[] readLine(InputStream inputStream) throws IOException {
        int b = -1;
        ByteArrayOutputStream bos = new ByteArrayOutputStream(4096);

        while((b = inputStream.read()) != -1) {
            if(b == '\n') {
                bos.write(b);
                break;
            }
            bos.write(b);
        }
        return bos.toByteArray();
    }

    /**
     * @param content
     * @return byte[]
     * @throws UnsupportedEncodingException
     */
    protected byte[] getAsciiBytes(String content) throws UnsupportedEncodingException {
        return content.getBytes("US-ASCII");
    }

    /**
     * @param outputStream
     * @return long
     * @throws IOException
     */
    protected long copy(PushbackInputStream pushback, OutputStream outputStream, byte[] boundary, int limit) throws IOException {
        int k = 0;
        int length = 0;
        long readBytes = 0L;
        int bufferSize = Math.max(boundary.length << 1, 4096);
        byte[] buffer = new byte[bufferSize];

        while((length = pushback.read(buffer, 0, bufferSize)) > 0) {
            k = indexOf(buffer, boundary, 0, length);

            if(k > -1) {
                readBytes += k;
                pushback.unread(buffer, k, length - k);
                outputStream.write(buffer, 0, k);
                break;
            }

            readBytes += (length - boundary.length);

            if(readBytes > limit) {
                throw new IOException("stream body too large: " + readBytes);
            }

            outputStream.write(buffer, 0, length - boundary.length);
            pushback.unread(buffer, length - boundary.length, boundary.length);
        }
        outputStream.flush();
        return readBytes;
    }

    /**
     * @return OutputStream
     * @throws IOException
     */
    protected File getTempFile(String work) throws IOException {
        if(work == null) {
            throw new IOException("'work' must be not null.");
        }

        long timeMillis = System.currentTimeMillis();
        File file = new File(work, timeMillis + ".tmp");

        while(file.exists()) {
            timeMillis++;
            file = new File(work, timeMillis + ".tmp");
        }
        file.createNewFile();
        return file;
    }

    /**
     * @param bytes
     * @param searchment
     * @param offset
     * @return int
     */
    private int indexOf(byte[] bytes, byte[] searchment, int offset, int length) {
        for(int j = offset; j < offset + length; j++) {
            if(equals(bytes, searchment, j)) {
                return j;
            }
        }
        return -1;
    }

    /**
     * @return the repository
     */
    public String getRepository() {
        return this.repository;
    }

    /**
     * @param repository the repository to set
     */
    public void setRepository(String repository) {
        this.repository = repository;
    }

    /**
     * @return the maxFileSize
     */
    public int getMaxFileSize() {
        return this.maxFileSize;
    }

    /**
     * @param maxFileSize the maxFileSize to set
     */
    public void setMaxFileSize(int maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    /**
     * @return the maxBodySize
     */
    public int getMaxBodySize() {
        return this.maxBodySize;
    }

    /**
     * @param maxBodySize the maxBodySize to set
     */
    public void setMaxBodySize(int maxBodySize) {
        this.maxBodySize = maxBodySize;
    }

    /**
     * @param buf1
     * @param buf2
     * @param offset
     * @param length
     * @return boolean
     */
    private static boolean equals(byte[] buf1, byte[] buf2, int offset) {
        int length = buf2.length;

        if(buf1.length - offset < length) {
            return false;
        }

        for(int i = 0; i < length; i++) {
            if(buf1[offset + i] != buf2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param part
     */
    public static void print(Part part) {
        HttpHeader httpHeader = part.getHttpHeader();
        System.out.print(httpHeader.toString());
        System.out.println("name: " + part.getName());
        System.out.println("fileName: " + part.getFileName());
        System.out.println("isFileField: " + part.isFileField());
        System.out.println("length: " + part.length());

        if(part.isFileField()) {
            System.out.println("file: " + part.getFile().getAbsolutePath());
            System.out.println("<<Blob>>");
        }
        else {
            System.out.println("body: " + part.getBody("utf-8"));
        }
    }

    /**
     * @param closeable
     */
    public static void close(Closeable closeable) {
        if(closeable != null) {
            try {
                closeable.close();
            }
            catch(IOException e) {
            }
        }
    }
}
