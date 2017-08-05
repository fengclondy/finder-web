/*
 * $RCSfile: JspServlet.java,v $
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.web.servlet;

import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;

import com.skin.finder.util.HtmlUtil;

/**
 * <p>Title: JspServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class JspServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @param out
     * @param value
     */
    protected void print(PrintWriter out, Object value) {
        if(value == null) {
            return;
        }

        if(value instanceof Number || value instanceof Boolean) {
            out.write(value.toString());
            return;
        }
        out.write(HtmlUtil.encode(value.toString()));
    }
}
