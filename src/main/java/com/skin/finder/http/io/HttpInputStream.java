/*
 * $RCSfile: HttpInputStream.java,v $
 * $Revision: 1.1  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.http.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>Title: HttpInputStream</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class HttpInputStream extends InputStream {
    private long position = 0L;
    private long contentLength;
    private InputStream inputStream;

    /**
     * @param inputStream
     */
    public HttpInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * @param inputStream
     * @param contentLength
     */
    public HttpInputStream(InputStream inputStream, long contentLength) {
        this.inputStream = inputStream;
        this.contentLength = contentLength;
    }

    /**
     * @return int
     * @throws IOException
     */
    @Override
    public int read() throws IOException {
        int i = -1;

        if(this.position < this.contentLength) {
            i = this.inputStream.read();

            if(i != -1) {
                this.position++;
            }
        }

        return i;
    }

    /**
     * @param buf
     * @return int
     * @throws IOException
     */
    @Override
    public int read(byte[] buf) throws IOException {
        return this.read(buf, 0, buf.length);
    }

    /**
     * @param buf
     * @param offset
     * @param length
     * @return int
     * @throws IOException
     */
    @Override
    public int read(byte[] buf, int offset, int length) throws IOException {
        if(this.position < this.contentLength) {
            int count = length;
            long remain = this.contentLength - this.position;

            if(remain < length) {
                count = (int)remain;
            }

            int bytes = this.inputStream.read(buf, offset, count);
            this.position += bytes;
            return bytes;
        }
        return -1;
    }
}
