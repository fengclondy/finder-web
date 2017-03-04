package test.com.skin.finder;

import java.io.File;

import com.skin.finder.FinderManager;
import com.skin.finder.config.Workspace;

/**
 * <p>Title: FinderManagerTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class FinderManagerTest {
    /**
     * @param args
     */
    public static void main(String[] args) {
        String workspace = "finder.workspace.vhost";
        String path = "\\template";
        String home = getWorkspace(workspace);
        FinderManager finderManager = new FinderManager(home);
        String realPath = finderManager.getRealPath(path);
        File file = new File(realPath);
        String parent = finderManager.getRelativePath(file.getParent());
        System.out.println(parent);
        test1();
    }

    /**
     *
     */
    public static void test1() {
        String parent = "D:\\workspace2\\finder\\webapp";
        String work   = "D:\\workspace2\\finder\\webapp\\";

        if(parent.length() >= work.length()) {
            parent = parent.substring(work.length()).replace('\\', '/');

            if(parent.length() < 1) {
                parent = "/";
            }
        }
        else {
            parent = null;
        }

        System.out.println("parent: " + parent);
    }

    /**
     * @param name
     * @return String
     */
    public static String getWorkspace(String name) {
        if(name == null) {
            throw new NullPointerException("workspace must be not null !");
        }

        Workspace workspace = Workspace.getInstance();
        String work = workspace.getValue(name.trim());

        if(work == null) {
            throw new NullPointerException("workspace must be not null !");
        }

        if(work.startsWith("file:")) {
            return new File(work.substring(5)).getAbsolutePath();
        }

        if(work.startsWith("contextPath:")) {
            return "D:\\workspace2\\finder\\webapp";
        }
        throw new NullPointerException("work directory error: " + work);
    }
}
