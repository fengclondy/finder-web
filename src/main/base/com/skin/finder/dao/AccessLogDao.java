/*
 * $RCSfile: AccessLogDao.java,v $$
 * $Revision: 1.1 $
 * $Date: 2014-01-03 $
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
import java.util.List;

import com.skin.finder.model.AccessLog;
import com.skin.j2ee.dao.Dao;
import com.skin.j2ee.dao.Insert;

/**
 * <p>Title: AccessLogDao</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class AccessLogDao extends Dao<AccessLog, Long> {
    /**
     * 
     */
    public AccessLogDao() {
    }

    /**
     * @param connection
     */
    public AccessLogDao(Connection connection) {
        super(connection);
    }

    /**
     * @param logId
     * @return AccessLog
     */
    public AccessLog getById(long logId) {
        String sql = "select * from skin_access_log where log_id=?";
        return this.getBean(sql, new Object[]{logId});
    }

    /**
     * @param accessLog
     * @return int
     */
    public int create(AccessLog accessLog) {
        return new Insert("skin_access_log", accessLog, true).execute(this.getConnection());
    }

    /**
     * @param accessLog
     * @return int
     */
    public int update(AccessLog accessLog) {
        throw new UnsupportedOperationException("update method is unsupported !");
    }

    /**
     * @return int
     */
    public int getCount() {
        return this.getInt("select count(*) as log_count from skin_access_log", new Object[0]);
    }

    /**
     * @param pageNum
     * @param pageSize
     * @return List<AccessLog>
     */
    public List<AccessLog> list(int pageNum, int pageSize) {
        String sql = "select * from skin_access_log order by access_time desc";
        return this.getList(sql, new Object[0], pageNum, pageSize);
    }

    /**
     * @param logId
     * @return int
     */
    public int delete(long logId) {
        throw new UnsupportedOperationException("delete method is unsupported !");
    }

    /**
     * @param resultSet
     * @return AccessLog
     * @throws SQLException
     */
    @Override
    public AccessLog getBean(ResultSet resultSet) throws SQLException {
        AccessLog accessLog = new AccessLog();
        accessLog.setLogId(resultSet.getLong("log_id"));
        accessLog.setAccessTime(resultSet.getTimestamp("access_time"));
        accessLog.setUserId(resultSet.getLong("user_id"));
        accessLog.setUserName(resultSet.getString("user_name"));
        accessLog.setLocalIp(resultSet.getString("local_ip"));
        accessLog.setThreadName(resultSet.getString("thread_name"));
        accessLog.setRemoteHost(resultSet.getString("remote_host"));
        accessLog.setRequestMethod(resultSet.getString("request_method"));
        accessLog.setRequestProtocol(resultSet.getString("request_protocol"));
        accessLog.setRequestUrl(resultSet.getString("request_url"));
        accessLog.setRequestReferer(resultSet.getString("request_referer"));
        accessLog.setClientId(resultSet.getString("client_id"));
        accessLog.setClientUserAgent(resultSet.getString("client_user_Agent"));
        accessLog.setClientCookie(resultSet.getString("client_cookie"));
        return accessLog;
    }
}