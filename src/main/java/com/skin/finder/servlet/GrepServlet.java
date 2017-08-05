/*
 * $RCSfile: GrepServlet.java,v $
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.servlet;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skin.finder.FileRange;
import com.skin.finder.Finder;
import com.skin.finder.Grep;
import com.skin.finder.Less;
import com.skin.finder.servlet.template.GrepTemplate;
import com.skin.finder.util.IO;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;

/**
 * <p>Title: GrepServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class GrepServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.grep")
    public void grep(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(LessServlet.prepare(request, response)) {
            GrepTemplate.execute(request, response);
        }
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("grep.find")
    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        File file = Finder.getFile(request);
        String keyword = this.getTrimString(request, "keyword");
        boolean regexp = this.getBoolean(request, "regexp", false);
        long position = this.getLong(request, "position", 0L);
        int rows = this.getInteger(request, "rows", 10);
        String charset = this.getTrimString(request, "charset");

        /**
         * 不允许请求太多内容
         */
        if(rows > 2000) {
            rows = 2000;
        }

        /**
         * 默认为utf-8
         */
        if(charset.length() < 1) {
            charset = "utf-8";
        }

        RandomAccessFile raf = null;

        try {
            raf = new RandomAccessFile(file, "r");
            FileRange range = Grep.find(raf, keyword, regexp, position, rows, charset);
            Less.callback(request, response, charset, 200, "success", range);
            return;
        }
        catch(IOException e) {
            Less.callback(request, response, charset, 500, "error", (FileRange)null);
            return;
        }
        finally {
            IO.close(raf);
        }
    }
}
