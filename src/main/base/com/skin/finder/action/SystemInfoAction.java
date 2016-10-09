/*
 * $RCSfile: SystemInfoAction.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-03-08 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.action;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.datasource.ConnectionManager;
import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.util.DatabaseInfo;
import com.skin.j2ee.util.ServletInfo;
import com.skin.j2ee.util.SystemInfo;
import com.skin.license.Version;
import com.skin.license.VersionFactory;

/**
 * <p>Title: SystemInfoAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class SystemInfoAction extends BaseAction {
    private static final Logger logger = LoggerFactory.getLogger(SystemInfoAction.class);

    /**
     * @throws ServletException
     * @throws IOException
     */
    @com.skin.j2ee.annotation.UrlPattern("/system/left.html")
    public void left() throws ServletException, IOException {
        this.forward("/template/system/left.jsp");
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @com.skin.j2ee.annotation.UrlPattern("/system/index.html")
    public void index() throws ServletException, IOException {
        Connection connection = null;
        DatabaseInfo databaseInfo = null;

        try {
            connection = ConnectionManager.getConnection();
            databaseInfo = DatabaseInfo.getInstance(connection);
        }
        catch(Throwable t) {
            if(logger.isWarnEnabled()) {
                logger.warn(t.getMessage(), t);
            }
        }
        finally {
            ConnectionManager.close(connection);
        }

        ServletInfo servletInfo = new ServletInfo(this.getServletContext());
        SystemInfo systemInfo = new SystemInfo();
        Version version = VersionFactory.getInstance();

        this.setAttribute("servletInfo", servletInfo);
        this.setAttribute("databaseInfo", databaseInfo);
        this.setAttribute("systemInfo", systemInfo);
        this.setAttribute("version", version);
        this.forward("/template/system/systemInfo.jsp");
    }
}
