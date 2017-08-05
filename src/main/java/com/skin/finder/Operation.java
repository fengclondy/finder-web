/*
 * $RCSfile: Operation.java,v $
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
 * <p>Title: Operation</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Operation {
    /**
     * none
     */
    public static final int NONE = 0;

    /**
     * 针对文件和目录: 可读
     */
    public static final int READ = 1;

    /**
     * 针对文件和目录: 可写
     */
    public static final int WRITE = 2;

    /**
     * 针对文件和目录: 可删
     */
    public static final int DELETE = 4;

    /**
     * 针对文件和目录: 可下载
     */
    public static final int DOWNLOAD = 8;

    /**
     * 针对文件: 可使用tail
     */
    public static final int TAIL = 16;

    /**
     * 针对文件: 可使用less
     */
    public static final int LESS = 32;

    /**
     * 针对文件: 可使用grep
     */
    public static final int GREP = 64;
}
