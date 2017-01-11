/*
 * $RCSfile: AccessLogAction.java,v $$
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
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skin.finder.manager.AccessLogManager;
import com.skin.finder.model.AccessLog;
import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.sso.Client;
import com.skin.j2ee.sso.session.UserSession;
import com.skin.j2ee.util.CookieUtil;
import com.skin.j2ee.util.Request;
import com.skin.util.IP;
import com.skin.util.StringUtil;

/**
 * <p>Title: AccessLogAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @version 1.0
 */
public class AccessLogAction extends BaseAction {
    /**
     * @throws ServletException
     * @throws IOException
     */
    @com.skin.j2ee.annotation.UrlPattern("/analytics/log.html")
    public void log() throws ServletException, IOException {
        AccessLogManager accessLogManager = new AccessLogManager();
        AccessLog accessLog = this.getAccessLog(this.request, this.response);
        accessLogManager.create(accessLog);
        this.response.setContentType("image/jpeg; charset=UTF-8");
    }

    /**
     * @param request
     * @param response
     * @return AccessLog
     */
    public AccessLog getAccessLog(HttpServletRequest request, HttpServletResponse response) {
        UserSession userSession = Client.getSession(this.getRequest());
        AccessLog accessLog = new AccessLog();
        accessLog.setAccessTime(new Date());
        accessLog.setLocalIp(IP.LOCAL);
        accessLog.setThreadName(StringUtil.right(Thread.currentThread().getName(), 64));
        accessLog.setRemoteHost(Request.getRemoteAddress(request));
        accessLog.setRequestMethod(request.getMethod());
        accessLog.setRequestProtocol(request.getProtocol());
        accessLog.setRequestUrl(request.getParameter("url"));
        accessLog.setRequestReferer(request.getParameter("ref"));
        accessLog.setClientId(CookieUtil.getValue(request, "clientId"));
        accessLog.setClientUserAgent(request.getHeader("User-Agent"));
        accessLog.setClientCookie(request.getHeader("Cookie"));

        if(userSession != null) {
            accessLog.setUserId(userSession.getUserId());
            accessLog.setUserName(userSession.getNickName());
        }
        else {
            accessLog.setUserId(Long.valueOf(0L));
            accessLog.setUserName("");
        }

        if(accessLog.getRemoteHost() == null) {
            accessLog.setRemoteHost("");
        }

        if(accessLog.getRequestMethod() == null) {
            accessLog.setRequestMethod("get");
        }

        if(accessLog.getRequestProtocol() == null) {
            accessLog.setRequestProtocol("HTTP/1.0");
        }

        if(accessLog.getRequestUrl() == null) {
            accessLog.setRequestUrl("");
        }

        if(accessLog.getRequestReferer() == null) {
            accessLog.setRequestReferer("");
        }

        if(accessLog.getClientId() == null) {
            String clientId = UUID.randomUUID().toString();
            accessLog.setClientId(clientId);
            CookieUtil.setCookie(response, "clientId", clientId, null, "/", 3 * 365 * 60 * 60 * 24, false);
        }

        if(accessLog.getClientUserAgent() == null) {
            accessLog.setClientUserAgent("");
        }

        if(accessLog.getClientCookie() == null) {
            accessLog.setClientCookie("");
        }
        return accessLog;
    }
}
