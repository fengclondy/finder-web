/*
 * $RCSfile: RandomUtil.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-5-6 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import java.util.Random;

/**
 * <p>Title: RandomUtil</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class RandomUtil {
    private static final char[] DEFAULT_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * @param min
     * @param max
     * @return String
     */
    public static String getRandString(int min, int max) {
        return getRandString(DEFAULT_CHARS, min, max);
    }

    /**
     * @param cbuf
     * @param min
     * @param max
     * @return String
     */
    public static String getRandString(char[] cbuf, int min, int max) {
        int length = min;
        Random random = new Random();
        length = min + random.nextInt(max - min + 1);
        length = Math.max(length, min);
        char[] chars = new char[length];

        for(int i = 0; i < length; i++) {
            chars[i] = cbuf[random.nextInt(16)];
        }
        return new String(chars);
    }
}
