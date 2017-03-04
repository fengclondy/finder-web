/*
 * $RCSfile: KeyInfo.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-03-04 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.config;

/**
 * <p>Title: KeyInfo</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class KeyInfo {
    private String name;
    private String title;
    private String type;
    private String value;
    private String description;
    private Options options;

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }
    
    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * @return the type
     */
    public String getType() {
        return this.type;
    }
    
    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * @return the value
     */
    public String getValue() {
        return this.value;
    }
    
    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }
    
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the options
     */
    public Options getOptions() {
        return this.options;
    }

    /**
     * @param options the options to set
     */
    public void setOptions(Options options) {
        this.options = options;
    }
}
