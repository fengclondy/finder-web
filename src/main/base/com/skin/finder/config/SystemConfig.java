/*
 * $RCSfile: SystemConfig.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-5-7 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.config;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.skin.finder.manager.SystemVariableManager;

/**
 * <p>Title: SystemConfig</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class SystemConfig {
    protected static final String DEVELOPER = "skin-finder";

    /**
     * @param name
     * @param value
     * @return String
     */
    public static String setValue(String name, String value) {
        SystemVariableManager systemVariableManager = new SystemVariableManager();
        return systemVariableManager.setVariable(name, value);
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public static String getValue(String name, String defaultValue) {
        if(name == null) {
            return null;
        }

        if(name.equals("developer")) {
            return "skinsoft";
        }

        if(name.equals("version")) {
            return "1.0.0.1";
        }

        SystemVariableManager systemVariableManager = new SystemVariableManager();
        String value = systemVariableManager.getVariable(name);

        if(value != null) {
            value = value.trim();
            return (value.length() > 0 ? value : defaultValue);
        }
        return defaultValue;
    }

    /**
     * @param name
     * @return String
     */
    public static String getString(String name) {
        return getValue(name, null);
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public static String getString(String name, String defaultValue) {
        return getValue(name, null);
    }

    /**
     * @param name
     * @return boolean
     */
    public static boolean getBoolean(String name) {
        return getBoolean(name, false);
    }

    /**
     * @param name
     * @param defaultValue
     * @return Boolean
     */
    public static boolean getBoolean(String name, boolean defaultValue) {
        String value = getValue(name, null);

        if(value == null) {
            return defaultValue;
        }
        return parseBoolean(value, defaultValue);
    }

    /**
     * @param name
     * @return Byte
     */
    public static Byte getByte(String name) {
        return getByte(name, (byte)(0));
    }

    /**
     * @param name
     * @param defaultValue
     * @return byte
     */
    public static byte getByte(String name, byte defaultValue) {
        String value = getValue(name, null);

        if(value == null) {
            return defaultValue;
        }
        return parseByte(value, defaultValue);
    }

    /**
     * @param name
     * @return Short
     */
    public static short getShort(String name) {
        return getShort(name, (short)(0));
    }

    /**
     * @param name
     * @param defaultValue
     * @return short
     */
    public static short getShort(String name, short defaultValue) {
        String value = getValue(name, null);

        if(value == null) {
            return defaultValue;
        }
        return parseShort(value, defaultValue);
    }

    /**
     * @param name
     * @return int
     */
    public static int getInt(String name) {
        return getInt(name, 0);
    }

    /**
     * @param name
     * @param defaultValue
     * @return int
     */
    public static int getInt(String name, int defaultValue) {
        String value = getValue(name, null);

        if(value == null) {
            return defaultValue;
        }
        return parseInt(value, defaultValue);
    }

    /**
     * @param name
     * @return float
     */
    public static float getFloat(String name) {
        return getFloat(name, 0.0f);
    }

    /**
     * @param name
     * @param defaultValue
     * @return float
     */
    public static float getFloat(String name, float defaultValue) {
        String value = getValue(name, null);

        if(value == null) {
            return defaultValue;
        }
        return parseFloat(value, defaultValue);
    }

    /**
     * @param name
     * @return double
     */
    public static double getDouble(String name) {
        return getDouble(name, 0.0d);
    }

    /**
     * @param name
     * @param defaultValue
     * @return double
     */
    public static double getDouble(String name, double defaultValue) {
        String value = getValue(name, null);

        if(value == null) {
            return defaultValue;
        }
        return parseDouble(value, defaultValue);
    }

    /**
     * @param name
     * @return long
     */
    public static long getLong(String name) {
        return getLong(name, 0L);
    }

    /**
     * @param name
     * @param defaultValue
     * @return long
     */
    public static long getLong(String name, long defaultValue) {
        String value = getValue(name, null);

        if(value == null) {
            return defaultValue;
        }
        return parseLong(value, defaultValue);
    }

    /**
     * @param name
     * @param pattern
     * @return java.util.Date
     */
    public static java.util.Date getDate(String name, String pattern) {
        String value = getValue(name, null);

        if(value == null) {
            return null;
        }
        return parseDate(value, pattern);
    }

    /**
     * @param name
     * @param pattern
     * @return java.sql.Date
     */
    public static java.sql.Date getSqlDate(String name, String pattern) {
        java.util.Date date = getDate(name, pattern);

        if(date != null) {
            return new java.sql.Date(date.getTime());
        }
        return null;
    }

    /**
     * @param name
     * @param pattern
     * @return java.sql.Timestamp
     */
    public static java.sql.Timestamp getTimestamp(String name, String pattern) {
        java.util.Date date = getDate(name, pattern);

        if(date != null) {
            return new java.sql.Timestamp(date.getTime());
        }
        return null;
    }

    /**
     * @param source
     * @param defaultValue
     * @return boolean
     */
    public static boolean parseBoolean(String source, boolean defaultValue) {
        boolean result = defaultValue;

        if(source != null) {
            try {
                String b = source.toLowerCase();
                result = ("1".equals(b) || "y".equals(b) || "on".equals(b) || "yes".equals(b) || "true".equals(b));
            }
            catch(NumberFormatException e) {
            }
        }
        return result;
    }

    /**
     * @param source
     * @param defaultValue
     * @return Byte
     */
    public static byte parseByte(String source, byte defaultValue) {
        byte result = defaultValue;

        if(source != null) {
            try {
                result = Byte.parseByte(source);
            }
            catch(NumberFormatException e) {
            }
        }
        return result;
    }

    /**
     * @param source
     * @param defaultValue
     * @return parseShort
     */
    public static short parseShort(String source, short defaultValue) {
        short result = defaultValue;

        if(source != null) {
            try {
                result = Short.parseShort(source);
            }
            catch(NumberFormatException e) {
            }
        }
        return result;
    }

    /**
     * @param source
     * @param defaultValue
     * @return Integer
     */
    public static int parseInt(String source, int defaultValue) {
        int result = defaultValue;

        if(source != null) {
            try {
                result = Integer.parseInt(source);
            }
            catch(NumberFormatException e) {
            }
        }
        return result;
    }

    /**
     * @param source
     * @param defaultValue
     * @return float
     */
    public static float parseFloat(String source, float defaultValue) {
        float result = defaultValue;

        if(source != null) {
            try {
                result = Float.parseFloat(source);
            }
            catch(NumberFormatException e) {
            }
        }
        return result;
    }

    /**
     * @param source
     * @param defaultValue
     * @return double
     */
    public static double parseDouble(String source, double defaultValue) {
        double result = defaultValue;

        if(source != null) {
            try {
                result = Double.parseDouble(source);
            }
            catch(NumberFormatException e) {
            }
        }
        return result;
    }

    /**
     * @param source
     * @param defaultValue
     * @return long
     */
    public static long parseLong(String source, long defaultValue) {
        long result = defaultValue;

        if(source != null) {
            try {
                result = Long.parseLong(source);
            }
            catch(NumberFormatException e) {
            }
        }
        return result;
    }

    /**
     * @param source
     * @param pattern
     * @return java.util.Date
     */
    public static java.util.Date parseDate(String source, String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);

        try {
            return dateFormat.parse(source);
        }
        catch(ParseException e) {
        }
        return null;
    }
}
