/*
 * $RCSfile: Constants.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.config;

/**
 * <p>Title: Constants</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Constants {
    /**
     * 集群中当前机器的名称, 全局唯一
     */
    public static final String CLUSTER_NODE_NAME = "finder.cluster.node.name";

    /**
     * 集群中master机器的名称
     */
    public static final String CLUSTER_MASTER_NAME = "finder.cluster.master.name";

    /**
     * 集群间通信安全KEY
     */
    public static final String CLUSTER_SECURITY_KEY = "finder.cluster.security.key";

    /**
     * 用户会话有效期
     */
    public static final String SESSION_TIMEOUT = "finder.session.timeout";

    /**
     * 首页是否显示导航条
     */
    public static final String DISPLAY_NAV_BAR = "finder.display.nav-bar";

    /**
     * 文件列表页面显示的按钮
     */
    public static final String DISPLAY_OPERATE_BUTTON = "finder.display.operate-button";

    /**
     * 客户端上传大文件的分片大小
     */
    public static final String UPLOAD_PART_SIZE = "finder.upload.part-size";
}
