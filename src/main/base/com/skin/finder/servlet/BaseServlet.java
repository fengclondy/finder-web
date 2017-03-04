/*
 * $RCSfile: BaseServlet.java,v $$
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
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
     */
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
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
    }

    /**
     * @return the servletContext
     */
    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    /**
     * @param servletContext the servletContext to set
     */
    protected void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * @param request
     * @param response
     * @param path
     * @throws ServletException
     * @throws IOException
     */
    protected void forward(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException {
        request.getRequestDispatcher(path).forward(request, response);
    }

    /**
     * @param request
     * @param response
     * @param status
     * @param message
     * @throws ServletException
     * @throws IOException
     */
    protected void error(HttpServletRequest request, HttpServletResponse response, int status, String message) throws ServletException, IOException {
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

    /**
     * @param source
     * @return String
     */
    protected String escape(String source) {
        if(source == null) {
            return "";
        }

        char c;
        StringBuilder buffer = new StringBuilder();

        for(int i = 0, length = source.length(); i < length; i++) {
            c = source.charAt(i);

            switch (c) {
                case '"': {
                    buffer.append("\\\"");
                    break;
                }
                case '\r': {
                    buffer.append("\\r");
                    break;
                }
                case '\n': {
                    buffer.append("\\n");
                    break;
                }
                case '\t': {
                    buffer.append("\\t");
                    break;
                }
                case '\b': {
                    buffer.append("\\b");
                    break;
                }
                case '\f': {
                    buffer.append("\\f");
                    break;
                }
                case '\\': {
                    buffer.append("\\\\");
                    break;
                }
                default : {
                    buffer.append(c);
                    break;
                }
            }
        }
        return buffer.toString();
    }
}
