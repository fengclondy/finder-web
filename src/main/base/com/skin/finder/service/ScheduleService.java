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
import java.util.List;
import java.util.Map;

import com.skin.datasource.ConnectionManager;
import com.skin.finder.dao.ScheduleDao;
import com.skin.finder.model.Schedule;
import com.skin.j2ee.util.ScrollPage;

/**
 * <p>Title: ScheduleService</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ScheduleService {
    /**
     * @param scheduleId
     * @return Schedule
     */
    public Schedule getById(long scheduleId) {
        Connection connection = null;

        try {
            connection = ConnectionManager.getConnection();
            ScheduleDao dao = new ScheduleDao(connection);
            return dao.getById(scheduleId);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param schedule
     * @return int
     */
    public int create(Schedule schedule) {
        Connection connection = null;

        try {
            connection = ConnectionManager.getConnection();
            ScheduleDao dao = new ScheduleDao(connection);
            return dao.create(schedule);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param schedule
     * @return Schedule
     */
    public int update(Schedule schedule) {
        Connection connection = null;

        try {
            connection = ConnectionManager.getConnection();
            ScheduleDao dao = new ScheduleDao(connection);
            return dao.update(schedule);
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
     * @return int
     */
    public int update(long scheduleId, Map<String, Object> params) {
        Connection connection = null;

        try {
            connection = ConnectionManager.getConnection();
            ScheduleDao dao = new ScheduleDao(connection);
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
     * @return List<Schedule>
     */
    public List<Schedule> list() {
        Connection connection = null;

        try {
            connection = ConnectionManager.getConnection();
            return new ScheduleDao(connection).list();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param page
     * @return ScrollPage<Schedule>
     */
    public ScrollPage<Schedule> getList(ScrollPage<Schedule> page) {
        Connection connection = null;

        try {
            connection = ConnectionManager.getConnection();
            return new ScheduleDao(connection).getList(page);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param status
     * @return List<Schedule>
     */
    public List<Schedule> getListByStatus(int status) {
        if(!ConnectionManager.available()) {
            return null;
        }

        Connection connection = null;

        try {
            connection = ConnectionManager.getConnection();
            return new ScheduleDao(connection).getListByStatus(status);
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
     * @param status
     * @return int
     */
    public int setStatus(long scheduleId, int status) {
        Connection connection = null;

        try {
            connection = ConnectionManager.getConnection();
            return new ScheduleDao(connection).setStatus(scheduleId, status);
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
     * @param oldOwner
     * @param newOwner
     * @return int
     */
    public boolean lock(long scheduleId, String oldOwner, String newOwner) {
        Connection connection = null;

        try {
            connection = ConnectionManager.getConnection();
            return new ScheduleDao(connection).lock(scheduleId, oldOwner, newOwner);
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
     * @return int
     */
    public int delete(long scheduleId) {
        Connection connection = null;

        try {
            connection = ConnectionManager.getConnection();
            return new ScheduleDao(connection).delete(scheduleId);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }
}
