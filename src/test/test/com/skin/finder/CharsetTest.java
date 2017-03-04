/*
 * $RCSfile: CharsetTest.java,v $$
 * $Revision: 1.1 $
 * $Date: 2016-10-7 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package test.com.skin.finder;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>Title: CharsetTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class CharsetTest {
    /**
     * @param args
     */
    public static void main(String[] args) {
        printAllCharset();
    }

    /**
     * 
     */
    public static void test1() {
        StringBuilder buffer = new StringBuilder();
        Map<String, Charset> map = Charset.availableCharsets();

        for(Map.Entry<String, Charset> entry : map.entrySet()) {
            Charset charset = entry.getValue();
            buffer.append("\"").append(charset.name()).append("\", ");
        }
        System.out.println(buffer.toString());
    }

    /**
     * 
     */
    public static void printAllCharset() {
        int i = 0;
        StringBuilder buffer = new StringBuilder("var Charset = [\r\n    ");
        Map<String, Charset> map = Charset.availableCharsets();
        Iterator<Map.Entry<String, Charset>> iterator = map.entrySet().iterator();

        while(iterator.hasNext()) {
            i++;
            Map.Entry<String, Charset> entry = iterator.next();
            Charset charset = entry.getValue();

            if(i % 8 == 0) {
                if(iterator.hasNext()) {
                    buffer.append("\"").append(charset.name()).append("\",\r\n    ");
                }
                else {
                    buffer.append("\"").append(charset.name()).append("\"\r\n    ");
                }
            }
            else {
                if(iterator.hasNext()) {
                    buffer.append("\"").append(charset.name()).append("\", ");
                }
                else {
                    buffer.append("\"").append(charset.name()).append("\"\r\n");
                }
            }
        }
        buffer.append("];\r\n");
        System.out.println(buffer.toString());
    }
}
