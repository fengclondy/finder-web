/*
 * $RCSfile: Ajax.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title: Ajax</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Ajax {
    /**
     * @param request
     * @param response
     * @param value
     * @throws IOException
     */
    public static void success(HttpServletRequest request, HttpServletResponse response, String value) throws IOException {
        StringBuilder buffer = new StringBuilder();
        buffer.append("{\"status\":");
        buffer.append(200);
        buffer.append(",\"message\":\"success\"");

        if(value != null) {
            buffer.append(",\"value\":");
            buffer.append(value);
        }

        buffer.append("}");
        Ajax.callback(request, response, request.getParameter("callback"), buffer.toString());
    }

    /**
     * @param request
     * @param response
     * @param message
     * @throws IOException
     */
    public static void error(HttpServletRequest request, HttpServletResponse response, String message) throws IOException {
        message(request, response, 500, message);
    }

    /**
     * @param request
     * @param response
     * @param status
     * @param message
     * @throws IOException
     */
    public static void error(HttpServletRequest request, HttpServletResponse response, int status, String message) throws IOException {
        message(request, response, status, message);
    }

    /**
     * @param request
     * @param response
     * @param status
     * @param message
     * @throws IOException
     */
    private static void message(HttpServletRequest request, HttpServletResponse response, int status, String message) throws IOException {
        StringBuilder buffer = new StringBuilder();
        buffer.append("{\"status\":");
        buffer.append(status);
        buffer.append(",\"message\":\"");
        buffer.append(StringUtil.escape(message));
        buffer.append("\"}");
        Ajax.callback(request, response, request.getParameter("callback"), buffer.toString());
    }

    /**
     * @param request
     * @param response
     * @param value
     * @throws IOException
     */
    protected static void callback(HttpServletRequest request, HttpServletResponse response, String value) throws IOException {
        callback(request, response, request.getParameter("callback"), value);
    }

    /**
     * @param request
     * @param response
     * @param callback
     * @param value
     * @throws IOException
     */
    protected static void callback(HttpServletRequest request, HttpServletResponse response, String callback, String value) throws IOException {
        if(callback != null) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(callback);
            buffer.append("(");

            if(value != null) {
                buffer.append(value);
            }
            else {
                buffer.append("null");
            }

            buffer.append(");");
            write(request, response, "text/javascript; charset=utf-8", buffer.toString());
        }
        else {
            if(value != null) {
                write(request, response, "text/javascript; charset=utf-8", value);
            }
            else {
                write(request, response, "text/javascript; charset=utf-8", "void(0)");
            }
        }
    }

    /**
     * @param request
     * @param response
     * @param contentType
     * @param content
     * @throws IOException
     */
    private static void write(HttpServletRequest request, HttpServletResponse response, String contentType, String content) throws IOException {
        byte[] buffer = content.getBytes("utf-8");
        response.setContentType(contentType);
        response.setContentLength(buffer.length);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(buffer);
        outputStream.flush();
    }

    /**
     * @param list
     * @return String
     */
    public static String stringify(List<String> list) {
        if(list == null) {
            return "null";
        }

        if(list.isEmpty()) {
            return "[]";
        }

        StringBuilder buffer = new StringBuilder();
        buffer.append("[");

        for(String content : list) {
            buffer.append("\"");
            buffer.append(StringUtil.escape(content));
            buffer.append("\",");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append("]");
        return buffer.toString();
    }
}
