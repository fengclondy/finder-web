/*
 * $RCSfile: Workspace.java,v $
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

/**
 * <p>Title: Workspace</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Workspace {
    private Map<String, String> context;

    /**
     * default
     */
    public Workspace() {
        this.context = new LinkedHashMap<String, String>();
    }

    /**
     * @param name
     * @param work
     */
    public Workspace(String name, String work) {
        this.context = new LinkedHashMap<String, String>();
        this.context.put(name, work);
    }

    /**
     * @param context
     */
    public Workspace(Map<String, String> context) {
        this.context = new LinkedHashMap<String, String>();

        if(context != null) {
            this.context.putAll(context);
        }
    }

    /**
     * @param name
     * @param work
     */
    public void add(String name, String work) {
        this.context.put(name, work);
    }

    /**
     * @param name
     * @return String
     */
    public String getWork(String name) {
        return this.context.get(name);
    }

    /**
     * @return List<String>
     */
    public List<String> getWorkspaces() {
        List<String> list = new ArrayList<String>();
        list.addAll(this.context.keySet());
        return list;
    }
}
