/*
 * $RCSfile: WorkspaceManager.java,v $
 * $Revision: 1.1 $
 * $Date: 2013-10-15 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * <p>Title: WorkspaceManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class WorkspaceManager {
    private static final Logger logger = LoggerFactory.getLogger(Workspace.class);
    private static final Workspace instance = load();

    /**
     * default
     */
    private WorkspaceManager() {
    }

    /**
     * @param name
     * @param work
     */
    public static void add(String name, String work) {
        instance.add(name, work);
    }

    /**
     * @param name
     * @return String
     */
    public static String getWork(String name) {
        return instance.getWork(name);
    }

    /**
     * @return List<String>
     */
    public static List<String> getWorkspaces() {
        return instance.getWorkspaces();
    }

    /**
     * @return Workspace
     */
    private static Workspace load() {
        ClassLoader classLoader = Workspace.class.getClassLoader();
        Map<String, String> map = new LinkedHashMap<String, String>();
        InputStream inputStream = classLoader.getResourceAsStream("META-INF/conf/workspace.xml");

        if(inputStream != null) {
            try {
                load(inputStream, map);
            }
            catch(Exception e) {
                logger.warn(e.getMessage(), e);
            }
            finally {
                try {
                    inputStream.close();
                }
                catch(IOException e) {
                }
            }
        }
        return new Workspace(map);
    }

    /**
     * @param inputStream
     * @param map
     * @return Map<String, String>
     * @throws Exception
     */
    private static Map<String, String> load(InputStream inputStream, Map<String, String> map) throws Exception {
        return load(new InputStreamReader(inputStream, "utf-8"), map);
    }

    /**
     * @param reader
     * @param map
     * @return Map<String, String>
     * @throws Exception
     */
    private static Map<String, String> load(Reader reader, Map<String, String> map) throws Exception {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(reader));
            Element element = document.getDocumentElement();
            NodeList childNodes = element.getChildNodes();

            for(int i = 0, length = childNodes.getLength(); i < length; i++) {
                Node node = childNodes.item(i);
                int nodeType = node.getNodeType();

                if(nodeType == Node.ELEMENT_NODE) {
                    String nodeName = node.getNodeName();

                    if(nodeName.equals("parameter")) {
                        String name = getAttribute(node, "name");

                        if(name != null) {
                            map.put(name, node.getTextContent());
                        }
                    }
                }
            }
        }
        catch(Exception e) {
            throw e;
        }
        return map;
    }

    /**
     * @param node
     * @param name
     * @return String
     */
    private static String getAttribute(Node node, String name) {
        NamedNodeMap map = node.getAttributes();

        if(map != null) {
            for(int i = 0, len = map.getLength(); i < len; i++) {
                Node n = map.item(i);

                if(name.equals(n.getNodeName())) {
                    return n.getNodeValue();
                }
            }
        }
        return null;
    }
}
