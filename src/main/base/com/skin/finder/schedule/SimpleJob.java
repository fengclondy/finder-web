/*
 * $RCSfile: SimpleJob.java,v $$
 * $Revision: 1.1  $
 * $Date: 2012-11-1  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.schedule;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.manager.ScheduleManager;

/**
 * <p>Title: SimpleJob</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class SimpleJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(SimpleJob.class);

    /**
     *
     */
    public SimpleJob() {
    }

    /**
     * @param context
     * @throws JobExecutionException
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        long scheduleId = jobDataMap.getLong("scheduleId");
        logger.info("simple job run: scheduleId: " + scheduleId);

        if(scheduleId < 1L) {
            logger.warn("scheduleId: " + scheduleId + " failed !");
            return;
        }

        Date nextFireTime = context.getNextFireTime();
        new ScheduleManager().execute(scheduleId, "auto", nextFireTime);
    }
}
