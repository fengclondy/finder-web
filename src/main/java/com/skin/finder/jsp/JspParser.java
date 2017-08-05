/*
 * $RCSfile: JspParser.java,v $
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.skin.finder.util.IO;

/**
 * <p>Title: JspParser</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class JspParser {
    /**
     * @param file
     * @param charset
     * @return String
     * @throws IOException
     */
    public static List<JspNode> parse(File file, String charset) throws IOException {
        String source = IO.toString(file, charset);

        char c;
        int i = 0;
        int length = source.length();
        StringBuilder text = new StringBuilder();
        List<JspNode> nodes = new ArrayList<JspNode>();

        while(i < length) {
            c = source.charAt(i);

            if(c == '<' && (i + 1) < length && source.charAt(i + 1) == '%') {
                i += 2;
                int k = source.indexOf("%>", i);
                String code = null;

                if(k > -1) {
                    code = source.substring(i, k);
                    i = k + 2;

                    if(i < length && source.charAt(i) == 0x0D) {
                        i++;
                    }

                    if(i < length && source.charAt(i) == 0x0A) {
                        i++;
                    }
                }
                else {
                    code = source.substring(i, length);
                    i = length;
                }

                if(text.length() > 0) {
                    nodes.add(new JspNode(NodeType.TEXT, text.toString()));
                    text.setLength(0);
                }

                if(code.length() > 0) {
                    if(code.charAt(0) == '=') {
                        nodes.add(new JspNode(NodeType.JSP_EXPRESSION, code.substring(1)));
                    }
                    else if(code.charAt(0) == '!') {
                        nodes.add(new JspNode(NodeType.JSP_DECLARATION, code.substring(1)));
                    }
                    else if(code.charAt(0) == '@') {
                        Map<String, String> attributes = parseAttributes(code.substring(1));

                        if("true".equals(attributes.get("page")) && attributes.get("import") != null) {
                            JspNode jspNode = new JspNode(NodeType.JSP_DIRECTIVE_PAGE, code.substring(1));
                            jspNode.setAttribute("import", attributes.get("import"));
                            nodes.add(jspNode);
                        }

                        if("true".equals(attributes.get("include")) && attributes.get("file") != null) {
                            include(nodes, file.getParentFile(), attributes.get("file"), charset);
                        }
                    }
                    else if(code.startsWith("--")) {
                        JspNode jspNode = new JspNode(NodeType.JSP_COMMENT, code.substring(2, code.length() - 2));
                        nodes.add(jspNode);
                    }
                    else {
                        nodes.add(new JspNode(NodeType.JSP_SCRIPTLET, code));
                    }
                }
            }
            else if(c == '$' && (i + 1) < length && source.charAt(i + 1) == '{') {
                i += 2;
                int k = source.indexOf("}", i);
                String expr = null;

                if(k > -1) {
                    expr = source.substring(i, k);
                    i = k + 1;
                }
                else {
                    expr = source.substring(i, length);
                    i = length;
                }

                if(text.length() > 0) {
                    nodes.add(new JspNode(NodeType.TEXT, text.toString()));
                    text.setLength(0);
                }

                if(expr.length() > 0) {
                    nodes.add(new JspNode(NodeType.EXPRESSION, expr));
                }
            }
            else {
                text.append(c);
                i++;
            }
        }

        if(text.length() > 0) {
            nodes.add(new JspNode(NodeType.TEXT, text.toString()));
            text.setLength(0);
        }

        /**
         * 对长文本做拆分, 要不然生成的代码太难看
         */
        return prettify(nodes);
    }

    /**
     * @param nodes
     * @return List<JspNode>
     */
    private static List<JspNode> prettify(List<JspNode> nodes) {
        List<JspNode> result = new ArrayList<JspNode>();

        for(JspNode jspNode : nodes) {
            if(jspNode.getNodeType() == NodeType.TEXT) {
                String content = jspNode.getContent();

                char c;
                int i = 0;
                int offset = 0;
                int length = content.length();

                while(i < length) {
                    c = content.charAt(i++);

                    if(c == 0x0a && (i - offset) >= 80) {
                        result.add(new JspNode(NodeType.TEXT, content.substring(offset, i)));
                        offset = i;
                    }
                }

                if(offset < length) {
                    result.add(new JspNode(NodeType.TEXT, content.substring(offset, length)));
                }
            }
            else {
                result.add(jspNode);
            }
        }
        return result;
    }

    /**
     * @param nodes
     * @param parent
     * @param file
     * @param charset
     * @throws IOException
     */
    private static void include(List<JspNode> nodes, File parent, String file, String charset) throws IOException {
        File page = new File(parent, file).getCanonicalFile();
        JspNode jspNode = new JspNode(NodeType.JSP_DIRECTIVE_INCLUDE, file);
        nodes.add(jspNode);

        List<JspNode> childs = parse(page, charset);
        nodes.addAll(childs);
    }

    /**
     * @return Map<String, String>
     */
    private static Map<String, String> parseAttributes(String text) {
        char c;
        int i = 0;
        int length = text.length();
        StringBuilder buffer = new StringBuilder();
        Map<String, String> attributes = new LinkedHashMap<String, String>();

        while(i < length) {
            /**
             * skip whitespace
             */
            while(i < length) {
                c = text.charAt(i);
    
                if(c <= 32) {
                    i++;
                    continue;
                }
                else {
                    break;
                }
            }

            if(i >= length) {
                break;
            }

            /**
             * read name
             */
            while(i < length) {
                c = text.charAt(i);

                if(Character.isLetter(c) || Character.isDigit(c) || c == ':' || c == '-' || c == '_') {
                    buffer.append(c);
                    i++;
                }
                else {
                    break;
                }
            }

            String name = buffer.toString();
            buffer.setLength(0);

            /**
             * skip whitespace
             */
            while(i < length) {
                c = text.charAt(i);
    
                if(c <= 32) {
                    i++;
                    continue;
                }
                else {
                    break;
                }
            }

            if(i < length && text.charAt(i) == '=') {
                i++;
            }
            else {
                attributes.put(name, "true");
                continue;
            }

            /**
             * skip whitespace
             */
            while(i < length) {
                c = text.charAt(i);
    
                if(c <= 32) {
                    i++;
                    continue;
                }
                else {
                    break;
                }
            }

            /**
             * quote
             */
            char q = (i < length ? text.charAt(i) : ' ');

            if(q == '\'' || q == '"') {
                i++;

                while(i < length) {
                    c = text.charAt(i++);

                    if(c != q) {
                        buffer.append(c);
                        continue;
                    }
                    else {
                        break;
                    }
                }
            }
            else {
                while(i < length) {
                    c = text.charAt(i++);

                    if(c > 32) {
                        buffer.append(c);
                        continue;
                    }
                    else {
                        break;
                    }
                }
            }

            String value = buffer.toString();
            buffer.setLength(0);
            attributes.put(name, value);
        }
        return attributes;
    }
}
