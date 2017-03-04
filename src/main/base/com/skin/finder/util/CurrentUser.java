/*
 * $RCSfile: CurrentUser.java,v $
 * $Revision: 1.1 $
 * $Date: 2009-03-25 $
 *
 * Copyright (C) 2005 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skin.j2ee.sso.Client;
import com.skin.j2ee.sso.session.UserSession;

/**
 * <p>Title: CurrentUser</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class CurrentUser {
    private CurrentUser(){}

    /**
     * @param request
     * @return String
     */
    public static long getSessionId(HttpServletRequest request) {
        UserSession userSession = Client.getSession(request);
        return (userSession != null ? userSession.getSessionId() : null);
    }

    /**
     * @param request
     * @return String
     */
    public static long getAppId(HttpServletRequest request) {
        UserSession userSession = Client.getSession(request);

        if(userSession != null) {
            return userSession.getAppId();
        }
        return 0L;
    }

    /**
     * @param request
     * @return String
     */
    public static long getUserId(HttpServletRequest request) {
        UserSession userSession = Client.getSession(request);

        if(userSession != null) {
            return userSession.getUserId();
        }
        return 0L;
    }

    /**
     * @param request
     * @return String
     */
    public static String getUserName(HttpServletRequest request) {
        UserSession userSession = Client.getSession(request);
        return (userSession != null ? userSession.getUserName() : null);
    }

    /**
     * @param request
     * @return String
     */
    public static String getNickName(HttpServletRequest request) {
        UserSession userSession = Client.getSession(request);
        return (userSession != null ? userSession.getNickName() : null);
    }

    /**
     * @param request
     * @return UserSession
     */
    public static UserSession getUserSession(HttpServletRequest request) {
        return Client.getSession(request);
    }

    /**
     * @param request
     * @return boolean
     */
    public static boolean exists(HttpServletRequest request) {
        return (CurrentUser.getUserId(request) > 0L);
    }

    /**
     * @param request
     * @param users
     * @return boolean
     */
    public static boolean is(HttpServletRequest request, long ... users) {
        long userId = getUserId(request);

        if(userId != 0L && users != null && users.length > 0) {
            for(int i = 0; i < users.length; i++) {
                if(userId == users[i]) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param request
     * @return boolean
     */
    public static boolean isWebmaster(HttpServletRequest request) {
        return isWebmaster(CurrentUser.getUserId(request));
    }

    /**
     * @param userId
     * @return boolean
     */
    public static boolean isWebmaster(long userId) {
        return (userId > 0L && userId < 5L);
    }

    /**
     * @param request
     * @param response
     * @param userSession
     * @param domain
     */
    public static void registe(HttpServletRequest request, HttpServletResponse response, UserSession userSession, String domain) {
        Client.registe(request, response, userSession, domain, "/", 7 * 24 * 60 * 60, false);
    }

    /**
     * @param response
     * @param domain
     */
    public static void logout(HttpServletResponse response, String domain) {
        Client.logout(response, domain);
    }
}
