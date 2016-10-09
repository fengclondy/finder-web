/*
 * $RCSfile: DataImport.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-4-24 $
 *
 * Copyright (C) 2008 Skine, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skine, Inc.
 * Use is subject to license terms.
 */
package test.com.skin.finder;

import java.util.Map;

/**
 * <p>Title: TestCommand</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class TestCommand {
    /**
     * @param context
     */
    public void execute(Map<String, String> context) {
        System.out.println("classLoader: " + this.getClass().getClassLoader().getClass().getName());
        for(Map.Entry<String, String> entry : context.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
