/*
 * $RCSfile: JspTest.java,v $
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/**
 * <p>Title: JspTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class JspTest {
    Object dispatcher;

    /**
     * @version 1.0
     */
    public static class BootstrapClassLoader extends URLClassLoader {
        /**
         * @param urls
         */
        public BootstrapClassLoader(URL[] urls) {
            super(urls);
        }

        /**
         * @param urls
         * @param parent
         */
        public BootstrapClassLoader(URL[] urls, ClassLoader parent) {
            super(urls, parent);
        }

        /**
         * @param parent
         * @param lib
         * @return HotBootClassLoader
         */
        public static BootstrapClassLoader getClassLoader(final ClassLoader parent, final File lib) {
            URL[] urls = getRepositories(lib.listFiles());
            return getClassLoader(parent, urls);
        }

        /**
         * @param parent
         * @param repositories
         * @return HotBootClassLoader
         */
        public static BootstrapClassLoader getClassLoader(final ClassLoader parent, final URL[] repositories) {
            ClassLoader classLoader = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
                @Override
                public ClassLoader run() {
                    if(parent == null) {
                        return new BootstrapClassLoader(repositories);
                    }
                    else {
                        return new BootstrapClassLoader(repositories, parent);
                    }
                }
            });
            return (BootstrapClassLoader)classLoader;
        }

        /**
         * @param parent
         * @param repositories
         * @return ClassLoader
         */
        public static ClassLoader getClassLoader(final ClassLoader parent, final List<URL> repositories) {
            URL[] urls = new URL[repositories.size()];
            repositories.toArray(urls);
            return getClassLoader(parent, urls);
        }

        /**
         * @param files
         * @return URL[]
         */
        public static URL[] getRepositories(File[] files) {
            Set<URL> set = new LinkedHashSet<URL>();

            if(files != null) {
                for(File file : files) {
                    String fileName = file.getName().toLowerCase(Locale.ENGLISH);

                    if(fileName.endsWith(".jar") || fileName.endsWith(".zip")) {
                        try {
                            set.add(file.toURI().toURL());
                        }
                        catch(IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            URL[] urls = new URL[set.size()];
            set.toArray(urls);
            return urls;
        }

        /**
         * @param urls
         * @param files
         * @return URL[]
         */
        public static List<URL> addRepositories(List<URL> urls, File[] files) {
            Set<URL> set = new LinkedHashSet<URL>();

            if(files != null) {
                for(File file : files) {
                    String fileName = file.getName().toLowerCase(Locale.ENGLISH);

                    if(fileName.endsWith(".jar") || fileName.endsWith(".zip")) {
                        try {
                            set.add(file.toURI().toURL());
                        }
                        catch(IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            urls.addAll(set);
            return urls;
        }
    }

    /**
     * @param path
     * @return ClassLoader
     * @throws Exception
     */
    protected ClassLoader getClassLoader(String path) throws Exception {
        List<URL> repositories = new ArrayList<URL>();
        repositories.add(new File(path).toURI().toURL());

        ClassLoader parent = this.getClass().getClassLoader();
        return BootstrapClassLoader.getClassLoader(parent, repositories);
    }

    /**
     * @param classLoader
     * @param className
     * @param parameterTypes
     * @param args
     * @return Object
     * @throws Exception
     */
    protected static Object getInstance(ClassLoader classLoader, String className) throws Exception {
        return classLoader.loadClass(className).newInstance();
    }

    /**
     * @param object
     * @param name
     * @param types
     * @param args
     * @throws Exception
     */
    protected static void invoke(Object object, String name, Class<?>[] types, Object[] args) throws Exception {
        Method method = object.getClass().getMethod(name, types);
        method.invoke(object, args);
    }

    /**
     * @param workspace
     * @param name
     * @param location
     * @throws Exception
     */
    protected static void addWorkspace(ClassLoader classLoader, String name, String location) throws Exception {
        Class<?> type = classLoader.loadClass("dev.text.WorkspaceManager");
        Method method = type.getMethod("add", new Class<?>[]{String.class, String.class});
        method.invoke(null, new Object[]{name, location});
    }

    /**
     * @param request
     * @param response
     */
    protected void reload(HttpServletRequest request, HttpServletResponse response) {
        if(this.dispatcher != null) {
            try {
                invoke(this.dispatcher, "destroy", null, null);
            }
            catch (Exception e) {
            }
            this.dispatcher = null;
        }
        response.setStatus(304);
        response.setHeader("Location", request.getRequestURI());
    }

    /**
     * @return boolean
     */
    protected static boolean getTrue() {
        return true;
    }

    /**
     * @param request
     * @param response
     * @throws ServletException 
     * @throws IOException 
     */
    @SuppressWarnings("resource")
    public void test(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PageContext pageContext = this.getPageContext();
        ServletContext application = this.getServletContext();
        JspWriter out = this.getJspWriter();

        try {
            response.resetBuffer();
            String action = request.getParameter("action");

            if(action != null && action.equals("reload")) {
                this.reload(request, response);
                return;
            }

            if(this.dispatcher == null) {
                // String jar = application.getRealPath("/WEB-INF/mydata/finder-web-2.0.0.jar");
                ClassLoader classLoader = this.getClassLoader("/tmp/finder-web-2.0.0.jar");
                Thread.currentThread().setContextClassLoader(classLoader);

                this.dispatcher = getInstance(classLoader, "com.skin.finder.web.ActionDispatcher");
                invoke(this.dispatcher, "setPackages", new Class<?>[]{String[].class}, new Object[]{new String[]{
                    "com.skin.finder.servlet"
                }});
                invoke(this.dispatcher, "init", new Class<?>[]{ServletContext.class}, new Object[]{application});
            }

            invoke(this.dispatcher, "service",
                    new Class<?>[]{HttpServletRequest.class, HttpServletResponse.class},
                    new Object[]{request, response});
        }
        catch(Throwable t) {
            throw new ServletException(t);
        }
        finally {
            response.flushBuffer();
            out.clear();
            out = pageContext.pushBody();
        }

        if(getTrue()) {
            return;
        }
    }

    /**
     * @return PageContext
     */
    private PageContext getPageContext() {
        return null;
    }

    /**
     * @return ServletContext
     */
    private ServletContext getServletContext() {
        return null;
    }

    /**
     * @return JspWriter
     */
    private JspWriter getJspWriter() {
        return null;
    }
}
