/*
 * $RCSfile: Options.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-03-04 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.config;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: Options</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Options {
    private String type;
    private boolean multiple;
    private List<Option> options;

    /**
     * 
     */
    public Options() {
        this.options = new ArrayList<Option>();
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
     * @return the multiple
     */
    public boolean isMultiple() {
        return this.multiple;
    }
    
    /**
     * @param multiple the multiple to set
     */
    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    /**
     * @return the options
     */
    public List<Option> getOptions() {
        return this.options;
    }

    /**
     * @param options the options to set
     */
    public void setOptions(List<Option> options) {
        this.options = options;
    }

    /**
     * @param option
     */
    public void add(Option option) {
        this.options.add(option);
    }
}
