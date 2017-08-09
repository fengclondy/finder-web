/*
 * $RCSfile: App.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.config;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.util.Loader;

/**
 * <p>Title: App</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final Properties properties = load();

    /**
     * @return String
     */
    public static String getName() {
        return properties.getProperty("name");
    }

    /**
     * @param name
     * @return String
     */
    public static String getString(String name) {
        return properties.getProperty(name);
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public static String getString(String name, String defaultValue) {
        String value = properties.getProperty(name);

        if(value != null && (value = value.trim()).length() > 0) {
            return value;
        }
        return defaultValue;
    }

    /**
     * @return Properties
     */
    private static Properties load() {
        try {
            return Loader.getProperties("app.properties", "utf-8");
        }
        catch(IOException e) {
            logger.warn("load app config failed. use default app name: finder.");
        }

        Properties properties = new Properties();
        properties.setProperty("name", "finder");
        return properties;
    }
}
