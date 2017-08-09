/*
 * $RCSfile: HttpResponse.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * <p>Title: HttpResponse</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class HttpResponse {
    private int statusCode;
    private String reasonPhrase;
    private HttpHeader httpHeader;
    private String characterEncoding;
    private HttpURLConnection connection;
    private InputStream inputStream;

    /**
     */
    public HttpResponse() {
        this(-1, null);
    }

    /**
     * @param statusCode
     * @param reasonPhrase
     */
    public HttpResponse(int statusCode, String reasonPhrase) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

    /**
     * @return the statusCode
     */
    public int getStatusCode() {
        return this.statusCode;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return the reasonPhrase
     */
    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    /**
     * @param reasonPhrase the reasonPhrase to set
     */
    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    /**
     * @param encoding
     */
    public void setCharacterEncoding(String encoding) {
        this.characterEncoding = encoding;
    }

    /**
     * @return String
     */
    public String getCharacterEncoding() {
        if(this.characterEncoding == null) {
            this.characterEncoding = "UTF-8";
        }

        return this.characterEncoding;
    }

    /**
     * @param httpHeader
     */
    public void setHttpHeader(HttpHeader httpHeader) {
        this.httpHeader = httpHeader;
    }

    /**
     * @return HttpHeader
     */
    public HttpHeader getHttpHeader() {
        return this.httpHeader;
    }

    /**
     * @param name
     * @param value
     */
    public void addHeader(String name, String value) {
        if(value != null) {
            this.httpHeader.addHeader(name, value);
        }
        else {
            this.httpHeader.remove(name);
        }
    }

    /**
     * @param name
     * @param value
     */
    public void setHeader(String name, String value) {
        if(value != null) {
            this.httpHeader.setHeader(name, value);
        }
        else {
            this.httpHeader.remove(name);
        }
    }

    /**
     * @param name
     */
    public void removeHeader(String name) {
        this.httpHeader.remove(name);
    }

    /**
     * @param name
     * @return - String
     */
    public String getHeader(String name) {
        return this.httpHeader.getHeader(name);
    }

    /**
     * @param name
     * @return - String
     */
    public String[] getHeaderValues(String name) {
        return this.httpHeader.getHeaderValues(name);
    }

    /**
     *
     * @return - String
     */
    public String getContentType() {
        return this.getHeader("Content-Type");
    }

    /**
     *
     * @return - int
     */
    public int getContentLength() {
        String value = this.getHeader("Content-Length");

        int contentLength = -1;

        if(value == null) {
            return contentLength;
        }

        try {
            contentLength = Integer.parseInt(value);
        }
        catch(NumberFormatException e) {
        }

        return contentLength;
    }

    /**
     * @return the connection
     */
    public HttpURLConnection getConnection() {
        return this.connection;
    }

    /**
     * @param connection the connection to set
     */
    public void setConnection(HttpURLConnection connection) {
        this.connection = connection;
    }

    /**
     * @return the inputStream
     */
    public InputStream getInputStream() {
        return this.inputStream;
    }

    /**
     * @param inputStream the inputStream to set
     */
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     *
     */
    public void close() {
        if(this.connection != null) {
            this.connection.disconnect();
        }

        if(this.inputStream != null) {
            try {
                this.inputStream.close();
            }
            catch(IOException e) {
            }
        }
    }
}
