/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.bean;

import org.dom4j.Attribute;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.tree.DefaultAttribute;

import org.xml.sax.Attributes;

/**
 * <p>
 * <code>BeanDocumentFactory</code> is a factory of DOM4J objects which may be
 * BeanElements which are backed by JavaBeans and their properties.
 * </p>
 * 
 * <p>
 * The tree built allows full XPath expressions from anywhere on the tree.
 * </p>
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision$
 */
public class BeanDocumentFactory extends DocumentFactory {
    /** The Singleton instance */
    private static BeanDocumentFactory singleton = new BeanDocumentFactory();

    /**
     * <p>
     * Access to the singleton instance of this factory.
     * </p>
     * 
     * @return the default singleon instance
     */
    public static DocumentFactory getInstance() {
        return singleton;
    }

    // Factory methods
    public Element createElement(QName qname) {
        Object bean = createBean(qname);

        if (bean == null) {
            return new BeanElement(qname);
        } else {
            return new BeanElement(qname, bean);
        }
    }

    public Element createElement(QName qname, Attributes attributes) {
        Object bean = createBean(qname, attributes);

        if (bean == null) {
            return new BeanElement(qname);
        } else {
            return new BeanElement(qname, bean);
        }
    }

    public Attribute createAttribute(Element owner, QName qname, String value) {
        return new DefaultAttribute(qname, value);
    }

    // Implementation methods
    protected Object createBean(QName qname) {
        return null;
    }

    protected Object createBean(QName qname, Attributes attributes) {
        String value = attributes.getValue("class");

        if (value != null) {
            try {
                Class beanClass = Class.forName(value, true,
                        BeanDocumentFactory.class.getClassLoader());

                return beanClass.newInstance();
            } catch (Exception e) {
                handleException(e);
            }
        }

        return null;
    }

    protected void handleException(Exception e) {
        // ignore introspection exceptions
        System.out.println("#### Warning: couldn't create bean: " + e);
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
