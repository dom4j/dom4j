/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.io;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Namespace;
import org.dom4j.QName;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <p>
 * Records SAX events such that they may be "replayed" at a later time. Provides
 * an alternative serialization approach when externalizing a DOM4J document.
 * Rather than serializing a document as text and re-parsing, the sax events may
 * be serialized instead.
 * </p>
 * Example usage:
 * 
 * <pre>
 * 
 *  
 *  
 *         SAXEventRecorder recorder = new SAXEventRecorder();
 *         SAXWriter saxWriter = new SAXWriter(recorder, recorder);
 *         saxWriter.write(document);
 *         out.writeObject(recorder);
 *         ...
 *         SAXEventRecorder recorder = (SAXEventRecorder)in.readObject();
 *         SAXContentHandler saxContentHandler = new SAXContentHandler();
 *         recorder.replay(saxContentHandler);
 *         Document document = saxContentHandler.getDocument();
 *  
 *   
 *  
 * </pre>
 * 
 * @author Todd Wolff (Bluestem Software)
 */
public class SAXEventRecorder extends DefaultHandler implements LexicalHandler,
        DeclHandler, DTDHandler, Externalizable {
    public static final long serialVersionUID = 1;

    private static final byte STRING = 0;

    private static final byte OBJECT = 1;

    private static final byte NULL = 2;

    private List events = new ArrayList();

    private Map prefixMappings = new HashMap();

    private static final String XMLNS = "xmlns";

    private static final String EMPTY_STRING = "";

    public SAXEventRecorder() {
    }

    public void replay(ContentHandler handler) throws SAXException {
        SAXEvent saxEvent;
        Iterator itr = events.iterator();

        while (itr.hasNext()) {
            saxEvent = (SAXEvent) itr.next();

            switch (saxEvent.event) {
                // replay to ContentHandler
                case SAXEvent.PROCESSING_INSTRUCTION:
                    handler.processingInstruction((String) saxEvent.getParm(0),
                            (String) saxEvent.getParm(1));

                    break;

                case SAXEvent.START_PREFIX_MAPPING:
                    handler.startPrefixMapping((String) saxEvent.getParm(0),
                            (String) saxEvent.getParm(1));

                    break;

                case SAXEvent.END_PREFIX_MAPPING:
                    handler.endPrefixMapping((String) saxEvent.getParm(0));

                    break;

                case SAXEvent.START_DOCUMENT:
                    handler.startDocument();

                    break;

                case SAXEvent.END_DOCUMENT:
                    handler.endDocument();

                    break;

                case SAXEvent.START_ELEMENT:

                    AttributesImpl attributes = new AttributesImpl();
                    List attParmList = (List) saxEvent.getParm(3);

                    if (attParmList != null) {
                        Iterator attsItr = attParmList.iterator();

                        while (attsItr.hasNext()) {
                            String[] attParms = (String[]) attsItr.next();
                            attributes.addAttribute(attParms[0], attParms[1],
                                    attParms[2], attParms[3], attParms[4]);
                        }
                    }

                    handler.startElement((String) saxEvent.getParm(0),
                            (String) saxEvent.getParm(1), (String) saxEvent
                                    .getParm(2), attributes);

                    break;

                case SAXEvent.END_ELEMENT:
                    handler.endElement((String) saxEvent.getParm(0),
                            (String) saxEvent.getParm(1), (String) saxEvent
                                    .getParm(2));

                    break;

                case SAXEvent.CHARACTERS:

                    char[] chars = (char[]) saxEvent.getParm(0);
                    int start = ((Integer) saxEvent.getParm(1)).intValue();
                    int end = ((Integer) saxEvent.getParm(2)).intValue();
                    handler.characters(chars, start, end);

                    break;

                // replay to LexicalHandler
                case SAXEvent.START_DTD:
                    ((LexicalHandler) handler).startDTD((String) saxEvent
                            .getParm(0), (String) saxEvent.getParm(1),
                            (String) saxEvent.getParm(2));

                    break;

                case SAXEvent.END_DTD:
                    ((LexicalHandler) handler).endDTD();

                    break;

                case SAXEvent.START_ENTITY:
                    ((LexicalHandler) handler).startEntity((String) saxEvent
                            .getParm(0));

                    break;

                case SAXEvent.END_ENTITY:
                    ((LexicalHandler) handler).endEntity((String) saxEvent
                            .getParm(0));

                    break;

                case SAXEvent.START_CDATA:
                    ((LexicalHandler) handler).startCDATA();

                    break;

                case SAXEvent.END_CDATA:
                    ((LexicalHandler) handler).endCDATA();

                    break;

                case SAXEvent.COMMENT:

                    char[] cchars = (char[]) saxEvent.getParm(0);
                    int cstart = ((Integer) saxEvent.getParm(1)).intValue();
                    int cend = ((Integer) saxEvent.getParm(2)).intValue();
                    ((LexicalHandler) handler).comment(cchars, cstart, cend);

                    break;

                // replay to DeclHandler
                case SAXEvent.ELEMENT_DECL:
                    ((DeclHandler) handler).elementDecl((String) saxEvent
                            .getParm(0), (String) saxEvent.getParm(1));

                    break;

                case SAXEvent.ATTRIBUTE_DECL:
                    ((DeclHandler) handler).attributeDecl((String) saxEvent
                            .getParm(0), (String) saxEvent.getParm(1),
                            (String) saxEvent.getParm(2), (String) saxEvent
                                    .getParm(3), (String) saxEvent.getParm(4));

                    break;

                case SAXEvent.INTERNAL_ENTITY_DECL:
                    ((DeclHandler) handler).internalEntityDecl(
                            (String) saxEvent.getParm(0), (String) saxEvent
                                    .getParm(1));

                    break;

                case SAXEvent.EXTERNAL_ENTITY_DECL:
                    ((DeclHandler) handler).externalEntityDecl(
                            (String) saxEvent.getParm(0), (String) saxEvent
                                    .getParm(1), (String) saxEvent.getParm(2));

                    break;

                default:
                    throw new SAXException("Unrecognized event: "
                            + saxEvent.event);
            }
        }
    }

    // ContentHandler interface
    // -------------------------------------------------------------------------
    public void processingInstruction(String target, String data)
            throws SAXException {
        SAXEvent saxEvent = new SAXEvent(SAXEvent.PROCESSING_INSTRUCTION);
        saxEvent.addParm(target);
        saxEvent.addParm(data);
        events.add(saxEvent);
    }

    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        SAXEvent saxEvent = new SAXEvent(SAXEvent.START_PREFIX_MAPPING);
        saxEvent.addParm(prefix);
        saxEvent.addParm(uri);
        events.add(saxEvent);
    }

    public void endPrefixMapping(String prefix) throws SAXException {
        SAXEvent saxEvent = new SAXEvent(SAXEvent.END_PREFIX_MAPPING);
        saxEvent.addParm(prefix);
        events.add(saxEvent);
    }

    public void startDocument() throws SAXException {
        SAXEvent saxEvent = new SAXEvent(SAXEvent.START_DOCUMENT);
        events.add(saxEvent);
    }

    public void endDocument() throws SAXException {
        SAXEvent saxEvent = new SAXEvent(SAXEvent.END_DOCUMENT);
        events.add(saxEvent);
    }

    public void startElement(String namespaceURI, String localName,
            String qualifiedName, Attributes attributes) throws SAXException {
        SAXEvent saxEvent = new SAXEvent(SAXEvent.START_ELEMENT);
        saxEvent.addParm(namespaceURI);
        saxEvent.addParm(localName);
        saxEvent.addParm(qualifiedName);

        QName qName = null;
        if (namespaceURI != null) {
            qName = new QName(localName, Namespace.get(namespaceURI));
        } else {
            qName = new QName(localName);
        }

        if ((attributes != null) && (attributes.getLength() > 0)) {
            List attParmList = new ArrayList(attributes.getLength());
            String[] attParms = null;

            for (int i = 0; i < attributes.getLength(); i++) {

                String attLocalName = attributes.getLocalName(i);

                if (attLocalName.startsWith(XMLNS)) {

                    // if SAXWriter is writing a DOMDocument, namespace
                    // decls are treated as attributes. record a start
                    // prefix mapping event
                    String prefix = null;
                    if (attLocalName.length() > 5) {
                        prefix = attLocalName.substring(6);
                    } else {
                        prefix = EMPTY_STRING;
                    }

                    SAXEvent prefixEvent = new SAXEvent(
                            SAXEvent.START_PREFIX_MAPPING);
                    prefixEvent.addParm(prefix);
                    prefixEvent.addParm(attributes.getValue(i));
                    events.add(prefixEvent);

                    // 'register' the prefix so that we can generate
                    // an end prefix mapping event within endElement
                    List prefixes = (List) prefixMappings.get(qName);
                    if (prefixes == null) {
                        prefixes = new ArrayList();
                        prefixMappings.put(qName, prefixes);
                    }
                    prefixes.add(prefix);

                } else {

                    attParms = new String[5];
                    attParms[0] = attributes.getURI(i);
                    attParms[1] = attLocalName;
                    attParms[2] = attributes.getQName(i);
                    attParms[3] = attributes.getType(i);
                    attParms[4] = attributes.getValue(i);
                    attParmList.add(attParms);

                }

            }

            saxEvent.addParm(attParmList);
        }

        events.add(saxEvent);
    }

    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException {

        SAXEvent saxEvent = new SAXEvent(SAXEvent.END_ELEMENT);
        saxEvent.addParm(namespaceURI);
        saxEvent.addParm(localName);
        saxEvent.addParm(qName);
        events.add(saxEvent);

        // check to see if a we issued a start prefix mapping event
        // for DOMDocument namespace decls

        QName elementName = null;
        if (namespaceURI != null) {
            elementName = new QName(localName, Namespace.get(namespaceURI));
        } else {
            elementName = new QName(localName);
        }

        List prefixes = (List) prefixMappings.get(elementName);
        if (prefixes != null) {
            Iterator itr = prefixes.iterator();
            while (itr.hasNext()) {
                SAXEvent prefixEvent = 
                        new SAXEvent(SAXEvent.END_PREFIX_MAPPING);
                prefixEvent.addParm(itr.next());
                events.add(prefixEvent);
            }
        }

    }

    public void characters(char[] ch, int start, int end) throws SAXException {
        SAXEvent saxEvent = new SAXEvent(SAXEvent.CHARACTERS);
        saxEvent.addParm(ch);
        saxEvent.addParm(new Integer(start));
        saxEvent.addParm(new Integer(end));
        events.add(saxEvent);
    }

    // LexicalHandler interface
    // -------------------------------------------------------------------------
    public void startDTD(String name, String publicId, String systemId)
            throws SAXException {
        SAXEvent saxEvent = new SAXEvent(SAXEvent.START_DTD);
        saxEvent.addParm(name);
        saxEvent.addParm(publicId);
        saxEvent.addParm(systemId);
        events.add(saxEvent);
    }

    public void endDTD() throws SAXException {
        SAXEvent saxEvent = new SAXEvent(SAXEvent.END_DTD);
        events.add(saxEvent);
    }

    public void startEntity(String name) throws SAXException {
        SAXEvent saxEvent = new SAXEvent(SAXEvent.START_ENTITY);
        saxEvent.addParm(name);
        events.add(saxEvent);
    }

    public void endEntity(String name) throws SAXException {
        SAXEvent saxEvent = new SAXEvent(SAXEvent.END_ENTITY);
        saxEvent.addParm(name);
        events.add(saxEvent);
    }

    public void startCDATA() throws SAXException {
        SAXEvent saxEvent = new SAXEvent(SAXEvent.START_CDATA);
        events.add(saxEvent);
    }

    public void endCDATA() throws SAXException {
        SAXEvent saxEvent = new SAXEvent(SAXEvent.END_CDATA);
        events.add(saxEvent);
    }

    public void comment(char[] ch, int start, int end) throws SAXException {
        SAXEvent saxEvent = new SAXEvent(SAXEvent.COMMENT);
        saxEvent.addParm(ch);
        saxEvent.addParm(new Integer(start));
        saxEvent.addParm(new Integer(end));
        events.add(saxEvent);
    }

    // DeclHandler interface
    // -------------------------------------------------------------------------
    public void elementDecl(String name, String model) throws SAXException {
        SAXEvent saxEvent = new SAXEvent(SAXEvent.ELEMENT_DECL);
        saxEvent.addParm(name);
        saxEvent.addParm(model);
        events.add(saxEvent);
    }

    public void attributeDecl(String eName, String aName, String type,
            String valueDefault, String value) throws SAXException {
        SAXEvent saxEvent = new SAXEvent(SAXEvent.ATTRIBUTE_DECL);
        saxEvent.addParm(eName);
        saxEvent.addParm(aName);
        saxEvent.addParm(type);
        saxEvent.addParm(valueDefault);
        saxEvent.addParm(value);
        events.add(saxEvent);
    }

    public void internalEntityDecl(String name, String value)
            throws SAXException {
        SAXEvent saxEvent = new SAXEvent(SAXEvent.INTERNAL_ENTITY_DECL);
        saxEvent.addParm(name);
        saxEvent.addParm(value);
        events.add(saxEvent);
    }

    public void externalEntityDecl(String name, String publicId, String sysId)
            throws SAXException {
        SAXEvent saxEvent = new SAXEvent(SAXEvent.EXTERNAL_ENTITY_DECL);
        saxEvent.addParm(name);
        saxEvent.addParm(publicId);
        saxEvent.addParm(sysId);
        events.add(saxEvent);
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        if (events == null) {
            out.writeByte(NULL);
        } else {
            out.writeByte(OBJECT);
            out.writeObject(events);
        }
    }

    public void readExternal(ObjectInput in) throws ClassNotFoundException,
            IOException {
        if (in.readByte() != NULL) {
            events = (List) in.readObject();
        }
    }

    // SAXEvent inner class
    // -------------------------------------------------------------------------
    static class SAXEvent implements Externalizable {
        public static final long serialVersionUID = 1;

        static final byte PROCESSING_INSTRUCTION = 1;

        static final byte START_PREFIX_MAPPING = 2;

        static final byte END_PREFIX_MAPPING = 3;

        static final byte START_DOCUMENT = 4;

        static final byte END_DOCUMENT = 5;

        static final byte START_ELEMENT = 6;

        static final byte END_ELEMENT = 7;

        static final byte CHARACTERS = 8;

        static final byte START_DTD = 9;

        static final byte END_DTD = 10;

        static final byte START_ENTITY = 11;

        static final byte END_ENTITY = 12;

        static final byte START_CDATA = 13;

        static final byte END_CDATA = 14;

        static final byte COMMENT = 15;

        static final byte ELEMENT_DECL = 16;

        static final byte ATTRIBUTE_DECL = 17;

        static final byte INTERNAL_ENTITY_DECL = 18;

        static final byte EXTERNAL_ENTITY_DECL = 19;

        protected byte event;

        protected List parms;

        public SAXEvent() {
        }

        SAXEvent(byte event) {
            this.event = event;
        }

        void addParm(Object parm) {
            if (parms == null) {
                parms = new ArrayList(3);
            }

            parms.add(parm);
        }

        Object getParm(int index) {
            if ((parms != null) && (index < parms.size())) {
                return parms.get(index);
            } else {
                return null;
            }
        }

        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeByte(event);

            if (parms == null) {
                out.writeByte(NULL);
            } else {
                out.writeByte(OBJECT);
                out.writeObject(parms);
            }
        }

        public void readExternal(ObjectInput in) throws ClassNotFoundException,
                IOException {
            event = in.readByte();

            if (in.readByte() != NULL) {
                parms = (List) in.readObject();
            }
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
