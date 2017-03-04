/*
 * $RCSfile: LessTest.java,v $$
 * $Revision: 1.1 $
 * $Date: 2016-10-6 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package test.com.skin.finder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.skin.finder.FileRange;
import com.skin.finder.servlet.GrepServlet;
import com.skin.finder.servlet.LessServlet;
import com.skin.util.IO;

/**
 * <p>Title: LessTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class LessTest {
    /**
     * @param args
     */
    public static void main(String[] args) {
        // prevTest();
        // printLF2(new File("D:\\opt\\resin\\log\\root2.log"));
        grepTest();
    }

    /**
     * 
     */
    public static void prevTest() {
        LessServlet less = new LessServlet();
        File file = new File("D:\\opt\\resin\\log\\test.log");
        RandomAccessFile raf = null;

        try {
            int rows = 75;
            long position = 292215L;
            raf = new RandomAccessFile(file, "r");
            FileRange range = less.prev(raf, position, rows, "utf-8");
            System.out.println(less.getReturnValue(200, "success", range));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        finally {
            IO.close(raf);
        }
    }

    /**
     * 
     */
    public static void nextTest() {
        LessServlet less = new LessServlet();
        File file = new File("D:\\opt\\resin\\log\\root2.log");
        RandomAccessFile raf = null;

        try {
            int count = 0;
            int rows = 20;
            long position = 600;
            raf = new RandomAccessFile(file, "r");
            FileRange range = null;

            while(count++ < 20) {
                range = less.next(raf, position, rows, "utf-8");
                System.out.println(less.getReturnValue(200, "success", range));
                // System.out.println(range.getContent());
                position = range.getEnd();
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        finally {
            IO.close(raf);
        }
    }

    /**
     * 
     */
    public static void tailTest() {
        LessServlet less = new LessServlet();
        File file = new File("D:\\opt\\resin\\log\\root2.log");
        RandomAccessFile raf = null;

        try {
            raf = new RandomAccessFile(file, "r");
            FileRange range = less.tail(raf, 0, 2, "utf-8");

            System.out.println(less.getReturnValue(200, "success", range));
            System.out.println(range.getContent());
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        finally {
            IO.close(raf);
        }
    }
    
    /**
     * @param file
     */
    public static void printLF(File file) {
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(file);
            
            int length = 0;
            long position = 0L;
            byte[] buffer = new byte[4096];
            
            while((length = inputStream.read(buffer, 0, 4096)) > 0) {
                for(int i = 0; i < length; i++) {
                    if(buffer[i] == '\n') {
                        System.out.println("\\n: " + (position + i));
                    }
                }
                position += length;
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        finally {
            IO.close(inputStream);
        }
    }
    
    /**
     * @param file
     */
    public static void printLF2(File file) {
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(file);

            int length = (int)(file.length());
            byte[] buffer = new byte[length];
            inputStream.read(buffer, 0, length);
            
            for(int i = 0; i < length; i++) {
                if(buffer[i] == '\n') {
                    System.out.println("\\n: " + i);
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        finally {
            IO.close(inputStream);
        }
    }

    /**
     * 
     */
    public static void grepTest() {
        RandomAccessFile raf = null;
        GrepServlet grep = new GrepServlet();

        try {
            raf = new RandomAccessFile(new File("D:\\opt\\resin\\log\\test.log"), "r");
            FileRange range1 = grep.find(raf, "test", false, 0L, 200, "gbk");
            System.out.println(grep.getReturnValue(200, "ok", range1));
            
            FileRange range2 = grep.find(raf, "test", false, range1.getEnd(), 200, "gbk");
            System.out.println(grep.getReturnValue(200, "ok", range2));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        finally {
            IO.close(raf);
        }
    }

    /**
     * 
     */
    public static void regexpTest() {
        String input = "execute time: 3 - /finder/grep.html";
        Pattern pattern = Pattern.compile("/finder/.*\\.html");
        Matcher matcher = pattern.matcher(input);
        System.out.println(matcher.find());
    }
}
