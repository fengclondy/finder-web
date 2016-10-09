/*
 * $RCSfile: KeyInfo.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-03-04 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.skin.util.Attributes;
import com.skin.util.JsonUtil;

/**
 * <p>Title: KeyInfo</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class KeyConfigFactory {
    /**
     * @param args
     */
    public static void main(String[] args) {
        ClassLoader classLoader = KeyConfigFactory.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("META-INF/conf/usergroup.xml");

        try {
            List<KeyInfo> keys = KeyConfigFactory.parse(inputStream);

            for(KeyInfo keyInfo : keys) {
                System.out.println(JsonUtil.stringify(keyInfo));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param resource
     * @return List<KeyInfo>
     */
    public static List<KeyInfo> parse(String resource) {
        ClassLoader classLoader = KeyConfigFactory.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(resource);

        if(inputStream != null) {
            return KeyConfigFactory.parse(inputStream);
        }
        else {
            return null;
        }
    }

    /**
     * @param resource
     * @param attributes
     * @return List<KeyInfo>
     */
    public static List<KeyInfo> parse(String resource, Attributes attributes) {
        ClassLoader classLoader = KeyConfigFactory.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(resource);

        if(inputStream != null) {
            List<KeyInfo> keyInfoList = KeyConfigFactory.parse(inputStream);

            if(keyInfoList != null && attributes != null) {
                for(KeyInfo keyInfo : keyInfoList) {
                    String name = keyInfo.getName();
                    String value = attributes.getValue(name);

                    if(value != null) {
                        keyInfo.setValue(value);
                    }
                }
            }
            return keyInfoList;
        }
        else {
            return null;
        }
    }

    /**
     * @param inputStream
     * @return List<KeyInfo>
     */
    public static List<KeyInfo> parse(InputStream inputStream) {
        List<KeyInfo> keys = new ArrayList<KeyInfo>();

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(inputStream));
            Element element = document.getDocumentElement();
            NodeList childNodes = element.getChildNodes();

            for(int i = 0, length = childNodes.getLength(); i < length; i++) {
                Node node = childNodes.item(i);
                int nodeType = node.getNodeType();

                if(nodeType == Node.ELEMENT_NODE) {
                    String nodeName = node.getNodeName();

                    if(nodeName.equals("key")) {
                        KeyInfo keyInfo = getKeyInfo(node);

                        if(keyInfo != null) {
                            keys.add(keyInfo);
                        }
                    }
                }
            }
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
        return keys;
    }

    /**
     * @param node
     * @return KeyInfo
     */
    private static KeyInfo getKeyInfo(Node node) {
        NodeList childNodes = node.getChildNodes();

        if(childNodes.getLength() < 1) {
            return null;
        }

        KeyInfo keyInfo = new KeyInfo();

        for(int i = 0, length = childNodes.getLength(); i < length; i++) {
            Node n = childNodes.item(i);

            if(n.getNodeType() == Node.ELEMENT_NODE) {
                String nodeName = n.getNodeName();

                if(nodeName.equals("name")) {
                    keyInfo.setName(n.getTextContent().trim());
                }
                else if(nodeName.equals("title")) {
                    keyInfo.setTitle(n.getTextContent().trim());
                }
                else if(nodeName.equals("type")) {
                    keyInfo.setType(n.getTextContent().trim());
                }
                else if(nodeName.equals("value")) {
                    keyInfo.setValue(n.getTextContent().trim());
                }
                else if(nodeName.equals("description")) {
                    keyInfo.setDescription(n.getTextContent().trim());
                }
                else if(nodeName.equals("options")) {
                    Options options = getOptions(n);
                    keyInfo.setOptions(options);
                }
            }
        }
        return keyInfo;
    }

    /**
     * @param node
     * @return Options
     */
    private static Options getOptions(Node node) {
        Options options = new Options();
        String type = getAttribute(node, "type");
        String multiple = getAttribute(node, "multiple");
        options.setType(type);
        options.setMultiple("true".equals(multiple));
        NodeList childNodes = node.getChildNodes();

        if(childNodes.getLength() > 0) {
            for(int i = 0, length = childNodes.getLength(); i < length; i++) {
                Node n = childNodes.item(i);

                if(n.getNodeType() == Node.ELEMENT_NODE) {
                    String nodeName = n.getNodeName();

                    if(nodeName.equals("option")) {
                        String text = n.getTextContent().trim();
                        String value = getAttribute(n, "value");
                        options.add(new Option(text, value));
                    }
                }
            }
        }
        return options;
    }

    /**
     * @param node
     * @param name
     * @return String
     */
    private static String getAttribute(Node node, String name) {
        NamedNodeMap map = node.getAttributes();
        Node n = map.getNamedItem(name);

        if(n != null) {
            return n.getTextContent().trim();
        }
        else {
            return null;
        }
    }
}
