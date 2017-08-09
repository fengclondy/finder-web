/*
 * $RCSfile: ErrorServlet.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skin.finder.servlet.template.ErrorTemplate;
import com.skin.finder.web.servlet.BaseServlet;

/**
 * <p>Title: ErrorServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ErrorServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @param request
     * @param response
     * @param code
     * @param message
     * @throws ServletException 
     * @throws IOException 
     */
    public static void error(HttpServletRequest request, HttpServletResponse response, int code, String message) throws IOException, ServletException {
        request.setAttribute("code", code);
        request.setAttribute("message", message);
        ErrorTemplate.execute(request, response);
    }
}
