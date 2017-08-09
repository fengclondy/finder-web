/*
 * $RCSfile: HttpConfig.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.skin.finder.config.Config;
import com.skin.finder.config.ConfigFactory;

/**
 * <p>Title: HttpConfig</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class HttpConfig {
    private static final Config instance = ConfigFactory.load("http.conf", "utf-8");

    /**
     * @return HttpConfig
     */
    public static Config getInstance() {
        return HttpConfig.instance;
    }

    /**
     * @return List<HeaderEntry>
     */
    public static List<HeaderEntry> getHttpHeaders() {
        Config config = HttpConfig.getInstance();
        Set<String> set = config.getNames();
        List<HeaderEntry> headers = new ArrayList<HeaderEntry>();

        for(String name : set) {
            if(name.startsWith("http.header.")) {
                headers.add(new HeaderEntry(name.substring(12), new String[]{config.getString(name)}));
            }
        }
        return headers;
    }
}
