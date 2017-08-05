/*
 * $RCSfile: UserSession.java,v $$
 * $Revision: 1.1  $
 * $Date: 2011-4-7  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.user;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Title: UserSession</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class UserSession implements Serializable {
    private static final long serialVersionUID = 1L;
    private long sessionId;
    private long appId;
    private long userId;
    private String userName;
    private String nickName;
    private String clientId;
    private Date createTime;
    private Date lastAccessTime;
    private String signature;

    /**
     * @param sessionId the sessionId to set
     */
    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * @return the sessionId
     */
    public long getSessionId() {
        return this.sessionId;
    }

    /**
     * @return the appId
     */
    public long getAppId() {
        return this.appId;
    }

    /**
     * @param appId the appId to set
     */
    public void setAppId(long appId) {
        this.appId = appId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * @return the userId
     */
    public long getUserId() {
        return this.userId;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * @param nickName the nickName to set
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * @return the nickName
     */
    public String getNickName() {
        return this.nickName;
    }

    /**
     * @return the clientId
     */
    public String getClientId() {
        return this.clientId;
    }

    /**
     * @param clientId the clientId to set
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
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

    /**
     * @return the lastAccessTime
     */
    public Date getLastAccessTime() {
        return this.lastAccessTime;
    }

    /**
     * @param lastAccessTime the lastAccessTime to set
     */
    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    /**
     * @param signature the signature to set
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * @return the signature
     */
    public String getSignature() {
        return this.signature;
    }
}
