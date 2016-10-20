/*
 * $RCSfile: LogGenerator.java,v $$
 * $Revision: 1.1 $
 * $Date: 2016-10-9 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: LogGenerator</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class LogGenerator {
    private static final Logger logger = LoggerFactory.getLogger(LogGenerator.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
        LogGenerator.generate(new File("D:\\opt\\resin\\log\\test.log"), 20L * 1024L * 1024L);
    }

    /**
     * generate log
     */
    public static void generate() {
        Exception e = new Exception("log generator...");

        for(int i = 0; i < 20; i++) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * @param file
     * @param size
     */
    public static void generate(File file, long size) {
        long id = 1L;
        long count = 0L;
        OutputStream outputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            StringBuilder buffer = new StringBuilder();

            while(count < size) {
                if(id < 10L) {
                    buffer.append("00000");
                    buffer.append(id);
                }
                else if(id < 100L) {
                    buffer.append("0000");
                    buffer.append(id);
                }
                else if(id < 1000L) {
                    buffer.append("000");
                    buffer.append(id);
                }
                else if(id < 10000L) {
                    buffer.append("00");
                    buffer.append(id);
                }
                else if(id < 100000L) {
                    buffer.append("0");
                    buffer.append(id);
                }
                else {
                    buffer.append(id);
                }

                buffer.append(" 012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789\r\n");
                byte[] bytes = buffer.toString().getBytes();
                outputStream.write(bytes);
                buffer.setLength(0);
                count += bytes.length;
                id++;
            }
            bufferedOutputStream.flush();
            outputStream.flush();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
