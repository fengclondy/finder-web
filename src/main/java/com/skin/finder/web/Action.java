/*
 * $RCSfile: Action.java,v $
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.web;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServlet;

/**
 * <p>Title: Action</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Action {
    private String url;
    private Method method;
    private HttpServlet httpServlet;

    /**
     * @return String
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return Method
     */
    public Method getMethod() {
        return this.method;
    }

    /**
     * @param method
     */
    public void setMethod(Method method) {
        this.method = method;
    }

    /**
     * @return HttpServlet
     */
    public HttpServlet getHttpServlet() {
        return this.httpServlet;
    }

    /**
     * @param httpServlet
     */
    public void setHttpServlet(HttpServlet httpServlet) {
        this.httpServlet = httpServlet;
    }
}
