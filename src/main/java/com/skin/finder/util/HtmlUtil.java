/*
 * $RCSfile: HtmlUtil.java,v $
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

/**
 * <p>Title: HtmlUtil</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class HtmlUtil {
    /**
     * @param source
     * @return String
     */
    public static String encode(String source) {
        if(source == null || source.length() < 1) {
            return "";
        }

        int length = source.length();
        StringBuilder buffer = null;

        for(int i = 0; i < length; i++) {
            char c = source.charAt(i);

            switch(c) {
                case '"':
                    if(buffer == null) {
                        buffer = getBuffer(source, 0, i);
                    }
                    buffer.append("&quot;");
                    break;
                case '<':
                    if(buffer == null) {
                        buffer = getBuffer(source, 0, i);
                    }
                    buffer.append("&lt;");
                    break;
                case '>':
                    if(buffer == null) {
                        buffer = getBuffer(source, 0, i);
                    }
                    buffer.append("&gt;");
                    break;
                case '&':
                    if(buffer == null) {
                        buffer = getBuffer(source, 0, i);
                    }
                    buffer.append("&amp;");
                    break;
                case '\'':
                    if(buffer == null) {
                        buffer = getBuffer(source, 0, i);
                    }
                    buffer.append("&#39;");
                    break;
                default:
                    if(buffer != null) {
                        buffer.append(c);
                    }
                    break;
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