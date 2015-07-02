/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.io;

import java.io.InputStream;
import java.io.Reader;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.dom4j.CharacterData;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Node;

/**
 * Reads a DOM4J {@link Document}, as well as other {@link Node}s, from a StAX
 * {@link XMLEventReader}.
 * 
 * @author Christian Niles
 */
public class STAXEventReader {
    /** Reference to the DocumentFactory used to build DOM4J nodes. */
    private DocumentFactory factory;

    /** A StAX input factory, used to construct streams from IO streams. */
    private XMLInputFactory inputFactory = XMLInputFactory.newInstance();

    /**
     * Constructs a default <code>STAXEventReader</code> instance with a
     * default {@link DocumentFactory}.
     */
    public STAXEventReader() {
        this.factory = DocumentFactory.getInstance();
    }

    /**
     * Constructs a <code>STAXEventReader</code> instance that uses the
     * specified {@link DocumentFactory}to construct DOM4J {@link Node}s.
     * 
     * @param factory
     *            The DocumentFactory to use when constructing DOM4J nodes, or
     *            <code>null</code> if a default should be used.
     */
    public STAXEventReader(DocumentFactory factory) {
        if (factory != null) {
            this.factory = factory;
        } else {
            this.factory = DocumentFactory.getInstance();
        }
    }

    /**
     * Sets the DocumentFactory to be used when constructing DOM4J nodes.
     * 
     * @param documentFactory
     *            The DocumentFactory to use when constructing DOM4J nodes, or
     *            <code>null</code> if a default should be used.
     */
    public void setDocumentFactory(DocumentFactory documentFactory) {
        if (documentFactory != null) {
            this.factory = documentFactory;
        } else {
            this.factory = DocumentFactory.getInstance();
        }
    }

    /**
     * Constructs a StAX event stream from the provided I/O stream and reads a
     * DOM4J document from it.
     * 
     * @param is
     *            The I/O stream from which the Document will be read.
     * 
     * @return The Document that was read from the stream.
     * 
     * @throws XMLStreamException
     *             If an error occurs reading content from the stream.
     */
    public Document readDocument(InputStream is) throws XMLStreamException {
        return readDocument(is, null);
    }

    /**
     * Constructs a StAX event stream from the provided I/O character stream and
     * reads a DOM4J document from it.
     * 
     * @param reader
     *            The character stream from which the Document will be read.
     * 
     * @return The Document that was read from the stream.
     * 
     * @throws XMLStreamException
     *             If an error occurs reading content from the stream.
     */
    public Document readDocument(Reader reader) throws XMLStreamException {
        return readDocument(reader, null);
    }

    /**
     * Constructs a StAX event stream from the provided I/O stream and reads a
     * DOM4J document from it.
     * 
     * @param is
     *            The I/O stream from which the Document will be read.
     * @param systemId
     *            A system id used to resolve entities.
     * 
     * @return The Document that was read from the stream.
     * 
     * @throws XMLStreamException
     *             If an error occurs reading content from the stream.
     */
    public Document readDocument(InputStream is, String systemId)
            throws XMLStreamException {
        XMLEventReader eventReader = inputFactory.createXMLEventReader(
                systemId, is);

        try {
            return readDocument(eventReader);
        } finally {
            eventReader.close();
        }
    }

    /**
     * Constructs a StAX event stream from the provided I/O character stream and
     * reads a DOM4J document from it.
     * 
     * @param reader
     *            The character stream from which the Document will be read.
     * @param systemId
     *            A system id used to resolve entities.
     * 
     * @return The Document that was read from the stream.
     * 
     * @throws XMLStreamException
     *             If an error occurs reading content from the stream.
     */
    public Document readDocument(Reader reader, String systemId)
            throws XMLStreamException {
        XMLEventReader eventReader = inputFactory.createXMLEventReader(
                systemId, reader);

        try {
            return readDocument(eventReader);
        } finally {
            eventReader.close();
        }
    }

    /**
     * Reads a {@link Node}from the event stream. If the next event is a
     * {@link StartElement}, all events until the closing {@link EndElement}
     * will be read, and the resulting nodes will be added to the returned
     * {@link Element}.
     * 
     * <p>
     * <strong>Pre-Conditions </strong>: The stream must be positioned before an
     * event other than an <code>EndElement</code>,<code>EndDocument</code>,
     * or any DTD-related events, which are not currently supported.
     * </p>
     * 
     * @param reader
     *            The reader from which events will be read.
     * 
     * @return A DOM4J {@link Node}constructed from the read events.
     * 
     * @throws XMLStreamException
     *             If an error occurs reading from the stream, or the stream was
     *             positioned before an unsupported event.
     */
    public Node readNode(XMLEventReader reader) throws XMLStreamException {
        XMLEvent event = reader.peek();

        if (event.isStartElement()) {
            return readElement(reader);
        } else if (event.isCharacters()) {
            return readCharacters(reader);
        } else if (event.isStartDocument()) {
            return readDocument(reader);
        } else if (event.isProcessingInstruction()) {
            return readProcessingInstruction(reader);
        } else if (event.isEntityReference()) {
            return readEntityReference(reader);
        } else if (event.isAttribute()) {
            return readAttribute(reader);
        } else if (event.isNamespace()) {
            return readNamespace(reader);
        } else {
            throw new XMLStreamException("Unsupported event: " + event);
        }
    }

    /**
     * Reads a DOM4J {@link Document}from the provided stream. The stream
     * should be positioned at the start of a document, or before a {@link
     * StartElement} event.
     * 
     * @param reader
     *            The event stream from which to read the {@link Document}.
     * 
     * @return The {@link Document}that was read from the stream.
     * 
     * @throws XMLStreamException
     *             If an error occurs reading events from the stream.
     */
    public Document readDocument(XMLEventReader reader)
            throws XMLStreamException {
        Document doc = null;

        while (reader.hasNext()) {
            XMLEvent nextEvent = reader.peek();
            int type = nextEvent.getEventType();

            switch (type) {
                case XMLStreamConstants.START_DOCUMENT:

                    StartDocument event = (StartDocument) reader.nextEvent();

                    if (doc == null) {
                        // create document
                        if (event.encodingSet()) {
                            String encodingScheme = event
                                    .getCharacterEncodingScheme();
                            doc = factory.createDocument(encodingScheme);
                        } else {
                            doc = factory.createDocument();
                        }
                    } else {
                        // duplicate or misplaced xml declaration
                        String msg = "Unexpected StartDocument event";
                        throw new XMLStreamException(msg, event.getLocation());
                    }

                    break;

                case XMLStreamConstants.END_DOCUMENT:
                case XMLStreamConstants.SPACE:
                case XMLStreamConstants.CHARACTERS:

                    // skip end document and space outside the root element
                    reader.nextEvent();

                    break;

                default:

                    if (doc == null) {
                        // create document
                        doc = factory.createDocument();
                    }

                    Node n = readNode(reader);
                    doc.add(n);
            }
        }

        return doc;
    }

    /**
     * Reads a DOM4J Element from the provided event stream. The stream must be
     * positioned before an {@link StartElement}event. In addition to the
     * initial start event, all events up to and including the closing {@link
     * EndElement} will be read, and included with the returned element.
     * 
     * @param eventReader
     *            The event stream from which to read the Element.
     * 
     * @return The Element that was read from the stream.
     * 
     * @throws XMLStreamException
     *             If an error occured reading events from the stream, or the
     *             stream was not positioned before a {@linkStartElement}event.
     */
    public Element readElement(XMLEventReader eventReader)
            throws XMLStreamException {
        XMLEvent event = eventReader.peek();

        if (event.isStartElement()) {
            // advance the reader and get the StartElement event
            StartElement startTag = eventReader.nextEvent().asStartElement();
            Element elem = createElement(startTag);

            // read element content
            while (true) {
                if (!eventReader.hasNext()) {
                    String msg = "Unexpected end of stream while reading"
                            + " element content";
                    throw new XMLStreamException(msg);
                }

                XMLEvent nextEvent = eventReader.peek();

                if (nextEvent.isEndElement()) {
                    EndElement endElem = eventReader.nextEvent().asEndElement();

                    if (!endElem.getName().equals(startTag.getName())) {
                        throw new XMLStreamException("Expected "
                                + startTag.getName() + " end-tag, but found"
                                + endElem.getName());
                    }

                    break;
                }

                Node child = readNode(eventReader);
                elem.add(child);
            }

            return elem;
        } else {
            throw new XMLStreamException("Expected Element event, found: "
                    + event);
        }
    }

    /**
     * Constructs a DOM4J Attribute from the provided event stream. The stream
     * must be positioned before an {@link Attribute}event.
     * 
     * @param reader
     *            The event stream from which to read the Attribute.
     * 
     * @return The Attribute that was read from the stream.
     * 
     * @throws XMLStreamException
     *             If an error occured reading events from the stream, or the
     *             stream was not positioned before an {@linkAttribute}event.
     */
    public org.dom4j.Attribute readAttribute(XMLEventReader reader)
            throws XMLStreamException {
        XMLEvent event = reader.peek();

        if (event.isAttribute()) {
            Attribute attr = (Attribute) reader.nextEvent();

            return createAttribute(null, attr);
        } else {
            throw new XMLStreamException("Expected Attribute event, found: "
                    + event);
        }
    }

    /**
     * Constructs a DOM4J Namespace from the provided event stream. The stream
     * must be positioned before a {@link Namespace}event.
     * 
     * @param reader
     *            The event stream from which to read the Namespace.
     * 
     * @return The Namespace that was read from the stream.
     * 
     * @throws XMLStreamException
     *             If an error occured reading events from the stream, or the
     *             stream was not positioned before a {@linkNamespace}event.
     */
    public org.dom4j.Namespace readNamespace(XMLEventReader reader)
            throws XMLStreamException {
        XMLEvent event = reader.peek();

        if (event.isNamespace()) {
            Namespace ns = (Namespace) reader.nextEvent();

            return createNamespace(ns);
        } else {
            throw new XMLStreamException("Expected Namespace event, found: "
                    + event);
        }
    }

    /**
     * Constructs a DOM4J Text or CDATA section from the provided event stream.
     * The stream must be positioned before a {@link Characters}event.
     * 
     * @param reader
     *            The event stream from which to read the Text or CDATA.
     * 
     * @return The Text or CDATA that was read from the stream.
     * 
     * @throws XMLStreamException
     *             If an error occured reading events from the stream, or the
     *             stream was not positioned before a {@linkCharacters}event.
     */
    public CharacterData readCharacters(XMLEventReader reader)
            throws XMLStreamException {
        XMLEvent event = reader.peek();

        if (event.isCharacters()) {
            Characters characters = reader.nextEvent().asCharacters();

            return createCharacterData(characters);
        } else {
            throw new XMLStreamException("Expected Characters event, found: "
                    + event);
        }
    }

    /**
     * Constructs a DOM4J Comment from the provided event stream. The stream
     * must be positioned before a {@link Comment}event.
     * 
     * @param reader
     *            The event stream from which to read the Comment.
     * 
     * @return The Comment that was read from the stream.
     * 
     * @throws XMLStreamException
     *             If an error occured reading events from the stream, or the
     *             stream was not positioned before a {@linkComment}event.
     */
    public org.dom4j.Comment readComment(XMLEventReader reader)
            throws XMLStreamException {
        XMLEvent event = reader.peek();

        if (event instanceof Comment) {
            return createComment((Comment) reader.nextEvent());
        } else {
            throw new XMLStreamException("Expected Comment event, found: "
                    + event);
        }
    }

    /**
     * Constructs a DOM4J Entity from the provided event stream. The stream must
     * be positioned before an {@link EntityReference}event.
     * 
     * @param reader
     *            The event stream from which to read the {@link
     *            EntityReference}.
     * 
     * @return The {@link org.dom4j.Entity}that was read from the stream.
     * 
     * @throws XMLStreamException
     *             If an error occured reading events from the stream, or the
     *             stream was not positioned before an {@linkEntityReference}
     *             event.
     */
    public Entity readEntityReference(XMLEventReader reader)
            throws XMLStreamException {
        XMLEvent event = reader.peek();

        if (event.isEntityReference()) {
            EntityReference entityRef = (EntityReference) reader.nextEvent();

            return createEntity(entityRef);
        } else {
            throw new XMLStreamException("Expected EntityRef event, found: "
                    + event);
        }
    }

    /**
     * Constructs a DOM4J ProcessingInstruction from the provided event stream.
     * The stream must be positioned before a {@link ProcessingInstruction}
     * event.
     * 
     * @param reader
     *            The event stream from which to read the ProcessingInstruction.
     * 
     * @return The ProcessingInstruction that was read from the stream.
     * 
     * @throws XMLStreamException
     *             If an error occured reading events from the stream, or the
     *             stream was not positioned before a {@link
     *             ProcessingInstruction} event.
     */
    public org.dom4j.ProcessingInstruction readProcessingInstruction(
            XMLEventReader reader) throws XMLStreamException {
        XMLEvent event = reader.peek();

        if (event.isProcessingInstruction()) {
            ProcessingInstruction pi = (ProcessingInstruction) reader
                    .nextEvent();

            return createProcessingInstruction(pi);
        } else {
            throw new XMLStreamException("Expected PI event, found: " + event);
        }
    }

    /**
     * Constructs a new DOM4J Element from the provided StartElement event. All
     * attributes and namespaces will be added to the returned element.
     * 
     * @param startEvent
     *            The StartElement event from which to construct the new DOM4J
     *            Element.
     * 
     * @return The Element constructed from the provided StartElement event.
     */
    public Element createElement(StartElement startEvent) {
        QName qname = startEvent.getName();
        org.dom4j.QName elemName = createQName(qname);

        Element elem = factory.createElement(elemName);

        // create attributes
        for (Iterator i = startEvent.getAttributes(); i.hasNext();) {
            Attribute attr = (Attribute) i.next();
            elem.addAttribute(createQName(attr.getName()), attr.getValue());
        }

        // create namespaces
        for (Iterator i = startEvent.getNamespaces(); i.hasNext();) {
            Namespace ns = (Namespace) i.next();
            elem.addNamespace(ns.getPrefix(), ns.getNamespaceURI());
        }

        return elem;
    }

    /**
     * Constructs a new DOM4J Attribute from the provided StAX Attribute event.
     * 
     * @param elem
     *            DOCUMENT ME!
     * @param attr
     *            The Attribute event from which to construct the new DOM4J
     *            Attribute.
     * 
     * @return The Attribute constructed from the provided Attribute event.
     */
    public org.dom4j.Attribute createAttribute(Element elem, Attribute attr) {
        return factory.createAttribute(elem, createQName(attr.getName()), attr
                .getValue());
    }

    /**
     * Constructs a new DOM4J Namespace from the provided StAX Namespace event.
     * 
     * @param ns
     *            The Namespace event from which to construct the new DOM4J
     *            Namespace.
     * 
     * @return The Namespace constructed from the provided Namespace event.
     */
    public org.dom4j.Namespace createNamespace(Namespace ns) {
        return factory.createNamespace(ns.getPrefix(), ns.getNamespaceURI());
    }

    /**
     * Constructs a new DOM4J Text or CDATA object from the provided Characters
     * event.
     * 
     * @param characters
     *            The Characters event from which to construct the new DOM4J
     *            Text or CDATA object.
     * 
     * @return The Text or CDATA object constructed from the provided Characters
     *         event.
     */
    public CharacterData createCharacterData(Characters characters) {
        String data = characters.getData();

        if (characters.isCData()) {
            return factory.createCDATA(data);
        } else {
            return factory.createText(data);
        }
    }

    /**
     * Constructs a new DOM4J Comment from the provided StAX Comment event.
     * 
     * @param comment
     *            The Comment event from which to construct the new DOM4J
     *            Comment.
     * 
     * @return The Comment constructed from the provided Comment event.
     */
    public org.dom4j.Comment createComment(Comment comment) {
        return factory.createComment(comment.getText());
    }

    /**
     * Constructs a new DOM4J Entity from the provided StAX EntityReference
     * event.
     * 
     * @param entityRef
     *            The EntityReference event from which to construct the new
     *            DOM4J Entity.
     * 
     * @return The Entity constructed from the provided EntityReference event.
     */
    public org.dom4j.Entity createEntity(EntityReference entityRef) {
        return factory.createEntity(entityRef.getName(), entityRef
                .getDeclaration().getReplacementText());
    }

    /**
     * Constructs a new DOM4J ProcessingInstruction from the provided StAX
     * ProcessingInstruction event.
     * 
     * @param pi
     *            The ProcessingInstruction event from which to construct the
     *            new DOM4J ProcessingInstruction.
     * 
     * @return The ProcessingInstruction constructed from the provided
     *         ProcessingInstruction event.
     */
    public org.dom4j.ProcessingInstruction createProcessingInstruction(
            ProcessingInstruction pi) {
        return factory
                .createProcessingInstruction(pi.getTarget(), pi.getData());
    }

    /**
     * Constructs a new DOM4J QName from the provided JAXP QName.
     * 
     * @param qname
     *            The JAXP QName from which to create a DOM4J QName.
     * 
     * @return The newly constructed DOM4J QName.
     */
    public org.dom4j.QName createQName(QName qname) {
        return factory.createQName(qname.getLocalPart(), qname.getPrefix(),
                qname.getNamespaceURI());
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
