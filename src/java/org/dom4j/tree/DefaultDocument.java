/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.tree;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.IllegalAddException;
import org.dom4j.Node;
import org.dom4j.ProcessingInstruction;

import org.xml.sax.EntityResolver;

/**
 * <p>
 * <code>DefaultDocument</code> is the default DOM4J default implementation of
 * an XML document.
 * </p>
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision$
 */
public class DefaultDocument extends AbstractDocument {
    protected static final List EMPTY_LIST = Collections.EMPTY_LIST;

    protected static final Iterator EMPTY_ITERATOR = EMPTY_LIST.iterator();

    /** The name of the document */
    private String name;

    /** The root element of this document */
    private Element rootElement;

    /**
     * Store the contents of the document as a lazily created <code>List</code>
     */
    private List content;

    /** The document type for this document */
    private DocumentType docType;

    /** The document factory used by default */
    private DocumentFactory documentFactory = DocumentFactory.getInstance();

    /** The resolver of URIs */
    private transient EntityResolver entityResolver;

    public DefaultDocument() {
    }

    public DefaultDocument(String name) {
        this.name = name;
    }

    public DefaultDocument(Element rootElement) {
        this.rootElement = rootElement;
    }

    public DefaultDocument(DocumentType docType) {
        this.docType = docType;
    }

    public DefaultDocument(Element rootElement, DocumentType docType) {
        this.rootElement = rootElement;
        this.docType = docType;
    }

    public DefaultDocument(String name, Element rootElement,
            DocumentType docType) {
        this.name = name;
        this.rootElement = rootElement;
        this.docType = docType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Element getRootElement() {
        return rootElement;
    }

    public DocumentType getDocType() {
        return docType;
    }

    public void setDocType(DocumentType docType) {
        this.docType = docType;
    }

    public Document addDocType(String docTypeName, String publicId,
            String systemId) {
        setDocType(getDocumentFactory().createDocType(docTypeName, publicId,
                systemId));

        return this;
    }

    public String getXMLEncoding() {
        return encoding;
    }

    public EntityResolver getEntityResolver() {
        return entityResolver;
    }

    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }

    public Object clone() {
        DefaultDocument document = (DefaultDocument) super.clone();
        document.rootElement = null;
        document.content = null;
        document.appendContent(this);

        return document;
    }

    public List processingInstructions() {
        List source = contentList();
        List answer = createResultList();
        int size = source.size();

        for (int i = 0; i < size; i++) {
            Object object = source.get(i);

            if (object instanceof ProcessingInstruction) {
                answer.add(object);
            }
        }

        return answer;
    }

    public List processingInstructions(String target) {
        List source = contentList();
        List answer = createResultList();
        int size = source.size();

        for (int i = 0; i < size; i++) {
            Object object = source.get(i);

            if (object instanceof ProcessingInstruction) {
                ProcessingInstruction pi = (ProcessingInstruction) object;

                if (target.equals(pi.getName())) {
                    answer.add(pi);
                }
            }
        }

        return answer;
    }

    public ProcessingInstruction processingInstruction(String target) {
        List source = contentList();
        int size = source.size();

        for (int i = 0; i < size; i++) {
            Object object = source.get(i);

            if (object instanceof ProcessingInstruction) {
                ProcessingInstruction pi = (ProcessingInstruction) object;

                if (target.equals(pi.getName())) {
                    return pi;
                }
            }
        }

        return null;
    }

    public boolean removeProcessingInstruction(String target) {
        List source = contentList();

        for (Iterator iter = source.iterator(); iter.hasNext();) {
            Object object = iter.next();

            if (object instanceof ProcessingInstruction) {
                ProcessingInstruction pi = (ProcessingInstruction) object;

                if (target.equals(pi.getName())) {
                    iter.remove();

                    return true;
                }
            }
        }

        return false;
    }

    public void setContent(List content) {
        rootElement = null;
        contentRemoved();

        if (content instanceof ContentListFacade) {
            content = ((ContentListFacade) content).getBackingList();
        }

        if (content == null) {
            this.content = null;
        } else {
            int size = content.size();
            List newContent = createContentList(size);

            for (int i = 0; i < size; i++) {
                Object object = content.get(i);

                if (object instanceof Node) {
                    Node node = (Node) object;
                    Document doc = node.getDocument();

                    if ((doc != null) && (doc != this)) {
                        node = (Node) node.clone();
                    }

                    if (node instanceof Element) {
                        if (rootElement == null) {
                            rootElement = (Element) node;
                        } else {
                            throw new IllegalAddException(
                                    "A document may only "
                                            + "contain one root " + "element: "
                                            + content);
                        }
                    }

                    newContent.add(node);
                    childAdded(node);
                }
            }

            this.content = newContent;
        }
    }

    public void clearContent() {
        contentRemoved();
        content = null;
        rootElement = null;
    }

    public void setDocumentFactory(DocumentFactory documentFactory) {
        this.documentFactory = documentFactory;
    }

    // Implementation methods
    // -------------------------------------------------------------------------
    protected List contentList() {
        if (content == null) {
            content = createContentList();

            if (rootElement != null) {
                content.add(rootElement);
            }
        }

        return content;
    }

    protected void addNode(Node node) {
        if (node != null) {
            Document document = node.getDocument();

            if ((document != null) && (document != this)) {
                // XXX: could clone here
                String message = "The Node already has an existing document: "
                        + document;
                throw new IllegalAddException(this, node, message);
            }

            contentList().add(node);
            childAdded(node);
        }
    }

    protected void addNode(int index, Node node) {
        if (node != null) {
            Document document = node.getDocument();

            if ((document != null) && (document != this)) {
                // XXX: could clone here
                String message = "The Node already has an existing document: "
                        + document;
                throw new IllegalAddException(this, node, message);
            }

            contentList().add(index, node);
            childAdded(node);
        }
    }

    protected boolean removeNode(Node node) {
        if (node == rootElement) {
            rootElement = null;
        }

        if (contentList().remove(node)) {
            childRemoved(node);

            return true;
        }

        return false;
    }

    protected void rootElementAdded(Element element) {
        this.rootElement = element;
        element.setDocument(this);
    }

    protected DocumentFactory getDocumentFactory() {
        return documentFactory;
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
