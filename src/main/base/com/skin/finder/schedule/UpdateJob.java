/*
 * $RCSfile: UpdateJob.java,v $$
 * $Revision: 1.1 $
 * $Date: 2014-10-20 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.schedule;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.license.License;
import com.skin.license.LicenseFactory;
import com.skin.util.ClassUtil;

/**
 * <p>Title: UpdateJob</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class UpdateJob {
    private static final Logger logger = LoggerFactory.getLogger(UpdateJob.class);

    /**
     * @param bind
     */
    public void execute(String bind) {
        License license = LicenseFactory.getInstance();

        if(LicenseFactory.validate(license)) {
            return;
        }

        Class<?> clazz = null;

        try {
            clazz = ClassUtil.getClass("com.skin.spider.SpiderService");
        }
        catch(ClassNotFoundException e) {
            logger.info("com.skin.spider.SpiderService not exists.");
        }

        if(clazz != null) {
            try {
                Method method = clazz.getMethod("getSpiderService", new Class<?>[0]);
                Object object = method.invoke(null, new Object[0]);
                object.getClass().getMethod("destroy", new Class<?>[0]).invoke(object, new Object[0]);
            }
            catch(Exception e) {
                logger.error(e.getMessage());
            }
        }
    }
}
