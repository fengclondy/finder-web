/*
 * $RCSfile: Schedule.java,v $$
 * $Revision: 1.1  $
 * $Date: 2012-10-31  $
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
 * <p>Title: Schedule</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Schedule implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long scheduleId;
    private String scheduleName;
    private String description;
    private Integer scheduleType;
    private Integer status;
    private String owner;
    private String expression;
    private String action;
    private String properties;
    private Date lastFireTime;
    private Date nextFireTime;
    private Integer executeStatus;
    private String executeResult;
    private Date createTime;
    private Date updateTime;

    /**
     * @return the scheduleId
     */
    public Long getScheduleId() {
        return this.scheduleId;
    }

    /**
     * @param scheduleId the scheduleId to set
     */
    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    /**
     * @return the scheduleName
     */
    public String getScheduleName() {
        return this.scheduleName;
    }

    /**
     * @param scheduleName the scheduleName to set
     */
    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the scheduleType
     */
    public Integer getScheduleType() {
        return this.scheduleType;
    }

    /**
     * @param scheduleType the scheduleType to set
     */
    public void setScheduleType(Integer scheduleType) {
        this.scheduleType = scheduleType;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return this.status;
    }

    /**
     * @return the owner
     */
    public String getOwner() {
        return this.owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @param expression the expression to set
     */
    public void setExpression(String expression) {
        this.expression = expression;
    }

    /**
     * @return the expression
     */
    public String getExpression() {
        return this.expression;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @return the action
     */
    public String getAction() {
        return this.action;
    }

    /**
     * @return the properties
     */
    public String getProperties() {
        return this.properties;
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties(String properties) {
        this.properties = properties;
    }

    /**
     * @param lastFireTime the lastFireTime to set
     */
    public void setLastFireTime(Date lastFireTime) {
        this.lastFireTime = lastFireTime;
    }

    /**
     * @return the lastFireTime
     */
    public Date getLastFireTime() {
        return this.lastFireTime;
    }

    /**
     * @param nextFireTime the nextFireTime to set
     */
    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    /**
     * @return the nextFireTime
     */
    public Date getNextFireTime() {
        return this.nextFireTime;
    }

    /**
     * @param executeStatus the executeStatus to set
     */
    public void setExecuteStatus(Integer executeStatus) {
        this.executeStatus = executeStatus;
    }

    /**
     * @return the executeStatus
     */
    public Integer getExecuteStatus() {
        return this.executeStatus;
    }

    /**
     * @param executeResult the executeResult to set
     */
    public void setExecuteResult(String executeResult) {
        this.executeResult = executeResult;
    }

    /**
     * @return the executeResult
     */
    public String getExecuteResult() {
        return this.executeResult;
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
}
