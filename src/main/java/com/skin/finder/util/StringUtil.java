/*
 * $RCSfile: StringUtil.java,v $
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: StringUtil</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class StringUtil {
    /**
     * default
     */
    private StringUtil() {
    }

    /**
     * @param text
     * @return boolean
     */
    public static boolean isBlank(String text) {
        if(text == null) {
            return true;
        }

        int length = text.length();

        if(length == 0) {
            return true;
        }

        for(int i = 0; i < length; i++) {
            if(Character.isWhitespace(text.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param text
     * @return boolean
     */
    public static boolean notBlank(String text) {
        return !isBlank(text);
    }

    /**
     * @param source
     * @param limit
     * @param trim
     * @param ignoreWhitespace
     * @return String[]
     */
    public static String[] split(String source, String limit, boolean trim, boolean ignoreWhitespace) {
        int i = 0;
        int j = 0;
        String s = null;
        List<String> list = new ArrayList<String>();

        while((j = source.indexOf(limit, i)) > -1) {
            if(j > i) {
                s = source.substring(i, j);

                if(trim) {
                    s = s.trim();
                }

                if(!ignoreWhitespace || s.length() > 0) {
                    list.add(s);
                }
            }
            i = j + limit.length();
        }

        if(i < source.length()) {
            s = source.substring(i);

            if(trim) {
                s = s.trim();
            }

            if(!ignoreWhitespace || s.length() > 0) {
                list.add(s);
            }
        }
        String[] result = new String[list.size()];
        return list.toArray(result);
    }

    /**
     * @param source
     * @param context
     * @return String
     */
    public static String replace(String source, Map<String, String> context) {
        char c;
        int length = source.length();
        StringBuilder name = new StringBuilder();
        StringBuilder result = new StringBuilder(4096);

        for(int i = 0; i < length; i++) {
            c = source.charAt(i);

            if(c == '$' && i < length - 1 && source.charAt(i + 1) == '{') {
                for(i = i + 2; i < length; i++) {
                    c = source.charAt(i);

                    if(c == '}') {
                        String value = context.get(name.toString().trim());

                        if(value != null) {
                            result.append(value);
                        }
                        break;
                    }
                    else {
                        name.append(c);
                    }
                }
                name.setLength(0);
            }
            else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * @param source
     * @return String
     */
    public static String escape(String source) {
        if(source == null) {
            return "";
        }

        char c;
        StringBuilder buffer = null;

        for(int i = 0, length = source.length(); i < length; i++) {
            c = source.charAt(i);

            switch (c) {
                case '"': {
                    if(buffer == null) {
                        buffer = StringUtil.getBuffer(source, 0, i);
                    }
                    buffer.append("\\\"");
                    break;
                }
                case '\r': {
                    if(buffer == null) {
                        buffer = StringUtil.getBuffer(source, 0, i);
                    }
                    buffer.append("\\r");
                    break;
                }
                case '\n': {
                    if(buffer == null) {
                        buffer = StringUtil.getBuffer(source, 0, i);
                    }
                    buffer.append("\\n");
                    break;
                }
                case '\t': {
                    if(buffer == null) {
                        buffer = StringUtil.getBuffer(source, 0, i);
                    }
                    buffer.append("\\t");
                    break;
                }
                case '\b': {
                    if(buffer == null) {
                        buffer = StringUtil.getBuffer(source, 0, i);
                    }
                    buffer.append("\\b");
                    break;
                }
                case '\f': {
                    if(buffer == null) {
                        buffer = StringUtil.getBuffer(source, 0, i);
                    }
                    buffer.append("\\f");
                    break;
                }
                case '\\': {
                    if(buffer == null) {
                        buffer = StringUtil.getBuffer(source, 0, i);
                    }
                    buffer.append("\\\\");
                    break;
                }
                default : {
                    if(buffer != null) {
                        buffer.append(c);
                    }
                    break;
                }
            }
        }
        return (buffer != null ? buffer.toString() : source);
    }

    /**
     * @param source
     * @param offset
     * @param length
     * @return StringBuilder
     */
    public static StringBuilder getBuffer(String source, int offset, int length) {
        StringBuilder buffer = new StringBuilder();

        if(length > 0) {
            char[] cbuf = new char[length];
            buffer = new StringBuilder((int)(source.length() * 1.2));
            source.getChars(offset, length, cbuf, 0);
            buffer.append(cbuf, 0, length);
        }
        return buffer;
    }
}
