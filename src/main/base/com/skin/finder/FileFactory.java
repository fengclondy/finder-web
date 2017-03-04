/*
 * $RCSfile: FileFactory.java,v $$
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
 * <p>Title: FileFactory</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @version 1.0
 */
public class FileFactory {
    /**
     * @param size
     * @return FileItem[]
     */
    public static FileItem[] random(int size) {
        FileItem[] fileItems = new FileItem[size];

        for(int i = 0; i < size; i++) {
            FileItem fileItem = new FileItem();
            fileItem.setFileName("test" + i + ".txt");
            fileItem.setLastModified(System.currentTimeMillis());
            fileItem.setFileSize(20000L);
            fileItem.setIsFile(true);
            fileItems[i] = fileItem;
        }
        return fileItems;
    }
}
