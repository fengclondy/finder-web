/*
 * $RCSfile: AgentServlet.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.config.ConfigFactory;
import com.skin.finder.util.Ajax;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;

/**
 * <p>Title: AgentServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class AgentServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(AgentServlet.class);

    /**
     * 接收slave的注册
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.master.registe")
    public void registe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String master = ConfigFactory.getMaster();
        String myname = ConfigFactory.getHostName();
        String host = request.getParameter("host");
        String securityKey = request.getHeader("Finder-Security-Key");

        /**
         * 如果不是master
         */
        if(!master.equals(myname)) {
            logger.warn("BadRequest: {}", host);
            Ajax.error(request, response, 404, "Bad Request.");
            return;
        }

        /**
         * 校验securityKey
         */
        if(securityKey == null || !securityKey.equals(ConfigFactory.getSecurityKey())) {
            logger.warn("BadRequest: {}", host);
            Ajax.error(request, response, 404, "Bad Request.");
            return;
        }

        /**
         * TODO: 注册到host.xml中, 并同步到集群
         * 1. 将新的host添加到host.xml中并写文件
         * 2. 遍历所有的host, 发送host.xml
         */
    }

    /**
     * 接收master向自己发送的host文件
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.slave.receive")
    public void receive(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /**
         * String command;
         * String content;
         * 
         * 1. 接收数据并写入文件
         * 2. 重新加载该配置文件
         * 3. 返回接收成功消息
         */
        Ajax.success(request, response, "true");
    }
}
