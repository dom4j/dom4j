/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.dom4j.rule.Pattern;
import org.dom4j.tree.AbstractDocument;
import org.dom4j.tree.DefaultAttribute;
import org.dom4j.tree.DefaultCDATA;
import org.dom4j.tree.DefaultComment;
import org.dom4j.tree.DefaultDocument;
import org.dom4j.tree.DefaultDocumentType;
import org.dom4j.tree.DefaultElement;
import org.dom4j.tree.DefaultEntity;
import org.dom4j.tree.DefaultProcessingInstruction;
import org.dom4j.tree.DefaultText;
import org.dom4j.tree.QNameCache;
import org.dom4j.util.SimpleSingleton;
import org.dom4j.util.SingletonStrategy;
import org.dom4j.xpath.DefaultXPath;
import org.dom4j.xpath.XPathPattern;
import org.jaxen.VariableContext;

/**
 * <p>
 * <code>DocumentFactory</code> is a collection of factory methods to allow
 * easy custom building of DOM4J trees. The default tree that is built uses a
 * doubly linked tree.
 * </p>
 * 
 * <p>
 * The tree built allows full XPath expressions from anywhere on the tree.
 * </p>
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 */
public class DocumentFactory implements Serializable {
    private static SingletonStrategy singleton = null;

    protected transient QNameCache cache;

    /** Default namespace prefix -> URI mappings for XPath expressions to use */
    private Map xpathNamespaceURIs;

    private static SingletonStrategy createSingleton() {
        SingletonStrategy result = null;
        
        String documentFactoryClassName;
        try {
            documentFactoryClassName = System.getProperty("org.dom4j.factory",
                    "org.dom4j.DocumentFactory");
        } catch (Exception e) {
            documentFactoryClassName = "org.dom4j.DocumentFactory";
        }

        try {
            String singletonClass = System.getProperty(
                    "org.dom4j.DocumentFactory.singleton.strategy",
                    "org.dom4j.util.SimpleSingleton");
            Class clazz = Class.forName(singletonClass);
            result = (SingletonStrategy) clazz.newInstance();
        } catch (Exception e) {
            result = new SimpleSingleton();
        }

        result.setSingletonClassName(documentFactoryClassName);
        
        return result;
    }

    public DocumentFactory() {
        init();
    }

    /**
     * <p>
     * Access to singleton implementation of DocumentFactory which is used if no
     * DocumentFactory is specified when building using the standard builders.
     * </p>
     * 
     * @return the default singleon instance
     */
    public static synchronized DocumentFactory getInstance() {
        if (singleton == null) {
            singleton = createSingleton();
        }
        return (DocumentFactory) singleton.instance();
    }

    // Factory methods
    public Document createDocument() {
        DefaultDocument answer = new DefaultDocument();
        answer.setDocumentFactory(this);

        return answer;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param encoding
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @since 1.5
     */
    public Document createDocument(String encoding) {
        // to keep the DocumentFactory backwards compatible, we have to do this
        // in this not so nice way, since subclasses only need to extend the
        // createDocument() method.
        Document answer = createDocument();

        if (answer instanceof AbstractDocument) {
            ((AbstractDocument) answer).setXMLEncoding(encoding);
        }

        return answer;
    }

    public Document createDocument(Element rootElement) {
        Document answer = createDocument();
        answer.setRootElement(rootElement);

        return answer;
    }

    public DocumentType createDocType(String name, String publicId,
            String systemId) {
        return new DefaultDocumentType(name, publicId, systemId);
    }

    public Element createElement(QName qname) {
        return new DefaultElement(qname);
    }

    public Element createElement(String name) {
        return createElement(createQName(name));
    }

    public Element createElement(String qualifiedName, String namespaceURI) {
        return createElement(createQName(qualifiedName, namespaceURI));
    }

    public Attribute createAttribute(Element owner, QName qname, String value) {
        return new DefaultAttribute(qname, value);
    }

    public Attribute createAttribute(Element owner, String name, String value) {
        return createAttribute(owner, createQName(name), value);
    }

    public CDATA createCDATA(String text) {
        return new DefaultCDATA(text);
    }

    public Comment createComment(String text) {
        return new DefaultComment(text);
    }

    public Text createText(String text) {
        if (text == null) {
            String msg = "Adding text to an XML document must not be null";
            throw new IllegalArgumentException(msg);
        }

        return new DefaultText(text);
    }

    public Entity createEntity(String name, String text) {
        return new DefaultEntity(name, text);
    }

    public Namespace createNamespace(String prefix, String uri) {
        return Namespace.get(prefix, uri);
    }

    public ProcessingInstruction createProcessingInstruction(String target,
            String data) {
        return new DefaultProcessingInstruction(target, data);
    }

    public ProcessingInstruction createProcessingInstruction(String target,
            Map data) {
        return new DefaultProcessingInstruction(target, data);
    }

    public QName createQName(String localName, Namespace namespace) {
        return cache.get(localName, namespace);
    }

    public QName createQName(String localName) {
        return cache.get(localName);
    }

    public QName createQName(String name, String prefix, String uri) {
        return cache.get(name, Namespace.get(prefix, uri));
    }

    public QName createQName(String qualifiedName, String uri) {
        return cache.get(qualifiedName, uri);
    }

    /**
     * <p>
     * <code>createXPath</code> parses an XPath expression and creates a new
     * XPath <code>XPath</code> instance.
     * </p>
     * 
     * @param xpathExpression
     *            is the XPath expression to create
     * 
     * @return a new <code>XPath</code> instance
     * 
     * @throws InvalidXPathException
     *             if the XPath expression is invalid
     */
    public XPath createXPath(String xpathExpression)
            throws InvalidXPathException {
        DefaultXPath xpath = new DefaultXPath(xpathExpression);

        if (xpathNamespaceURIs != null) {
            xpath.setNamespaceURIs(xpathNamespaceURIs);
        }

        return xpath;
    }

    /**
     * <p>
     * <code>createXPath</code> parses an XPath expression and creates a new
     * XPath <code>XPath</code> instance.
     * </p>
     * 
     * @param xpathExpression
     *            is the XPath expression to create
     * @param variableContext
     *            is the variable context to use when evaluating the XPath
     * 
     * @return a new <code>XPath</code> instance
     */
    public XPath createXPath(String xpathExpression,
            VariableContext variableContext) {
        XPath xpath = createXPath(xpathExpression);
        xpath.setVariableContext(variableContext);

        return xpath;
    }

    /**
     * <p>
     * <code>createXPathFilter</code> parses a NodeFilter from the given XPath
     * filter expression. XPath filter expressions occur within XPath
     * expressions such as <code>self::node()[ filterExpression ]</code>
     * </p>
     * 
     * @param xpathFilterExpression
     *            is the XPath filter expression to create
     * @param variableContext
     *            is the variable context to use when evaluating the XPath
     * 
     * @return a new <code>NodeFilter</code> instance
     */
    public NodeFilter createXPathFilter(String xpathFilterExpression,
            VariableContext variableContext) {
        XPath answer = createXPath(xpathFilterExpression);

        // DefaultXPath answer = new DefaultXPath( xpathFilterExpression );
        answer.setVariableContext(variableContext);

        return answer;
    }

    /**
     * <p>
     * <code>createXPathFilter</code> parses a NodeFilter from the given XPath
     * filter expression. XPath filter expressions occur within XPath
     * expressions such as <code>self::node()[ filterExpression ]</code>
     * </p>
     * 
     * @param xpathFilterExpression
     *            is the XPath filter expression to create
     * 
     * @return a new <code>NodeFilter</code> instance
     */
    public NodeFilter createXPathFilter(String xpathFilterExpression) {
        return createXPath(xpathFilterExpression);

        // return new DefaultXPath( xpathFilterExpression );
    }

    /**
     * <p>
     * <code>createPattern</code> parses the given XPath expression to create
     * an XSLT style {@link Pattern}instance which can then be used in an XSLT
     * processing model.
     * </p>
     * 
     * @param xpathPattern
     *            is the XPath pattern expression to create
     * 
     * @return a new <code>Pattern</code> instance
     */
    public Pattern createPattern(String xpathPattern) {
        return new XPathPattern(xpathPattern);
    }

    // Properties
    // -------------------------------------------------------------------------

    /**
     * Returns a list of all the QName instances currently used by this document
     * factory
     * 
     * @return DOCUMENT ME!
     */
    public List getQNames() {
        return cache.getQNames();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return the Map of namespace URIs that will be used by by XPath
     *         expressions to resolve namespace prefixes into namespace URIs.
     *         The map is keyed by namespace prefix and the value is the
     *         namespace URI. This value could well be null to indicate no
     *         namespace URIs are being mapped.
     */
    public Map getXPathNamespaceURIs() {
        return xpathNamespaceURIs;
    }

    /**
     * Sets the namespace URIs to be used by XPath expressions created by this
     * factory or by nodes associated with this factory. The keys are namespace
     * prefixes and the values are namespace URIs.
     * 
     * @param namespaceURIs
     *            DOCUMENT ME!
     */
    public void setXPathNamespaceURIs(Map namespaceURIs) {
        this.xpathNamespaceURIs = namespaceURIs;
    }

    // Implementation methods
    // -------------------------------------------------------------------------

    /**
     * <p>
     * <code>createSingleton</code> creates the singleton instance from the
     * given class name.
     * </p>
     * 
     * @param className
     *            is the name of the DocumentFactory class to use
     * 
     * @return a new singleton instance.
     */
    protected static DocumentFactory createSingleton(String className) {
        // let's try and class load an implementation?
        try {
            // I'll use the current class loader
            // that loaded me to avoid problems in J2EE and web apps
            Class theClass = Class.forName(className, true,
                    DocumentFactory.class.getClassLoader());

            return (DocumentFactory) theClass.newInstance();
        } catch (Throwable e) {
            System.out.println("WARNING: Cannot load DocumentFactory: "
                    + className);

            return new DocumentFactory();
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @param qname
     *            DOCUMENT ME!
     * 
     * @return the cached QName instance if there is one or adds the given qname
     *         to the cache if not
     */
    protected QName intern(QName qname) {
        return cache.intern(qname);
    }

    /**
     * Factory method to create the QNameCache. This method should be overloaded
     * if you wish to use your own derivation of QName.
     * 
     * @return DOCUMENT ME!
     */
    protected QNameCache createQNameCache() {
        return new QNameCache(this);
    }

    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        in.defaultReadObject();
        init();
    }

    protected void init() {
        cache = createQNameCache();
    }
}

/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * 3. The name "DOM4J" must not be used to endorse or promote products derived
 * from this Software without prior written permission of MetaStuff, Ltd. For
 * written permission, please contact dom4j-info@metastuff.com.
 * 
 * 4. Products derived from this Software may not be called "DOM4J" nor may
 * "DOM4J" appear in their names without prior written permission of MetaStuff,
 * Ltd. DOM4J is a registered trademark of MetaStuff, Ltd.
 * 
 * 5. Due credit should be given to the DOM4J Project - http://www.dom4j.org
 * 
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL METASTUFF, LTD. OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 */