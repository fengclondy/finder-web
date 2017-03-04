/*
 * $RCSfile: InsertTest.java,v $$
 * $Revision: 1.1 $
 * $Date: 2010-8-27 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package test.com.skin.dao;

import java.util.Arrays;

import test.com.skin.model.TestModel;

import com.skin.j2ee.dao.ExecutableFactory;
import com.skin.j2ee.dao.Insert;
import com.skin.j2ee.util.DaoUtil;

/**
 * <p>Title: InsertTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class InsertTest {
    /**
     * @param args
     */
    public static void main(String[] args) {
        test1();
    }

    /**
     *
     */
    public static void test1() {
        TestModel model = new TestModel();
        model.setName("test111");

        try {
            Insert insert = ExecutableFactory.getInsert("test_model", model, true);
            String sql = insert.getSql();
            Object[] parameters = insert.getParameters();

            System.out.println(insert.getSql());
            System.out.println(Arrays.toString(parameters));
            System.out.println(DaoUtil.setParameter(sql, parameters));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
