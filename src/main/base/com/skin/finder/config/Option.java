/*
 * $RCSfile: Option.java,v $$
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
 * <p>Title: Option</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Option {
    private String text;
    private String value;

    /**
     *
     */
    public Option() {
    }

    /**
     * @param text
     * @param value
     */
    public Option(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * @return the text
     */
    public String getText() {
        return this.text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
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
}
