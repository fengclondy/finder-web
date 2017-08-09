/*
 * $RCSfile: ClusterUtil.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.cluster;

import java.util.List;

import com.skin.finder.util.HtmlUtil;
import com.skin.finder.util.StringUtil;

/**
 * <p>Title: ClusterUtil</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ClusterUtil {
    private static final String CRLF = "\r\n";

    /**
     * default
     */
    private ClusterUtil() {
    }

    /**
     * @param cluster
     * @return String
     */
    public static String build(Cluster cluster) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(CRLF);
        buffer.append("<cluster>").append(CRLF);
        List<Host> hosts = cluster.getHosts();

        if(hosts != null && hosts.size() > 0) {
            for(Host host : hosts) {
                build(host, buffer, "    ");
            }
        }

        buffer.append("</cluster>").append(CRLF);
        return buffer.toString();
    }

    /**
     * @param host
     * @return String
     */
    public static String build(Host host) {
        return build(host, new StringBuilder(), "").toString();
    }

    /**
     * @param host
     * @param buffer
     * @param indent
     * @return String
     */
    public static StringBuilder build(Host host, StringBuilder buffer, String indent) {
        buffer.append(indent);
        buffer.append("<host");

        if(StringUtil.notBlank(host.getName())) {
            buffer.append(" name=\"");
            buffer.append(HtmlUtil.encode(host.getName()));
            buffer.append("\"");
        }

        if(StringUtil.notBlank(host.getUrl())) {
            buffer.append(" url=\"");
            buffer.append(HtmlUtil.encode(host.getUrl()));
            buffer.append("\"");
        }
        buffer.append(">").append(CRLF);

        List<Workspace> workspaces = host.getWorkspaces();

        if(workspaces != null && workspaces.size() > 0) {
            for(Workspace workspace : workspaces) {
                build(workspace, buffer, "        ");
            }
        }

        buffer.append(indent);
        buffer.append("</host>").append(CRLF);
        return buffer;
    }

    /**
     * @param workspace
     * @return String
     */
    public static String build(Workspace workspace) {
        return build(workspace, new StringBuilder(), "").toString();
    }

    /**
     * @param workspace
     * @param buffer
     * @param indent
     * @return StringBuilder
     */
    private static StringBuilder build(Workspace workspace, StringBuilder buffer, String indent) {
        buffer.append(indent);
        buffer.append("<workspace");

        if(StringUtil.notBlank(workspace.getName())) {
            buffer.append(" name=\"");
            buffer.append(HtmlUtil.encode(workspace.getName()));
            buffer.append("\"");
        }
        
        if(StringUtil.notBlank(workspace.getWork())) {
            buffer.append(" work=\"");
            buffer.append(HtmlUtil.encode(workspace.getWork()));
            buffer.append("\"");
        }

        buffer.append(" readonly=\"");
        buffer.append(workspace.getReadonly());
        buffer.append("/>").append(CRLF);
        return buffer;
    }
}
