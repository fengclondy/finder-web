/*
 * $RCSfile: ActionDispatcher.java,v $
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: ActionDispatcher</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ActionDispatcher {
    private String[] packages = null;
    private ClassLoader classLoader = null;
    private ServletContext servletContext = null;
    private ActionContext actionContext;
    private static final String SERVLETCONTEXT = "servletContext";
    private static final Class<HttpServlet> SERVLET = HttpServlet.class;
    private static final Logger logger = LoggerFactory.getLogger(ActionDispatcher.class);

    /**
     * default
     */
    public ActionDispatcher() {
    }

    /**
     * @param servletContext
     * @throws ServletException
     */
    public void init(ServletContext servletContext) throws ServletException {
        this.servletContext = servletContext;
        this.actionContext = new ActionContext(servletContext);

        try {
            this.scan();
        }
        catch(ServletException e) {
            throw e;
        }
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String requestURI = request.getRequestURI();
        String action = request.getParameter("action");
        request.setAttribute(SERVLETCONTEXT, this.getServletContext());
        request.setAttribute("requestURI", requestURI);
        request.setAttribute("contextPath", this.getContextPath(request));

        if(action == null) {
            action = "index";
        }

        boolean b = this.dispatch(request, response, action);

        if(!b) {
            response.sendError(404);
        }
    }

    /**
     * @param request
     * @param response
     * @param action
     * @return boolean
     * @throws ServletException
     * @throws IOException
     */
    public boolean dispatch(HttpServletRequest request, HttpServletResponse response, String action) throws ServletException, IOException {
        String requestURI = this.getStrictPath(request.getRequestURI());
        request.setAttribute("ActionDispatcher$requestURI", requestURI);
        request.setAttribute("servletContext", this.servletContext);
        Method method = this.actionContext.getMethod(action);
        logger.debug("{}: {}", action, method);

        if(method == null) {
            logger.debug("bad request - action: {}, requestURI: {}", action, requestURI);
            return false;
        }

        Throwable throwable = null;

        try {
            HttpServlet servlet = this.actionContext.getServlet(method);
            method.invoke(servlet, new Object[]{request, response});
        }
        catch(Throwable t) {
            throwable = t;
            logger.error(t.getMessage(), t);
        }

        if(throwable != null) {
            Throwable t = throwable.getCause();

            if(t != null) {
                throwable = t;
            }

            if(throwable instanceof RuntimeException) {
                throw (RuntimeException)throwable;
            }
            else if(throwable instanceof ServletException) {
                throw (ServletException)throwable;
            }
            else {
                throw new ServletException(throwable);
            }
        }
        return true;
    }

    /**
     * @param request
     * @return String
     */
    public String getContextPath(HttpServletRequest request) {
        String contextPath = request.getContextPath();

        if(contextPath == null || contextPath.length() <= 1) {
            return "";
        }
        return contextPath;
    }

    /**
     * @param packages
     */
    public void setPackages(String[] packages) {
        this.packages = packages;
    }

    /**
     * @return String
     */
    public String[] getPackages() {
        return this.packages;
    }

    /**
     * @return ServletContext
     */
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    /**
     * @param servletContext
     */
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * @return ClassLoader
     */
    public ClassLoader getClassLoader() {
        return (this.classLoader != null ? this.classLoader : Thread.currentThread().getContextClassLoader());
    }

    /**
     * @param classLoader
     */
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * @throws ServletException
     */
    public void scan() throws ServletException {
        if(this.packages != null) {
            for(int i = 0; i < this.packages.length; i++) {
                this.load(this.packages[i]);
            }
        }
    }

    /**
     * @param packageName
     */
    private void load(String packageName) throws ServletException {
        String path = packageName.replace('.', '/');
        java.util.Enumeration<URL> urls = null;

        try {
            urls = this.getClassLoader().getResources(path);
        }
        catch(IOException e) {
            return;
        }

        while(urls.hasMoreElements()) {
            String urlPath = urls.nextElement().getFile();

            try {
                urlPath = URLDecoder.decode(urlPath, "UTF-8");
            }
            catch(UnsupportedEncodingException e) {
                throw new ServletException(e);
            }

            if(urlPath.startsWith("file:")) {
                urlPath = urlPath.substring(5);
            }

            if(urlPath.indexOf('!') > 0) {
                urlPath = urlPath.substring(0, urlPath.indexOf('!'));
            }

            File file = new File(urlPath);

            if(file.isDirectory()) {
                loadFromDirectory(path, file);
            }
            else {
                loadFromJar(path, file);
            }
        }
    }

    /**
     * @param parent
     * @param location
     */
    private void loadFromDirectory(String parent, File location) throws ServletException {
        File files[] = location.listFiles();

        for(int i = 0, length = files.length; i < length; i++) {
            File file = files[i];
            StringBuilder buffer = new StringBuilder(100);
            buffer.append(parent).append("/").append(file.getName());
            String packageOrClass = parent != null ? buffer.toString() : file.getName();

            if(file.isDirectory()) {
                loadFromDirectory(packageOrClass, file);
                continue;
            }

            if(file.getName().endsWith(".class")) {
                this.add(packageOrClass);
            }
        }
    }

    /**
     * @param parent
     * @param jarfile
     */
    private void loadFromJar(String parent, File jarfile) throws ServletException {
        InputStream inputStream = null;
        JarInputStream jarInputStream = null;

        try {
            JarEntry entry = null;
            inputStream = new FileInputStream(jarfile);
            jarInputStream = new JarInputStream(new FileInputStream(jarfile));

            while((entry = jarInputStream.getNextJarEntry()) != null) {
                String name = entry.getName();

                if(!entry.isDirectory() && name.startsWith(parent) && name.endsWith(".class")) {
                    add(name);
                }
            }
        }
        catch(IOException e) {
            throw new ServletException(e);
        }
        finally {
            if(jarInputStream != null) {
                try {
                    jarInputStream.close();
                }
                catch(IOException e) {
                }
            }

            if(inputStream != null) {
                try {
                    inputStream.close();
                }
                catch(IOException e) {
                }
            }
        }
    }

    /**
     * @param fqn
     */
    private void add(String fqn) throws ServletException {
        String path = null;
        Class<?> type = null;
        String externalName = fqn.substring(0, fqn.indexOf('.')).replace('/', '.');

        try {
            type = getClassLoader().loadClass(externalName);
        }
        catch(ClassNotFoundException e) {
            throw new ServletException(e);
        }

        if(Modifier.isAbstract(type.getModifiers())) {
            return;
        }

        if(Modifier.isInterface(type.getModifiers())) {
            return;
        }

        if(!SERVLET.isAssignableFrom(type)) {
            return;
        }

        Namespace namespace = type.getAnnotation(Namespace.class);

        if(namespace != null) {
            path = namespace.value();
        }

        if(path == null) {
            path = "";
        }

        Method[] methods = type.getMethods();

        for(Method method : methods) {
            if(!Modifier.isPublic(method.getModifiers())) {
                continue;
            }

            UrlPattern urlPattern = method.getAnnotation(UrlPattern.class);

            if(urlPattern == null) {
                continue;
            }

            String[] values = urlPattern.value();

            if(values == null || values.length < 1) {
                continue;
            }

            for(String url : values) {
                Method old = this.actionContext.getMethod(url);

                if(old != null) {
                    Class<?> clazz = old.getDeclaringClass();

                    if(!externalName.equals(clazz.getName())) {
                        throw new ServletException("class: " + clazz.getName() + " - " + url + " already exists: " + type.getName());
                    }
                    else {
                        /**
                         * ignore
                         */
                    }
                }
                else {
                    this.actionContext.setAction(url, method);
                    logger.info("[{}]: {}.{}", url, type.getName(), method.getName());
                }
            }
        }
    }

    /**
     * @param path
     * @return String
     */
    public String getStrictPath(String path) {
        char c;
        StringBuilder buffer = new StringBuilder();

        for(int i = 0, length = path.length(); i < length; i++) {
            c = path.charAt(i);

            if(c == '\\' || c == '/') {
                if(buffer.length() < 1 || buffer.charAt(buffer.length() - 1) != '/') {
                    buffer.append("/");
                }
            }
            else {
                buffer.append(c);
            }
        }
        return buffer.toString();
    }

    /**
     * destroy
     */
    public void destroy() {
        this.packages = null;
        this.actionContext.clear();
        this.actionContext = null;
    }
}
