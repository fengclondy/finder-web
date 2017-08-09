/*
 * $RCSfile: LoginServlet.java,v $$
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skin.finder.cluster.Agent;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.servlet.template.LoginTemplate;
import com.skin.finder.user.SimpleUserManager;
import com.skin.finder.user.UserSession;
import com.skin.finder.util.Ajax;
import com.skin.finder.util.Password;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;
import com.skin.finder.web.util.Client;

/**
 * <p>Title: LoginServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class LoginServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    /**
     * 这只是一个非常简陋的实现
     * 权限系统一般比较复杂, 所以一开始finder就不打算提供任何的权限控制
     * 而是交给其他系统来进行接入
     * 这里的实现只是为了满足一些只需要用户登录的小需求
     * 更多的控制请自行实现
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.login")
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /**
         * 只有master提供登录服务
         */
        String master = ConfigFactory.getMaster();

        if(Agent.dispatch(request, response, master)) {
            return;
        }

        String userName = this.getTrimString(request, "userName");
        String password = this.getTrimString(request, "password");
        response.setHeader("Cluster-Node", request.getLocalAddr() + ":" + request.getLocalPort());

        if(userName.length() < 1 || password.length() < 1) {
            LoginTemplate.execute(request, response);
            return;
        }

        SimpleUserManager userManager = SimpleUserManager.getInstance(this.servletContext);
        Map<String, String> user = userManager.getByName(userName);

        if(user == null) {
            Ajax.error(request, response, 501, "用户不存在！");
            return;
        }

        String userSalt = user.get("userSalt");
        String userPass = Password.encode(password, userSalt);

        if(userPass.equals(user.get("password"))) {
            long userId = Long.parseLong(user.get("userId"));
            String clientId = String.valueOf(System.currentTimeMillis());
            Date createTime = new Date();

            UserSession userSession = new UserSession();
            userSession.setAppId(1L);
            userSession.setUserId(userId);
            userSession.setUserName(userName);
            userSession.setNickName(userName);
            userSession.setClientId(clientId);
            userSession.setCreateTime(createTime);
            userSession.setLastAccessTime(createTime);

            int expiry = 7 * 24 * 60 * 60;
            Client.registe(request, response, userSession, null, "/", expiry, false);
            Ajax.success(request, response, "true");
        }
        else {
            Ajax.error(request, response, 501, "密码错误！");
            return;
        }
    }
}
