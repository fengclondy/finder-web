/*
 * $RCSfile: Password.java,v $
 * $Revision: 1.1 $
 * $Date: 2014-3-24 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

/**
 * <p>Title: Password</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @version 1.0
 */
public class Password {
    /**
     * @param size
     * @return String
     */
    public static String salt(int size) {
        return Hex.encode(Digest.salt(size));
    }

    /**
     * @param length
     * @return String
     */
    public static String random(int length) {
        char[] cbuf = new char[] {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                'u', 'v', 'w', 'x', 'y', 'z',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                'U', 'V', 'W', 'X', 'Y', 'Z',
                '!', '@', '#', '$', '%', '&', '*', '+', '?', '='
        };

        char[] chars = new char[length];
        SecureRandom secureRandom = new SecureRandom();

        for(int i = 0; i < length; i++) {
            chars[i] = cbuf[secureRandom.nextInt(cbuf.length)];
        }
        return new String(chars);
    }

    /**
     * @param password
     * @param salt
     * @return String
     */
    public static String encode(String password, String salt) {
        try {
            byte[] bytes = Hex.decode(salt);
            return Hex.encode(Digest.sha1(password.getBytes("utf-8"), bytes));
        }
        catch(UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param source
     * @return String
     */
    public static String md5(String source) {
        try {
            return Hex.encode(Digest.md5(source.getBytes("UTF-8"), null));
        }
        catch(UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
