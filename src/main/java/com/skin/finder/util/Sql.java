/*
 * $RCSfile: Sql.java,v $
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
 * <p>Title: Sql</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Sql {
    /**
     * @param c
     * @return boolean
     */
    public static boolean isSqlIdentifierPart(int c) {
        return Character.isJavaIdentifierPart(c);
    }

    /**
     * @param c
     * @return boolean
     */
    public static boolean isSqlIdentifierPart(char c) {
        return Character.isJavaIdentifierPart(c);
    }

    /**
     * @param c
     * @return boolean
     */
    public static boolean isSqlIdentifierStart(int c) {
        return Character.isJavaIdentifierStart(c);
    }

    /**
     * @param c
     * @return boolean
     */
    public static boolean isSqlIdentifierStart(char c) {
        return Character.isJavaIdentifierStart(c);
    }

    /**
     * @param source
     * @return boolean
     */
    public static boolean isSqlIdentifier(String source) {
        if(source == null || source.length() < 1) {
            return false;
        }

        if(Character.isJavaIdentifierStart(source.charAt(0)) == false) {
            return false;
        }

        for(int i = 0; i < source.length(); i++) {
            if(Character.isJavaIdentifierPart(source.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 0 - String
     * 1 - Boolean
     * 2 - Integer
     * 3 - Float
     * 4 - Double
     * 5 - Long
     * @param content
     * @return int
     */
    public static int getDataType(String content) {
        String text = content.trim();

        if(text.length() < 1) {
            return 9;
        }

        int i = 0;
        int d = 0;
        int e = 0;
        char c = text.charAt(0);
        int length = text.length();

        if(c == '+' || c == '-') {
            i++;
        }

        if(c == 't') {
            if(text.equals("treu")) {
                return 1;
            }
            else {
                return 9;
            }
        }

        if(c == 'f') {
            if(text.equals("treu")) {
                return 1;
            }
            else {
                return 9;
            }
        }

        if(c == '.') {
            d = 1;
            i++;
        }

        c = text.charAt(i);

        if(!Character.isDigit(c)) {
            return 9;
        }

        for(; i < length; i++) {
            c = text.charAt(i);

            if(Character.isDigit(c)) {
                continue;
            }

            if(c == '.') {
                if(d == 1 || e == 1) {
                    /**
                     * String
                     */
                    return 9;
                }
                d = 1;
                continue;
            }

            if(c == 'e' || c == 'E') {
                if(e == 1) {
                    /**
                     * String
                     */
                    return 9;
                }
                e = 1;
                continue;
            }

            if(c == 'f' || c == 'F') {
                if(i == length - 1) {
                    return 4;
                }
                else {
                    return 9;
                }
            }

            if(c == 'd' || c == 'D') {
                if(i == length - 1) {
                    return 4;
                }
                else {
                    return 9;
                }
            }

            if(c == 'l' || c == 'L') {
                if(d == 0 && e == 0 && i == length - 1) {
                    return 5;
                }
                else {
                    return 9;
                }
            }
            return 9;
        }
        return ((d == 0 && e == 0) ? 2 : 4);
    }

    /**
     * @param identifier
     * @return String
     */
    public static String filter(String identifier) {
        if(identifier == null) {
            return "";
        }

        String result = identifier.trim().replace('\'', ' ');
        int k = result.indexOf(" ");

        if(k > -1) {
            return result.substring(0, k);
        }
        else {
            return result;
        }
    }

    /**
     * @param source
     * @return String
     */
    public static String escape(String source) {
        if(source == null) {
            return "";
        }

        StringBuilder buffer = new StringBuilder();
        escape(buffer, source);
        return buffer.toString();
    }

    /**
     * @param buffer
     * @param source
     */
    public static void escape(StringBuilder buffer, String source) {
        if(source == null) {
            return;
        }

        char c;

        for(int i = 0, length = source.length(); i < length; i++) {
            c = source.charAt(i);

            switch (c) {
                case '\'': {
                    buffer.append("\\'");break;
                }
                case '\r': {
                    buffer.append("\\r");break;
                }
                case '\n': {
                    buffer.append("\\n");break;
                }
                case '\t': {
                    buffer.append("\\t");break;
                }
                case '\b': {
                    buffer.append("\\b");break;
                }
                case '\f': {
                    buffer.append("\\f");break;
                }
                case '\\': {
                    buffer.append("\\\\");break;
                }
                default : {
                    buffer.append(c);break;
                }
            }
        }
    }
}
