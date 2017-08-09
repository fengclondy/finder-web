/*
 * $RCSfile: JspKit.java,v $
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.jsp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skin.finder.util.DateUtil;
import com.skin.finder.util.IO;
import com.skin.finder.util.Path;
import com.skin.finder.util.StringUtil;
import com.skin.finder.web.servlet.JspServlet;

/**
 * <p>Title: JspKit</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class JspKit {
    private String packageName;
    private String output;
    private static final String JAVA_TEMPLATE = JspKit.getJavaTemplate();

    /**
     * @param args
     */
    public static void main(String[] args) {
        generate(new File("webapp/template/finder"), "com.skin.finder.servlet.template", "src/main/java");
    }

    /**
     * @param dir
     * @param packageName
     * @param output
     */
    public static void generate(File dir, String packageName, String output) {
        System.out.println("scan dir: " + dir.getAbsolutePath());

        JspKit jspKit = new JspKit();
        jspKit.setPackageName(packageName);
        jspKit.setOutput(output);

        try {
            jspKit.scan(dir);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     */
    public static void test1() {
        JspKit jspKit = new JspKit();
        String className = jspKit.getClassName(jspKit.packageName, "webapp/template/finder.jsp");
        String packageName = jspKit.getPackageName(className);
        String simpleName = jspKit.getSimpleName(className);
        System.out.println(className);
        System.out.println(packageName);
        System.out.println(simpleName);
    }

    /**
     * 
     */
    public static void test2() {
        try {
            JspKit jspKit = new JspKit();
            jspKit.setPackageName("test.mytest");
            System.out.println(jspKit.generate(new File("webapp/template/finder.jsp"), "a.b.FinderTemplate"));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param dir
     * @throws IOException 
     */
    public void scan(File dir) throws IOException {
        this.scan(dir, dir);
    }

    /**
     * @param home
     * @param dir
     * @throws IOException
     */
    private void scan(File home, File dir) throws IOException {
        if(dir.isFile()) {
            return;
        }

        File[] files = dir.listFiles();
        String work = home.getCanonicalPath();

        for(File file : files) {
            if(file.isDirectory()) {
                this.scan(home, file);
            }
            else {
                if(file.getName().endsWith(".jsp")) {
                    System.out.println(file.getAbsolutePath());
                    this.generate(work, file);
                }
            }
        }
    }

    /**
     * @param work
     * @param file
     * @throws IOException
     */
    private void generate(String work, File file) throws IOException {
        String path = Path.getRelativePath(work, file.getCanonicalPath());
        String className = this.getClassName(this.packageName, path);
        String packageName = this.getPackageName(className);
        String simpleName = this.getSimpleName(className);
        String code = this.generate(file, className);

        File target = new File(this.output, packageName.replace('.', '/') + "/" + simpleName + ".java");
        File parent = target.getParentFile();

        if(!parent.exists()) {
            parent.mkdirs();
        }
        IO.write(target, code.getBytes("utf-8"));
    }

    /**
     * @param file
     * @param className
     * @return String
     * @throws IOException
     */
    public String generate(File file, String className) throws IOException {
        Date date = new Date();
        List<JspNode> nodes = JspParser.parse(file, "utf-8");

        String jspDirective = getJspDirective(nodes);
        String jspDeclaration = getJspDeclaration(nodes);
        String methodBody = getMethodBody(nodes);
        String subClassBody = "";
        String staticDeclaration = "";
        String dependencies = "";
        String packageName = this.getPackageName(className);
        String simpleName = this.getSimpleName(className);

        Map<String, String> context = new HashMap<String, String>();
        context.put("java.className", simpleName);
        context.put("java.packageName", packageName);
        context.put("java.super.className", JspServlet.class.getName());
        context.put("build.date", DateUtil.format(date, "yyyy-MM-dd"));
        context.put("build.time", DateUtil.format(date, "yyyy-MM-dd HH:mm:ss SSS"));
        context.put("template.home", "");
        context.put("template.path", "");
        context.put("template.lastModified", "");
        context.put("template.dependencies", dependencies);
        context.put("options.fastJstl", "true");
        context.put("compiler.version", "1.0.0");
        context.put("jsp.directive.import", jspDirective);
        context.put("jsp.declaration", jspDeclaration);
        context.put("jsp.method.body", methodBody);
        context.put("jsp.subclass.body", subClassBody);
        context.put("jsp.static.declaration", staticDeclaration);
        return StringUtil.replace(JAVA_TEMPLATE, context);
    }
    
    /**
     * @param nodes
     * @return String
     */
    private String getJspDirective(List<JspNode> nodes) {
        StringBuilder buffer = new StringBuilder();

        for(JspNode jspNode : nodes) {
            if(jspNode.getNodeType() != NodeType.JSP_DIRECTIVE_PAGE) {
                continue;
            }

            if(jspNode.getAttribute("import") == null) {
                continue;
            }
            buffer.append("import ");
            buffer.append(jspNode.getAttribute("import"));
            buffer.append(";\r\n");
        }
        return buffer.toString();
    }

    /**
     * @param nodes
     * @return String
     */
    private String getJspDeclaration(List<JspNode> nodes) {
        StringBuilder buffer = new StringBuilder();

        for(JspNode jspNode : nodes) {
            if(jspNode.getNodeType() != NodeType.JSP_DECLARATION) {
                continue;
            }
            buffer.append(jspNode.getContent());
            buffer.append("\r\n");
        }
        return buffer.toString();
    }

    /**
     * @param nodes
     * @return String
     */
    private String getMethodBody(List<JspNode> nodes) {
        int nodeType = 0;
        StringBuilder buffer = new StringBuilder();

        for(JspNode jspNode : nodes) {
            nodeType = jspNode.getNodeType();

            if(nodeType == NodeType.TEXT) {
                buffer.append("        out.write(\"" + this.escape(jspNode.getContent()) + "\");\r\n");
            }
            else if(nodeType == NodeType.EXPRESSION) {
                buffer.append("        this.print(out, request.getAttribute(\"" + jspNode.getContent() + "\"));\r\n");
            }
            else if(nodeType == NodeType.JSP_EXPRESSION) {
                buffer.append("        this.print(out, (" + jspNode.getContent() + "));\r\n");
            }
            else if(nodeType == NodeType.JSP_SCRIPTLET) {
                buffer.append(jspNode.getContent());
                buffer.append("\r\n");
            }
            else if(nodeType == NodeType.JSP_DIRECTIVE_INCLUDE) {
                buffer.append("        // jsp.directive.include: ");
                buffer.append(jspNode.getContent());
                buffer.append("\r\n");
            }
        }
        return buffer.toString();
    }

    /**
     * @param text
     * @return String
     */
    private String escape(String text) {
        char c;
        int length = text.length();
        StringBuilder buffer = new StringBuilder();

        for(int i = 0; i < length; i++) {
            c = text.charAt(i);

            switch (c) {
                case '\\': {
                    buffer.append("\\\\");
                    break;
                }
                case '\'': {
                    buffer.append("\\\'");
                    break;
                }
                case '"': {
                    buffer.append("\\\"");
                    break;
                }
                case '\r': {
                    buffer.append("\\r");
                    break;
                }
                case '\n': {
                    buffer.append("\\n");
                    break;
                }
                case '\t': {
                    buffer.append("\\t");
                    break;
                }
                case '\b': {
                    buffer.append("\\b");
                    break;
                }
                case '\f': {
                    buffer.append("\\f");
                    break;
                }
                default : {
                    buffer.append(c);
                    break;
                }
            }
        }
        return buffer.toString();
    }

    /**
     * @param prefix
     * @param path
     * @return String
     */
    protected String getClassName(String prefix, String path) {
        String className = Path.getStrictPath(path);
        int k = className.lastIndexOf(".");

        if(k > -1) {
            className = className.substring(0, k);
        }

        String[] array = StringUtil.split(className, "/", true, true);
        String simpleName = this.getJavaIdentifier(array[array.length - 1]);

        if(simpleName.length() < 1) {
            throw new RuntimeException("BadFileNameException: file \"" + path + "\"");
        }

        StringBuilder buffer = new StringBuilder();

        if(prefix != null && prefix.length() > 0) {
            buffer.append(prefix.trim());
        }

        for(int i = 0, length = array.length - 1; i < length; i++) {
            if(buffer.length() > 0) {
                buffer.append("._");
            }
            else {
                buffer.append("_");
            }
            buffer.append(array[i]);
        }

        buffer.append(".");
        buffer.append(Character.toUpperCase(simpleName.charAt(0)));
        buffer.append(simpleName.substring(1));
        buffer.append("Template");
        return buffer.toString();
    }

    /**
     * @param className
     * @return String
     */
    protected String getSimpleName(String className) {
        int k = className.lastIndexOf(".");

        if(k > -1) {
            return className.substring(k + 1);
        }
        return className;
    }

    /**
     * @param className
     * @return String
     */
    protected String getPackageName(String className) {
        int k = className.lastIndexOf(".");

        if(k > -1) {
            return className.substring(0, k);
        }
        return "";
    }

    /**
     * @param name
     * @return String
     */
    protected String getJavaIdentifier(String name) {
        char c;
        StringBuilder buffer = new StringBuilder();

        for(int i = 0; i < name.length(); i++) {
            c = name.charAt(i);

            if(Character.isJavaIdentifierPart(c)) {
                if(buffer.length() < 1) {
                    if(c == '_' || !Character.isJavaIdentifierStart(c)) {
                        buffer.append("_");
                    }
                }
                buffer.append(c);
            }
        }
        return buffer.toString();
    }

    /**
     * @return String
     */
    protected static String getJavaTemplate() {
        InputStream inputStream = JspKit.class.getResourceAsStream("servlet.jsp");

        if(inputStream != null) {
            try {
                return IO.toString(inputStream, "utf-8");
            }
            catch (IOException e) {
            }
            finally {
                IO.close(inputStream);
            }
        }

        inputStream = JspKit.class.getResourceAsStream("class.jsp");

        if(inputStream != null) {
            try {
                return IO.toString(inputStream, "utf-8");
            }
            catch (IOException e) {
            }
            finally {
                IO.close(inputStream);
            }
        }
        return null;
    }

    /**
     * @return the packageName
     */
    public String getPackageName() {
        return this.packageName;
    }

    /**
     * @param packageName the packageName to set
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * @return the output
     */
    public String getOutput() {
        return this.output;
    }

    /**
     * @param output the output to set
     */
    public void setOutput(String output) {
        this.output = output;
    }
}
