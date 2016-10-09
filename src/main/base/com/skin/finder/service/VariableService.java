/*
 * $RCSfile: VariableService.java,v $$
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
import java.util.Map;

import com.skin.datasource.ConnectionManager;
import com.skin.finder.dao.VariableDao;
import com.skin.finder.model.Variable;

/**
 * <p>Title: VariableService</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class VariableService {
    private String tableName;

    /**
     * @param tableName 
     */
    public VariableService(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @param id
     * @return Variable
     */
    public Variable getById(String id) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            VariableDao variableDao = new VariableDao(this.tableName, connection);
            return variableDao.getById(id);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param variable
     * @return int
     */
    public int create(Variable variable) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            VariableDao variableDao = new VariableDao(this.tableName, connection);
            return variableDao.create(variable);
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
            VariableDao variableDao = new VariableDao(this.tableName, connection);
            return variableDao.setVariable(name, value);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param variable
     * @return int
     */
    public int update(Variable variable) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            VariableDao variableDao = new VariableDao(this.tableName, connection);
            return variableDao.update(variable);
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
     * @param params
     * @return int
     */
    public int update(String name, Map<String, Object> params) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            VariableDao variableDao = new VariableDao(this.tableName, connection);
            return variableDao.update(name, params);
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
     * @param description
     * @return int
     */
    public int save(String name, String value, String description) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            VariableDao variableDao = new VariableDao(this.tableName, connection);
            return variableDao.save(name, value, description);
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
    public int getCount() {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            VariableDao variableDao = new VariableDao(this.tableName, connection);
            return variableDao.getCount();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @return List<Variable>
     */
    public List<Variable> getList() {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            VariableDao variableDao = new VariableDao(this.tableName, connection);
            return variableDao.getList();
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
     * @return List<Variable>
     */
    public List<Variable> getList(int pageNum, int pageSize) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            VariableDao variableDao = new VariableDao(this.tableName, connection);
            return variableDao.getList(pageNum, pageSize);
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
            VariableDao variableDao = new VariableDao(this.tableName, connection);
            return variableDao.delete(id);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }
}