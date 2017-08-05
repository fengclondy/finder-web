/*
 * $RCSfile: ResourceManager.java,v $
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.ContentEntry;

/**
 * <p>Title: ResourceManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ResourceManager {
    protected String file;
    protected String home;
    protected long lastModified;
    private String compress = "^js$|^css$|^xml$|^txt$|^text$|^htm$|^html$";
    private Map<String, ContentEntry> cache;
    private static final Logger logger = LoggerFactory.getLogger(ResourceManager.class);

    /**
     * @param file
     * @param home
     */
    public ResourceManager(String file, String home) {
        this.file = file;
        this.home = home;
        this.reload();
    }

    /**
     * @param path
     * @return ByteEntry
     */
    public ContentEntry get(String path) {
        if(path == null) {
            return null;
        }
        return this.cache.get(path);
    }

    /**
     * reload
     */
    public void reload() {
        File file = new File(this.file);
        Map<String, ContentEntry> temp = this.cache;
        this.cache = this.load(file, this.home);
        this.lastModified = file.lastModified();

        if(temp != null) {
            temp.clear();
        }
    }

    /**
     * @param file
     * @param home
     * @return Map<String, ContentEntry>
     */
    protected Map<String, ContentEntry> load(File file, String home) {
        InputStream inputStream = null;
        JarInputStream jarInputStream = null;
        JarFile jarFile = null;
        Map<String, ContentEntry> map = new HashMap<String, ContentEntry>();

        if(!file.exists() || !file.isFile()) {
            logger.error("{} not exists. load resource failed.");
            return map;
        }

        try {
            JarEntry entry = null;
            jarFile = new JarFile(file);
            inputStream = new FileInputStream(file);
            jarInputStream = new JarInputStream(inputStream);
            Pattern pattern = Pattern.compile(this.compress);

            while((entry = jarInputStream.getNextJarEntry()) != null) {
                if(entry.isDirectory()) {
                    continue;
                }

                String name = entry.getName();

                if(!name.startsWith("/")) {
                    name = "/" + name;
                }

                if(name.endsWith(".class") || !name.startsWith(home)) {
                    continue;
                }

                if(home.length() > 1) {
                    name = name.substring(home.length());
                }

                String extension = this.getExtension(name);
                byte[] bytes = this.read(jarFile.getInputStream(entry));
                long lastModified = entry.getTime();
                logger.info("cache: {}", name);

                if(pattern.matcher(extension).matches()) {
                    map.put(name, new ContentEntry(name, ContentEntry.ZIP, this.gzip(bytes), lastModified));
                }
                else {
                    map.put(name, new ContentEntry(name, ContentEntry.BIN, bytes, lastModified));
                }
            }
        }
        catch(IOException e) {
        }
        finally {
            close(jarInputStream);
            close(inputStream);

            if(jarFile != null) {
                try {
                    jarFile.close();
                }
                catch(IOException e) {
                }
            }
        }
        return map;
    }

    /**
     * @param path
     * @return String
     */
    protected String getExtension(String path) {
        int i = path.lastIndexOf(".");

        if(i > -1) {
            return path.substring(i + 1).toLowerCase();
        }
        return "";
    }

    /**
     * @param inputStream
     * @return byte[]
     * @throws IOException
     */
    protected byte[] read(InputStream inputStream) throws IOException {
        int length = 0;
        byte[] buffer = new byte[2048];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        while((length = inputStream.read(buffer, 0, 2048)) > 0) {
            bos.write(buffer, 0, length);
        }
        return bos.toByteArray();
    }

    /**
     * @param bytes
     * @return byte[]
     * @throws IOException
     */
    protected byte[] gzip(byte[] bytes) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
        gzipOutputStream.write(bytes, 0, bytes.length);
        gzipOutputStream.finish();
        gzipOutputStream.flush();
        return outputStream.toByteArray();
    }

    /**
     * @param bytes
     * @return byte[]
     * @throws IOException
     */
    public static byte[] ungzip(byte[] bytes) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream, bytes.length);
        copy(gzipInputStream, outputStream);
        return outputStream.toByteArray();
    }

    /**
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        int length = 0;
        int bufferSize = 2048;
        byte[] buffer = new byte[bufferSize];

        while((length = inputStream.read(buffer, 0, bufferSize)) > -1) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
    }

    /**
     * @param type
     * @return File
     */
    public static File getJarFile(Class<?> type) {
        URL url = type.getResource(type.getSimpleName() + ".class");
        String path = url.toExternalForm();
        int k = path.indexOf("!/");

        if(k > -1) {
            path = path.substring(0, k);
        }

        if(!path.startsWith("jar:")) {
            return null;
        }

        path = path.substring(4);

        if(path.startsWith("file:")) {
            path = path.substring(5);
        }

        try {
            path = URLDecoder.decode(path, "utf-8");
        }
        catch (UnsupportedEncodingException e) {
        }
        return new File(path);
    }

    /**
     * @param resource
     */
    public static void close(Closeable resource) {
        if(resource != null) {
            try {
                resource.close();
            }
            catch(IOException e) {
            }
        }
    }
}
