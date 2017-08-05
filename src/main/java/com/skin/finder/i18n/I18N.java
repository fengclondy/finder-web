/*
 * $RCSfile: I18N.java,v $
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title: I18N</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author Admin
 * @version 1.0
 */
public class I18N {
    private static final LocalizationContext EMPTY = new LocalizationContext((ResourceBundle)null, Locale.ENGLISH);

    /**
     * @param args
     */
    public static void main(String[] args) {
        LocalizationContext localizationContext = I18N.getBundle("zh_cn", "");
        ResourceBundle resourceBundle = localizationContext.getResourceBundle();
        System.out.println(localizationContext.format("finder.list.open"));
        System.out.println(resourceBundle.getString("finder.list.open"));
    }

    /**
     * default
     */
    private I18N() {
    }

    /**
     * @param request
     * @return LocalizationContext
     */
    public static LocalizationContext getBundle(HttpServletRequest request) {
        Locale locale = getLocale(request);
        LocalizationContext bundle = BundleManager.getInstance().getBundle("", locale);

        if(bundle == null) {
            return EMPTY;
        }
        return bundle;
    }

    /**
     * @param value
     * @param variant
     * @return LocalizationContext
     */
    public static LocalizationContext getBundle(String value, String variant) {
        Locale locale = getLocale(value, variant);
        LocalizationContext bundle = BundleManager.getInstance().getBundle("", locale);

        if(bundle == null) {
            return EMPTY;
        }
        return bundle;
    }

    /**
     * @param request
     * @return Locale
     */
    public static Locale getLocale(HttpServletRequest request) {
        Locale locale = request.getLocale();

        if(locale == null) {
            locale = Locale.getDefault();
        }
        return locale;
    }

    /**
     * @param value
     * @param variant
     * @return Locale
     */
    public static Locale getLocale(String value, String variant) {
        int i = 0;
        char ch = '\000';
        int length = value.length();
        StringBuilder buffer = new StringBuilder();

        for(; i < length; i++) {
            ch = value.charAt(i);

            if(ch != '_' && ch != '-') {
                buffer.append(ch);
            }
            else {
                break;
            }
        }

        String language = buffer.toString();
        String country = "";

        if((ch == '_') || (ch == '-')) {
            buffer.setLength(0);

            for(i++; i < length; i++) {
                ch = value.charAt(i);

                if(ch != '_' && ch != '-') {
                    buffer.append(ch);
                }
                else {
                    break;
                }
            }
            country = buffer.toString();
        }

        if(variant != null && variant.length() > 0) {
            return new Locale(language, country, variant);
        }
        return new Locale(language, country);
    }
}
