/*
 * $RCSfile: SimpleUserManager.java,v $$
 * $Revision: 1.1 $
 * $Date: 2016-10-17 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.security;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.util.IO;
import com.skin.finder.util.StringUtil;

/**
 * <p>Title: SimpleUserManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class SimpleUserManager implements UserManager {
    private String home;
    private static final Logger logger = LoggerFactory.getLogger(SimpleUserManager.class);

    /**
     * @param home
     */
    public SimpleUserManager(String home) {
        this.home = home;
    }

    /**
     * @param home
     * @return SimpleUserManager
     */
    public static SimpleUserManager getInstance(File home) {
        return new SimpleUserManager(home.getAbsolutePath());
    }

    /**
     * @param servletContext
     * @return SimpleUserManager
     */
    public SimpleUserManager getInstance(ServletContext servletContext) {
        String home = servletContext.getRealPath("/WEB-INF/user");
        return new SimpleUserManager(home);
    }

    /**
     * @param userName
     * @return User
     */
    @Override
    public User getByName(String userName) {
        return this.load(new File(this.home, userName + ".dat"));
    }

    /**
     * @param user
     */
    @Override
    public int update(User user) {
        String userName = user.getUserName();

        try {
            this.save(user, new File(this.home, userName + ".dat"));
            return 1;
        }
        catch(IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public int create(User user) {
        String userName = user.getUserName();
        long userId = Math.abs(userName.hashCode());
        user.setUserId(userId);

        try {
            this.save(user, new File(this.home, userName + ".dat"));
            return 1;
        }
        catch(IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public int delete(String userName) {
        return 0;
    }

    /**
     * @param file
     * @return User
     */
    public User load(File file) {
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(file);
            return load(inputStream, "utf-8");    
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        finally {
            IO.close(inputStream);
        }
        return null;
    }

    /**
     * @param inputStream
     * @param charset
     * @return Map<String, String>
     */
    private User load(InputStream inputStream, String charset) {
        if(inputStream != null) {
            try {
                return load(new InputStreamReader(inputStream, charset));
            }
            catch(IOException e) {
                logger.warn(e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * @param reader
     * @return Map<String, String>
     */
    private User load(Reader reader) {
        if(reader == null) {
            return null;
        }

        try {
            String line = null;
            BufferedReader buffer = new BufferedReader(reader);
            User user = new User();

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
                        this.setProperty(user, name, value);
                    }
                }
            }
            return null;
        }
        catch(IOException e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    /**
     * @param file
     * @param user
     * @throws IOException 
     */
    private void save(User user, File file) throws IOException {
        File parent = file.getParentFile();

        if(!parent.exists()) {
            parent.mkdirs();
        }

        String content = this.stringify(user);
        IO.write(file, content.getBytes("utf-8"));
    }

    /**
     * @param user
     * @return String
     */
    private String stringify(User user) {
        StringBuilder buffer = new StringBuilder();
        this.append(buffer, "userId", user.getUserId());
        this.append(buffer, "groupId", user.getGroupId());
        this.append(buffer, "userName", user.getUserName());
        this.append(buffer, "nickName", user.getNickName());
        this.append(buffer, "userSalt", user.getUserSalt());
        this.append(buffer, "password", user.getPassword());
        this.append(buffer, "userLevel", user.getUserLevel());
        this.append(buffer, "userGender", user.getUserGender());
        this.append(buffer, "userAvatar", user.getUserAvatar());
        this.append(buffer, "userSignature", user.getUserSignature());
        this.append(buffer, "userEmail", user.getUserEmail());
        this.append(buffer, "createTime", user.getCreateTime());
        this.append(buffer, "updateTime", user.getUpdateTime());
        this.append(buffer, "expireTime", user.getExpireTime());
        this.append(buffer, "lastLoginTime", user.getLastLoginTime());
        this.append(buffer, "lastLoginIp", user.getLastLoginIp());
        this.append(buffer, "userStatus", user.getUserStatus());
        this.append(buffer, "userOrder", user.getUserOrder());
        return buffer.toString();
    }

    /**
     * @param user
     * @param name
     * @param value
     */
    private void setProperty(User user, String name, String value) {
        if(name.equals("userId")) {
            user.setUserId(Long.parseLong(value));
        }
        else if(name.equals("groupId")) {
            user.setGroupId(Long.parseLong(value));
        }
        else if(name.equals("userName")) {
            user.setUserName(value);
        }
        else if(name.equals("nickName")) {
            user.setNickName(value);
        }
        else if(name.equals("userSalt")) {
            user.setUserSalt(value);
        }
        else if(name.equals("password")) {
            user.setPassword(value);
        }
        else if(name.equals("userLevel")) {
            user.setUserLevel(Integer.parseInt(value));
        }
        else if(name.equals("userGender")) {
            user.setUserGender(Integer.parseInt(value));
        }
        else if(name.equals("userAvatar")) {
            user.setUserAvatar(value);
        }
        else if(name.equals("userSignature")) {
            user.setUserSignature(value);
        }
        else if(name.equals("userEmail")) {
            user.setUserEmail(value);
        }
        else if(name.equals("createTime")) {
            user.setCreateTime(new Date(Long.parseLong(value)));
        }
        else if(name.equals("updateTime")) {
            user.setUpdateTime(new Date(Long.parseLong(value)));
        }
        else if(name.equals("expireTime")) {
            user.setExpireTime(new Date(Long.parseLong(value)));
        }
        else if(name.equals("lastLoginTime")) {
            user.setLastLoginTime(new Date(Long.parseLong(value)));
        }
        else if(name.equals("lastLoginIp")) {
            user.setLastLoginIp(value);
        }
        else if(name.equals("userStatus")) {
            user.setUserStatus(Integer.parseInt(value));
        }
        else if(name.equals("userOrder")) {
            user.setUserOrder(Integer.parseInt(value));
        }
    }

    /**
     * @param buffer
     * @param name
     * @param value
     * @return StringBuilder
     */
    private StringBuilder append(StringBuilder buffer, String name, Object value) {
        if(value == null) {
            return buffer;
        }

        if(value instanceof Date) {
            buffer.append("name = ");
            buffer.append(((Date)value).getTime());
            buffer.append("\r\n");
            return buffer;
        }

        if(value instanceof Number) {
            buffer.append("name = ");
            buffer.append(value.toString());
            buffer.append("\r\n");
            return buffer;
        }

        buffer.append("name = ");
        buffer.append(StringUtil.escape(value.toString()));
        buffer.append("\r\n");
        return buffer;
    }
}
