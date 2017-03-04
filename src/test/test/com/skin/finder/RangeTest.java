/*
 * $RCSfile: RangeTest.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-1-6 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package test.com.skin.finder;

import com.skin.finder.Range;

/**
 * <p>Title: RangeTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class RangeTest {
    /**
     * @param args
     */
    public static void main(String[] args) {
        test("bytes=0-", 10L);
        test("bytes=0-4", 10L);
        test("bytes=-4", 10L);
    }

    /**
     * @param header
     * @param length
     */
    public static void test(String header, long length) {
        Range range = Range.parse(header, length);
        System.out.println("====================");
        System.out.println("header: " + header);
        System.out.println("requestRange: " + range.toString());
        System.out.println("contentRange: " + range.getContentRange());
    }
}
