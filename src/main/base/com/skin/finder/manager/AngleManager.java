/*
 * $RCSfile: AngleManager.java,v $$
 * $Revision: 1.1 $
 * $Date: 2015-02-06 09:57:22 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.manager;

import java.util.Date;

import com.skin.finder.model.Angle;
import com.skin.finder.service.AngleService;

/**
 * <p>Title: AngleManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class AngleManager {
    private AngleService angleService;

    public AngleManager() {
        this.angleService = new AngleService();
    }

    /**
     * @param angleId
     * @return Angle
     */
    public Angle getById(long angleId) {
        return this.getAngleService().getById(angleId);
    }

    /**
     * @param angle
     * @return int
     */
    public int insert(Angle angle) {
        return this.getAngleService().insert(angle);
    }

    /**
     * @param angleId
     * @param content
     * @param updateTime
     * @return int
     */
    public int update(long angleId, String content, Date updateTime) {
        return this.getAngleService().update(angleId, content, updateTime);
    }

    /**
     * @param angleId
     * @return int
     */
    public int delete(long angleId) {
        return this.getAngleService().delete(angleId);
    }

    /**
     * @return the angleService
     */
    public AngleService getAngleService() {
        return this.angleService;
    }

    /**
     * @param angleService the angleService to set
     */
    public void setAngleService(AngleService angleService) {
        this.angleService = angleService;
    }
}