/*
 * $RCSfile: LessAction.java,v $$
 * $Revision: 1.1 $
 * $Date: 2016-10-02 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.action;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;

import com.skin.finder.manager.SimpleUserManager;
import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.annotation.UrlPattern;
import com.skin.j2ee.sso.Client;
import com.skin.j2ee.sso.session.UserSession;
import com.skin.j2ee.util.CookieUtil;
import com.skin.j2ee.util.JsonUtil;
import com.skin.security.Password;

/**
 * <p>Title: LessAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class LoginAction extends BaseAction {
    /**
     * 这只是一个非常简陋的实现
     * 权限系统一般比较复杂, 所以一开始finder就不打算提供任何的权限控制
     * 而是交给其他系统来进行接入
     * 这里的实现只是为了满足一些只需要用户登录的小需求
     * 更多的控制请自行实现
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/login.html")
    public void execute() throws ServletException, IOException {
        String userName = this.getTrimString("userName");
        String password = this.getTrimString("password");

        if(userName.length() < 1 || password.length() < 1) {
            this.forward("/template/finder/login.jsp");
            return;
        }

        SimpleUserManager userManager = this.getUserManager();
        Map<String, String> user = userManager.getByName(userName);

        if(user == null) {
            JsonUtil.error(this.request, this.response, 501, "用户不存在！");
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
            String md5Key = Client.getMd5Key();
            String certificate = Client.getCertificate(userSession, md5Key);
            CookieUtil.setCookie(this.response, "passport", certificate, null, "/", expiry, false);
            JsonUtil.success(this.request, this.response, "ok");
        }
        else {
            JsonUtil.error(this.request, this.response, 501, "密码错误！");
            return;
        }
    }

    /**
     * @return SimpleUserManager
     */
    public SimpleUserManager getUserManager() {
        String home = this.servletContext.getRealPath("/WEB-INF/user");
        return new SimpleUserManager(home);
    }
}
