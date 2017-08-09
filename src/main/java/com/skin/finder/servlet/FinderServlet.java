/*
 * $RCSfile: FinderServlet.java,v $
 * $Revision: 1.1 $
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

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.FileItem;
import com.skin.finder.FileItemList;
import com.skin.finder.FileRange;
import com.skin.finder.FileType;
import com.skin.finder.Finder;
import com.skin.finder.FinderManager;
import com.skin.finder.WorkspaceManager;
import com.skin.finder.cluster.Agent;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.servlet.template.DisplayTemplate;
import com.skin.finder.servlet.template.FinderTemplate;
import com.skin.finder.servlet.template.PlayTemplate;
import com.skin.finder.upload.MultipartHttpRequest;
import com.skin.finder.upload.Part;
import com.skin.finder.util.Ajax;
import com.skin.finder.util.IO;
import com.skin.finder.util.IP;
import com.skin.finder.util.Path;
import com.skin.finder.util.StringUtil;
import com.skin.finder.web.Response;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.FileServlet;

/**
 * <p>Title: FinderServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class FinderServlet extends FileServlet {
    private static final long serialVersionUID = 1L;
    private static final Map<String, String> map = new HashMap<String, String>();
    private static final Logger logger = LoggerFactory.getLogger(FinderServlet.class);

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
     */
    public FinderServlet(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * @param servletConfig
     */
    @Override
    public void init(ServletConfig servletConfig) {
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ErrorServlet.error(request, response, 404, "Not Found !");
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.getFile")
    public void getFile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String work = Finder.getWorkspace(request, workspace);
        FinderManager finderManager = new FinderManager(work);
        String realPath = finderManager.getRealPath(path);

        if(realPath == null) {
            throw new ServletException("Can't access !");
        }

        File file = new File(realPath);

        if(file.exists()) {
            FileItem fileItem = FinderManager.getFileItem(file);
            Ajax.success(request, response, FileItemList.getJSONString(fileItem));
            return;
        }
        else {
            Ajax.success(request, response, (String)null);
            return;
        }
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.getFileList")
    public void getFileList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String work = Finder.getWorkspace(request, workspace);
        FinderManager finderManager = new FinderManager(work);
        String realPath = finderManager.getRealPath(path);

        if(realPath == null) {
            throw new ServletException("Can't access !");
        }

        File file = new File(realPath);

        if(file.isDirectory()) {
            long t1 = System.currentTimeMillis();
            FileItemList fileItemList = finderManager.list(path);
            Ajax.success(request, response, fileItemList.getJSONString());
            long t2 = System.currentTimeMillis();

            if(logger.isDebugEnabled()) {
                logger.debug("time: {}", (t2 - t1));
            }
            return;
        }
        else {
            Ajax.error(request, response, path + " is not directory !");
            return;
        }
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.display")
    public void display(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String home = Finder.getWorkspace(request, workspace);
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

        String parent = finderManager.getRelativePath(file.getParent());
        String relativePath = Path.getRelativePath(home, realPath);

        if(file.isDirectory()) {
            if(StringUtil.isBlank(relativePath)) {
                relativePath = "/";
            }
            request.setAttribute("localIp", IP.LOCAL);
            request.setAttribute("host", ConfigFactory.getHostName());
            request.setAttribute("workspace", workspace);
            request.setAttribute("work", finderManager.getWork());
            request.setAttribute("path", relativePath);
            request.setAttribute("parent", parent);
            FinderTemplate.execute(request, response);
            return;
        }

        String type = request.getParameter("type");
        String encoding = request.getParameter("encoding");
        String theme = request.getParameter("theme");

        if(type == null || type.length() < 1) {
            type = FileType.getExtension(path).toLowerCase();
        }
        else {
            type = type.toLowerCase();
        }

        if(theme == null || theme.length() < 1) {
            theme = "Default";
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
            content = range.getContent();
        }

        /**
         * 配置项参数
         * 上传文件大小
         */
        request.setAttribute("localIp", IP.LOCAL);
        request.setAttribute("host", ConfigFactory.getHostName());
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
        DisplayTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.play")
    public void play(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String home = Finder.getWorkspace(request, workspace);
        FinderManager finderManager = new FinderManager(home);
        String realPath = finderManager.getRealPath(path);

        if(realPath == null) {
            throw new ServletException("Can't access !");
        }

        File file = new File(realPath);

        if(!file.exists() || file.isDirectory()) {
            Response.write(request, response, "<h1>" + path + " not exists !</h1>");
            return;
        }

        String parent = finderManager.getRelativePath(file.getParent());
        String relativePath = Path.getRelativePath(home, realPath);

        request.setAttribute("localIp", IP.LOCAL);
        request.setAttribute("host", ConfigFactory.getHostName());
        request.setAttribute("workspace", workspace);
        request.setAttribute("work", finderManager.getWork());
        request.setAttribute("path", relativePath);
        request.setAttribute("parent", parent);
        PlayTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.download")
    public void download(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }
        this.execute(request, response, true);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.suggest")
    public void suggest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String path = request.getParameter("path");
        String workspace = request.getParameter("workspace");
        String home = Finder.getWorkspace(request, workspace);
        FinderManager finderManager = new FinderManager(home);
        List<FileItem> fileItemList = finderManager.suggest(workspace, path);
        String json = FileItemList.getJSONString(fileItemList);
        Ajax.success(request, response, json);
        return;
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.rename")
    public void rename(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getParameter("path");
        String newName = request.getParameter("newName");
        String workspace = request.getParameter("workspace");
        String home = Finder.getWorkspace(request, workspace);

        if(WorkspaceManager.getReadonly(workspace)) {
            throw new IOException("The workspace is readonly!");
        }

        FinderManager finderManager = new FinderManager(home);
        int count = finderManager.rename(path, newName);
        Ajax.success(request, response, Boolean.toString(count > 0));
        return;
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.mkdir")
    public void mkdir(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String workspace = request.getParameter("workspace");

        if(WorkspaceManager.getReadonly(workspace)) {
            Ajax.error(request, response, 501, "The workspace is readonly !");
            return;
        }

        String name = request.getParameter("name");
        String path = request.getParameter("path");
        String home = Finder.getWorkspace(request, workspace);
        FinderManager finderManager = new FinderManager(home);
        boolean success = finderManager.mkdir(path, name);
        Ajax.success(request, response, Boolean.toString(success));
        return;
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.upload")
    public void upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String workspace = request.getParameter("workspace");

        if(WorkspaceManager.getReadonly(workspace)) {
            Ajax.error(request, response, 501, "Workspace is readonly.");
            return;
        }

        String path = request.getParameter("path");
        String work = Finder.getWorkspace(request, workspace);

        int maxFileSize = 20 * 1024 * 1024;
        int maxBodySize = 24 * 1024 * 1024;
        String repository = System.getProperty("java.io.tmpdir");
        Part uploadFile = null;
        MultipartHttpRequest multipartRequest = null;

        try {
            multipartRequest = MultipartHttpRequest.parse(request, maxFileSize, maxBodySize, repository);
            uploadFile = multipartRequest.getFileItem("uploadFile");

            if(uploadFile == null || !uploadFile.isFileField()) {
                Ajax.error(request, response, 504, "Bad Request.");
                return;
            }

            FinderManager finderManager = new FinderManager(work);
            String fileName = uploadFile.getFileName();
            String realPath = finderManager.getRealPath(path);
            logger.info("fileName: {}", fileName);

            if(fileName.endsWith(".link.tail")) {
                Ajax.error(request, response, 501, "Access Denied.");
                return;
            }

            if(realPath == null) {
                Ajax.error(request, response, 501, "Access Denied.");
                return;
            }

            File dir = new File(realPath);

            if(!dir.exists() || !dir.isDirectory()) {
                Ajax.error(request, response, 502, "File not exists.");
                return;
            }

            boolean exists = true;
            File target = new File(dir, fileName);
            long offset = multipartRequest.getLong("offset", 0L);
            long lastModified = multipartRequest.getLong("lastModified", 0L);

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
                Ajax.error(request, response, 502, "Create file failed.");
                return;
            }

            finderManager.write(uploadFile.getFile(), target, offset, lastModified);
            Ajax.success(request, response, Boolean.toString(true));
            return;
        }
        catch(Exception e) {
            Ajax.error(request, response, 500, "System Error.");
            return;
        }
        finally {
            if(uploadFile != null) {
                uploadFile.delete();
            }
            if(multipartRequest != null) {
                multipartRequest.destroy();
            }
        }
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.cut")
    public void cut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String workspace = request.getParameter("workspace");

        if(WorkspaceManager.getReadonly(workspace)) {
            Ajax.error(request, response, 501, "Workspace is readonly.");
            return;
        }
        this.move(request, response, true);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.copy")
    public void copy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String workspace = request.getParameter("workspace");

        if(WorkspaceManager.getReadonly(workspace)) {
            Ajax.error(request, response, 501, "Workspace is readonly.");
            return;
        }
        this.move(request, response, false);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.delete")
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String workspace = request.getParameter("workspace");

        if(WorkspaceManager.getReadonly(workspace)) {
            Ajax.error(request, response, 501, "Workspace is readonly.");
            return;
        }

        int count = 0;
        String[] path = request.getParameterValues("path");
        String home = Finder.getWorkspace(request, workspace);
        FinderManager finderManager = new FinderManager(home);

        if(path != null && path.length > 0) {
            for(int i = 0; i < path.length; i++) {
                count += finderManager.delete(path[i]);
            }
        }
        Ajax.success(request, response, Integer.toString(count));
        return;
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
        String home = Finder.getWorkspace(request, workspace);
        String realPath = Finder.getRealPath(home, path);

        if(realPath == null) {
            logger.debug("can't access - {}: {}: {}", workspace, home, path);
            response.setStatus(404);
            return;
        }

        File file = new File(realPath);

        if(file.isDirectory()) {
            this.display(request, response);
            return;
        }

        if(file.exists() == false) {
            logger.debug("file not exists: {}", file.getAbsolutePath());
            response.setStatus(404);
            return;
        }
        this.service(request, response, file, download);
    }

    /**
     * @param request
     * @param response
     * @param delete
     * @throws ServletException
     * @throws IOException
     */
    private void move(HttpServletRequest request, HttpServletResponse response, boolean delete) throws ServletException, IOException {
        String sourceWorkspace = request.getParameter("sourceWorkspace");
        String sourcePath = request.getParameter("sourcePath");
        String targetWorkspace = request.getParameter("workspace");
        String targetPath = request.getParameter("path");
        String[] fileList = request.getParameterValues("file");
        String sourceHome = Finder.getWorkspace(request, sourceWorkspace);
        String targetHome = Finder.getWorkspace(request, targetWorkspace);

        if(fileList == null || fileList.length < 1) {
            Ajax.error(request, response, 404, "Bad Request.");
            return;
        }

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
                    f2 = Finder.getFile(f2.getParentFile(), f2.getName());
                }
            }

            IO.copy(f1, f2, true);

            if(delete) {
                IO.delete(f1);
            }
        }
        Ajax.success(request, response, "true");
        return;
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
        }
        finally {
            IO.close(raf);
        }
        return null;
    }
}
