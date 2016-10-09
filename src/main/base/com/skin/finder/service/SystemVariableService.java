/*
 * $RCSfile: SystemVariableService.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-05-06 $
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
import com.skin.finder.dao.SystemVariableDao;
import com.skin.finder.model.SystemVariable;

/**
 * <p>Title: SystemVariableService</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class SystemVariableService {
    /**
     *
     */
    public SystemVariableService() {
    }

    /**
     * @param id
     * @return SystemVariable
     */
    public SystemVariable getById(String id) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            SystemVariableDao systemVariableDao = new SystemVariableDao(connection);
            return systemVariableDao.getById(id);
        }
        catch(SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param systemVariable
     * @return int
     */
    public int create(SystemVariable systemVariable) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            SystemVariableDao systemVariableDao = new SystemVariableDao(connection);
            return systemVariableDao.create(systemVariable);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param name
     * @param value
     * @return int
     */
    public int setVariable(String name, String value) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            SystemVariableDao systemVariableDao = new SystemVariableDao(connection);
            return systemVariableDao.setVariable(name, value);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param systemVariable
     * @return int
     */
    public int update(SystemVariable systemVariable) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            SystemVariableDao systemVariableDao = new SystemVariableDao(connection);
            return systemVariableDao.update(systemVariable);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @return int
     */
    public int getVariableCount() {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            SystemVariableDao systemVariableDao = new SystemVariableDao(connection);
            return systemVariableDao.getVariableCount();
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
     * @return List<SystemVariable>
     */
    public List<SystemVariable> getVariableList(int pageNum, int pageSize) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            SystemVariableDao systemVariableDao = new SystemVariableDao(connection);
            return systemVariableDao.getVariableList(pageNum, pageSize);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param id
     * @return int
     */
    public int delete(String id) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            SystemVariableDao systemVariableDao = new SystemVariableDao(connection);
            return systemVariableDao.delete(id);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }
}