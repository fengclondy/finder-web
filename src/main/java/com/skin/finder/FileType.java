/*
 * $RCSfile: FileType.java,v $
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Title: FileType</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class FileType {
    private static final Map<String, String> map = new HashMap<String, String>();

    static{
        map.put("aac",   "aac");
        map.put("ace",   "ace");
        map.put("ai",    "ai");
        map.put("ain",   "ain");
        map.put("amr",   "amr");
        map.put("apk",   "zip");
        map.put("app",   "app");
        map.put("arj",   "arj");
        map.put("asf",   "asf");
        map.put("asp",   "asp");
        map.put("aspx",  "aspx");
        map.put("av",    "av");
        map.put("avi",   "avi");
        map.put("bin",   "bin");
        map.put("bmp",   "bmp");
        map.put("cab",   "cab");
        map.put("cad",   "cad");
        map.put("cat",   "cat");
        map.put("cdr",   "cdr");
        map.put("chm",   "chm");
        map.put("class", "class");
        map.put("com",   "com");
        map.put("css",   "css");
        map.put("cur",   "cur");
        map.put("dat",   "dat");
        map.put("db",    "db");
        map.put("dll",   "dll");
        map.put("dmv",   "dmv");
        map.put("doc",   "doc");
        map.put("docx",  "doc");
        map.put("dot",   "dot");
        map.put("dps",   "dps");
        map.put("dpt",   "dpt");
        map.put("dwg",   "dwg");
        map.put("dxf",   "dxf");
        map.put("emf",   "emf");
        map.put("eps",   "eps");
        map.put("et",    "et");
        map.put("ett",   "ett");
        map.put("exe",   "exe");
        map.put("fla",   "fla");
        map.put("ftp",   "ftp");
        map.put("gif",   "gif");
        map.put("hlp",   "hlp");
        map.put("htm",   "htm");
        map.put("html",  "htm");
        map.put("icl",   "icl");
        map.put("ico",   "ico");
        map.put("img",   "img");
        map.put("inf",   "inf");
        map.put("ini",   "ini");
        map.put("iso",   "iso");
        map.put("java",  "java");
        map.put("jpeg",  "jpeg");
        map.put("jpg",   "jpg");
        map.put("js",    "js");
        map.put("m3u",   "m3u");
        map.put("max",   "max");
        map.put("mdb",   "mdb");
        map.put("mde",   "mde");
        map.put("mht",   "mht");
        map.put("mid",   "mid");
        map.put("midi",  "midi");
        map.put("mov",   "mov");
        map.put("mp3",   "mp3");
        map.put("mp4",   "mp4");
        map.put("mpeg",  "mpeg");
        map.put("mpg",   "mpg");
        map.put("msi",   "msi");
        map.put("nrg",   "nrg");
        map.put("ocx",   "ocx");
        map.put("ogg",   "ogg");
        map.put("ogm",   "ogm");
        map.put("pdf",   "pdf");
        map.put("png",   "png");
        map.put("pot",   "pot");
        map.put("ppt",   "ppt");
        map.put("psd",   "psd");
        map.put("pub",   "pub");
        map.put("qt",    "qt");
        map.put("ra",    "ra");
        map.put("ram",   "ram");
        map.put("rar",   "rar");
        map.put("jar",   "jar");
        map.put("ear",   "ear");
        map.put("war",   "war");
        map.put("rar",   "rar");
        map.put("rm",    "rm");
        map.put("rmvb",  "rmvb");
        map.put("rtf",   "rtf");
        map.put("swf",   "swf");
        map.put("sql",   "sql");
        map.put("tar",   "tar");
        map.put("tif",   "tif");
        map.put("tiff",  "tiff");
        map.put("txt",   "txt");
        map.put("url",   "url");
        map.put("vbs",   "vbs");
        map.put("vsd",   "vsd");
        map.put("vss",   "vss");
        map.put("vst",   "vst");
        map.put("wav",   "wav");
        map.put("wave",  "wave");
        map.put("wm",    "wm");
        map.put("wma",   "wma");
        map.put("wmd",   "wmd");
        map.put("wmf",   "wmf");
        map.put("wmv",   "wmv");
        map.put("wps",   "wps");
        map.put("wpt",   "wpt");
        map.put("xls",   "xls");
        map.put("xlt",   "xlt");
        map.put("xml",   "xml");
        map.put("zip",   "zip");
    }

    /**
     * @param path
     * @return String
     */
    public static String getIcon(String path) {
        String extension = FileType.getExtension(path);
        String icon = map.get(extension.toLowerCase());
        return (icon != null ? icon : "unknown");
    }

    /**
     * @param path
     * @return String
     */
    public static String getType(String path) {
        return FileType.getExtension(path).toLowerCase();
    }

    /**
     * @param path
     * @return String
     */
    public static String getName(String path) {
        if(path != null && path.length() > 0) {
            char c;
            int i = path.length() - 1;
            StringBuilder buffer = new StringBuilder();

            for(; i > -1; i--) {
                c = path.charAt(i);

                if(c == '/' || c == '\\' || c == ':') {
                    break;
                }
            }
            buffer.append(path.substring(i + 1));
            return buffer.toString();
        }
        return "";
    }

    /**
     * @param path
     * @return String
     */
    public static String getExtension(String path) {
        if(path != null && path.length() > 0) {
            char c = '0';
            int i = path.length() - 1;

            for(; i > -1; i--) {
                c = path.charAt(i);

                if(c == '.' ) {
                    break;
                }
                else if(c == '/' || c == '\\' || c == ':') {
                    break;
                }
            }

            if(c == '.') {
                return path.substring(i + 1);
            }
            return "";
        }
        return "";
    }
}
