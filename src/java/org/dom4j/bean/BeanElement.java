/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.bean;

import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.tree.DefaultElement;

/** <p><code>BeanElement</code> uses a Java Bean to store its attributes.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class BeanElement extends DefaultElement {

    /** The <code>DocumentFactory</code> instance used by default */
    private static final DocumentFactory DOCUMENT_FACTORY = BeanDocumentFactory.getInstance();
    
    /** The JavaBean which defines my attributes */
    private Object bean;
    
    
    public BeanElement(String name, Object bean) { 
        this( DOCUMENT_FACTORY.createQName(name), bean );
    }

    public BeanElement(String name,Namespace namespace, Object bean) { 
        this( DOCUMENT_FACTORY.createQName(name, namespace), bean );
    }

    public BeanElement(QName qname, Object bean) { 
        super( qname);
        this.bean = bean;
    }

    public BeanElement(QName qname) { 
        super( qname);
    }

    /** @return the JavaBean associated with this element 
      */
    public Object getData() {
        return bean;
    }
    
    public void setData(Object bean) {
        this.bean = bean;
        
        // force the attributeList to be lazily
        // created next time an attribute related
        // method is called again.
        setAttributeList(null);
    }
    
    public Attribute attribute(String name) {
        return getBeanAttributeList().attribute(name);
    }
    
    public Attribute attribute(QName qname) {
        return getBeanAttributeList().attribute(qname);
    }
    
    public Element addAttribute(String name, String value) {
        Attribute attribute = attribute(name);
        if (attribute != null ) {
            attribute.setValue(value);
        }
        return this;
    }

    public Element addAttribute(QName qName, String value) {
        Attribute attribute = attribute(qName);
        if (attribute != null ) {
            attribute.setValue(value);
        }
        return this;
    }
    
    public void setAttributes(List attributes) {
        throw new UnsupportedOperationException( "setAttributes(List) is not supported yet!" );
    }
    
    
    
    // Implementation methods
    //-------------------------------------------------------------------------        
    protected DocumentFactory getDocumentFactory() {
        return DOCUMENT_FACTORY;
    }
    
    protected BeanAttributeList getBeanAttributeList() {
        return (BeanAttributeList) attributeList();
    }
    
    /** A Factory Method pattern which lazily creates 
      * a List implementation used to store content
      */
    protected List createAttributeList() {
        return new BeanAttributeList(this);
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
