/*
 * $RCSfile: FileComparator.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-10-21 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder;

import java.io.File;

/**
 * <p>Title: FileComparator</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @version 1.0
 */
public class FileComparator implements java.util.Comparator<File> {
    private static final FileComparator instance = new FileComparator();

    /**
     * @return FileComparator
     */
    public static FileComparator getInstance() {
        return instance;
    }

    /**
     * @param f1
     * @param f2
     * @return int
     */
    @Override
    public int compare(File f1, File f2) {
        if(f1 == null && f2 == null) {
            return 0;
        }

        if(f1 == null) {
            return 1;
        }

        if(f2 == null) {
            return -1;
        }

        String s1 = f1.getName();
        String s2 = f2.getName();
        return s1.compareToIgnoreCase(s2);
    }
}
