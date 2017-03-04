/*
 * $RCSfile: VariableManager.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-05-06 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.manager;

import java.util.List;

import com.skin.finder.model.Variable;
import com.skin.finder.service.VariableService;

/**
 * <p>Title: VariableManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class VariableManager {
    private VariableService variableService = null;

    /**
     *
     */
    public VariableManager() {
        this.variableService = new VariableService("null");
    }

    /**
     * @param variable
     * @return int
     */
    public int create(Variable variable) {
        return this.getVariableService().create(variable);
    }

    /**
     * @param id
     * @return Variable
     */
    public Variable getById(String id) {
        return this.getVariableService().getById(id);
    }

    /**
     * @param name
     * @return String
     */
    public String getVariable(String name) {
        Variable variable = this.getById(name);

        if(variable != null) {
            return variable.getValue();
        }
        else {
            return "";
        }
    }

    /**
     * @param name
     * @param value
     * @return int
     */
    public int setVariable(String name, String value) {
        return this.getVariableService().setVariable(name, value);
    }

    /**
     * @param variable
     * @return int
     */
    public int update(Variable variable) {
        return this.getVariableService().update(variable);
    }

    /**
     * @return int
     */
    public int getCount() {
        return this.getVariableService().getCount();
    }

    /**
     * @return List<Variable>
     */
    public List<Variable> getList() {
        return this.getVariableService().getList();
    }

    /**
     * @param pageNum
     * @param pageSize
     * @return List<Variable>
     */
    public List<Variable> getList(int pageNum, int pageSize) {
        return this.getVariableService().getList(pageNum, pageSize);
    }

    /**
     * @param variableName
     * @return int
     */
    public int delete(String variableName) {
        return this.getVariableService().delete(variableName);
    }

    /**
     * @return the variableService
     */
    public VariableService getVariableService() {
        return this.variableService;
    }

    /**
     * @param variableService the variableService to set
     */
    public void setVariableService(VariableService variableService) {
        this.variableService = variableService;
    }
}
