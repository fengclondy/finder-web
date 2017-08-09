/*
 * $RCSfile: FileItemList.java,v $
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder;

import java.util.List;

import com.skin.finder.util.StringUtil;

/**
 * <p>Title: FileItemList</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class FileItemList {
    private String work;
    private String path;
    private String parent;
    private List<FileItem> fileList;

    /**
     * @return the work
     */
    public String getWork() {
        return this.work;
    }

    /**
     * @param work the work to set
     */
    public void setWork(String work) {
        this.work = work;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the parent
     */
    public String getParent() {
        return this.parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(String parent) {
        this.parent = parent;
    }

    /**
     * @return the fileList
     */
    public List<FileItem> getFileList() {
        return this.fileList;
    }

    /**
     * @param fileList the fileList to set
     */
    public void setFileList(List<FileItem> fileList) {
        this.fileList = fileList;
    }

    /**
     * @return String
     */
    public String getJSONString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("{\"work\":\"");
        buffer.append(StringUtil.escape(this.getWork()));
        buffer.append("\",\"path\":\"");
        buffer.append(StringUtil.escape(this.getPath()));
        buffer.append("\",\"parent\":\"");
        buffer.append(StringUtil.escape(this.getParent()));
        buffer.append("\",\"fileList\":[");

        if(this.fileList != null && this.fileList.size() > 0) {
            for(FileItem fileItem : this.fileList) {
                buffer.append("{\"fileName\":\"");
                buffer.append(StringUtil.escape(fileItem.getFileName()));
                buffer.append("\",\"fileType\":\"");
                buffer.append(StringUtil.escape(fileItem.getFileType()));
                buffer.append("\",\"fileIcon\":\"");
                buffer.append(StringUtil.escape(fileItem.getFileIcon()));
                buffer.append("\",\"fileSize\":");
                buffer.append(fileItem.getFileSize());
                buffer.append(",\"lastModified\":");
                buffer.append(fileItem.getLastModified());
                buffer.append(",\"isFile\":");
                buffer.append(fileItem.getIsFile());
                buffer.append(",\"mode\":");
                buffer.append(fileItem.getMode());
                buffer.append("},");
            }
            buffer.deleteCharAt(buffer.length() - 1);
        }
        buffer.append("]}");
        return buffer.toString();
    }

    /**
     * @return String
     */
    public static String getJSONString(List<FileItem> fileItemList) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("[");

        if(fileItemList != null && fileItemList.size() > 0) {
            for(FileItem fileItem : fileItemList) {
                if(fileItem == null) {
                    continue;
                }

                buffer.append("{\"fileName\":\"");
                buffer.append(StringUtil.escape(fileItem.getFileName()));
                buffer.append("\",\"fileType\":\"");
                buffer.append(StringUtil.escape(fileItem.getFileType()));
                buffer.append("\",\"fileIcon\":\"");
                buffer.append(StringUtil.escape(fileItem.getFileIcon()));
                buffer.append("\",\"fileSize\":");
                buffer.append(fileItem.getFileSize());
                buffer.append(",\"lastModified\":");
                buffer.append(fileItem.getLastModified());
                buffer.append(",\"isFile\":");
                buffer.append(fileItem.getIsFile());
                buffer.append(",\"mode\":");
                buffer.append(fileItem.getMode());
                buffer.append("},");
            }
            buffer.deleteCharAt(buffer.length() - 1);
        }
        buffer.append("]");
        return buffer.toString();
    }

    /**
     * @param fileItem
     * @return String
     */
    public static String getJSONString(FileItem fileItem) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("{\"fileName\":\"");
        buffer.append(StringUtil.escape(fileItem.getFileName()));
        buffer.append("\",\"fileType\":\"");
        buffer.append(StringUtil.escape(fileItem.getFileType()));
        buffer.append("\",\"fileIcon\":\"");
        buffer.append(StringUtil.escape(fileItem.getFileIcon()));
        buffer.append("\",\"fileSize\":");
        buffer.append(fileItem.getFileSize());
        buffer.append(",\"lastModified\":");
        buffer.append(fileItem.getLastModified());
        buffer.append(",\"isFile\":");
        buffer.append(fileItem.getIsFile());
        buffer.append(",\"mode\":");
        buffer.append(fileItem.getMode());
        buffer.append("}");
        return buffer.toString();
    }
}
