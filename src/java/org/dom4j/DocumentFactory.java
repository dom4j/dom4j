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

import org.dom4j.tree.DefaultAttribute;
import org.dom4j.tree.DefaultCDATA;
import org.dom4j.tree.DefaultComment;
import org.dom4j.tree.DefaultDocument;
import org.dom4j.tree.DefaultDocumentType;
import org.dom4j.tree.DefaultElement;
import org.dom4j.tree.DefaultEntity;
import org.dom4j.tree.DefaultNamespace;
import org.dom4j.tree.DefaultProcessingInstruction;
import org.dom4j.tree.DefaultText;

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
    protected static DocumentFactory singleton = new DocumentFactory();
 
    /** The <code>{@link XPathEngine}</code> used to resolve XPath expressions */
    protected XPathEngine XPathEngine;
    
    // Some static helper methods
    
    /** <p>Access to singleton implementation of DocumentFactory which 
      * is used if no DocumentFactory is specified when building using the 
      * standard builders.</p>
      *
      * @return the default singleon instance
      */
    public static DocumentFactory getInstance() {
        return singleton;
    }
    
    
    
    /** <p>This will return the <code>{@link XPathEngine}</code>
      * instance which will be used for all <code>Document</code>
      * instances created by this factory.</p>
      *
      * @return the resolver of XPath expressions
      */
    public XPathEngine getXPathEngine() {
        if ( XPathEngine == null ) {
            XPathEngine = createXPathEngine();
        }
        return XPathEngine;
    }

    /** <p>This will set the <code>{@link XPathEngine}</code>
      * instance which will be used for all <code>Document</code>
      * instances created by this factory.</p>
      *
      * @param XPathEngine <code>XPathEngine</code> - the resolver of XPath expressions
      */
    public void setXPathEngine(XPathEngine XPathEngine) {
        this.XPathEngine = XPathEngine;
    }


    // Static helper methods
    
    public static Document newDocument() {
        return singleton.createDocument();
    }
    
    public static Document newDocument(Element rootElement) {
        return singleton.createDocument(rootElement);
    }

    
    public static Element newElement(QName qname) {
        return singleton.createElement(qname);
    }
    
    public static Element newElement(String name) {
        return singleton.createElement(name);
    }
    
    public static Element newElement(String name, String prefix, String uri) {
        return singleton.createElement(name, prefix, uri);
    }
    
    public static Element newElement(String name, Namespace namespace) {
        return singleton.createElement(name, namespace);
    }
    
    
    public static Attribute newAttribute(QName qname, String value) {
        return singleton.createAttribute(qname, value);
    }
    
    public static Attribute newAttribute(String name, String value) {
        return singleton.createAttribute(name, value);
    }
    
    public static Attribute newAttribute(String name, String value, Namespace namespace) {
        return singleton.createAttribute(name, value, namespace);
    }

    public static CDATA newCDATA(String text) {
        return singleton.createCDATA(text);
    }
    
    public static Comment newComment(String text) {
        return singleton.createComment(text);
    }
    
    public static Text newText(String text) {
        return singleton.createText(text);
    }
    
    
    public static Entity newEntity(String name) {
        return singleton.createEntity(name);
    }
    
    public static Entity newEntity(String name, String text) {
        return singleton.createEntity(name, text);
    }
    
    public static Namespace newNamespace(String prefix, String uri) {
        return singleton.createNamespace(prefix, uri);
    }
    
    public static ProcessingInstruction newProcessingInstruction(String target, String data) {
        return singleton.createProcessingInstruction(target, data);
    }
    
    public static ProcessingInstruction newProcessingInstruction(String target, Map data) {
        return singleton.createProcessingInstruction(target, data);
    }
    
    public static QName newQName(String localName, Namespace namespace) {
        return singleton.createQName(localName, namespace);
    }
    
    public static QName newQName(String localName) {
        return singleton.createQName(localName);
    }
    
    
    
    // Factory methods
    
    public Document createDocument() {
        Document answer = new DefaultDocument();
        answer.setXPathEngine( getXPathEngine() );
        return answer;
    }
    
    
    public Document createDocument(Element rootElement) {
        Document answer = createDocument();
        answer.setRootElement(rootElement);
        return answer;
    }
    
    
    
    public Element createElement(QName qname) {
        return new DefaultElement(qname);
    }
    
    public Element createElement(String name) {
        return createElement(createQName(name));
    }
    
    public Element createElement(String name, String prefix, String uri) {
        return createElement(createQName(name, createNamespace(prefix, uri)));
    }
    
    public Element createElement(String name, Namespace namespace) {
        return createElement(createQName(name, namespace));
    }
    
    
    public Attribute createAttribute(QName qname, String value) {
        return new DefaultAttribute(qname, value);
    }
    
    public Attribute createAttribute(String name, String value) {
        return createAttribute(createQName(name), value);
    }
    
    public Attribute createAttribute(String name, String value, Namespace namespace) {
        return createAttribute(createQName(name, namespace), value);
    }

    public CDATA createCDATA(String text) {
        return new DefaultCDATA(text);
    }
    
    public Comment createComment(String text) {
        return new DefaultComment(text);
    }
    
    public Text createText(String text) {
        return new DefaultText(text);
    }
    
    
    public Entity createEntity(String name) {
        return new DefaultEntity(name);
    }
    
    public Entity createEntity(String name, String text) {
        return new DefaultEntity(name, text);
    }
    
    public Namespace createNamespace(String prefix, String uri) {
        return DefaultNamespace.get(prefix, uri);
    }
    
    public ProcessingInstruction createProcessingInstruction(String target, String data) {
        return new DefaultProcessingInstruction(target, data);
    }
    
    public ProcessingInstruction createProcessingInstruction(String target, Map data) {
        return new DefaultProcessingInstruction(target, data);
    }
    
    public QName createQName(String localName, Namespace namespace) {
        return QName.get(localName, namespace);
    }
    
    public QName createQName(String localName) {
        return QName.get(localName);
    }
    
    /** <p>A Factory Method pattern to create the default 
      * <code>{@link XPathEngine}</code> instance which will be used for 
      * all <code>Document</code> instances created by this factory.</p>
      *
      * @return the new resolver of XPath expressions
      */
    protected XPathEngine createXPathEngine() {
        // use the default
        return XPathHelper.getInstance();
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
