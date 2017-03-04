/*
 * $RCSfile: MailLogDao.java,v $$
 * $Revision: 1.1 $
 * $Date: 2015-02-06 17:29:27 $
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

import com.skin.finder.model.MailLog;
import com.skin.j2ee.dao.DAOException;
import com.skin.j2ee.dao.Dao;
import com.skin.j2ee.dao.Insert;
import com.skin.j2ee.dao.Update;
import com.skin.j2ee.util.ScrollPage;

/**
 * <p>Title: MailLogDao</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class MailLogDao extends Dao<MailLog, Long> {
    /**
     * default
     */
    public MailLogDao() {
    }

    /**
     * @param connection
     */
    public MailLogDao(Connection connection) {
        super(connection);
    }

    /**
     * @param appId
     * @param logId
     * @return MailLog
     */
    public MailLog getById(long appId, long logId) {
        String sql = "select * from skin_mail_log where log_id=? and app_id=?";
        return this.getBean(sql, new Object[]{logId, appId});
    }

    /**
     * @param mailLog
     * @return int
     */
    public int insert(MailLog mailLog) {
        try {
            Insert insert = new Insert("skin_mail_log", mailLog, true);
            insert.remove("logId");
            return insert.execute(this.getConnection());
        }
        catch (Exception e) {
            throw new DAOException(e);
        }
    }

    /**
     * @param mailLog
     * @return int
     */
    public int update(MailLog mailLog) {
        try {
            Update update = new Update("skin_mail_log", mailLog, false);
            update.where("logId", "=", mailLog.getLogId());
            return update.execute(this.getConnection());
        }
        catch(Exception e) {
            throw new DAOException(e);
        }
    }

    /**
     * @param appId
     * @param page
     * @return ScrollPage<MailLog>
     */
    public ScrollPage<MailLog> getList(long appId, ScrollPage<MailLog> page) {
        String sql = "select * from skin_mail_log where app_id=? order send_time desc";
        return this.getPage(sql, new Object[]{appId}, page);
    }

    /**
     * @param appId
     * @param userId
     * @param page
     * @return ScrollPage<MailLog>
     */
    public ScrollPage<MailLog> getListByUserId(long appId, long userId, ScrollPage<MailLog> page) {
        String sql = "select * from skin_mail_log where app_id=? and user_id=?";
        return this.getPage(sql, new Object[]{appId, userId}, page);
    }

    /**
     * @param appId
     * @param logId
     * @return int
     */
    public int delete(long appId, long logId) {
        if(logId > 0L) {
            return this.update("delete from skin_mail_log where app_id=? and log_id=?", appId, logId);
        }
        return 0;
    }

    /**
     * @param resultSet
     * @return MailLog
     * @throws SQLException 
     */
    @Override
    public MailLog getBean(ResultSet resultSet) throws SQLException {
        MailLog mailLog = new MailLog();
        mailLog.setLogId(resultSet.getLong("log_id"));
        mailLog.setAppId(resultSet.getLong("app_id"));
        mailLog.setUserId(resultSet.getLong("user_id"));
        mailLog.setCustomerId(resultSet.getLong("customer_id"));
        mailLog.setBizId(resultSet.getLong("biz_id"));
        mailLog.setBizType(resultSet.getString("biz_type"));
        mailLog.setMailType(resultSet.getString("mail_type"));
        mailLog.setSender(resultSet.getString("sender"));
        mailLog.setReceiver(resultSet.getString("receiver"));
        mailLog.setSubject(resultSet.getString("subject"));
        mailLog.setContent(resultSet.getString("content"));
        mailLog.setSendStatus(resultSet.getInt("send_status"));
        mailLog.setSendTime(resultSet.getTimestamp("send_time"));
        return mailLog;
    }

}