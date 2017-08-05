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

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: BundleManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class BundleManager {
    private ConcurrentHashMap<String, FutureTask<LocalizationContext>> cache;
    private static final String BASEDIR = "META-INF/conf/lang/";
    private static final BundleManager instance = new BundleManager();
    private static final Logger logger = LoggerFactory.getLogger(BundleManager.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
        test("", new Locale("zh", "cn", ""));
        test("", Locale.ENGLISH);
        test("", Locale.SIMPLIFIED_CHINESE);
        test("", Locale.TAIWAN);
    }

    /**
     * @param baseName
     * @param locale
     */
    public static void test(String baseName, Locale locale) {
        BundleManager bundleManager = BundleManager.getInstance();
        LocalizationContext localizationContext = bundleManager.getBundle(baseName, locale);
        ResourceBundle resourceBundle = localizationContext.getResourceBundle();
        System.out.println(localizationContext.format("finder.list.open"));
        System.out.println(resourceBundle.getString("finder.list.open"));
    }

    /**
     * default
     */
    private BundleManager() {
        this.cache = new ConcurrentHashMap<String, FutureTask<LocalizationContext>>();
    }

    /**
     * @return BundleManager
     */
    public static BundleManager getInstance() {
        return instance;
    }

    /**
     * @param baseName
     * @param locale
     * @return LocalizationContext
     */
    public LocalizationContext getBundle(final String baseName, final Locale locale) {
        String language = locale.getLanguage();
        String country = locale.getCountry();
        String variant = locale.getVariant();
        StringBuilder buffer = new StringBuilder();

        if(baseName != null && baseName.length() > 0) {
            buffer.append(baseName);
            buffer.append("_");
        }

        buffer.append(language);
        int length = buffer.length();

        if(country != null && country.length() > 0) {
            buffer.append("_");
            buffer.append(country);
        }

        if(variant != null && variant.length() > 0) {
            buffer.append("_");
            buffer.append(variant);
        }

        LocalizationContext localizationContext = this.getCachedBundle(buffer.toString().toLowerCase(), locale);

        if(localizationContext == null) {
            buffer.setLength(length);

            if(country != null && country.length() > 0) {
                buffer.append("_");
                buffer.append(country);
            }
            localizationContext = this.getCachedBundle(buffer.toString().toLowerCase(), locale);
        }

        if(localizationContext == null) {
            buffer.setLength(length);
            localizationContext = this.getCachedBundle(buffer.toString().toLowerCase(), locale);
        }
        return localizationContext;
    }

    /**
     * @param fullName
     * @param locale
     * @return LocalizationContext
     */
    public LocalizationContext getCachedBundle(final String fullName, final Locale locale) {
        FutureTask<LocalizationContext> f = this.cache.get(fullName);

        if(f == null) {
            Callable<LocalizationContext> callable = new Callable<LocalizationContext>() {
                /**
                 * @throws InterruptedException
                 */
                @Override
                public LocalizationContext call() throws InterruptedException {
                    try {
                        ResourceBundle resourceBundle = getBaseBundle(fullName);

                        if(resourceBundle != null) {
                            return new LocalizationContext(resourceBundle, locale);
                        }
                        return null;
                    }
                    catch(Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            };

            FutureTask<LocalizationContext> futureTask = new FutureTask<LocalizationContext>(callable);
            f = this.cache.putIfAbsent(fullName, futureTask);

            if(f == null) {
                f = futureTask;
                f.run();
            }
        }

        LocalizationContext localizationContext = null;

        try {
            localizationContext = f.get();
        }
        catch(Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return localizationContext;
    }

    /**
     * @param baseName
     * @param locale
     * @return String
     */
    protected String getFullName(String baseName, Locale locale) {
        String language = locale.getLanguage();
        String country = locale.getCountry();
        String variant = locale.getVariant();

        StringBuilder buffer = new StringBuilder();
        buffer.append(baseName);
        buffer.append("_");
        buffer.append(language);

        if(country != null && country.length() > 0) {
            buffer.append("_");
            buffer.append(country);
        }

        if(variant != null && variant.length() > 0) {
            buffer.append("_");
            buffer.append(variant);
        }
        return buffer.toString();
    }

    /**
     * @param name
     * @return ResourceBundle
     */
    public ResourceBundle getBaseBundle(String name) {
        InputStream inputStream = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        logger.debug(BASEDIR + name.replace('.', '/') + ".properties");

        try {
            inputStream = loader.getResourceAsStream(BASEDIR + name.replace('.', '/') + ".properties");
            return new PropertyResourceBundle(inputStream);
        }
        catch(Exception e) {
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
        return null;
    }
}
