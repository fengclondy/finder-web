/*
 * $RCSfile: SessionFilter.java,v $
 * $Revision: 1.1 $
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

import com.skin.finder.user.UserSession;
import com.skin.finder.web.util.Client;

/**
 * <p>Title: SessionFilter</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class SessionFilter implements Filter {
    protected String loginUrl;
    private static final Logger logger = LoggerFactory.getLogger(SessionFilter.class);

    /**
     * @param filterConfig
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.loginUrl = filterConfig.getInitParameter("loginUrl");
        this.loginUrl = (this.loginUrl != null ? this.loginUrl.trim() : "");

        if(this.loginUrl == null || this.loginUrl.length() < 1) {
            throw new ServletException("loginUrl must be not null.");
        }

        logger.info("loginUrl: {}", this.loginUrl);
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
        String action = this.getAction(request);
        logger.debug("requestURI: {}", requestURI);

        if(action == null) {
            action = "index";
        }

        if(action.equals("res") || action.equals("finder.login")) {
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
        response.sendRedirect(contextPath + this.loginUrl);
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
     * @param request
     * @return String
     */
    private String getAction(HttpServletRequest request) {
        String queryString = request.getQueryString();

        if(queryString == null) {
            return null;
        }
 
        int i = queryString.indexOf("action=");

        if(i < 0) {
            return null;
        }

        i += 7;
        int j = queryString.indexOf('&', i);

        if(j < 0) {
            return queryString.substring(i);
        }
        return queryString.substring(i, j);
    }

    /**
     * 
     */
    @Override
    public void destroy() {
    }
}
