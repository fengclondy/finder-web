/*
 * $RCSfile: DispatchFilter.java,v $
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skin.finder.util.StringUtil;
import com.skin.finder.web.ActionDispatcher;

/**
 * <p>Title: DispatchFilter</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class DispatchFilter implements Filter {
    private ActionDispatcher dispatcher;
    private ServletContext servletContext;

    /**
     * default
     */
    public DispatchFilter() {
        this.dispatcher = new ActionDispatcher();
    }

    /**
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.servletContext = filterConfig.getServletContext();
        String[] packages = this.getArray(filterConfig, "packages");
        this.dispatcher.setPackages(packages);
        this.dispatcher.init(this.servletContext);
    }

    /**
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        if(!(servletRequest instanceof HttpServletRequest) || !(servletResponse instanceof HttpServletResponse)) {
            throw new ServletException("DispatchFilter just supports HTTP requests");
        }
        this.dispatch((HttpServletRequest)servletRequest, (HttpServletResponse)servletResponse, filterChain);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void dispatch(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String action = request.getParameter("action");
        String requestURI = request.getRequestURI();
        String contextPath = this.getContextPath(request);
        request.setAttribute("servletContext", this.servletContext);
        request.setAttribute("requestURI", requestURI);
        request.setAttribute("contextPath", this.getContextPath(request));

        if(action == null) {
            action = this.getAction(contextPath, requestURI);
        }

        boolean b = this.dispatcher.dispatch(request, response, action);

        if(!b) {
            filterChain.doFilter(request, response);
        }
    }

    /**
     * @param request
     * @return String
     */
    private String getContextPath(HttpServletRequest request) {
        String contextPath = request.getContextPath();

        if(contextPath == null || contextPath.length() <= 1) {
            return "";
        }
        return contextPath;
    }

    /**
     * @param contextPath
     * @param requestURI
     * @return String
     */
    protected String getAction(String contextPath, String requestURI) {
        int s = 0;
        int k = requestURI.lastIndexOf('.');

        if(contextPath.length() > 0 && requestURI.startsWith(contextPath)) {
            s = contextPath.length();
        }

        if(s < requestURI.length() && requestURI.charAt(s) == '/') {
            s++;
        }

        if(k < 0) {
            k = requestURI.length();
        }
        return requestURI.substring(s, k).replace('/', '.');
    }

    /**
     * @param filterConfig
     * @return String[]
     */
    private String[] getArray(FilterConfig filterConfig, String name) {
        String content = filterConfig.getInitParameter(name);

        if(content == null) {
            return new String[0];
        }
        return StringUtil.split(content, ",", true, true);
    }

    /**
     * destroy
     */
    @Override
    public void destroy() {
        if(this.dispatcher != null) {
            this.dispatcher.destroy();
            this.dispatcher = null;
        }
    }
}
