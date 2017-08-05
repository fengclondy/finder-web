/*
 * $RCSfile: LessServlet.java,v $
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.FileRange;
import com.skin.finder.Finder;
import com.skin.finder.Less;
import com.skin.finder.servlet.template.LessTemplate;
import com.skin.finder.servlet.template.TailTemplate;
import com.skin.finder.util.IO;
import com.skin.finder.util.Path;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;

/**
 * <p>Title: LessServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class LessServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(LessServlet.class);

    /**
     * default
     */
    public LessServlet() {
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.less")
    public void less(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(LessServlet.prepare(request, response)) {
            LessTemplate.execute(request, response);
        }
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.tail")
    public void tail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(LessServlet.prepare(request, response)) {
            TailTemplate.execute(request, response);
        }
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("less.getRange")
    public void getRange(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long t1 = System.nanoTime();

        File file = Finder.getFile(request);
        this.getRange(request, response, file);

        long t2 = System.nanoTime();
        logger.info("times: {}", (((t2 - t1) / 10000) / 100.0f));
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("less.getTail")
    public void getTail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        File file = Finder.getFile(request);
        getTail(request, response, file);
    }

    /**
     * @param request
     * @param response
     * @return boolean
     * @throws ServletException
     * @throws IOException
     */
    public static boolean prepare(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String work = Finder.getWorkspace(request, workspace);
        String charset = request.getParameter("charset");
        String realPath = Finder.getRealPath(work, path);

        if(realPath == null) {
            FinderServlet.error(request, response, 404, work + "/" + path + " not exists.");
            return false;
        }

        if(charset == null || charset.trim().length() < 1) {
            charset = "utf-8";
        }

        File file = new File(realPath);

        if(!file.exists() || !file.isFile()) {
            FinderServlet.error(request, response, 404, realPath + " not exists.");
            return false;
        }

        String parent = Path.getRelativePath(work, file.getParent());
        String relativePath = Path.getRelativePath(work, realPath);

        request.setAttribute("workspace", workspace);
        request.setAttribute("work", work);
        request.setAttribute("path", relativePath);
        request.setAttribute("parent", parent);
        request.setAttribute("charset", charset);
        request.setAttribute("fileName", file.getName());
        request.setAttribute("absolutePath", file.getCanonicalPath());
        return true;
    }

    /**
     * @param request
     * @param response
     * @param file
     * @throws ServletException
     * @throws IOException
     */
    private void getRange(HttpServletRequest request, HttpServletResponse response, File file) throws ServletException, IOException {
        long position = this.getLong(request, "position", 0L);
        int type = this.getInteger(request, "type", 0);
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

            if(type == 1) {
                FileRange range = Less.next(raf, position, rows, charset);
                Less.callback(request, response, charset, 200, "success", range);
                return;
            }
            else {
                FileRange range = Less.prev(raf, position, rows, charset);
                Less.callback(request, response, charset, 200, "success", range);
                return;
            }
        }
        catch(IOException e) {
            logger.error(e.getMessage(), e);
            Less.callback(request, response, charset, 500, "error", (FileRange)null);
            return;
        }
        finally {
            IO.close(raf);
        }
    }

    /**
     * @param request
     * @param response
     * @param file
     * @throws ServletException
     * @throws IOException
     */
    private void getTail(HttpServletRequest request, HttpServletResponse response, File file) throws ServletException, IOException {
        int rows = this.getInteger(request, "rows", 10);
        long position = this.getLong(request, "position", 0);
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
            FileRange range = Less.tail(raf, position, rows, charset);
            Less.callback(request, response, charset, 200, "success", range);
        }
        catch(IOException e) {
            logger.error(e.getMessage(), e);
            Less.callback(request, response, charset, 500, "error", (FileRange)null);
            return;
        }
        finally {
            IO.close(raf);
        }
    }
}
