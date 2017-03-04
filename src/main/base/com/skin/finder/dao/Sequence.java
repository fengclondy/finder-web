/*
 * $RCSfile: Sequence.java,v $$
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
import java.sql.SQLException;

/**
 * <p>Title: Sequence</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public abstract class Sequence {
    private String tableName;
    private String columnName;
    private String columnValue;

    /**
     *
     */
    public Sequence() {
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * @param columnName the columnName to set
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * @return the columnName
     */
    public String getColumnName() {
        return this.columnName;
    }

    /**
     * @param columnValue the columnValue to set
     */
    public void setColumnValue(String columnValue) {
        this.columnValue = columnValue;
    }

    /**
     * @return the columnValue
     */
    public String getColumnValue() {
        return this.columnValue;
    }

    /**
     * @param name
     * @return long
     */
    protected abstract long next(Connection connection, String name) throws SQLException;
}
