/*
 * $RCSfile: ScheduleLogManager.java,v $$
 * $Revision: 1.1 $
 * $Date: 2012-10-30 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.manager;

import java.util.Map;

import com.skin.finder.model.ScheduleLog;
import com.skin.finder.service.ScheduleLogService;
import com.skin.j2ee.util.ScrollPage;

/**
 * <p>Title: ScheduleLogManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ScheduleLogManager {
    private ScheduleLogService scheduleLogService;

    /**
     *
     */
    public ScheduleLogManager() {
        this.scheduleLogService = new ScheduleLogService();
    }

    /**
     * @param scheduleId
     * @return ScheduleLog
     */
    public ScheduleLog getById(long scheduleId) {
        return this.scheduleLogService.getById(scheduleId);
    }

    /**
     * @param scheduleLog
     * @return int
     */
    public long create(ScheduleLog scheduleLog) {
        return this.scheduleLogService.create(scheduleLog);
    }

    /**
     * @param scheduleLog
     * @return int
     */
    public int update(ScheduleLog scheduleLog) {
        return this.scheduleLogService.update(scheduleLog);
    }

    /**
     * @param scheduleId
     * @param params
     * @return int
     */
    public int update(long scheduleId, Map<String, Object> params) {
        return this.scheduleLogService.update(scheduleId,  params);
    }

    /**
     * @param scheduleId
     * @param page
     * @return ScrollPage<ScheduleLog>
     */
    public ScrollPage<ScheduleLog> getListByScheduleId(long scheduleId, ScrollPage<ScheduleLog> page) {
        return this.scheduleLogService.getListByScheduleId(scheduleId, page);
    }

    /**
     * @param scheduleId
     * @return int
     */
    public int delete(long scheduleId) {
        return this.scheduleLogService.delete(scheduleId);
    }
}
