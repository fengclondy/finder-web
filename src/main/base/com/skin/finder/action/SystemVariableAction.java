/*
 * $RCSfile: SystemVariableAction.java,v $$
 * $Revision: 1.1  $
 * $Date: 2013-12-15  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import com.skin.datasource.ConnectionManager;
import com.skin.finder.manager.SystemVariableManager;
import com.skin.finder.model.SystemVariable;
import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.util.JsonUtil;

/**
 * <p>Title: SystemVariableAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class SystemVariableAction extends BaseAction {
    /**
     * @throws ServletException
     * @throws IOException
     */
    @com.skin.j2ee.annotation.UrlPattern("/system/config.html")
    public void list() throws ServletException, IOException {
        int pageNum = this.getInteger("pageNum", 1);
        int pageSize = this.getInteger("pageSize", 20);
        int variableCount = 0;
        List<SystemVariable> systemVariableList = null;

        if(ConnectionManager.available()) {
            SystemVariableManager systemVariableManager = new SystemVariableManager();
            variableCount = systemVariableManager.getVariableCount();
            systemVariableList = systemVariableManager.getVariableList(pageNum, pageSize);
        }

        this.setAttribute("pageNum", pageNum);
        this.setAttribute("pageSize", pageSize);
        this.setAttribute("variableCount", variableCount);
        this.setAttribute("systemVariableList", systemVariableList);
        this.forward("/template/system/systemConfig.jsp");
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @com.skin.j2ee.annotation.UrlPattern("/system/config/save.html")
    public void save() throws ServletException, IOException {
        if(!ConnectionManager.available()) {
            JsonUtil.error(this.request, this.response, 500, "没有数据库连接！");
            return;
        }
        
        String variableName = this.getString("variableName", "");
        String variableValue = this.getString("variableValue", "");
        String variableDesc = this.getString("variableDesc", "");

        SystemVariableManager systemVariableManager = new SystemVariableManager();
        SystemVariable systemVariable = systemVariableManager.getById(variableName);

        if(systemVariable == null) {
            systemVariable = new SystemVariable();
            systemVariable.setVariableName(variableName);
            systemVariable.setVariableValue(variableValue);
            systemVariable.setVariableDesc(variableDesc);
            systemVariableManager.create(systemVariable);
        }
        else {
            systemVariable.setVariableName(variableName);
            systemVariable.setVariableValue(variableValue);
            systemVariable.setVariableDesc(variableDesc);
            systemVariableManager.update(systemVariable);
        }
        JsonUtil.success(this.request, this.response, true);
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @com.skin.j2ee.annotation.UrlPattern("/system/config/delete.html")
    public void delete() throws ServletException, IOException {
        if(!ConnectionManager.available()) {
            JsonUtil.error(this.request, this.response, 500, "没有数据库连接！");
            return;
        }

        String variableName = this.getString("variableName", "");
        SystemVariableManager systemVariableManager = new SystemVariableManager();
        SystemVariable systemVariable = systemVariableManager.getById(variableName);

        if(systemVariable != null) {
            systemVariableManager.delete(variableName);
        }
        JsonUtil.success(this.request, this.response, true);
    }
}
