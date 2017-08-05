/*
 * $RCSfile: FileRange.java,v $
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * <p>Title: FileRange</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class FileRange {
    private long start;
    private long end;
    private long count;
    private long length;
    private int rows;
    private byte[] buffer;
    private String charset;

    /**
     * @return the start
     */
    public long getStart() {
        return this.start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(long start) {
        this.start = start;
    }

    /**
     * @return the end
     */
    public long getEnd() {
        return this.end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(long end) {
        this.end = end;
    }

    /**
     * @return the count
     */
    public long getCount() {
        return this.count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(long count) {
        this.count = count;
    }

    /**
     * @return the length
     */
    public long getLength() {
        return this.length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(long length) {
        this.length = length;
    }

    /**
     * @return the rows
     */
    public int getRows() {
        return this.rows;
    }

    /**
     * @param rows the rows to set
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * @return the buffer
     */
    public byte[] getBuffer() {
        return this.buffer;
    }

    /**
     * @param buffer the buffer to set
     */
    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
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
     * @return String
     */
    public String getContent() {
        if(this.charset != null) {
            return this.getContent(this.charset);
        }
        else {
            return this.getContent("utf-8");
        }
    }

    /**
     * @param charset
     * @return String
     */
    public String getContent(String charset) {
        if(this.buffer == null) {
            return "";
        }

        try {
            return new String(this.buffer, 0, (int)(this.count), charset);
        }
        catch (UnsupportedEncodingException e) {
        }
        return "";
    }

    /**
     * @param outputStream
     * @throws IOException
     */
    public void write(OutputStream outputStream) throws IOException {
        if(this.buffer != null && this.count > 0L) {
            outputStream.write(this.buffer, 0, (int)(this.count));
        }
    }
}
