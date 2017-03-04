/*
 * $RCSfile: AnalyticsAction.java,v $$
 * $Revision: 1.1 $
 * $Date: 2014-1-3 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import com.skin.datasource.ConnectionManager;
import com.skin.finder.manager.AccessLogManager;
import com.skin.finder.model.AccessLog;
import com.skin.j2ee.action.BaseAction;

/**
 * <p>Title: AnalyticsAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @version 1.0
 */
public class AnalyticsAction extends BaseAction {
    /**
     * @throws ServletException
     * @throws IOException
     */
    @com.skin.j2ee.annotation.UrlPattern("/system/accesslog/index.html")
    public void log() throws ServletException, IOException {
        int logCount = 0;
        int pageSize = 50;
        int pageNum = this.getInteger("pageNum", 1);
        List<AccessLog> accessLogList = null;

        if(ConnectionManager.available()) {
            AccessLogManager accessLogManager = new AccessLogManager();
            logCount = accessLogManager.getCount();
            accessLogList = accessLogManager.list(pageNum, pageSize);
        }

        this.setCache(0);
        this.setAttribute("pageNum", pageNum);
        this.setAttribute("pageSize", pageSize);
        this.setAttribute("logCount", logCount);
        this.setAttribute("accessLogList", accessLogList);
        this.forward("/template/system/accesslog.jsp");
    }
}
