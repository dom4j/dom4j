/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.IllegalAddException;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.ProcessingInstruction;
import org.dom4j.QName;

/**
 * <p>
 * <code>DefaultElement</code> is the default DOM4J default implementation of
 * an XML element.
 * </p>
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision$
 */
public class DefaultElement extends AbstractElement {
    /** The <code>DocumentFactory</code> instance used by default */
    private static final transient DocumentFactory DOCUMENT_FACTORY = 
            DocumentFactory.getInstance();

    /** The <code>QName</code> for this element */
    private QName qname;

    /**
     * Stores the parent branch of this node which is either a Document if this
     * element is the root element in a document, or another Element if it is a
     * child of the root document, or null if it has not been added to a
     * document yet.
     */
    private Branch parentBranch;

    /**
     * Stores null for no content, a Node for a single content node or a List
     * for multiple content nodes. The List will be lazily constructed when
     * required.
     */
    private Object content;

    /** Lazily constructes list of attributes */
    private Object attributes;

    public DefaultElement(String name) {
        this.qname = DOCUMENT_FACTORY.createQName(name);
    }

    public DefaultElement(QName qname) {
        this.qname = qname;
    }

    public DefaultElement(QName qname, int attributeCount) {
        this.qname = qname;

        if (attributeCount > 1) {
            this.attributes = new ArrayList(attributeCount);
        }
    }

    public DefaultElement(String name, Namespace namespace) {
        this.qname = DOCUMENT_FACTORY.createQName(name, namespace);
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

    public String getText() {
        final Object contentShadow = content;

        if (contentShadow instanceof List) {
            return super.getText();
        } else {
            if (contentShadow != null) {
                return getContentAsText(contentShadow);
            } else {
                return "";
            }
        }
    }

    public String getStringValue() {
        final Object contentShadow = content;

        if (contentShadow instanceof List) {
            List list = (List) contentShadow;

            int size = list.size();

            if (size > 0) {
                if (size == 1) {
                    // optimised to avoid StringBuffer creation
                    return getContentAsStringValue(list.get(0));
                } else {
                    StringBuffer buffer = new StringBuffer();

                    for (int i = 0; i < size; i++) {
                        Object node = list.get(i);

                        String string = getContentAsStringValue(node);

                        if (string.length() > 0) {
                            if (USE_STRINGVALUE_SEPARATOR) {
                                if (buffer.length() > 0) {
                                    buffer.append(' ');
                                }
                            }

                            buffer.append(string);
                        }
                    }

                    return buffer.toString();
                }
            }
        } else {
            if (contentShadow != null) {
                return getContentAsStringValue(contentShadow);
            }
        }

        return "";
    }

    public Object clone() {
        DefaultElement answer = (DefaultElement) super.clone();

        if (answer != this) {
            answer.content = null;

            answer.attributes = null;

            answer.appendAttributes(this);

            answer.appendContent(this);
        }

        return answer;
    }

    public Namespace getNamespaceForPrefix(String prefix) {
        if (prefix == null) {
            prefix = "";
        }

        if (prefix.equals(getNamespacePrefix())) {
            return getNamespace();
        } else if (prefix.equals("xml")) {
            return Namespace.XML_NAMESPACE;
        } else {
            final Object contentShadow = content;

            if (contentShadow instanceof List) {
                List list = (List) contentShadow;

                int size = list.size();

                for (int i = 0; i < size; i++) {
                    Object object = list.get(i);

                    if (object instanceof Namespace) {
                        Namespace namespace = (Namespace) object;

                        if (prefix.equals(namespace.getPrefix())) {
                            return namespace;
                        }
                    }
                }
            } else if (contentShadow instanceof Namespace) {
                Namespace namespace = (Namespace) contentShadow;

                if (prefix.equals(namespace.getPrefix())) {
                    return namespace;
                }
            }
        }

        Element parent = getParent();

        if (parent != null) {
            Namespace answer = parent.getNamespaceForPrefix(prefix);

            if (answer != null) {
                return answer;
            }
        }

        if ((prefix == null) || (prefix.length() <= 0)) {
            return Namespace.NO_NAMESPACE;
        }

        return null;
    }

    public Namespace getNamespaceForURI(String uri) {
        if ((uri == null) || (uri.length() <= 0)) {
            return Namespace.NO_NAMESPACE;
        } else if (uri.equals(getNamespaceURI())) {
            return getNamespace();
        } else {
            final Object contentShadow = content;

            if (contentShadow instanceof List) {
                List list = (List) contentShadow;

                int size = list.size();

                for (int i = 0; i < size; i++) {
                    Object object = list.get(i);

                    if (object instanceof Namespace) {
                        Namespace namespace = (Namespace) object;

                        if (uri.equals(namespace.getURI())) {
                            return namespace;
                        }
                    }
                }
            } else if (contentShadow instanceof Namespace) {
                Namespace namespace = (Namespace) contentShadow;

                if (uri.equals(namespace.getURI())) {
                    return namespace;
                }
            }

            Element parent = getParent();

            if (parent != null) {
                return parent.getNamespaceForURI(uri);
            }

            return null;
        }
    }

    public List declaredNamespaces() {
        BackedList answer = createResultList();

        // if (getNamespaceURI().length() > 0) {
        //
        // answer.addLocal(getNamespace());
        //
        // }
        final Object contentShadow = content;

        if (contentShadow instanceof List) {
            List list = (List) contentShadow;

            int size = list.size();

            for (int i = 0; i < size; i++) {
                Object object = list.get(i);

                if (object instanceof Namespace) {
                    answer.addLocal(object);
                }
            }
        } else {
            if (contentShadow instanceof Namespace) {
                answer.addLocal(contentShadow);
            }
        }

        return answer;
    }

    public List additionalNamespaces() {
        final Object contentShadow = content;

        if (contentShadow instanceof List) {
            List list = (List) contentShadow;

            int size = list.size();

            BackedList answer = createResultList();

            for (int i = 0; i < size; i++) {
                Object object = list.get(i);

                if (object instanceof Namespace) {
                    Namespace namespace = (Namespace) object;

                    if (!namespace.equals(getNamespace())) {
                        answer.addLocal(namespace);
                    }
                }
            }

            return answer;
        } else {
            if (contentShadow instanceof Namespace) {
                Namespace namespace = (Namespace) contentShadow;

                if (namespace.equals(getNamespace())) {
                    return createEmptyList();
                }

                return createSingleResultList(namespace);
            } else {
                return createEmptyList();
            }
        }
    }

    public List additionalNamespaces(String defaultNamespaceURI) {
        final Object contentShadow = content;

        if (contentShadow instanceof List) {
            List list = (List) contentShadow;

            BackedList answer = createResultList();

            int size = list.size();

            for (int i = 0; i < size; i++) {
                Object object = list.get(i);

                if (object instanceof Namespace) {
                    Namespace namespace = (Namespace) object;

                    if (!defaultNamespaceURI.equals(namespace.getURI())) {
                        answer.addLocal(namespace);
                    }
                }
            }

            return answer;
        } else {
            if (contentShadow instanceof Namespace) {
                Namespace namespace = (Namespace) contentShadow;

                if (!defaultNamespaceURI.equals(namespace.getURI())) {
                    return createSingleResultList(namespace);
                }
            }
        }

        return createEmptyList();
    }

    // Processing instruction API
    public List processingInstructions() {
        final Object contentShadow = content;

        if (contentShadow instanceof List) {
            List list = (List) contentShadow;

            BackedList answer = createResultList();

            int size = list.size();

            for (int i = 0; i < size; i++) {
                Object object = list.get(i);

                if (object instanceof ProcessingInstruction) {
                    answer.addLocal(object);
                }
            }

            return answer;
        } else {
            if (contentShadow instanceof ProcessingInstruction) {
                return createSingleResultList(contentShadow);
            }

            return createEmptyList();
        }
    }

    public List processingInstructions(String target) {
        final Object shadow = content;

        if (shadow instanceof List) {
            List list = (List) shadow;

            BackedList answer = createResultList();

            int size = list.size();

            for (int i = 0; i < size; i++) {
                Object object = list.get(i);

                if (object instanceof ProcessingInstruction) {
                    ProcessingInstruction pi = (ProcessingInstruction) object;

                    if (target.equals(pi.getName())) {
                        answer.addLocal(pi);
                    }
                }
            }

            return answer;
        } else {
            if (shadow instanceof ProcessingInstruction) {
                ProcessingInstruction pi = (ProcessingInstruction) shadow;

                if (target.equals(pi.getName())) {
                    return createSingleResultList(pi);
                }
            }

            return createEmptyList();
        }
    }

    public ProcessingInstruction processingInstruction(String target) {
        final Object shadow = content;

        if (shadow instanceof List) {
            List list = (List) shadow;

            int size = list.size();

            for (int i = 0; i < size; i++) {
                Object object = list.get(i);

                if (object instanceof ProcessingInstruction) {
                    ProcessingInstruction pi = (ProcessingInstruction) object;

                    if (target.equals(pi.getName())) {
                        return pi;
                    }
                }
            }
        } else {
            if (shadow instanceof ProcessingInstruction) {
                ProcessingInstruction pi = (ProcessingInstruction) shadow;

                if (target.equals(pi.getName())) {
                    return pi;
                }
            }
        }

        return null;
    }

    public boolean removeProcessingInstruction(String target) {
        final Object shadow = content;

        if (shadow instanceof List) {
            List list = (List) shadow;

            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Object object = iter.next();

                if (object instanceof ProcessingInstruction) {
                    ProcessingInstruction pi = (ProcessingInstruction) object;

                    if (target.equals(pi.getName())) {
                        iter.remove();

                        return true;
                    }
                }
            }
        } else {
            if (shadow instanceof ProcessingInstruction) {
                ProcessingInstruction pi = (ProcessingInstruction) shadow;

                if (target.equals(pi.getName())) {
                    this.content = null;

                    return true;
                }
            }
        }

        return false;
    }

    public Element element(String name) {
        final Object contentShadow = content;

        if (contentShadow instanceof List) {
            List list = (List) contentShadow;

            int size = list.size();

            for (int i = 0; i < size; i++) {
                Object object = list.get(i);

                if (object instanceof Element) {
                    Element element = (Element) object;

                    if (name.equals(element.getName())) {
                        return element;
                    }
                }
            }
        } else {
            if (contentShadow instanceof Element) {
                Element element = (Element) contentShadow;

                if (name.equals(element.getName())) {
                    return element;
                }
            }
        }

        return null;
    }

    public Element element(QName qName) {
        final Object contentShadow = content;

        if (contentShadow instanceof List) {
            List list = (List) contentShadow;

            int size = list.size();

            for (int i = 0; i < size; i++) {
                Object object = list.get(i);

                if (object instanceof Element) {
                    Element element = (Element) object;

                    if (qName.equals(element.getQName())) {
                        return element;
                    }
                }
            }
        } else {
            if (contentShadow instanceof Element) {
                Element element = (Element) contentShadow;

                if (qName.equals(element.getQName())) {
                    return element;
                }
            }
        }

        return null;
    }

    public Element element(String name, Namespace namespace) {
        return element(getDocumentFactory().createQName(name, namespace));
    }

    public void setContent(List content) {
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
                    Element parent = node.getParent();

                    if ((parent != null) && (parent != this)) {
                        node = (Node) node.clone();
                    }

                    newContent.add(node);
                    childAdded(node);
                } else if (object != null) {
                    String text = object.toString();
                    Node node = getDocumentFactory().createText(text);
                    newContent.add(node);
                    childAdded(node);
                }
            }

            this.content = newContent;
        }
    }

    public void clearContent() {
        if (content != null) {
            contentRemoved();

            content = null;
        }
    }

    public Node node(int index) {
        if (index >= 0) {
            final Object contentShadow = content;
            Object node;

            if (contentShadow instanceof List) {
                List list = (List) contentShadow;

                if (index >= list.size()) {
                    return null;
                }

                node = list.get(index);
            } else {
                node = (index == 0) ? contentShadow : null;
            }

            if (node != null) {
                if (node instanceof Node) {
                    return (Node) node;
                } else {
                    return new DefaultText(node.toString());
                }
            }
        }

        return null;
    }

    public int indexOf(Node node) {
        final Object contentShadow = content;

        if (contentShadow instanceof List) {
            List list = (List) contentShadow;

            return list.indexOf(node);
        } else {
            if ((contentShadow != null) && contentShadow.equals(node)) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    public int nodeCount() {
        final Object contentShadow = content;

        if (contentShadow instanceof List) {
            List list = (List) contentShadow;

            return list.size();
        } else {
            return (contentShadow != null) ? 1 : 0;
        }
    }

    public Iterator nodeIterator() {
        final Object contentShadow = content;

        if (contentShadow instanceof List) {
            List list = (List) contentShadow;

            return list.iterator();
        } else {
            if (contentShadow != null) {
                return createSingleIterator(contentShadow);
            } else {
                return EMPTY_ITERATOR;
            }
        }
    }

    public List attributes() {
        return new ContentListFacade(this, attributeList());
    }

    public void setAttributes(List attributes) {
        if (attributes instanceof ContentListFacade) {
            attributes = ((ContentListFacade) attributes).getBackingList();
        }

        this.attributes = attributes;
    }

    public Iterator attributeIterator() {
        final Object attributesShadow = this.attributes;

        if (attributesShadow instanceof List) {
            List list = (List) attributesShadow;

            return list.iterator();
        } else if (attributesShadow != null) {
            return createSingleIterator(attributesShadow);
        } else {
            return EMPTY_ITERATOR;
        }
    }

    public Attribute attribute(int index) {
        final Object attributesShadow = this.attributes;

        if (attributesShadow instanceof List) {
            List list = (List) attributesShadow;

            return (Attribute) list.get(index);
        } else if ((attributesShadow != null) && (index == 0)) {
            return (Attribute) attributesShadow;
        } else {
            return null;
        }
    }

    public int attributeCount() {
        final Object attributesShadow = this.attributes;

        if (attributesShadow instanceof List) {
            List list = (List) attributesShadow;

            return list.size();
        } else {
            return (attributesShadow != null) ? 1 : 0;
        }
    }

    public Attribute attribute(String name) {
        final Object attributesShadow = this.attributes;

        if (attributesShadow instanceof List) {
            List list = (List) attributesShadow;

            int size = list.size();

            for (int i = 0; i < size; i++) {
                Attribute attribute = (Attribute) list.get(i);

                if (name.equals(attribute.getName())) {
                    return attribute;
                }
            }
        } else if (attributesShadow != null) {
            Attribute attribute = (Attribute) attributesShadow;

            if (name.equals(attribute.getName())) {
                return attribute;
            }
        }

        return null;
    }

    public Attribute attribute(QName qName) {
        final Object attributesShadow = this.attributes;

        if (attributesShadow instanceof List) {
            List list = (List) attributesShadow;

            int size = list.size();

            for (int i = 0; i < size; i++) {
                Attribute attribute = (Attribute) list.get(i);

                if (qName.equals(attribute.getQName())) {
                    return attribute;
                }
            }
        } else if (attributesShadow != null) {
            Attribute attribute = (Attribute) attributesShadow;

            if (qName.equals(attribute.getQName())) {
                return attribute;
            }
        }

        return null;
    }

    public Attribute attribute(String name, Namespace namespace) {
        return attribute(getDocumentFactory().createQName(name, namespace));
    }

    public void add(Attribute attribute) {
        if (attribute.getParent() != null) {
            String message = "The Attribute already has an existing parent \""
                    + attribute.getParent().getQualifiedName() + "\"";

            throw new IllegalAddException(this, attribute, message);
        }

        if (attribute.getValue() == null) {
            // try remove a previous attribute with the same
            // name since adding an attribute with a null value
            // is equivalent to removing it.
            Attribute oldAttribute = attribute(attribute.getQName());

            if (oldAttribute != null) {
                remove(oldAttribute);
            }
        } else {
            if (attributes == null) {
                attributes = attribute;
            } else {
                attributeList().add(attribute);
            }

            childAdded(attribute);
        }
    }

    public boolean remove(Attribute attribute) {
        boolean answer = false;
        final Object attributesShadow = this.attributes;

        if (attributesShadow instanceof List) {
            List list = (List) attributesShadow;

            answer = list.remove(attribute);

            if (!answer) {
                // we may have a copy of the attribute
                Attribute copy = attribute(attribute.getQName());

                if (copy != null) {
                    list.remove(copy);

                    answer = true;
                }
            }
        } else if (attributesShadow != null) {
            if (attribute.equals(attributesShadow)) {
                this.attributes = null;

                answer = true;
            } else {
                // we may have a copy of the attribute
                Attribute other = (Attribute) attributesShadow;

                if (attribute.getQName().equals(other.getQName())) {
                    attributes = null;

                    answer = true;
                }
            }
        }

        if (answer) {
            childRemoved(attribute);
        }

        return answer;
    }

    // Implementation methods
    // -------------------------------------------------------------------------
    protected void addNewNode(Node node) {
        final Object contentShadow = content;

        if (contentShadow == null) {
            this.content = node;
        } else {
            if (contentShadow instanceof List) {
                List list = (List) contentShadow;

                list.add(node);
            } else {
                List list = createContentList();

                list.add(contentShadow);

                list.add(node);

                this.content = list;
            }
        }

        childAdded(node);
    }

    protected boolean removeNode(Node node) {
        boolean answer = false;
        final Object contentShadow = content;

        if (contentShadow != null) {
            if (contentShadow == node) {
                this.content = null;

                answer = true;
            } else if (contentShadow instanceof List) {
                List list = (List) contentShadow;

                answer = list.remove(node);
            }
        }

        if (answer) {
            childRemoved(node);
        }

        return answer;
    }

    protected List contentList() {
        final Object contentShadow = content;

        if (contentShadow instanceof List) {
            return (List) contentShadow;
        } else {
            List list = createContentList();

            if (contentShadow != null) {
                list.add(contentShadow);
            }

            this.content = list;

            return list;
        }
    }

    protected List attributeList() {
        final Object attributesShadow = this.attributes;

        if (attributesShadow instanceof List) {
            return (List) attributesShadow;
        } else if (attributesShadow != null) {
            List list = createAttributeList();

            list.add(attributesShadow);

            this.attributes = list;

            return list;
        } else {
            List list = createAttributeList();

            this.attributes = list;

            return list;
        }
    }

    protected List attributeList(int size) {
        final Object attributesShadow = this.attributes;

        if (attributesShadow instanceof List) {
            return (List) attributesShadow;
        } else if (attributesShadow != null) {
            List list = createAttributeList(size);

            list.add(attributesShadow);

            this.attributes = list;

            return list;
        } else {
            List list = createAttributeList(size);

            this.attributes = list;

            return list;
        }
    }

    protected void setAttributeList(List attributeList) {
        this.attributes = attributeList;
    }

    protected DocumentFactory getDocumentFactory() {
        DocumentFactory factory = qname.getDocumentFactory();

        return (factory != null) ? factory : DOCUMENT_FACTORY;
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
