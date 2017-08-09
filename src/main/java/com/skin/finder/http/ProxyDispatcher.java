/*
 * $RCSfile: ProxyDispatcher.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.util.IO;
import com.skin.finder.util.StringUtil;

/**
 * <p>Title: ProxyDispatcher</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ProxyDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(ProxyDispatcher.class);

    /**
     * @param request
     * @param response
     * @param address
     * @throws IOException
     */
    public static void dispatch(HttpServletRequest request, HttpServletResponse response, String address) throws IOException {
        HttpURLConnection connection = null;
        OutputStream outputStream = response.getOutputStream();
        String method = request.getMethod();
        String queryString = request.getQueryString();
        String cottentType = request.getContentType();
        String requestURL = address;

        if(StringUtil.notBlank(queryString)) {
            requestURL = requestURL + "?" + queryString;
        }

        logger.debug("address: {}", address);
        logger.debug("queryString: {}", queryString);
        logger.debug("requestURL: {}", requestURL);

        try {
            URL url = new URL(requestURL);
            connection = (HttpURLConnection)(url.openConnection());
            connection.setConnectTimeout(1 * 60 * 1000);
            connection.setReadTimeout(1 * 60 * 1000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod(method);
            response.setHeader("Host-Name", url.getHost() + ":" + url.getPort());

            logger.debug("=================================================");
            logger.debug("request.method: {}", method);
            setRequestHeaders(request, connection);
            connection.addRequestProperty("Host", url.getHost());
            connection.setRequestProperty("Connection", "Close");

            if(!"get".equalsIgnoreCase(method)) {
                /**
                 * 如果之前调用过request.getParameter方法, 可能不会有数据传输
                 * 这导致集成模式下如果使用了其他框架将无法使用集群功能
                 * 这种情况可以考虑使用nginx直接跳转, 而不是使用finder跳转
                 * finder的跳转中间经过了jsp/servlet容器, 容器可能会解析post请求中的body
                 */
                logger.debug("copy request.body");

                if(cottentType != null && cottentType.indexOf("application/x-www-form-urlencoded") > -1) {
                    /**
                     * TODO: 重新生成request.body
                     */
                    IO.copy(request.getInputStream(), connection.getOutputStream(), 8192);
                }
                else {
                    /**
                     * TODO: 根据contentLength设定bufferSize
                     */
                    IO.copy(request.getInputStream(), connection.getOutputStream(), 8192);
                }
            }

            int status = connection.getResponseCode();
            response.setStatus(status);

            logger.debug("=================================================");
            logger.debug("response.status: {}", status);
            setResponseHeaders(connection, response);
            IO.copy(connection.getInputStream(), outputStream, 8192);
        }
        finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * @param request
     * @param connection
     */
    private static void setRequestHeaders(HttpServletRequest request, HttpURLConnection connection) {
        java.util.Enumeration<?> enums = request.getHeaderNames();

        while(enums.hasMoreElements()) {
            String name = enums.nextElement().toString();

            if(name.equalsIgnoreCase("Host")) {
                continue;
            }
            setRequestHeaders(connection, name, request.getHeaders(name));
        }
    }

    /**
     * @param connection
     * @param name
     * @param values
     */
    private static void setRequestHeaders(HttpURLConnection connection, String name, java.util.Enumeration<?> values) {
        if(values != null) {
            while(values.hasMoreElements()) {
                String value = values.nextElement().toString();
                connection.addRequestProperty(name, value);
                logger.debug("request - {}: {}", name, value);
            }
        }
    }

    /**
     * @param connection
     * @param response
     */
    private static void setResponseHeaders(HttpURLConnection connection, HttpServletResponse response) {
        /**
         * 如果是Transfer-Encoding: chunked, 可能存在问题
         * 暂时先不管
         */
        Map<String, List<String>> map = connection.getHeaderFields();

        for(Map.Entry<String, List<String>> entry : map.entrySet()) {
            String name = entry.getKey();

            /**
             * HTTP/1.1 200
             */
            if(name == null) {
                continue;
            }

            if(name.equalsIgnoreCase("Transfer-Encoding")) {
                continue;
            }

            List<String> values = entry.getValue();

            if(values != null && values.size() > 0) {
                for(String value : values) {
                    logger.debug("response - {}: {}", name, value);
                    response.addHeader(name, value);
                }
            }
        }
    }

    /**
     * @param request
     * @return String
     */
    protected String getPostBody(HttpServletRequest request) {
        return null;
    }
}
