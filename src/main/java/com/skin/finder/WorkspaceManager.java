/*
 * $RCSfile: WorkspaceManager.java,v $
 * $Revision: 1.1 $
 * $Date: 2013-10-15 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.cluster.ClusterManager;
import com.skin.finder.cluster.Host;
import com.skin.finder.cluster.Workspace;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.config.Constants;

/**
 * <p>Title: WorkspaceManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class WorkspaceManager {
    private static final Logger logger = LoggerFactory.getLogger(WorkspaceManager.class);
    private static final Map<String, Workspace> workspaces = load();

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(workspaces);
    }

    /**
     * default
     */
    private WorkspaceManager() {
    }

    /**
     * @param name
     * @return String
     */
    public static String getWork(String name) {
        Workspace workspace = workspaces.get(name);
        return (workspace != null ? workspace.getWork() : null);
    }

    /**
     * @param name
     * @return boolean
     */
    public static boolean getReadonly(String name) {
        Workspace workspace = workspaces.get(name);
        return (workspace != null ? workspace.getReadonly() : false);
    }

    /**
     * @return List<String>
     */
    public static List<String> getWorkspaces() {
        List<String> list = new ArrayList<String>();
        list.addAll(workspaces.keySet());
        return list;
    }

    /**
     * @return Workspace
     */
    private static Map<String, Workspace> load() {
        Map<String, Workspace> map = new LinkedHashMap<String, Workspace>();
        String name = ConfigFactory.getString(Constants.CLUSTER_NODE_NAME);
        Host self = ClusterManager.getHost(name);

        if(self == null) {
            logger.warn("Can't init workspace: {}", name);
            return map;
        }

        List<Workspace> workspaces = self.getWorkspaces();
        logger.info("host: {}", self.toString());
        logger.info("workspaces: {}", workspaces);

        if(workspaces != null) {
            for(Workspace workspace : workspaces) {
                map.put(workspace.getName(), workspace);
            }
        }
        return map;
    }
}
