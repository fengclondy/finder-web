/*
 * $RCSfile: FileItem.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-11-1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder;

/**
 * <p>Title: FileItem</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @version 1.0
 */
public class FileItem {
    private String fileName;
    private String fileType;
    private String fileIcon;
    private long fileSize;
    private long lastModified;
    private boolean isFile;

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
     * @return the fileType
     */
    public String getFileType() {
        return this.fileType;
    }
    
    /**
     * @param fileType the fileType to set
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
    /**
     * @return the fileIcon
     */
    public String getFileIcon() {
        return this.fileIcon;
    }
    
    /**
     * @param fileIcon the fileIcon to set
     */
    public void setFileIcon(String fileIcon) {
        this.fileIcon = fileIcon;
    }
    
    /**
     * @return the fileSize
     */
    public long getFileSize() {
        return this.fileSize;
    }
    
    /**
     * @param fileSize the fileSize to set
     */
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
    
    /**
     * @return the lastModified
     */
    public long getLastModified() {
        return this.lastModified;
    }
    
    /**
     * @param lastModified the lastModified to set
     */
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
    
    /**
     * @return the isFile
     */
    public boolean getIsFile() {
        return this.isFile;
    }

    /**
     * @param isFile the isFile to set
     */
    public void setIsFile(boolean isFile) {
        this.isFile = isFile;
    }
}
