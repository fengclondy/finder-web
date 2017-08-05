/*
 * $RCSfile: IO.java,v $
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: IO</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class IO {
    /**
     * @param file
     * @param charset
     * @return String
     * @throws IOException
     */
    public static String read(File file, String charset) throws IOException {
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(file);

            int length = (int)(file.length());
            byte[] buffer = new byte[length];
            inputStream.read(buffer, 0, length);

            if(charset != null) {
                return new String(buffer, 0, buffer.length, charset);
            }
            else {
                return new String(buffer, 0, buffer.length);
            }
        }
        finally {
            close(inputStream);
        }
    }

    /**
     * @param file
     * @param charset
     * @return String
     * @throws IOException
     */
    public static List<String> list(File file, String charset) throws IOException {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;

        try {
            inputStream = new FileInputStream(file);

            if(charset == null || charset.length() < 1) {
                inputStreamReader = new InputStreamReader(inputStream);
            }
            else {
                inputStreamReader = new InputStreamReader(inputStream, charset);
            }
            return list(inputStreamReader);
        }
        finally {
            close(inputStreamReader);
            close(inputStream);
        }
    }

    /**
     * @param reader
     * @return List<String>
     * @throws IOException
     */
    public static List<String> list(Reader reader) throws IOException {
        BufferedReader buffer = null;
        List<String> list = new ArrayList<String>();

        if(reader instanceof BufferedReader) {
            buffer = (BufferedReader)reader;
        }
        else {
            buffer = new BufferedReader(reader);
        }

        try {
            String line = null;
            while((line = buffer.readLine()) != null) {
                list.add(line);
            }
            return list;
        }
        finally {
            // never closed
            if(reader == null) {
                buffer.close();
            }
        }
    }

    /**
     * @param file
     * @param buffer
     * @throws IOException
     */
    public static void write(File file, byte[] buffer) throws IOException {
        OutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(buffer);
            outputStream.flush();
        }
        finally {
            close(outputStream);
        }
    }

    /**
     * @param source
     * @param target
     * @param lastModified
     * @throws IOException
     */
    public static void copy(File source, File target, boolean lastModified) throws IOException {
        if(source.isFile()) {
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                int bufferSize = 0;
                long length = source.length();

                if(length < 4096) {
                    bufferSize = 2048;
                }
                else {
                    bufferSize = 8192;
                }

                inputStream = new FileInputStream(source);
                outputStream = new FileOutputStream(target);
                copy(inputStream, outputStream, bufferSize);
            }
            finally {
                close(inputStream);
                close(outputStream);
            }

            if(lastModified) {
                target.setLastModified(source.lastModified());
            }
        }
        else {
            if(!target.exists()) {
                target.mkdirs();
            }

            File[] files = source.listFiles();

            for(File file : files) {
                copy(file, new File(target, file.getName()), lastModified);
            }
        }
    }

    /**
     *
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        copy(inputStream, outputStream, 4096);
    }

    /**
     * @param inputStream
     * @param outputStream
     * @param bufferSize
     * @throws IOException
     */
    public static void copy(InputStream inputStream, OutputStream outputStream, int bufferSize) throws IOException {
        int length = 0;
        byte[] buffer = new byte[bufferSize];

        while((length = inputStream.read(buffer, 0, bufferSize)) > -1) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
    }

    /**
     * @param inputStream
     * @param outputStream
     * @param bufferSize
     * @param size
     * @throws IOException
     */
    public static void copy(InputStream inputStream, OutputStream outputStream, int bufferSize, long size) throws IOException {
        if(size > 0) {
            int readBytes = 0;
            long count = size;
            int length = Math.min(bufferSize, (int)(size));
            byte[] buffer = new byte[length];

            while(count > 0) {
                if(count > length) {
                    readBytes = inputStream.read(buffer, 0, length);
                }
                else {
                    readBytes = inputStream.read(buffer, 0, (int)count);
                }

                if(readBytes > 0) {
                    outputStream.write(buffer, 0, readBytes);
                    count -= readBytes;
                }
                else {
                    break;
                }
            }
            outputStream.flush();
        }
    }

    /**
     * @param reader
     * @param writer
     * @throws IOException
     */
    public static void copy(Reader reader, Writer writer) throws IOException {
        copy(reader, writer, 2048);
    }

    /**
     * @param reader
     * @param writer
     * @param bufferSize
     * @throws IOException
     */
    public static void copy(Reader reader, Writer writer, int bufferSize) throws IOException {
        int length = 0;
        char[] buffer = new char[bufferSize];

        while((length = reader.read(buffer, 0, bufferSize)) > -1) {
            writer.write(buffer, 0, length);
        }
        writer.flush();
    }

    /**
     * @param file
     */
    public static void delete(File file) {
        if(file.isDirectory()) {
            File[] files = file.listFiles();

            for(File f : files) {
                if(f.isDirectory()) {
                    delete(f);
                }
                else {
                    f.delete();
                }
            }
        }
        file.delete();
    }

    /**
     * @param file
     * @throws IOException
     */
    public static void toucn(File file) throws IOException {
        OutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
        }
        finally {
            close(outputStream);
        }
    }

    /**
     * @param path
     * @return String
     */
    public static String getFileName(String path) {
        if(path != null && path.length() > 0) {
            int i = path.length() - 1;

            for(; i > -1; i--) {
                char c = path.charAt(i);

                if(c == '/' || c == '\\' || c == ':') {
                    break;
                }
            }
            return path.substring(i + 1);
        }
        return "";
    }

    /**
     * @param path
     * @return String
     */
    public static String getExtension(String path) {
        int i = path.lastIndexOf(".");

        if(i > -1) {
            return path.substring(i);
        }
        return "";
    }

    /**
     *
     * @param file
     * @param charset
     * @return String
     * @throws IOException
     */
    public static String toString(File file, String charset) throws IOException {
        InputStream in = null;

        try {
            in = new FileInputStream(file);
            return toString(in, charset);
        }
        finally {
            close(in);
        }
    }

    /**
     *
     * @param inputStream
     * @return String
     * @throws IOException
     */
    public static String toString(InputStream inputStream) throws IOException {
        return toString(new java.io.InputStreamReader(inputStream));
    }

    /**
     *
     * @param inputStream
     * @param charset
     * @return String
     * @throws IOException
     */
    public static String toString(InputStream inputStream, String charset) throws IOException {
        Reader reader = null;

        try {
            reader = new java.io.InputStreamReader(inputStream, charset);
            return toString(reader);
        }
        finally {
            IO.close(reader);
        }
    }

    /**
     *
     * @param reader
     * @return String
     * @throws IOException
     */
    public static String toString(Reader reader) throws IOException {
        return toString(reader, 2048);
    }

    /**
     *
     * @param reader
     * @param bufferSize
     * @return String
     * @throws IOException
     */
    public static String toString(Reader reader, int bufferSize) throws IOException {
        try {
            int length = 0;
            char[] buffer = new char[Math.max(bufferSize, 2048)];
            StringWriter out = new StringWriter();

            while((length = reader.read(buffer)) > -1) {
                out.write(buffer, 0, length);
            }
            return out.toString();
        }
        finally {
        }
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
