/*
 * $RCSfile: IndexServlet.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skin.finder.servlet.template.BlankTemplate;
import com.skin.finder.servlet.template.ConfigTemplate;
import com.skin.finder.servlet.template.HelpTemplate;
import com.skin.finder.servlet.template.IndexTemplate;
import com.skin.finder.util.IP;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.FileServlet;

/**
 * <p>Title: IndexServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class IndexServlet extends FileServlet {
    private static final long serialVersionUID = 1L;

    /**
     * default
     */
    public IndexServlet() {
    }

    /**
     * @param servletContext
     */
    public IndexServlet(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * @param servletConfig
     */
    @Override
    public void init(ServletConfig servletConfig) {
        
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ErrorServlet.error(request, response, 404, "Not Found");
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern({"", "index", "finder.index"})
    public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("localIp", IP.getLocalHostAddress());
        IndexTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.config")
    public void config(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConfigTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.blank")
    public void blank(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BlankTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.help")
    public void help(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HelpTemplate.execute(request, response);
    }
}
