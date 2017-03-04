/*
 * $RCSfile: SystemVariableDao.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-05-06 14:05:24 234 $
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

import com.skin.finder.model.SystemVariable;
import com.skin.j2ee.dao.DAOException;
import com.skin.j2ee.dao.Dao;
import com.skin.j2ee.dao.Insert;

/**
 * <p>Title: SystemVariableDao</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class SystemVariableDao extends Dao<SystemVariable, String> {
    /**
     *
     */
    public SystemVariableDao() {
    }

    /**
     * @param connection
     */
    public SystemVariableDao(Connection connection) {
        super(connection);
    }

    /**
     * @param name
     * @return SystemVariable
     */
    public SystemVariable getById(String name) {
        return this.getBean("select * from system_variable where variable_name=?", new Object[]{name});
    }

    /**
     * @param systemVariable
     * @return int
     */
    public int create(SystemVariable systemVariable) {
        if(systemVariable.getVariableName() == null) {
            throw new DAOException("primary key must be not 0");
        }
        Insert insert = new Insert("system_variable", systemVariable, true);
        return insert.execute(this.getConnection());
    }

    /**
     * @param name
     * @param value
     * @return int
     */
    public int setVariable(String name, String value) {
        String sql = "update system_variable set variable_value=? where variable_name=?";
        return this.update(sql, new Object[]{value, name});
    }

    /**
     * @param systemVariable
     * @return int
     */
    public int update(SystemVariable systemVariable) {
        String sql = "update system_variable set variable_value=?, variable_desc=? where variable_name=?";
        return this.update(sql, new Object[]{systemVariable.getVariableValue(), systemVariable.getVariableDesc(), systemVariable.getVariableName()});
    }

    /**
     * @return int
     */
    public int getVariableCount() {
        return this.getInt("select count(1) as variable_count from system_variable");
    }

    /**
     * @param pageNum
     * @param pageSize
     * @return List<SystemVariable>
     */
    public List<SystemVariable> getVariableList(int pageNum, int pageSize) {
        String sql = "select * from system_variable order by variable_name asc";
        return this.getList(sql, new Object[0], pageNum, pageSize);
    }

    /**
     * @param name
     * @return int
     */
    public int delete(String name) {
        if(name != null) {
            return this.update("delete from system_variable where variable_name=?", name);
        }
        return 0;
    }

    /**
     * @param resultSet
     * @return SystemVariable
     * @throws SQLException
     */
    @Override
    public SystemVariable getBean(ResultSet resultSet) throws SQLException {
        SystemVariable systemVariable = new SystemVariable();
        systemVariable.setVariableName(resultSet.getString("variable_name"));
        systemVariable.setVariableValue(resultSet.getString("variable_value"));
        systemVariable.setVariableDesc(resultSet.getString("variable_desc"));
        return systemVariable;
    }
}