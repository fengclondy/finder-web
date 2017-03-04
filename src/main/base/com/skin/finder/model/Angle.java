/*
 * $RCSfile: Angle.java,v $$
 * $Revision: 1.1 $
 * $Date: 2015-02-06 09:51:48 $
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
 * <p>Title: Angle</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Angle implements Serializable {
    private static final long serialVersionUID = 1L;
    private long angleId;
    private long appId;
    private String angleName;
    private String catalog;
    private String content;
    private Date createTime;
    private Date updateTime;

    /**
     * @param angleId the angleId to set
     */
    public void setAngleId(long angleId) {
        this.angleId = angleId;
    }

    /**
     * @return the angleId
     */
    public long getAngleId() {
        return this.angleId;
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
     * @param angleName the angleName to set
     */
    public void setAngleName(String angleName) {
        this.angleName = angleName;
    }

    /**
     * @return the angleName
     */
    public String getAngleName() {
        return this.angleName;
    }

    /**
     * @param catalog the catalog to set
     */
    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    /**
     * @return the catalog
     */
    public String getCatalog() {
        return this.catalog;
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