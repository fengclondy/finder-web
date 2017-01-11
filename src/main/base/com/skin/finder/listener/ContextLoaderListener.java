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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.config.SiteConfig;
import com.skin.finder.jstl.util.FileType;
import com.skin.finder.util.FinderUtil;
import com.skin.util.ClassUtil;
import com.skin.util.ZipUtil;

/**
 * <p>Title: ContextLoaderListener</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ContextLoaderListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ContextLoaderListener.class);

    /**
     * @param servletContextEvent
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        FinderUtil.setServletContext(servletContext);
        SiteConfig siteConfig = SiteConfig.getInstance();
        servletContext.setAttribute("siteConfig", siteConfig);
        servletContext.setAttribute("FileType",   new FileType());
        this.build(servletContext);
        this.unzip(servletContext);
    }

    /**
     * build schedule
     * @param servletContext
     */
    protected void build(ServletContext servletContext) {
        logger.info("build shcedule.");

        try {
            Object[] arguments = null;
            Class<?> clazz = ClassUtil.getClass("org.quartz.SchedulerFactory");
            clazz = ClassUtil.getClass("com.skin.finder.schedule.ScheduleBuilder");
            Method method = clazz.getDeclaredMethod("build", new Class<?>[0]);
            method.invoke(null, arguments);
        }
        catch(Exception e) {
            logger.warn("com.skin.finder.schedule: {}", e.getMessage());
        }
    }

    /**
     * unzip resource
     * @param servletContext
     */
    protected void unzip(ServletContext servletContext) {
        File file = this.getResource(servletContext.getRealPath("/WEB-INF/lib"));

        if(file != null) {
            logger.info("unzip: {}", file.getAbsolutePath());
            File webapp = new File(servletContext.getRealPath("/"));

            try {
                ZipUtil.unzip(file, webapp);
            }
            catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * @param lib
     * @return File
     */
    public File getResource(String lib) {
        File[] files = new File(lib).listFiles();

        for(File file : files) {
            String name = file.getName().toLowerCase();

            if(name.startsWith("finder-res-") && name.endsWith(".jar")) {
                return file;
            }
        }
        return null;
    }

    /**
     * @param servletContextEvent
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
