/*
 * $RCSfile: ContentEntry.java,v $
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder;

/**
 * <p>Title: ContentEntry</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ContentEntry {
    private String name;
    private int type;
    private byte[] bytes;
    private long lastModified;

    /**
     * bin
     */
    public static final int BIN = 0;

    /**
     * gzip
     */
    public static final int ZIP = 1;

    /**
     * @param name
     * @param type
     * @param bytes
     * @param lastModified
     */
    public ContentEntry(String name, int type, byte[] bytes, long lastModified) {
        this.name = name;
        this.type = type;
        this.bytes = bytes;
        this.lastModified = lastModified;
    }

    /**
     * @return String
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public int getType() {
        return this.type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return the bytes
     */
    public byte[] getBytes() {
        return this.bytes;
    }

    /**
     * @param bytes the bytes to set
     */
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * @return long
     */
    public long getLastModified() {
        return this.lastModified;
    }

    /**
     * @param lastModified
     */
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
}
