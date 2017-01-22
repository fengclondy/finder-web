/*
 * $RCSfile: AccessLogService.java,v $$
 * $Revision: 1.1 $
 * $Date: 2014-01-03 $
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

import com.skin.datasource.ConnectionManager;
import com.skin.finder.dao.AccessLogDao;
import com.skin.finder.model.AccessLog;

/**
 * <p>Title: AccessLogService</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class AccessLogService {
    /**
     *
     */
    public AccessLogService() {
    }

    /**
     * @param logId
     * @return AccessLog
     */
    public AccessLog getById(long logId) {
        Connection connection = null;

        try {
            connection = ConnectionManager.getConnection();
            AccessLogDao dao = new AccessLogDao(connection);
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
     * @param accessLog
     * @return int
     */
    public int create(AccessLog accessLog) {
        Connection connection = null;

        try {
            connection = ConnectionManager.getConnection();
            AccessLogDao dao = new AccessLogDao(connection);
            return dao.create(accessLog);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
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
        Connection connection = null;

        try {
            connection = ConnectionManager.getConnection();
            AccessLogDao dao = new AccessLogDao(connection);
            return dao.getCount();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param pageNum
     * @param pageSize
     * @return List<AccessLog>
     */
    public List<AccessLog> list(int pageNum, int pageSize) {
        Connection connection = null;
        
        try {
            connection = ConnectionManager.getConnection();
            AccessLogDao dao = new AccessLogDao(connection);
            return dao.list(pageNum, pageSize);
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
        throw new UnsupportedOperationException("delete method is unsupported !");
    }
}
