/*
 * $RCSfile: MailLogManager.java,v $$
 * $Revision: 1.1 $
 * $Date: 2015-02-06 17:29:27 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.manager;

import com.skin.finder.model.MailLog;
import com.skin.finder.service.MailLogService;
import com.skin.j2ee.util.ScrollPage;

/**
 * <p>Title: MailLogManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class MailLogManager {
    private MailLogService mailLogService = null;

    /**
     * default
     */
    public MailLogManager() {
        this.mailLogService = new MailLogService();
    }

    /**
     * @param appId
     * @param logId
     * @return MailLog
     */
    public MailLog getById(long appId, long logId) {
        return this.getMailLogService().getById(appId, logId);
    }

    /**
     * @param mailLog
     * @return int
     */
    public int insert(MailLog mailLog) {
        return this.getMailLogService().insert(mailLog);
    }

    /**
     * @param mailLog
     * @return int
     */
    public int update(MailLog mailLog) {
        return this.getMailLogService().update(mailLog);
    }

    /**
     * @param appId
     * @param userId
     * @param page
     * @return ScrollPage<MailLog>
     */
    public ScrollPage<MailLog> getListByUserId(long appId, long userId, ScrollPage<MailLog> page) {
        return this.getMailLogService().getListByUserId(appId, userId, page);
    }

    /**
     * @param appId
     * @param page
     * @return ScrollPage<MailLog>
     */
    public ScrollPage<MailLog> getList(long appId, ScrollPage<MailLog> page) {
        return this.getMailLogService().getList(appId, page);
    }

    /**
     * @param appId
     * @param logId
     * @return int
     */
    public int delete(long appId, long logId) {
        return this.getMailLogService().delete(appId, logId);
    }

    /**
     * @return the mailLogService
     */
    public MailLogService getMailLogService() {
        return this.mailLogService;
    }

    /**
     * @param mailLogService the mailLogService to set
     */
    public void setMailLogService(MailLogService mailLogService) {
        this.mailLogService = mailLogService;
    }
}