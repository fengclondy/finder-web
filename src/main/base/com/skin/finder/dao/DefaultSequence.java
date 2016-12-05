/*
 * $RCSfile: DefaultSequence.java,v $$
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
import java.sql.Statement;

/**
 * <p>Title: DefaultSequence</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class DefaultSequence extends Sequence {
    private long maxId = 0;
    private long nextId = 0;
    private int cacheSize = 1;
    private Object lock = new Object();

    /**
     *
     */
    public DefaultSequence() {
        super();
        this.setTableName("SEQUENCE");
        this.setColumnName("SEQ_NAME");
        this.setColumnValue("SEQ_VALUE");
    }

    /**
     * @param name
     * @return long
     * @throws SQLException
     */
    @Override
    public long next(Connection connection, String name) throws SQLException {
        synchronized(this.lock) {
            if(this.nextId == this.maxId) {
                this.nextId = this.getNextValue(connection, name, this.cacheSize) - this.cacheSize + 1;
                this.maxId = this.nextId + this.cacheSize - 1;
            }
            else {
                this.nextId++;
            }
            return this.nextId;
        }
    }

    /**
     * @param connection
     * @param name
     * @param size
     * @return long
     * @throws SQLException
     */
    public long getNextValue(Connection connection, String name, int size) throws SQLException {
        ResultSet resultSet = null;
        Statement statement = null;
        String query = String.format("SELECT %s FROM %s WHERE %s='%s'", this.getColumnValue(), this.getTableName(), this.getColumnName(), name);
        String update = String.format("UPDATE %s SET %s=${new} WHERE %s='%s' AND %s=${old}", this.getTableName(), this.getColumnValue(), this.getColumnName(), name, this.getColumnValue());

        try {
            int count = 0;
            statement = connection.createStatement();

            while(count++ < 64) {
                long value = 0;

                try {
                    resultSet = statement.executeQuery(query);

                    if(!resultSet.next()) {
                        throw new RuntimeException("getNextValue() failed after executing an update");
                    }

                    value = resultSet.getLong(1);
                }
                finally {
                    if(resultSet != null) {
                        try {
                            resultSet.close();
                        }
                        catch(SQLException e) {
                        }
                    }
                }

                String sql = this.replace(update, "${new}", String.valueOf(value + size));
                sql = this.replace(sql, "${old}", String.valueOf(value));

                int rows = statement.executeUpdate(sql);

                if(rows > 0) {
                    return value + size;
                }
            }

            throw new RuntimeException("getNextValue() failed after executing an update: " + count);
        }
        finally {
            if(statement != null) {
                try {
                    statement.close();
                }
                catch(SQLException e) {
                }
            }
        }
    }

    /**
     * @param source
     * @param search
     * @param replacement
     * @return String
     */
    public String replace(String source, String search, String replacement) {
        if(source == null) {
            return "";
        }

        if(search == null) {
            return source;
        }

        int s = 0;
        int e = 0;
        int d = search.length();
        StringBuilder buffer = new StringBuilder();

        do {
            e = source.indexOf(search, s);

            if(e == -1) {
                buffer.append(source.substring(s));
                break;
            }
            else {
                buffer.append(source.substring(s, e)).append(replacement);
                s = e + d;
            }
        }
        while(true);

        return buffer.toString();
    }
}
