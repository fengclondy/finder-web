/*
 * $RCSfile: NodeType.java,v $
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.jsp;

/**
 * <p>Title: NodeType</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class NodeType {
    /**
     * text
     */
    public static final int TEXT = 1000;

    /**
     * <%@ page
     */
    public static final int JSP_DIRECTIVE_PAGE    = 1010;

    /**
     * <%@ taglib
     */
    public static final int JSP_DIRECTIVE_TAGLIB  = 1020;

    /**
     * <%@ include file=""%>
     */
    public static final int JSP_DIRECTIVE_INCLUDE = 1030;

    /**
     * <%! ... %>
     */
    public static final int JSP_DECLARATION = 1040;

    /**
     * <%=xxx%>
     */
    public static final int JSP_EXPRESSION  = 1050;

    /**
     * <% .... %>
     */
    public static final int JSP_SCRIPTLET   = 1060;

    /**
     * <%-- .... --%>
     */
    public static final int JSP_COMMENT    = 1070;

    /**
     * ${ ... }
     */
    public static final int EXPRESSION      = 1080;
}
