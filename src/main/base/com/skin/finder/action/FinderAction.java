/*
 * $RCSfile: FinderAction.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-10-14 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.action;

import java.io.IOException;

import javax.servlet.ServletException;

import com.skin.finder.servlet.FinderServlet;
import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.annotation.UrlPattern;

/**
 * <p>Title: FinderAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @version 1.0
 */
public class FinderAction extends BaseAction {
    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/index.html")
    public void index() throws ServletException, IOException {
        new FinderServlet(this.getServletContext(), "/template").index(this.getRequest(), this.getResponse());
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/hello.html")
    public void hello() throws ServletException, IOException {
        new FinderServlet(this.getServletContext(), "/template").hello(this.getRequest(), this.getResponse());
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/tree.html")
    public void tree() throws ServletException, IOException {
        new FinderServlet(this.getServletContext(), "/template").tree(this.getRequest(), this.getResponse());
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/getWorkspaceXml.html")
    public void getWorkspaceXml() throws ServletException, IOException {
        String listUrl = this.getContextPath() + "/finder/display.html?a=1";
        String xmlUrl = this.getContextPath() + "/finder/getFolderXml.html?a=1";
        new FinderServlet(this.getServletContext(), "/template").getWorkspaceXml(this.getRequest(), this.getResponse(), listUrl, xmlUrl);
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/getFolderXml.html")
    public void getFolderXml() throws ServletException, IOException {
        String listUrl = this.getContextPath() + "/finder/display.html?a=1";
        String xmlUrl = this.getContextPath() + "/finder/getFolderXml.html?a=1";
        new FinderServlet(this.getServletContext(), "/template").getFolderXml(this.getRequest(), this.getResponse(), listUrl, xmlUrl);
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/getFileList.html")
    public void getFileList() throws ServletException, IOException {
        new FinderServlet(this.getServletContext(), "/template").getFileList(this.getRequest(), this.getResponse());
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/display.html")
    public void display() throws ServletException, IOException {
        new FinderServlet(this.getServletContext(), "/template").display(this.getRequest(), this.getResponse());
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/download.html")
    public void download() throws ServletException, IOException {
        new FinderServlet(this.getServletContext(), "/template").download(this.getRequest(), this.getResponse());
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/play.html")
    public void play() throws ServletException, IOException {
        new FinderServlet(this.getServletContext(), "/template").play(this.getRequest(), this.getResponse());
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/suggest.html")
    public void suggest() throws ServletException, IOException {
        new FinderServlet(this.getServletContext(), "/template").suggest(this.getRequest(), this.getResponse());
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/rename.html")
    public void rename() throws ServletException, IOException {
        new FinderServlet(this.getServletContext(), "/template").rename(this.getRequest(), this.getResponse());
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/mkdir.html")
    public void mkdir() throws ServletException, IOException {
        new FinderServlet(this.getServletContext(), "/template").mkdir(this.getRequest(), this.getResponse());
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/upload.html")
    public void upload() throws ServletException, IOException {
        new FinderServlet(this.getServletContext(), "/template").upload(this.getRequest(), this.getResponse());
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/cut.html")
    public void cut() throws ServletException, IOException {
        new FinderServlet(this.getServletContext(), "/template").cut(this.getRequest(), this.getResponse());
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/copy.html")
    public void copy() throws ServletException, IOException {
        new FinderServlet(this.getServletContext(), "/template").copy(this.getRequest(), this.getResponse());
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/delete.html")
    public void delete() throws ServletException, IOException {
        new FinderServlet(this.getServletContext(), "/template").delete(this.getRequest(), this.getResponse());
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/blank.html")
    public void blank() throws ServletException, IOException {
        new FinderServlet(this.getServletContext(), "/template").blank(this.getRequest(), this.getResponse());
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/help.html")
    public void help() throws ServletException, IOException {
        new FinderServlet(this.getServletContext(), "/template").help(this.getRequest(), this.getResponse());
    }
}
