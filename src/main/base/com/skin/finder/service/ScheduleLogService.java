/*
 * $RCSfile: ScheduleManager.java,v $$
 * $Revision: 1.1  $
 * $Date: 2012-10-30  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import com.skin.datasource.ConnectionManager;
import com.skin.finder.dao.ScheduleLogDao;
import com.skin.finder.model.ScheduleLog;
import com.skin.j2ee.util.ScrollPage;

/**
 * <p>Title: ScheduleLogService</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ScheduleLogService {
    /**
     * @param logId
     * @return ScheduleLog
     */
    public ScheduleLog getById(long logId) {
        Connection connection = null;

        try {
            connection = ConnectionManager.getConnection();
            ScheduleLogDao dao = new ScheduleLogDao(connection);
            return dao.getById(logId);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param scheduleLog
     * @return long
     */
    public int create(ScheduleLog scheduleLog) {
        Connection connection = null;

        try {
            connection = ConnectionManager.getConnection();
            ScheduleLogDao dao = new ScheduleLogDao(connection);
            return dao.create(scheduleLog);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param scheduleLog
     * @return ScheduleLog
     */
    public int update(ScheduleLog scheduleLog) {
        Connection connection = null;

        try {
            connection = ConnectionManager.getConnection();
            ScheduleLogDao dao = new ScheduleLogDao(connection);
            return dao.update(scheduleLog);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param scheduleId
     * @param params
     * @return ScheduleLog
     */
    public int update(long scheduleId, Map<String, Object> params) {
        Connection connection = null;

        try {
            connection = ConnectionManager.getConnection();
            ScheduleLogDao dao = new ScheduleLogDao(connection);
            return dao.update(scheduleId, params);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param scheduleId
     * @param page
     * @return ScrollPage<ScheduleLog>
     */
    public ScrollPage<ScheduleLog> getListByScheduleId(long scheduleId, ScrollPage<ScheduleLog> page) {
        Connection connection = null;

        try {
            connection = ConnectionManager.getConnection();
            ScheduleLogDao dao = new ScheduleLogDao(connection);
            return dao.getListByScheduleId(scheduleId, page);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param logId
     * @return int
     */
    public int delete(long logId) {
        Connection connection = null;

        try {
            connection = ConnectionManager.getConnection();
            return new ScheduleLogDao(connection).delete(logId);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }
}
