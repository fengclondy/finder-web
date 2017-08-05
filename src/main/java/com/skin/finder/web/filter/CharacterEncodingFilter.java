/*
 * $RCSfile: CharacterEncodingFilter.java,v $
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
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title: CharacterEncodingFilter</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class CharacterEncodingFilter implements Filter {
    private String encoding;

    /**
     *
     */
    public CharacterEncodingFilter() {
    }

    /**
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.encoding = filterConfig.getInitParameter("encoding");
    }

    /**
     * @param encoding
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
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
            throw new ServletException("CharacterEncodingFilter just supports HTTP requests");
        }

        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        request.setAttribute("FILTER_REQUEST_URI", request.getRequestURI());
        request.setAttribute("FILTER_REQUEST_URL", request.getRequestURL());
        doFilterInternal(request, response, filterChain);
    }

    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(this.encoding != null && request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(this.encoding);
            response.setCharacterEncoding(this.encoding);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * destroy
     */
    @Override
    public void destroy() {
    }
}
