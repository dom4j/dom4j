/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.tree;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.CDATA;
import org.dom4j.CharacterData;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.IllegalAddException;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.ProcessingInstruction;
import org.dom4j.QName;
import org.dom4j.Text;
import org.dom4j.Visitor;
import org.dom4j.io.XMLWriter;
import org.xml.sax.Attributes;

/** <p><code>AbstractElement</code> is an abstract base class for 
  * tree implementors to use for implementation inheritence.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */

public abstract class AbstractElement
    extends AbstractBranch
    implements Element {

    /** The <code>DocumentFactory</code> instance used by default */

    private static final DocumentFactory DOCUMENT_FACTORY =
        DocumentFactory.getInstance();

    protected static final List EMPTY_LIST = Collections.EMPTY_LIST;

    protected static final Iterator EMPTY_ITERATOR = EMPTY_LIST.iterator();

    protected static final boolean VERBOSE_TOSTRING = false;

    protected static final boolean USE_STRINGVALUE_SEPARATOR = false;

    public AbstractElement() {

    }

    public short getNodeType() {

        return ELEMENT_NODE;

    }

    public boolean isRootElement() {

        Document document = getDocument();

        if (document != null) {

            Element root = document.getRootElement();

            if (root == this) {

                return true;

            }

        }

        return false;

    }

    public void setName(String name) {

        setQName(getDocumentFactory().createQName(name));

    }

    public void setNamespace(Namespace namespace) {

        setQName(getDocumentFactory().createQName(getName(), namespace));

    }

    /** Returns the XPath expression to match this Elements name
     * which is getQualifiedName() if there is a namespace prefix defined or
     * if no namespace is present then it is getName() or if a namespace is defined
     * with no prefix then the expression is *[name()='X'] where X = getName().    
     */

    public String getXPathNameStep() {

        String uri = getNamespaceURI();

        if (uri == null || uri.length() == 0) {

            return getName();

        }

        String prefix = getNamespacePrefix();

        if (prefix == null || prefix.length() == 0) {

            return "*[name()='" + getName() + "']";

        }

        return getQualifiedName();

    }

    public String getPath(Element context) {

        Element parent = getParent();

        if (parent == null) {

            return "/" + getXPathNameStep();

        }

        else if (parent == context) {

            return getXPathNameStep();

        }

        return parent.getPath(context) + "/" + getXPathNameStep();

    }

    public String getUniquePath(Element context) {

        Element parent = getParent();

        if (parent == null) {

            return "/" + getXPathNameStep();

        }

        StringBuffer buffer = new StringBuffer();

        if (parent != context) {

            buffer.append(parent.getUniquePath(context));

            buffer.append("/");

        }

        buffer.append(getXPathNameStep());

        List mySiblings = parent.elements(getQName());

        if (mySiblings.size() > 1) {

            int idx = mySiblings.indexOf(this);

            if (idx >= 0) {

                buffer.append("[");

                buffer.append(Integer.toString(++idx));

                buffer.append("]");

            }

        }

        return buffer.toString();

    }

    public String asXML() {

        try {

            StringWriter out = new StringWriter();

            XMLWriter writer = new XMLWriter(out, outputFormat);

            writer.write(this);

            return out.toString();

        }

        catch (IOException e) {

            throw new RuntimeException(
                "Wierd IOException while generating textual representation: " + e.getMessage());

        }

    }

    public void write(Writer out) throws IOException {

        XMLWriter writer = new XMLWriter(out, outputFormat);

        writer.write(this);

    }

    /** <p><code>accept</code> method is the <code>Visitor Pattern</code> method.
      * </p>
      *
      * @param visitor <code>Visitor</code> is the visitor.
      */

    public void accept(Visitor visitor) {

        visitor.visit(this);

        // visit attributes

        for (int i = 0, size = attributeCount(); i < size; i++) {

            Attribute attribute = attribute(i);

            visitor.visit(attribute);

        }

        // visit content

        for (int i = 0, size = nodeCount(); i < size; i++) {

            Node node = node(i);

            node.accept(visitor);

        }

    }

    public String toString() {

        String uri = getNamespaceURI();

        if (uri != null && uri.length() > 0) {

            if (VERBOSE_TOSTRING) {

                return super.toString()
                    + " [Element: <"
                    + getQualifiedName()
                    + " uri: "
                    + uri
                    + " attributes: "
                    + attributeList()
                    + " content: "
                    + contentList()
                    + " />]";

            }

            else {

                return super.toString()
                    + " [Element: <"
                    + getQualifiedName()
                    + " uri: "
                    + uri
                    + " attributes: "
                    + attributeList()
                    + "/>]";

            }

        }

        else {

            if (VERBOSE_TOSTRING) {

                return super.toString()
                    + " [Element: <"
                    + getQualifiedName()
                    + " attributes: "
                    + attributeList()
                    + " content: "
                    + contentList()
                    + " />]";

            }

            else {

                return super.toString()
                    + " [Element: <"
                    + getQualifiedName()
                    + " attributes: "
                    + attributeList()
                    + "/>]";

            }

        }

    }

    // QName methods

    //-------------------------------------------------------------------------    

    public Namespace getNamespace() {

        return getQName().getNamespace();

    }

    public String getName() {

        return getQName().getName();

    }

    public String getNamespacePrefix() {

        return getQName().getNamespacePrefix();

    }

    public String getNamespaceURI() {

        return getQName().getNamespaceURI();

    }

    public String getQualifiedName() {

        return getQName().getQualifiedName();

    }

    public Object getData() {

        return getText();

    }

    public void setData(Object data) {

        // ignore this method

    }

    // Node methods

    //-------------------------------------------------------------------------    

    public Node node(int index) {

        if (index >= 0) {

            List list = contentList();

            if (index >= list.size()) {

                return null;

            }

            Object node = list.get(index);

            if (node != null) {

                if (node instanceof Node) {

                    return (Node) node;

                }

                else {

                    return getDocumentFactory().createText(node.toString());

                }

            }

        }

        return null;

    }

    public int indexOf(Node node) {

        return contentList().indexOf(node);

    }

    public int nodeCount() {

        return contentList().size();

    }

    public Iterator nodeIterator() {

        return contentList().iterator();

    }

    // Element methods

    //-------------------------------------------------------------------------    

    public Element element(String name) {

        List list = contentList();

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

        return null;

    }

    public Element element(QName qName) {

        List list = contentList();

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

        return null;

    }

    public Element element(String name, Namespace namespace) {

        return element(getDocumentFactory().createQName(name, namespace));

    }

    public List elements() {

        List list = contentList();

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

    public List elements(String name) {

        List list = contentList();

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

    public List elements(QName qName) {

        List list = contentList();

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

    public List elements(String name, Namespace namespace) {

        return elements(getDocumentFactory().createQName(name, namespace));

    }

    public Iterator elementIterator() {

        List list = contentList();

        return new ElementIterator(list.iterator());

    }

    public Iterator elementIterator(String name) {

        List list = contentList();

        return new ElementNameIterator(list.iterator(), name);

    }

    public Iterator elementIterator(QName qName) {

        List list = contentList();

        return new ElementQNameIterator(list.iterator(), qName);

    }

    public Iterator elementIterator(String name, Namespace namespace) {

        return elementIterator(getDocumentFactory().createQName(name, namespace));

    }

    // Attribute methods

    //-------------------------------------------------------------------------    

    public List attributes() {

        return new ContentListFacade(this, attributeList());

    }

    public Iterator attributeIterator() {

        return attributeList().iterator();

    }

    public Attribute attribute(int index) {

        return (Attribute) attributeList().get(index);

    }

    public int attributeCount() {

        return attributeList().size();

    }

    public Attribute attribute(String name) {

        List list = attributeList();

        int size = list.size();

        for (int i = 0; i < size; i++) {

            Attribute attribute = (Attribute) list.get(i);

            if (name.equals(attribute.getName())) {

                return attribute;

            }

        }

        return null;

    }

    public Attribute attribute(QName qName) {

        List list = attributeList();

        int size = list.size();

        for (int i = 0; i < size; i++) {

            Attribute attribute = (Attribute) list.get(i);

            if (qName.equals(attribute.getQName())) {

                return attribute;

            }

        }

        return null;

    }

    public Attribute attribute(String name, Namespace namespace) {

        return attribute(getDocumentFactory().createQName(name, namespace));

    }

    /** This method provides a more optimal way of setting all the attributes
     * on an Element particularly for use in {@link org.dom4j.io.SAXReader}.
      */

    public void setAttributes(
        Attributes attributes,
        NamespaceStack namespaceStack,
        boolean noNamespaceAttributes) {

        // now lets add all attribute values

        int size = attributes.getLength();

        if (size > 0) {

            DocumentFactory factory = getDocumentFactory();

            if (size == 1) {

                // allow lazy construction of the List of Attributes

                String attributeQualifiedName = attributes.getQName(0);

                if (noNamespaceAttributes || !attributeQualifiedName.startsWith("xmlns")) {

                    String attributeURI = attributes.getURI(0);

                    String attributeLocalName = attributes.getLocalName(0);

                    String attributeValue = attributes.getValue(0);

                    QName attributeQName =
                        namespaceStack.getAttributeQName(
                            attributeURI,
                            attributeLocalName,
                            attributeQualifiedName);

                    add(factory.createAttribute(this, attributeQName, attributeValue));

                }

            }

            else {

                List list = attributeList(size);

                list.clear();

                for (int i = 0; i < size; i++) {

                    // optimised to avoid the call to attribute(QName) to 

                    // lookup an attribute for a given QName

                    String attributeQualifiedName = attributes.getQName(i);

                    if (noNamespaceAttributes || !attributeQualifiedName.startsWith("xmlns")) {

                        String attributeURI = attributes.getURI(i);

                        String attributeLocalName = attributes.getLocalName(i);

                        String attributeValue = attributes.getValue(i);

                        QName attributeQName =
                            namespaceStack.getAttributeQName(
                                attributeURI,
                                attributeLocalName,
                                attributeQualifiedName);

                        Attribute attribute =
                            factory.createAttribute(this, attributeQName, attributeValue);

                        list.add(attribute);

                        childAdded(attribute);

                    }

                }

            }

        }

    }

    public String attributeValue(String name) {

        Attribute attrib = attribute(name);

        if (attrib == null) {

            return null;

        }

        else {

            return attrib.getValue();

        }

    }

    public String attributeValue(QName qName) {

        Attribute attrib = attribute(qName);

        if (attrib == null) {

            return null;

        }

        else {

            return attrib.getValue();

        }

    }

    public String attributeValue(String name, String defaultValue) {

        String answer = attributeValue(name);

        return (answer != null) ? answer : defaultValue;

    }

    public String attributeValue(QName qName, String defaultValue) {

        String answer = attributeValue(qName);

        return (answer != null) ? answer : defaultValue;

    }

    public void setAttributeValue(String name, String value) {

        addAttribute(name, value);

    }

    public void setAttributeValue(QName qName, String value) {

        addAttribute(qName, value);

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

            attributeList().add(attribute);

            childAdded(attribute);

        }

    }

    public boolean remove(Attribute attribute) {

        List list = attributeList();

        boolean answer = list.remove(attribute);

        if (answer) {

            childRemoved(attribute);

        }

        else {

            // we may have a copy of the attribute

            Attribute copy = attribute(attribute.getQName());

            if (copy != null) {

                list.remove(copy);

                answer = true;

            }

        }

        return answer;

    }

    // Processing instruction API

    //-------------------------------------------------------------------------    

    public List processingInstructions() {

        List list = contentList();

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

    public List processingInstructions(String target) {

        List list = contentList();

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

    public ProcessingInstruction processingInstruction(String target) {

        List list = contentList();

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

        return null;

    }

    public boolean removeProcessingInstruction(String target) {

        List list = contentList();

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

        return false;

    }

    // Content Model methods

    //-------------------------------------------------------------------------    

    public Node getXPathResult(int index) {

        Node answer = node(index);

        if (answer != null && !answer.supportsParent()) {

            return answer.asXPathResult(this);

        }

        return answer;

    }

    public Element addAttribute(String name, String value) {

        // adding a null value is equivalent to removing the attribute

        Attribute attribute = attribute(name);

        if (value != null) {

            if (attribute == null) {

                add(getDocumentFactory().createAttribute(this, name, value));

            }

            else if (attribute.isReadOnly()) {

                remove(attribute);

                add(getDocumentFactory().createAttribute(this, name, value));

            }

            else {

                attribute.setValue(value);

            }

        }

        else if (attribute != null) {

            remove(attribute);

        }

        return this;

    }

    public Element addAttribute(QName qName, String value) {

        // adding a null value is equivalent to removing the attribute

        Attribute attribute = attribute(qName);

        if (value != null) {

            if (attribute == null) {

                add(getDocumentFactory().createAttribute(this, qName, value));

            }

            else if (attribute.isReadOnly()) {

                remove(attribute);

                add(getDocumentFactory().createAttribute(this, qName, value));

            }

            else {

                attribute.setValue(value);

            }

        }

        else if (attribute != null) {

            remove(attribute);

        }

        return this;

    }

    public Element addCDATA(String cdata) {

        CDATA node = getDocumentFactory().createCDATA(cdata);

        addNewNode(node);

        return this;

    }

    public Element addComment(String comment) {

        Comment node = getDocumentFactory().createComment(comment);

        addNewNode(node);

        return this;

    }

    public Element addElement(String name) {

        DocumentFactory factory = getDocumentFactory();

        int index = name.indexOf(":");

        String prefix = "";

        String localName = name;

        Namespace namespace = null;

        if (index > 0) {

            prefix = name.substring(0, index);

            localName = name.substring(index + 1);

            namespace = getNamespaceForPrefix(prefix);

            if (namespace == null) {

                throw new IllegalAddException(
                    "No such namespace prefix: "
                        + prefix
                        + " is in scope on: "
                        + this
                        + " so cannot add element: "
                        + name);

            }

        }

        else {

            namespace = getNamespaceForPrefix("");

        }

        Element node;

        if (namespace != null) {

            QName qname = factory.createQName(localName, namespace);

            node = factory.createElement(qname);

        }

        else {

            node = factory.createElement(name);

        }

        addNewNode(node);

        return node;

    }

    public Element addEntity(String name, String text) {

        Entity node = getDocumentFactory().createEntity(name, text);

        addNewNode(node);

        return this;

    }

    public Element addNamespace(String prefix, String uri) {

        Namespace node = getDocumentFactory().createNamespace(prefix, uri);

        addNewNode(node);

        return this;

    }

    public Element addProcessingInstruction(String target, String data) {

        ProcessingInstruction node =
            getDocumentFactory().createProcessingInstruction(target, data);

        addNewNode(node);

        return this;

    }

    public Element addProcessingInstruction(String target, Map data) {

        ProcessingInstruction node =
            getDocumentFactory().createProcessingInstruction(target, data);

        addNewNode(node);

        return this;

    }

    public Element addText(String text) {

        Text node = getDocumentFactory().createText(text);

        addNewNode(node);

        return this;

    }

    // polymorphic node methods    

    public void add(Node node) {

        switch (node.getNodeType()) {

            case ELEMENT_NODE :

                add((Element) node);

                break;

            case ATTRIBUTE_NODE :

                add((Attribute) node);

                break;

            case TEXT_NODE :

                add((Text) node);

                break;

            case CDATA_SECTION_NODE :

                add((CDATA) node);

                break;

            case ENTITY_REFERENCE_NODE :

                add((Entity) node);

                break;

            case PROCESSING_INSTRUCTION_NODE :

                add((ProcessingInstruction) node);

                break;

            case COMMENT_NODE :

                add((Comment) node);

                break;

                /*  XXXX: to do!              
                            case DOCUMENT_TYPE_NODE:
                                add((DocumentType) node);
                                break;
                */

            case NAMESPACE_NODE :

                add((Namespace) node);

                break;

            default :

                invalidNodeTypeAddException(node);

        }

    }

    public boolean remove(Node node) {

        switch (node.getNodeType()) {

            case ELEMENT_NODE :

                return remove((Element) node);

            case ATTRIBUTE_NODE :

                return remove((Attribute) node);

            case TEXT_NODE :

                return remove((Text) node);

            case CDATA_SECTION_NODE :

                return remove((CDATA) node);

            case ENTITY_REFERENCE_NODE :

                return remove((Entity) node);

            case PROCESSING_INSTRUCTION_NODE :

                return remove((ProcessingInstruction) node);

            case COMMENT_NODE :

                return remove((Comment) node);

                /*                
                            case DOCUMENT_TYPE_NODE:
                                return remove((DocumentType) node);
                */

            case NAMESPACE_NODE :

                return remove((Namespace) node);

            default :

                return false;

        }

    }

    // typesafe versions using node classes

    public void add(CDATA cdata) {

        addNode(cdata);

    }

    public void add(Comment comment) {

        addNode(comment);

    }

    public void add(Element element) {

        addNode(element);

    }

    public void add(Entity entity) {

        addNode(entity);

    }

    public void add(Namespace namespace) {

        addNode(namespace);

    }

    public void add(ProcessingInstruction pi) {

        addNode(pi);

    }

    public void add(Text text) {

        addNode(text);

    }

    public boolean remove(CDATA cdata) {

        return removeNode(cdata);

    }

    public boolean remove(Comment comment) {

        return removeNode(comment);

    }

    public boolean remove(Element element) {

        return removeNode(element);

    }

    public boolean remove(Entity entity) {

        return removeNode(entity);

    }

    public boolean remove(Namespace namespace) {

        return removeNode(namespace);

    }

    public boolean remove(ProcessingInstruction pi) {

        return removeNode(pi);

    }

    public boolean remove(Text text) {

        return removeNode(text);

    }

    // Helper methods

    //-------------------------------------------------------------------------    

    public boolean hasMixedContent() {

        List content = contentList();

        if (content == null || content.isEmpty() || content.size() < 2) {

            return false;

        }

        Class prevClass = null;

        for (Iterator iter = content.iterator(); iter.hasNext();) {

            Object object = iter.next();

            Class newClass = object.getClass();

            if (newClass != prevClass) {

                if (prevClass != null) {

                    return true;

                }

                prevClass = newClass;

            }

        }

        return false;

    }

    public boolean isTextOnly() {

        List content = contentList();

        if (content == null || content.isEmpty()) {

            return true;

        }

        for (Iterator iter = content.iterator(); iter.hasNext();) {

            Object object = iter.next();

            if (!(object instanceof CharacterData) && !(object instanceof String)) {

                return false;

            }

        }

        return true;

    }

    public void setText(String text) {

        /* remove all text nodes */
        List allContent = contentList();
        if (allContent != null) {
            Iterator it = allContent.iterator();
            while (it.hasNext()) {
                Node node = (Node) it.next();
                switch (node.getNodeType()) {
                    case CDATA_SECTION_NODE:
                    //case ENTITY_NODE:
                    case ENTITY_REFERENCE_NODE:
                    case TEXT_NODE:
                        it.remove();
                }
            }
        }

        addText(text);

    }

    public String getStringValue() {

        List list = contentList();

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

        return "";

    }

    /**
     * Puts all <code>Text</code> nodes in the full depth of the sub-tree 
     * underneath this <code>Node</code>, including attribute nodes, into a 
     * "normal" form where only structure (e.g., elements, comments, 
     * processing instructions, CDATA sections, and entity references) 
     * separates <code>Text</code> nodes, i.e., there are neither adjacent 
     * <code>Text</code> nodes nor empty <code>Text</code> nodes. This can 
     * be used to ensure that the DOM view of a document is the same as if 
     * it were saved and re-loaded, and is useful when operations (such as 
     * XPointer  lookups) that depend on a particular document tree 
     * structure are to be used.In cases where the document contains 
     * <code>CDATASections</code>, the normalize operation alone may not be 
     * sufficient, since XPointers do not differentiate between 
     * <code>Text</code> nodes and <code>CDATASection</code> nodes.
     * @version DOM Level 2
     */

    public void normalize() {

        List content = contentList();

        Text previousText = null;

        int i = 0;

        while (i < content.size()) {

            Node node = (Node) content.get(i);

            if (node instanceof Text) {

                Text text = (Text) node;

                if (previousText != null) {

                    previousText.appendText(text.getText());

                    remove(text);

                }

                else {

                    String value = text.getText();

                    // only remove empty Text nodes, not whitespace nodes

                    //if ( value == null || value.trim().length() <= 0 ) {

                    if (value == null || value.length() <= 0) {

                        remove(text);

                    }

                    else {

                        previousText = text;

                        i++;

                    }

                }

            }

            else {

                if (node instanceof Element) {

                    Element element = (Element) node;

                    element.normalize();

                }

                previousText = null;

                i++;

            }

        }

    }

    public String elementText(String name) {

        Element element = element(name);

        return (element != null) ? element.getText() : null;

    }

    public String elementText(QName qName) {

        Element element = element(qName);

        return (element != null) ? element.getText() : null;

    }

    public String elementTextTrim(String name) {

        Element element = element(name);

        return (element != null) ? element.getTextTrim() : null;

    }

    public String elementTextTrim(QName qName) {

        Element element = element(qName);

        return (element != null) ? element.getTextTrim() : null;

    }

    // add to me content from another element

    // analagous to the addAll(collection) methods in Java 2 collections

    public void appendAttributes(Element element) {

        for (int i = 0, size = element.attributeCount(); i < size; i++) {

            Attribute attribute = element.attribute(i);

            if (attribute.supportsParent()) {

                addAttribute(attribute.getQName(), attribute.getValue());

            }

            else {

                add(attribute);

            }

        }

    }

    /** <p>This returns a deep clone of this element.
      * The new element is detached from its parent, and getParent() on the 
      * clone will return null.</p>
      *
      * @return the clone of this element
      */

    /*    
        public Object clone() {
            Element clone = createElement(getQName());
            clone.appendAttributes(this);
            clone.appendContent(this);
            return clone;
        }
    */

    public Element createCopy() {

        Element clone = createElement(getQName());

        clone.appendAttributes(this);

        clone.appendContent(this);

        return clone;

    }

    public Element createCopy(String name) {

        Element clone = createElement(name);

        clone.appendAttributes(this);

        clone.appendContent(this);

        return clone;

    }

    public Element createCopy(QName qName) {

        Element clone = createElement(qName);

        clone.appendAttributes(this);

        clone.appendContent(this);

        return clone;

    }

    public QName getQName(String qualifiedName) {

        String prefix = "";

        String localName = qualifiedName;

        int index = qualifiedName.indexOf(":");

        if (index > 0) {

            prefix = qualifiedName.substring(0, index);

            localName = qualifiedName.substring(index + 1);

        }

        Namespace namespace = getNamespaceForPrefix(prefix);

        if (namespace != null) {

            return getDocumentFactory().createQName(localName, namespace);

        }

        else {

            return getDocumentFactory().createQName(localName);

        }

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

            List list = contentList();

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

            List list = contentList();

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

            return null;

        }

    }

    public List declaredNamespaces() {

        BackedList answer = createResultList();

        if (getNamespaceURI().length() > 0) {

            answer.addLocal(getNamespace());

        }

        List list = contentList();

        int size = list.size();

        for (int i = 0; i < size; i++) {

            Object object = list.get(i);

            if (object instanceof Namespace) {

                answer.addLocal(object);

            }

        }

        return answer;

    }

    public List additionalNamespaces() {

        List list = contentList();

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

    public List additionalNamespaces(String defaultNamespaceURI) {

        List list = contentList();

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

    // Implementation helper methods

    //-------------------------------------------------------------------------    

    /** Ensures that the list of attributes has the given size */

    public void ensureAttributesCapacity(int minCapacity) {

        if (minCapacity > 1) {

            List list = attributeList();

            if (list instanceof ArrayList) {

                ArrayList arrayList = (ArrayList) list;

                arrayList.ensureCapacity(minCapacity);

            }

        }

    }

    // Implementation methods

    //-------------------------------------------------------------------------    

    protected Element createElement(String name) {

        return getDocumentFactory().createElement(name);

    }

    protected Element createElement(QName qName) {

        return getDocumentFactory().createElement(qName);

    }

    protected void addNode(Node node) {

        if (node.getParent() != null) {

            // XXX: could clone here

            String message =
                "The Node already has an existing parent of \""
                    + node.getParent().getQualifiedName()
                    + "\"";

            throw new IllegalAddException(this, node, message);

        }

        addNewNode(node);

    }

    /** Like addNode() but does not require a parent check */

    protected void addNewNode(Node node) {

        contentList().add(node);

        childAdded(node);

    }

    protected boolean removeNode(Node node) {

        boolean answer = contentList().remove(node);

        if (answer) {

            childRemoved(node);

        }

        return answer;

    }

    /** Called when a new child node is added to
      * create any parent relationships
      */

    protected void childAdded(Node node) {

        if (node != null) {

            node.setParent(this);

        }

    }

    protected void childRemoved(Node node) {

        if (node != null) {

            node.setParent(null);

            node.setDocument(null);

        }

    }

    /** @return the internal List used to store attributes or
      * creates one if one is not available
      */

    protected abstract List attributeList();

    /** @return the internal List used to store attributes or
      * creates one with the specified size if one is not available
      */

    protected abstract List attributeList(int attributeCount);

    protected DocumentFactory getDocumentFactory() {

        QName qName = getQName();

        // QName might be null as we might not have been constructed yet

        if (qName != null) {

            DocumentFactory factory = qName.getDocumentFactory();

            if (factory != null) {

                return factory;

            }

        }

        return DOCUMENT_FACTORY;

    }

    /** A Factory Method pattern which creates 
      * a List implementation used to store attributes
      */

    protected List createAttributeList() {

        return createAttributeList(DEFAULT_CONTENT_LIST_SIZE);

    }

    /** A Factory Method pattern which creates 
      * a List implementation used to store attributes
      */

    protected List createAttributeList(int size) {

        return new ArrayList(size);

    }

    protected Iterator createSingleIterator(Object result) {

        return new SingleIterator(result);

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
