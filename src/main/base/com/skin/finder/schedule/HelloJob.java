/*
 * $RCSfile: SimpleJob.java,v $$
 * $Revision: 1.1  $
 * $Date: 2012-11-1  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: HelloJob</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class HelloJob {
    private static final Logger logger = LoggerFactory.getLogger(HelloJob.class);

    /**
     * @param properties
     */
    public void execute(String properties) {
        if(logger.isInfoEnabled()) {
            logger.info("Hello: {}", properties);
        }
    }
}
