/*
 * $RCSfile: ClusterManager.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.cluster;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

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

import com.skin.finder.config.ConfigFactory;
import com.skin.finder.util.StringUtil;

/**
 * <p>Title: ClusterManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ClusterManager {
    private static final Logger logger = LoggerFactory.getLogger(ClusterManager.class);

    /**
     * cluster config
     */
    public static Cluster cluster = load();

    /**
     * @param args
     */
    public static void main(String[] args) {
        String xml = ClusterUtil.build(cluster);
        System.out.println(xml);
    }

    /**
     * @return Cluster
     */
    public static Cluster getInstance() {
        return cluster;
    }

    /**
     * @param name
     * @return Host
     */
    public static Host getHost(String name) {
        return cluster.getHost(name);
    }

    /**
     * @param host
     * @param workspace
     * @return Workspace
     */
    public static Workspace getHost(String host, String workspace) {
        Host node = cluster.getHost(host);
        return (node != null ? node.getWorkspace(workspace) : null);
    }

    /**
     * @return Cluster
     */
    public static Cluster load() {
        InputStream inputStream = ConfigFactory.getInputStream("host.xml");

        if(inputStream != null) {
            try {
                return ClusterManager.load(new InputStreamReader(inputStream, "utf-8"));
            }
            catch(Exception e) {
                logger.error(e.getMessage(), e);
            }
            finally {
                try {
                    inputStream.close();
                }
                catch(IOException e) {
                }
            }
        }
        return null;
    }

    /**
     * @param reader
     * @return Cluster
     * @throws Exception
     */
    private static Cluster load(Reader reader) throws Exception {
        Cluster cluster = new Cluster();
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

                if(nodeName.equals("host")) {
                    Host host = getHost(node);

                    if(host != null) {
                        cluster.add(host);
                    }
                }
            }
        }
        return cluster;
    }

    /**
     * @param node
     * @return Host
     * @throws Exception
     */
    private static Host getHost(Node node) throws Exception {
        Host host = new Host();
        host.setName(getAttribute(node, "name", null));
        host.setUrl(getAttribute(node, "url", null));

        if(StringUtil.isBlank(host.getName())) {
            throw new NullPointerException("name must be not null");
        }

        if(StringUtil.isBlank(host.getUrl())) {
            throw new NullPointerException("url must be not null");
        }

        NodeList childNodes = node.getChildNodes();

        if(childNodes.getLength() < 1) {
            return host;
        }

        for(int i = 0, length = childNodes.getLength(); i < length; i++) {
            Node item = childNodes.item(i);

            if(item.getNodeType() == Node.ELEMENT_NODE) {
                String nodeName = item.getNodeName();

                if(nodeName.equals("workspace")) {
                    host.add(getWorkspace(item));
                }
            }
        }
        return host;
    }

    /**
     * @param node
     * @return Workspace
     */
    private static Workspace getWorkspace(Node node) {
        Workspace workspace = new Workspace();
        workspace.setName(getAttribute(node, "name", null));
        workspace.setWork(getAttribute(node, "work", null));
        workspace.setReadonly(getBooleanAttribute(node, "readonly", false));
        return workspace;
    }

    /**
     * @param node
     * @param name
     * @param defaultValue
     * @return String
     */
    private static String getAttribute(Node node, String name, String defaultValue) {
        NamedNodeMap attributes = node.getAttributes();

        if(attributes != null) {
            Node item = attributes.getNamedItem(name);

            if(item != null) {
                return item.getTextContent();
            }
        }
        return defaultValue;
    }

    /**
     * @param node
     * @param name
     * @param defaultValue
     * @return int
     */
    protected static int getIntAttribute(Node node, String name, int defaultValue) {
        String value = getAttribute(node, name, null);

        if(value != null) {
            try {
                return Integer.parseInt(value);
            }
            catch(NumberFormatException e) {
            }
        }
        return defaultValue;
    }

    /**
     * @param node
     * @param name
     * @param defaultValue
     * @return boolean
     */
    private static boolean getBooleanAttribute(Node node, String name, boolean defaultValue) {
        String value = getAttribute(node, name, null);

        if(value != null) {
            return value.equals("true");
        }
        return defaultValue;
    }

    /**
     * @param host
     * @return String
     */
    protected static String getDomain(String host, int port, String path) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("http://");
        buffer.append(host);

        if(port > 0 && port != 80) {
            buffer.append(port);
        }
        buffer.append(path);
        return buffer.toString();
    }
}
