/*
 * $RCSfile: AppConfig.java,v $$
 * $Revision: 1.1 $
 * $Date: 2014-04-10 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.config.Config;
import com.skin.resource.ClassPathResource;
import com.skin.resource.PropertyResource;

/**
 * <p>Title: AppConfig</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class AppConfig extends Config {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);
    private static final AppConfig instance = load("app.properties", "utf-8");

    /**
     * @return AppConfig
     */
    public static AppConfig getInstance() {
        return instance;
    }

    /**
     * @return String
     */
    public static String getName() {
        return instance.get("name");
    }
    
    /**
     * @param resource
     * @param charset
     * @return
     */
    private static AppConfig load(String resource, String charset) {
        InputStream inputStream = null;
        AppConfig appConfig = new AppConfig();

        try {
            inputStream = ClassPathResource.getResourceAsStream(resource);

            if(inputStream != null) {
                Map<String, String> map = PropertyResource.load(inputStream, charset);
                appConfig.putAll(map);
            }
        }
        catch(Exception e) {
            logger.warn(e.getMessage(), e);
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
        return appConfig;
    }
}
