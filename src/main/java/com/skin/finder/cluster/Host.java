/*
 * $RCSfile: Host.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.cluster;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: Host</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Host {
    private String name;
    private String url;
    private Map<String, Workspace> workspaces;

    /**
     * default
     */
    public Host() {
        this.workspaces = new LinkedHashMap<String, Workspace>();
    }

    /**
     * @param workspace
     */
    public void add(Workspace workspace) {
        String name = workspace.getName();

        if(name == null || (name = name.trim()).length() < 1) {
            throw new NullPointerException("name must be not null");
        }
        this.workspaces.put(name, workspace);
    }

    /**
     * @param name
     * @return Workspace
     */
    public Workspace getWorkspace(String name) {
        return this.workspaces.get(name);
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return List<Workspace>
     */
    public List<Workspace> getWorkspaces() {
        List<Workspace> list = new ArrayList<Workspace>();
        list.addAll(this.workspaces.values());
        return list;
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("{name: ").append(this.name);
        buffer.append(", url: ").append(this.url);
        buffer.append("}");
        return buffer.toString();
    }

    /**
     * destroy
     */
    public void destroy() {
        if(this.workspaces != null) {
            this.workspaces.clear();
            this.workspaces = null;
        }
    }
}
