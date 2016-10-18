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

import javax.servlet.ServletException;

import com.skin.finder.manager.SimpleUserManager;
import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.annotation.UrlPattern;
import com.skin.j2ee.util.JsonUtil;
import com.skin.security.Password;
import com.skin.util.RandomUtil;

/**
 * <p>Title: LessAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class SimpleUserAction extends BaseAction {
    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/user/add.html")
    public void add() throws ServletException, IOException {
        this.forward("/template/finder/userAdd.jsp");
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/user/save.html")
    public void save() throws ServletException, IOException {
        String userName = this.getTrimString("userName");
        String password = this.getTrimString("password");

        if(userName.length() < 1 || password.length() < 1) {
            JsonUtil.error(this.request, this.response, 501, "用户名或者密码不能为空！");
            return;
        }

        if(!this.validate(userName)) {
            JsonUtil.error(this.request, this.response, 501, "用户名只能包含英文字母或者数字且不少于4个字符！");
            return;
        }

        String userSalt = RandomUtil.getRandString(8, 8);
        String userPass = Password.encode(password, userSalt);

        SimpleUserManager userManager = this.getUserManager();
        userManager.update(userName, userPass, userSalt);
        JsonUtil.success(this.request, this.response, "保存成功！");
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
     * @return SimpleUserManager
     */
    public SimpleUserManager getUserManager() {
        String home = this.servletContext.getRealPath("/WEB-INF/user");
        return new SimpleUserManager(home);
    }
}
