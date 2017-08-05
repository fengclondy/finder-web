/*
 * $RCSfile: Base64.java,v $$
 * $Revision: 1.1  $
 * $Date: 2011-6-2  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import java.io.UnsupportedEncodingException;

/**
 * <p>Title: Base64</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Base64 {
    private static final byte[] ENCODE_TABLE = {
        (byte)'A', (byte)'B', (byte)'C', (byte)'D', (byte)'E', (byte)'F', (byte)'G', (byte)'H', (byte)'I', (byte)'J', (byte)'K',
        (byte)'L', (byte)'M', (byte)'N', (byte)'O', (byte)'P', (byte)'Q', (byte)'R', (byte)'S', (byte)'T', (byte)'U', (byte)'V',
        (byte)'W', (byte)'X', (byte)'Y', (byte)'Z', (byte)'a', (byte)'b', (byte)'c', (byte)'d', (byte)'e', (byte)'f', (byte)'g',
        (byte)'h', (byte)'i', (byte)'j', (byte)'k', (byte)'l', (byte)'m', (byte)'n', (byte)'o', (byte)'p', (byte)'q', (byte)'r',
        (byte)'s', (byte)'t', (byte)'u', (byte)'v', (byte)'w', (byte)'x', (byte)'y', (byte)'z', (byte)'0', (byte)'1', (byte)'2',
        (byte)'3', (byte)'4', (byte)'5', (byte)'6', (byte)'7', (byte)'8', (byte)'9', (byte)'+', (byte)'/'
    };

    private static final byte[] DECODE_TABLE = {
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
        52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
        -1,  0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14,
        15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
        -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
        41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1
    };

    /**
     * @param data
     * @return byte[]
     */
    public static byte[] encode(byte[] data) {
        int b1 = 0;
        int b2 = 0;
        int b3 = 0;

        int modulus = data.length % 3;
        int length = data.length - modulus;
        byte[] bytes = new byte[(data.length + 2) / 3 * 4];

        for(int i = 0, j = 0; i < length;) {
            b1 = data[i++] & 0xff;
            b2 = data[i++] & 0xff;
            b3 = data[i++] & 0xff;

            bytes[j++] = ENCODE_TABLE[((b1 >>> 2) & 0x3f)];
            bytes[j++] = ENCODE_TABLE[((b1 << 4) | (b2 >>> 4)) & 0x3f];
            bytes[j++] = ENCODE_TABLE[((b2 << 2) | (b3 >>> 6)) & 0x3f];
            bytes[j++] = ENCODE_TABLE[(b3 & 0x3f)];
        }

        int b4 = 0;
        int b5 = 0;

        if(modulus == 1) {
            b4 = data[data.length - 1] & 0xff;
            b1 = (b4 >>> 2) & 0x3f;
            b2 = (b4 << 4) & 0x3f;
            bytes[bytes.length - 4] = ENCODE_TABLE[b1];
            bytes[bytes.length - 3] = ENCODE_TABLE[b2];
            bytes[bytes.length - 2] = 61;
            bytes[bytes.length - 1] = 61;
        }
        else if(modulus == 2) {
            b4 = data[data.length - 2] & 0xff;
            b5 = data[data.length - 1] & 0xff;
            b1 = ((b4 >>> 2) & 0x3f);
            b2 = ((b4 << 4) | (b5 >>> 4)) & 0x3f;
            b3 = ((b5 << 2) & 0x3f);
            bytes[bytes.length - 4] = ENCODE_TABLE[b1];
            bytes[bytes.length - 3] = ENCODE_TABLE[b2];
            bytes[bytes.length - 2] = ENCODE_TABLE[b3];
            bytes[bytes.length - 1] = 61;
        }

        return bytes;
    }

    /**
     * @param data
     * @return byte[]
     */
    public static byte[] decode(byte[] data) {
        byte b1 = 0;
        byte b2 = 0;
        byte b3 = 0;
        byte b4 = 0;
        byte[] bytes;

        if(data[data.length - 2] == 61) {
            bytes = new byte[(((data.length / 4) - 1) * 3) + 1];
        }
        else if(data[data.length - 1] == 61) {
            bytes = new byte[(((data.length / 4) - 1) * 3) + 2];
        }
        else {
            bytes = new byte[((data.length / 4) * 3)];
        }

        for(int i = 0, j = 0, length = data.length - 4; i < length;) {
            b1 = DECODE_TABLE[data[i++]];
            b2 = DECODE_TABLE[data[i++]];
            b3 = DECODE_TABLE[data[i++]];
            b4 = DECODE_TABLE[data[i++]];

            bytes[j++] = (byte)((b1 << 2) | (b2 >> 4));
            bytes[j++] = (byte)((b2 << 4) | (b3 >> 2));
            bytes[j++] = (byte)((b3 << 6) | b4);
        }

        if(data[data.length - 2] == 61) {
            b1 = DECODE_TABLE[data[data.length - 4]];
            b2 = DECODE_TABLE[data[data.length - 3]];
            bytes[bytes.length - 1] = (byte)((b1 << 2) | (b2 >> 4));
        }
        else if(data[data.length - 1] == 61) {
            b1 = DECODE_TABLE[data[data.length - 4]];
            b2 = DECODE_TABLE[data[data.length - 3]];
            b3 = DECODE_TABLE[data[data.length - 2]];
            bytes[bytes.length - 2] = (byte)((b1 << 2) | (b2 >> 4));
            bytes[bytes.length - 1] = (byte)((b2 << 4) | (b3 >> 2));
        }
        else {
            b1 = DECODE_TABLE[data[data.length - 4]];
            b2 = DECODE_TABLE[data[data.length - 3]];
            b3 = DECODE_TABLE[data[data.length - 2]];
            b4 = DECODE_TABLE[data[data.length - 1]];
            bytes[bytes.length - 3] = (byte)((b1 << 2) | (b2 >> 4));
            bytes[bytes.length - 2] = (byte)((b2 << 4) | (b3 >> 2));
            bytes[bytes.length - 1] = (byte)((b3 << 6) | b4);
        }

        return bytes;
    }

    /**
     * @param data
     * @param encoding
     * @return String
     */
    public static String encode(String data, String encoding) {
        try {
            byte[] bytes = encode(data.getBytes(encoding));
            return new String(bytes, encoding);
        }
        catch(UnsupportedEncodingException e) {
        }

        return null;
    }

    /**
     * @param data
     * @param encoding
     * @return String
     */
    public static String decode(String data, String encoding) {
        try {
            return new String(decode(data.getBytes(encoding)), encoding);
        }
        catch(UnsupportedEncodingException e) {
        }

        return null;
    }

    /**
     * @param b
     * @return boolean
     */
    protected static boolean validate(byte b) {
        if(b == 61) {
            return true;
        }
        else if((b < 0) || (b >= 128)) {
            return false;
        }
        else if(DECODE_TABLE[b] == -1) {
            return false;
        }

        return true;
    }
}
