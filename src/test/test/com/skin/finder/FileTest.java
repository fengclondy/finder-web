/*
 * $RCSfile: FileTest.java,v $$
 * $Revision: 1.1 $
 * $Date: 2014-10-8 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package test.com.skin.finder;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.FinderManager;

/**
 * <p>Title: FileTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class FileTest {
    private static final Logger logger = LoggerFactory.getLogger(FileTest.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
        getParentTest();
    }

    /**
     *
     */
    public static void getPathTest() {
        String work = "E:\\WorkSpace\\finder\\webapp\\";
        String temp = "/template";

        try {
            File file = new File(work, temp);
            temp = file.getCanonicalPath();

            System.out.println("work: " + work);
            System.out.println("temp: " + temp);

            if(temp.startsWith(work) == false) {
                System.out.println("null");
            }
            else {
                System.out.println(temp);
            }
        }
        catch(IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }

    /**
     *
     */
    public static void getParentTest() {
        String file = "E:\\WorkSpace\\finder\\webapp\\template";
        String work   = "E:\\WorkSpace\\finder\\webapp\\";
        FinderManager finderManager = new FinderManager(work);
        System.out.println(finderManager.getRelativePath(new File(file).getParent()));
    }
}
