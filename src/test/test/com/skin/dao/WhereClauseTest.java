package test.com.skin.dao;

import java.util.Arrays;

import com.skin.j2ee.dao.WhereClause;

/**
 * <p>Title: WhereClauseTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * @author xuesong.net
 * @version 1.0
 */
public class WhereClauseTest {
    /**
     * @param args
     */
    public static void main(String[] args) {
        WhereClause whereClause = new WhereClause();
        whereClause.and("a", "=", 1);
        whereClause.and("b", "like", "%xyz%");
        whereClause.and("c", "in", Arrays.asList("inc"));
        whereClause.and("d", "in", Arrays.asList(1, 2, 3));
        whereClause.and("e", "in", Arrays.asList("1", "2", "3"));

        /*
        whereClause.eq("and", "a", 1);
        whereClause.like("and", "b", "%xyz%");
        whereClause.in("and", "c", Arrays.asList("inc"));
        whereClause.in("and", "d", Arrays.asList(1, 2, 3));
        whereClause.in("and", "e", Arrays.asList("1", "2", "3"));
        */
        System.out.println(whereClause.getWhereClause());
        System.out.println(Arrays.toString(whereClause.getParameters()));
    }
}
