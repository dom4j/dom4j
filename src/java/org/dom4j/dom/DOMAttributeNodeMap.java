/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.dom;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

/**
 * <p>
 * <code>DOMAttributeNodeMap</code> implements a W3C NameNodeMap for the
 * attributes of an element.
 * </p>
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision$
 */
public class DOMAttributeNodeMap implements org.w3c.dom.NamedNodeMap {
    private DOMElement element;

    public DOMAttributeNodeMap(DOMElement element) {
        this.element = element;
    }

    // org.w3c.dom.NamedNodeMap interface
    // -------------------------------------------------------------------------
    public void foo() throws DOMException {
        DOMNodeHelper.notSupported();
    }

    public Node getNamedItem(String name) {
        return element.getAttributeNode(name);
    }

    public Node setNamedItem(Node arg) throws DOMException {
        if (arg instanceof Attr) {
            return element.setAttributeNode((org.w3c.dom.Attr) arg);
        } else {
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR,
                    "Node is not an Attr: " + arg);
        }
    }

    public Node removeNamedItem(String name) throws DOMException {
        org.w3c.dom.Attr attr = element.getAttributeNode(name);

        if (attr == null) {
            throw new DOMException(DOMException.NOT_FOUND_ERR,
                    "No attribute named " + name);
        }

        return element.removeAttributeNode(attr);
    }

    public Node item(int index) {
        return DOMNodeHelper.asDOMAttr(element.attribute(index));
    }

    public int getLength() {
        return element.attributeCount();
    }

    public Node getNamedItemNS(String namespaceURI, String localName) {
        return element.getAttributeNodeNS(namespaceURI, localName);
    }

    public Node setNamedItemNS(Node arg) throws DOMException {
        if (arg instanceof Attr) {
            return element.setAttributeNodeNS((org.w3c.dom.Attr) arg);
        } else {
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR,
                    "Node is not an Attr: " + arg);
        }
    }

    public Node removeNamedItemNS(String namespaceURI, String localName)
            throws DOMException {
        org.w3c.dom.Attr attr = element.getAttributeNodeNS(namespaceURI,
                localName);

        if (attr != null) {
            return element.removeAttributeNode(attr);
        }

        return attr;
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
