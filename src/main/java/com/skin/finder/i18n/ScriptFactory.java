/*
 * $RCSfile: ScriptFactory.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.i18n;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.config.ConfigFactory;
import com.skin.finder.util.StringUtil;

/**
 * <p>Title: ScriptFactory</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author Admin
 * @version 1.0
 */
public class ScriptFactory {
    private static final Logger logger = LoggerFactory.getLogger(ScriptFactory.class);

    /**
     * 
     */
    public static void main(String[] args) {
        try {
            URL url = ConfigFactory.getResource("lang");

            if("file".equals(url.getProtocol())) {
                File dir = new File(url.getFile());
                logger.debug("build: {}", dir.getAbsolutePath());

                if(dir.isDirectory()) {
                    File[] list = dir.listFiles();

                    for(File file : list) {
                        logger.debug("build: {}", file.getAbsolutePath());
                        Properties properties = ConfigFactory.getProperties("lang/" + file.getName(), "utf-8");
                        String script = generate(properties);
                        System.out.println(script);
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     */
    public static void generate() {
        Properties properties = ConfigFactory.getProperties("lang/en.properties", "utf-8");
        String script = generate(properties);
        System.out.println(script);
    }

    /**
     * @param properties
     * @param path
     * @return String
     */
    public static String generate(Properties properties) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("(function() {\r\n");
        buffer.append("if(typeof(I18N) == \"undefined\") {\r\n");
        buffer.append("    return;\r\n");
        buffer.append("}\r\n");
        buffer.append("I18N.bundle = {\r\n");

        for(Map.Entry<?, ?> entry : properties.entrySet()) {
            String name = entry.getKey().toString();
            String value = entry.getValue().toString();

            buffer.append("\"");
            buffer.append(name);
            buffer.append("\": \"");
            buffer.append(StringUtil.escape(value));
            buffer.append("\",\r\n");
        }

        buffer.append("\"finder.lang.end\": \"ok\"\r\n};\r\n");
        buffer.append("})();\r\n");
        return buffer.toString();
    }
}
