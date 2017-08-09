/*
 * $RCSfile: UserServlet.java,v $
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

import com.skin.finder.cluster.Agent;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.servlet.template.ErrorTemplate;
import com.skin.finder.servlet.template.UserAddTemplate;
import com.skin.finder.user.SimpleUserManager;
import com.skin.finder.util.Ajax;
import com.skin.finder.util.Password;
import com.skin.finder.util.RandomUtil;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;
import com.skin.finder.web.util.CurrentUser;

/**
 * <p>Title: UserServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class UserServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("user.add")
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(!this.check(request, response)) {
            return;
        }
        UserAddTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("user.save")
    public void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /**
         * 只有master提供用户服务
         */
        String master = ConfigFactory.getMaster();

        if(Agent.dispatch(request, response, master)) {
            return;
        }

        if(!this.check(request, response)) {
            return;
        }

        String userName = this.getTrimString(request, "userName");
        String password = this.getTrimString(request, "password");

        if(userName.length() < 1 || password.length() < 1) {
            Ajax.error(request, response, 501, "用户名或者密码不能为空！");
            return;
        }

        if(!this.validate(userName)) {
            Ajax.error(request, response, 501, "用户名只能包含英文字母或者数字且不少于4个字符！");
            return;
        }

        String userSalt = RandomUtil.getRandString(8, 8);
        String userPass = Password.encode(password, userSalt);

        SimpleUserManager userManager = SimpleUserManager.getInstance(this.servletContext);
        userManager.update(userName, userPass, userSalt);
        Ajax.success(request, response, "true");
        return;
    }

    /**
     * @param userName
     * @return boolean
     */
    public boolean validate(String userName) {
        if(userName.length() < 4 || userName.length() > 20) {
            return false;
        }

        String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for(int i = 0; i < userName.length(); i++) {
            char c = userName.charAt(i);
            
            if(chars.indexOf(c) < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param request
     * @param response
     * @return boolean
     */
    private boolean check(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = CurrentUser.getUserName(request);

        if(userName != null && userName.equals("admin")) {
            return true;
        }
        else {
            request.setAttribute("code", 500);
            request.setAttribute("message", "没有权限.");
            ErrorTemplate.execute(request, response);
            return false;
        }
    }
}
