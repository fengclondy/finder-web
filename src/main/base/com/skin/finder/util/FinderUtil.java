/*
 * $RCSfile: FinderUtil.java,v $$
 * $Revision: 1.1 $
 * $Date: 2016-10-02 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.skin.finder.FinderManager;
import com.skin.finder.config.Workspace;

/**
 * <p>Title: FinderUtil</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class FinderUtil {
    private static ServletContext servletContext;

    /**
     * @param servletContext
     */
    public static void setServletContext(ServletContext servletContext) {
        FinderUtil.servletContext = servletContext;
    }

    /**
     * @return ServletContext
     */
    public static ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * @param request
     * @return ServletContext
     */
    public static ServletContext getServletContext(HttpServletRequest request) {
        if(servletContext != null) {
            return servletContext;
        }
        return (ServletContext)(request.getAttribute("servletContext"));
    }

    /**
     * @param request
     * @param name
     * @return String
     */
    public static String getWorkspace(HttpServletRequest request, String name) {
        if(name == null) {
            throw new NullPointerException("workspace must be not null !");
        }

        Workspace workspace = Workspace.getInstance();
        String work = workspace.getValue(name.trim());

        if(work == null) {
            throw new NullPointerException("[" + name + "] workspace not exists !");
        }

        if(work.startsWith("file:")) {
            return new File(work.substring(5)).getAbsolutePath();
        }

        if(work.startsWith("contextPath:")) {
            ServletContext servletContext = getServletContext(request);
            return servletContext.getRealPath(work.substring(12));
        }
        throw new NullPointerException("work directory not exists: " + work);
    }

    /**
     * @param request
     * @return File
     * @throws ServletException
     */
    public static File getFile(HttpServletRequest request) throws ServletException {
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String home = FinderUtil.getWorkspace(request, workspace);
        FinderManager finderManager = new FinderManager(home);
        String realPath = finderManager.getRealPath(path);

        if(realPath == null) {
            throw new ServletException("Can't access !");
        }

        String filePath = FinderUtil.getFilePath(realPath);
        return new File(filePath);
    }

    /**
     * @param filePath
     * @return String
     */
    public static String getFilePath(String filePath) {
        if(filePath.endsWith(".link.tail")) {
            InputStream inputStream = null;

            try {
                byte[] buffer = new byte[1024];
                inputStream = new FileInputStream(filePath);
                int length = inputStream.read(buffer);
                String realPath = new String(buffer, 0, length, "utf-8");

                if(realPath.trim().length() > 0) {
                    return realPath;
                }
                else {
                    return filePath;
                }
            }
            catch(Exception e) {
            }
            finally {
                if(inputStream != null) {
                    try {
                        inputStream.close();
                    }
                    catch (IOException e) {
                    }
                }
            }
            return filePath;
        }
        else {
            return filePath;
        }
    }

    /**
     * @param dir
     * @param name
     * @return File
     * @throws IOException 
     */
    public static File getFile(File dir, String name) throws IOException {
        String prefix = null;
        String extension = null;
        int k = name.lastIndexOf('.');

        if(k > -1) {
            prefix = name.substring(0, k);
            extension = name.substring(k);
        }
        else {
            prefix = name;
            extension = "";
        }

        int i = 1;
        int count = 0;
        File file = new File(dir, name);

        if(!file.exists()) {
            file.createNewFile();
            return file;
        }

        while((count < 1000000)) {
            file = new File(dir, prefix + "(" + i + ")" + extension);
            i++;

            if(!file.exists()) {
                file.createNewFile();
                return file;
            }
        }
        return null;
    }
}
