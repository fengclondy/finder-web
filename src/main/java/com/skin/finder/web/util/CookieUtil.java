/*
 * $RCSfile: CookieUtil.java,v $$
 * $Revision: 1.1  $
 * $Date: 2011-4-9  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title: CookieUtil</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class CookieUtil {
    private CookieUtil(){}

    /**
     * @param response
     * @param name
     * @param value
     */
    public static void setCookie(HttpServletResponse response, String name, String value) {
        CookieUtil.setCookie(response, name, value, null, "/", 60 * 60 * 24 * 7, false);
    }

    /**
     * @param response
     * @param name
     * @param value
     * @param domain
     */
    public static void setCookie(HttpServletResponse response, String name, String value, String domain) {
        CookieUtil.setCookie(response, name, value, domain, "/", 60 * 60 * 24 * 7, false);
    }

    /**
     * @param response
     * @param name
     * @param value
     * @param domain
     * @param path
     * @param expiry
     * @param secure
     */
    public static void setCookie(HttpServletResponse response, String name, String value, String domain, String path, int expiry, boolean secure) {
        String data = null;

        try {
            data = URLEncoder.encode(value, "UTF-8");
        }
        catch(UnsupportedEncodingException e) {
        }

        if(data != null) {
            Cookie cookie = new Cookie(name, data);

            if(domain != null) {
                cookie.setDomain(domain);
            }

            if(path != null) {
                cookie.setPath(path);
            }

            if(expiry > 0) {
                cookie.setMaxAge(expiry);
            }

            cookie.setSecure(secure);
            response.addCookie(cookie);
        }
    }

    /**
     * @param request
     * @param name
     * @return Cookie
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if(cookies != null && cookies.length > 0) {
            for(Cookie cookie : cookies) {
                if(name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }

        return null;
    }

    /**
     * @param request
     * @param name
     * @return String
     */
    public static String getValue(HttpServletRequest request, String name) {
        String result = null;
        Cookie cookie = getCookie(request, name);

        if(cookie != null) {
            try {
                result = URLDecoder.decode(cookie.getValue(), "UTF-8");
            }
            catch(UnsupportedEncodingException e) {
            }
        }

        return result;
    }

    /**
     * @param response
     * @param name
     */
    public static void remove(HttpServletResponse response, String name) {
        CookieUtil.remove(response, name, null);
    }

    /**
     * @param response
     * @param name
     * @param domain
     */
    public static void remove(HttpServletResponse response, String name, String domain) {
        Cookie cookie = new Cookie(name, "");

        if(domain != null) {
            cookie.setDomain(domain);
        }

        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    /**
     * @param name
     * @param value
     * @param domain
     * @param path
     * @param expiry
     * @param secure
     * @return String
     */
    public static String getScript(String name, String value, String domain, String path, int expiry, boolean secure) {
        long timeMillis = System.currentTimeMillis();
        StringBuilder buffer = new StringBuilder();

        buffer.append("\"");
        buffer.append(name);
        buffer.append("=\" + encodeURIComponent(\"");
        buffer.append(value);
        buffer.append("\") + \"; expires=\" + (new Date(");
        buffer.append((timeMillis + expiry));
        buffer.append(")).toUTCString()");

        if(domain != null && domain.trim().length() > 0) {
            buffer.append(" + \"; domain=");
            buffer.append(domain.trim()).append("\"");
        }

        if(path != null && path.trim().length() > 0) {
            buffer.append(" + \"; path=");
            buffer.append(path.trim()).append("\"");
        }

        if(secure) {
            buffer.append(" + \"; secure\"");
        }
        return buffer.toString();
    }
}
