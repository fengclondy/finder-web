/*
 * $RCSfile: HttpRequest.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-01-06 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: HttpRequest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class URLParameter {
    private Map<String, String[]> map;
    private static final Logger logger = LoggerFactory.getLogger(URLParameter.class);

    /**
     *
     */
    public URLParameter() {
        this.map = new HashMap<String, String[]>();
    }

    /**
     * @param map
     */
    public URLParameter(Map<String, String[]> map) {
        this.map = new HashMap<String, String[]>();
        this.map.putAll(map);
    }
    

    /**
     * @param query
     * @return Map<String, List<String>>
     */
    public static URLParameter parse(String query) {
        return URLParameter.parse(query, "UTF-8");
    }

    /**
     * @param url
     * @param encode
     * @return Map<String, List<String>>
     */
    public static URLParameter parse(String url, String encode) {
        URLParameter parameters = new URLParameter();

        if(url == null || url.length() < 1)  {
            return parameters;
        }

        Map<String, String[]> params = new HashMap<String, String[]>();
        Map<String, List<String>> map = new HashMap<String, List<String>>();

        String key = null;
        String query = url;
        StringBuilder name = new StringBuilder();
        StringBuilder value = new StringBuilder();

        for(int i = 0, length = query.length(); i < length; i++) {
            char c = query.charAt(i);

            if(c == '?') {
                if(i + 1 < length) {
                    query = query.substring(i + 1);
                }
                else {
                    query = "";
                }

                break;
            }
        }

        for(int i = 0, length = query.length(); i < length; i++) {
            char c = query.charAt(i);

            if(c == '?' || c == '&') {
                continue;
            }
            else if(c == '#') {
                for(i++; i < length; i++) {
                    c = query.charAt(i);

                    if(c == '?' || c == '&' || c == '#') {
                        i--;
                        break;
                    }
                }
            }
            else if(c == '=') {
                for(i++; i < length; i++) {
                    c = query.charAt(i);

                    if(c == '?' || c == '&' || c == '#') {
                        if(c == '#') {
                            i--;
                        }

                        break;
                    }
                    value.append(c);
                }

                if(name.length() > 0) {
                    key = name.toString();
                    List<String> values = map.get(key);

                    if(values == null) {
                        values = new ArrayList<String>();
                        map.put(key, values);
                    }

                    try {
                        values.add(URLDecoder.decode(value.toString(), encode));
                    }
                    catch(UnsupportedEncodingException e) {
                    }
                }

                name.setLength(0);
                value.setLength(0);
            }
            else {
                name.append(c);
            }
        }

        for(Map.Entry<String, List<String>> entry : map.entrySet()) {
            key = entry.getKey();
            List<String> list = entry.getValue();

            String[] values = new String[list.size()];
            list.toArray(values);
            params.put(key, values);
        }

        parameters.addParameter(params);
        return parameters;
    }

    /**
     * @param name
     * @param value
     */
    public void addParameter(String name, String value) {
        this.addParameter(name, new String[]{value});
    }

    /**
     * @param name
     * @param values
     */
    public void addParameter(String name, String[] values) {
        String[] oldValues = this.map.get(name);

        if(oldValues == null) {
            this.map.put(name, values);
            return;
        }

        String[] newValues = new String[oldValues.length + values.length];
        System.arraycopy(oldValues, 0, newValues, 0, oldValues.length);
        System.arraycopy(values, 0, newValues, oldValues.length, values.length);
    }

    /**
     * @param map
     */
    public void addParameter(Map<String, String[]> map) {
        this.map.putAll(map);
    }

    /**
     * @param name
     */
    public void removeParameter(String name) {
        this.map.remove(name);
    }

    /**
     * @param name
     * @return String
     */
    public String getParameter(String name) {
        String[] values = this.map.get(name);

        if(values != null && values.length > 0) {
            return values[0];
        }
        return null;
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public String getParameter(String name, String defaultValue) {
        String value = this.getParameter(name);
        return (value != null ? value : defaultValue);
    }

    /**
     * @param name
     * @return String
     */
    public String[] getParameterValues(String name) {
        String[] values = this.map.get(name);
        return (values != null ? values : new String[0]);
    }

    /**
     * @param name
     * @return String
     */
    public String getString(String name) {
        String[] values = this.map.get(name);

        if(values != null && values.length > 0) {
            return values[0];
        }
        return null;
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public String getString(String name, String defaultValue) {
        String value = this.getString(name);
        return (value != null ? value : defaultValue);
    }

    /**
     * @param name
     * @return String
     */
    public String getTrimString(String name) {
        String value = this.getString(name);
        return (value != null ? value.trim() : "");
    }

    /**
     * @param name
     * @return Byte
     */
    public Byte getByte(String name) {
        String value = this.getParameter(name);

        if(value != null) {
            try {
                return Byte.parseByte(value.trim());
            }
            catch(NumberFormatException e){}
        }
        return null;
    }

    /**
     * @param name
     * @param defaultValue
     * @return Byte
     */
    public Byte getByte(String name, byte defaultValue) {
        String value = this.getParameter(name);

        if(value != null) {
            try {
                return Byte.parseByte(value.trim());
            }
            catch(NumberFormatException e){}
        }
        return Byte.valueOf(defaultValue);
    }

    /**
     * @param name
     * @return Short
     */
    public Short getShort(String name) {
        String value = this.getParameter(name);

        if(value != null) {
            try {
                return Short.parseShort(value.trim());
            }
            catch(NumberFormatException e){}
        }
        return null;
    }

    /**
     * @param name
     * @param defaultValue
     * @return Short
     */
    public Short getShort(String name, short defaultValue) {
        String value = this.getParameter(name);

        if(value != null) {
            try {
                return Short.parseShort(value.trim());
            }
            catch(NumberFormatException e){}
        }
        return Short.valueOf(defaultValue);
    }

    /**
     * @param name
     * @return Boolean
     */
    public Boolean getBoolean(String name) {
        String value = this.getParameter(name);

        if(value != null) {
            value = value.trim();
            boolean b = ("1".equalsIgnoreCase(value) || "y".equalsIgnoreCase(value) || "on".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value));
            return Boolean.valueOf(b);
        }
        return Boolean.FALSE;
    }

    /**
     * @param name
     * @param defaultValue
     * @return Boolean
     */
    public Boolean getBoolean(String name, boolean defaultValue) {
        String value = this.getParameter(name);

        if(value != null) {
            value = value.trim();
            boolean b = ("1".equalsIgnoreCase(value) || "y".equalsIgnoreCase(value) || "on".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value));
            return Boolean.valueOf(b);
        }
        return Boolean.valueOf(defaultValue);
    }

    /**
     * @param name
     * @return Character
     */
    public Character getCharacter(String name) {
        String value = this.getParameter(name);

        if(value != null && value.trim().length() > 0) {
            return Character.valueOf(value.trim().charAt(0));
        }
        return null;
    }

    /**
     * @param name
     * @param defaultValue
     * @return Character
     */
    public Character getCharacter(String name, char defaultValue) {
        String value = this.getParameter(name);

        if(value != null && value.trim().length() > 0) {
            return Character.valueOf(value.trim().charAt(0));
        }
        return Character.valueOf(defaultValue);
    }

    /**
     * @param name
     * @return Integer
     */
    public Integer getInteger(String name) {
        String value = this.getParameter(name);

        if(value != null) {
            try {
                return Integer.parseInt(value.trim());
            }
            catch(NumberFormatException e){}
        }
        return null;
    }

    /**
     * @param name
     * @param defaultValue
     * @return Integer
     */
    public Integer getInteger(String name, int defaultValue) {
        String value = this.getParameter(name);

        if(value != null) {
            try {
                return Integer.parseInt(value.trim());
            }
            catch(NumberFormatException e){}
        }
        return Integer.valueOf(defaultValue);
    }

    /**
     * @param name
     * @return Float
     */
    public Float getFloat(String name) {
        String value = this.getParameter(name);

        if(value != null) {
            try {
                return Float.parseFloat(value.trim());
            }
            catch(NumberFormatException e){}
        }
        return null;
    }

    /**
     * @param name
     * @param defaultValue
     * @return Float
     */
    public Float getFloat(String name, float defaultValue) {
        String value = this.getParameter(name);

        if(value != null) {
            try {
                return Float.parseFloat(value.trim());
            }
            catch(NumberFormatException e){}
        }
        return Float.valueOf(defaultValue);
    }

    /**
     * @param name
     * @return Double
     */
    public Double getDouble(String name) {
        String value = this.getParameter(name);

        if(value != null) {
            try {
                return Double.parseDouble(value.trim());
            }
            catch(NumberFormatException e){}
        }
        return null;
    }

    /**
     * @param name
     * @param defaultValue
     * @return Double
     */
    public Double getDouble(String name, double defaultValue) {
        String value = this.getParameter(name);

        if(value != null) {
            try {
                return Double.parseDouble(value.trim());
            }
            catch(NumberFormatException e){}
        }
        return Double.valueOf(defaultValue);
    }

    /**
     * @param name
     * @return Double
     */
    public Long getLong(String name) {
        String value = this.getParameter(name);

        if(value != null) {
            try {
                return Long.parseLong(value.trim());
            }
            catch(NumberFormatException e){}
        }
        return null;
    }

    /**
     * @param name
     * @param defaultValue
     * @return Long
     */
    public Long getLong(String name, long defaultValue) {
        String value = this.getParameter(name);

        if(value != null) {
            try {
                return Long.parseLong(value.trim());
            }
            catch(NumberFormatException e){}
        }
        return Long.valueOf(defaultValue);
    }

    /**
     * @param name
     * @return Long
     */
    public long[] getLongValues(String name) {
        String[] values = this.getParameterValues(name);

        if(values != null && values.length > 0) {
            int index = 0;
            long[] result = new long[values.length];

            for(String value : values) {
                try {
                    result[index] = Long.parseLong(value.trim());
                    index++;
                }
                catch(NumberFormatException e) {
                }
            }

            if(index >= result.length) {
                return result;
            }
            long[] temp = new long[index];
            System.arraycopy(result, 0, temp, 0, index);
            return temp;
        }
        return new long[0];
    }

    /**
     * @param name
     * @param pattern
     * @return java.util.Date
     */
    public Date getDate(String name, String pattern) {
        String date = this.getParameter(name);

        if(date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

            try {
                return dateFormat.parse(date.trim());
            }
            catch(ParseException e) {
            }
        }
        return null;
    }

    /**
     * @param name
     * @param pattern
     * @return Timestamp
     */
    public Timestamp getTimestamp(String name, String pattern) {
        java.util.Date date = this.getDate(name, pattern);

        if(date != null) {
            return new Timestamp(date.getTime());
        }
        return null;
    }

    /**
     * @param <T>
     * @param bean
     * @return <T>
     */
    public <T> T parse(T bean) {
        String name = null;
        String value = null;
        Method[] methods = bean.getClass().getMethods();

        for(int i = 0; i < methods.length; i++) {
            name = methods[i].getName();
            if(name.length() > 3 && name.startsWith("set")) {
                try {
                    name = Character.toLowerCase(name.charAt(3)) + name.substring(4);
                    value = this.getParameter(name);

                    if(value != null) {
                        Class<?>[] types = methods[i].getParameterTypes();

                        if(types != null && types.length == 1) {
                            // TODO: ClassUtil.cast
                            // Object object = ClassUtil.cast(value, types[0]);
                            Object object = cast(value, types[0]);

                            if(object != null) {
                                methods[i].invoke(bean, new Object[]{object});
                            }
                        }
                    }
                }
                catch(Exception e) {
                    logger.warn(e.getMessage());
                }
            }
        }
        return bean;
    }

    /**
     * @param value
     * @param type
     * @return Object
     */
    public Object cast(Object value, Class<?> type) {
        return value;
    }
}
