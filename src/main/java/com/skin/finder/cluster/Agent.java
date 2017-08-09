/*
 * $RCSfile: Agent.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.cluster;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.config.ConfigFactory;
import com.skin.finder.config.Constants;
import com.skin.finder.http.ProxyDispatcher;
import com.skin.finder.util.StringUtil;
import com.skin.finder.util.URLParameter;

/**
 * <p>Title: Agent</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Agent {
    private static final Logger logger = LoggerFactory.getLogger(ClusterManager.class);

    /**
     * 向master注册自己
     * @throws IOException
     */
    public static void registe() throws IOException {
        String master = ConfigFactory.getMaster();
        String host = ConfigFactory.getHostName();
        String key = ConfigFactory.getSecurityKey();
        Host node = ClusterManager.getHost(master);

        StringBuilder buffer = new StringBuilder();
        buffer.append(node.getUrl());
        buffer.append("?action=finder.custer.registe");
        buffer.append("&host=");
        buffer.append(URLEncoder.encode(host, "utf-8"));
        buffer.append("&key=");
        buffer.append(URLEncoder.encode(key, "utf-8"));
        logger.info("touch: {}", buffer.toString());
        // HttpUtil.touch(url);
    }

    /**
     * @param request
     * @param response
     * @return boolean
     * @throws IOException
     */
    public static boolean dispatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String queryString = request.getQueryString();
        URLParameter params = URLParameter.parse(queryString);
        return dispatch(request, response, params.getString("host"));
    }

    /**
     * @param request
     * @param response
     * @param host
     * @return boolean
     * @throws IOException
     */
    public static boolean dispatch(HttpServletRequest request, HttpServletResponse response, String host) throws IOException {
        if(StringUtil.isBlank(host)) {
            return false;
        }

        String self = ConfigFactory.getString(Constants.CLUSTER_NODE_NAME);

        if(host.equals(self)) {
            return false;
        }

        logger.debug("agent.host: {}", host);

        Host node = ClusterManager.getHost(host);
        String url = node.getUrl();

        logger.debug("agent: {}, {}", host, url);
        ProxyDispatcher.dispatch(request, response, url);
        return true;
    }
}
