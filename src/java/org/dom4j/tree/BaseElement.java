/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.tree;

import java.util.List;

import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;

/**
 * <p>
 * <code>BaseElement</code> is a useful base class for implemementation
 * inheritence of an XML element.
 * </p>
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision$
 */
public class BaseElement extends AbstractElement {
    /** The <code>QName</code> for this element */
    private QName qname;

    /**
     * Stores the parent branch of this node which is either a Document if this
     * element is the root element in a document, or another Element if it is a
     * child of the root document, or null if it has not been added to a
     * document yet.
     */
    private Branch parentBranch;

    /** List of content nodes. */
    protected List content;

    /** list of attributes */
    protected List attributes;

    public BaseElement(String name) {
        this.qname = getDocumentFactory().createQName(name);
    }

    public BaseElement(QName qname) {
        this.qname = qname;
    }

    public BaseElement(String name, Namespace namespace) {
        this.qname = getDocumentFactory().createQName(name, namespace);
    }

    public Element getParent() {
        Element result = null;

        if (parentBranch instanceof Element) {
            result = (Element) parentBranch;
        }

        return result;
    }

    public void setParent(Element parent) {
        if (parentBranch instanceof Element || (parent != null)) {
            parentBranch = parent;
        }
    }

    public Document getDocument() {
        if (parentBranch instanceof Document) {
            return (Document) parentBranch;
        } else if (parentBranch instanceof Element) {
            Element parent = (Element) parentBranch;

            return parent.getDocument();
        }

        return null;
    }

    public void setDocument(Document document) {
        if (parentBranch instanceof Document || (document != null)) {
            parentBranch = document;
        }
    }

    public boolean supportsParent() {
        return true;
    }

    public QName getQName() {
        return qname;
    }

    public void setQName(QName name) {
        this.qname = name;
    }

    public void clearContent() {
        contentList().clear();
    }

    public void setContent(List content) {
        this.content = content;

        if (content instanceof ContentListFacade) {
            this.content = ((ContentListFacade) content).getBackingList();
        }
    }

    public void setAttributes(List attributes) {
        this.attributes = attributes;

        if (attributes instanceof ContentListFacade) {
            this.attributes = ((ContentListFacade) attributes).getBackingList();
        }
    }

    // Implementation methods
    // -------------------------------------------------------------------------
    protected List contentList() {
        if (content == null) {
            content = createContentList();
        }

        return content;
    }

    protected List attributeList() {
        if (attributes == null) {
            attributes = createAttributeList();
        }

        return attributes;
    }

    protected List attributeList(int size) {
        if (attributes == null) {
            attributes = createAttributeList(size);
        }

        return attributes;
    }

    protected void setAttributeList(List attributeList) {
        this.attributes = attributeList;
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
