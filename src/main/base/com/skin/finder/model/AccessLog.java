/*
 * $RCSfile: AccessLog.java,v $$
 * $Revision: 1.1 $
 * $Date: 2014-01-03 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.model;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Title: AccessLog</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class AccessLog implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long logId;
    private Long userId;
    private String userName;
    private String remoteHost;
    private String requestMethod;
    private String requestProtocol;
    private String requestUrl;
    private String requestReferer;
    private String clientId;
    private String clientUserAgent;
    private String clientCookie;
    private Date createTime;

    /**
     * 
     */
    public AccessLog() {
    }

    /**
     * @param logId the logId to set
     */
    public void setLogId(Long logId) {
        this.logId = logId;
    }

    /**
     * @return the logId
     */
    public Long getLogId() {
        return this.logId;
    }

    /**
     * @return the userId
     */
    public Long getUserId() {
        return this.userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @param remoteHost the remoteHost to set
     */
    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    /**
     * @return the remoteHost
     */
    public String getRemoteHost() {
        return this.remoteHost;
    }

    /**
     * @param requestMethod the requestMethod to set
     */
    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    /**
     * @return the requestMethod
     */
    public String getRequestMethod() {
        return this.requestMethod;
    }

    /**
     * @param requestProtocol the requestProtocol to set
     */
    public void setRequestProtocol(String requestProtocol) {
        this.requestProtocol = requestProtocol;
    }

    /**
     * @return the requestProtocol
     */
    public String getRequestProtocol() {
        return this.requestProtocol;
    }

    /**
     * @param requestUrl the requestUrl to set
     */
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    /**
     * @return the requestUrl
     */
    public String getRequestUrl() {
        return this.requestUrl;
    }

    /**
     * @param requestReferer the requestReferer to set
     */
    public void setRequestReferer(String requestReferer) {
        this.requestReferer = requestReferer;
    }

    /**
     * @return the requestReferer
     */
    public String getRequestReferer() {
        return this.requestReferer;
    }

    /**
     * @param clientId the clientId to set
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * @return the clientUserId
     */
    public String getClientId() {
        return this.clientId;
    }

    /**
     * @param clientUserAgent the clientUserAgent to set
     */
    public void setClientUserAgent(String clientUserAgent) {
        this.clientUserAgent = clientUserAgent;
    }

    /**
     * @return the clientUserAgent
     */
    public String getClientUserAgent() {
        return this.clientUserAgent;
    }

    /**
     * @param clientCookie the clientCookie to set
     */
    public void setClientCookie(String clientCookie) {
        this.clientCookie = clientCookie;
    }

    /**
     * @return the clientCookie
     */
    public String getClientCookie() {
        return this.clientCookie;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the createTime
     */
    public Date getCreateTime() {
        return this.createTime;
    }
}