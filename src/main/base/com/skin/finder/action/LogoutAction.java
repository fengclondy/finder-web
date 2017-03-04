/*
 * $RCSfile: LogoutAction.java,v $$
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

import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.annotation.UrlPattern;
import com.skin.j2ee.util.CookieUtil;

/**
 * <p>Title: LogoutAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class LogoutAction extends BaseAction {
    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/logout.html")
    public void execute() throws ServletException, IOException {
        CookieUtil.remove(this.response, "passport", null);
        this.redirect("${contextPath}/finder/login.html");
    }
}
