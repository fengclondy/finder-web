package com.skin.finder.test;

import java.io.File;
import java.io.RandomAccessFile;

import com.skin.finder.FileRange;
import com.skin.finder.Less;

/**
 * <p>Title: TailTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author Admin
 * @version 1.0
 */
public class TailTest {
    /**
     * @param args
     */
    public static void main(String[] args) {
        RandomAccessFile raf = null;

        try {
            raf = new RandomAccessFile(new File("D:\\opt\\ll.log"), "r");
            FileRange range = Less.tail(raf, 57L, 200, "utf-8");
            System.out.println(range.getContent("utf-8"));
            System.out.println(Less.getReturnValue(200, "success", range));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
