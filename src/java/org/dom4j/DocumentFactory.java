/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j;

import java.util.Map;

import org.dom4j.rule.Pattern;
import org.dom4j.tree.DefaultAttribute;
import org.dom4j.tree.DefaultCDATA;
import org.dom4j.tree.DefaultComment;
import org.dom4j.tree.DefaultDocument;
import org.dom4j.tree.DefaultDocumentType;
import org.dom4j.tree.DefaultElement;
import org.dom4j.tree.DefaultEntity;
import org.dom4j.tree.DefaultProcessingInstruction;
import org.dom4j.tree.QNameCache;
import org.dom4j.tree.XPathEntity;
import org.dom4j.tree.XPathText;
import org.dom4j.xpath.DefaultXPath;
import org.dom4j.xpath.XPathPattern;

import org.xml.sax.Attributes;

/** <p><code>DocumentFactory</code> is a collection of factory methods to allow
  * easy custom building of DOM4J trees. The default tree that is built uses
  * a doubly linked tree. </p>
  *
  * <p>The tree built allows full XPath expressions from anywhere on the 
  * tree.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DocumentFactory {

    /** The Singleton instance */
    private static DocumentFactory singleton;
    
    protected transient QNameCache cache;

    static {
        String className = System.getProperty( 
            "org.dom4j.factory", 
            "org.dom4j.DocumentFactory" 
        );
        singleton = createSingleton( className );
    }
    
    /** <p>Access to singleton implementation of DocumentFactory which 
      * is used if no DocumentFactory is specified when building using the 
      * standard builders.</p>
      *
      * @return the default singleon instance
      */
    public static DocumentFactory getInstance() {
        return singleton;
    }

    public DocumentFactory() {
        cache = new QNameCache(this);
    }
    
    // Factory methods
    
    public Document createDocument() {
        DefaultDocument answer = new DefaultDocument();
        answer.setDocumentFactory( this );
        return answer;
    }
    
    public Document createDocument(Element rootElement) {
        Document answer = createDocument();
        answer.setRootElement(rootElement);
        return answer;
    }
    
    public DocumentType createDocType(String name, String publicId, String systemId) {
        return new DefaultDocumentType( name, publicId, systemId );
    }
    
    public Element createElement(QName qname) {
        return new DefaultElement(qname);
    }
    
    public Element createElement(QName qname, Attributes attributes) {
        return new DefaultElement(qname, attributes);
    }
    
    public Element createElement(String name) {
        return createElement(createQName(name));
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
        // allow editing in place by default
        return new XPathText(text);
    }
    
    
    public Entity createEntity(String name) {
        // allow editing in place by default
        return new XPathEntity(name);
    }
    
    public Entity createEntity(String name, String text) {
        return new DefaultEntity(name, text);
    }
    
    public Namespace createNamespace(String prefix, String uri) {
        return Namespace.get(prefix, uri);
    }
    
    public ProcessingInstruction createProcessingInstruction(String target, String data) {
        return new DefaultProcessingInstruction(target, data);
    }
    
    public ProcessingInstruction createProcessingInstruction(String target, Map data) {
        return new DefaultProcessingInstruction(target, data);
    }
    
    public QName createQName(String localName, Namespace namespace) {
        return cache.get(localName, namespace);
    }
    
    public QName createQName(String localName) {
        return cache.get(localName);
    }
    
    public QName createQName(String name, String prefix, String uri) {
        return cache.get(name, Namespace.get( prefix, uri ));
    }

    public QName createQName(String qualifiedName, String uri) {
        return cache.get(qualifiedName, uri);
    }
    
    /** <p><code>createXPath</code> parses an XPath expression
      * and creates a new XPath <code>XPath</code> instance.</p>
      *
      * @param xpathExpression is the XPath expression to create
      * @return a new <code>XPath</code> instance
      */
    public XPath createXPath(String xpathExpression) {
        return new DefaultXPath( xpathExpression );
    }

    /** <p><code>createXPath</code> parses an XPath expression
      * and creates a new XPath <code>XPath</code> instance.</p>
      *
      * @param xpathExpression is the XPath expression to create
      * @param variableContext is the variable context to use when evaluating the XPath
      * @return a new <code>XPath</code> instance
      */
    public XPath createXPath(String xpathExpression, VariableContext variableContext) {
        XPath xpath = createXPath( xpathExpression );
        xpath.setVariableContext( variableContext );
        return xpath;
    }

    /** <p><code>createXPathFilter</code> parses a NodeFilter
      * from the given XPath filter expression.
      * XPath filter expressions occur within XPath expressions such as
      * <code>self::node()[ filterExpression ]</code></p>
      *
      * @param xpathFilterExpression is the XPath filter expression 
      * to create
      * @param variableContext is the variable context to use when evaluating the XPath
      * @return a new <code>NodeFilter</code> instance
      */
    public NodeFilter createXPathFilter(String xpathFilterExpression, VariableContext variableContext) {
        DefaultXPath answer = new DefaultXPath( ".[" + xpathFilterExpression + "]" );        
        answer.setVariableContext( variableContext );
        return answer;
    }
    
    /** <p><code>createXPathFilter</code> parses a NodeFilter
      * from the given XPath filter expression.
      * XPath filter expressions occur within XPath expressions such as
      * <code>self::node()[ filterExpression ]</code></p>
      *
      * @param xpathFilterExpression is the XPath filter expression 
      * to create
      * @return a new <code>NodeFilter</code> instance
      */
    public NodeFilter createXPathFilter(String xpathFilterExpression) {
        return new DefaultXPath( ".[" + xpathFilterExpression + "]" );        
    }
    
    /** <p><code>createPattern</code> parses the given 
      * XPath expression to create an XSLT style {@link Pattern} instance
      * which can then be used in an XSLT processing model.</p>
      *
      * @param xpathPattern is the XPath pattern expression 
      * to create
      * @return a new <code>Pattern</code> instance
      */
    public Pattern createPattern(String xpathPattern) {
        return new XPathPattern( xpathPattern );
    }
    
    
    // Implementation methods
    
    /** <p><code>createSingleton</code> creates the singleton instance
      * from the given class name.</p>
      *
      * @param className is the name of the DocumentFactory class to use
      * @return a new singleton instance.
      */
    protected static DocumentFactory createSingleton(String className) {
        // let's try and class load an implementation?
        try {
            // I'll use the current class loader
            // that loaded me to avoid problems in J2EE and web apps
            Class theClass = Class.forName( 
                className, 
                true, 
                DocumentHelper.class.getClassLoader() 
            );
            return (DocumentFactory) theClass.newInstance();
        }
        catch (Throwable e) {
            System.out.println( "WARNING: Cannot load DocumentFactory: " + className );
            return new DocumentFactory();
        }
    }        
    
    /** @return the cached QName instance if there is one or adds the given
      * qname to the cache if not 
       */
    protected QName intern(QName qname) {
        return cache.intern(qname);
    }
}




/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "DOM4J" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of MetaStuff, Ltd.  For written permission,
 *    please contact dom4j-info@metastuff.com.
 *
 * 4. Products derived from this Software may not be called "DOM4J"
 *    nor may "DOM4J" appear in their names without prior written
 *    permission of MetaStuff, Ltd. DOM4J is a registered
 *    trademark of MetaStuff, Ltd.
 *
 * 5. Due credit should be given to the DOM4J Project
 *    (http://dom4j.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * METASTUFF, LTD. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * $Id$
 */
