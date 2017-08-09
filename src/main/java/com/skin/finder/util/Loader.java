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
package com.skin.finder.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Properties;

/**
 * <p>Title: Loader</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Loader {
    /**
     * @param name
     * @return InputStream
     */
    public static URL getResource(String name) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource(name);

        if(url != null) {
            return url;
        }
        return Loader.class.getClassLoader().getResource(name);
    }
    
    
    /**
     * @param name
     * @return InputStream
     */
    public static InputStream getInputStream(String name) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(name);

        if(inputStream != null) {
            return inputStream;
        }
        return Loader.class.getClassLoader().getResourceAsStream(name);
    }

    /**
     * @param name
     * @param charset
     * @return Properties
     * @throws IOException 
     */
    public static Properties getProperties(String name, String charset) throws IOException {
        InputStream inputStream = getInputStream(name);

        if(inputStream == null) {
            throw new IOException(name + " not found.");
        }

        try {
            return getProperties(new InputStreamReader(inputStream, charset));
        }
        finally {
            IO.close(inputStream);
        }
    }

    /**
     * @param inputStream
     * @param charset
     * @return Properties
     * @throws IOException 
     */
    public static Properties getProperties(InputStream inputStream, String charset) throws IOException {
        return getProperties(new InputStreamReader(inputStream, charset));
    }

    /**
     * @param reader
     * @return Properties
     * @throws IOException
     */
    public static Properties getProperties(Reader reader) throws IOException {
        Properties properties = new Properties();

        if(reader != null) {
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
        return properties;
    }
}
