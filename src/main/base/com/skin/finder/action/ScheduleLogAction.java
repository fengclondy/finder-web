/*
 * $RCSfile: ScheduleAction.java,v $$

 * $Revision: 1.1  $
 * $Date: 2012-11-3  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.action;

import java.io.IOException;

import javax.servlet.ServletException;

import com.skin.datasource.ConnectionManager;
import com.skin.finder.manager.ScheduleLogManager;
import com.skin.finder.model.ScheduleLog;
import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.util.ScrollPage;

/**
 * <p>Title: ScheduleAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ScheduleLogAction extends BaseAction {
    /**
     * @throws ServletException
     * @throws IOException
     */
    @com.skin.j2ee.annotation.UrlPattern("/system/schedule/log.html")
    public void list() throws ServletException, IOException {
        long scheduleId = this.getLong("scheduleId", 0L);
        ScrollPage<ScheduleLog> page = this.getScrollPage(ScheduleLog.class);

        if(ConnectionManager.available()) {
            new ScheduleLogManager().getListByScheduleId(scheduleId, page);
        }

        this.setAttribute("pageNum", page.getPageNum());
        this.setAttribute("pageSize", page.getPageSize());
        this.setAttribute("logCount", page.getCount());
        this.setAttribute("scheduleLogList", page.getItems());
        this.forward("/template/system/schedule/scheduleLogList.jsp");
    }
}
