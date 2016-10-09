/*
 * $RCSfile: ScheduleLogDao.java,v $$
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
import java.util.Map;

import com.skin.finder.model.ScheduleLog;
import com.skin.j2ee.dao.Dao;
import com.skin.j2ee.dao.Insert;
import com.skin.j2ee.dao.Update;
import com.skin.j2ee.util.ScrollPage;

/**
 * <p>Title: ScheduleLogDao</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ScheduleLogDao extends Dao<ScheduleLog, Long> {
    /**
     *
     */
    public ScheduleLogDao() {
        super();
    }

    /**
     * @param connection
     */
    public ScheduleLogDao(Connection connection) {
        super(connection);
    }

    /**
     * @param logId
     * @return ScheduleLog
     */
    public ScheduleLog getById(long logId) {
        return this.getBean("select * from skin_schedule_log where schedule_id=?", new Object[]{logId});
    }

    /**
     * @param scheduleLog
     * @return int
     */
    public int create(ScheduleLog scheduleLog) {
        Insert insert = new Insert("skin_schedule_log", scheduleLog, true);
        insert.remove("logId");
        return insert.execute(this.getConnection());
    }

    /**
     * @param scheduleLog
     * @return int
     */
    public int update(ScheduleLog scheduleLog) {
        Update update = new Update("skin_schedule_log", scheduleLog, false);
        update.where("logId", "=", scheduleLog.getLogId());
        return update.execute(this.getConnection());
    }

    /**
     * @param scheduleId
     * @param params
     * @return int
     */
    public int update(long scheduleId, Map<String, Object> params) {
        Update update = new Update("skin_schedule_log", params, true);
        update.where("scheduleId", "=", scheduleId);
        return update.execute(this.getConnection());
    }

    /**
     * @param scheduleId
     * @param page
     * @return ScrollPage<ScheduleLog>
     */
    public ScrollPage<ScheduleLog> getListByScheduleId(long scheduleId, ScrollPage<ScheduleLog> page) {
        String sql = "select * from skin_schedule_log where schedule_id=? order by fire_time desc";
        return this.getPage(sql, new Object[]{scheduleId}, page);
    }

    /**
     * @param logId
     * @return int
     */
    public int delete(Long logId){
        if(logId != null && logId.longValue() > 0L) {
            return this.update("delete from skin_schedule_log where log_id=?", logId);
        }
        return 0;
    }

    /**
     * @param resultSet
     * @return ScheduleLog
     * @throws SQLException
     */
    @Override
    public ScheduleLog getBean(ResultSet resultSet) throws SQLException {
        ScheduleLog scheduleLog = new ScheduleLog();
        scheduleLog.setLogId(resultSet.getLong("log_id"));
        scheduleLog.setScheduleId(resultSet.getLong("schedule_id"));
        scheduleLog.setScheduleName(resultSet.getString("schedule_name"));
        scheduleLog.setScheduleType(resultSet.getInt("schedule_type"));
        scheduleLog.setInvocation(resultSet.getString("invocation"));
        scheduleLog.setFireTime(resultSet.getTimestamp("fire_time"));
        scheduleLog.setNextFireTime(resultSet.getTimestamp("next_fire_time"));
        scheduleLog.setExecuteStatus(resultSet.getInt("execute_status"));
        scheduleLog.setExecuteResult(resultSet.getString("execute_result"));
        return scheduleLog;
    }
}
