/*
 * $RCSfile: FinderServlet.java,v $$
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.FileRange;
import com.skin.finder.FileType;
import com.skin.finder.FinderManager;
import com.skin.finder.config.Workspace;
import com.skin.finder.util.FinderUtil;
import com.skin.j2ee.upload.FileItem;
import com.skin.j2ee.upload.MultipartHttpRequest;
import com.skin.j2ee.util.JsonUtil;
import com.skin.j2ee.util.Response;
import com.skin.j2ee.util.UpdateChecker;
import com.skin.util.HtmlUtil;
import com.skin.util.IO;
import com.skin.util.IP;
import com.skin.util.Path;

/**
 * <p>Title: FinderServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @version 1.0
 */
public class FinderServlet extends FileServlet {
    private String prefix;
    private static final Map<String, String> map = new HashMap<String, String>();
    private static final Logger logger = LoggerFactory.getLogger(FinderServlet.class);
    private static final long serialVersionUID = 1L;

    static{
        map.put("exe",    "exe");
        map.put("bin",    "bin");
        map.put("class",  "class");
        map.put("swf",    "swf");
        map.put("ico",    "ico");
        map.put("jpg",    "jpg");
        map.put("jpeg",   "jpeg");
        map.put("gif",    "gif");
        map.put("bmp",    "bmp");
        map.put("png",    "png");
        map.put("pdf",    "pdf");
        map.put("doc",    "doc");
        map.put("zip",    "zip");
        map.put("rar",    "rar");
        map.put("jar",    "jar");
        map.put("ear",    "ear");
        map.put("war",    "war");
    }

    /**
     * default
     */
    public FinderServlet() {
    }

    /**
     * @param servletContext
     * @param prefix
     */
    public FinderServlet(ServletContext servletContext, String prefix) {
        this.servletContext = servletContext;
        this.prefix = prefix;
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.error(request, response, 404, "Not Found !");
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void hello(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.forward(request, response, this.prefix + "/finder/hello.jsp");
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void config(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.forward(request, response, this.prefix + "/finder/config.jsp");
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void blank(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.forward(request, response, this.prefix + "/finder/blank.jsp");
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void help(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.forward(request, response, this.prefix + "/finder/help/index.jsp");
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = UpdateChecker.getUrl();
        boolean hasNewVersion = UpdateChecker.has();

        request.setAttribute("appDownloadUrl", url);
        request.setAttribute("hasNewVersion", hasNewVersion);
        this.forward(request, response, this.prefix + "/finder/index.jsp");
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void tree(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String workspace = request.getParameter("workspace");
        List<String> workspaceList = Workspace.getInstance().getWorkspaces();

        request.setAttribute("workspace", workspace);
        request.setAttribute("workspaceList", workspaceList);
        this.forward(request, response, this.prefix + "/finder/tree.jsp");
    }

    /**
     * @param request
     * @param response
     * @param listUrl
     * @param xmlUrl
     * @throws ServletException
     * @throws IOException
     */
    public void getWorkspaceXml(HttpServletRequest request, HttpServletResponse response, String listUrl, String xmlUrl) throws ServletException, IOException {
        Workspace workspace = Workspace.getInstance();
        List<String> list = workspace.getWorkspaces();
        String xml = FinderManager.getWorkspaceXml(list, listUrl, xmlUrl);
        Response.setCache(response, 0);
        Response.write(request, response, "text/xml; charset=UTF-8", xml);
    }

    /**
     * @param request
     * @param response
     * @param listUrl
     * @param xmlUrl
     * @throws ServletException
     * @throws IOException
     */
    public void getFolderXml(HttpServletRequest request, HttpServletResponse response, String listUrl, String xmlUrl) throws ServletException, IOException {
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String home = FinderUtil.getWorkspace(request, workspace);
        FinderManager finderManager = new FinderManager(home);
        String xml = finderManager.getFolderXml(workspace, path, listUrl, xmlUrl);

        Response.setCache(response, 0);
        Response.write(request, response, "text/xml; charset=UTF-8", xml);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getFileList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String home = FinderUtil.getWorkspace(request, workspace);
        FinderManager finderManager = new FinderManager(home);
        String realPath = finderManager.getRealPath(path);

        if(realPath == null) {
            throw new ServletException("Can't access !");
        }

        File file = new File(realPath);

        if(file.isDirectory()) {
            request.setAttribute("workspace", workspace);
            Map<String, Object> context = finderManager.list(path);

            if(context == null) {
                JsonUtil.error(request, response, path + " not exists !");
                return;
            }
            JsonUtil.success(request, response, context);
            return;
        }
        else {
            JsonUtil.error(request, response, path + " is not directory !");
            return;
        }
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void display(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String home = FinderUtil.getWorkspace(request, workspace);
        FinderManager finderManager = new FinderManager(home);
        String realPath = finderManager.getRealPath(path);

        if(realPath == null) {
            throw new ServletException("Can't access !");
        }

        File file = new File(realPath);

        if(!file.exists()) {
            Response.write(request, response, "<h1>" + path + " not exists !</h1>");
            return;
        }

        if(file.isDirectory()) {
            Map<String, Object> context = finderManager.list(path);

            if(context == null) {
                Response.write(request, response, "<h1>" + path + " not exists !</h1>");
                return;
            }

            for(Map.Entry<String, Object> entry : context.entrySet()) {
                request.setAttribute(entry.getKey(), entry.getValue());
            }

            request.setAttribute("localIp", IP.LOCAL);
            request.setAttribute("workspace", workspace);
            this.forward(request, response, this.prefix + "/finder/finder.jsp");
            return;
        }

        String type = request.getParameter("type");
        String encoding = request.getParameter("encoding");
        String theme = request.getParameter("theme");
        String parent = finderManager.getRelativePath(file.getParent());
        String relativePath = Path.getRelativePath(home, realPath);

        if(type == null || type.length() < 1) {
            type = FileType.getExtension(path).toLowerCase();
        }
        else {
            type = type.toLowerCase();
        }

        if(map.get(type) != null) {
            this.execute(request, response, false);
            return;
        }

        long start = 0L;
        long end = 0L;
        long length = file.length();
        long maxLength = 128L * 1024L;
        FileRange range = null;
        String charset = encoding;

        if(charset == null || charset.trim().length() < 1) {
            charset = "utf-8";
        }

        /**
         * 返回不超过128k的数据
         */
        if(length > maxLength) {
            long offset = length - maxLength;
            range = this.getRange(file, offset, charset);
        }
        else if(length > 0L) {
            range = this.getRange(file, 0L, charset);
        }

        String content = "";

        if(range != null) {
            start = range.getStart();
            end = range.getEnd();
            content = HtmlUtil.encode(range.getContent());
        }

        request.setAttribute("workspace", workspace);
        request.setAttribute("work", finderManager.getWork());
        request.setAttribute("path", relativePath);
        request.setAttribute("parent", parent);
        request.setAttribute("content", content);
        request.setAttribute("encoding", encoding);
        request.setAttribute("type", type);
        request.setAttribute("theme", theme);
        request.setAttribute("start", start);
        request.setAttribute("end", end);
        request.setAttribute("length", length);
        this.forward(request, response, this.prefix + "/finder/display.jsp");
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void play(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String home = FinderUtil.getWorkspace(request, workspace);
        FinderManager finderManager = new FinderManager(home);
        String realPath = finderManager.getRealPath(path);

        if(realPath == null) {
            throw new ServletException("Can't access !");
        }

        File file = new File(realPath);

        if(!file.exists()) {
            Response.write(request, response, "<h1>" + path + " not exists !</h1>");
            return;
        }

        if(file.isDirectory()) {
            this.display(request, response);
            return;
        }

        request.setAttribute("workspace", workspace);
        request.setAttribute("work", finderManager.getWork());
        request.setAttribute("path", realPath);
        this.forward(request, response, this.prefix + "/finder/play.jsp");
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void download(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.execute(request, response, true);
    }

    /**
     * @param request
     * @param response
     * @param download
     * @throws ServletException
     * @throws IOException
     */
    public void execute(HttpServletRequest request, HttpServletResponse response, boolean download) throws ServletException, IOException {
        String path = request.getParameter("path");
        String workspace = request.getParameter("workspace");
        String home = FinderUtil.getWorkspace(request, workspace);
        FinderManager finderManager = new FinderManager(home);
        String realPath = finderManager.getRealPath(path);

        if(realPath == null) {
            response.setStatus(404);
            return;
        }

        File file = new File(realPath);

        if(file.isDirectory()) {
            this.display(request, response);
            return;
        }

        if(file.exists() == false) {
            response.setStatus(404);
            return;
        }
        this.service(request, response, file, download);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void suggest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getParameter("path");
        String workspace = request.getParameter("workspace");
        String home = FinderUtil.getWorkspace(request, workspace);
        FinderManager finderManager = new FinderManager(home);
        Object json = finderManager.suggest(workspace, path);
        JsonUtil.success(request, response, json);
        return;
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void rename(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getParameter("path");
        String newName = request.getParameter("newName");
        String workspace = request.getParameter("workspace");
        String home = FinderUtil.getWorkspace(request, workspace);
        FinderManager finderManager = new FinderManager(home);
        int count = finderManager.rename(path, newName);
        JsonUtil.success(request, response, (count > 0));
        return;
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void mkdir(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String path = request.getParameter("path");
        String workspace = request.getParameter("workspace");
        String home = FinderUtil.getWorkspace(request, workspace);
        FinderManager finderManager = new FinderManager(home);
        boolean success = finderManager.mkdir(path, name);
        JsonUtil.success(request, response, success);
        return;
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getParameter("path");
        String workspace = request.getParameter("workspace");
        String home = FinderUtil.getWorkspace(request, workspace);

        int maxFileSize = 20 * 1024 * 1024;
        int maxBodySize = 24 * 1024 * 1024;
        String repository = System.getProperty("java.io.tmpdir");
        FileItem uploadFile = null;
        MultipartHttpRequest multipartRequest = null;

        try {
            multipartRequest = MultipartHttpRequest.parse(request, maxFileSize, maxBodySize, repository);
            uploadFile = multipartRequest.getFileItem("uploadFile");

            if(uploadFile == null || !uploadFile.isFileField()) {
                JsonUtil.error(request, response, 501, "缺少文件！");
                return;
            }

            FinderManager finderManager = new FinderManager(home);
            String fileName = uploadFile.getFileName();
            String realPath = finderManager.getRealPath(path);
            logger.info("fileName: {}", fileName);

            if(fileName.endsWith(".link.tail")) {
                JsonUtil.error(request, response, 501, "没有权限！");
                return;
            }

            if(realPath == null) {
                JsonUtil.error(request, response, 501, "没有权限！");
                return;
            }

            File dir = new File(realPath);

            if(!dir.exists() || !dir.isDirectory()) {
                JsonUtil.error(request, response, 502, "上传失败，目录不存在！");
                return;
            }

            boolean exists = true;
            File target = new File(dir, fileName);
            long offset = multipartRequest.getLong("offset", 0L);

            if(!target.exists()) {
                try {
                    exists = target.createNewFile();
                }
                catch(IOException e) {
                    exists = false;
                    logger.error(e.getMessage(), e);
                }
            }
    
            if(!exists) {
                JsonUtil.error(request, response, 502, "创建文件失败！");
                return;
            }

            logger.info("upload: {}, offset: {}", target.getAbsolutePath(), offset);
            finderManager.write(uploadFile.getFile(), target, offset);
            JsonUtil.success(request, response, true);
            return;
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
            JsonUtil.error(request, response, 500, "系统错误，请稍后再试！");
            return;
        }
        finally {
            if(uploadFile != null) {
                uploadFile.delete();
            }
            if(multipartRequest != null) {
                multipartRequest.destroy();
            }
            logger.info("thread: {} complete !", Thread.currentThread().getName());
        }
    }

    /**
     * @param request
     * @param etag
     * @param httpDate
     * @return boolean
     */
    public int getHttpStatus(HttpServletRequest request, String etag, String httpDate) {
        String ifMatch = request.getHeader("If-Match");
        String ifNoneMatch = request.getHeader("If-None-Match");
        String ifModifiedSince = request.getHeader("If-Modified-Since");
        String ifUnmodifiedSince = request.getHeader("If-Unmodified-Since");
        String unlessModifiedSince = request.getHeader("Unless-Modified-Since");

        /**
         * match
         */
        if(ifMatch != null) {
            if(ifMatch.equals(etag)) {
                return 200;
            }
            else {
                return 412;
            }
        }

        if(ifNoneMatch != null) {
            if(!ifNoneMatch.equals(etag)) {
                return 200;
            }
            else {
                return 304;
            }
        }

        if(ifModifiedSince != null) {
            if(ifModifiedSince.equals(httpDate)) {
                /**
                 * 如果没有修改
                 */
                return 304;
            }
            else {
                /**
                 * 如果有修改
                 */
                return 200;
            }
        }

        if(ifUnmodifiedSince != null) {
            if(ifUnmodifiedSince.equals(httpDate)) {
                /**
                 * 如果没有修改
                 */
                return 200;
            }
            else {
                /**
                 * 如果有修改
                 * 412: Precondition failed
                 */
                return 412;
            }
        }

        if(unlessModifiedSince != null && !unlessModifiedSince.equals(httpDate)) {
            return 200;
        }
        return 200;
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void cut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.move(request, response, true);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void copy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.move(request, response, false);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int count = 0;
        String[] path = request.getParameterValues("path");
        String workspace = request.getParameter("workspace");
        String home = FinderUtil.getWorkspace(request, workspace);
        FinderManager finderManager = new FinderManager(home);

        for(int i = 0; i < path.length; i++) {
            count += finderManager.delete(path[i]);
        }
        JsonUtil.success(request, response, count);
        return;
    }

    /**
     * @param request
     * @param response
     * @param delete
     * @throws ServletException
     * @throws IOException
     */
    public void move(HttpServletRequest request, HttpServletResponse response, boolean delete) throws ServletException, IOException {
        String sourceWorkspace = request.getParameter("sourceWorkspace");
        String sourcePath = request.getParameter("sourcePath");
        String targetWorkspace = request.getParameter("workspace");
        String targetPath = request.getParameter("path");
        String[] fileList = request.getParameterValues("file");
        String sourceHome = FinderUtil.getWorkspace(request, sourceWorkspace);
        String targetHome = FinderUtil.getWorkspace(request, targetWorkspace);

        FinderManager sourceManager = new FinderManager(sourceHome);
        FinderManager targetManager = new FinderManager(targetHome);

        for(String file : fileList) {
            String sourceFile = sourceManager.getRealPath(sourcePath + "/" + file);
            String targetFile = targetManager.getRealPath(targetPath + "/" + file);

            File f1 = new File(sourceFile);
            File f2 = new File(targetFile);

            if(f1.equals(f2)) {
                if(delete) {
                    continue;
                }
                else {
                    f2 = FinderUtil.getFile(f2.getParentFile(), f2.getName());
                }
            }

            IO.copy(f1, f2, true);

            if(delete) {
                IO.delete(f1);
            }
        }
        JsonUtil.success(request, response, true);
        return;
    }

    /**
     * @param file
     * @param offset
     * @param charset
     * @return String
     */
    public FileRange getRange(File file, long offset, String charset) {
        RandomAccessFile raf = null;

        try {
            raf = new RandomAccessFile(file, "r");

            byte LF = 0x0A;
            int readBytes = 0;
            int bufferSize = 4096;
            byte[] buffer = new byte[bufferSize];

            long start = offset;
            long length = raf.length();
            raf.seek(start);

            if(offset > 0) {
                boolean flag = false;

                while((readBytes = raf.read(buffer, 0, bufferSize)) > 0) {
                    for(int i = 0; i < readBytes; i++) {
                        if(buffer[i] == LF) {
                            start = start + i + 1;
                            flag = true;
                            break;
                        }
                    }

                    if(flag) {
                        break;
                    }
                    else {
                        start += readBytes;
                    }
                }

                if(flag) {
                    raf.seek(start);
                }
                else {
                    FileRange range = new FileRange();
                    range.setStart(length - 1);
                    range.setEnd(length - 1);
                    range.setLength(length);
                    range.setRows(-1);
                    range.setCharset(charset);
                    return range;
                }
            }

            readBytes = Math.max((int)(length - start), 0);
            byte[] bytes = new byte[readBytes];

            if(readBytes > 0) {
                readBytes = raf.read(bytes, 0, readBytes);
            }

            FileRange range = new FileRange();
            range.setStart(start);
            range.setEnd(start + readBytes - 1);
            range.setCount(readBytes);
            range.setLength(length);
            range.setRows(-1);
            range.setBuffer(bytes);
            range.setCharset(charset);
            return range;
        }
        catch(IOException e) {
            logger.error(e.getMessage(), e);
        }
        finally {
            IO.close(raf);
        }
        return null;
    }
}
