/*
 * $RCSfile: ContentDisposition.java,v $
 * $Revision: 1.1 $
 * $Date: 2014-01-03 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.upload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: ContentDisposition</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ContentDisposition {
    private Map<String, List<String>> map;

    /**
     * @param args
     */
    public static void main(String[] args) {
        String source = "form-data; name=\"myfile1\"; filename=\"11.tmp\"";
        Map<String, List<String>> map = ContentDisposition.parse1(source);

        for(Map.Entry<String, List<String>> entry : map.entrySet()) {
            String name = entry.getKey();
            List<String> list = entry.getValue();

            for(String value : list) {
                System.out.println(name + "=" + value);
            }
        }
    }

    /**
     * @param map
     */
    private ContentDisposition(Map<String, List<String>> map) {
        this.map = map;
    }

    /**
     * @param name
     * @return String
     */
    public String getProperty(String name) {
        List<String> list = this.map.get(name);

        if(list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * @param content
     * @return ContentDisposition
     */
    public static ContentDisposition parse(String content) {
        Map<String, List<String>> map = parse1(content);
        return new ContentDisposition(map);
    }

    /**
     * @param source
     * @return Map<String, List<String>>
     */
    public static Map<String, List<String>> parse1(String source) {
        String content = source.trim();
        StringBuilder name = new StringBuilder();
        StringBuilder value = new StringBuilder();
        Map<String, List<String>> map = new HashMap<String, List<String>>();

        int i = 0;
        int length = content.length();

        while(i < length) {
            char c = content.charAt(i);

            if(c == ';') {
                put(map, name.toString(), value.toString());
                name.setLength(0);
                value.setLength(0);

                i++;
                while(i < length) {
                    c = content.charAt(i);

                    if(c > ' ') {
                        break;
                    }
                    i++;
                }
                continue;
            }

            if(c == '=') {
                i++;
                while(i < length) {
                    c = content.charAt(i);

                    if(c == ';') {
                        break;
                    }
                    value.append(c);
                    i++;
                }

                i++;
                while(i < length) {
                    c = content.charAt(i);

                    if(c > ' ') {
                        break;
                    }
                    i++;
                }

                put(map, name.toString(), value.toString());
                name.setLength(0);
                value.setLength(0);
            }
            name.append(c);
            i++;
        }
        return map;
    }

    /**
     * @param map
     * @param name
     * @param value
     */
    private static void put(Map<String, List<String>> map, String name, String value) {
        if(name == null || name.length() < 1) {
            return;
        }
        
        String content = null;

        if(value == null) {
            content = "";
        }
        else {
            content = value.trim();
        }

        List<String> list = map.get(name);

        if(list == null) {
            list = new ArrayList<String>();
            map.put(name, list);
        }

        if(content.startsWith("\"")) {
            content = content.substring(1);
        }

        if(content.endsWith("\"")) {
            content = content.substring(0, content.length() - 1);
        }
        list.add(content);
    }
}
