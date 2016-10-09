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
     * generate log
     */
    public static void generate() {
        for(int i = 0; i < 300; i++) {
            if(i < 10) {
                logger.info("0000" + i + " 012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
            }
            else if(i < 100) {
                logger.info("000" + i + " 012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
            }
            else {
                logger.info("00" + i + " 012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
            }
        }
    }
}
