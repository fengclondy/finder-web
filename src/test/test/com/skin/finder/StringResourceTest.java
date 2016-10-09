/*
 * $RCSfile: StringResourceTest.java,v $$
 * $Revision: 1.1  $
 * $Date: 2013-12-14  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package test.com.skin.finder;

import java.util.Map;

import com.skin.config.ConfigFactory;
import com.skin.finder.config.Workspace;

/**
 * <p>Title: StringResourceTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class StringResourceTest {
    /**
     * @param args
     */
    public static void main(String[] args) {
        Workspace workspace = ConfigFactory.getConfig("conf/workspace.xml", Workspace.class);

        for(Map.Entry<String, String> entry : workspace.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
