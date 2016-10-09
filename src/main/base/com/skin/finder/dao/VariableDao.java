/*
 * $RCSfile: VariableDao.java,v $$
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
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.skin.finder.model.Variable;
import com.skin.j2ee.dao.DAOException;
import com.skin.j2ee.dao.Dao;
import com.skin.j2ee.dao.Insert;
import com.skin.j2ee.dao.Update;

/**
 * <p>Title: VariableDao</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class VariableDao extends Dao<Variable, String> {
    private String tableName;

    /**
     *
     */
    public VariableDao() {
    }

    /**
     * @param connection
     */
    public VariableDao(Connection connection) {
        super(connection);
    }

    /**
     * @param tableName
     * @param connection
     */
    public VariableDao(String tableName, Connection connection) {
        super(connection);
        this.tableName = tableName;
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @param name
     * @return Variable
     */
    public Variable getById(String name) {
        return this.getBean("select * from " + this.tableName + " where name=?", new Object[]{name});
    }

    /**
     * @param variable
     * @return int
     */
    public int create(Variable variable) {
        if(variable.getName() == null) {
            throw new DAOException("primary key must be not 0");
        }
        Insert insert = new Insert(this.tableName, variable, true);
        return insert.execute(this.getConnection());
    }

    /**
     * @param name
     * @param value
     * @return int
     */
    public int setVariable(String name, String value) {
        String sql = "update " + this.tableName + " set value=? where name=?";
        return this.update(sql, new Object[]{value, name});
    }

    /**
     * @param variable
     * @return int
     */
    public int update(Variable variable) {
        String sql = "update " + this.tableName + " set value=?, description=? where name=?";
        return this.update(sql, new Object[]{variable.getValue(), variable.getDescription(), variable.getName()});
    }

    /**
     * @param name
     * @param params
     * @return int
     */
    public int update(String name, Map<String, Object> params) {
        Update update = new Update(this.tableName, params, false);
        update.where("name", "=", name);
        return update.execute(this.getConnection());
    }

    /**
     * @param name
     * @param value
     * @param description
     * @return int
     */
    public int save(String name, String value, String description) {
        Date updateTime = new Date();
        Update update = new Update(this.tableName, null, false);
        update.set("value", value);
        update.set("updateTime", updateTime);
        update.where("name", "=", name);
        int rows = update.execute(this.getConnection());

        if(rows < 1) {
            Variable variable = new Variable();
            variable.setName(name);
            variable.setValue(value);
            variable.setDescription(description);
            variable.setCreateTime(updateTime);
            variable.setUpdateTime(updateTime);
            return this.create(variable);
        }
        else {
            return rows;
        }
    }

    /**
     * @return int
     */
    public int getCount() {
        return this.getInt("select count(1) as variable_count from " + this.tableName);
    }

    /**
     * @return List<Variable>
     */
    public List<Variable> getList() {
        String sql = "select * from " + this.tableName + " order by name asc";
        return this.getList(sql, new Object[0]);
    }

    /**
     * @param pageNum
     * @param pageSize
     * @return List<Variable>
     */
    public List<Variable> getList(int pageNum, int pageSize) {
        String sql = "select * from " + this.tableName + " order by name asc";
        return this.getList(sql, new Object[0], pageNum, pageSize);
    }

    /**
     * @param name
     * @return int
     */
    public int delete(String name) {
        if(name != null) {
            return this.update("delete from " + this.tableName + " where name=?", name);
        }
        else {
            return 0;
        }
    }

    /**
     * @param resultSet
     * @return Variable
     * @throws SQLException
     */
    @Override
    public Variable getBean(ResultSet resultSet) throws SQLException {
        Variable variable = new Variable();
        variable.setName(resultSet.getString("name"));
        variable.setValue(resultSet.getString("value"));
        variable.setDescription(resultSet.getString("description"));
        variable.setCreateTime(resultSet.getTimestamp("create_time"));
        variable.setUpdateTime(resultSet.getTimestamp("update_time"));
        return variable;
    }
}