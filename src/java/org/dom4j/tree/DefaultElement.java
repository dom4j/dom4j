/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
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

/** <p><code>DefaultElement</code> is the default DOM4J default implementation
  * of an XML element.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */

public class DefaultElement extends AbstractElement {

    /** The <code>DocumentFactory</code> instance used by default */

    private static transient final DocumentFactory DOCUMENT_FACTORY =
        DocumentFactory.getInstance();

    /** The <code>QName</code> for this element */

    private QName qname;

    /** Stores the parent branch of this node which is either a Document 
      * if this element is the root element in a document, or another Element 
      * if it is a child of the root document, or null if it has not been added
      * to a document yet. 
       */

    private Branch parentBranch;

    /** Stores null for no content, a Node for a single content node
      * or a List for multiple content nodes. 
      * The List will be lazily constructed when required. */

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

        return (parentBranch instanceof Element) ? (Element) parentBranch : null;

    }

    public void setParent(Element parent) {

        if (parentBranch instanceof Element || parent != null) {

            parentBranch = parent;

        }

    }

    public Document getDocument() {

        if (parentBranch instanceof Document) {

            return (Document) parentBranch;

        }

        else if (parentBranch instanceof Element) {

            Element parent = (Element) parentBranch;

            return parent.getDocument();

        }

        return null;

    }

    public void setDocument(Document document) {

        if (parentBranch instanceof Document || document != null) {

            parentBranch = document;

        }

    }

    public boolean supportsParent() {

        return true;

    }

    public QName getQName() {

        return qname;

    }

    public void setQName(QName qname) {

        this.qname = qname;

    }

    public String getText() {

        if (content instanceof List) {

            return super.getText();

        }

        else {

            if (content != null) {

                return getContentAsText(content);

            }

            else {

                return "";

            }

        }

    }

    public String getStringValue() {

        if (content instanceof List) {

            List list = (List) content;

            int size = list.size();

            if (size > 0) {

                if (size == 1) {

                    // optimised to avoid StringBuffer creation

                    return getContentAsStringValue(list.get(0));

                }

                else {

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

        }

        else {

            if (content != null) {

                return getContentAsStringValue(content);

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

        }

        else if (prefix.equals("xml")) {

            return Namespace.XML_NAMESPACE;

        }

        else {

            if (content instanceof List) {

                List list = (List) content;

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

            }

            else if (content instanceof Namespace) {

                Namespace namespace = (Namespace) content;

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

        if (prefix == null || prefix.length() <= 0) {

            return Namespace.NO_NAMESPACE;

        }

        return null;

    }

    public Namespace getNamespaceForURI(String uri) {

        if (uri == null || uri.length() <= 0) {

            return Namespace.NO_NAMESPACE;

        }

        else if (uri.equals(getNamespaceURI())) {

            return getNamespace();

        }

        else {

            if (content instanceof List) {

                List list = (List) content;

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

            }

            else if (content instanceof Namespace) {

                Namespace namespace = (Namespace) content;

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

        if (getNamespaceURI().length() > 0) {

            answer.addLocal(getNamespace());

        }

        if (content instanceof List) {

            List list = (List) content;

            int size = list.size();

            for (int i = 0; i < size; i++) {

                Object object = list.get(i);

                if (object instanceof Namespace) {

                    answer.addLocal(object);

                }

            }

        }

        else {

            if (content instanceof Namespace) {

                answer.addLocal(content);

            }

        }

        return answer;

    }

    public List additionalNamespaces() {

        if (content instanceof List) {

            List list = (List) content;

            int size = list.size();

            BackedList answer = createResultList();

            for (int i = 0; i < size; i++) {

                Object object = list.get(i);

                if (object instanceof Namespace) {

                    Namespace namespace = (Namespace) object;

                    answer.addLocal(namespace);

                }

            }

            return answer;

        }

        else {

            if (content instanceof Namespace) {

                Namespace namespace = (Namespace) content;

                return createSingleResultList(namespace);

            }

            else {

                return createEmptyList();

            }

        }

    }

    public List additionalNamespaces(String defaultNamespaceURI) {

        if (content instanceof List) {

            List list = (List) content;

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

        }

        else {

            if (content instanceof Namespace) {

                Namespace namespace = (Namespace) content;

                if (!defaultNamespaceURI.equals(namespace.getURI())) {

                    return createSingleResultList(namespace);

                }

            }

        }

        return createEmptyList();

    }

    // Processing instruction API

    public List processingInstructions() {

        if (content instanceof List) {

            List list = (List) content;

            BackedList answer = createResultList();

            int size = list.size();

            for (int i = 0; i < size; i++) {

                Object object = list.get(i);

                if (object instanceof ProcessingInstruction) {

                    answer.addLocal(object);

                }

            }

            return answer;

        }

        else {

            if (content instanceof ProcessingInstruction) {

                return createSingleResultList(content);

            }

            return createEmptyList();

        }

    }

    public List processingInstructions(String target) {

        if (content instanceof List) {

            List list = (List) content;

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

        }

        else {

            if (content instanceof ProcessingInstruction) {

                ProcessingInstruction pi = (ProcessingInstruction) content;

                if (target.equals(pi.getName())) {

                    return createSingleResultList(pi);

                }

            }

            return createEmptyList();

        }

    }

    public ProcessingInstruction processingInstruction(String target) {

        if (content instanceof List) {

            List list = (List) content;

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

        }

        else {

            if (content instanceof ProcessingInstruction) {

                ProcessingInstruction pi = (ProcessingInstruction) content;

                if (target.equals(pi.getName())) {

                    return pi;

                }

            }

        }

        return null;

    }

    public boolean removeProcessingInstruction(String target) {

        if (content instanceof List) {

            List list = (List) content;

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

        }

        else {

            if (content instanceof ProcessingInstruction) {

                ProcessingInstruction pi = (ProcessingInstruction) content;

                if (target.equals(pi.getName())) {

                    content = null;

                    return true;

                }

            }

        }

        return false;

    }

    public Element element(String name) {

        if (content instanceof List) {

            List list = (List) content;

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

        }

        else {

            if (content instanceof Element) {

                Element element = (Element) content;

                if (name.equals(element.getName())) {

                    return element;

                }

            }

        }

        return null;

    }

    public Element element(QName qName) {

        if (content instanceof List) {

            List list = (List) content;

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

        }

        else {

            if (content instanceof Element) {

                Element element = (Element) content;

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

    public List elements() {

        if (content instanceof List) {

            List list = (List) content;

            BackedList answer = createResultList();

            int size = list.size();

            for (int i = 0; i < size; i++) {

                Object object = list.get(i);

                if (object instanceof Element) {

                    answer.addLocal(object);

                }

            }

            return answer;

        }

        else {

            if (content instanceof Element) {

                Element element = (Element) content;

                return createSingleResultList(element);

            }

            return createEmptyList();

        }

    }

    public List elements(String name) {

        if (content instanceof List) {

            List list = (List) content;

            BackedList answer = createResultList();

            int size = list.size();

            for (int i = 0; i < size; i++) {

                Object object = list.get(i);

                if (object instanceof Element) {

                    Element element = (Element) object;

                    if (name.equals(element.getName())) {

                        answer.addLocal(element);

                    }

                }

            }

            return answer;

        }

        else {

            if (content instanceof Element) {

                Element element = (Element) content;

                if (name.equals(element.getName())) {

                    return createSingleResultList(element);

                }

            }

            return createEmptyList();

        }

    }

    public List elements(QName qName) {

        if (content instanceof List) {

            List list = (List) content;

            BackedList answer = createResultList();

            int size = list.size();

            for (int i = 0; i < size; i++) {

                Object object = list.get(i);

                if (object instanceof Element) {

                    Element element = (Element) object;

                    if (qName.equals(element.getQName())) {

                        answer.addLocal(element);

                    }

                }

            }

            return answer;

        }

        else {

            if (content instanceof Element) {

                Element element = (Element) content;

                if (qName.equals(element.getQName())) {

                    return createSingleResultList(element);

                }

            }

            return createEmptyList();

        }

    }

    public List elements(String name, Namespace namespace) {

        return elements(getDocumentFactory().createQName(name, namespace));

    }

    public Iterator elementIterator() {

        if (content instanceof List) {

            List list = (List) content;

            return new ElementIterator(list.iterator());

        }

        else {

            if (content instanceof Element) {

                Element element = (Element) content;

                return createSingleIterator(element);

            }

            return EMPTY_ITERATOR;

        }

    }

    public Iterator elementIterator(String name) {

        if (content instanceof List) {

            List list = (List) content;

            return new ElementNameIterator(list.iterator(), name);

        }

        else {

            if (content instanceof Element) {

                Element element = (Element) content;

                if (name.equals(element.getName())) {

                    return createSingleIterator(element);

                }

            }

            return EMPTY_ITERATOR;

        }

    }

    public Iterator elementIterator(QName qName) {

        if (content instanceof List) {

            List list = (List) content;

            return new ElementQNameIterator(list.iterator(), qName);

        }

        else {

            if (content instanceof Element) {

                Element element = (Element) content;

                if (qName.equals(element.getQName())) {

                    return createSingleIterator(element);

                }

            }

            return EMPTY_ITERATOR;

        }

    }

    public Iterator elementIterator(String name, Namespace namespace) {

        return elementIterator(getDocumentFactory().createQName(name, namespace));

    }

    public void setContent(List content) {

        if (content instanceof ContentListFacade) {

            content = ((ContentListFacade) content).getBackingList();

        }

        if (content == null) {

            this.content = null;

        }

        else {

            int size = content.size();

            List newContent = createContentList(size);

            for (int i = 0; i < size; i++) {

                Object object = content.get(i);

                if (object instanceof Node) {

                    Node node = (Node) object;

                    Element parent = node.getParent();

                    if (parent != null && parent != this) {

                        node = (Node) node.clone();

                    }

                    newContent.add(node);

                    childAdded(node);

                }

                else if (object != null) {

                    String text = object.toString();

                    Node node = getDocumentFactory().createText(text);

                    newContent.add(node);

                    childAdded(node);

                }

            }

            contentRemoved();

            this.content = newContent;

        }

    }

    public void clearContent() {

        if (content != null) {

            contentRemoved();

        }

        content = null;

    }

    public Node node(int index) {

        if (index >= 0) {

            Object node = content;

            if (content instanceof List) {

                List list = (List) content;

                if (index >= list.size()) {

                    return null;

                }

                node = list.get(index);

            }

            if (node != null) {

                if (node instanceof Node) {

                    return (Node) node;

                }

                else {

                    return new DefaultText(node.toString());

                }

            }

        }

        return null;

    }

    public int indexOf(Node node) {

        if (content instanceof List) {

            List list = (List) content;

            return list.indexOf(node);

        }

        else {

            return (content != null && content.equals(node)) ? 0 : -1;

        }

    }

    public int nodeCount() {

        if (content instanceof List) {

            List list = (List) content;

            return list.size();

        }

        else {

            return (content != null) ? 1 : 0;

        }

    }

    public Iterator nodeIterator() {

        if (content instanceof List) {

            List list = (List) content;

            return list.iterator();

        }

        else {

            if (content != null) {

                return createSingleIterator(content);

            }

            else {

                return EMPTY_ITERATOR;

            }

        }

    }

    public List attributes() {

        return new ContentListFacade(this, attributeList());

    }

    public void setAttributes(List attributes) {

        this.attributes = attributes;

        if (attributes instanceof ContentListFacade) {

            this.attributes = ((ContentListFacade) attributes).getBackingList();

        }

    }

    public Iterator attributeIterator() {

        if (attributes instanceof List) {

            List list = (List) attributes;

            return list.iterator();

        }

        else if (attributes != null) {

            return createSingleIterator(attributes);

        }

        else {

            return EMPTY_ITERATOR;

        }

    }

    public Attribute attribute(int index) {

        if (attributes instanceof List) {

            List list = (List) attributes;

            return (Attribute) list.get(index);

        }

        else if (attributes != null && index == 0) {

            return (Attribute) attributes;

        }

        else {

            return null;

        }

    }

    public int attributeCount() {

        if (attributes instanceof List) {

            List list = (List) attributes;

            return list.size();

        }

        else {

            return (attributes != null) ? 1 : 0;

        }

    }

    public Attribute attribute(String name) {

        if (attributes instanceof List) {

            List list = (List) attributes;

            int size = list.size();

            for (int i = 0; i < size; i++) {

                Attribute attribute = (Attribute) list.get(i);

                if (name.equals(attribute.getName())) {

                    return attribute;

                }

            }

        }

        else if (attributes != null) {

            Attribute attribute = (Attribute) attributes;

            if (name.equals(attribute.getName())) {

                return attribute;

            }

        }

        return null;

    }

    public Attribute attribute(QName qName) {

        if (attributes instanceof List) {

            List list = (List) attributes;

            int size = list.size();

            for (int i = 0; i < size; i++) {

                Attribute attribute = (Attribute) list.get(i);

                if (qName.equals(attribute.getQName())) {

                    return attribute;

                }

            }

        }

        else if (attributes != null) {

            Attribute attribute = (Attribute) attributes;

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

            String message =
                "The Attribute already has an existing parent \""
                    + attribute.getParent().getQualifiedName()
                    + "\"";

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

        }

        else {

            if (attributes == null) {

                attributes = attribute;

            }

            else {

                attributeList().add(attribute);

            }

            childAdded(attribute);

        }

    }

    public boolean remove(Attribute attribute) {

        boolean answer = false;

        if (attributes instanceof List) {

            List list = (List) attributes;

            answer = list.remove(attribute);

            if (!answer) {

                // we may have a copy of the attribute

                Attribute copy = attribute(attribute.getQName());

                if (copy != null) {

                    list.remove(copy);

                    answer = true;

                }

            }

        }

        else if (attributes != null) {

            if (attribute.equals(attributes)) {

                attributes = null;

                answer = true;

            }

            else {

                // we may have a copy of the attribute

                Attribute other = (Attribute) attributes;

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

    //-------------------------------------------------------------------------    

    protected void addNewNode(Node node) {

        if (content == null) {

            content = node;

        }

        else {

            if (content instanceof List) {

                List list = (List) content;

                list.add(node);

            }

            else {

                List list = createContentList();

                list.add(content);

                list.add(node);

                content = list;

            }

        }

        childAdded(node);

    }

    protected boolean removeNode(Node node) {

        boolean answer = false;

        if (content != null) {

            if (content == node) {

                content = null;

                answer = true;

            }

            else if (content instanceof List) {

                List list = (List) content;

                answer = list.remove(node);

            }

        }

        if (answer) {

            childRemoved(node);

        }

        return answer;

    }

    protected List contentList() {

        if (content instanceof List) {

            return (List) content;

        }

        else {

            List list = createContentList();

            if (content != null) {

                list.add(content);

            }

            content = list;

            return list;

        }

    }

    protected List attributeList() {

        if (attributes instanceof List) {

            return (List) attributes;

        }

        else if (attributes != null) {

            List list = createAttributeList();

            list.add(attributes);

            attributes = list;

            return list;

        }

        else {

            List list = createAttributeList();

            attributes = list;

            return list;

        }

    }

    protected List attributeList(int size) {

        if (attributes instanceof List) {

            return (List) attributes;

        }

        else if (attributes != null) {

            List list = createAttributeList(size);

            list.add(attributes);

            attributes = list;

            return list;

        }

        else {

            List list = createAttributeList(size);

            attributes = list;

            return list;

        }

    }

    protected void setAttributeList(List attributes) {

        this.attributes = attributes;

    }

    protected DocumentFactory getDocumentFactory() {

        DocumentFactory factory = qname.getDocumentFactory();

        return (factory != null) ? factory : DOCUMENT_FACTORY;

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
