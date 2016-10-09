/*
 * $RCSfile: ScheduleLog.java,v $$
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
 * <p>Title: ScheduleLog</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ScheduleLog implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long logId;
    private Long scheduleId;
    private String scheduleName;
    private Integer scheduleType;
    private String invocation;
    private Date fireTime;
    private Date nextFireTime;
    private Integer executeStatus;
    private String executeResult;

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
     * @return the invocation
     */
    public String getInvocation() {
        return this.invocation;
    }

    /**
     * @param invocation the invocation to set
     */
    public void setInvocation(String invocation) {
        this.invocation = invocation;
    }

    /**
     * @param fireTime the fireTime to set
     */
    public void setFireTime(Date fireTime) {
        this.fireTime = fireTime;
    }

    /**
     * @return the fireTime
     */
    public Date getFireTime() {
        return this.fireTime;
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
}
