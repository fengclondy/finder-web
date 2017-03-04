/*
 * $RCSfile: MailLog.java,v $$
 * $Revision: 1.1 $
 * $Date: 2015-02-06 17:47:04 $
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
 * <p>Title: MailLog</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class MailLog implements Serializable {
    private static final long serialVersionUID = 1L;
    private long logId;
    private long appId;
    private long userId;
    private long customerId;
    private long bizId;
    private String bizType;
    private String mailType;
    private String sender;
    private String receiver;
    private String subject;
    private String content;
    private int sendStatus;
    private Date sendTime;

    /**
     * @param logId the logId to set
     */
    public void setLogId(long logId) {
        this.logId = logId;
    }

    /**
     * @return the logId
     */
    public long getLogId() {
        return this.logId;
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
     * @param customerId the customerId to set
     */
    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the customerId
     */
    public long getCustomerId() {
        return this.customerId;
    }

    /**
     * @param bizId the bizId to set
     */
    public void setBizId(long bizId) {
        this.bizId = bizId;
    }

    /**
     * @return the bizId
     */
    public long getBizId() {
        return this.bizId;
    }

    /**
     * @param bizType the bizType to set
     */
    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    /**
     * @return the bizType
     */
    public String getBizType() {
        return this.bizType;
    }

    /**
     * @param mailType the mailType to set
     */
    public void setMailType(String mailType) {
        this.mailType = mailType;
    }

    /**
     * @return the mailType
     */
    public String getMailType() {
        return this.mailType;
    }

    /**
     * @param sender the sender to set
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * @return the sender
     */
    public String getSender() {
        return this.sender;
    }

    /**
     * @param receiver the receiver to set
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    /**
     * @return the receiver
     */
    public String getReceiver() {
        return this.receiver;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return this.subject;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return this.content;
    }

    /**
     * @param sendStatus the sendStatus to set
     */
    public void setSendStatus(int sendStatus) {
        this.sendStatus = sendStatus;
    }

    /**
     * @return the sendStatus
     */
    public int getSendStatus() {
        return this.sendStatus;
    }

    /**
     * @param sendTime the sendTime to set
     */
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * @return the sendTime
     */
    public Date getSendTime() {
        return this.sendTime;
    }
}