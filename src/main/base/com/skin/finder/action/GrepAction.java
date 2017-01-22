/*
 * $RCSfile: FinderUtil.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-10-15 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.action;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;

import com.skin.finder.FinderManager;
import com.skin.finder.servlet.GrepServlet;
import com.skin.finder.util.FinderUtil;
import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.annotation.UrlPattern;

/**
 * <p>Title: FinderUtil</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class GrepAction extends BaseAction {
    protected static GrepServlet grepServlet = new GrepServlet();

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/grep.html")
    public void grep() throws ServletException, IOException {
        this.execute("/template/finder/grep.jsp");
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/grep/find.html")
    public void find() throws ServletException, IOException {
        File file = FinderUtil.getFile(this.request);
        grepServlet.grep(this.getRequest(), this.getResponse(), file);
    }

    /**
     * @param page
     * @throws ServletException
     * @throws IOException
     */
    public void execute(String page) throws ServletException, IOException {
        String workspace = this.request.getParameter("workspace");
        String path = this.request.getParameter("path");
        String home = FinderUtil.getWorkspace(this.request, workspace);
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
}
