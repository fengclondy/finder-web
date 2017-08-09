/*
 * $RCSfile: Cluster.java,v $
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
 * <p>Title: Cluster</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Cluster {
    private Map<String, Host> lookup;

    /**
     * default
     */
    public Cluster() {
        this.lookup = new LinkedHashMap<String, Host>();
    }

    /**
     * @param host
     */
    public void add(Host host) {
        String name = host.getName();

        if(name == null || (name = name.trim()).length() < 1) {
            throw new NullPointerException("name must be not null");
        }
        this.lookup.put(name, host);
    }

    /**
     * @param name
     * @return Host
     */
    public Host getHost(String name) {
        return this.lookup.get(name);
    }

    /**
     * @param name
     * @param workspace
     * @return Workspace
     */
    public Workspace getWorkspace(String name, String workspace) {
        Host host = this.getHost(name);
        return (host != null ? host.getWorkspace(workspace) : null);
    }

    /**
     * @return List<Host>
     */
    public List<Host> getHosts() {
        List<Host> hosts = new ArrayList<Host>();
        hosts.addAll(this.lookup.values());
        return hosts;
    }

    /**
     * destroy
     */
    public void destroy() {
        if(this.lookup != null) {
            for(Map.Entry<String, Host> entry : this.lookup.entrySet()) {
                Host host = entry.getValue();
                host.destroy();
            }
            this.lookup.clear();
            this.lookup = null;
        }
    }
}
