/*
 * $RCSfile: FileType.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-3-29 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.jstl.util;


/**
 * <p>Title: FileType</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class FileType {
    /**
     * @param path
     * @return String
     */
    public static String getIcon(String path) {
        return com.skin.finder.FileType.getIcon(path);
    }

    /**
     * @param path
     * @return String
     */
    public static String getType(String path) {
        return com.skin.finder.FileType.getType(path);
    }

    /**
     * @param path
     * @return String
     */
    public static String getExtension(String path) {
        return com.skin.finder.FileType.getExtension(path);
    }
}
