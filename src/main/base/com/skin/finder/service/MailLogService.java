/*
 * $RCSfile: MailLogService.java,v $$
 * $Revision: 1.1 $
 * $Date: 2014-11-15 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.skin.datasource.ConnectionManager;
import com.skin.finder.dao.MailLogDao;
import com.skin.finder.model.MailLog;
import com.skin.j2ee.util.ScrollPage;

/**
 * <p>Title: MailLogService</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class MailLogService {

    /**
     * @param arg0
     * @param arg1
     * @return int
     */
    public int delete(long arg0, long arg1) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            MailLogDao mailLogDao = new MailLogDao(connection);
            return mailLogDao.delete(arg0, arg1);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param arg0
     * @return int
     */
    public int insert(MailLog arg0) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            MailLogDao mailLogDao = new MailLogDao(connection);
            return mailLogDao.insert(arg0);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param arg0
     * @return int
     */
    public int update(MailLog arg0) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            MailLogDao mailLogDao = new MailLogDao(connection);
            return mailLogDao.update(arg0);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param arg0
     * @param arg1
     * @return MailLog
     */
    public MailLog getById(long arg0, long arg1) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            MailLogDao mailLogDao = new MailLogDao(connection);
            return mailLogDao.getById(arg0, arg1);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param arg0
     * @param page
     * @return ScrollPage<MailLog>
     */
    public ScrollPage<MailLog> getList(long arg0, ScrollPage<MailLog> page) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            MailLogDao mailLogDao = new MailLogDao(connection);
            return mailLogDao.getList(arg0, page);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param arg0
     * @param arg1
     * @param page
     * @return ScrollPage<MailLog>
     */
    public ScrollPage<MailLog> getListByUserId(long arg0, long arg1, ScrollPage<MailLog> page) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            MailLogDao mailLogDao = new MailLogDao(connection);
            return mailLogDao.getListByUserId(arg0, arg1, page);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }
}
