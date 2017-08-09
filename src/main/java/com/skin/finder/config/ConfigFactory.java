/*
 * $RCSfile: ConfigFactory.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.util.IO;
import com.skin.finder.util.Loader;

/**
 * <p>Title: ConfigFactory</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ConfigFactory {
    private static final Logger logger = LoggerFactory.getLogger(ConfigFactory.class);
    private static final String APP_NAME = App.getName();
    private static final String APP_CONF = App.getString("finder.conf", "META-INF/conf/");
    private static final Config config = ConfigFactory.load("finder.conf", "utf-8");

    /**
     * @return String
     */
    public static String getMaster() {
        return ConfigFactory.getString(Constants.CLUSTER_MASTER_NAME);
    }

    /**
     * @return String
     */
    public static String getHostName() {
        return ConfigFactory.getString(Constants.CLUSTER_NODE_NAME);
    }

    /**
     * @return String
     */
    public static String getSecurityKey() {
        return ConfigFactory.getString(Constants.CLUSTER_SECURITY_KEY);
    }

    /**
     * @param name
     * @param value
     */
    public static void setValue(String name, String value) {
        config.setValue(name, value);
    }

    /**
     * @param name
     * @return String
     */
    public static String getValue(String name) {
        return config.getValue(name);
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public static String getValue(String name, String defaultValue) {
        return config.getValue(name, defaultValue);
    }

    /**
     * @param name
     * @return String
     */
    public static String getString(String name) {
        return config.getString(name);
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public static String getString(String name, String defaultValue) {
        return config.getString(name, defaultValue);
    }

    /**
     * @param name
     * @return String
     */
    public static Character getCharacter(String name) {
        return config.getCharacter(name);
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public static Character getCharacter(String name, Character defaultValue) {
        return config.getCharacter(name, defaultValue);
    }

    /**
     * @param name
     * @return String
     */
    public static Boolean getBoolean(String name) {
        return config.getBoolean(name);
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public static Boolean getBoolean(String name, Boolean defaultValue) {
        return config.getBoolean(name, defaultValue);
    }

    /**
     * @param name
     * @return String
     */
    public static Byte getByte(String name) {
        return config.getByte(name);
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public static Byte getByte(String name, Byte defaultValue) {
        return config.getByte(name, defaultValue);
    }

    /**
     * @param name
     * @return String
     */
    public static Short getShort(String name) {
        return config.getShort(name);
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public static Short getShort(String name, Short defaultValue) {
        return config.getShort(name, defaultValue);
    }

    /**
     * @param name
     * @return String
     */
    public static Integer getInteger(String name) {
        return config.getInteger(name);
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public static Integer getInteger(String name, Integer defaultValue) {
        return config.getInteger(name, defaultValue);
    }

    /**
     * @param name
     * @return String
     */
    public static Float getFloat(String name) {
        return config.getFloat(name);
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public static Float getFloat(String name, Float defaultValue) {
        return config.getFloat(name, defaultValue);
    }

    /**
     * @param name
     * @return String
     */
    public static Double getDouble(String name) {
        return config.getDouble(name);
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public static Double getDouble(String name, Double defaultValue) {
        return config.getDouble(name, defaultValue);
    }

    /**
     * @param name
     * @return String
     */
    public static Long getLong(String name) {
        return config.getLong(name);
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public static Long getLong(String name, Long defaultValue) {
        return config.getLong(name, defaultValue);
    }

    /**
     * @param name
     * @param pattern
     * @return String
     */
    public static Date getDate(String name, String pattern) {
        return config.getDate(name, pattern);
    }

    /**
     * @param name
     * @param type
     * @return String
     */
    public static <T> T getObject(String name, Class<T> type) {
        return config.getObject(name, type);
    }

    /**
     * @param name
     * @return String
     */
    public static boolean has(String name) {
        return config.has(name);
    }

    /**
     * @param name
     * @param value
     * @return String
     */
    public static boolean contains(String name, String value) {
        return config.contains(name, value);
    }

    /**
     * @param config
     */
    public static void extend(Config config) {
        config.extend(config);
    }

    /**
     * @param config
     */
    public static void copy(Config config) {
        config.copy(config);
    }

    /**
     * @param map
     */
    public static void setConfig(Map<?, ?> map) {
        if(map == null) {
            return;
        }

        for(Map.Entry<?, ?> entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            if(key == null) {
                continue;
            }

            if(value == null) {
                setValue(key.toString(), null);
            }
            else {
                setValue(key.toString(), value.toString());
            }
        }
    }

    /**
     * @param name
     * @param charset
     * @return Config
     */
    public static Config load(String name, String charset) {
        Properties properties = getProperties(name, charset);
        return new Config(properties);
    }

    /**
     * @param name
     * @param charset
     * @return Properties
     */
    public static Properties getProperties(String name, String charset) {
        InputStream inputStream = null;

        try {
            inputStream = ConfigFactory.getInputStream(name);

            if(inputStream != null) {
                return Loader.getProperties(new InputStreamReader(inputStream, charset));
            }
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        finally {
            IO.close(inputStream);
        }
        return new Properties();
    }

    /**
     * @param name
     * @return InputStream
     */
    public static InputStream getInputStream(String name) {
        String conf = APP_CONF;

        if(conf == null || (conf = conf.trim()).length() < 1) {
            conf = "META-INF/conf/";
        }

        String location = conf + name;
        File file = ConfigFactory.getFile(location);
        logger.info("try load from file: {}", file.getAbsolutePath());

        if(file.exists() && file.isFile()) {
            try {
                return new FileInputStream(file);
            }
            catch(IOException e) {
                logger.error(e.getMessage(), e);
            }
        }

        logger.info("try load from classpath: {}", location);
        return Loader.getInputStream(location);
    }

    /**
     * @param name
     * @return URL
     * @throws IOException
     */
    public static URL getResource(String name) throws IOException {
        String conf = APP_CONF;

        if(conf == null || (conf = conf.trim()).length() < 1) {
            conf = "META-INF/conf/";
        }

        String location = conf + name;
        File file = ConfigFactory.getFile(location);
        logger.info("try load from file: {}", file.getAbsolutePath());

        if(file.exists()) {
            return file.toURI().toURL();
        }
        return Loader.getResource(location);
    }

    /**
     * @param resource
     * @return File
     */
    private static File getFile(String resource) {
        String userHome = System.getProperty("user.home");

        if(resource.startsWith("/") || resource.startsWith("\\")) {
            return new File(userHome, "skinx/" + APP_NAME + resource);
        }
        else {
            return new File(userHome, "skinx/" + APP_NAME + "/" + resource);
        }
    }
}
