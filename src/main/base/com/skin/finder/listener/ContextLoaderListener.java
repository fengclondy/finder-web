/*
 * $RCSfile: ContextLoaderListener.java,v $$
 * $Revision: 1.1  $
 * $Date: 2009-11-16  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.listener;

import java.lang.reflect.Method;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.config.SiteConfig;
import com.skin.finder.jstl.util.FileType;
import com.skin.util.ClassUtil;

/**
 * <p>Title: ContextLoaderListener</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ContextLoaderListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ContextLoaderListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        SiteConfig siteConfig = SiteConfig.getInstance();
        servletContext.setAttribute("siteConfig", siteConfig);
        servletContext.setAttribute("FileType",   new FileType());
        this.build();
    }

    /**
     *
     */
    public void build() {
        try {
            Object[] arguments = null;
            Class<?> clazz = ClassUtil.getClass("org.quartz.SchedulerFactory");
            clazz = ClassUtil.getClass("com.skin.finder.schedule.ScheduleBuilder");
            Method method = clazz.getDeclaredMethod("build", new Class<?>[0]);
            method.invoke(null, arguments);
        }
        catch(ClassNotFoundException e) {
            logger.warn("java.lang.ClassNotFoundException: " + e.getMessage());
        }
        catch(Exception e) {
            logger.warn("com.skin.finder.schedule: error.");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
