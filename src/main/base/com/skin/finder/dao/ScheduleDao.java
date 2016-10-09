/*
 * $RCSfile: ScheduleDao.java,v $$
 * $Revision: 1.1  $
 * $Date: 2012-10-31  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.skin.finder.model.Schedule;
import com.skin.j2ee.dao.Dao;
import com.skin.j2ee.dao.Insert;
import com.skin.j2ee.dao.Update;
import com.skin.j2ee.util.ScrollPage;

/**
 * <p>Title: ScheduleDao</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ScheduleDao extends Dao<Schedule, Long> {
    /**
     *
     */
    public ScheduleDao() {
        super();
    }

    /**
     * @param connection
     */
    public ScheduleDao(Connection connection) {
        super(connection);
    }

    /**
     * @param scheduleId
     * @return Schedule
     */
    public Schedule getById(long scheduleId) {
        return this.getBean("select * from skin_schedule where schedule_id=?", new Object[]{scheduleId});
    }

    /**
     * @param schedule
     * @return int
     */
    public int create(Schedule schedule) {
        schedule.setOwner("anyone");
        Insert insert = new Insert("skin_schedule", schedule, true);
        insert.remove("scheduleId");
        return insert.execute(this.getConnection());
    }

    /**
     * @param schedule
     * @return int
     */
    public int update(Schedule schedule) {
        Update update = new Update("skin_schedule", schedule, false);
        update.where("scheduleId", "=", schedule.getScheduleId());
        return update.execute(this.getConnection());
    }

    /**
     * @param scheduleId
     * @param params
     * @return int
     */
    public int update(long scheduleId, Map<String, Object> params) {
        Update update = new Update("skin_schedule", params, true);
        update.where("scheduleId", "=", scheduleId);
        return update.execute(this.getConnection());
    }

    /**
     * @return List<Schedule>
     */
    public List<Schedule> list() {
        String sql = "select * from skin_schedule order by schedule_id asc";
        return this.getList(sql, new Object[0]);
    }

    /**
     * @param page
     * @return ScrollPage<Schedule>
     */
    public ScrollPage<Schedule> getList(ScrollPage<Schedule> page) {
        String sql = "select * from skin_schedule order by schedule_id asc";
        return this.getPage(sql, new Object[0], page);
    }

    /**
     * @param status
     * @return List<Schedule>
     */
    public List<Schedule> getListByStatus(int status) {
        String sql = "select * from skin_schedule where status=? order by schedule_id asc";
        return this.getList(sql, new Object[] {status});
    }

    /**
     * @param scheduleId
     * @param status
     * @return int
     */
    public int setStatus(long scheduleId, int status) {
        return this.update("update skin_schedule set status=? where schedule_id=?", new Object[]{status, scheduleId});
    }

    /**
     * @param scheduleId
     * @param oldOwner
     * @param newOwner
     * @return int
     */
    public boolean lock(long scheduleId, String oldOwner, String newOwner) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        int rows = this.update("update skin_schedule set owner=?, update_time=? where schedule_id=? and owner=?", new Object[]{newOwner, timestamp, scheduleId, oldOwner});
        return (rows > 0);
    }

    /**
     * @param scheduleId
     * @return int
     */
    public int delete(Long scheduleId){
        if(scheduleId != null && scheduleId.longValue() > 0L) {
            return this.update("delete from skin_schedule where schedule_id=?", scheduleId);
        }
        return 0;
    }

    /**
     * @param resultSet
     * @return Schedule
     * @throws SQLException
     */
    @Override
    public Schedule getBean(ResultSet resultSet) throws SQLException {
        Schedule schedule = new Schedule();
        schedule.setScheduleId(resultSet.getLong("schedule_id"));
        schedule.setScheduleName(resultSet.getString("schedule_name"));
        schedule.setDescription(resultSet.getString("description"));
        schedule.setScheduleType(resultSet.getInt("schedule_type"));
        schedule.setStatus(resultSet.getInt("status"));
        schedule.setOwner(resultSet.getString("owner"));
        schedule.setExpression(resultSet.getString("expression"));
        schedule.setAction(resultSet.getString("action"));
        schedule.setProperties(resultSet.getString("properties"));
        schedule.setLastFireTime(resultSet.getTimestamp("last_fire_time"));
        schedule.setNextFireTime(resultSet.getTimestamp("next_fire_time"));
        schedule.setExecuteStatus(resultSet.getInt("execute_status"));
        schedule.setExecuteResult(resultSet.getString("execute_result"));
        schedule.setCreateTime(resultSet.getTimestamp("create_time"));
        schedule.setUpdateTime(resultSet.getTimestamp("update_time"));
        return schedule;
    }
}
