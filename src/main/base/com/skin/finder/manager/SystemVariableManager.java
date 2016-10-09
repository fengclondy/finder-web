/*
 * $RCSfile: SystemVariableManager.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-05-06 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skin.cache.Cache;
import com.skin.cache.CacheFactory;
import com.skin.cache.Key;
import com.skin.finder.model.SystemVariable;
import com.skin.finder.service.SystemVariableService;

/**
 * <p>Title: SystemVariableManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class SystemVariableManager {
    private Cache cache = null;
    private SystemVariableService systemVariableService = null;
    protected static final String CACHE_PREFIX = "systemVariable";
    protected static final int EXPIRES = 60000;

    /**
     *
     */
    public SystemVariableManager() {
        this.systemVariableService = new SystemVariableService();
        this.cache = CacheFactory.getCache();
    }

    /**
     * @param systemVariable
     * @return int
     */
    public int create(SystemVariable systemVariable) {
        return this.getSystemVariableService().create(systemVariable);
    }

    /**
     * @param id
     * @return SystemVariable
     */
    public SystemVariable getById(String id) {
        String key = Key.build(CACHE_PREFIX, id);
        SystemVariable systemVariable = null;

        if(this.cache != null) {
            systemVariable = (SystemVariable)(this.cache.getCache(key));
        }

        if(systemVariable == null) {
            systemVariable = this.getSystemVariableService().getById(id);

            if(systemVariable != null && this.cache != null) {
                this.cache.setCache(key, EXPIRES, systemVariable);
            }
        }
        return systemVariable;
    }

    /**
     * @param name
     * @return String
     */
    public String getVariable(String name) {
        SystemVariable systemVariable = this.getById(name);

        if(systemVariable == null) {
            return "";
        }
        else {
            return systemVariable.getVariableValue();
        }
    }

    /**
     * @param name
     * @param value
     * @return String
     */
    public String setVariable(String name, String value) {
        SystemVariable systemVariable = this.getById(name);

        if(systemVariable != null) {
            int rows = this.getSystemVariableService().setVariable(name, value);

            if(rows > 0) {
                this.flush(systemVariable.getVariableName());
                return value;
            }
        }
        else {
            systemVariable = new SystemVariable();
            systemVariable.setVariableName(name);
            systemVariable.setVariableValue(value);
            systemVariable.setVariableDesc("");
            this.getSystemVariableService().create(systemVariable);
        }
        return null;
    }

    /**
     * @param systemVariable
     * @return int
     */
    public int update(SystemVariable systemVariable) {
        int rows = this.getSystemVariableService().update(systemVariable);
        this.flush(systemVariable.getVariableName());
        return rows;
    }

    /**
     * @return int
     */
    public int getVariableCount() {
        return this.getSystemVariableService().getVariableCount();
    }

    /**
     * @param pageNum
     * @param pageSize
     * @return List<SystemVariable>
     */
    public List<SystemVariable> getVariableList(int pageNum, int pageSize) {
        return this.getSystemVariableService().getVariableList(pageNum, pageSize);
    }

    /**
     * @return Map<String, String>
     */
    public Map<String, String> getContext() {
        int pageNum = 1;
        int pageSize = 100;
        Map<String, String> context = new HashMap<String, String>();

        while(true) {
            List<SystemVariable> variables = this.getVariableList(pageNum, pageSize);

            if(variables == null || variables.size() < 1) {
                break;
            }

            for(SystemVariable systemVariable : variables) {
                context.put(systemVariable.getVariableName(), systemVariable.getVariableValue());
            }

            if(variables.size() < pageSize) {
                break;
            }
            pageNum++;
        }
        return context;
    }

    /**
     * @param variableName
     * @return int
     */
    public int delete(String variableName) {
        int rows = this.getSystemVariableService().delete(variableName);
        this.flush(variableName);
        return rows;
    }

    /**
     * @param variableName
     */
    public void flush(String variableName) {
        this.cache.flush(this.getCacheKey(variableName));
    }

    /**
     * @param variableName
     * @return String
     */
    public String getCacheKey(String variableName) {
        return Key.build(CACHE_PREFIX, variableName);
    }

    /**
     * @return the this.cache
     */
    public Cache getCache() {
        return this.cache;
    }

    /**
     * @param cache the this.cache to set
     */
    public void setCache(Cache cache) {
        this.cache = cache;
    }

    /**
     * @return the systemVariableService
     */
    public SystemVariableService getSystemVariableService() {
        return this.systemVariableService;
    }

    /**
     * @param systemVariableService the systemVariableService to set
     */
    public void setSystemVariableService(SystemVariableService systemVariableService) {
        this.systemVariableService = systemVariableService;
    }
}
