/*
 * $RCSfile: SiteConfig.java,v $$
 * $Revision: 1.1 $
 * $Date: 2009-07-14 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.config;

import com.skin.config.Config;

/**
 * <p>Title: SiteConfig</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class SiteConfig extends Config {
    private static final long serialVersionUID = 1L;
    private static final SiteConfig instance = new SiteConfig();

    private SiteConfig() {
    }

    /**
     * @return SiteConfig
     */
    public static SiteConfig getInstance() {
        return SiteConfig.instance;
    }

    /**
     * @param key
     * @param value
     */
    @Override
    public String put(String key, String value) {
        return SystemConfig.setValue(key, value);
    }

    /**
     * @param key
     * @return String
     */
    @Override
    public String get(Object key) {
        if(key != null && key instanceof String) {
            return SystemConfig.getValue((String)(key), null);
        }
        return null;
    }
}
