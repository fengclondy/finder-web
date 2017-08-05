/*
 * $RCSfile: ConfigFactory.java,v $
 * $Revision: 1.1 $
 * $Date: 2013-10-15 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: ConfigFactory</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ConfigFactory {
    private static final Logger logger = LoggerFactory.getLogger(ConfigFactory.class);
    private static final Config config = ConfigFactory.load();

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
     * @return Config
     */
    public static Config load() {
        InputStream inputStream = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try {
            inputStream = loader.getResourceAsStream("META-INF/conf/finder.conf");
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                }
                catch(IOException e) {
                }
            }
        }
        return new Config(getProperties(inputStream, "utf-8"));
    }

    /**
     * @param inputStream
     * @param charset
     * @return Properties
     */
    public static Properties getProperties(InputStream inputStream, String charset) {
        if(inputStream == null) {
            logger.warn("inputStream is null.");
            return new Properties();
        }

        try {
            return getProperties(new InputStreamReader(inputStream, charset));
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new Properties();
    }

    /**
     * @param reader
     * @return Map<String, String>
     */
    public static Properties getProperties(Reader reader) {
        Properties properties = new Properties();

        if(reader != null) {
            try {
                String line = null;
                BufferedReader buffer = new BufferedReader(reader);

                while((line = buffer.readLine()) != null) {
                    line = line.trim();

                    if(line.length() < 1) {
                        continue;
                    }

                    if(line.startsWith("#")) {
                        continue;
                    }

                    int i = line.indexOf("=");

                    if(i > -1) {
                        String name = line.substring(0, i).trim();
                        String value = line.substring(i + 1).trim();

                        if(name.length() > 0 && value.length() > 0) {
                            properties.setProperty(name, value);
                        }
                    }
                }
            }
            catch(IOException e) {
            }
        }
        return properties;
    }
}
