/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.DTD;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.util.XMLEventConsumer;

import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.CDATA;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.Text;

/**
 * Writes DOM4J {@link Node}s to a StAX event stream. In addition the
 * <code>createXXX</code> methods are provided to directly create STAX events
 * from DOM4J nodes.
 * 
 * @author Christian Niles
 */
public class STAXEventWriter {
    /** The event stream to which events are written. */
    private XMLEventConsumer consumer;

    /** The event factory used to construct events. */
    private XMLEventFactory factory = XMLEventFactory.newInstance();

    private XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

    public STAXEventWriter() {
    }

    /**
     * Constructs a <code>STAXEventWriter</code> that writes events to the
     * provided file.
     * 
     * @param file
     *            The file to which events will be written.
     * 
     * @throws XMLStreamException
     *             If an error occurs creating an event writer from the file.
     * @throws IOException
     *             If an error occurs openin the file for writing.
     */
    public STAXEventWriter(File file) throws XMLStreamException, IOException {
        consumer = outputFactory.createXMLEventWriter(new FileWriter(file));
    }

    /**
     * Constructs a <code>STAXEventWriter</code> that writes events to the
     * provided character stream.
     * 
     * @param writer
     *            The character stream to which events will be written.
     * 
     * @throws XMLStreamException
     *             If an error occurs constructing an event writer from the
     *             character stream.
     */
    public STAXEventWriter(Writer writer) throws XMLStreamException {
        consumer = outputFactory.createXMLEventWriter(writer);
    }

    /**
     * Constructs a <code>STAXEventWriter</code> that writes events to the
     * provided stream.
     * 
     * @param stream
     *            The output stream to which events will be written.
     * 
     * @throws XMLStreamException
     *             If an error occurs constructing an event writer from the
     *             stream.
     */
    public STAXEventWriter(OutputStream stream) throws XMLStreamException {
        consumer = outputFactory.createXMLEventWriter(stream);
    }

    /**
     * Constructs a <code>STAXEventWriter</code> that writes events to the
     * provided event stream.
     * 
     * @param consumer
     *            The event stream to which events will be written.
     */
    public STAXEventWriter(XMLEventConsumer consumer) {
        this.consumer = consumer;
    }

    /**
     * Returns a reference to the underlying event consumer to which events are
     * written.
     * 
     * @return The underlying event consumer to which events are written.
     */
    public XMLEventConsumer getConsumer() {
        return consumer;
    }

    /**
     * Sets the underlying event consumer to which events are written.
     * 
     * @param consumer
     *            The event consumer to which events should be written.
     */
    public void setConsumer(XMLEventConsumer consumer) {
        this.consumer = consumer;
    }

    /**
     * Returns a reference to the event factory used to construct STAX events.
     * 
     * @return The event factory used to construct STAX events.
     */
    public XMLEventFactory getEventFactory() {
        return factory;
    }

    /**
     * Sets the event factory used to construct STAX events.
     * 
     * @param eventFactory
     *            The new event factory.
     */
    public void setEventFactory(XMLEventFactory eventFactory) {
        this.factory = eventFactory;
    }

    /**
     * Writes a DOM4J {@link Node}to the stream. This method is simply a
     * gateway to the overloaded methods such as {@link#writeElement(Element)}.
     * 
     * @param n
     *            The DOM4J {@link Node}to write to the stream.
     * 
     * @throws XMLStreamException
     *             If an error occurs writing to the stream.
     */
    public void writeNode(Node n) throws XMLStreamException {
        switch (n.getNodeType()) {
            case Node.ELEMENT_NODE:
                writeElement((Element) n);

                break;

            case Node.TEXT_NODE:
                writeText((Text) n);

                break;

            case Node.ATTRIBUTE_NODE:
                writeAttribute((Attribute) n);

                break;

            case Node.NAMESPACE_NODE:
                writeNamespace((Namespace) n);

                break;

            case Node.COMMENT_NODE:
                writeComment((Comment) n);

                break;

            case Node.CDATA_SECTION_NODE:
                writeCDATA((CDATA) n);

                break;

            case Node.PROCESSING_INSTRUCTION_NODE:
                writeProcessingInstruction((org.dom4j.ProcessingInstruction) n);

                break;

            case Node.ENTITY_REFERENCE_NODE:
                writeEntity((Entity) n);

                break;

            case Node.DOCUMENT_NODE:
                writeDocument((Document) n);

                break;

            case Node.DOCUMENT_TYPE_NODE:
                writeDocumentType((DocumentType) n);

                break;

            default:
                throw new XMLStreamException("Unsupported DOM4J Node: " + n);
        }
    }

    /**
     * Writes each child node within the provided {@link Branch}instance. This
     * method simply iterates through the {@link Branch}'s nodes and calls
     * {@link #writeNode(Node)}.
     * 
     * @param branch
     *            The node whose children will be written to the stream.
     * 
     * @throws XMLStreamException
     *             If an error occurs writing to the stream.
     */
    public void writeChildNodes(Branch branch) throws XMLStreamException {
        for (int i = 0, s = branch.nodeCount(); i < s; i++) {
            Node n = branch.node(i);
            writeNode(n);
        }
    }

    /**
     * Writes a DOM4J {@link Element}node and its children to the stream.
     * 
     * @param elem
     *            The {@link Element}node to write to the stream.
     * 
     * @throws XMLStreamException
     *             If an error occurs writing to the stream.
     */
    public void writeElement(Element elem) throws XMLStreamException {
        consumer.add(createStartElement(elem));
        writeChildNodes(elem);
        consumer.add(createEndElement(elem));
    }

    /**
     * Constructs a STAX {@link StartElement}event from a DOM4J {@link
     * Element}.
     * 
     * @param elem
     *            The {@link Element}from which to construct the event.
     * 
     * @return The newly constructed {@link StartElement}event.
     */
    public StartElement createStartElement(Element elem) {
        // create name
        QName tagName = createQName(elem.getQName());

        // create attribute & namespace iterators
        Iterator attrIter = new AttributeIterator(elem.attributeIterator());
        Iterator nsIter = new NamespaceIterator(elem.declaredNamespaces()
                .iterator());

        // create start event
        return factory.createStartElement(tagName, attrIter, nsIter);
    }

    /**
     * Constructs a STAX {@link EndElement}event from a DOM4J {@link Element}.
     * 
     * @param elem
     *            The {@link Element}from which to construct the event.
     * 
     * @return The newly constructed {@link EndElement}event.
     */
    public EndElement createEndElement(Element elem) {
        QName tagName = createQName(elem.getQName());
        Iterator nsIter = new NamespaceIterator(elem.declaredNamespaces()
                .iterator());

        return factory.createEndElement(tagName, nsIter);
    }

    /**
     * Writes a DOM4J {@link Attribute}to the stream.
     * 
     * @param attr
     *            The {@link Attribute}to write to the stream.
     * 
     * @throws XMLStreamException
     *             If an error occurs writing to the stream.
     */
    public void writeAttribute(Attribute attr) throws XMLStreamException {
        consumer.add(createAttribute(attr));
    }

    /**
     * Constructs a STAX {@link javax.xml.stream.events.Attribute}event from a
     * DOM4J {@link Attribute}.
     * 
     * @param attr
     *            The {@link Attribute}from which to construct the event.
     * 
     * @return The newly constructed {@link javax.xml.stream.events.Attribute}
     *         event.
     */
    public javax.xml.stream.events.Attribute createAttribute(Attribute attr) {
        QName attrName = createQName(attr.getQName());
        String value = attr.getValue();

        return factory.createAttribute(attrName, value);
    }

    /**
     * Writes a DOM4J {@link Namespace}to the stream.
     * 
     * @param ns
     *            The {@link Namespace}to write to the stream.
     * 
     * @throws XMLStreamException
     *             If an error occurs writing to the stream.
     */
    public void writeNamespace(Namespace ns) throws XMLStreamException {
        consumer.add(createNamespace(ns));
    }

    /**
     * Constructs a STAX {@link javax.xml.stream.events.Namespace}event from a
     * DOM4J {@link Namespace}.
     * 
     * @param ns
     *            The {@link Namespace}from which to construct the event.
     * 
     * @return The constructed {@link javax.xml.stream.events.Namespace}event.
     */
    public javax.xml.stream.events.Namespace createNamespace(Namespace ns) {
        String prefix = ns.getPrefix();
        String uri = ns.getURI();

        return factory.createNamespace(prefix, uri);
    }

    /**
     * Writes a DOM4J {@link Text}to the stream.
     * 
     * @param text
     *            The {@link Text}to write to the stream.
     * 
     * @throws XMLStreamException
     *             If an error occurs writing to the stream.
     */
    public void writeText(Text text) throws XMLStreamException {
        consumer.add(createCharacters(text));
    }

    /**
     * Constructs a STAX {@link Characters}event from a DOM4J {@link Text}.
     * 
     * @param text
     *            The {@link Text}from which to construct the event.
     * 
     * @return The constructed {@link Characters}event.
     */
    public Characters createCharacters(Text text) {
        return factory.createCharacters(text.getText());
    }

    /**
     * Writes a DOM4J {@link CDATA}to the event stream.
     * 
     * @param cdata
     *            The {@link CDATA}to write to the stream.
     * 
     * @throws XMLStreamException
     *             If an error occurs writing to the stream.
     */
    public void writeCDATA(CDATA cdata) throws XMLStreamException {
        consumer.add(createCharacters(cdata));
    }

    /**
     * Constructs a STAX {@link Characters}event from a DOM4J {@link CDATA}.
     * 
     * @param cdata
     *            The {@link CDATA}from which to construct the event.
     * 
     * @return The newly constructed {@link Characters}event.
     */
    public Characters createCharacters(CDATA cdata) {
        return factory.createCData(cdata.getText());
    }

    /**
     * Writes a DOM4J {@link Comment}to the stream.
     * 
     * @param comment
     *            The {@link Comment}to write to the stream.
     * 
     * @throws XMLStreamException
     *             If an error occurs writing to the stream.
     */
    public void writeComment(Comment comment) throws XMLStreamException {
        consumer.add(createComment(comment));
    }

    /**
     * Constructs a STAX {@link javax.xml.stream.events.Comment}event from a
     * DOM4J {@link Comment}.
     * 
     * @param comment
     *            The {@link Comment}from which to construct the event.
     * 
     * @return The constructed {@link javax.xml.stream.events.Comment}event.
     */
    public javax.xml.stream.events.Comment createComment(Comment comment) {
        return factory.createComment(comment.getText());
    }

    /**
     * Writes a DOM4J {@link ProcessingInstruction}to the stream.
     * 
     * @param pi
     *            The {@link ProcessingInstruction}to write to the stream.
     * 
     * @throws XMLStreamException
     *             If an error occurs writing to the stream.
     */
    public void writeProcessingInstruction(org.dom4j.ProcessingInstruction pi)
            throws XMLStreamException {
        consumer.add(createProcessingInstruction(pi));
    }

    /**
     * Constructs a STAX {@link javax.xml.stream.events.ProcessingInstruction}
     * event from a DOM4J {@link ProcessingInstruction}.
     * 
     * @param pi
     *            The {@link ProcessingInstruction}from which to construct the
     *            event.
     * 
     * @return The constructed {@link
     *         javax.xml.stream.events.ProcessingInstruction} event.
     */
    public ProcessingInstruction createProcessingInstruction(
            org.dom4j.ProcessingInstruction pi) {
        String target = pi.getTarget();
        String data = pi.getText();

        return factory.createProcessingInstruction(target, data);
    }

    /**
     * Writes a DOM4J {@link Entity}to the stream.
     * 
     * @param entity
     *            The {@link Entity}to write to the stream.
     * 
     * @throws XMLStreamException
     *             If an error occurs writing to the stream.
     */
    public void writeEntity(Entity entity) throws XMLStreamException {
        consumer.add(createEntityReference(entity));
    }

    /**
     * Constructs a STAX {@link EntityReference}event from a DOM4J {@link
     * Entity}.
     * 
     * @param entity
     *            The {@link Entity}from which to construct the event.
     * 
     * @return The constructed {@link EntityReference}event.
     */
    private EntityReference createEntityReference(Entity entity) {
        return factory.createEntityReference(entity.getName(), null);
    }

    /**
     * Writes a DOM4J {@link DocumentType}to the stream.
     * 
     * @param docType
     *            The {@link DocumentType}to write to the stream.
     * 
     * @throws XMLStreamException
     *             If an error occurs writing to the stream.
     */
    public void writeDocumentType(DocumentType docType)
            throws XMLStreamException {
        consumer.add(createDTD(docType));
    }

    /**
     * Constructs a STAX {@link DTD}event from a DOM4J {@link DocumentType}.
     * 
     * @param docType
     *            The {@link DocumentType}from which to construct the event.
     * 
     * @return The constructed {@link DTD}event.
     * 
     * @throws RuntimeException
     *             DOCUMENT ME!
     */
    public DTD createDTD(DocumentType docType) {
        StringWriter decl = new StringWriter();

        try {
            docType.write(decl);
        } catch (IOException e) {
            throw new RuntimeException("Error writing DTD", e);
        }

        return factory.createDTD(decl.toString());
    }

    /**
     * Writes a DOM4J {@link Document}node, and all its contents, to the
     * stream.
     * 
     * @param doc
     *            The {@link Document}to write to the stream.
     * 
     * @throws XMLStreamException
     *             If an error occurs writing to the stream.
     */
    public void writeDocument(Document doc) throws XMLStreamException {
        consumer.add(createStartDocument(doc));

        writeChildNodes(doc);

        consumer.add(createEndDocument(doc));
    }

    /**
     * Constructs a STAX {@link StartDocument}event from a DOM4J {@link
     * Document}.
     * 
     * @param doc
     *            The {@link Document}from which to construct the event.
     * 
     * @return The constructed {@link StartDocument}event.
     */
    public StartDocument createStartDocument(Document doc) {
        String encoding = doc.getXMLEncoding();

        if (encoding != null) {
            return factory.createStartDocument(encoding);
        } else {
            return factory.createStartDocument();
        }
    }

    /**
     * Constructs a STAX {@link EndDocument}event from a DOM4J {@link
     * Document}.
     * 
     * @param doc
     *            The {@link Document}from which to construct the event.
     * 
     * @return The constructed {@link EndDocument}event.
     */
    public EndDocument createEndDocument(Document doc) {
        return factory.createEndDocument();
    }

    /**
     * Constructs a STAX {@link QName}from a DOM4J {@link org.dom4j.QName}.
     * 
     * @param qname
     *            The {@link org.dom4j.QName}from which to construct the STAX
     *            {@link QName}.
     * 
     * @return The constructed {@link QName}.
     */
    public QName createQName(org.dom4j.QName qname) {
        return new QName(qname.getNamespaceURI(), qname.getName(), qname
                .getNamespacePrefix());
    }

    /**
     * Internal {@link Iterator}implementation used to pass DOM4J {@link
     * Attribute}s to the stream.
     */
    private class AttributeIterator implements Iterator {
        /** The underlying DOm4J attribute iterator. */
        private Iterator iter;

        public AttributeIterator(Iterator iter) {
            this.iter = iter;
        }

        public boolean hasNext() {
            return iter.hasNext();
        }

        public Object next() {
            Attribute attr = (Attribute) iter.next();
            QName attrName = createQName(attr.getQName());
            String value = attr.getValue();

            return factory.createAttribute(attrName, value);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Internal {@link Iterator}implementation used to pass DOM4J {@link
     * Namespace}s to the stream.
     */
    private class NamespaceIterator implements Iterator {
        private Iterator iter;

        public NamespaceIterator(Iterator iter) {
            this.iter = iter;
        }

        public boolean hasNext() {
            return iter.hasNext();
        }

        public Object next() {
            Namespace ns = (Namespace) iter.next();
            String prefix = ns.getPrefix();
            String nsURI = ns.getURI();

            return factory.createNamespace(prefix, nsURI);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
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
