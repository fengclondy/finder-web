/*
 * $RCSfile: JspNode.java,v $
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.jsp;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Title: JspNode</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class JspNode {
    private int nodeType;
    private String content;
    private Map<String, String> attributes;

    /**
     * @param nodeType
     */
    public JspNode(int nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * @param nodeType
     * @param content
     */
    public JspNode(int nodeType, String content) {
        this.nodeType = nodeType;
        this.content = content;
    }

    /**
     * @return the nodeType
     */
    public int getNodeType() {
        return this.nodeType;
    }

    /**
     * @param nodeType the nodeType to set
     */
    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return this.content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the attributes
     */
    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    /**
     * @param name
     * @param value
     */
    public void setAttribute(String name, String value) {
        if(this.attributes == null) {
            this.attributes = new LinkedHashMap<String, String>();
        }
        this.attributes.put(name, value);
    }

    /**
     * @param name
     * @return String
     */
    public String getAttribute(String name) {
        if(this.attributes == null) {
            return null;
        }
        return this.attributes.get(name);
    }
}
