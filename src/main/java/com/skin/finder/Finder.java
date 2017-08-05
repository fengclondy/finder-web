/*
 * $RCSfile: Finder.java,v $
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.skin.finder.util.Path;

/**
 * <p>Title: Finder</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Finder {
    /**
     * servletContext
     */
    public static final String SERVLETCONTEXT = "servletContext";

    /**
     * @param request
     * @return File
     * @throws ServletException
     */
    public static File getFile(HttpServletRequest request) throws ServletException {
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String work = Finder.getWorkspace(request, workspace);
        String realPath = Finder.getRealPath(work, path);

        if(realPath == null) {
            throw new ServletException("Can't access !");
        }
        return new File(realPath);
    }

    /**
     * @param parent
     * @param path
     * @return String
     */
    public static String getRealPath(String parent, String path) {
        String temp = null;

        if(path != null) {
            temp = Path.getStrictPath(path).trim();
        }
        else {
            temp = "/";
        }

        if(temp.length() < 1 || temp.equals("/")) {
            return parent;
        }

        String work = Path.join(parent, "");
        String full = Path.join(parent, temp);

        if(full.startsWith(work)) {
            return full;
        }
        else {
            return null;
        }
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

        String work = WorkspaceManager.getWork(name);

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
     * @param dir
     * @return List<FileItem>
     */
    public static List<FileItem> list(File dir) {
        File[] fileList = dir.listFiles();
        List<FileItem> fileItemList = new ArrayList<FileItem>();

        if(fileList != null) {
            if(System.getProperty("os.name").indexOf("Windows") < 0) {
                Arrays.sort(fileList, FileComparator.getInstance());
            }

            for(File file : fileList) {
                if(!file.isFile()) {
                    FileItem fileItem = new FileItem();
                    fileItem.setFileName(file.getName());
                    fileItem.setFileType(null);
                    fileItem.setFileIcon("folder");
                    fileItem.setFileSize(0L);
                    fileItem.setLastModified(file.lastModified());
                    fileItem.setIsFile(false);
                    fileItemList.add(fileItem);
                }
            }

            for(File file : fileList) {
                if(file.isFile()) {
                    FileItem fileItem = new FileItem();
                    fileItem.setFileName(file.getName());
                    fileItem.setFileIcon(FileType.getIcon(file.getName()));
                    fileItem.setFileType(FileType.getType(file.getName()));
                    fileItem.setFileSize(file.length());
                    fileItem.setLastModified(file.lastModified());
                    fileItem.setIsFile(true);
                    fileItemList.add(fileItem);
                }
            }
        }
        return fileItemList;
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

    /**
     * @param request
     * @return ServletContext
     */
    public static ServletContext getServletContext(HttpServletRequest request) {
        return (ServletContext)(request.getAttribute(SERVLETCONTEXT));
    }
}
