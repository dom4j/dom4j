/*
 * Copyright (c) 1999-2000 by David Brownell.  All Rights Reserved.
 *
 * This program is open source software; you may use, copy, modify, and
 * redistribute it under the terms of the LICENSE with which it was
 * originally distributed.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * LICENSE for more details.
 */

package org.dom4j.io.aelfred;

import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;


// $Id$

/**
 * This class extends the SAX base handler class to support the
 * SAX2 Lexical and Declaration handlers.  All the handler methods do
 * is return; except that the SAX base class handles fatal errors by
 * throwing an exception.
 *
 * @author David Brownell
 * @version $Date$
 */
public class DefaultHandler extends org.xml.sax.helpers.DefaultHandler
    implements LexicalHandler, DeclHandler
{
    /** Constructs a handler which ignores all parsing events. */
    public DefaultHandler () { }

//    // SAX1 DocumentHandler (methods not in SAX2 ContentHandler)
//
//    /** <b>SAX1</b> called at the beginning of an element */
//    public void startElement (String name, AttributeList attrs)
//    throws SAXException
//  {}
//
//    /** <b>SAX1</b> called at the end of an element */
//    public void endElement (String name)
//    throws SAXException
//  {}

    // SAX2 LexicalHandler

    /** <b>SAX2</b>:  called before parsing CDATA characters */
    public void startCDATA ()
    throws SAXException
    {}

    /** <b>SAX2</b>:  called after parsing CDATA characters */
    public void endCDATA ()
    throws SAXException
    {}

    /** <b>SAX2</b>:  called when the doctype is partially parsed */
    public void startDTD (String root, String pubid, String sysid)
    throws SAXException
    {}

    /** <b>SAX2</b>:  called after the doctype is parsed */
    public void endDTD ()
    throws SAXException
    {}

    /**
     * <b>SAX2</b>:  called before parsing a general entity in content
     */
    public void startEntity (String name)
    throws SAXException
    {}

    /**
     * <b>SAX2</b>:  called after parsing a general entity in content
     */
    public void endEntity (String name)
    throws SAXException
    {}

    /** <b>SAX2</b>:  called when comments are parsed */
    public void comment (char buf [], int off, int len)
    throws SAXException
    { }

    // SAX2 DeclHandler

    /** <b>SAX2</b>:  called on attribute declarations */
    public void attributeDecl (String element, String name,
        String type, String defaultType, String defaltValue)
    throws SAXException
    {}

    /** <b>SAX2</b>:  called on element declarations */
    public void elementDecl (String name, String model)
    throws SAXException
    {}

    /** <b>SAX2</b>:  called on external entity declarations */
    public void externalEntityDecl (String name, String pubid, String sysid)
    throws SAXException
    {}

    /** <b>SAX2</b>:  called on internal entity declarations */
    public void internalEntityDecl (String name, String value)
    throws SAXException
    {}
}

