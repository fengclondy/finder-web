/*
 * $RCSfile: LazyFileOutputStream.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.http.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>Title: LazyFileOutputStream</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class LazyFileOutputStream extends OutputStream {
    private String file;
    private FileOutputStream outputStream;

    /**
     * @param file
     */
    public LazyFileOutputStream(String file) {
        this.file = file;
    }

    /**
     * @param b
     * @throws IOException
     */
    @Override
    public void write(int b) throws IOException {
        if(this.outputStream == null) {
            this.outputStream = new FileOutputStream(this.file);
        }
        this.outputStream.write(b);
    }

    /**
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        if(this.outputStream != null) {
            try {
                this.outputStream.close();
            }
            catch(IOException e) {
            }
        }
    }
}
