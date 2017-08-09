/*
 * $RCSfile: HttpRequest.java,v $
 * $Revision: 1.1 $
 * $Date: 2013-01-06 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.http;

import com.skin.finder.util.URLParameter;

/**
 * <p>Title: HttpRequest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class HttpRequest {
    private String action;
    private String method;
    private HttpHeader httpHeader;
    private URLParameter urlParameter;

    /**
     * default
     */
    public HttpRequest() {
        this.httpHeader = new HttpHeader();
        this.urlParameter = new URLParameter();
    }

    /**
     * @param method
     * @param action
     */
    public HttpRequest(String method, String action) {
        this.method = method;
        this.action = action;
        this.httpHeader = new HttpHeader();
        this.urlParameter = URLParameter.parse(action, "utf-8");
    }

    /**
     * @return the action
     */
    public String getAction() {
        return this.action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @return the method
     */
    public String getMethod() {
        return this.method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * @return the httpHeader
     */
    public HttpHeader getHttpHeader() {
        return this.httpHeader;
    }

    /**
     * @param httpHeader the httpHeader to set
     */
    public void setHttpHeader(HttpHeader httpHeader) {
        this.httpHeader = httpHeader;
    }

    /**
     * @param name
     * @param value
     */
    public void addHeader(String name, String value) {
        this.httpHeader.addHeader(name, value);
    }

    /**
     * @param name
     * @param value
     */
    public void setHeader(String name, String value) {
        this.httpHeader.setHeader(name, value);
    }

    /**
     * @param name
     */
    public void removeHeader(String name) {
        this.httpHeader.setHeader(name, (String)null);
    }

    /**
     * @return the urlParameter
     */
    public URLParameter getUrlParameter() {
        return this.urlParameter;
    }

    /**
     * @param urlParameter the urlParameter to set
     */
    public void setUrlParameter(URLParameter urlParameter) {
        this.urlParameter = urlParameter;
    }

    /**
     * @param name
     * @param value
     */
    public void addParameter(String name, String value) {
        this.urlParameter.addParameter(name, value);
    }

    /**
     * @param name
     */
    public void removeParameter(String name) {
        this.urlParameter.removeParameter(name);
    }
}
