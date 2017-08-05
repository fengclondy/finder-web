/*
 * $RCSfile: Part.java,v $
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * <p>Title: Part</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Part {
    private String name;
    private String fileName;
    private String contentType;
    private String charset;
    private long length;
    private File file;
    private InputStream inputStream;
    private HttpHeader httpHeader;

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @return the contentType
     */
    public String getContentType() {
        return this.contentType;
    }
    
    /**
     * @return the charset
     */
    public String getCharset() {
        return this.charset;
    }

    /**
     * @param charset the charset to set
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }

    /**
     * @return the length
     */
    public long length() {
        return this.length;
    }

    /**
     * @param length the length to set
     */
    protected void setLength(long length) {
        this.length = length;
    }

    /**
     * @return the httpHeader
     */
    public HttpHeader getHttpHeader() {
        return this.httpHeader;
    }

    /**
     * @param httpHeader the httpHeader to set
     */
    protected void setHttpHeader(HttpHeader httpHeader) {
        this.httpHeader = httpHeader;
    }

    /**
     * @param name
     * @return String
     */
    public String getHeader(String name) {
        return this.httpHeader.getHeader(name);
    }

    /**
     * @return the fileField
     */
    public boolean isFormField() {
        return (this.getFileName() == null);
    }

    /**
     * @return the fileField
     */
    public boolean isFileField() {
        return (this.getFileName() != null);
    }

    /**
     * @return the inputStream
     * @throws IOException
     */
    public InputStream getInputStream() throws IOException {
        if(this.file != null) {
            return new FileInputStream(this.file);
        }
        return this.inputStream;
    }

    /**
     * @param inputStream the inputStream to set
     */
    protected void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * @return boolean
     */
    public boolean isInMemory() {
        return (this.inputStream instanceof ByteArrayInputStream);
    }

    /**
     * @param file the file to set
     */
    protected void setFile(File file) {
        this.file = file;
    }

    /**
     * @return File
     */
    public File getFile() {
        return this.file;
    }

    /**
     * @return String
     */
    public String getBody() {
        if(this.charset != null) {
            return this.getBody(this.charset);
        }
        else {
            return this.getBody("utf-8");
        }
    }

    /**
     * @param charset
     * @return String
     */
    public String getBody(String charset) {
        InputStream inputStream = null;

        try {
            inputStream = this.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream, charset);
            StringBuilder buffer = new StringBuilder();

            int length = 0;
            char[] cbuf = new char[4096];

            while((length = reader.read(cbuf, 0, 4096)) > 0) {
                buffer.append(cbuf, 0, length);
            }
            return buffer.toString();
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            if(inputStream instanceof ByteArrayInputStream) {
                ((ByteArrayInputStream)inputStream).reset();
            }
            else if(inputStream != null) {
                try {
                    inputStream.close();
                }
                catch(IOException e) {
                }
            }
        }
    }

    /**
     * @param file
     * @throws IOException
     */
    public void write(File file) throws IOException {
        OutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            this.write(outputStream);
        }
        finally {
            if(outputStream != null) {
                try {
                    outputStream.close();
                }
                catch (IOException e) {
                }
            }
        }
    }

    /**
     * @param outputStream
     * @throws IOException
     */
    public void write(OutputStream outputStream) throws IOException {
        this.write(outputStream, 8192);
    }

    /**
     * @param outputStream
     * @param bufferSize
     * @throws IOException
     */
    public void write(OutputStream outputStream, int bufferSize) throws IOException {
        InputStream inputStream = null;

        try {
            int length = 0;
            byte[] buffer = new byte[bufferSize];
            inputStream = this.getInputStream();

            while((length = inputStream.read(buffer, 0, bufferSize)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
        }
        finally {
            if(inputStream instanceof ByteArrayInputStream) {
                ((ByteArrayInputStream)inputStream).reset();
            }
            else if(inputStream != null) {
                try {
                    inputStream.close();
                }
                catch(IOException e) {
                }
            }
        }
    }
    
    /**
     * delete the file
     */
    public void delete() {
        if(this.file != null) {
            try {
                this.file.delete();
            }
            catch(Exception e) {
            }
        }
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(this.httpHeader.toString());
        buffer.append("form-data");
        buffer.append("; name=\"").append(this.name).append("\"");

        if(this.isFileField()) {
            buffer.append("; filename=\"").append(this.fileName).append("\"");
        }
        buffer.append("; length=").append(this.length);

        if(!this.isFileField()) {
            buffer.append("\r\n<<");
            buffer.append(this.getBody("UTF-8"));
            buffer.append(">>\r\n");
        }
        return buffer.toString();
    }
}
