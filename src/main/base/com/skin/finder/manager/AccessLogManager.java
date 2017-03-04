/*
 * $RCSfile: AccessLogManager.java,v $$
 * $Revision: 1.1 $
 * $Date: 2014-01-03 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.manager;

import java.util.List;

import com.skin.finder.model.AccessLog;
import com.skin.finder.service.AccessLogService;

/**
 * <p>Title: AccessLogManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class AccessLogManager {
    private AccessLogService accessLogService = null;

    /**
     *
     */
    public AccessLogManager() {
        this.accessLogService = new AccessLogService();
    }

    /**
     * @param logId
     * @return AccessLog
     */
    public AccessLog getById(long logId) {
        return this.getAccessLogService().getById(logId);
    }

    /**
     * @param accessLog
     * @return int
     */
    public int create(AccessLog accessLog) {
        return this.getAccessLogService().create(accessLog);
    }

    /**
     * @param accessLog
     * @return int
     */
    public int update(AccessLog accessLog) {
        return this.getAccessLogService().update(accessLog);
    }

    /**
     * @return int
     */
    public int getCount() {
        return this.getAccessLogService().getCount();
    }

    /**
     * @param pageNum
     * @param pageSize
     * @return List<AccessLog>
     */
    public List<AccessLog> list(int pageNum, int pageSize) {
        return this.getAccessLogService().list(pageNum, pageSize);
    }

    /**
     * @param logId
     * @return int
     */
    public int delete(long logId) {
        return this.getAccessLogService().delete(logId);
    }

    /**
     * @return the accessLogService
     */
    public AccessLogService getAccessLogService() {
        return this.accessLogService;
    }

    /**
     * @param accessLogService the accessLogService to set
     */
    public void setAccessLogService(AccessLogService accessLogService) {
        this.accessLogService = accessLogService;
    }
}