/*
 * $RCSfile: FileScanJob.java,v $$
 * $Revision: 1.1 $
 * $Date: 2014-10-21 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.schedule;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.j2ee.context.WebApplicationContext;
import com.skin.util.FileUtil;
import com.skin.util.IO;

/**
 * <p>Title: FileScanJob</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class FileScanJob {
    private static final Logger logger = LoggerFactory.getLogger(FileScanJob.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
        FileScanJob fsj = new FileScanJob();
        fsj.execute("");
    }

    /**
     * @param bind
     */
    public void execute(String bind) {
        File home = null;
        File logFile = null;
        ServletContext servletContext = WebApplicationContext.getServletContext();

        if(servletContext != null) {
            home = new File(servletContext.getRealPath("/"));
            logFile = new File(home, "WEB-INF/scan.md5");
        }
        else {
            home = new File(".");
            logFile = new File("bin/scan.md5");
        }

        logger.info("scan: " + home.getAbsolutePath());
        logFile.getParentFile().mkdirs();
        FileWriter fileWriter = null;
        PrintWriter writer = null;

        try {
            fileWriter = new FileWriter(logFile);
            writer = new PrintWriter(fileWriter);
            this.process(home, writer);
        }
        catch(Exception e) {
        }
        finally {
            IO.close(writer);
            IO.close(fileWriter);
        }
    }

    /**
     * @param file
     * @param writer
     */
    public void process(File file, PrintWriter writer) {
        if(file.isDirectory()) {
            File[] files = file.listFiles();

            for(File f : files) {
                if(f.isDirectory()) {
                    process(f, writer);
                }
                else {
                    try {
                        writer.println(FileUtil.md5(f) + ": " + f.getAbsolutePath());
                    }
                    catch(Exception e) {
                    }
                }
            }
        }
    }
}
