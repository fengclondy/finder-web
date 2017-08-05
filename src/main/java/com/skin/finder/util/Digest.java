/*
 * $RCSfile: Digest.java,v $
 * $Revision: 1.1 $
 * $Date: 2014-3-24 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * <p>Title: Digest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @version 1.0
 */
public class Digest {
    /**
     * @param input
     * @return byte[]
     */
    public static byte[] sha1(byte[] input) {
        return digest("SHA-1", input, null);
    }

    /**
     * @param input
     * @param salt
     * @return byte[]
     */
    public static byte[] sha1(byte[] input, byte[] salt) {
        return digest("SHA-1", input, salt);
    }

    /**
     * @param input
     * @param salt
     * @return byte[]
     */
    public static byte[] md5(byte[] input, byte[] salt) {
        return digest("MD5", input, salt);
    }

    /**
     * @param input
     * @param algorithm
     * @param salt
     * @return byte[]
     */
    public static byte[] digest(String algorithm, byte[] input, byte[] salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);

            if(salt != null) {
                digest.update(salt);
            }
            return digest.digest(input);
        }
        catch(GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param size
     * @return byte[]
     */
    public static byte[] salt(int size) {
        byte[] bytes = new byte[size];
        new SecureRandom().nextBytes(bytes);
        return bytes;
    }
}
