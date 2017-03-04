/*
 * $RCSfile: ScheduleManager.java,v $$
 * $Revision: 1.1 $
 * $Date: 2012-10-30 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.manager;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.model.Schedule;
import com.skin.finder.model.ScheduleLog;
import com.skin.finder.service.ScheduleLogService;
import com.skin.finder.service.ScheduleService;
import com.skin.j2ee.util.ScrollPage;
import com.skin.util.ClassUtil;
import com.skin.util.HttpUtil;

/**
 * <p>Title: ScheduleManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ScheduleManager {
    private ScheduleService scheduleService;
    private ScheduleLogService scheduleLogService;
    private static final String ID = getLocalHostAddress();
    private static final Logger logger = LoggerFactory.getLogger(ScheduleManager.class);

    /**
     *
     */
    public ScheduleManager() {
        this.scheduleService = new ScheduleService();
        this.scheduleLogService = new ScheduleLogService();
    }

    /**
     * @param scheduleId
     * @return Schedule
     */
    public Schedule getById(long scheduleId) {
        return this.scheduleService.getById(scheduleId);
    }

    /**
     * @param schedule
     * @return int
     */
    public int create(Schedule schedule) {
        return this.scheduleService.create(schedule);
    }

    /**
     * @param schedule
     * @return int
     */
    public int update(Schedule schedule) {
        return this.scheduleService.update(schedule);
    }

    /**
     * @param scheduleId
     * @param params
     * @return int
     */
    public int update(long scheduleId, Map<String, Object> params) {
        return this.scheduleService.update(scheduleId,  params);
    }

    /**
     * @return List<Schedule>
     */
    public List<Schedule> list() {
        return this.scheduleService.list();
    }

    /**
     * @param page
     * @return ScrollPage<Schedule>
     */
    public ScrollPage<Schedule> getList(ScrollPage<Schedule> page) {
        return this.scheduleService.getList(page);
    }

    /**
     * @param status
     * @return List<Schedule>
     */
    public List<Schedule> getListByStatus(int status) {
        return this.scheduleService.getListByStatus(status);
    }

    /**
     * @param scheduleId
     * @param status
     * @return int
     */
    public int setStatus(long scheduleId, int status) {
        return this.scheduleService.setStatus(scheduleId, status);
    }

    /**
     * @param scheduleId
     * @param oldOwner
     * @param newOwner
     * @return int
     */
    public boolean lock(long scheduleId, String oldOwner, String newOwner) {
        return this.scheduleService.lock(scheduleId, oldOwner, newOwner);
    }

    /**
     * @param scheduleId
     * @return int
     */
    public int delete(long scheduleId) {
        return this.scheduleService.delete(scheduleId);
    }

    /**
     * @param scheduleId
     * @param invocation
     * @param nextFireTime
     */
    public void execute(long scheduleId, String invocation, Date nextFireTime) {
        Schedule schedule = this.getById(scheduleId);

        if(schedule == null) {
            logger.warn("scheduleId: {} not exists.", scheduleId);
            return;
        }

        String owner = schedule.getOwner();
        String action = schedule.getAction();

        if(owner == null) {
            owner = "anyone";
        }

        logger.info("scheduleId: {}, owner: {}, action: {} , nextFireTime: ",
                scheduleId, owner, action, this.format(nextFireTime));

        if(action == null || action.trim().length() < 0) {
            return;
        }

        /**
         * 如果任务已经被其他机器执行那么检查最后更新事件
         * 如果owner还活着那么认为任务还在执行
         * 否则当前机器抢占任务并执行
         */
        if(!owner.equals("anyone")) {
            logger.info("scheduleId: {}, owner: {} running.", scheduleId, owner);

            /**
             * 如果owner还活着
             * owner必须开放80端口
             */
            if(this.test(owner)) {
                return;
            }
        }

        /**
         * 使用数据库的乐观锁锁定任务
         * 如果锁定成功则执行, 否则退出
         */
        boolean lock = this.lock(scheduleId, schedule.getOwner(), ID);

        if(!lock) {
            logger.info("scheduleId: {}, lock faild.", scheduleId);
            return;
        }

        Date sysTime = new Date();
        int executeStatus = 200;
        String executeResult = "success";
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("lastFireTime", sysTime);
        model.put("updateTime", sysTime);

        try {
            if(logger.isDebugEnabled()) {
                logger.debug("scheduleId: {}, action: [{}]", scheduleId, action);
            }

            /**
             * 更新调度时间
             */
            this.update(scheduleId, model);
            this.execute(action, schedule.getProperties());
        }
        catch(Exception e) {
            executeStatus = 500;
            executeResult = e.getClass().getName() + ": " + e.getMessage();
            logger.warn(e.getMessage(), e);
        }

        /**
         * 更新调度时间和执行结果
         */
        model.remove("lastFireTime");
        model.put("nextFireTime", nextFireTime);
        model.put("executeStatus", executeStatus);
        model.put("executeResult", executeResult);
        model.put("owner", "anyone");
        model.put("updateTime", new Date());
        this.update(scheduleId, model);

        try {
            /**
             * 保存执行日志
             */
            ScheduleLog scheduleLog = new ScheduleLog();
            scheduleLog.setScheduleId(scheduleId);
            scheduleLog.setScheduleName(schedule.getScheduleName());
            scheduleLog.setScheduleType(schedule.getScheduleType());
            scheduleLog.setInvocation(invocation);
            scheduleLog.setFireTime(sysTime);
            scheduleLog.setNextFireTime(nextFireTime);
            scheduleLog.setExecuteStatus(executeStatus);
            scheduleLog.setExecuteResult(executeResult);
            this.scheduleLogService.create(scheduleLog);
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * @param className
     * @param parameters
     * @throws Exception
     */
    public void execute(String className, String parameters) throws Exception {
        try {
            Object object = ClassUtil.getInstance(className, null);

            if(object != null) {
                Class<?> type = object.getClass();
                Method method = type.getMethod("execute", new Class[]{String.class});
                method.invoke(object, new Object[]{parameters});
            }
        }
        catch(Exception e) {
            Throwable cause = e.getCause();

            if(cause instanceof Exception) {
                throw (Exception)cause;
            }
            else {
                throw e;
            }
        }
    }

    /**
     * @param host
     * @return boolean
     */
    private boolean test(String host) {
        try {
            String result = HttpUtil.get("http://" + host + "/status.html");

            if(result != null && result.trim().equals("ok")) {
                return true;
            }
        }
        catch(IOException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * @param date
     * @return String
     */
    private String format(Date date) {
        if(date != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
            return dateFormat.format(date);
        }
        return "";
    }

    /**
     * @return String
     */
    private static String getLocalHostAddress() {
        try {
            StringBuilder buffer = new StringBuilder();
            byte[] bytes = InetAddress.getLocalHost().getAddress();

            for(int i = 0; i < bytes.length; i++) {
                buffer.append(bytes[i] & 0xFF).append(".");
            }

            if(buffer.length() > 0) {
                buffer.deleteCharAt(buffer.length() - 1);
            }
            return buffer.toString();
        }
        catch(UnknownHostException e) {
            logger.warn(e.getMessage(), e);
        }
        return "127.0.0.1";
    }
}
