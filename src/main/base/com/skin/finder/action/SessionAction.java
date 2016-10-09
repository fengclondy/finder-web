/*
 * $RCSfile: SessionAction.java,v $$
 * $Revision: 1.1 $
 * $Date: 2014-1-3 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.action;

import java.io.IOException;

import javax.servlet.ServletException;

import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.sso.Client;
import com.skin.j2ee.sso.session.UserSession;
import com.skin.j2ee.util.JsonUtil;

/**
 * <p>Title: SessionAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @version 1.0
 */
public class SessionAction extends BaseAction {
    /**
     * @throws ServletException
     * @throws IOException
     */
    @com.skin.j2ee.annotation.UrlPattern("/system/session.html")
    public void execute() throws ServletException, IOException {
        UserSession userSession = Client.getSession(this.request);
        this.setAttribute("userSession", userSession);
        this.forward("/template/system/session.jsp");
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @com.skin.j2ee.annotation.UrlPattern("/system/session/parse.html")
    public void parse() throws ServletException, IOException {
        String passport = this.getTrimString("passport");
        UserSession userSession = Client.parse(passport);

        if(userSession != null) {
            JsonUtil.success(this.request, this.response, userSession);
        }
        else {
            JsonUtil.error(this.request, this.response, 500, "非法的认证码！");
        }
    }
}
