/*
 * $RCSfile: DispatchServlet.java,v $
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.web.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skin.finder.util.StringUtil;
import com.skin.finder.web.ActionDispatcher;

/**
 * <p>Title: DispatchServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class DispatchServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private ActionDispatcher dispatcher;

    /**
     * default
     */
    public DispatchServlet() {
        this.dispatcher = new ActionDispatcher();
    }

    /**
     * @param servletConfig
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        String[] packages = this.getArray(servletConfig, "packages");
        this.dispatcher.setPackages(packages);
        this.dispatcher.init(this.getServletContext());
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        this.dispatcher.service(request, response);
    }

    /**
     * @param servletConfig
     * @return String[]
     */
    private String[] getArray(ServletConfig servletConfig, String name) {
        String content = servletConfig.getInitParameter(name);

        if(content == null) {
            return new String[0];
        }
        return StringUtil.split(content, ",", true, true);
    }

    /**
     * destroy
     */
    @Override
    public void destroy() {
        if(this.dispatcher != null) {
            this.dispatcher.destroy();
            this.dispatcher = null;
        }
    }
}
