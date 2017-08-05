/*
 * $RCSfile: BundleManager.java,v $
 * $Revision: 1.1 $
 * $Date: 2013-02-26 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.i18n;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: PropertyResourceBundle</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class PropertyResourceBundle extends ResourceBundle {
    private Map<String, Object> lookup;
    private static final Logger logger = LoggerFactory.getLogger(PropertyResourceBundle.class);

    /**
     * @param stream
     * @throws IOException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public PropertyResourceBundle(InputStream stream) throws IOException {
        Properties properties = getProperties(stream, "utf-8");
        this.lookup = new HashMap(properties);
    }

    /**
     * @param reader
     * @throws IOException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public PropertyResourceBundle(Reader reader) throws IOException {
        Properties properties = getProperties(reader);
        this.lookup = new HashMap(properties);
    }

    /**
     * @param key
     */
    @Override
    public Object handleGetObject(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return this.lookup.get(key);
    }

    /**
     * Returns an <code>Enumeration</code> of the keys contained in
     * this <code>ResourceBundle</code> and its parent bundles.
     *
     * @return an <code>Enumeration</code> of the keys contained in
     *         this <code>ResourceBundle</code> and its parent bundles.
     * @see #keySet()
     */
    @Override
    public Enumeration<String> getKeys() {
        ResourceBundle parent = this.parent;
        return new ResourceBundleEnumeration(this.lookup.keySet(), (parent != null) ? parent.getKeys() : null);
    }

    /**
     * Returns a <code>Set</code> of the keys contained
     * <em>only</em> in this <code>ResourceBundle</code>.
     *
     * @return a <code>Set</code> of the keys contained only in this
     *         <code>ResourceBundle</code>
     * @since 1.6
     * @see #keySet()
     */
    @Override
    protected Set<String> handleKeySet() {
        return this.lookup.keySet();
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