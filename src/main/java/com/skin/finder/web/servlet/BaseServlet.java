/*
 * $RCSfile: BaseServlet.java,v $
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.web.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title: BaseServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class BaseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    protected ServletContext servletContext;

    /**
     * default
     */
    public BaseServlet() {
    }

    /**
     * @param servletConfig
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        this.servletContext = servletConfig.getServletContext();
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        throw new ServletException("unsupported operation.");
    }

    /**
     * @return the servletContext
     */
    @Override
    public ServletContext getServletContext() {
        if(this.servletContext != null) {
            return this.servletContext;
        }
        return super.getServletContext();
    }

    /**
     * @param servletContext the servletContext to set
     */
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
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
     * @param request
     * @param response
     * @param status
     * @param message
     * @throws ServletException
     * @throws IOException
     */
    protected void setStatus(HttpServletRequest request, HttpServletResponse response, int status, String message) throws ServletException, IOException {
        request.setAttribute("javax_servlet_error", message);
        response.sendError(status);
    }

    /**
     * @param request
     * @param name
     * @param defaultValue
     * @return boolean
     */
    protected boolean getBoolean(HttpServletRequest request, String name, boolean defaultValue) {
        String value = request.getParameter(name);

        if(value != null) {
            return value.equals("true");
        }
        return defaultValue;
    }

    /**
     * @param request
     * @param name
     * @param defaultValue
     * @return int
     */
    protected int getInteger(HttpServletRequest request, String name, int defaultValue) {
        String value = request.getParameter(name);

        if(value != null) {
            try {
                return Integer.valueOf(Integer.parseInt(value));
            }
            catch(NumberFormatException e) {
            }
        }
        return defaultValue;
    }

    /**
     * @param request
     * @param name
     * @return String
     */
    protected String getTrimString(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return (value != null ? value.trim() : "");
    }

    /**
     * @param request
     * @param name
     * @param defaultValue
     * @return long
     */
    protected long getLong(HttpServletRequest request, String name, long defaultValue) {
        String value = request.getParameter(name);

        try {
            return Long.parseLong(value);
        }
        catch(NumberFormatException e) {
        }
        return defaultValue;
    }
}
