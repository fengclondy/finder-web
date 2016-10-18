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
package com.skin.finder.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.resource.PropertyResource;
import com.skin.util.IO;
import com.skin.util.StringUtil;

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
            return PropertyResource.load(inputStream, "utf-8");    
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
