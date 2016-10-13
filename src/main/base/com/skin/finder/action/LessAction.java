/*
 * $RCSfile: LessAction.java,v $$
 * $Revision: 1.1 $
 * $Date: 2016-10-02 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;

import com.skin.finder.FinderManager;
import com.skin.finder.config.Workspace;
import com.skin.finder.servlet.LessServlet;
import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.annotation.UrlPattern;

/**
 * <p>Title: LessAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class LessAction extends BaseAction {
    protected static LessServlet lessServlet = new LessServlet();

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/less.html")
    public void less() throws ServletException, IOException {
        this.execute("/template/finder/less.jsp");
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/tail.html")
    public void tail() throws ServletException, IOException {
        this.execute("/template/finder/tail.jsp");
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/getRange.html")
    public void getRange() throws ServletException, IOException {
        File file = this.getFile();
        lessServlet.getRange(this.request, this.response, file);
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/getTail.html")
    public void getTail() throws ServletException, IOException {
        File file = this.getFile();
        lessServlet.getTail(this.getRequest(), this.getResponse(), file);
    }

    /**
     * @param page
     * @throws ServletException
     * @throws IOException
     */
    public void execute(String page) throws ServletException, IOException {
        String workspace = this.request.getParameter("workspace");
        String path = this.request.getParameter("path");
        String home = this.getWorkspace(workspace);
        String charset = this.getTrimString("charset", "utf-8");
        FinderManager finderManager = new FinderManager(home);
        String realPath = finderManager.getRealPath(path);

        if(realPath == null) {
            throw new ServletException("Can't access !");
        }

        File file = new File(realPath);
        String parent = finderManager.getRelativePath(file.getParent());
        String temp = realPath.substring(finderManager.getWork().length()).replace('\\', '/');

        this.setAttribute("workspace", workspace);
        this.setAttribute("work", finderManager.getWork());
        this.setAttribute("path", temp);
        this.setAttribute("parent", parent);
        this.setAttribute("charset", charset);
        this.setAttribute("absolutePath", file.getCanonicalPath());
        this.forward(page);
    }

    /**
     * @return File
     * @throws ServletException
     */
    public File getFile() throws ServletException {
        String workspace = this.request.getParameter("workspace");
        String path = this.request.getParameter("path");
        String home = this.getWorkspace(workspace);
        FinderManager finderManager = new FinderManager(home);
        String realPath = finderManager.getRealPath(path);

        if(realPath == null) {
            throw new ServletException("Can't access !");
        }

        String filePath = this.getFilePath(realPath);
        return new File(filePath);
    }

    /**
     * @param name
     * @return String
     */
    public String getWorkspace(String name) {
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
            return this.getServletContext().getRealPath(work.substring(12));
        }
        throw new NullPointerException("work directory not exists: " + work);
    }

    /**
     * @param filePath
     * @return String
     */
    public String getFilePath(String filePath) {
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
}
