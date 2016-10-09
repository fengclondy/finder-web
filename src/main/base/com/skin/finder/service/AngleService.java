/*
 * $RCSfile: AngleService.java,v $$
 * $Revision: 1.1 $
 * $Date: 2014-11-15 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.service;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import com.skin.config.Config;
import com.skin.datasource.ConnectionManager;
import com.skin.finder.dao.AngleDao;
import com.skin.finder.model.Angle;

/**
 * <p>Title: AngleService</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class AngleService {

    /**
     * @param arg0
     * @return Angle
     */
    public Angle getById(long arg0) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            AngleDao angleDao = new AngleDao(connection);
            return angleDao.getById(arg0);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param appId
     * @param angleName
     * @return Angle
     * @throws Exception 
     */
    public static Angle getByName(long appId, String angleName) throws Exception {
        Connection connection = null;

        try {
            connection = ConnectionManager.getConnection();
            AngleDao angleDao = new AngleDao(connection);
            return angleDao.getByName(appId, angleName);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param appId
     * @param angleName
     * @return Properties
     * @throws Exception
     */
    public static Properties getProperties(long appId, String angleName) throws Exception {
        Properties properties = new Properties();
        Angle angle = getByName(appId, angleName);

        if(angle != null && angle.getContent() != null) {
            properties.load(new StringReader(angle.getContent()));
            Set<Entry<Object, Object>> set = properties.entrySet();

            for(Entry<Object, Object> entry : set) {
                String key = (String)(entry.getKey());
                String value = (String)(entry.getValue());
                properties.setProperty(key, URLDecoder.decode(value, "UTF-8"));
            }
        }
        return properties;
    }

    /**
     * @param appId
     * @param angleName
     * @return Config
     * @throws Exception
     */
    public static Config getConfig(long appId, String angleName) throws Exception {
        Config config = new PropertiesConfig();
        Properties properties = getProperties(appId, angleName);
        Set<Entry<Object, Object>> set = properties.entrySet();

        for(Entry<Object, Object> entry : set) {
            String key = (String)(entry.getKey());
            String value = (String)(entry.getValue());
            config.put(key, value);
        }
        return config;
    }

    /**
     * @param appId
     * @param angleName
     * @param map
     * @throws Exception
     */
    public static void save(long appId, String angleName, Map<String, String> map) throws Exception {
        Connection connection = null;

        try {
            Date updateTime = new Date();
            String content = getContent(map);
            connection = ConnectionManager.getConnection();
            AngleDao angleDao = new AngleDao(connection);
            Angle angle = angleDao.getByName(appId, angleName);

            if(angle != null) {
                angleDao.update(angle.getAngleId(), content, updateTime);
            }
            else {
                angle = new Angle();
                angle.setAppId(appId);
                angle.setAngleName(angleName);
                angle.setCatalog("default");
                angle.setContent(content);
                angle.setCreateTime(updateTime);
                angle.setUpdateTime(updateTime);
                angleDao.insert(angle);
            }
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * @param map
     * @return String
     */
    public static String getContent(Map<String, String> map) {
        StringBuilder buffer = new StringBuilder();

        for(Map.Entry<String, String> entry : map.entrySet()) {
            buffer.append(entry.getKey());
            buffer.append("=");

            try {
                buffer.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            buffer.append("\r\n");
        }
        return buffer.toString();
    }

    /**
     * @param arg0
     * @return int
     */
    public int insert(Angle arg0) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            AngleDao angleDao = new AngleDao(connection);
            return angleDao.insert(arg0);
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
    public int delete(long arg0) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            AngleDao angleDao = new AngleDao(connection);
            return angleDao.delete(arg0);
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
     * @param arg2
     * @return int
     */
    public int update(long arg0, String arg1, Date arg2) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            AngleDao angleDao = new AngleDao(connection);
            return angleDao.update(arg0, arg1, arg2);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * <p>Title: PropertiesConfig</p>
     * <p>Description: </p>
     * <p>Copyright: Copyright (c) 2006</p>
     * @author xuesong.net
     * @version 1.0
     */
    public static class PropertiesConfig extends Config {
        private static final long serialVersionUID = 1L;
    }
}
