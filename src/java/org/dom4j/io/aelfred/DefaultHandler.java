/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

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

import org.xml.sax.AttributeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.ext.DeclHandler;


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
//	{}
//
//    /** <b>SAX1</b> called at the end of an element */
//    public void endElement (String name)
//    throws SAXException
//	{}

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
