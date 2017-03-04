package test.com.skin.dao;

import com.skin.datasource.ConnectionManager;
import com.skin.datasource.DataSourceInitlet;
import com.skin.finder.config.SiteConfig;

/**
 * @version 1.0
 */
public class SiteConfigTest {
    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            DataSourceInitlet.init("jdbc.test.properties");
            SiteConfig siteConfig = SiteConfig.getInstance();
            System.out.println("resource: " + siteConfig.getString("resource"));
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            ConnectionManager.disconnect();
        }
    }
}
