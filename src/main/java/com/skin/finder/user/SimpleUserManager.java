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
package com.skin.finder.user;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
public class SimpleUserManager {
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
    public static SimpleUserManager getInstance(ServletContext servletContext) {
        String home = servletContext.getRealPath("/WEB-INF/user");
        return new SimpleUserManager(home);
    }

    /**
     * @param userName
     * @return Map<String, String>
     */
    public Map<String, String> getByName(String userName) {
        return this.load(new File(this.home, userName + ".dat"));
    }

    /**
     * @param userName
     * @param password
     * @param userSalt
     * @throws IOException 
     */
    public void update(String userName, String password, String userSalt) throws IOException {
        long userId = Math.abs(userName.hashCode());
        Map<String, String> user = new LinkedHashMap<String, String>();
        user.put("userId", String.valueOf(userId));
        user.put("userName", userName);
        user.put("password", password);
        user.put("userSalt", userSalt);
        this.save(new File(this.home, userName + ".dat"), user);
    }

    /**
     * @param file
     * @return Map<String, String>
     */
    public Map<String, String> load(File file) {
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
    private static Map<String, String> load(InputStream inputStream, String charset) {
        Map<String, String> map = new HashMap<String, String>();

        if(inputStream != null) {
            try {
                return load(new InputStreamReader(inputStream, charset));
            }
            catch(IOException e) {
                logger.warn(e.getMessage(), e);
            }
        }
        return map;
    }

    /**
     * @param reader
     * @return Map<String, String>
     */
    private static Map<String, String> load(Reader reader) {
        Map<String, String> map = new HashMap<String, String>();

        if(reader != null) {
            try {
                String line = null;
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
                            map.put(name, value);
                        }
                    }
                }
            }
            catch(IOException e) {
            }
        }
        return map;
    }

    /**
     * @param file
     * @param user
     * @throws IOException 
     */
    private void save(File file, Map<String, String> user) throws IOException {
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
    private String stringify(Map<String, String> user) {
        StringBuilder buffer = new StringBuilder();

        for(Map.Entry<String, String> entry : user.entrySet()) {
            buffer.append(entry.getKey());
            buffer.append(" = ");
            buffer.append(StringUtil.escape(entry.getValue()));
            buffer.append("\r\n");
        }
        return buffer.toString();
    }
}
