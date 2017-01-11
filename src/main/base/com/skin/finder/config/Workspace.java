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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.config.Config;
import com.skin.config.Skinx;
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
        Workspace workspace = new Workspace();
        InputStream inputStream = Skinx.getInputStream("META-INF/conf/workspace.xml");

        if(inputStream != null) {
            try {
                StringResource.load(inputStream, workspace);
            }
            catch(Exception e) {
                logger.warn(e.getMessage(), e);
            }
            finally {
                try {
                    inputStream.close();
                }
                catch(IOException e) {
                }
            }
        }
        return workspace;
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
