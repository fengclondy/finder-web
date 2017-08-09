/*
 * $RCSfile: User.java,v $
 * $Revision: 1.1 $
 * $Date: 2013-02-07 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.security;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Title: User</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long userId;
    private Long groupId;
    private String userName;
    private String nickName;
    private String userSalt;
    private String password;
    private Integer userLevel;
    private Integer userGender;
    private String userAvatar;
    private String userSignature;
    private String userEmail;
    private Date createTime;
    private Date updateTime;
    private Date expireTime;
    private Date lastLoginTime;
    private String lastLoginIp;
    private Integer userStatus;
    private Integer userOrder;

    /**
     * 
     */
    public User() {
        super();
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return the userId
     */
    public Long getUserId() {
        return this.userId;
    }

    /**
     * @return the groupId
     */
    public Long getGroupId() {
        return this.groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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
     * @return the userSalt
     */
    public String getUserSalt() {
        return this.userSalt;
    }

    /**
     * @param userSalt the userSalt to set
     */
    public void setUserSalt(String userSalt) {
        this.userSalt = userSalt;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * @param userLevel the userLevel to set
     */
    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    /**
     * @return the userLevel
     */
    public Integer getUserLevel() {
        return this.userLevel;
    }

    /**
     * @param userGender the userGender to set
     */
    public void setUserGender(Integer userGender) {
        this.userGender = userGender;
    }

    /**
     * @return the userGender
     */
    public Integer getUserGender() {
        return this.userGender;
    }

    /**
     * @param userAvatar the userAvatar to set
     */
    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    /**
     * @return the userAvatar
     */
    public String getUserAvatar() {
        return this.userAvatar;
    }

    /**
     * @param userSignature the userSignature to set
     */
    public void setUserSignature(String userSignature) {
        this.userSignature = userSignature;
    }

    /**
     * @return the userSignature
     */
    public String getUserSignature() {
        return this.userSignature;
    }

    /**
     * @param userEmail the userEmail to set
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * @return the userEmail
     */
    public String getUserEmail() {
        return this.userEmail;
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
     * @param updateTime the updateTime to set
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return the updateTime
     */
    public Date getUpdateTime() {
        return this.updateTime;
    }

    /**
     * @param expireTime the expireTime to set
     */
    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    /**
     * @return the expireTime
     */
    public Date getExpireTime() {
        return this.expireTime;
    }

    /**
     * @param lastLoginTime the lastLoginTime to set
     */
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * @return the lastLoginTime
     */
    public Date getLastLoginTime() {
        return this.lastLoginTime;
    }

    /**
     * @return the lastLoginIp
     */
    public String getLastLoginIp() {
        return this.lastLoginIp;
    }

    /**
     * @param lastLoginIp the lastLoginIp to set
     */
    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    /**
     * @param userStatus the userStatus to set
     */
    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    /**
     * @return the userStatus
     */
    public Integer getUserStatus() {
        return this.userStatus;
    }

    /**
     * @param userOrder the userOrder to set
     */
    public void setUserOrder(Integer userOrder) {
        this.userOrder = userOrder;
    }

    /**
     * @return the userOrder
     */
    public Integer getUserOrder() {
        return this.userOrder;
    }
}
