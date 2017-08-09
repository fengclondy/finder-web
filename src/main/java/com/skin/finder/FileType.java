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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        map.put("3ds",  "3ds");
        map.put("7z",   "rar");
        map.put("aac",  "aac");
        map.put("ace",  "rar");
        map.put("ai",   "ai");
        map.put("ain",  "ain");
        map.put("air",  "air");
        map.put("amr",  "amr");
        map.put("apk",  "apk");
        map.put("app",  "app");
        map.put("arj",  "arj");
        map.put("as",   "as");
        map.put("asax", "asax");
        map.put("ascx", "ascx");
        map.put("asf",  "asf");
        map.put("ashx", "ashx");
        map.put("asm",  "asm");
        map.put("asmx", "asmx");
        map.put("asp",  "asp");
        map.put("aspx", "aspx");
        map.put("av",   "av");
        map.put("avi",  "avi");
        map.put("bat",  "cmd");
        map.put("bin",  "bin");
        map.put("bmp",  "bmp");
        map.put("bz2",  "rar");
        map.put("c",    "c");
        map.put("cab",  "cab");
        map.put("cad",  "cad");
        map.put("cat",  "cat");
        map.put("cdr",  "cdr");
        map.put("cer",  "cer");
        map.put("chm",  "chm");
        map.put("class", "class");
        map.put("cmd",  "cmd");
        map.put("com",  "com");
        map.put("cpp",  "cpp");
        map.put("cs",   "cs");
        map.put("css",  "css");
        map.put("csv",  "csv");
        map.put("cur",  "cur");
        map.put("dat",  "dat");
        map.put("db",   "db");
        map.put("dll",  "dll");
        map.put("dmg",  "dmg");
        map.put("dmv",  "dmv");
        map.put("doc",  "doc");
        map.put("docm", "docm");
        map.put("docx", "docx");
        map.put("dot",  "dot");
        map.put("dotm", "dotm");
        map.put("dotx", "dotx");
        map.put("dps",  "dps");
        map.put("dpt",  "dpt");
        map.put("dtd",  "dtd");
        map.put("dwg",  "dwg");
        map.put("dxf",  "dxf");
        map.put("ear",  "rar");
        map.put("emf",  "wmf");
        map.put("eps",  "eps");
        map.put("epub", "epub");
        map.put("et",   "et");
        map.put("ett",  "ett");
        map.put("exe",  "exe");
        map.put("f",    "f");
        map.put("file", "file");
        map.put("fla",  "fla");
        map.put("flv",  "flv");
        map.put("fon",  "fon");
        map.put("font", "font");
        map.put("ftp",  "ftp");
        map.put("gif",  "gif");
        map.put("gz",   "rar");
        map.put("h",    "h");
        map.put("hlp",  "hlp");
        map.put("htm",  "htm");
        map.put("html", "htm");
        map.put("icl",  "icl");
        map.put("ico",  "ico");
        map.put("img",  "png");
        map.put("indd", "indd");
        map.put("inf",  "inf");
        map.put("ini",  "ini");
        map.put("ipa",  "ipa");
        map.put("iso",  "iso");
        map.put("jar",  "jar");
        map.put("java", "java");
        map.put("jpeg", "jpg");
        map.put("jpg",  "jpg");
        map.put("js",   "js");
        map.put("json", "json");
        map.put("key",  "key");
        map.put("ldf",  "ldf");
        map.put("m3u",  "m3u");
        map.put("max",  "max");
        map.put("md",   "md");
        map.put("mdb",  "mdb");
        map.put("mde",  "mde");
        map.put("mdf",  "mdf");
        map.put("mht",  "mht");
        map.put("mid",  "mp4");
        map.put("midi", "midi");
        map.put("mkv",  "mp4");
        map.put("mov",  "mpeg");
        map.put("mp3",  "mp3");
        map.put("mp4",  "mp4");
        map.put("mpeg", "mpeg");
        map.put("mpg",  "mpeg");
        map.put("mpp",  "mpp");
        map.put("mpt",  "mpt");
        map.put("msg",  "msg");
        map.put("msi",  "msi");
        map.put("music", "music");
        map.put("nrg",  "nrg");
        map.put("o",    "o");
        map.put("ocx",  "ocx");
        map.put("odp",  "odp");
        map.put("ods",  "ods");
        map.put("odt",  "odt");
        map.put("oexe", "oexe");
        map.put("ogg",  "ogg");
        map.put("ogm",  "ogm");
        map.put("pages", "pages");
        map.put("pdb",  "pdb");
        map.put("pdf",  "pdf");
        map.put("php",  "php");
        map.put("pkg",  "pkg");
        map.put("pl",   "pl");
        map.put("png",  "png");
        map.put("pot",  "pot");
        map.put("pps",  "pps");
        map.put("ppsx", "ppsx");
        map.put("ppt",  "ppt");
        map.put("pptx", "pptx");
        map.put("ps1",  "ps1");
        map.put("psd",  "psd");
        map.put("pst",  "pst");
        map.put("pub",  "pub");
        map.put("py",   "py");
        map.put("qt",   "qt");
        map.put("ra",   "ra");
        map.put("ram",  "ram");
        map.put("rar",  "rar");
        map.put("rb",   "rb");
        map.put("reg",  "reg");
        map.put("resx", "resx");
        map.put("rm",   "rm");
        map.put("rmvb", "mp4");
        map.put("rtf",  "rtf");
        map.put("s",    "s");
        map.put("sitx", "sitx");
        map.put("sln",  "sln");
        map.put("sql",  "sql");
        map.put("suo",  "suo");
        map.put("svg",  "svg");
        map.put("swf",  "swf");
        map.put("tar",  "rar");
        map.put("test", "test");
        map.put("tif",  "tif");
        map.put("tiff", "tiff");
        map.put("txt",  "txt");
        map.put("url",  "url");
        map.put("vb",   "vb");
        map.put("vbs",  "vbs");
        map.put("vcf",  "vcf");
        map.put("vdw",  "vdw");
        map.put("vdx",  "vdx");
        map.put("vsd",  "vsd");
        map.put("vsdx", "vsdx");
        map.put("vss",  "vss");
        map.put("vst",  "vst");
        map.put("vsx",  "vsx");
        map.put("vtx",  "vtx");
        map.put("war",  "war");
        map.put("wav",  "wav");
        map.put("wave", "wave");
        map.put("wm",   "mpeg");
        map.put("wma",  "wma");
        map.put("wmd",  "mpeg");
        map.put("wmf",  "wmf");
        map.put("wmv",  "wmv");
        map.put("wps",  "wps");
        map.put("wpt",  "wpt");
        map.put("xaml", "xaml");
        map.put("xap",  "xap");
        map.put("xls",  "xls");
        map.put("xlsb", "xlsb");
        map.put("xlsm", "xlsm");
        map.put("xlsx", "xlsx");
        map.put("xlt",  "xlt");
        map.put("xltx", "xltx");
        map.put("xml",  "xml");
        map.put("xps",  "xps");
        map.put("xsd",  "xsd");
        map.put("xsl",  "xsl");
        map.put("y",    "y");
        map.put("zip",  "rar");
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

    /**
     * @return List<String>
     */
    public static List<String> list() {
        Set<String> set = map.keySet();
        List<String> list = new ArrayList<String>();

        list.addAll(set);
        return list;
    }
}
