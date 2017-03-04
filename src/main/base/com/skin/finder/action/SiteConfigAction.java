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
import java.util.Map;

import javax.servlet.ServletException;

import com.skin.finder.config.KeyConfigFactory;
import com.skin.finder.config.KeyInfo;
import com.skin.finder.manager.SystemVariableManager;
import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.util.JsonUtil;
import com.skin.util.Attributes;

/**
 * <p>Title: SystemVariableAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class SiteConfigAction extends BaseAction {
    /**
     * @throws ServletException
     * @throws IOException
     */
    @com.skin.j2ee.annotation.UrlPattern("/system/site/config.html")
    public void execute() throws ServletException, IOException {
        SystemVariableManager systemVariableManager = new SystemVariableManager();
        Map<String, String> context = systemVariableManager.getContext();
        Attributes attributes = new Attributes(context);

        List<KeyInfo> siteConfigKeyList = KeyConfigFactory.parse("META-INF/conf/site-config.xml", attributes);
        this.setAttribute("siteConfigKeyList", siteConfigKeyList);
        this.forward("/template/system/siteConfig.jsp");
    }

    /**
     * @throws Exception
     */
    @com.skin.j2ee.annotation.UrlPattern("/system/site/config/save.html")
    public void save() throws Exception {
        List<KeyInfo> keyInfoList = KeyConfigFactory.parse("META-INF/conf/site-config.xml");

        if(keyInfoList != null && keyInfoList.size() > 0) {
            SystemVariableManager systemVariableManager = new SystemVariableManager();

            for(KeyInfo keyInfo : keyInfoList) {
                String name = keyInfo.getName();
                String value = this.getTrimString(name);

                if(value.length() < 1) {
                    value = keyInfo.getValue();
                }
                systemVariableManager.setVariable(name, value);
            }
        }
        JsonUtil.success(this.request, this.response, true);
    }
}
