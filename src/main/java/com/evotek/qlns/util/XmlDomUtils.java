/**
 * Copyright (C) 2012 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.evotek.qlns.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.evotek.qlns.common.impl.NamespaceContextImpl;

/**
 *
 * @author os_linhlh2
 */
public class XmlDomUtils {

    private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    private Document dom;
    private XPath xpath = XPathFactory.newInstance().newXPath();

    public XmlDomUtils() {
    }
    
    public Document read(File file) {
        try {
            DocumentBuilder db = this.dbf.newDocumentBuilder();

            this.dom = db.parse(file);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return this.dom;
    }

    public Document read(InputStream inputStream) {
        try {
            DocumentBuilder db = this.dbf.newDocumentBuilder();

            this.dom = db.parse(inputStream);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return this.dom;
    }

    public Document read(Blob blob) {
        try {
            if(Validator.isNull(blob)){
                return null;
            }

            DocumentBuilder db = this.dbf.newDocumentBuilder();

            InputStream inputStream = blob.getBinaryStream();

            this.dom = db.parse(inputStream);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return this.dom;
    }

    public Document read(String uri) {
        try {
            DocumentBuilder db = this.dbf.newDocumentBuilder();

            this.dom = db.parse(uri);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return this.dom;
    }

    public Document read(File file, boolean namespaceAware) {
        try {
            this.dbf.setNamespaceAware(namespaceAware);

            this.dom = read(file);

            this.setNamespaceContext(this.dom);
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }

        return this.dom;
    }

    public Document read(InputStream inputStream, boolean namespaceAware) {
        try {
            this.dbf.setNamespaceAware(namespaceAware);

            this.dom = read(inputStream);

            this.setNamespaceContext(this.dom);
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }

        return this.dom;
    }

    public Document read(String uri, boolean namespaceAware) {
        try {
            this.dbf.setNamespaceAware(namespaceAware);

            this.dom = read(uri);

            this.setNamespaceContext(this.dom);
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }

        return this.dom;
    }

    public Document readString(String xml) {
        try {
            DocumentBuilder db = this.dbf.newDocumentBuilder();

            InputSource is = new InputSource(new StringReader(xml));

            this.dom = db.parse(is);
        }catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return this.dom;
    }

    public Element getRootElement(Document doc) {
        return doc.getDocumentElement();
    }

    public Document createDocument() {
        try {
            DocumentBuilder db = this.dbf.newDocumentBuilder();

            this.dom = db.newDocument();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }

        return this.dom;
    }

    public NodeList selectNodeList(Document doc, String tagName) {
        return  doc.getElementsByTagName(tagName);
    }

    public NodeList selectNodeList(Element element, String tagName) {
        return  element.getElementsByTagName(tagName);
    }

    public List<Node> selectNode(Document doc, String tagName) {
        NodeList nodeList = doc.getElementsByTagName(tagName);

        List<Node> nodes = new ArrayList<Node>();

        for(int i=0; i<nodeList.getLength(); i++) {
            nodes.add(nodeList.item(i));
        }

        return nodes;
    }

    public List<Node> selectNode(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);

        List<Node> nodes = new ArrayList<Node>();

        for(int i=0; i<nodeList.getLength(); i++) {
            nodes.add(nodeList.item(i));
        }

        return nodes;
    }
    
    public Node selectUniqueNode(NodeList nodeList, String tagName, String attrName, 
            String attrValue) {
        Node node = null;
        
        for(int i=0; i<nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            
            String attr = element.getAttribute(attrName);
            
            if(attr!=null && attr.equals(attrValue)){
                node = element;
            }
        }
        
        return node;
    }

    public Node selectUniqueNode(Document doc, String tagName, String attrName,
            String attrValue) {
        NodeList nodeList = selectNodeList(doc, tagName);

        return selectUniqueNode(nodeList, tagName, attrName, attrValue);
    }

    public Node selectUniqueNode(Element element, String tagName, String attrName,
            String attrValue) {
        NodeList nodeList = selectNodeList(element, tagName);

        return selectUniqueNode(nodeList, tagName, attrName, attrValue);
    }

    public Node selectFirstNode(Document doc, String tagName) {
        Node node = null;

        NodeList nodeList = doc.getElementsByTagName(tagName);

        if(nodeList.getLength()>0) {
            node = nodeList.item(0);
        }

        return node;
    }

    public Node selectFirstNode(Element element, String tagName) {
        Node node = null;

        NodeList nodeList = element.getElementsByTagName(tagName);

        if(nodeList.getLength()>0) {
            node = nodeList.item(0);
        }

        return node;
    }

    public Node selectLastNode(Document doc, String tagName) {
        Node node = null;

        NodeList nodeList = doc.getElementsByTagName(tagName);

        if(nodeList.getLength()>0) {
            node = nodeList.item(nodeList.getLength()-1);
        }

        return node;
    }

    public Node selectLastNode(Element element, String tagName) {
        Node node = null;

        NodeList nodeList = element.getElementsByTagName(tagName);

        if(nodeList.getLength()>0) {
            node = nodeList.item(nodeList.getLength()-1);
        }

        return node;
    }

    public Node selectNodeByIndex(Document doc, String tagName, int index) {
        Node node = null;

        NodeList nodeList = doc.getElementsByTagName(tagName);

        if(nodeList.getLength()>0 && index< nodeList.getLength()) {
            node = nodeList.item(index);
        }

        return node;
    }

    public Node selectNodeByIndex(Element element, String tagName, int index) {
        Node node = null;

        NodeList nodeList = element.getElementsByTagName(tagName);

        if(nodeList.getLength()>0 && index< nodeList.getLength()) {
            node = nodeList.item(index);
        }

        return node;
    }

    public String getAttribute(Node node, String attrName) {
        Element element = (Element) node;

        return element.getAttribute(attrName);
    }

    public Map<String, String> getMapAttribute(NodeList nodeList, String key,
            String value) {
        Map<String, String> mapAttribute = new HashMap<String, String>();

        for(int i=0; i<nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);

            String _key = element.getAttribute(key);
            String _value = element.getAttribute(key);

            if(_key!=null && _value!=null) {
                mapAttribute.put(_key, _value);
            }
        }

        return mapAttribute;
    }

    public Map<String, String> getMapAttribute(Document doc, String tagName,
            String key, String value) {
        Map<String, String> mapAttribute = new HashMap<String, String>();

        NodeList nodeList = doc.getElementsByTagName(tagName);

        if(nodeList.getLength()>0) {
            mapAttribute = getMapAttribute(nodeList, key, value);
        }

        return mapAttribute;
    }

    public List<String[]> getChildValues(NodeList nodeList) {
        List<String[]> results = new ArrayList<String[]>();

        for(int i=0; i< nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);

            NodeList childNodes = element.getChildNodes();

            if(childNodes.getLength()>0) {
                String[] values = new String[childNodes.getLength()];

                for(int j=0; j< childNodes.getLength(); j++) {
                    values[j] = childNodes.item(j).getTextContent();
                }

                results.add(values);
            }
        }

        return results;
    }

    public List<String[]> getChildValues(Document doc, String tagName) {
        NodeList nodeList = doc.getElementsByTagName(tagName);

        return getChildValues(nodeList);
    }

    public List<String[]> getChildValues(NodeList nodeList, String[] tagNames) {
        List<String[]> results = new ArrayList<String[]>();

        int size = tagNames.length;

        for(int i=0; i< nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);

            if(size>0) {
                String[] values = new String[size];

                for(int j=0; j< size; j++) {
                    Element child = (Element) selectFirstNode(element, tagNames[j]);

                    values[j] = child.getTextContent();
                }

                results.add(values);
            }
        }

        return results;
    }

    public List<String[]> getChildValues(Document doc, String tagName,
            String[] tagNames) {
        NodeList nodeList = doc.getElementsByTagName(tagName);

        return getChildValues(nodeList, tagNames);
    }

    public NodeList getNodeListByXpath(Document doc, String path)
            throws Exception {
        NodeList nodes = null;

        try {
            XPathExpression xPathExpression = this.xpath.compile(path);

            Object result = xPathExpression.evaluate(doc, XPathConstants.NODESET);

            if (result == null) {
                return null;
            }

            nodes = (NodeList) result;
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return nodes;
    }

    public Node getNodeByXpath(Document doc, String path)
            throws Exception {
        Node node = null;

        try {
            XPathExpression xPathExpression = this.xpath.compile(path);

            Object result = xPathExpression.evaluate(doc, XPathConstants.NODE);

            if (result == null) {
                return null;
            }

            node = (Node) result;
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return node;
    }

    public Element getElementByXpath(Document doc, String path)
            throws Exception {
        Element child = null;

        try {
            Node node = getNodeByXpath(doc, path);

            if (node == null) {
                return null;
            }

            child = (Element) node;
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }


        return child;
    }

    public NodeList getNodeListByXpath(Element e, String path)
            throws Exception {
        NodeList nodes = null;

        try {
            XPathExpression xPathExpression = this.xpath.compile(path);

            Object result = xPathExpression.evaluate(e, XPathConstants.NODESET);

            if (result == null) {
                return null;
            }

            nodes = (NodeList) result;
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return nodes;
    }

    public Node getNodeByXpath(Element e, String path)
            throws Exception {
        Node node = null;

        try {

            XPathExpression xPathExpression = this.xpath.compile(path);

            Object result = xPathExpression.evaluate(e, XPathConstants.NODE);

            if (result == null) {
                return null;
            }

            node = (Node) result;

        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return node;
    }

    public Element getElementByXpath(Element e, String path)
            throws Exception {
        Element child = null;

        try {
            Node node = getNodeByXpath(e, path);

            if (node == null) {
                return null;
            }
            
            child = (Element) node;
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return child;
    }

    private void setNamespaceContext(Document doc)
            throws Exception {
        try {
            if (this.dbf.isNamespaceAware()) {
                this.xpath.setNamespaceContext(new NamespaceContextImpl(doc, true));
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    public Element createElement(Document doc, String nameElement,
            String data, Element parent) {
        Element element = null;

        try {
            if (nameElement != null) {
                element = doc.createElement(nameElement);
                if (data != null) {
                    element.appendChild(doc.createTextNode(data));
                }
                if (parent != null) {
                    parent.appendChild(element);
                }
            }
        } catch (DOMException ex) {
            _log.error(ex.getMessage(), ex);
        }

        return element;
    }
    
    public String getElementContent(Element parent, String path){
        String content = StringPool.BLANK;
        
        try {
            Element child = getElementByXpath(parent, path);
            
            if(child != null){
                content = child.getTextContent();
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
        
        return content;
    }
    
    private static final Logger _log = LogManager.getLogger(XmlDomUtils.class);
}
