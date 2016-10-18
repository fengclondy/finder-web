/*
 * $RCSfile: SessionFilter.java,v $$
 * $Revision: 1.1 $
 * $Date: 2016-10-17 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.j2ee.sso.Client;
import com.skin.j2ee.sso.session.UserSession;
import com.skin.util.PathMatcher;

/**
 * <p>Title: SessionFilter</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class SessionFilter implements Filter {
    protected String[] includes;
    protected String[] excludes;
    private static final Logger logger = LoggerFactory.getLogger(SessionFilter.class);

    /**
     * @param filterConfig
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.includes = PathMatcher.split(filterConfig.getInitParameter("includes"), ",");
        this.excludes = PathMatcher.split(filterConfig.getInitParameter("excludes"), ",");

        if(this.includes == null || this.includes.length < 1) {
            this.includes = new String[] {"/finder/**/*"};
        }

        if(this.excludes == null || this.excludes.length < 1) {
            this.excludes = new String[] {"/finder/login.html"};
        }
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
            throw new ServletException("SessionFilter just supports HTTP requests");
        }

        HttpServletRequest request = (HttpServletRequest)(servletRequest);
        HttpServletResponse response = (HttpServletResponse)(servletResponse);
        String requestURI = request.getRequestURI();
        String contextPath = this.getContextPath(request);
        logger.debug("requestURI: {}", requestURI);

        if(requestURI.startsWith(contextPath)) {
            requestURI = requestURI.substring(contextPath.length());
        }

        if(PathMatcher.match(requestURI, this.excludes, true)) {
            filterChain.doFilter(request, response);
            return;
        }

        if(!PathMatcher.match(requestURI, this.includes, true)) {
            filterChain.doFilter(request, response);
            return;
        }

        UserSession userSession = Client.getSession(request);

        if(userSession == null) {
            if(logger.isDebugEnabled()) {
                logger.debug("user not login: {}", requestURI);
            }
            this.login(request, response);
            return;
        }

        long userId = userSession.getUserId();
        String userName = userSession.getUserName();

        if(userId < 1L) {
            if(logger.isDebugEnabled()) {
                logger.debug("user not login: {}", requestURI);
            }
            this.login(request, response);
            return;
        }

        logger.info("{}|{}|{}", userId, userName, requestURI);
        filterChain.doFilter(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     */
    protected void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String contextPath = this.getContextPath(request);
        response.sendRedirect(contextPath + "/finder/login.html");
    }

    /**
     * @param request
     * @return String
     */
    protected String getContextPath(HttpServletRequest request) {
        String contextPath = request.getContextPath();

        if(contextPath == null || contextPath.equals("/")) {
            return "";
        }
        return contextPath;
    }

    /**
     * 
     */
    @Override
    public void destroy() {
    }

}
