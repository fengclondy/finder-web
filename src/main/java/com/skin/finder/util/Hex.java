/*
 * $RCSfile: Hex.java,v $
 * $Revision: 1.1  $
 * $Date: 2009-7-2  $
 *
 * Copyright (C) 2005 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import java.io.UnsupportedEncodingException;

/**
 * <p>Title: Hex</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Hex {
    private static final char[] digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * @return byte[]
     */
    public static byte[] getBytes() {
        byte[] bytes = new byte[256];

        for(int i = 0, j = Byte.MIN_VALUE; i < 256; i++, j++) {
            bytes[i] = (byte)(j);
        }

        return bytes;
    }

    /**
     * @param b
     * @return String
     */
    public static String encode(byte b) {
        char[] chars = new char[2];
        chars[0] = digit[(b >>> 4) & 0X0F];
        chars[1] = digit[(b & 0X0F)];
        return new String(chars);
    }

    /**
     * @param bytes
     * @return String
     */
    public static String encode(byte[] bytes) {
        return encode(bytes, 0, bytes.length);
    }

    /**
     * @param bytes
     * @param offset
     * @param length
     * @return String
     */
    public static String encode(byte[] bytes, int offset, int length) {
        char[] chars = new char[2];
        StringBuilder buffer = new StringBuilder(bytes.length * 2);

        for(int i = offset; i < length; i++) {
            chars[0] = digit[(bytes[i] >>> 4) & 0X0F];
            chars[1] = digit[(bytes[i] & 0X0F)];
            buffer.append(chars);
        }
        return buffer.toString();
    }

    /**
     * @param s
     * @return byte[]
     */
    public static byte[] decode(String s) {
        char chars[] = s.toCharArray();
        byte bytes[] = new byte[chars.length / 2];

        for(int i = 0, j = 0; j < chars.length; i++, j += 2) {
            byte b = (byte)((b = (byte)((b = (byte)(0 | a(chars[j]))) << 4)) | a(chars[j + 1]));
            bytes[i] = b;
        }
        return bytes;
    }

    /**
     * @param s
     * @param charset
     * @return String
     */
    public static String encode(String s, String charset) {
        try {
            return encode(s.getBytes(charset));
        }
        catch(UnsupportedEncodingException e) {
        }
        return null;
    }

    /**
     * @param s
     * @param charset
     * @return String
     */
    public static String decode(String s, String charset) {
        byte[] bytes = decode(s);

        try {
            return new String(bytes, charset);
        }
        catch(UnsupportedEncodingException e) {
        }
        return null;
    }

    /**
     * @param c
     * @return byte
     */
    private static final byte a(char c) {
        switch(c) {
            case 48: // '0'
                return 0;

            case 49: // '1'
                return 1;

            case 50: // '2'
                return 2;

            case 51: // '3'
                return 3;

            case 52: // '4'
                return 4;

            case 53: // '5'
                return 5;

            case 54: // '6'
                return 6;

            case 55: // '7'
                return 7;

            case 56: // '8'
                return 8;

            case 57: // '9'
                return 9;

            case 97: // 'a'
                return 10;

            case 98: // 'b'
                return 11;

            case 99: // 'c'
                return 12;

            case 100: // 'd'
                return 13;

            case 101: // 'e'
                return 14;

            case 102: // 'f'
                return 15;

            case 58: // ':'
            case 59: // ';'
            case 60: // '<'
            case 61: // '='
            case 62: // '>'
            case 63: // '?'
            case 64: // '@'
            case 65: // 'A'
                return 10;

            case 66: // 'B'
                return 11;

            case 67: // 'C'
                return 12;

            case 68: // 'D'
                return 13;

            case 69: // 'E'
                return 14;

            case 70: // 'F'
                return 15;

            case 71: // 'G'
            case 72: // 'H'
            case 73: // 'I'
            case 74: // 'J'
            case 75: // 'K'
            case 76: // 'L'
            case 77: // 'M'
            case 78: // 'N'
            case 79: // 'O'
            case 80: // 'P'
            case 81: // 'Q'
            case 82: // 'R'
            case 83: // 'S'
            case 84: // 'T'
            case 85: // 'U'
            case 86: // 'V'
            case 87: // 'W'
            case 88: // 'X'
            case 89: // 'Y'
            case 90: // 'Z'
            case 91: // '['
            case 92: // '\\'
            case 93: // ']'
            case 94: // '^'
            case 95: // '_'
            case 96: // '`'
            default:
                return 0;
        }
    }
}
