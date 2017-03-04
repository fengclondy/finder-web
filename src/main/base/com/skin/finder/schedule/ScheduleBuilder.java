/*
 * $RCSfile: ScheduleBuilder.java,v $$
 * $Revision: 1.1  $
 * $Date: 2012-11-1  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.schedule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.manager.ScheduleManager;
import com.skin.finder.model.Schedule;

/**
 * <p>Title: ScheduleBuilder</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ScheduleBuilder {
    private static Scheduler instance = null;
    private static final Logger logger = LoggerFactory.getLogger(ScheduleBuilder.class);

    /**
     * @throws Exception
     */
    public static synchronized void build() throws Exception {
        ScheduleBuilder.shutdown(true);
        ScheduleManager scheduleManager = new ScheduleManager();

        try {
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            instance = schedulerFactory.getScheduler();
            List<Schedule> list = scheduleManager.getListByStatus(1);

            if(list != null && list.size() > 0) {
                int count = 0;

                for(Schedule schedule : list) {
                    Long scheduleId = schedule.getScheduleId();
                    Integer status = schedule.getStatus();
                    String expression = schedule.getExpression();

                    if(status == null || status.intValue() == 0) {
                        logger.info("scheduleId: {} - ignore status: 0", scheduleId);
                        continue;
                    }

                    if(expression == null) {
                        continue;
                    }

                    expression = expression.trim();

                    if(expression.length() < 1) {
                        continue;
                    }

                    JobBuilder jobBuilder = JobBuilder.newJob(SimpleJob.class);
                    jobBuilder.withIdentity("job" + (count + 1), "group" + (count + 1));
                    JobDetail jobDetail = jobBuilder.build();

                    JobDataMap jobDataMap = jobDetail.getJobDataMap();
                    jobDataMap.put("scheduleId", scheduleId);

                    CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(expression);
                    CronTrigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity("trigger" + (count + 1), "group" + (count + 1))
                        .withSchedule(cronScheduleBuilder)
                        .build();

                    Date nextFireTime = trigger.getFireTimeAfter(new Date());

                    if(nextFireTime == null) {
                        scheduleManager.setStatus(schedule.getScheduleId(), 0);
                        continue;
                    }

                    logger.info("scheduleId: {} - nextFireTime: {}", scheduleId, format(nextFireTime));
                    Date date = instance.scheduleJob(jobDetail, trigger);
                    logger.info("scheduleId: {} - {} has been scheduled to run at: {}, and repeat based on expression: {}",
                            scheduleId, jobDetail.getKey(), format(date), trigger.getCronExpression());
                    count++;
                }

                if(count > 0) {
                    instance.start();
                }
                else {
                    logger.info("no schedule to run...");
                }
            }
        }
        finally {
        }
    }

    /**
     * @param flag
     */
    public static synchronized void shutdown(boolean flag) {
        if(instance != null) {
            try {
                if(instance.isShutdown() == false) {
                    instance.shutdown(flag);
                    instance.clear();
                }
            }
            catch(SchedulerException e) {
            }
        }
    }

    /**
     * @param date
     * @return String
     */
    public static String format(Date date) {
        if(date != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
            return dateFormat.format(date);
        }
        return "";
    }
}
