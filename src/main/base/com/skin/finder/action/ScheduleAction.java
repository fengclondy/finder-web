/*
 * $RCSfile: ScheduleAction.java,v $$

 * $Revision: 1.1  $
 * $Date: 2012-11-3  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.action;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.datasource.ConnectionManager;
import com.skin.finder.manager.ScheduleManager;
import com.skin.finder.model.Schedule;
import com.skin.finder.schedule.ScheduleBuilder;
import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.util.JsonUtil;
import com.skin.j2ee.util.ScrollPage;

/**
 * <p>Title: ScheduleAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ScheduleAction extends BaseAction {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleAction.class);

    /**
     * @throws ServletException
     * @throws IOException
     */
    @com.skin.j2ee.annotation.UrlPattern("/system/schedule/list.html")
    public void list() throws ServletException, IOException {
        ScrollPage<Schedule> page = this.getScrollPage(Schedule.class);

        if(ConnectionManager.available()) {
            ScheduleManager scheduleManager = new ScheduleManager();
            scheduleManager.getList(page);
        }

        this.setAttribute("scheduleList", page.getItems());
        this.setAttribute("pageNum", page.getPageNum());
        this.setAttribute("pageSize", page.getPageSize());
        this.setAttribute("scheduleCount", page.getCount());
        this.forward("/template/system/schedule/scheduleList.jsp");
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @com.skin.j2ee.annotation.UrlPattern("/system/schedule/build.html")
    public void build() throws ServletException, IOException {
        try {
            ScheduleBuilder.build();
        }
        catch(Exception e) {
            if(logger.isWarnEnabled()) {
                logger.warn(e.getMessage(), e);
            }
        }
        JsonUtil.success(this.request, this.response, true);
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @com.skin.j2ee.annotation.UrlPattern("/system/schedule/add.html")
    public void add() throws ServletException, IOException {
        this.forward("/template/system/schedule/scheduleAdd.jsp");
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @com.skin.j2ee.annotation.UrlPattern("/system/schedule/create.html")
    public void create() throws ServletException, IOException {
        String scheduleName = this.getString("scheduleName");
        String description = this.getString("description");
        String expression = this.getString("expression");
        String action = this.getString("action");
        String properties = this.getString("properties");

        if(!ConnectionManager.available()) {
            JsonUtil.error(this.request, this.response, 500, "没有数据库连接！");
            return;
        }

        try {
            int scheduleType = 1;
            Date sysTime = new Date();
            Schedule schedule = new Schedule();
            schedule.setScheduleName(scheduleName);
            schedule.setDescription(description);
            schedule.setScheduleType(scheduleType);
            schedule.setStatus(1);
            schedule.setOwner("anyone");
            schedule.setExpression(expression);
            schedule.setAction(action);
            schedule.setProperties(properties);
            schedule.setLastFireTime((Date)null);
            schedule.setNextFireTime((Date)null);
            schedule.setExecuteStatus(0);
            schedule.setExecuteResult("");
            schedule.setCreateTime(sysTime);
            schedule.setUpdateTime(sysTime);
            new ScheduleManager().create(schedule);
            ScheduleBuilder.build();
            JsonUtil.success(this.request, this.response, true);
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
            JsonUtil.error(this.request, this.response, 500, "系统错误，请稍后再试！");
            return;
        }
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @com.skin.j2ee.annotation.UrlPattern("/system/schedule/update.html")
    public void update() throws ServletException, IOException {
        long scheduleId = this.getLong("scheduleId", 0L);

        if(!ConnectionManager.available()) {
            JsonUtil.error(this.request, this.response, 500, "没有数据库连接！");
            return;
        }

        if(scheduleId > 0L) {
            Schedule schedule = new ScheduleManager().getById(scheduleId);
            this.setAttribute("schedule", schedule);
        }
        this.forward("/template/system/schedule/scheduleEdit.jsp");
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @com.skin.j2ee.annotation.UrlPattern("/system/schedule/save.html")
    public void save() throws ServletException, IOException {
        long scheduleId = this.getLong("scheduleId", 0L);
        String scheduleName = this.getString("scheduleName");
        String description = this.getString("description");
        String expression = this.getString("expression");
        String action = this.getString("action");
        String properties = this.getString("properties");

        if(!ConnectionManager.available()) {
            JsonUtil.error(this.request, this.response, 500, "没有数据库连接！");
            return;
        }
        
        ScheduleManager scheduleManager = new ScheduleManager();
        Schedule schedule = scheduleManager.getById(scheduleId);

        if(schedule != null) {
            boolean rebuild = false;
            Date sysTime = new Date();
            String oldExpression = schedule.getExpression();
            Date nextFireTime = schedule.getNextFireTime();

            if(oldExpression != null && !oldExpression.equals(expression)) {
                rebuild = true;
                nextFireTime = null;
            }

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("scheduleName", scheduleName);
            params.put("description", description);
            params.put("expression", expression);
            params.put("action", action);
            params.put("properties", properties);
            params.put("nextFireTime", nextFireTime);
            params.put("updateTime", sysTime);
            scheduleManager.update(scheduleId, params);

            if(rebuild) {
                try {
                    ScheduleBuilder.build();
                } 
                catch(Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        JsonUtil.success(this.request, this.response, true);
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @com.skin.j2ee.annotation.UrlPattern("/system/schedule/setStatus.html")
    public void setStatus() throws ServletException, IOException {
        long scheduleId = this.getLong("scheduleId", 0L);
        int status = this.getInteger("status", 0);

        if(!ConnectionManager.available()) {
            JsonUtil.error(this.request, this.response, 500, "没有数据库连接！");
            return;
        }

        if(scheduleId > 0) {
            if(status != 0) {
                status = 1;
            }

            new ScheduleManager().setStatus(scheduleId, status);

            try {
                ScheduleBuilder.build();
            } 
            catch(Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        JsonUtil.success(this.request, this.response, true);
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @com.skin.j2ee.annotation.UrlPattern("/system/schedule/delete.html")
    public void delete() throws ServletException, IOException {
        long scheduleId = this.getLong("scheduleId", 0L);

        if(!ConnectionManager.available()) {
            JsonUtil.error(this.request, this.response, 500, "没有数据库连接！");
            return;
        }

        if(scheduleId > 0) {
            new ScheduleManager().delete(scheduleId);

            try {
                ScheduleBuilder.build();
            } 
            catch(Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        JsonUtil.success(this.request, this.response, true);
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @com.skin.j2ee.annotation.UrlPattern("/system/schedule/execute.html")
    public void execute() throws ServletException, IOException {
        final long scheduleId = this.getLong("scheduleId", 0L);

        if(scheduleId > 0L) {
            new Thread(){
                @Override
                public void run() {
                    new ScheduleManager().execute(scheduleId, "manul", (Date)null);
                }
            }.start();
            
        }
        JsonUtil.success(this.request, this.response, true);
    }
}
