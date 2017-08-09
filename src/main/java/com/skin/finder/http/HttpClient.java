/*
 * $RCSfile: HttpClient.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import com.skin.finder.http.io.HttpInputStream;

/**
 * <p>Title: HttpClient</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class HttpClient {
    private List<HeaderEntry> httpHeaders;

    /**
     * @param address
     * @param encoding
     * @return String
     * @throws IOException
     */
    public String get(String address, String encoding) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        this.download(address, null, outputStream);
        return new String(outputStream.toByteArray(), encoding);
    }

    /**
     * @param address
     * @param referer
     * @param outputStream
     * @return int
     * @throws IOException
     */
    public int download(String address, String referer, OutputStream outputStream) throws IOException {
        InputStream inputStream = null;
        HttpURLConnection connection = null;

        try {
            HttpResponse response = this.download(address, referer);
            int statusCode = response.getStatusCode();
            connection = response.getConnection();
            inputStream = response.getInputStream();

            if(statusCode == 200) {
                this.copy(inputStream, outputStream);
                return response.getStatusCode();
            }

            return statusCode;
        }
        finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                }
                catch(IOException e) {
                }
            }

            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * @param address
     * @param referer
     * @return HttpResponse
     * @throws IOException
     */
    public HttpResponse download(String address, String referer) throws IOException {
        URL url = null;
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        int status = -1;

        try {
            url = new URL(address);
            connection = (HttpURLConnection)(url.openConnection());
            connection.setConnectTimeout(1 * 60 * 1000);
            connection.setReadTimeout(1 * 60 * 1000);
            connection.setRequestMethod("GET");

            if(this.httpHeaders != null && this.httpHeaders.size() > 0) {
                for(HeaderEntry headerEntry : this.httpHeaders) {
                    List<String> values = headerEntry.getValues();

                    for(String value : values) {
                        connection.addRequestProperty(headerEntry.getName(), value);
                    }
                }
            }

            if(referer != null && referer.length() > 0) {
                connection.addRequestProperty("Referer", referer);
            }

            connection.addRequestProperty("Host", url.getHost());

            if(this.container("X-Forwarded-For")) {
                connection.addRequestProperty("X-Forwarded-For", this.getXForward(3));
            }

            connection.setDoInput(true);
            connection.setUseCaches(false);

            status = connection.getResponseCode();
            HttpResponse resposne = new HttpResponse();
            HttpHeader httpHeader = this.getHttpResponseHeaders(connection);
            resposne.setConnection(connection);
            resposne.setStatusCode(status);
            resposne.setReasonPhrase(connection.getResponseMessage());
            resposne.setHttpHeader(httpHeader);

            if(status == 200) {
                long contentLength = connection.getContentLength();

                if(contentLength > -1) {
                    inputStream = new HttpInputStream(connection.getInputStream(), contentLength);
                }
                else {
                    inputStream = connection.getInputStream();
                }

                String contentEncoding = connection.getHeaderField("Content-Encoding");
                boolean gzip = (contentEncoding != null && contentEncoding.equals("gzip"));

                if(gzip) {
                    resposne.setInputStream(new GZIPInputStream(inputStream));
                }
                else {
                    resposne.setInputStream(inputStream);
                }
            }

            return resposne;
        }
        finally {
        }
    }

    /**
     * @param count
     * @return String
     */
    protected String getXForward(int count) {
        StringBuilder buffer = new StringBuilder();

        for(int i = 0; i < count; i++) {
            if(i > 0) {
                buffer.append(",");
            }

            for(int j = 0; j < 4; j++) {
                if(j > 0) {
                    buffer.append(".");
                }

                buffer.append(10 + (int)(Math.random() * 245));
            }
        }

        return buffer.toString();
    }

    /**
     * @param connection
     * @return Map<String, HeaderEntry>
     */
    protected HttpHeader getHttpResponseHeaders(HttpURLConnection connection) {
        Map<String, List<String>> headerFields = connection.getHeaderFields();
        HttpHeader httpHeader = new HttpHeader();

        for(Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
            String name = entry.getKey();

            if(name != null) {
                List<String> values = entry.getValue();

                for(String value : values) {
                    httpHeader.addHeader(name, value);
                }
            }
        }

        return httpHeader;
    }

    /**
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    protected void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        try {
            int length = 0;
            int bufferSize = 8192;
            byte[] buffer = new byte[bufferSize];

            while((length = inputStream.read(buffer, 0, bufferSize)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
        }
        finally {
        }
    }

    /**
     * @param header
     * @return boolean
     */
    private boolean container(String header) {
        if(this.httpHeaders != null && this.httpHeaders.size() > 0) {
            for(HeaderEntry headerEntry : this.httpHeaders) {
                if(headerEntry.getName().equalsIgnoreCase(header)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * @return the httpHeaders
     */
    public List<HeaderEntry> getHttpHeaders() {
        return this.httpHeaders;
    }

    /**
     * @param httpHeaders the httpHeaders to set
     */
    public void setHttpHeaders(List<HeaderEntry> httpHeaders) {
        this.httpHeaders = httpHeaders;
    }
}
