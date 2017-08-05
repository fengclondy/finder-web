/*
 * $RCSfile: MultipartHttpRequest.java,v $
 * $Revision: 1.1 $
 * $Date: 2013-03-20 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.upload;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * <p>Title: MultipartHttpRequest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class MultipartHttpRequest extends HttpServletRequestWrapper {
    private Map<String, Object[]> params;

    /**
     * @param request
     */
    protected MultipartHttpRequest(HttpServletRequest request) {
        super(request);
        this.params = new HashMap<String, Object[]>();
        this.params.putAll(this.getParameterMap(request));
    }

    /**
     * @param request
     * @param maxFileSize
     * @param maxBodySize
     * @param repository
     * @return MultipartHttpRequest
     * @throws IOException 
     */
    public static MultipartHttpRequest parse(HttpServletRequest request, int maxFileSize, int maxBodySize, String repository) throws IOException {
        if(request instanceof MultipartHttpRequest) {
            return (MultipartHttpRequest)request;
        }

        Multipart multipart = new Multipart();
        multipart.setRepository(repository);
        multipart.setMaxFileSize(maxFileSize);
        multipart.setMaxBodySize(maxBodySize);
        Map<String, Part[]> map = new HashMap<String, Part[]>();
        MultipartHttpRequest multipartHttpRequest = new MultipartHttpRequest(request);
        List<Part> items = multipart.parse(request);

        if(items != null) {
            for(Part part : items) {
                String name = part.getName();
                Part[] list = map.get(name);

                if(list == null) {
                    map.put(name, new Part[]{part});
                }
                else {
                    Part[] temp = new Part[list.length + 1];
                    System.arraycopy(list, 0, temp, 0, list.length);
                    temp[list.length] = part;
                    map.put(name, temp);
                }
            }
        }
        multipartHttpRequest.putAll(map);
        return multipartHttpRequest;
    }

    /**
     * @param request
     * @return Map<String, String[]>
     */
    protected Map<String, String[]> getParameterMap(HttpServletRequest request) {
        Map<?, ?> map = request.getParameterMap();
        Map<String, String[]> result = new HashMap<String, String[]>();

        if(map != null && map.size() > 0) {
            for(Map.Entry<?, ?> entry : map.entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();

                if(key instanceof String) {
                    String name = (String)key;

                    if(value instanceof String) {
                        result.put(name, new String[]{value.toString()});
                    }
                    else if(value instanceof String[]) {
                        result.put(name, (String[])value);
                    }
                    else if(value instanceof List) {
                        List<?> list = (List<?>)value;
                        String[] array = new String[list.size()];
                        list.toArray(array);
                        result.put(name, array);
                    }
                }
            }
        }
        return result;
    }

    /**
     * @param map
     */
    private void putAll(Map<String, Part[]> map) {
        if(map != null && map.size() > 0) {
            for(Map.Entry<String, Part[]> entry : map.entrySet()) {
                String key = entry.getKey();
                Part[] fileItemList = entry.getValue();

                if(fileItemList != null && fileItemList.length > 0) {
                    Object[] oldValues = this.params.get(key);

                    if(oldValues == null) {
                        this.params.put(key, fileItemList);
                    }
                    else {
                        Object[] newValues = this.merge(oldValues, fileItemList);
                        this.params.put(key, newValues);
                    }
                }
            }
        }
    }

    /**
     * @param values
     * @param items
     * @return Object[]
     */
    private Object[] merge(Object[] values, Part[] items) {
        int index = 0;
        Object[] result = new Object[values.length + items.length];

        for(int i = 0; i < values.length; i++) {
            result[index] = values[i];
            index++;
        }

        for(int i = 0; i < items.length; i++) {
            result[index] = items[i];
            index++;
        }
        return result;
    }

    /**
     * @param name
     * @return String
     */
    @Override
    public String getParameter(String name) {
        String[] values = this.getParameterValues(name);

        if(values != null && values.length > 0) {
            return values[0];
        }
        else {
            return null;
        }
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public String getString(String name, String defaultValue) {
        String value = this.getParameter(name);
        return (value != null ? value : defaultValue);
    }

    /**
     * @param name
     * @return String
     */
    public String getTrimString(String name) {
        String value = this.getParameter(name);
        return (value != null ? value.trim() : "");
    }

    /**
     * @param name
     * @param defaultValue
     * @return Integer
     */
    public Boolean getBoolean(String name, Boolean defaultValue) {
        String value = this.getParameter(name);

        if(value != null) {
            return (value.equalsIgnoreCase("true") || value.equals("1") || value.equalsIgnoreCase("y") || value.equalsIgnoreCase("on"));
        }
        return defaultValue;
    }

    /**
     * @param name
     * @param defaultValue
     * @return Integer
     */
    public Byte getByte(String name, Byte defaultValue) {
        String value = this.getParameter(name);

        if(value != null) {
            try {
                return Byte.valueOf(Byte.parseByte(value));
            }
            catch(NumberFormatException e) {
            }
        }
        return defaultValue;
    }

    /**
     * @param name
     * @param defaultValue
     * @return Integer
     */
    public Short getShort(String name, Short defaultValue) {
        String value = this.getParameter(name);

        if(value != null) {
            try {
                return Short.valueOf(Short.parseShort(value));
            }
            catch(NumberFormatException e) {
            }
        }
        return defaultValue;
    }

    /**
     * @param name
     * @param defaultValue
     * @return Integer
     */
    public Integer getInteger(String name, Integer defaultValue) {
        String value = this.getParameter(name);

        if(value != null) {
            try {
                return Integer.valueOf(Integer.parseInt(value));
            }
            catch(NumberFormatException e) {
            }
        }
        return defaultValue;
    }

    /**
     * @param name
     * @param defaultValue
     * @return Integer
     */
    public Float getFloat(String name, Float defaultValue) {
        String value = this.getParameter(name);

        if(value != null) {
            try {
                return Float.valueOf(Float.parseFloat(value));
            }
            catch(NumberFormatException e) {
            }
        }
        return defaultValue;
    }

    /**
     * @param name
     * @param defaultValue
     * @return Integer
     */
    public Long getLong(String name, Long defaultValue) {
        String value = this.getParameter(name);

        if(value != null) {
            try {
                return Long.valueOf(Long.parseLong(value));
            }
            catch(NumberFormatException e) {
            }
        }
        return defaultValue;
    }

    /**
     * @param name
     * @param pattern
     * @return Integer
     */
    public Date getDate(String name, String pattern) {
        return this.getDate(name, pattern, null);
    }

    /**
     * @param name
     * @param pattern
     * @param defaultValue
     * @return Integer
     */
    public Date getDate(String name, String pattern, Date defaultValue) {
        String value = this.getParameter(name);

        if(value != null) {
            try {
                DateFormat dateFormat = new SimpleDateFormat(pattern);
                return dateFormat.parse(pattern);
            }
            catch(ParseException e) {
            }
        }
        return defaultValue;
    }

    /**
     * @param name
     * @return Part
     */
    public Part getFileItem(String name) {
        Object[] values = this.params.get(name);

        if(values == null) {
            return null;
        }

        Part part = null;

        for(int i = 0; i < values.length; i++) {
            Object value = values[i];

            if(value instanceof Part) {
                part = (Part)value;

                if(part.isFileField()) {
                    return part;
                }
            }
        }
        return null;
    }

    /**
     * @param name
     * @return String
     */
    @Override
    public String[] getParameterValues(String name) {
        Object[] values = this.params.get(name);

        if(values == null) {
            return new String[0];
        }

        Part part = null;
        List<String> list = new ArrayList<String>();

        for(int i = 0; i < values.length; i++) {
            Object value = values[i];

            if(value == null) {
                continue;
            }

            if(value instanceof String) {
                list.add((String)value);
            }
            else if(value instanceof Part) {
                part = (Part)value;

                if(part.isFormField()) {
                    list.add(part.getBody(this.getCharacterEncoding()));
                }
            }
            else {
                list.add(value.toString());
            }
        }
        String[] result = new String[list.size()];
        list.toArray(result);
        return result;
    }

    /**
     * 
     */
    public void destroy() {
        if(this.params != null) {
            for(Map.Entry<String, Object[]> entry : this.params.entrySet()) {
                Object[] values = entry.getValue();

                if(values != null) {
                    for(Object item : values) {
                        if(item instanceof Part) {
                            ((Part)item).delete();
                        }
                    }
                }
            }
        }
    }
}
