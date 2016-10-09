package test.com.skin.dao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import test.com.skin.model.TestModel;

import com.skin.j2ee.dao.ExecutableFactory;
import com.skin.j2ee.dao.Update;
import com.skin.j2ee.util.DaoUtil;

/**
 * <p>Title: UpdateTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * @author xuesong.net
 * @version 1.0
 */
public class UpdateTest {
    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("a", null);
            model.put("b", 1);
            
            Update update = new Update("task", model, true);
            System.out.println(update.getSql());
            System.out.println(Arrays.toString(update.getParameters()));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public static void test1() {
        try {
            TestModel model = new TestModel();
            Map<String, Object> map = DaoUtil.getProperties(model);
            StringBuilder sql = ExecutableFactory.getUpdateSql("task", map);
            System.out.println("sql: " + sql);
            map.put("id", 1);
            sql.append(" where id=?");

            Object[] parameters = DaoUtil.getParameters(map.values());
            String s1 = DaoUtil.setParameter(sql.toString(), parameters);
            System.out.println(s1);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public static void test2() {
        try {
            TestModel model = new TestModel();
            Update update = ExecutableFactory.getUpdate("model", model);
            update.and("id", "=", model.getId());

            String sql = update.getSql();
            Object[] parameters = update.getParameters();
            String s1 = DaoUtil.setParameter(sql.toString(), parameters);
            System.out.println(s1);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public static void test3() {
        TestModel model = new TestModel();

        try {
            Update update = ExecutableFactory.getUpdate("test_model", model);
            update.and("id", "=", model.getId());

            String sql = update.getSql();
            Object[] parameters = update.getParameters();
            String s1 = DaoUtil.setParameter(sql.toString(), parameters);
            System.out.println(s1);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     */
    public static void test4() {
        try {
            Update update = new Update("task");
            update.set("a", 1);
            update.set("b", 2);
            update.set("c", 3);
            update.set("d", 4);

            update.and("a", "=", 1L);
            update.or("a",  "=", 2L);
            update.and("b", "like", "%xyz%");
            update.and("c", "in", Arrays.asList("inc"));
            update.and("d", "in", Arrays.asList(1, 2, 3));
            update.and("e", "in", Arrays.asList("1", "2", "3"));

            System.out.println(update.getSql());
            System.out.println(Arrays.toString(update.getParameters()));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
