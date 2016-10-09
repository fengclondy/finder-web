/*
 * $RCSfile: SystemVariable.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-05-06 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.model;

import java.io.Serializable;

/**
 * <p>Title: SystemVariable</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class SystemVariable implements Serializable {
    private static final long serialVersionUID = 1L;
    private String variableName;
    private String variableValue;
    private String variableDesc;

    /**
     *
     */
    public SystemVariable() {
    }

    /**
     * @param name
     * @param value
     */
    public SystemVariable(String name, String value) {
        this.variableName = name;
        this.variableValue = value;
    }

    /**
     * @param variableName the variableName to set
     */
    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    /**
     * @return the variableName
     */
    public String getVariableName() {
        return this.variableName;
    }

    /**
     * @param variableValue the variableValue to set
     */
    public void setVariableValue(String variableValue) {
        this.variableValue = variableValue;
    }

    /**
     * @return the variableValue
     */
    public String getVariableValue() {
        return this.variableValue;
    }

    /**
     * @param variableDesc the variableDesc to set
     */
    public void setVariableDesc(String variableDesc) {
        this.variableDesc = variableDesc;
    }

    /**
     * @return the variableDesc
     */
    public String getVariableDesc() {
        return this.variableDesc;
    }
}