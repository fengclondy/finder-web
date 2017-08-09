/*
 * $RCSfile: StatusServlet.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skin.finder.Finder;
import com.skin.finder.FinderManager;
import com.skin.finder.cluster.Agent;
import com.skin.finder.cluster.Cluster;
import com.skin.finder.cluster.ClusterManager;
import com.skin.finder.cluster.Host;
import com.skin.finder.cluster.Workspace;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.servlet.template.TreeTemplate;
import com.skin.finder.web.Response;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;

/**
 * <p>Title: StatusServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class TreeServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.tree")
    public void tree(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TreeTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.getHostXml")
    public void getHostXml(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String listUrl = requestURI + "?action=finder.display&amp;";
        String xmlUrl = requestURI + "?action=finder.getWorkspaceXml&amp;";
        Cluster cluster = ClusterManager.getInstance();
        List<Host> hosts = cluster.getHosts();

        String xml = FinderManager.getHostXml(hosts, listUrl, xmlUrl);
        Response.setCache(response, 0);
        Response.write(request, response, "text/xml; charset=UTF-8", xml);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.getWorkspaceXml")
    public void getWorkspaceXml(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String host = request.getParameter("host");
        String listUrl = requestURI + "?action=finder.display&amp;host=" + host + "&amp;";
        String xmlUrl = requestURI + "?action=finder.getFolderXml&amp;host=" + host + "&amp;";
        Cluster cluster = ClusterManager.getInstance();
        Host node = cluster.getHost(host);
        List<Workspace> workspaces = node.getWorkspaces();

        String xml = FinderManager.getWorkspaceXml(workspaces, listUrl, xmlUrl);
        Response.setCache(response, 0);
        Response.write(request, response, "text/xml; charset=utf-8", xml);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.getFolderXml")
    public void getFolderXml(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String host = ConfigFactory.getHostName();
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String work = Finder.getWorkspace(request, workspace);

        String requestURI = request.getRequestURI();
        String listUrl = requestURI + "?action=finder.display&amp;host=" + URLEncoder.encode(host, "utf-8") + "&amp;";
        String xmlUrl = requestURI + "?action=finder.getFolderXml&amp;host=" + URLEncoder.encode(host, "utf-8") + "&amp;";
        FinderManager finderManager = new FinderManager(work);
        String xml = finderManager.getFolderXml(workspace, path, listUrl, xmlUrl);
        Response.setCache(response, 0);
        Response.write(request, response, "text/xml; charset=UTF-8", xml);
    }
}
