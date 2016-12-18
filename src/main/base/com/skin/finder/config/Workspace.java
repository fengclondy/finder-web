/*
 * $RCSfile: Workspace.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-10-15 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.config.Config;
import com.skin.config.XmlConfigFactory;
import com.skin.resource.StringResource;

/**
 * <p>Title: Workspace</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @version 1.0
 */
public class Workspace extends Config {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(Workspace.class);
    private static final Workspace instance = load();

    /**
     * @return Workspace
     */
    public static Workspace getInstance() {
        return instance;
    }

    /**
     * @return Workspace
     */
    private static Workspace load() {
        Workspace workspace = null;
        String appName = AppConfig.getName();
        String userHome = System.getProperty("user.home");
        File file = new File(userHome, "skinx/" + appName + "/conf/workspace.xml");
        logger.info("try load: {}", file.getAbsolutePath());

        if(file.exists() && file.isFile()) {
            logger.info("load from: {}", file.getAbsolutePath());
            workspace = load(file);
        }

        if(workspace == null) {
            logger.info("load from: META-INF/conf/workspace.xml");
            workspace = XmlConfigFactory.getConfig("META-INF/conf/workspace.xml", Workspace.class);
        }
        return workspace;
    }

    /**
     * @param file
     * @return Workspace
     */
    private static Workspace load(File file) {
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(file);
            Workspace workspace = new Workspace();
            StringResource.load(inputStream, workspace);
            return workspace;
        }
        catch(Exception e) {
            logger.warn(e.getMessage(), e);
        }
        finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                }
                catch(IOException e) {
                }
            }
        }
        return null;
    }

    /**
     * @return List<String>
     */
    public List<String> getWorkspaces() {
        Set<String> set = this.keySet();
        List<String> list = new ArrayList<String>();
        list.addAll(set);
        Collections.sort(list);
        return list;
    }
}
