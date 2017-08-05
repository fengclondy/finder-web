/*
 * $RCSfile: Client.java,v $
 * $Revision: 1.1 $
 * $Date: 2011-4-7 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.web.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skin.finder.user.UserSession;
import com.skin.finder.util.Base64;
import com.skin.finder.util.Digest;
import com.skin.finder.util.Hex;

/**
 * <p>Title: Client</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Client {
    /**
     *
     */
    public static String COOKIE_NAME;

    /**
     *
     */
    public static String MD5KEY;

    /**
     *
     */
    public static String SEPARATOR;

    /**
     *
     */
    public static String CHARSET;

    /**
     *
     */
    public static final Properties properties = Client.load();

    static{
        Client.CHARSET = properties.getProperty("cookie.charset", "UTF-8");
        Client.SEPARATOR = properties.getProperty("cookie.passport.separator", "|");
        Client.MD5KEY = properties.getProperty("cookie.passport.md5.key", "cf9=kmi@rqo$a8p=abq?a2z$fae+x5y%uwz$jh3?n2d+vv6=k2d@duw@k55*csz?");
        Client.COOKIE_NAME = properties.getProperty("cookie.passport.name", "passport");
    }

    /**
     * @param request
     * @param response
     * @param userSession
     * @param domain
     * @param path
     * @param expiry
     * @param secure
     */
    public static void registe(HttpServletRequest request, HttpServletResponse response, UserSession userSession, String domain, String path, int expiry, boolean secure) {
        String certificate = Client.getCertificate(userSession);
        CookieUtil.setCookie(response, COOKIE_NAME, certificate, domain, path, expiry, secure);
    }

    /**
     * @param userSession
     * @return String
     */
    public static String getCertificate(UserSession userSession) {
        return Client.getCertificate(userSession, MD5KEY);
    }

    /**
     * @param userSession
     * @param key
     * @return String
     */
    public static String getCertificate(UserSession userSession, String key) {
        StringBuilder buffer = Client.build(userSession, key);
        String signature = md5(buffer.toString());
        int length = buffer.length();
        buffer.delete(length - key.length(), length);
        buffer.append(Base64.encode(signature, CHARSET));
        return buffer.toString();
    }

    /**
     * @param request
     * @return UserSession
     */
    public static UserSession getSession(HttpServletRequest request) {
        Object object = request.getAttribute("finderUserSession");

        if(object instanceof Integer) {
            return null;
        }

        UserSession userSession = null;

        if(object instanceof UserSession) {
            userSession = (UserSession)(object);
        }

        if(userSession == null) {
            String certificate = CookieUtil.getValue(request, COOKIE_NAME);

            if(certificate != null) {
                userSession = parse(certificate, MD5KEY);
            }
        }

        if(userSession != null) {
            request.setAttribute("finderUserSession", userSession);
        }
        else {
            request.setAttribute("finderUserSession", Integer.valueOf(0));
        }
        return userSession;
    }

    /**
     * @param request
     * @param name
     * @param key
     * @return UserSession
     */
    public static UserSession getUserSession(HttpServletRequest request, String name, String key) {
        String certificate = CookieUtil.getValue(request, name);

        if(certificate != null) {
            return parse(certificate, key);
        }
        return null;
    }

    /**
     * @param certificate
     * @return UserSession
     */
    public static UserSession parse(String certificate) {
        return parse(certificate, MD5KEY);
    }

    /**
     * @param certificate
     * @param key
     * @return UserSession
     */
    public static UserSession parse(String certificate, String key) {
        if(certificate == null) {
            return null;
        }

        String[] splits = certificate.split("\\|");

        if(splits == null || splits.length < 7) {
            return null;
        }

        long sessionId = parseLong(Base64.decode(splits[0], CHARSET));
        long appId = parseLong(Base64.decode(splits[1], CHARSET));
        long userId = parseLong(Base64.decode(splits[2], CHARSET));
        long timeMillis = parseLong(Base64.decode(splits[5], CHARSET));

        Date createTime = new Date(timeMillis);
        UserSession userSession = new UserSession();
        userSession.setSessionId(sessionId);
        userSession.setAppId(appId);
        userSession.setUserId(userId);
        userSession.setUserName(Base64.decode(splits[3], CHARSET));
        userSession.setNickName(Base64.decode(splits[4], CHARSET));
        userSession.setCreateTime(createTime);
        userSession.setSignature(Base64.decode(splits[6], CHARSET));

        if(key != null) {
            if(Client.validate(userSession, key)) {
                return userSession;
            }
        }
        else {
            return userSession;
        }
        return null;
    }

    /**
     * @param userSession
     * @param key
     * @return StringBuilder
     */
    public static StringBuilder build(UserSession userSession, String key) {
        long timeMillis = userSession.getCreateTime().getTime();
        String sessionId = Base64.encode(String.valueOf(userSession.getSessionId()), CHARSET);
        String appId = Base64.encode(String.valueOf(userSession.getAppId()), CHARSET);
        String userId = Base64.encode(String.valueOf(userSession.getUserId()), CHARSET);
        String userName = Base64.encode(userSession.getUserName(), CHARSET);
        String nickName = Base64.encode(userSession.getNickName(), CHARSET);
        String createTime = Base64.encode(String.valueOf(timeMillis), CHARSET);
        StringBuilder buffer = new StringBuilder();
        buffer.append(sessionId);
        buffer.append(SEPARATOR).append(appId);
        buffer.append(SEPARATOR).append(userId);
        buffer.append(SEPARATOR).append(userName);
        buffer.append(SEPARATOR).append(nickName);
        buffer.append(SEPARATOR).append(createTime);
        buffer.append(SEPARATOR).append(key);
        return buffer;
    }

    /**
     * @param userSession
     * @param key
     * @return boolean
     */
    public static boolean validate(UserSession userSession, String key) {
        StringBuilder buffer = Client.build(userSession, key);
        String signature = md5(buffer.toString());
        return (signature.equals(userSession.getSignature()));
    }

    /**
     * @param response
     */
    public static void remove(HttpServletResponse response) {
        CookieUtil.remove(response, COOKIE_NAME);
    }

    /**
     * @param response
     * @param domain
     */
    public static void remove(HttpServletResponse response, String domain) {
        CookieUtil.remove(response, COOKIE_NAME, domain);
    }

    /**
     * @param userSession
     * @return String
     */
    public static String toJson(UserSession userSession) {
        StringBuilder buffer = new StringBuilder();

        if(userSession != null) {
            long createTime = 0L;

            if(userSession.getCreateTime() != null) {
                createTime = userSession.getCreateTime().getTime();
            }

            buffer.append("{");
            buffer.append("\"sessionId\": ");
            buffer.append("\"").append(userSession.getSessionId()).append("\"");
            buffer.append(", \"appId\": ");
            buffer.append("\"").append(userSession.getAppId()).append("\"");
            buffer.append(", \"userId\": ");
            buffer.append("\"").append(userSession.getUserId()).append("\"");
            buffer.append(", \"userName\": ");
            buffer.append("\"").append(userSession.getUserName()).append("\"");
            buffer.append(", \"nickName\": ");
            buffer.append("\"").append(userSession.getNickName()).append("\"");
            buffer.append(", \"createTime\": ");
            buffer.append("\"").append(createTime).append("\"");
            buffer.append(", \"signature\": ");
            buffer.append("\"").append(userSession.getSignature()).append("\"");
            buffer.append("}");
        }
        else {
            buffer.append("{}");
        }
        return buffer.toString();
    }

    /**
     * @return String
     */
    public static String getDomain() {
        return properties.getProperty("cookie.domain", null);
    }

    /**
     * @return String
     */
    public static String getCookieName() {
        return properties.getProperty("cookie.cookiename", "passport");
    }

    /**
     * @return String
     */
    public static String getMd5Key() {
        return properties.getProperty("cookie.md5key", "cf9=kmi@rqo$a8p=abq?a2z$fae+x5y%uwz$jh3?n2d+vv6=k2d@duw@k55*csz?");
    }

    /**
     * @param key
     * @param defaultValue
     * @return String
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * @param num
     * @return long
     */
    private static long parseLong(String num) {
        try {
            return Long.parseLong(num, 10);
        }
        catch(Exception e) {
        }
        return 0;
    }

    /**
     * @return Properties
     */
    private static Properties load() {
        InputStream inputStream = Client.class.getClassLoader().getResourceAsStream("cookie.properties");
        Properties properties = new Properties();

        if(inputStream != null) {
            InputStreamReader reader = null;

            try {
                String line = null;
                reader = new InputStreamReader(inputStream, "utf-8");
                BufferedReader buffer = new BufferedReader(reader);

                while((line = buffer.readLine()) != null) {
                    line = line.trim();

                    if(line.length() < 1) {
                        continue;
                    }

                    if(line.startsWith("#")) {
                        continue;
                    }

                    int i = line.indexOf("=");

                    if(i > -1) {
                        String name = line.substring(0, i).trim();
                        String value = line.substring(i + 1).trim();

                        if(name.length() > 0 && value.length() > 0) {
                            properties.setProperty(name, value);
                        }
                    }
                }
            }
            catch(IOException e) {
            }
        }
        return properties;
    }

    /**
     * @param text
     * @return String
     */
    public static String md5(String text) {
        try {
            return Hex.encode(Digest.md5(text.getBytes("utf-8"), null));
        }
        catch(UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param text
     * @return String
     */
    public static String sha1(String text) {
        try {
            return Hex.encode(Digest.sha1(text.getBytes("utf-8"), null));
        }
        catch(UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
