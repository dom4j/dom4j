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

//
// Copyright (c) 1997, 1998 by Microstar Software Ltd.
// From Microstar's README (the entire original license):
//
// AElfred is free for both commercial and non-commercial use and
// redistribution, provided that Microstar's copyright and disclaimer are
// retained intact.  You are free to modify AElfred for your own use and
// to redistribute AElfred with your modifications, provided that the
// modifications are clearly documented.
//
// This program is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of
// merchantability or fitness for a particular purpose.  Please use it AT
// YOUR OWN RISK.
//


package org.dom4j.io.aelfred;

import java.io.BufferedInputStream;
import java.io.CharConversionException;
import java.io.EOFException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.xml.sax.SAXException;


// $Id$

/**
 * Parse XML documents and return parse events through call-backs.
 * Use the <code>SAXDriver</code> class as your entry point, as the
 * internal parser interfaces are subject to change.
 *
 * @author Written by David Megginson &lt;dmeggins@microstar.com&gt;
 *	(version 1.2a with bugfixes)
 * @author Updated by David Brownell &lt;david-b@pacbell.net&gt;
 * @version $Date$
 * @see SAXDriver
 */
final class XmlParser
{
    //
    // Use special cheats that speed up the code by
    // avoiding per-character readCh () method calls.
    //
    private final static boolean USE_CHEATS = true;


    //////////////////////////////////////////////////////////////////////
    // Constructors.
    ////////////////////////////////////////////////////////////////////////


    /**
     * Construct a new parser with no associated handler.
     * @see #setHandler
     * @see #parse
     */
    // package private
    XmlParser ()
    {
	    cleanupVariables ();
    }


    /**
     * Set the handler that will receive parsing events.
     * @param handler The handler to receive callback events.
     * @see #parse
     */
    // package private
    void setHandler (SAXDriver handler)
    {
	    this.handler = handler;
    }


    /**
     * Parse an XML document from the character stream, byte stream, or URI
     * that you provide (in that order of preference).  Any URI that you
     * supply will become the base URI for resolving relative URI, and may
     * be used to acquire a reader or byte stream.
     *
     * <p>You may parse more than one document, but that must be done
     * sequentially.  Only one thread at a time may use this parser.
     *
     * @param systemId The URI of the document; should never be null,
     *	but may be so iff a reader <em>or</em> a stream is provided.
     * @param publicId The public identifier of the document, or null.
     * @param reader A character stream; must be null if stream isn't.
     * @param stream A byte input stream; must be null if reader isn't.
     * @param encoding The suggested encoding, or null if unknown.
     * @exception java.lang.Exception Basically SAXException or IOException
     */
    // package private 
    void doParse (
	String		systemId,
	String		publicId,
	Reader		reader,
	InputStream	stream,
	String		encoding
    ) throws Exception
    {
	if (handler == null)
	    throw new IllegalStateException ("no callback handler");

	basePublicId = publicId;
	baseURI = systemId;
	baseReader = reader;
	baseInputStream = stream;

	initializeVariables ();

	// predeclare the built-in entities here (replacement texts)
	// we don't need to intern(), since we're guaranteed literals
	// are always (globally) interned.
	setInternalEntity ("amp", "&#38;");
	setInternalEntity ("lt", "&#60;");
	setInternalEntity ("gt", "&#62;");
	setInternalEntity ("apos", "&#39;");
	setInternalEntity ("quot", "&#34;");

	handler.startDocument ();

	pushURL ("[document]", basePublicId, baseURI,
		baseReader, baseInputStream, encoding);

	try {
	    parseDocument ();
	    handler.endDocument ();
	} finally {
	    if (baseReader != null)
		try { baseReader.close ();
		} catch (IOException e) { /* ignore */ }
	    if (baseInputStream != null)
		try { baseInputStream.close ();
		} catch (IOException e) { /* ignore */ }
	    if (is != null)
		try { is.close ();
		} catch (IOException e) { /* ignore */ }
	    if (reader != null)
		try {
		    reader.close ();
		} catch (IOException e) { /* ignore */
		}
	    cleanupVariables ();
	}
    }


    ////////////////////////////////////////////////////////////////////////
    // Constants.
    ////////////////////////////////////////////////////////////////////////

    //
    // Constants for element content type.
    //

    /**
     * Constant: an element has not been declared.
     * @see #getElementContentType
     */
    public final static int CONTENT_UNDECLARED = 0;

    /**
     * Constant: the element has a content model of ANY.
     * @see #getElementContentType
     */
    public final static int CONTENT_ANY = 1;

    /**
     * Constant: the element has declared content of EMPTY.
     * @see #getElementContentType
     */
    public final static int CONTENT_EMPTY = 2;

    /**
     * Constant: the element has mixed content.
     * @see #getElementContentType
     */
    public final static int CONTENT_MIXED = 3;

    /**
     * Constant: the element has element content.
     * @see #getElementContentType
     */
    public final static int CONTENT_ELEMENTS = 4;


    //
    // Constants for the entity type.
    //

    /**
     * Constant: the entity has not been declared.
     * @see #getEntityType
     */
    public final static int ENTITY_UNDECLARED = 0;

    /**
     * Constant: the entity is internal.
     * @see #getEntityType
     */
    public final static int ENTITY_INTERNAL = 1;

    /**
     * Constant: the entity is external, non-XML data.
     * @see #getEntityType
     */
    public final static int ENTITY_NDATA = 2;

    /**
     * Constant: the entity is external XML data.
     * @see #getEntityType
     */
    public final static int ENTITY_TEXT = 3;


    //
    // Constants for attribute type.
    //

    /**
     * Constant: the attribute has not been declared for this element type.
     * @see #getAttributeType
     */
    public final static int ATTRIBUTE_UNDECLARED = 0;

    /**
     * Constant: the attribute value is a string value.
     * @see #getAttributeType
     */
    public final static int ATTRIBUTE_CDATA = 1;

    /**
     * Constant: the attribute value is a unique identifier.
     * @see #getAttributeType
     */
    public final static int ATTRIBUTE_ID = 2;

    /**
     * Constant: the attribute value is a reference to a unique identifier.
     * @see #getAttributeType
     */
    public final static int ATTRIBUTE_IDREF = 3;

    /**
     * Constant: the attribute value is a list of ID references.
     * @see #getAttributeType
     */
    public final static int ATTRIBUTE_IDREFS = 4;

    /**
     * Constant: the attribute value is the name of an entity.
     * @see #getAttributeType
     */
    public final static int ATTRIBUTE_ENTITY = 5;

    /**
     * Constant: the attribute value is a list of entity names.
     * @see #getAttributeType
     */
    public final static int ATTRIBUTE_ENTITIES = 6;

    /**
     * Constant: the attribute value is a name token.
     * @see #getAttributeType
     */
    public final static int ATTRIBUTE_NMTOKEN = 7;

    /**
     * Constant: the attribute value is a list of name tokens.
     * @see #getAttributeType
     */
    public final static int ATTRIBUTE_NMTOKENS = 8;

    /**
     * Constant: the attribute value is a token from an enumeration.
     * @see #getAttributeType
     */
    public final static int ATTRIBUTE_ENUMERATED = 9;

    /**
     * Constant: the attribute is the name of a notation.
     * @see #getAttributeType
     */
    public final static int ATTRIBUTE_NOTATION = 10;


    //
    // When the class is loaded, populate the hash table of
    // attribute types.
    //

    /**
     * Hash table of attribute types.
     */
    private static HashMap attributeTypeHash;
    static {
	attributeTypeHash = new HashMap (13);
	attributeTypeHash.put ("CDATA", new Integer (ATTRIBUTE_CDATA));
	attributeTypeHash.put ("ID", new Integer (ATTRIBUTE_ID));
	attributeTypeHash.put ("IDREF", new Integer (ATTRIBUTE_IDREF));
	attributeTypeHash.put ("IDREFS", new Integer (ATTRIBUTE_IDREFS));
	attributeTypeHash.put ("ENTITY", new Integer (ATTRIBUTE_ENTITY));
	attributeTypeHash.put ("ENTITIES", new Integer (ATTRIBUTE_ENTITIES));
	attributeTypeHash.put ("NMTOKEN", new Integer (ATTRIBUTE_NMTOKEN));
	attributeTypeHash.put ("NMTOKENS", new Integer (ATTRIBUTE_NMTOKENS));
	attributeTypeHash.put ("NOTATION", new Integer (ATTRIBUTE_NOTATION));
    }


    //
    // Constants for supported encodings.  "external" is just a flag.
    //
    private final static int ENCODING_EXTERNAL = 0;
    private final static int ENCODING_UTF_8 = 1;
    private final static int ENCODING_ISO_8859_1 = 2;
    private final static int ENCODING_UCS_2_12 = 3;
    private final static int ENCODING_UCS_2_21 = 4;
    private final static int ENCODING_UCS_4_1234 = 5;
    private final static int ENCODING_UCS_4_4321 = 6;
    private final static int ENCODING_UCS_4_2143 = 7;
    private final static int ENCODING_UCS_4_3412 = 8;
    private final static int ENCODING_ASCII = 9;


    //
    // Constants for attribute default value.
    //

    /**
     * Constant: the attribute is not declared.
     * @see #getAttributeDefaultValueType
     */
    public final static int ATTRIBUTE_DEFAULT_UNDECLARED = 30;

    /**
     * Constant: the attribute has a literal default value specified.
     * @see #getAttributeDefaultValueType
     * @see #getAttributeDefaultValue
     */
    public final static int ATTRIBUTE_DEFAULT_SPECIFIED = 31;

    /**
     * Constant: the attribute was declared #IMPLIED.
     * @see #getAttributeDefaultValueType
     */
    public final static int ATTRIBUTE_DEFAULT_IMPLIED = 32;

    /**
     * Constant: the attribute was declared #REQUIRED.
     * @see #getAttributeDefaultValueType
     */
    public final static int ATTRIBUTE_DEFAULT_REQUIRED = 33;

    /**
     * Constant: the attribute was declared #FIXED.
     * @see #getAttributeDefaultValueType
     * @see #getAttributeDefaultValue
     */
    public final static int ATTRIBUTE_DEFAULT_FIXED = 34;


    //
    // Constants for input.
    //
    private final static int INPUT_NONE = 0;
    private final static int INPUT_INTERNAL = 1;
    private final static int INPUT_EXTERNAL = 2;
    private final static int INPUT_STREAM = 3;
    private final static int INPUT_BUFFER = 4;
    private final static int INPUT_READER = 5;


    //
    // Flags for reading literals.
    //
	// expand general entity refs (attribute values in dtd and content)
    private final static int LIT_ENTITY_REF = 2;
	// normalize this value (whitespace etc) (attributes, public ids)
    private final static int LIT_NORMALIZE = 4;
	// literal is an attribute value 
    private final static int LIT_ATTRIBUTE = 8;
	// don't expand parameter entities
    private final static int LIT_DISABLE_PE = 16;
	// don't expand [or parse] character refs
    private final static int LIT_DISABLE_CREF = 32;
	// don't parse general entity refs
    private final static int LIT_DISABLE_EREF = 64;
	// don't expand general entities, but make sure we _could_
    private final static int LIT_ENTITY_CHECK = 128;


    //
    // Flags affecting PE handling in DTDs (if expandPE is true).
    // PEs expand with space padding, except inside literals.
    //
    private final static int CONTEXT_NORMAL = 0;
    private final static int CONTEXT_LITERAL = 1;


    //////////////////////////////////////////////////////////////////////
    // Error reporting.
    //////////////////////////////////////////////////////////////////////


    /**
     * Report an error.
     * @param message The error message.
     * @param textFound The text that caused the error (or null).
     * @see SAXDriver#error
     * @see #line
     */
    private void error (String message, String textFound, String textExpected)
    throws SAXException
    {
	if (textFound != null) {
	    message = message + " (found \"" + textFound + "\")";
	}
	if (textExpected != null) {
	    message = message + " (expected \"" + textExpected + "\")";
	}
	String uri = null;

	if (externalEntity != null) {
	    uri = externalEntity.getURL ().toString ();
	}
	handler.error (message, uri, line, column);

	// "can't happen"
	throw new SAXException (message);
    }


    /**
     * Report a serious error.
     * @param message The error message.
     * @param textFound The text that caused the error (or null).
     */
    private void error (String message, char textFound, String textExpected)
    throws SAXException
    {
	error (message, new Character (textFound).toString (), textExpected);
    }

    /** Report typical case fatal errors. */
    private void error (String message)
    throws SAXException
    {
	error (message, null, null);
    }


    //////////////////////////////////////////////////////////////////////
    // Major syntactic productions.
    //////////////////////////////////////////////////////////////////////


    /**
     * Parse an XML document.
     * <pre>
     * [1] document ::= prolog element Misc*
     * </pre>
     * <p>This is the top-level parsing function for a single XML
     * document.  As a minimum, a well-formed document must have
     * a document element, and a valid document must have a prolog
     * (one with doctype) as well.
     */
    private void parseDocument ()
    throws Exception
    {
    	char c;
        try {                                       // added by MHK
    	    parseProlog ();
    	    require ('<');
    	    parseElement ();
        } catch (EOFException ee) {                 // added by MHK
            error("premature end of file", "[EOF]", null);
        }
        
    	try {
    	    parseMisc ();   //skip all white, PIs, and comments
    	    c = readCh ();    //if this doesn't throw an exception...
    	    error ("unexpected characters after document end", c, null);
    	} catch (EOFException e) {
    	    return;
    	}
    }


    /**
     * Skip a comment.
     * <pre>
     * [15] Comment ::= '&lt;!--' ((Char - '-') | ('-' (Char - '-')))* "-->"
     * </pre>
     * <p> (The <code>&lt;!--</code> has already been read.)
     */
    private void parseComment ()
    throws Exception
    {
	char c;
	boolean saved = expandPE;

	expandPE = false;
	parseUntil ("--");
	require ('>');
	expandPE = saved;
	handler.comment (dataBuffer, 0, dataBufferPos);
	dataBufferPos = 0;
    }


    /**
     * Parse a processing instruction and do a call-back.
     * <pre>
     * [16] PI ::= '&lt;?' PITarget
     *		(S (Char* - (Char* '?&gt;' Char*)))?
     *		'?&gt;'
     * [17] PITarget ::= Name - ( ('X'|'x') ('M'|m') ('L'|l') )
     * </pre>
     * <p> (The <code>&lt;?</code> has already been read.)
     */
    private void parsePI ()
    throws SAXException, IOException
    {
	String name;
	boolean saved = expandPE;

	expandPE = false;
	name = readNmtoken (true);
	if ("xml".equalsIgnoreCase (name))
	    error ("Illegal processing instruction target", name, null);
	if (!tryRead ("?>")) {
	    requireWhitespace ();
	    parseUntil ("?>");
	}
	expandPE = saved;
	handler.processingInstruction (name, dataBufferToString ());
    }


    /**
     * Parse a CDATA section.
     * <pre>
     * [18] CDSect ::= CDStart CData CDEnd
     * [19] CDStart ::= '&lt;![CDATA['
     * [20] CData ::= (Char* - (Char* ']]&gt;' Char*))
     * [21] CDEnd ::= ']]&gt;'
     * </pre>
     * <p> (The '&lt;![CDATA[' has already been read.)
     */
    private void parseCDSect ()
    throws Exception
    {
	parseUntil ("]]>");
	dataBufferFlush ();
    }


    /**
     * Parse the prolog of an XML document.
     * <pre>
     * [22] prolog ::= XMLDecl? Misc* (Doctypedecl Misc*)?
     * </pre>
     * <p>There are a couple of tricks here.  First, it is necessary to
     * declare the XML default attributes after the DTD (if present)
     * has been read. [??]  Second, it is not possible to expand general
     * references in attribute value literals until after the entire
     * DTD (if present) has been parsed.
     * <p>We do not look for the XML declaration here, because it was
     * handled by pushURL ().
     * @see pushURL
     */
    private void parseProlog ()
    throws Exception
    {
	parseMisc ();

	if (tryRead ("<!DOCTYPE")) {
	    parseDoctypedecl ();
	    parseMisc ();
	}
    }


    /**
     * Parse the XML declaration.
     * <pre>
     * [23] XMLDecl ::= '&lt;?xml' VersionInfo EncodingDecl? SDDecl? S? '?&gt;'
     * [24] VersionInfo ::= S 'version' Eq
     *		("'" VersionNum "'" | '"' VersionNum '"' )
     * [26] VersionNum ::= ([a-zA-Z0-9_.:] | '-')*
     * [32] SDDecl ::= S 'standalone' Eq
     *		( "'"" ('yes' | 'no') "'"" | '"' ("yes" | "no") '"' )
     * [80] EncodingDecl ::= S 'encoding' Eq
     *		( "'" EncName "'" | "'" EncName "'" )
     * [81] EncName ::= [A-Za-z] ([A-Za-z0-9._] | '-')*
     * </pre>
     * <p> (The <code>&lt;?xml</code> and whitespace have already been read.)
     * @return the encoding in the declaration, uppercased; or null
     * @see #parseTextDecl
     * @see #setupDecoding
     */
    private String parseXMLDecl (boolean ignoreEncoding)
    throws SAXException, IOException
    {
	String	version;
	String	encodingName = null;
	String	standalone = null;
	boolean	white;
	int	flags = LIT_DISABLE_CREF | LIT_DISABLE_PE | LIT_DISABLE_EREF;

	// Read the version.
	require ("version");
	parseEq ();
	version = readLiteral (flags);
	if (!version.equals ("1.0")) {
	    error ("unsupported XML version", version, "1.0");
	}

	// Try reading an encoding declaration.
	white = tryWhitespace ();
	if (tryRead ("encoding")) {
	    if (!white)
		error ("whitespace required before 'encoding='");
	    parseEq ();
	    encodingName = readLiteral (flags);
	    if (!ignoreEncoding)
		setupDecoding (encodingName);
	}

	// Try reading a standalone declaration
	if (encodingName != null)
	    white = tryWhitespace ();
	if (tryRead ("standalone")) {
	    if (!white)
		error ("whitespace required before 'standalone='");
	    parseEq ();
	    standalone = readLiteral (flags);
	    if (! ("yes".equals (standalone) || "no".equals (standalone)))
		error ("standalone flag must be 'yes' or 'no'");
	}

	skipWhitespace ();
	require ("?>");

	return encodingName;
    }


    /**
     * Parse a text declaration.
     * <pre>
     * [79] TextDecl ::= '&lt;?xml' VersionInfo? EncodingDecl S? '?&gt;'
     * [80] EncodingDecl ::= S 'encoding' Eq
     *		( '"' EncName '"' | "'" EncName "'" )
     * [81] EncName ::= [A-Za-z] ([A-Za-z0-9._] | '-')*
     * </pre>
     * <p> (The <code>&lt;?xml</code>' and whitespace have already been read.)
     * @return the encoding in the declaration, uppercased; or null
     * @see #parseXMLDecl
     * @see #setupDecoding
     */
    private String parseTextDecl (boolean ignoreEncoding)
    throws SAXException, IOException
    {
	String	encodingName = null;
	int	flags = LIT_DISABLE_CREF | LIT_DISABLE_PE | LIT_DISABLE_EREF;

	// Read an optional version.
	if (tryRead ("version")) {
	    String version;
	    parseEq ();
	    version = readLiteral (flags);
	    if (!version.equals ("1.0")) {
		error ("unsupported XML version", version, "1.0");
	    }
	    requireWhitespace ();
	}


	// Read the encoding.
	require ("encoding");
	parseEq ();
	encodingName = readLiteral (flags);
	if (!ignoreEncoding)
	    setupDecoding (encodingName);

	skipWhitespace ();
	require ("?>");

	return encodingName;
    }


    /**
     * Sets up internal state so that we can decode an entity using the
     * specified encoding.  This is used when we start to read an entity
     * and we have been given knowledge of its encoding before we start to
     * read any data (e.g. from a SAX input source or from a MIME type).
     *
     * <p> It is also used after autodetection, at which point only very
     * limited adjustments to the encoding may be used (switching between
     * related builtin decoders).
     *
     * @param encodingName The name of the encoding specified by the user.
     * @exception IOException if the encoding isn't supported either
     *	internally to this parser, or by the hosting JVM.
     * @see #parseXMLDecl
     * @see #parseTextDecl
     */
    private void setupDecoding (String encodingName)
    throws SAXException, IOException
    {
	encodingName = encodingName.toUpperCase ();

	// ENCODING_EXTERNAL indicates an encoding that wasn't
	// autodetected ... we can use builtin decoders, or
	// ones from the JVM (InputStreamReader).

	// Otherwise we can only tweak what was autodetected, and
	// only for single byte (ASCII derived) builtin encodings.

	// ASCII-derived encodings
	if (encoding == ENCODING_UTF_8 || encoding == ENCODING_EXTERNAL) {
	    if (encodingName.equals ("ISO-8859-1")
		    || encodingName.equals ("8859_1")
		    || encodingName.equals ("ISO8859_1")
	      ) {
		encoding = ENCODING_ISO_8859_1;
		return;
	    } else if (encodingName.equals ("US-ASCII")
			|| encodingName.equals ("ASCII")) {
		encoding = ENCODING_ASCII;
		return;
	    } else if (encodingName.equals ("UTF-8")
			|| encodingName.equals ("UTF8")) {
		encoding = ENCODING_UTF_8;
		return;
	    } else if (encoding != ENCODING_EXTERNAL) {
		// fatal error
		error ("unsupported ASCII-derived encoding",
		       encodingName,
		       "UTF-8, US-ASCII, or ISO-8859-1");
	    }
	    // else fallthrough ...
	    // it's ASCII-ish and something other than a builtin
	}

	// Unicode and such
	if (encoding == ENCODING_UCS_2_12 || encoding == ENCODING_UCS_2_21) {
	    if (!(encodingName.equals ("ISO-10646-UCS-2")
		    || encodingName.equals ("UTF-16")
		    || encodingName.equals ("UTF-16BE")
		    || encodingName.equals ("UTF-16LE")))
		error ("unsupported Unicode encoding",
		       encodingName,
		       "UTF-16");
	    return;
	}

	// four byte encodings
	if (encoding == ENCODING_UCS_4_1234
		|| encoding == ENCODING_UCS_4_4321
		|| encoding == ENCODING_UCS_4_2143
		|| encoding == ENCODING_UCS_4_3412) {
	    if (!encodingName.equals ("ISO-10646-UCS-4"))
		error ("unsupported 32-bit encoding",
		       encodingName,
		       "ISO-10646-UCS-4");
	    return;
	}

	// assert encoding == ENCODING_EXTERNAL
	// if (encoding != ENCODING_EXTERNAL)
	//     throw new RuntimeException ("encoding = " + encoding);

	if (encodingName.equals ("UTF-16BE")) {
	    encoding = ENCODING_UCS_2_12;
	    return;
	}
	if (encodingName.equals ("UTF-16LE")) {
	    encoding = ENCODING_UCS_2_21;
	    return;
	}

	// We couldn't use the builtin decoders at all.  But we can try to
	// create a reader, since we haven't messed up buffering.  Tweak
	// the encoding name if necessary.

	if (encodingName.equals ("UTF-16")
		|| encodingName.equals ("ISO-10646-UCS-2"))
	    encodingName = "Unicode";
	// Ignoring all the EBCDIC aliases here

	reader = new InputStreamReader (is, encodingName);
	sourceType = INPUT_READER;
	is = null;
    }


    /**
     * Parse miscellaneous markup outside the document element and DOCTYPE
     * declaration.
     * <pre>
     * [27] Misc ::= Comment | PI | S
     * </pre>
     */
    private void parseMisc ()
    throws Exception
    {
	while (true) {
	    skipWhitespace ();
	    if (tryRead ("<?")) {
		parsePI ();
	    } else if (tryRead ("<!--")) {
		parseComment ();
	    } else {
		return;
	    }
	}
    }


    /**
     * Parse a document type declaration.
     * <pre>
     * [28] doctypedecl ::= '&lt;!DOCTYPE' S Name (S ExternalID)? S?
     *		('[' (markupdecl | PEReference | S)* ']' S?)? '&gt;'
     * </pre>
     * <p> (The <code>&lt;!DOCTYPE</code> has already been read.)
     */
    private void parseDoctypedecl ()
    throws Exception
    {
	char c;
	String doctypeName, ids[];

	// Read the document type name.
	requireWhitespace ();
	doctypeName = readNmtoken (true);

	// Read the External subset's IDs
	skipWhitespace ();
	ids = readExternalIds (false);

	// report (a) declaration of name, (b) lexical info (ids)
	handler.doctypeDecl (doctypeName, ids [0], ids [1]);

	// Internal subset is parsed first, if present
	skipWhitespace ();
	if (tryRead ('[')) {

	    // loop until the subset ends
	    while (true) {
		expandPE = true;
		skipWhitespace ();
		expandPE = false;
		if (tryRead (']')) {
		    break; 		// end of subset
		} else {
		    // WFC, PEs in internal subset (only between decls)
		    peIsError = expandPE = true;
		    parseMarkupdecl ();
		    peIsError = expandPE = false;
		}
	    }
	}

	// Read the external subset, if any
	if (ids [1] != null) {
	    pushURL ("[external subset]", ids [0], ids [1], null, null, null);

	    // Loop until we end up back at '>'
	    while (true) {
		expandPE = true;
		skipWhitespace ();
		expandPE = false;
		if (tryRead ('>')) {
		    break;
		} else {
		    expandPE = true;
		    parseMarkupdecl ();
		    expandPE = false;
		}
	    }
	} else {
	    // No external subset.
	    skipWhitespace ();
	    require ('>');
	}

	// done dtd
	handler.endDoctype ();
	expandPE = false;
    }


    /**
     * Parse a markup declaration in the internal or external DTD subset.
     * <pre>
     * [29] markupdecl ::= elementdecl | Attlistdecl | EntityDecl
     *		| NotationDecl | PI | Comment
     * [30] extSubsetDecl ::= (markupdecl | conditionalSect
     *		| PEReference | S) *
     * </pre>
     * <p> Reading toplevel PE references is handled as a lexical issue
     * by the caller, as is whitespace.
     */
    private void parseMarkupdecl ()
    throws Exception
    {
	if (tryRead ("<!ELEMENT")) {
	    parseElementdecl ();
	} else if (tryRead ("<!ATTLIST")) {
	    parseAttlistDecl ();
	} else if (tryRead ("<!ENTITY")) {
	    parseEntityDecl ();
	} else if (tryRead ("<!NOTATION")) {
	    parseNotationDecl ();
	} else if (tryRead ("<?")) {
	    parsePI ();
	} else if (tryRead ("<!--")) {
	    parseComment ();
	} else if (tryRead ("<![")) {
	    if (inputStack.size () > 0)
		parseConditionalSect ();
	    else
		error ("conditional sections illegal in internal subset");
	} else {
	    error ("expected markup declaration");
	}
    }


    /**
     * Parse an element, with its tags.
     * <pre>
     * [39] element ::= EmptyElementTag | STag content ETag
     * [40] STag ::= '&lt;' Name (S Attribute)* S? '&gt;'
     * [44] EmptyElementTag ::= '&lt;' Name (S Attribute)* S? '/&gt;'
     * </pre>
     * <p> (The '&lt;' has already been read.)
     * <p>NOTE: this method actually chains onto parseContent (), if necessary,
     * and parseContent () will take care of calling parseETag ().
     */
    private void parseElement ()
    throws Exception
    {
	String	gi;
	char	c;
	int	oldElementContent = currentElementContent;
	String	oldElement = currentElement;
	Object	element [];

	// This is the (global) counter for the
	// array of specified attributes.
	tagAttributePos = 0;

	// Read the element type name.
	gi = readNmtoken (true);

	// Determine the current content type.
	currentElement = gi;
	element = (Object []) elementInfo.get (gi);
	currentElementContent = getContentType (element, CONTENT_ANY);

	// Read the attributes, if any.
	// After this loop, "c" is the closing delimiter.
	boolean white = tryWhitespace ();
	c = readCh ();
	while (c != '/' && c != '>') {
	    unread (c);
	    if (!white)
		error ("need whitespace between attributes");
	    parseAttribute (gi);
	    white = tryWhitespace ();
	    c = readCh ();
	}

	// Supply any defaulted attributes.
	Iterator atts = declaredAttributes (element);
	if (atts != null) {
	    String aname;
loop:
	    while (atts.hasNext ()) {
		aname = (String) atts.next ();
		// See if it was specified.
		for (int i = 0; i < tagAttributePos; i++) {
		    if (tagAttributes [i] == aname) {
			continue loop;
		    }
		}
		// I guess not...
		handler.attribute (aname,
				   getAttributeExpandedValue (gi, aname),
				   false);
	    }
	}

	// Figure out if this is a start tag
	// or an empty element, and dispatch an
	// event accordingly.
	switch (c) {
	case '>':
	    handler.startElement (gi);
	    parseContent ();
	    break;
	case '/':
	    require ('>');
	    handler.startElement (gi);
	    handler.endElement (gi);
	    break;
	}

	// Restore the previous state.
	currentElement = oldElement;
	currentElementContent = oldElementContent;
    }


    /**
     * Parse an attribute assignment.
     * <pre>
     * [41] Attribute ::= Name Eq AttValue
     * </pre>
     * @param name The name of the attribute's element.
     * @see SAXDriver#attribute
     */
    private void parseAttribute (String name)
    throws Exception
    {
	String aname;
	int type;
	String value;
	int flags = LIT_ATTRIBUTE |  LIT_ENTITY_REF;

	// Read the attribute name.
	aname = readNmtoken (true);
	type = getAttributeType (name, aname);

	// Parse '='
	parseEq ();

	// Read the value, normalizing whitespace
	// unless it is CDATA.
	if (type == ATTRIBUTE_CDATA || type == ATTRIBUTE_UNDECLARED) {
	    value = readLiteral (flags);
	} else {
	    value = readLiteral (flags | LIT_NORMALIZE);
	}

	// WFC: no duplicate attributes
	for (int i = 0; i < tagAttributePos; i++)
	    if (aname.equals (tagAttributes [i]))
		error ("duplicate attribute", aname, null);

	// Inform the handler about the
	// attribute.
	handler.attribute (aname, value, true);
	dataBufferPos = 0;

	// Note that the attribute has been
	// specified.
	if (tagAttributePos == tagAttributes.length) {
	    String newAttrib[] = new String [tagAttributes.length * 2];
	    System.arraycopy (tagAttributes, 0, newAttrib, 0, tagAttributePos);
	    tagAttributes = newAttrib;
	}
	tagAttributes [tagAttributePos++] = aname;
    }


    /**
     * Parse an equals sign surrounded by optional whitespace.
     * <pre>
     * [25] Eq ::= S? '=' S?
     * </pre>
     */
    private void parseEq ()
    throws SAXException, IOException
    {
	skipWhitespace ();
	require ('=');
	skipWhitespace ();
    }


    /**
     * Parse an end tag.
     * <pre>
     * [42] ETag ::= '</' Name S? '>'
     * </pre>
     * <p>NOTE: parseContent () chains to here, we already read the
     * "&lt;/".
     */
    private void parseETag ()
    throws Exception
    {
	require (currentElement);
	skipWhitespace ();
	require ('>');
	handler.endElement (currentElement);
	// not re-reporting any SAXException re bogus end tags,
	// even though that diagnostic might be clearer ...
    }


    /**
     * Parse the content of an element.
     * <pre>
     * [43] content ::= (element | CharData | Reference
     *		| CDSect | PI | Comment)*
     * [67] Reference ::= EntityRef | CharRef
     * </pre>
     * <p> NOTE: consumes ETtag.
     */
    private void parseContent ()
    throws Exception
    {
	String data;
	char c;

	while (true) {
	    switch (currentElementContent) {
	        case CONTENT_ANY:
	        case CONTENT_MIXED:
	        case CONTENT_UNDECLARED:    // this line added by MHK 24 May 2000
	        case CONTENT_EMPTY:         // this line added by MHK 8 Sept 2000
		        parseCharData ();
		        break;
	        case CONTENT_ELEMENTS:
		        parseWhitespace ();
		        break;
	    }

	    // Handle delimiters
	    c = readCh ();
	    switch (c) {

	    case '&': 			// Found "&"

    		c = readCh ();
    		if (c == '#') {
    		    parseCharRef ();
    		} else {
    		    unread (c);
    		    parseEntityRef (true);
    		}
    		break;

	    case '<': 			// Found "<"
    		dataBufferFlush ();
    		c = readCh ();
    		switch (c) {
    		  case '!': 			// Found "<!"
    		    c = readCh ();
    		    switch (c) {
    		      case '-': 		// Found "<!-"
        			require ('-');
        			parseComment ();
        			break;
    		      case '[': 		// Found "<!["
        			require ("CDATA[");
        			handler.startCDATA ();
        			inCDATA = true;
        			parseCDSect ();
        			inCDATA = false;
        			handler.endCDATA ();
        			break;
    		      default:
        			error ("expected comment or CDATA section", c, null);
        			break;
    		    }
    		    break;

    		  case '?': 		// Found "<?"
    		    parsePI ();
    		    break;

    		  case '/': 		// Found "</"
    		    parseETag ();
    		    return;

    		  default: 		// Found "<" followed by something else
    		    unread (c);
    		    parseElement ();
    		    break;
    		}
	        }
	    }
    }


    /**
     * Parse an element type declaration.
     * <pre>
     * [45] elementdecl ::= '&lt;!ELEMENT' S Name S contentspec S? '&gt;'
     * </pre>
     * <p> NOTE: the '&lt;!ELEMENT' has already been read.
     */
    private void parseElementdecl ()
    throws Exception
    {
	String name;

	requireWhitespace ();
	// Read the element type name.
	name = readNmtoken (true);

	requireWhitespace ();
	// Read the content model.
	parseContentspec (name);

	skipWhitespace ();
	require ('>');
    }


    /**
     * Content specification.
     * <pre>
     * [46] contentspec ::= 'EMPTY' | 'ANY' | Mixed | elements
     * </pre>
     */
    private void parseContentspec (String name)
    throws Exception
    {
	if (tryRead ("EMPTY")) {
	    setElement (name, CONTENT_EMPTY, null, null);
	    return;
	} else if (tryRead ("ANY")) {
	    setElement (name, CONTENT_ANY, null, null);
	    return;
	} else {
	    require ('(');
	    dataBufferAppend ('(');
	    skipWhitespace ();
	    if (tryRead ("#PCDATA")) {
		dataBufferAppend ("#PCDATA");
		parseMixed ();
		setElement (name, CONTENT_MIXED, dataBufferToString (), null);
	    } else {
		parseElements ();
		setElement (name, CONTENT_ELEMENTS,
			dataBufferToString (), null);
	    }
	}
    }


    /**
     * Parse an element-content model.
     * <pre>
     * [47] elements ::= (choice | seq) ('?' | '*' | '+')?
     * [49] choice ::= '(' S? cp (S? '|' S? cp)+ S? ')'
     * [50] seq ::= '(' S? cp (S? ',' S? cp)* S? ')'
     * </pre>
     *
     * <p> NOTE: the opening '(' and S have already been read.
     */
    private void parseElements ()
    throws Exception
    {
	char c;
	char sep;

	// Parse the first content particle
	skipWhitespace ();
	parseCp ();

	// Check for end or for a separator.
	skipWhitespace ();
	c = readCh ();
	switch (c) {
	case ')':
	    dataBufferAppend (')');
	    c = readCh ();
	    switch (c) {
	    case '*':
	    case '+':
	    case '?':
		dataBufferAppend (c);
		break;
	    default:
		unread (c);
	    }
	    return;
	case ',': 			// Register the separator.
	case '|':
	    sep = c;
	    dataBufferAppend (c);
	    break;
	default:
	    error ("bad separator in content model", c, null);
	    return;
	}

	// Parse the rest of the content model.
	while (true) {
	    skipWhitespace ();
	    parseCp ();
	    skipWhitespace ();
	    c = readCh ();
	    if (c == ')') {
		dataBufferAppend (')');
		break;
	    } else if (c != sep) {
		error ("bad separator in content model", c, null);
		return;
	    } else {
		dataBufferAppend (c);
	    }
	}

	// Check for the occurrence indicator.
	c = readCh ();
	switch (c) {
	case '?':
	case '*':
	case '+':
	    dataBufferAppend (c);
	    return;
	default:
	    unread (c);
	    return;
	}
    }


    /**
     * Parse a content particle.
     * <pre>
     * [48] cp ::= (Name | choice | seq) ('?' | '*' | '+')?
     * </pre>
     */
    private void parseCp ()
    throws Exception
    {
	char c;

	if (tryRead ('(')) {
	    dataBufferAppend ('(');
	    parseElements ();
	} else {
	    dataBufferAppend (readNmtoken (true));
	    c = readCh ();
	    switch (c) {
	    case '?':
	    case '*':
	    case '+':
		dataBufferAppend (c);
		break;
	    default:
		unread (c);
		break;
	    }
	}
    }


    /**
     * Parse mixed content.
     * <pre>
     * [51] Mixed ::= '(' S? ( '#PCDATA' (S? '|' S? Name)*) S? ')*'
     *	      | '(' S? ('#PCDATA') S? ')'
     * </pre>
     */
    private void parseMixed ()
    throws Exception
    {
	char c;

	// Check for PCDATA alone.
	skipWhitespace ();
	if (tryRead (')')) {
	    dataBufferAppend (")*");
	    tryRead ('*');
	    return;
	}

	// Parse mixed content.
	skipWhitespace ();
	while (!tryRead (")*")) {
	    require ('|');
	    dataBufferAppend ('|');
	    skipWhitespace ();
	    dataBufferAppend (readNmtoken (true));
	    skipWhitespace ();
	}
	dataBufferAppend (")*");
    }


    /**
     * Parse an attribute list declaration.
     * <pre>
     * [52] AttlistDecl ::= '&lt;!ATTLIST' S Name AttDef* S? '&gt;'
     * </pre>
     * <p>NOTE: the '&lt;!ATTLIST' has already been read.
     */
    private void parseAttlistDecl ()
    throws Exception
    {
	String elementName;

	requireWhitespace ();
	elementName = readNmtoken (true);
	boolean white = tryWhitespace ();
	while (!tryRead ('>')) {
	    if (!white)
		error ("whitespace required before attribute definition");
	    parseAttDef (elementName);
	    white = tryWhitespace ();
	}
    }


    /**
     * Parse a single attribute definition.
     * <pre>
     * [53] AttDef ::= S Name S AttType S DefaultDecl
     * </pre>
     */
    private void parseAttDef (String elementName)
    throws Exception
    {
	String name;
	int type;
	String enum = null;

	// Read the attribute name.
	name = readNmtoken (true);

	// Read the attribute type.
	requireWhitespace ();
	type = readAttType ();

	// Get the string of enumerated values
	// if necessary.
	if (type == ATTRIBUTE_ENUMERATED || type == ATTRIBUTE_NOTATION) {
	    enum = dataBufferToString ();
	}

	// Read the default value.
	requireWhitespace ();
	parseDefault (elementName, name, type, enum);
    }


    /**
     * Parse the attribute type.
     * <pre>
     * [54] AttType ::= StringType | TokenizedType | EnumeratedType
     * [55] StringType ::= 'CDATA'
     * [56] TokenizedType ::= 'ID' | 'IDREF' | 'IDREFS' | 'ENTITY'
     *		| 'ENTITIES' | 'NMTOKEN' | 'NMTOKENS'
     * [57] EnumeratedType ::= NotationType | Enumeration
     * </pre>
     */
    private int readAttType ()
    throws Exception
    {
	String typeString;
	Integer type;

	if (tryRead ('(')) {
	    parseEnumeration (false);
	    return ATTRIBUTE_ENUMERATED;
	} else {
	    typeString = readNmtoken (true);
	    if (typeString.equals ("NOTATION")) {
		parseNotationType ();
	    }
	    type = (Integer) attributeTypeHash.get (typeString);
	    if (type == null) {
		error ("illegal attribute type", typeString, null);
		return ATTRIBUTE_UNDECLARED;
	    } else {
		return type.intValue ();
	    }
	}
    }


    /**
     * Parse an enumeration.
     * <pre>
     * [59] Enumeration ::= '(' S? Nmtoken (S? '|' S? Nmtoken)* S? ')'
     * </pre>
     * <p>NOTE: the '(' has already been read.
     */
    private void parseEnumeration (boolean isNames)
    throws Exception
    {
	char c;

	dataBufferAppend ('(');

	// Read the first token.
	skipWhitespace ();
	dataBufferAppend (readNmtoken (isNames));
	// Read the remaining tokens.
	skipWhitespace ();
	while (!tryRead (')')) {
	    require ('|');
	    dataBufferAppend ('|');
	    skipWhitespace ();
	    dataBufferAppend (readNmtoken (isNames));
	    skipWhitespace ();
	}
	dataBufferAppend (')');
    }


    /**
     * Parse a notation type for an attribute.
     * <pre>
     * [58] NotationType ::= 'NOTATION' S '(' S? NameNtoks
     *		(S? '|' S? name)* S? ')'
     * </pre>
     * <p>NOTE: the 'NOTATION' has already been read
     */
    private void parseNotationType ()
    throws Exception
    {
	requireWhitespace ();
	require ('(');

	parseEnumeration (true);
    }


    /**
     * Parse the default value for an attribute.
     * <pre>
     * [60] DefaultDecl ::= '#REQUIRED' | '#IMPLIED'
     *		| (('#FIXED' S)? AttValue)
     * </pre>
     */
    private void parseDefault (
	String elementName,
	String name,
	int type,
	String enum
    ) throws Exception
    {
	int	valueType = ATTRIBUTE_DEFAULT_SPECIFIED;
	String	value = null;
	int	flags = LIT_ATTRIBUTE | LIT_DISABLE_CREF | LIT_ENTITY_CHECK;

	// Note: char refs not checked here, and input not normalized,
	// since it's done correctly later when we actually expand any
	// entity refs.  We ought to report char ref syntax errors now,
	// but don't.  Cost: unused defaults mean unreported WF errs.
	
	// LIT_ATTRIBUTE forces '<' checks now (ASAP) and turns whitespace
	// chars to spaces (doesn't matter when that's done if it doesn't
	// interfere with char refs expanding to whitespace).

	if (tryRead ('#')) {
	    if (tryRead ("FIXED")) {
		valueType = ATTRIBUTE_DEFAULT_FIXED;
		requireWhitespace ();
		value = readLiteral (flags);
	    } else if (tryRead ("REQUIRED")) {
		valueType = ATTRIBUTE_DEFAULT_REQUIRED;
	    } else if (tryRead ("IMPLIED")) {
		valueType = ATTRIBUTE_DEFAULT_IMPLIED;
	    } else {
		error ("illegal keyword for attribute default value");
	    }
	} else
	    value = readLiteral (flags);
	setAttribute (elementName, name, type, enum, value, valueType);
    }


    /**
     * Parse a conditional section.
     * <pre>
     * [61] conditionalSect ::= includeSect || ignoreSect
     * [62] includeSect ::= '&lt;![' S? 'INCLUDE' S? '['
     *		extSubsetDecl ']]&gt;'
     * [63] ignoreSect ::= '&lt;![' S? 'IGNORE' S? '['
     *		ignoreSectContents* ']]&gt;'
     * [64] ignoreSectContents ::= Ignore
     *		('&lt;![' ignoreSectContents* ']]&gt;' Ignore )*
     * [65] Ignore ::= Char* - (Char* ( '&lt;![' | ']]&gt;') Char* )
     * </pre>
     * <p> NOTE: the '&gt;![' has already been read.
     */
    private void parseConditionalSect ()
    throws Exception
    {
	skipWhitespace ();
	if (tryRead ("INCLUDE")) {
	    skipWhitespace ();
	    require ('[');
	    skipWhitespace ();
	    while (!tryRead ("]]>")) {
		parseMarkupdecl ();
		skipWhitespace ();
	    }
	} else if (tryRead ("IGNORE")) {
	    skipWhitespace ();
	    require ('[');
	    int nesting = 1;
	    char c;
	    expandPE = false;
	    for (int nest = 1; nest > 0;) {
		c = readCh ();
		switch (c) {
		case '<':
		    if (tryRead ("![")) {
			nest++;
		    }
		case ']':
		    if (tryRead ("]>")) {
			nest--;
		    }
		}
	    }
	    expandPE = true;
	} else {
	    error ("conditional section must begin with INCLUDE or IGNORE");
	}
    }


    /**
     * Read and interpret a character reference.
     * <pre>
     * [66] CharRef ::= '&#' [0-9]+ ';' | '&#x' [0-9a-fA-F]+ ';'
     * </pre>
     * <p>NOTE: the '&#' has already been read.
     */
    private void parseCharRef ()
    throws SAXException, IOException
    {
	int value = 0;
	char c;

	if (tryRead ('x')) {
loop1:
	    while (true) {
		c = readCh ();
		switch (c) {
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
		case 'a':
		case 'A':
		case 'b':
		case 'B':
		case 'c':
		case 'C':
		case 'd':
		case 'D':
		case 'e':
		case 'E':
		case 'f':
		case 'F':
		    value *= 16;
		    value += Integer.parseInt (new Character (c).toString (),
				    16);
		    break;
		case ';':
		    break loop1;
		default:
		    error ("illegal character in character reference", c, null);
		    break loop1;
		}
	    }
	} else {
loop2:
	    while (true) {
		c = readCh ();
		switch (c) {
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
		    value *= 10;
		    value += Integer.parseInt (new Character (c).toString (),
				    10);
		    break;
		case ';':
		    break loop2;
		default:
		    error ("illegal character in character reference", c, null);
		    break loop2;
		}
	    }
	}

	// check for character refs being legal XML
	if ((value < 0x0020
		&& ! (value == '\n' || value == '\t' || value == '\r'))
		|| (value >= 0xD800 && value <= 0xDFFF)
		|| value == 0xFFFE || value == 0xFFFF
		|| value > 0x0010ffff)
	    error ("illegal XML character reference U+"
		    + Integer.toHexString (value));

	// Check for surrogates: 00000000 0000xxxx yyyyyyyy zzzzzzzz
	//  (1101|10xx|xxyy|yyyy + 1101|11yy|zzzz|zzzz:
	if (value <= 0x0000ffff) {
	    // no surrogates needed
	    dataBufferAppend ((char) value);
	} else if (value <= 0x0010ffff) {
	    value -= 0x10000;
	    // > 16 bits, surrogate needed
	    dataBufferAppend ((char) (0xd800 | (value >> 10)));
	    dataBufferAppend ((char) (0xdc00 | (value & 0x0003ff)));
	} else {
	    // too big for surrogate
	    error ("character reference " + value + " is too large for UTF-16",
		   new Integer (value).toString (), null);
	}
    }


    /**
     * Parse and expand an entity reference.
     * <pre>
     * [68] EntityRef ::= '&' Name ';'
     * </pre>
     * <p>NOTE: the '&amp;' has already been read.
     * @param externalAllowed External entities are allowed here.
     */
    private void parseEntityRef (boolean externalAllowed)
    throws SAXException, IOException
    {
	String name;

	name = readNmtoken (true);
	require (';');
	switch (getEntityType (name)) {
	case ENTITY_UNDECLARED:
	    error ("reference to undeclared entity", name, null);
	    break;
	case ENTITY_INTERNAL:
	    pushString (name, getEntityValue (name));
	    break;
	case ENTITY_TEXT:
	    if (externalAllowed) {
		pushURL (name, getEntityPublicId (name),
			 getEntitySystemId (name),
			 null, null, null);
	    } else {
		error ("reference to external entity in attribute value.",
			name, null);
	    }
	    break;
	case ENTITY_NDATA:
	    if (externalAllowed) {
		error ("unparsed entity reference in content", name, null);
	    } else {
		error ("reference to external entity in attribute value.",
			name, null);
	    }
	    break;
	}
    }


    /**
     * Parse and expand a parameter entity reference.
     * <pre>
     * [69] PEReference ::= '%' Name ';'
     * </pre>
     * <p>NOTE: the '%' has already been read.
     */
    private void parsePEReference ()
    throws SAXException, IOException
    {
	String name;

	name = "%" + readNmtoken (true);
	require (';');
	switch (getEntityType (name)) {
	case ENTITY_UNDECLARED:
	    // this is a validity problem, not a WFC violation ... but
	    // we should disable handling of all subsequent declarations
	    // unless this is a standalone document
	    // warn ("reference to undeclared parameter entity", name, null);

	    break;
	case ENTITY_INTERNAL:
	    if (inLiteral)
		pushString (name, getEntityValue (name));
	    else
		pushString (name, " " + getEntityValue (name) + ' ');
	    break;
	case ENTITY_TEXT:
	    if (!inLiteral)
		pushString (null, " ");
	    pushURL (name, getEntityPublicId (name),
		     getEntitySystemId (name),
		     null, null, null);
	    if (!inLiteral)
		pushString (null, " ");
	    break;
	}
    }

    /**
     * Parse an entity declaration.
     * <pre>
     * [70] EntityDecl ::= GEDecl | PEDecl
     * [71] GEDecl ::= '&lt;!ENTITY' S Name S EntityDef S? '&gt;'
     * [72] PEDecl ::= '&lt;!ENTITY' S '%' S Name S PEDef S? '&gt;'
     * [73] EntityDef ::= EntityValue | (ExternalID NDataDecl?)
     * [74] PEDef ::= EntityValue | ExternalID
     * [75] ExternalID ::= 'SYSTEM' S SystemLiteral
     *		   | 'PUBLIC' S PubidLiteral S SystemLiteral
     * [76] NDataDecl ::= S 'NDATA' S Name
     * </pre>
     * <p>NOTE: the '&lt;!ENTITY' has already been read.
     */
    private void parseEntityDecl ()
    throws Exception
    {
	char c;
	boolean peFlag = false;
	String name, value, notationName, ids[];

	// Check for a parameter entity.
	expandPE = false;
	requireWhitespace ();
	if (tryRead ('%')) {
	    peFlag = true;
	    requireWhitespace ();
	}
	expandPE = true;

	// Read the entity name, and prepend
	// '%' if necessary.
	name = readNmtoken (true);
	if (peFlag) {
	    name = "%" + name;
	}

	// Read the entity value.
	requireWhitespace ();
	c = readCh ();
	unread (c);
	if (c == '"' || c == '\'') {
	    // Internal entity ... replacement text has expanded refs
	    // to characters and PEs, but not to general entities
	    value = readLiteral (0);
	    setInternalEntity (name, value);
	} else {
	    // Read the external IDs
	    ids = readExternalIds (false);
	    if (ids [1] == null) {
		error ("system identifer missing", name, null);
	    }

	    // Check for NDATA declaration.
	    boolean white = tryWhitespace ();
	    if (!peFlag && tryRead ("NDATA")) {
		if (!white)
		    error ("whitespace required before NDATA");
		requireWhitespace ();
		notationName = readNmtoken (true);
		setExternalDataEntity (name, ids [0], ids [1], notationName);
	    } else {
		setExternalTextEntity (name, ids [0], ids [1]);
	    }
	}

	// Finish the declaration.
	skipWhitespace ();
	require ('>');
    }


    /**
     * Parse a notation declaration.
     * <pre>
     * [82] NotationDecl ::= '&lt;!NOTATION' S Name S
     *		(ExternalID | PublicID) S? '&gt;'
     * [83] PublicID ::= 'PUBLIC' S PubidLiteral
     * </pre>
     * <P>NOTE: the '&lt;!NOTATION' has already been read.
     */
    private void parseNotationDecl ()
    throws Exception
    {
	String nname, ids[];


	requireWhitespace ();
	nname = readNmtoken (true);

	requireWhitespace ();

	// Read the external identifiers.
	ids = readExternalIds (true);
	if (ids [0] == null && ids [1] == null) {
	    error ("external identifer missing", nname, null);
	}

	// Register the notation.
	setNotation (nname, ids [0], ids [1]);

	skipWhitespace ();
	require ('>');
    }


    /**
     * Parse character data.
     * <pre>
     * [14] CharData ::= [^&lt;&amp;]* - ([^&lt;&amp;]* ']]&gt;' [^&lt;&amp;]*)
     * </pre>
     */
    private void parseCharData ()
    throws Exception
    {
	char c;

	// Start with a little cheat -- in most
	// cases, the entire sequence of
	// character data will already be in
	// the readBuffer; if not, fall through to
	// the normal approach.
	if (USE_CHEATS) {
	    int lineAugment = 0;
	    int columnAugment = 0;

loop:
	    for (int i = readBufferPos; i < readBufferLength; i++) {
		switch (c = readBuffer [i]) {
		case '\n':
		    lineAugment++;
		    columnAugment = 0;
		    break;
		case '&':
		case '<':
		    int start = readBufferPos;
		    columnAugment++;
		    readBufferPos = i;
		    if (lineAugment > 0) {
			line += lineAugment;
			column = columnAugment;
		    } else {
			column += columnAugment;
		    }
		    dataBufferAppend (readBuffer, start, i - start);
		    return;
		case ']':
		    // XXX missing two end-of-buffer cases
		    if ((i + 2) < readBufferLength) {
			if (readBuffer [i + 1] == ']'
				&& readBuffer [i + 2] == '>') {
			    error ("character data may not contain ']]>'");
			}
		    }
		    columnAugment++;
		    break;
		default:
		    if (c < 0x0020 || c > 0xFFFD)
			error ("illegal XML character U+"
				+ Integer.toHexString (c));
		    // FALLTHROUGH
		case '\r':
		case '\t':
		    columnAugment++;
		}
	    }
	}

	// OK, the cheat didn't work; start over
	// and do it by the book.
	while (true) {
	    c = readCh ();
	    switch (c) {
	    case '<':
	    case '&':
		unread (c);
		return;
	    // XXX "]]>" precluded ...
	    default:
		dataBufferAppend (c);
		break;
	    }
	}
    }


    //////////////////////////////////////////////////////////////////////
    // High-level reading and scanning methods.
    //////////////////////////////////////////////////////////////////////

    /**
     * Require whitespace characters.
     */
    private void requireWhitespace ()
    throws SAXException, IOException
    {
	char c = readCh ();
	if (isWhitespace (c)) {
	    skipWhitespace ();
	} else {
	    error ("whitespace required", c, null);
	}
    }


    /**
     * Parse whitespace characters, and leave them in the data buffer.
     */
    private void parseWhitespace ()
    throws Exception
    {
	char c = readCh ();
	while (isWhitespace (c)) {
	    dataBufferAppend (c);
	    c = readCh ();
	}
	unread (c);
    }


    /**
     * Skip whitespace characters.
     * <pre>
     * [3] S ::= (#x20 | #x9 | #xd | #xa)+
     * </pre>
     */
    private void skipWhitespace ()
    throws SAXException, IOException
    {
	// Start with a little cheat.  Most of
	// the time, the white space will fall
	// within the current read buffer; if
	// not, then fall through.
	if (USE_CHEATS) {
	    int lineAugment = 0;
	    int columnAugment = 0;

loop:
	    for (int i = readBufferPos; i < readBufferLength; i++) {
		switch (readBuffer [i]) {
		case ' ':
		case '\t':
		case '\r':
		    columnAugment++;
		    break;
		case '\n':
		    lineAugment++;
		    columnAugment = 0;
		    break;
		case '%':
		    if (expandPE)
			break loop;
		    // else fall through...
		default:
		    readBufferPos = i;
		    if (lineAugment > 0) {
			line += lineAugment;
			column = columnAugment;
		    } else {
			column += columnAugment;
		    }
		    return;
		}
	    }
	}

	// OK, do it by the book.
	char c = readCh ();
	while (isWhitespace (c)) {
	    c = readCh ();
	}
	unread (c);
    }


    /**
     * Read a name or (when parsing an enumeration) name token.
     * <pre>
     * [5] Name ::= (Letter | '_' | ':') (NameChar)*
     * [7] Nmtoken ::= (NameChar)+
     * </pre>
     */
    private String readNmtoken (boolean isName)
    throws SAXException, IOException
    {
	char c;

	if (USE_CHEATS) {
loop:
	    for (int i = readBufferPos; i < readBufferLength; i++) {
		c = readBuffer [i];
		switch (c) {
		  case '%':
		    if (expandPE)
			break loop;
		    // else fall through...

		    // What may legitimately come AFTER a name/nmtoken?
		  case '<': case '>': case '&':
		  case ',': case '|': case '*': case '+': case '?':
		  case ')':
		  case '=':
		  case '\'': case '"':
		  case '[':
		  case ' ': case '\t': case '\r': case '\n':
		  case ';':
		  case '/':
		    int start = readBufferPos;
		    if (i == start)
			error ("name expected", readBuffer [i], null);
		    readBufferPos = i;
		    return intern (readBuffer, start, i - start);

		  default:
		    // punt on exact tests from Appendix A; approximate
		    // them using the Unicode ID start/part rules
		    if (i == readBufferPos && isName) {
			if (!Character.isUnicodeIdentifierStart (c)
				&& c != ':' && c != '_')
			    error ("Not a name start character, U+"
				  + Integer.toHexString (c));
		    } else if (!Character.isUnicodeIdentifierPart (c)
			    && c != '-' && c != ':' && c != '_' && c != '.'
			    && !isExtender (c))
			error ("Not a name character, U+"
				+ Integer.toHexString (c));
		}
	    }
	}

	nameBufferPos = 0;

	// Read the first character.
loop:
	while (true) {
	    c = readCh ();
	    switch (c) {
	    case '%':
	    case '<': case '>': case '&':
	    case ',': case '|': case '*': case '+': case '?':
	    case ')':
	    case '=':
	    case '\'': case '"':
	    case '[':
	    case ' ': case '\t': case '\n': case '\r':
	    case ';':
	    case '/':
		unread (c);
		if (nameBufferPos == 0) {
		    error ("name expected");
		}
		// punt on exact tests from Appendix A, but approximate them
		if (isName
			&& !Character.isUnicodeIdentifierStart (
				nameBuffer [0])
			&& ":_".indexOf (nameBuffer [0]) == -1)
		    error ("Not a name start character, U+"
			      + Integer.toHexString (nameBuffer [0]));
		String s = intern (nameBuffer, 0, nameBufferPos);
		nameBufferPos = 0;
		return s;
	    default:
		// punt on exact tests from Appendix A, but approximate them

		if ((nameBufferPos != 0 || !isName)
			&& !Character.isUnicodeIdentifierPart (c)
			&& ":-_.".indexOf (c) == -1
			&& !isExtender (c))
		    error ("Not a name character, U+"
			    + Integer.toHexString (c));
		if (nameBufferPos >= nameBuffer.length)
		    nameBuffer =
			(char[]) extendArray (nameBuffer,
				    nameBuffer.length, nameBufferPos);
		nameBuffer [nameBufferPos++] = c;
	    }
	}
    }

    private static boolean isExtender (char c)
    {
	// [88] Extender ::= ...
	return c == 0x00b7 || c == 0x02d0 || c == 0x02d1 || c == 0x0387
	       || c == 0x0640 || c == 0x0e46 || c == 0x0ec6 || c == 0x3005
	       || (c >= 0x3031 && c <= 0x3035)
	       || (c >= 0x309d && c <= 0x309e)
	       || (c >= 0x30fc && c <= 0x30fe);
    }


    /**
     * Read a literal.  With matching single or double quotes as
     * delimiters (and not embedded!) this is used to parse:
     * <pre>
     *	[9] EntityValue ::= ... ([^%&amp;] | PEReference | Reference)* ...
     *	[10] AttValue ::= ... ([^<&] | Reference)* ...
     *	[11] SystemLiteral ::= ... (URLchar - "'")* ...
     *	[12] PubidLiteral ::= ... (PubidChar - "'")* ...
     * </pre>
     * as well as the quoted strings in XML and text declarations
     * (for version, encoding, and standalone) which have their
     * own constraints.
     */
    private String readLiteral (int flags)
    throws SAXException, IOException
    {
	char	delim, c;
	int	startLine = line;
	boolean	saved = expandPE;

	// Find the first delimiter.
	delim = readCh ();
	if (delim != '"' && delim != '\'' && delim != (char) 0) {
	    error ("expected '\"' or \"'\"", delim, null);
	    return null;
	}
	inLiteral = true;
	if ((flags & LIT_DISABLE_PE) != 0)
	    expandPE = false;

	// Each level of input source has its own buffer; remember
	// ours, so we won't read the ending delimiter from any
	// other input source, regardless of entity processing.
	char ourBuf [] = readBuffer;

	// Read the literal.
	try {
	    c = readCh ();
loop:
	    while (! (c == delim && readBuffer == ourBuf)) {
		switch (c) {
		    // Can't escape this normalization for attributes
		case '\n':
		case '\r':
		case '\t':
		    if ((flags & LIT_ATTRIBUTE) != 0)
			c = ' ';
		    break;
		case '&':
		    c = readCh ();
		    // Char refs are expanded immediately, except for
		    // all the cases where it's deferred.
		    if (c == '#') {
			if ((flags & LIT_DISABLE_CREF) != 0) {
			    dataBufferAppend ('&');
			    dataBufferAppend ('#');
			    continue;
			}
			parseCharRef ();

		    // It looks like an entity ref ...
		    } else {
			unread (c);
			// Expand it?
			if ((flags & LIT_ENTITY_REF) > 0) {
			    parseEntityRef (false);

			// Is it just data?
			} else if ((flags & LIT_DISABLE_EREF) != 0) {
			    dataBufferAppend ('&');

			// OK, it will be an entity ref -- expanded later.
			} else {
			    String name = readNmtoken (true);
			    require (';');
			    if ((flags & LIT_ENTITY_CHECK) != 0
				    && getEntityType (name) ==
					    ENTITY_UNDECLARED) {
				error ("General entity '" + name
				    + "' must be declared before use");
			    }
			    dataBufferAppend ('&');
			    dataBufferAppend (name);
			    dataBufferAppend (';');
			}
		    }
		    c = readCh ();
		    continue loop;

		case '<':
		    // and why?  Perhaps so "&foo;" expands the same
		    // inside and outside an attribute?
		    if ((flags & LIT_ATTRIBUTE) != 0)
			error ("attribute values may not contain '<'");
		    break;

		// We don't worry about case '%' and PE refs, readCh does.

		default:
		    break;
		}
		dataBufferAppend (c);
		c = readCh ();
	    }
	} catch (EOFException e) {
	    error ("end of input while looking for delimiter (started on line "
		   + startLine + ')', null, new Character (delim).toString ());
	}
	inLiteral = false;
	expandPE = saved;

	// Normalise whitespace if necessary.
	if ((flags & LIT_NORMALIZE) > 0) {
	    dataBufferNormalize ();
	}

	// Return the value.
	return dataBufferToString ();
    }


    /**
     * Try reading external identifiers.
     * A system identifier is not required for notations.
     * @param inNotation Are we in a notation?
     * @return A two-member String array containing the identifiers.
     */
    private String[] readExternalIds (boolean inNotation)
    throws Exception
    {
	char	c;
	String	ids[] = new String [2];
	int	flags = LIT_DISABLE_CREF | LIT_DISABLE_PE | LIT_DISABLE_EREF;

	if (tryRead ("PUBLIC")) {
	    requireWhitespace ();
	    ids [0] = readLiteral (LIT_NORMALIZE | flags);
	    if (inNotation) {
		skipWhitespace ();
		c = readCh ();
		unread (c);
		if (c == '"' || c == '\'') {
		    ids [1] = readLiteral (flags);
		}
	    } else {
		requireWhitespace ();
		ids [1] = readLiteral (flags);
	    }

	    for (int i = 0; i < ids [0].length (); i++) {
		c = ids [0].charAt (i);
		if (c >= 'a' && c <= 'z')
		    continue;
		if (c >= 'A' && c <= 'Z')
		    continue;
		if (" \r\n0123456789-' ()+,./:=?;!*#@$_%".indexOf (c) != -1)
		    continue;
		error ("illegal PUBLIC id character U+"
			+ Integer.toHexString (c));
	    }
	} else if (tryRead ("SYSTEM")) {
	    requireWhitespace ();
	    ids [1] = readLiteral (flags);
	}

	// XXX should normalize system IDs as follows:
	// - Convert to UTF-8
	// - Map reserved and non-ASCII characters to %HH

	return ids;
    }


    /**
     * Test if a character is whitespace.
     * <pre>
     * [3] S ::= (#x20 | #x9 | #xd | #xa)+
     * </pre>
     * @param c The character to test.
     * @return true if the character is whitespace.
     */
    private final boolean isWhitespace (char c)
    {
	if (c > 0x20)
	    return false;
	if (c == 0x20 || c == 0x0a || c == 0x09 || c == 0x0d)
	    return true;
	return false;	// illegal ...
    }


    //////////////////////////////////////////////////////////////////////
    // Utility routines.
    //////////////////////////////////////////////////////////////////////


    /**
     * Add a character to the data buffer.
     */
    private void dataBufferAppend (char c)
    {
	// Expand buffer if necessary.
	if (dataBufferPos >= dataBuffer.length)
	    dataBuffer =
		(char[]) extendArray (dataBuffer,
			dataBuffer.length, dataBufferPos);
	dataBuffer [dataBufferPos++] = c;
    }


    /**
     * Add a string to the data buffer.
     */
    private void dataBufferAppend (String s)
    {
	dataBufferAppend (s.toCharArray (), 0, s.length ());
    }


    /**
     * Append (part of) a character array to the data buffer.
     */
    private void dataBufferAppend (char ch[], int start, int length)
    {
	dataBuffer = (char[])
		extendArray (dataBuffer, dataBuffer.length,
				    dataBufferPos + length);

	System.arraycopy (ch, start, dataBuffer, dataBufferPos, length);
	dataBufferPos += length;
    }


    /**
     * Normalise whitespace in the data buffer.
     */
    private void dataBufferNormalize ()
    {
	int i = 0;
	int j = 0;
	int end = dataBufferPos;

	// Skip whitespace at the start.
	while (j < end && isWhitespace (dataBuffer [j])) {
	    j++;
	}

	// Skip whitespace at the end.
	while (end > j && isWhitespace (dataBuffer [end - 1])) {
	    end --;
	}

	// Start copying to the left.
	while (j < end) {

	    char c = dataBuffer [j++];

	    // Normalise all other whitespace to
	    // a single space.
	    if (isWhitespace (c)) {
		while (j < end && isWhitespace (dataBuffer [j++])) {}

		dataBuffer [i++] = ' ';
		dataBuffer [i++] = dataBuffer [j - 1];
	    } else {
		dataBuffer [i++] = c;
	    }
	}

	// The new length is <= the old one.
	dataBufferPos = i;
    }


    /**
     * Convert the data buffer to a string.
     */
    private String dataBufferToString ()
    {
	String s = new String (dataBuffer, 0, dataBufferPos);
	dataBufferPos = 0;
	return s;
    }


    /**
     * Flush the contents of the data buffer to the handler, as
     * appropriate, and reset the buffer for new input.
     */
    private void dataBufferFlush ()
    throws SAXException
    {
	if (currentElementContent == CONTENT_ELEMENTS
		&& dataBufferPos > 0
		&& !inCDATA
		) {
	    // We can't just trust the buffer to be whitespace, there
	    // are cases when it isn't
	    for (int i = 0; i < dataBufferPos; i++) {
		if (!isWhitespace (dataBuffer [i])) {
		    handler.charData (dataBuffer, 0, dataBufferPos);
		    dataBufferPos = 0;
		}
	    }
	    if (dataBufferPos > 0) {
		handler.ignorableWhitespace (dataBuffer, 0, dataBufferPos);
		dataBufferPos = 0;
	    }
	} else if (dataBufferPos > 0) {
	    handler.charData (dataBuffer, 0, dataBufferPos);
	    dataBufferPos = 0;
	}
    }


    /**
     * Require a string to appear, or throw an exception.
     * <p><em>Precondition:</em> Entity expansion is not required.
     * <p><em>Precondition:</em> data buffer has no characters that
     * will get sent to the application.
     */
    private void require (String delim)
    throws SAXException, IOException
    {
	int	length = delim.length ();
	char	ch [];
		
	if (length < dataBuffer.length) {
	    ch = dataBuffer;
	    delim.getChars (0, length, ch, 0);
	} else
	    ch = delim.toCharArray ();

	if (USE_CHEATS
		&& length <= (readBufferLength - readBufferPos)) {
	    int offset = readBufferPos;

	    for (int i = 0; i < length; i++, offset++)
		if (ch [i] != readBuffer [offset])
		    error ("required string", null, delim);
	    readBufferPos = offset;
	    
	} else {
	    for (int i = 0; i < length; i++)
		require (ch [i]);
	}
    }


    /**
     * Require a character to appear, or throw an exception.
     */
    private void require (char delim)
    throws SAXException, IOException
    {
	char c = readCh ();

	if (c != delim) {
	    error ("required character", c, new Character (delim).toString ());
	}
    }


    /**
     * Create an interned string from a character array.
     * &AElig;lfred uses this method to create an interned version
     * of all names and name tokens, so that it can test equality
     * with <code>==</code> instead of <code>String.equals ()</code>.
     *
     * <p>This is much more efficient than constructing a non-interned
     * string first, and then interning it.
     *
     * @param ch an array of characters for building the string.
     * @param start the starting position in the array.
     * @param length the number of characters to place in the string.
     * @return an interned string.
     * @see #intern (String)
     * @see java.lang.String#intern
     */
    public String intern (char ch[], int start, int length)
    {
	int	index = 0;
	int	hash = 0;
	Object	bucket [];

	// Generate a hash code.
	for (int i = start; i < start + length; i++)
	    hash = 31 * hash + ch [i];
	hash = (hash & 0x7fffffff) % SYMBOL_TABLE_LENGTH;

	// Get the bucket -- consists of {array,String} pairs
	if ((bucket = symbolTable [hash]) == null) {
	    // first string in this bucket
	    bucket = new Object [8];

	// Search for a matching tuple, and
	// return the string if we find one.
	} else {
	    while (index < bucket.length) {
		char chFound [] = (char []) bucket [index];

		// Stop when we hit a null index.
		if (chFound == null)
		    break;

		// If they're the same length, check for a match.
		if (chFound.length == length) {
		    for (int i = 0; i < chFound.length; i++) {
			// continue search on failure
			if (ch [start + i] != chFound [i]) {
			    break;
			} else if (i == length - 1) {
			    // That's it, we have a match!
			    return (String) bucket [index + 1];
			}
		    }
		}
		index += 2;
	    }
	    // Not found -- we'll have to add it.

	    // Do we have to grow the bucket?
	    bucket = (Object []) extendArray (bucket, bucket.length, index);
	}
	symbolTable [hash] = bucket;

	// OK, add it to the end of the bucket -- "local" interning.
	// Intern "globally" to let applications share interning benefits.
	String s = new String (ch, start, length).intern ();
	bucket [index] = s.toCharArray ();
	bucket [index + 1] = s;
	return s;
    }


    /**
     * Ensure the capacity of an array, allocating a new one if
     * necessary.  Usually called only a handful of times.
     */
    private Object extendArray (Object array, int currentSize, int requiredSize)
    {
	if (requiredSize < currentSize) {
	    return array;
	} else {
	    Object newArray = null;
	    int newSize = currentSize * 2;

	    if (newSize <= requiredSize)
		newSize = requiredSize + 1;

	    if (array instanceof char[])
		newArray = new char [newSize];
	    else if (array instanceof Object[])
		newArray = new Object [newSize];
	    else
		throw new RuntimeException ();

	    System.arraycopy (array, 0, newArray, 0, currentSize);
	    return newArray;
	}
    }


    //////////////////////////////////////////////////////////////////////
    // XML query routines.
    //////////////////////////////////////////////////////////////////////


    //
    // Elements
    //

    /**
     * Get the declared elements for an XML document.
     * <p>The results will be valid only after the DTD (if any) has been
     * parsed.
     * @return An enumeration of all element types declared for this
     *	 document (as Strings).
     * @see #getElementContentType
     * @see #getElementContentModel
     */
    public Iterator declaredElements ()
    {
	return elementInfo.keySet().iterator();
    }


    /**
     * Look up the content type of an element.
     * @param element element info vector
     * @param defaultType value for null vector
     * @return An integer constant representing the content type.
     * @see #CONTENT_UNDECLARED
     * @see #CONTENT_ANY
     * @see #CONTENT_EMPTY
     * @see #CONTENT_MIXED
     * @see #CONTENT_ELEMENTS
     */
    private int getContentType (Object element [], int defaultType)
    {
	if (element == null)
	    return defaultType;
	else
	    return ((Integer) element [0]).intValue ();
    }


    /**
     * Look up the content type of an element.
     * @param name The element type name.
     * @return An integer constant representing the content type.
     * @see #getElementContentModel
     * @see #CONTENT_UNDECLARED
     * @see #CONTENT_ANY
     * @see #CONTENT_EMPTY
     * @see #CONTENT_MIXED
     * @see #CONTENT_ELEMENTS
     */
    public int getElementContentType (String name)
    {
	Object element [] = (Object []) elementInfo.get (name);
	return getContentType (element, CONTENT_UNDECLARED);
    }


    /**
     * Look up the content model of an element.
     * <p>The result will always be null unless the content type is
     * CONTENT_ELEMENTS or CONTENT_MIXED.
     * @param name The element type name.
     * @return The normalised content model, as a string.
     * @see #getElementContentType
     */
    public String getElementContentModel (String name)
    {
	Object element[] = (Object[]) elementInfo.get (name);
	if (element == null) {
	    return null;
	} else {
	    return (String) element [1];
	}
    }


    /**
     * Register an element.
     * Array format:
     *  element type
     *  attribute hash table
     */
    private void setElement (String name, int contentType,
		      String contentModel, HashMap attributes)
    throws Exception
    {
	Object element[];

	// Try looking up the element
	element = (Object[]) elementInfo.get (name);

	// Make a new one if necessary.
	if (element == null) {
	    element = new Object [3];
	    element [0] = new Integer (CONTENT_UNDECLARED);
	    element [1] = null;
	    element [2] = null;
	} else if (contentType != CONTENT_UNDECLARED
		&& ((Integer) element [0]).intValue () != CONTENT_UNDECLARED
		) {
	    // warn ("multiple declarations for element type", name, null);
	    return;
	}

	// Insert the content type, if any.
	if (contentType != CONTENT_UNDECLARED) {
	    element [0] = new Integer (contentType);
	}

	// Insert the content model, if any.
	if (contentModel != null) {
	    element [1] = contentModel;
	}

	// Insert the attributes, if any.
	if (attributes != null) {
	    element [2] = attributes;
	}

	// Save the element info.
	elementInfo.put (name, element);
    }


    /**
     * Look up the attribute hash table for an element.
     * The hash table is the second item in the element array.
     */
    private HashMap getElementAttributes (String name)
    {
	Object element[] = (Object[]) elementInfo.get (name);
	if (element == null) {
	    return null;
	} else {
	    return (HashMap) element [2];
	}
    }



    //
    // Attributes
    //

    /**
     * Get the declared attributes for an element type.
     * @param elname The name of the element type.
     * @return An Iterator of all the attributes declared for
     *	 a specific element type.  The results will be valid only
     *	 after the DTD (if any) has been parsed.
     * @see #getAttributeType
     * @see #getAttributeIterator
     * @see #getAttributeDefaultValueType
     * @see #getAttributeDefaultValue
     * @see #getAttributeExpandedValue
     */
    private Iterator declaredAttributes (Object element [])
    {
	HashMap attlist;

	if (element == null)
	    return null;
	if ((attlist = (HashMap) element [2]) == null)
	    return null;
	return attlist.keySet().iterator();
    }

    /**
     * Get the declared attributes for an element type.
     * @param elname The name of the element type.
     * @return An Iterator of all the attributes declared for
     *	 a specific element type.  The results will be valid only
     *	 after the DTD (if any) has been parsed.
     * @see #getAttributeType
     * @see #getAttributeIterator
     * @see #getAttributeDefaultValueType
     * @see #getAttributeDefaultValue
     * @see #getAttributeExpandedValue
     */
    public Iterator declaredAttributes (String elname)
    {
	return declaredAttributes ((Object []) elementInfo.get (elname));
    }


    /**
     * Retrieve the declared type of an attribute.
     * @param name The name of the associated element.
     * @param aname The name of the attribute.
     * @return An integer constant representing the attribute type.
     * @see #ATTRIBUTE_UNDECLARED
     * @see #ATTRIBUTE_CDATA
     * @see #ATTRIBUTE_ID
     * @see #ATTRIBUTE_IDREF
     * @see #ATTRIBUTE_IDREFS
     * @see #ATTRIBUTE_ENTITY
     * @see #ATTRIBUTE_ENTITIES
     * @see #ATTRIBUTE_NMTOKEN
     * @see #ATTRIBUTE_NMTOKENS
     * @see #ATTRIBUTE_ENUMERATED
     * @see #ATTRIBUTE_NOTATION
     */
    public int getAttributeType (String name, String aname)
    {
	Object attribute[] = getAttribute (name, aname);
	if (attribute == null) {
	    return ATTRIBUTE_UNDECLARED;
	} else {
	    return ((Integer) attribute [0]).intValue ();
	}
    }


    /**
     * Retrieve the allowed values for an enumerated attribute type.
     * @param name The name of the associated element.
     * @param aname The name of the attribute.
     * @return A string containing the token list.
     * @see #ATTRIBUTE_ENUMERATED
     * @see #ATTRIBUTE_NOTATION
     */
    public String getAttributeIterator (String name, String aname)
    {
	Object attribute[] = getAttribute (name, aname);
	if (attribute == null) {
	    return null;
	} else {
	    return (String) attribute [3];
	}
    }


    /**
     * Retrieve the default value of a declared attribute.
     * @param name The name of the associated element.
     * @param aname The name of the attribute.
     * @return The default value, or null if the attribute was
     *	 #IMPLIED or simply undeclared and unspecified.
     * @see #getAttributeExpandedValue
     */
    public String getAttributeDefaultValue (String name, String aname)
    {
	Object attribute[] = getAttribute (name, aname);
	if (attribute == null) {
	    return null;
	} else {
	    return (String) attribute [1];
	}
    }


    /**
     * Retrieve the expanded value of a declared attribute.
     * <p>General entities will be expanded (once).
     * @param name The name of the associated element.
     * @param aname The name of the attribute.
     * @return The expanded default value, or null if the attribute was
     *	 #IMPLIED or simply undeclared
     * @see #getAttributeDefaultValue
     */
    public String getAttributeExpandedValue (String name, String aname)
    throws Exception
    {
	Object attribute[] = getAttribute (name, aname);

	if (attribute == null) {
	    return null;
	} else if (attribute [4] == null && attribute [1] != null) {
	    // we MUST use the same buf for both quotes else the literal
	    // can't be properly terminated
	    char buf [] = new char [1];
	    int	flags = LIT_ENTITY_REF | LIT_ATTRIBUTE;
	    int	type = getAttributeType (name, aname);

	    if (type != ATTRIBUTE_CDATA && type != ATTRIBUTE_UNDECLARED)
		flags |= LIT_NORMALIZE;
	    buf [0] = '"';
	    pushCharArray (null, buf, 0, 1);
	    pushString (null, (String) attribute [1]);
	    pushCharArray (null, buf, 0, 1);
	    attribute [4] = readLiteral (flags);
	}
	return (String) attribute [4];
    }


    /**
     * Retrieve the default value type of a declared attribute.
     * @see #ATTRIBUTE_DEFAULT_SPECIFIED
     * @see #ATTRIBUTE_DEFAULT_IMPLIED
     * @see #ATTRIBUTE_DEFAULT_REQUIRED
     * @see #ATTRIBUTE_DEFAULT_FIXED
     */
    public int getAttributeDefaultValueType (String name, String aname)
    {
	Object attribute[] = getAttribute (name, aname);
	if (attribute == null) {
	    return ATTRIBUTE_DEFAULT_UNDECLARED;
	} else {
	    return ((Integer) attribute [2]).intValue ();
	}
    }


    /**
     * Register an attribute declaration for later retrieval.
     * Format:
     * - String type
     * - String default value
     * - int value type
     */
    private void setAttribute (String elName, String name, int type,
			String enumeration,
			String value, int valueType)
    throws Exception
    {
	HashMap attlist;
	Object attribute[];

	// Create a new hashtable if necessary.
	attlist = getElementAttributes (elName);
	if (attlist == null) {
	    attlist = new HashMap ();
	}

	// ignore multiple attribute declarations!
	if (attlist.get (name) != null) {
	    return;
	} else {
	    attribute = new Object [5];
	    attribute [0] = new Integer (type);
	    attribute [1] = value;
	    attribute [2] = new Integer (valueType);
	    attribute [3] = enumeration;
	    attribute [4] = null;
	    attlist.put (name, attribute);

	    // Use CONTENT_UNDECLARED to avoid overwriting
	    // existing element declaration.
	    setElement (elName, CONTENT_UNDECLARED, null, attlist);
	}
    }


    /**
     * Retrieve the three-member array representing an
     * attribute declaration.
     */
    private Object[] getAttribute (String elName, String name)
    {
	HashMap attlist;
	Object attribute[];

	attlist = getElementAttributes (elName);
	if (attlist == null) {
	    return null;
	}

	attribute = (Object[]) attlist.get (name);
	return attribute;
    }


    //
    // Entities
    //

    /**
     * Get declared entities.
     * @return An Iterator of all the entities declared for
     *	 this XML document.  The results will be valid only
     *	 after the DTD (if any) has been parsed.
     * @see #getEntityType
     * @see #getEntityPublicId
     * @see #getEntitySystemId
     * @see #getEntityValue
     * @see #getEntityNotationName
     */
    public Iterator declaredEntities ()
    {
	return entityInfo.keySet().iterator();
    }


    /**
     * Find the type of an entity.
     * @returns An integer constant representing the entity type.
     * @see #ENTITY_UNDECLARED
     * @see #ENTITY_INTERNAL
     * @see #ENTITY_NDATA
     * @see #ENTITY_TEXT
     */
    public int getEntityType (String ename)
    {
	Object entity[] = (Object[]) entityInfo.get (ename);
	if (entity == null) {
	    return ENTITY_UNDECLARED;
	} else {
	    return ((Integer) entity [0]).intValue ();
	}
    }


    /**
     * Return an external entity's public identifier, if any.
     * @param ename The name of the external entity.
     * @return The entity's system identifier, or null if the
     *	 entity was not declared, if it is not an
     *	 external entity, or if no public identifier was
     *	 provided.
     * @see #getEntityType
     */
    public String getEntityPublicId (String ename)
    {
	Object entity[] = (Object[]) entityInfo.get (ename);
	if (entity == null) {
	    return null;
	} else {
	    return (String) entity [1];
	}
    }


    /**
     * Return an external entity's system identifier.
     * @param ename The name of the external entity.
     * @return The entity's system identifier, or null if the
     *	 entity was not declared, or if it is not an
     *	 external entity.
     * @see #getEntityType
     */
    public String getEntitySystemId (String ename)
    {
	Object entity[] = (Object[]) entityInfo.get (ename);
	if (entity == null) {
	    return null;
	} else {
	    return (String) entity [2];
	}
    }


    /**
     * Return the value of an internal entity.
     * @param ename The name of the internal entity.
     * @return The entity's value, or null if the entity was
     *	 not declared, or if it is not an internal entity.
     * @see #getEntityType
     */
    public String getEntityValue (String ename)
    {
	Object entity[] = (Object[]) entityInfo.get (ename);
	if (entity == null) {
	    return null;
	} else {
	    return (String) entity [3];
	}
    }


    /**
     * Get the notation name associated with an NDATA entity.
     * @param ename The NDATA entity name.
     * @return The associated notation name, or null if the
     *	 entity was not declared, or if it is not an
     *	 NDATA entity.
     * @see #getEntityType
     */
    public String getEntityNotationName (String eName)
    {
	Object entity[] = (Object[]) entityInfo.get (eName);
	if (entity == null) {
	    return null;
	} else {
	    return (String) entity [4];
	}
    }


    /**
     * Register an entity declaration for later retrieval.
     */
    private void setInternalEntity (String eName, String value)
    {
	setEntity (eName, ENTITY_INTERNAL, null, null, value, null);
    }


    /**
     * Register an external data entity.
     */
    private void setExternalDataEntity (String eName, String pubid,
				 String sysid, String nName)
    {
	setEntity (eName, ENTITY_NDATA, pubid, sysid, null, nName);
    }


    /**
     * Register an external text entity.
     */
    private void setExternalTextEntity (String eName,
		    String pubid, String sysid)
    {
	setEntity (eName, ENTITY_TEXT, pubid, sysid, null, null);
    }


    /**
     * Register an entity declaration for later retrieval.
     */
    private void setEntity (String eName, int eClass,
		     String pubid, String sysid,
		     String value, String nName)
    {
	Object entity[];

	if (entityInfo.get (eName) == null) {
	    entity = new Object [5];
	    entity [0] = new Integer (eClass);
	    entity [1] = pubid;
	    entity [2] = sysid;
	    entity [3] = value;
	    entity [4] = nName;

	    entityInfo.put (eName, entity);
	}
    }


    //
    // Notations.
    //

    /**
     * Get declared notations.
     * @return An Iterator of all the notations declared for
     *	 this XML document.  The results will be valid only
     *	 after the DTD (if any) has been parsed.
     * @see #getNotationPublicId
     * @see #getNotationSystemId
     */
    public Iterator declaredNotations ()
    {
	return notationInfo.keySet().iterator();
    }


    /**
     * Look up the public identifier for a notation.
     * You will normally use this method to look up a notation
     * that was provided as an attribute value or for an NDATA entity.
     * @param nname The name of the notation.
     * @return A string containing the public identifier, or null
     *	 if none was provided or if no such notation was
     *	 declared.
     * @see #getNotationSystemId
     */
    public String getNotationPublicId (String nname)
    {
	Object notation[] = (Object[]) notationInfo.get (nname);
	if (notation == null) {
	    return null;
	} else {
	    return (String) notation [0];
	}
    }


    /**
     * Look up the system identifier for a notation.
     * You will normally use this method to look up a notation
     * that was provided as an attribute value or for an NDATA entity.
     * @param nname The name of the notation.
     * @return A string containing the system identifier, or null
     *	 if no such notation was declared.
     * @see #getNotationPublicId
     */
    public String getNotationSystemId (String nname)
    {
	Object notation[] = (Object[]) notationInfo.get (nname);
	if (notation == null) {
	    return null;
	} else {
	    return (String) notation [1];
	}
    }


    /**
     * Register a notation declaration for later retrieval.
     * Format:
     * - public id
     * - system id
     */
    private void setNotation (String nname, String pubid, String sysid)
    throws Exception
    {
	Object notation[];

	if (notationInfo.get (nname) == null) {
	    notation = new Object [2];
	    notation [0] = pubid;
	    notation [1] = sysid;
	    notationInfo.put (nname, notation);
	} else {
	    // VC: Unique Notation Name
	    // (it's not fatal)
	}
    }


    //
    // Location.
    //


    /**
     * Return the current line number.
     */
    public int getLineNumber ()
    {
	return line;
    }


    /**
     * Return the current column number.
     */
    public int getColumnNumber ()
    {
	return column;
    }


    //////////////////////////////////////////////////////////////////////
    // High-level I/O.
    //////////////////////////////////////////////////////////////////////


    /**
     * Read a single character from the readBuffer.
     * <p>The readDataChunk () method maintains the buffer.
     * <p>If we hit the end of an entity, try to pop the stack and
     * keep going.
     * <p> (This approach doesn't really enforce XML's rules about
     * entity boundaries, but this is not currently a validating
     * parser).
     * <p>This routine also attempts to keep track of the current
     * position in external entities, but it's not entirely accurate.
     * @return The next available input character.
     * @see #unread (char)
     * @see #unread (String)
     * @see #readDataChunk
     * @see #readBuffer
     * @see #line
     * @return The next character from the current input source.
     */
    private char readCh ()
    throws SAXException, IOException
    {
	char c;

	// As long as there's nothing in the
	// read buffer, try reading more data
	// (for an external entity) or popping
	// the entity stack (for either).
	while (readBufferPos >= readBufferLength) {
	    switch (sourceType) {
	    case INPUT_READER:
	    case INPUT_EXTERNAL:
	    case INPUT_STREAM:
		readDataChunk ();
		while (readBufferLength < 1) {
		    popInput ();
		    if (readBufferLength < 1) {
			readDataChunk ();
		    }
		}
		break;

	    default:

		popInput ();
		break;
	    }
	}

	c = readBuffer [readBufferPos++];

	if (c == '\n') {
	    line++;
	    column = 0;
	} else {
	    if (c == '<')
		/* favorite return to parseContent () .. NOP */ ;
	    else if ((c < 0x0020 && (c != '\t') && (c != '\r')) || c > 0xFFFD)
		error ("illegal XML character U+"
			+ Integer.toHexString (c));

	    // If we're in the DTD and in a context where PEs get expanded,
	    // do so ... 1/14/2000 errata identify those contexts.  There
	    // are also spots in the internal subset where PE refs are fatal
	    // errors, hence yet another flag.
	    else if (c == '%' && expandPE) {
		if (peIsError)
		    error ("PE reference within decl in internal subset.");
		parsePEReference ();
		return readCh ();
	    }
	    column++;
	}

	return c;
    }


    /**
     * Push a single character back onto the current input stream.
     * <p>This method usually pushes the character back onto
     * the readBuffer, while the unread (String) method treats the
     * string as a new internal entity.
     * <p>I don't think that this would ever be called with 
     * readBufferPos = 0, because the methods always reads a character
     * before unreading it, but just in case, I've added a boundary
     * condition.
     * @param c The character to push back.
     * @see #readCh
     * @see #unread (String)
     * @see #unread (char[])
     * @see #readBuffer
     */
    private void unread (char c)
    throws SAXException
    {
	// Normal condition.
	if (c == '\n') {
	    line--;
	    column = -1;
	}
	if (readBufferPos > 0) {
	    readBuffer [--readBufferPos] = c;
	} else {
	    pushString (null, new Character (c).toString ());
	}
    }


    /**
     * Push a char array back onto the current input stream.
     * <p>NOTE: you must <em>never</em> push back characters that you
     * haven't actually read: use pushString () instead.
     * @see #readCh
     * @see #unread (char)
     * @see #unread (String)
     * @see #readBuffer
     * @see #pushString
     */
    private void unread (char ch[], int length)
    throws SAXException
    {
	for (int i = 0; i < length; i++) {
	    if (ch [i] == '\n') {
		line--;
		column = -1;
	    }
	}
	if (length < readBufferPos) {
	    readBufferPos -= length;
	} else {
	    pushCharArray (null, ch, 0, length);
	    sourceType = INPUT_BUFFER;
	}
    }


    /**
     * Push a new external input source.
     * The source will be some kind of parsed entity, such as a PE
     * (including the external DTD subset) or content for the body.
     * <p>TODO: Right now, this method always attempts to autodetect
     * the encoding; in the future, it should allow the caller to 
     * request an encoding explicitly, and it should also look at the
     * headers with an HTTP connection.
     * @param url The java.net.URL object for the entity.
     * @see SAXDriver#resolveEntity
     * @see #pushString
     * @see #sourceType
     * @see #pushInput
     * @see #detectEncoding
     * @see #sourceType
     * @see #readBuffer
     */
    private void pushURL (
	String		ename,
	String		publicId,
	String		systemId,
	Reader		reader,
	InputStream	stream,
	String		encoding
    ) throws SAXException, IOException
    {
	URL	url;
	boolean	ignoreEncoding = false;

	// Push the existing status.
	pushInput (ename);

	// Create a new read buffer.
	// (Note the four-character margin)
	readBuffer = new char [READ_BUFFER_MAX + 4];
	readBufferPos = 0;
	readBufferLength = 0;
	readBufferOverflow = -1;
	is = null;
	line = 1;

	currentByteCount = 0;

	// Make any system ID (URI/URL) absolute.  There's one case
	// where it may be null:  parser was invoked without providing
	// one, e.g. since the XML data came from a memory buffer.

	if (systemId != null && externalEntity != null) {
	    systemId = new URL (externalEntity.getURL (), systemId).toString ();
	} else if (baseURI != null) {
	    systemId = new URL (new URL (baseURI), systemId).toString ();
	    // throws IOException if couldn't create new URL
	}

	// See if the application wants to
	// redirect the system ID and/or
	// supply its own character stream.
	if (reader == null && stream == null && systemId != null) {
	    Object input = handler.resolveEntity (publicId, systemId);
	    if (input != null) {
		if (input instanceof String) {
		    systemId = (String) input;
		} else if (input instanceof InputStream) {
		    stream = (InputStream) input;
		} else if (input instanceof Reader) {
		    reader = (Reader) input;
		}
	    }
	}

	// Start the entity.
	if (systemId != null) {
	    handler.startExternalEntity (systemId);
	} else {
	    handler.startExternalEntity ("[unidentified data stream]");
	}

	// If there's an explicit character stream, just
	// ignore encoding declarations.
	if (reader != null) {
	    sourceType = INPUT_READER;
	    this.reader = reader;
	    tryEncodingDecl (true);
	    return;
	}
	
	// Else we handle the conversion, and need to ensure
	// it's done right.
	if (stream != null) {
	    sourceType = INPUT_STREAM;
	    is = stream;
	    url = null;
	} else {
	    // We have to open our own stream to the URL.

	    // Set the new status
	    sourceType = INPUT_EXTERNAL;
	    url = new URL (systemId);

	    externalEntity = url.openConnection ();
	    externalEntity.connect ();
	    is = externalEntity.getInputStream ();
	}

	// If we get to here, there must be
	// an InputStream available.
	if (!is.markSupported ()) {
	    is = new BufferedInputStream (is);
	}

	// Get any external encoding label.
	if (encoding == null && externalEntity != null) {
	    // External labels can be untrustworthy; filesystems in
	    // particular often have the wrong default for content
	    // that wasn't locally originated.  Those we autodetect.
	    if (!"file".equals (externalEntity.getURL ().getProtocol ())) {
		int temp;

		// application/xml;charset=something;otherAttr=...
		// ... with many variants on 'something'
		encoding = externalEntity.getContentType ();

		// MHK code (fix for Saxon 5.5.1/007): protect against encoding==null
		if (encoding==null) {
		    temp = -1;
		} else {
		    temp = encoding.indexOf ("charset");
		}

		// RFC 2376 sez MIME text defaults to ASCII, but since the
		// JDK will create a MIME type out of thin air, we always
		// autodetect when there's no explicit charset attribute.
		if (temp < 0)
		    encoding = null;	// autodetect
		else {
		    temp = encoding.indexOf ('=', temp + 7);
		    encoding = encoding.substring (temp);
		    if ((temp = encoding.indexOf (';')) > 0)
			encoding = encoding.substring (0, temp);

		    // attributes can have comment fields (RFC 822)
		    if ((temp = encoding.indexOf ('(')) > 0)
			encoding = encoding.substring (0, temp);
		    // ... and values may be quoted
		    if ((temp = encoding.indexOf ('"')) > 0)
			encoding = encoding.substring (temp + 1,
				encoding.indexOf ('"', temp + 2));
		    encoding.trim ();
		}
	    }
	}

	// if we got an external encoding label, use it ...
	if (encoding != null) {
	    this.encoding = ENCODING_EXTERNAL;
	    setupDecoding (encoding);
	    ignoreEncoding = true;
	
	// ... else autodetect
	} else {
	    detectEncoding ();
	    ignoreEncoding = false;
	}

	// Read any XML or text declaration.
	tryEncodingDecl (ignoreEncoding);
    }


    /**
     * Check for an encoding declaration.  This is the second part of the
     * XML encoding autodetection algorithm, relying on detectEncoding to
     * get to the point that this part can read any encoding declaration
     * in the document (using only US-ASCII characters).
     *
     * <p> Because this part starts to fill parser buffers with this data,
     * it's tricky to to a reader so that Java's built-in decoders can be
     * used for the character encodings that aren't built in to this parser
     * (such as EUC-JP, KOI8-R, Big5, etc).
     *
     * @return any encoding in the declaration, uppercased; or null
     * @see detectEncoding
     */
    private String tryEncodingDecl (boolean ignoreEncoding)
    throws SAXException, IOException
    {
	// Read the XML/text declaration.
	if (tryRead ("<?xml")) {
	    dataBufferFlush ();
	    if (tryWhitespace ()) {
		if (inputStack.size () > 0) {
		    return parseTextDecl (ignoreEncoding);
		} else {
		    return parseXMLDecl (ignoreEncoding);
		}
	    } else {
		unread ("xml".toCharArray (), 3);
		parsePI ();
	    }
	}
	return null;
    }


    /**
     * Attempt to detect the encoding of an entity.
     * <p>The trick here (as suggested in the XML standard) is that
     * any entity not in UTF-8, or in UCS-2 with a byte-order mark, 
     * <b>must</b> begin with an XML declaration or an encoding
     * declaration; we simply have to look for "&lt;?xml" in various
     * encodings.
     * <p>This method has no way to distinguish among 8-bit encodings.
     * Instead, it sets up for UTF-8, then (possibly) revises its assumption
     * later in setupDecoding ().  Any ASCII-derived 8-bit encoding
     * should work, but most will be rejected later by setupDecoding ().
     * <p>I don't currently detect EBCDIC, since I'm concerned that it
     * could also be a valid UTF-8 sequence; I'll have to do more checking
     * later.
     * @see #tryEncoding (byte[], byte, byte, byte, byte)
     * @see #tryEncoding (byte[], byte, byte)
     * @see #setupDecoding
     * @see #read8bitEncodingDeclaration
     */
    private void detectEncoding ()
    throws SAXException, IOException
    {
	byte signature[] = new byte [4];

	// Read the first four bytes for
	// autodetection.
	is.mark (4);
	is.read (signature);
	is.reset ();

	//
	// FIRST:  four byte encodings (who uses these?)
	//
	if (tryEncoding (signature, (byte) 0x00, (byte) 0x00,
			  (byte) 0x00, (byte) 0x3c)) {
	    // UCS-4 must begin with "<?xml"
	    // 0x00 0x00 0x00 0x3c: UCS-4, big-endian (1234)
	    encoding = ENCODING_UCS_4_1234;

	} else if (tryEncoding (signature, (byte) 0x3c, (byte) 0x00,
				 (byte) 0x00, (byte) 0x00)) {
	    // 0x3c 0x00 0x00 0x00: UCS-4, little-endian (4321)
	    encoding = ENCODING_UCS_4_4321;

	} else if (tryEncoding (signature, (byte) 0x00, (byte) 0x00,
				 (byte) 0x3c, (byte) 0x00)) {
	    // 0x00 0x00 0x3c 0x00: UCS-4, unusual (2143)
	    encoding = ENCODING_UCS_4_2143;

	} else if (tryEncoding (signature, (byte) 0x00, (byte) 0x3c,
				 (byte) 0x00, (byte) 0x00)) {
	    // 0x00 0x3c 0x00 0x00: UCS-4, unusual (3421)
	    encoding = ENCODING_UCS_4_3412;

	    // 00 00 fe ff UCS_4_1234 (with BOM)
	    // ff fe 00 00 UCS_4_4321 (with BOM)
	}

	//
	// SECOND:  two byte encodings
	// note ... with 1/14/2000 errata the XML spec identifies some
	// more "broken UTF-16" autodetection cases, with no XML decl,
	// which we don't handle here (that's legal too).
	//
	else if (tryEncoding (signature, (byte) 0xfe, (byte) 0xff)) {
	    // UCS-2 with a byte-order marker. (UTF-16)
	    // 0xfe 0xff: UCS-2, big-endian (12)
	    encoding = ENCODING_UCS_2_12;
	    is.read (); is.read ();

	} else if (tryEncoding (signature, (byte) 0xff, (byte) 0xfe)) {
	    // UCS-2 with a byte-order marker. (UTF-16)
	    // 0xff 0xfe: UCS-2, little-endian (21)
	    encoding = ENCODING_UCS_2_21;
	    is.read (); is.read ();

	} else if (tryEncoding (signature, (byte) 0x00, (byte) 0x3c,
				 (byte) 0x00, (byte) 0x3f)) {
	    // UTF-16-BE (otherwise, malformed UTF-16)
	    // 0x00 0x3c 0x00 0x3f: UCS-2, big-endian, no byte-order mark
	    encoding = ENCODING_UCS_2_12;
	    error ("no byte-order mark for UCS-2 entity");

	} else if (tryEncoding (signature, (byte) 0x3c, (byte) 0x00,
				 (byte) 0x3f, (byte) 0x00)) {
	    // UTF-16-LE (otherwise, malformed UTF-16)
	    // 0x3c 0x00 0x3f 0x00: UCS-2, little-endian, no byte-order mark
	    encoding = ENCODING_UCS_2_21;
	    error ("no byte-order mark for UCS-2 entity");
	}

	//
	// THIRD:  ASCII-derived encodings, fixed and variable lengths
	//
	else if (tryEncoding (signature, (byte) 0x3c, (byte) 0x3f,
			       (byte) 0x78, (byte) 0x6d)) {
	    // ASCII derived
	    // 0x3c 0x3f 0x78 0x6d: UTF-8 or other 8-bit markup (read ENCODING)
	    encoding = ENCODING_UTF_8;
	    read8bitEncodingDeclaration ();

	} else {
	    // 4c 6f a7 94 ... we don't understand EBCDIC flavors
	    // ... but we COULD at least kick in some fixed code page

	    // (default) UTF-8 without encoding/XML declaration
	    encoding = ENCODING_UTF_8;
	}
    }


    /**
     * Check for a four-byte signature.
     * <p>Utility routine for detectEncoding ().
     * <p>Always looks for some part of "<?XML" in a specific encoding.
     * @param sig The first four bytes read.
     * @param b1 The first byte of the signature
     * @param b2 The second byte of the signature
     * @param b3 The third byte of the signature
     * @param b4 The fourth byte of the signature
     * @see #detectEncoding
     */
    private static boolean tryEncoding (
	byte sig[], byte b1, byte b2, byte b3, byte b4)
    {
	return (sig [0] == b1 && sig [1] == b2
		&& sig [2] == b3 && sig [3] == b4);
    }


    /**
     * Check for a two-byte signature.
     * <p>Looks for a UCS-2 byte-order mark.
     * <p>Utility routine for detectEncoding ().
     * @param sig The first four bytes read.
     * @param b1 The first byte of the signature
     * @param b2 The second byte of the signature
     * @see #detectEncoding
     */
    private static boolean tryEncoding (byte sig[], byte b1, byte b2)
    {
	return ((sig [0] == b1) && (sig [1] == b2));
    }


    /**
     * This method pushes a string back onto input.
     * <p>It is useful either as the expansion of an internal entity, 
     * or for backtracking during the parse.
     * <p>Call pushCharArray () to do the actual work.
     * @param s The string to push back onto input.
     * @see #pushCharArray
     */
    private void pushString (String ename, String s)
    throws SAXException
    {
	char ch[] = s.toCharArray ();
	pushCharArray (ename, ch, 0, ch.length);
    }


    /**
     * Push a new internal input source.
     * <p>This method is useful for expanding an internal entity,
     * or for unreading a string of characters.  It creates a new
     * readBuffer containing the characters in the array, instead
     * of characters converted from an input byte stream.
     * @param ch The char array to push.
     * @see #pushString
     * @see #pushURL
     * @see #readBuffer
     * @see #sourceType
     * @see #pushInput
     */
    private void pushCharArray (String ename, char ch[], int start, int length)
    throws SAXException
    {
	// Push the existing status
	pushInput (ename);
	sourceType = INPUT_INTERNAL;
	readBuffer = ch;
	readBufferPos = start;
	readBufferLength = length;
	readBufferOverflow = -1;
    }


    /**
     * Save the current input source onto the stack.
     * <p>This method saves all of the global variables associated with
     * the current input source, so that they can be restored when a new
     * input source has finished.  It also tests for entity recursion.
     * <p>The method saves the following global variables onto a stack
     * using a fixed-length array:
     * <ol>
     * <li>sourceType
     * <li>externalEntity
     * <li>readBuffer
     * <li>readBufferPos
     * <li>readBufferLength
     * <li>line
     * <li>encoding
     * </ol>
     * @param ename The name of the entity (if any) causing the new input.
     * @see #popInput
     * @see #sourceType
     * @see #externalEntity
     * @see #readBuffer
     * @see #readBufferPos
     * @see #readBufferLength
     * @see #line
     * @see #encoding
     */
    private void pushInput (String ename)
    throws SAXException
    {
	Object input[] = new Object [12];

	// Check for entity recursion.
	if (ename != null) {
	    Iterator entities = entityStack.iterator ();
	    while (entities.hasNext ()) {
		String e = (String) entities.next ();
		if (e == ename) {
		    error ("recursive reference to entity", ename, null);
		}
	    }
	}
	entityStack.add (ename);

	// Don't bother if there is no current input.
	if (sourceType == INPUT_NONE) {
	    return;
	}

	// Set up a snapshot of the current
	// input source.
	input [0] = new Integer (sourceType);
	input [1] = externalEntity;
	input [2] = readBuffer;
	input [3] = new Integer (readBufferPos);
	input [4] = new Integer (readBufferLength);
	input [5] = new Integer (line);
	input [6] = new Integer (encoding);
	input [7] = new Integer (readBufferOverflow);
	input [8] = is;
	input [9] = new Integer (currentByteCount);
	input [10] = new Integer (column);
	input [11] = reader;

	// Push it onto the stack.
	inputStack.add (input);
    }


    /**
     * Restore a previous input source.
     * <p>This method restores all of the global variables associated with
     * the current input source.
     * @exception java.io.EOFException
     *    If there are no more entries on the input stack.
     * @see #pushInput
     * @see #sourceType
     * @see #externalEntity
     * @see #readBuffer
     * @see #readBufferPos
     * @see #readBufferLength
     * @see #line
     * @see #encoding
     */
    private void popInput ()
    throws SAXException, IOException
    {
	Object input[];


	switch (sourceType) {

	case INPUT_EXTERNAL:
	    if (externalEntity != null) {
		handler.endExternalEntity (
			externalEntity.getURL ().toString ());
	    }
	    break;
	case INPUT_STREAM:
	    if (baseURI != null) {
		handler.endExternalEntity (baseURI);
	    }
	    is.close ();
	    break;
	case INPUT_READER:
	    if (baseURI != null) {
		handler.endExternalEntity (baseURI);
	    }
	    reader.close ();
	    break;
	}

	// Throw an EOFException if there
	// is nothing else to pop.
	if (inputStack.isEmpty ()) {
	    throw new EOFException ("no more input");
	} else {
	    String s;
	    input = (Object[]) inputStack.remove ( inputStack.size() - 1 );
	    s = (String) entityStack.remove ( entityStack.size() - 1 );
	}

	sourceType = ((Integer) input [0]).intValue ();
	externalEntity = (URLConnection) input [1];
	readBuffer = (char[]) input [2];
	readBufferPos = ((Integer) input [3]).intValue ();
	readBufferLength = ((Integer) input [4]).intValue ();
	line = ((Integer) input [5]).intValue ();
	encoding = ((Integer) input [6]).intValue ();
	readBufferOverflow = ((Integer) input [7]).intValue ();
	is = (InputStream) input [8];
	currentByteCount = ((Integer) input [9]).intValue ();
	column = ((Integer) input [10]).intValue ();
	reader = (Reader) input [11];
    }


    /**
     * Return true if we can read the expected character.
     * <p>Note that the character will be removed from the input stream
     * on success, but will be put back on failure.  Do not attempt to
     * read the character again if the method succeeds.
     * @param delim The character that should appear next.  For a
     *	      insensitive match, you must supply this in upper-case.
     * @return true if the character was successfully read, or false if
     *	 it was not.
     * @see #tryRead (String)
     */
    private boolean tryRead (char delim)
    throws SAXException, IOException
    {
	char c;

	// Read the character
	c = readCh ();

	// Test for a match, and push the character
	// back if the match fails.
	if (c == delim) {
	    return true;
	} else {
	    unread (c);
	    return false;
	}
    }


    /**
     * Return true if we can read the expected string.
     * <p>This is simply a convenience method.
     * <p>Note that the string will be removed from the input stream
     * on success, but will be put back on failure.  Do not attempt to
     * read the string again if the method succeeds.
     * <p>This method will push back a character rather than an
     * array whenever possible (probably the majority of cases).
     * <p><b>NOTE:</b> This method currently has a hard-coded limit
     * of 100 characters for the delimiter.
     * @param delim The string that should appear next.
     * @return true if the string was successfully read, or false if
     *	 it was not.
     * @see #tryRead (char)
     */
    private boolean tryRead (String delim)
    throws SAXException, IOException
    {
	char ch[] = delim.toCharArray ();
	char c;

	// Compare the input, character-
	// by character.

	for (int i = 0; i < ch.length; i++) {
	    c = readCh ();
	    if (c != ch [i]) {
		unread (c);
		if (i != 0) {
		    unread (ch, i);
		}
		return false;
	    }
	}
	return true;
    }



    /**
     * Return true if we can read some whitespace.
     * <p>This is simply a convenience method.
     * <p>This method will push back a character rather than an
     * array whenever possible (probably the majority of cases).
     * @return true if whitespace was found.
     */
    private boolean tryWhitespace ()
    throws SAXException, IOException
    {
	char c;
	c = readCh ();
	if (isWhitespace (c)) {
	    skipWhitespace ();
	    return true;
	} else {
	    unread (c);
	    return false;
	}
    }


    /**
     * Read all data until we find the specified string.
     * This is useful for scanning CDATA sections and PIs.
     * <p>This is inefficient right now, since it calls tryRead ()
     * for every character.
     * @param delim The string delimiter
     * @see #tryRead (String, boolean)
     * @see #readCh
     */
    private void parseUntil (String delim)
    throws SAXException, IOException
    {
	char c;
	int startLine = line;

	try {
	    while (!tryRead (delim)) {
		c = readCh ();
		dataBufferAppend (c);
	    }
	} catch (EOFException e) {
	    error ("end of input while looking for delimiter "
		+ "(started on line " + startLine
		+ ')', null, delim);
	}
    }


    /**
     * Read just the encoding declaration (or XML declaration) at the 
     * start of an external entity.
     * When this method is called, we know that the declaration is
     * present (or appears to be).  We also know that the entity is
     * in some sort of ASCII-derived 8-bit encoding.
     * The idea of this is to let us read what the 8-bit encoding is
     * before we've committed to converting any more of the file; the
     * XML or encoding declaration must be in 7-bit ASCII, so we're
     * safe as long as we don't go past it.
     */
    private void read8bitEncodingDeclaration ()
    throws SAXException, IOException
    {
	int ch;
	readBufferPos = readBufferLength = 0;

	while (true) {
	    ch = is.read ();
	    readBuffer [readBufferLength++] = (char) ch;
	    switch (ch) {
	      case (int) '>':
		return;
	      case - 1:
		error ("end of file before end of XML or encoding declaration.",
		       null, "?>");
	    }
	    if (readBuffer.length == readBufferLength)
		error ("unfinished XML or encoding declaration");
	}
    }


    //////////////////////////////////////////////////////////////////////
    // Low-level I/O.
    //////////////////////////////////////////////////////////////////////


    /**
     * Read a chunk of data from an external input source.
     * <p>This is simply a front-end that fills the rawReadBuffer
     * with bytes, then calls the appropriate encoding handler.
     * @see #encoding
     * @see #rawReadBuffer
     * @see #readBuffer
     * @see #filterCR
     * @see #copyUtf8ReadBuffer
     * @see #copyIso8859_1ReadBuffer
     * @see #copyUcs_2ReadBuffer
     * @see #copyUcs_4ReadBuffer
     */
    private void readDataChunk ()
    throws SAXException, IOException
    {
	int count, i, j;

	// See if we have any overflow (filterCR sets for CR at end)
	if (readBufferOverflow > -1) {
	    readBuffer [0] = (char) readBufferOverflow;
	    readBufferOverflow = -1;
	    readBufferPos = 1;
	    sawCR = true;
	} else {
	    readBufferPos = 0;
	    sawCR = false;
	}

	// input from a character stream.
	if (sourceType == INPUT_READER) {
	    count = reader.read (readBuffer,
			    readBufferPos, READ_BUFFER_MAX - readBufferPos);
	    if (count < 0)
		readBufferLength = readBufferPos;
	    else
		readBufferLength = readBufferPos + count;
	    if (readBufferLength > 0)
		filterCR (count >= 0);
	    sawCR = false;
	    return;
	}

	// Read as many bytes as possible into the raw buffer.
	count = is.read (rawReadBuffer, 0, READ_BUFFER_MAX);

	// Dispatch to an encoding-specific reader method to populate
	// the readBuffer.  In most parser speed profiles, these routines
	// show up at the top of the CPU usage chart.
	if (count > 0) {
	    switch (encoding) {
	      // one byte builtins
	      case ENCODING_ASCII:
		copyIso8859_1ReadBuffer (count, (char) 0x0080);
		break;
	      case ENCODING_UTF_8:
		copyUtf8ReadBuffer (count);
		break;
	      case ENCODING_ISO_8859_1:
		copyIso8859_1ReadBuffer (count, (char) 0);
		break;

	      // two byte builtins
	      case ENCODING_UCS_2_12:
		copyUcs2ReadBuffer (count, 8, 0);
		break;
	      case ENCODING_UCS_2_21:
		copyUcs2ReadBuffer (count, 0, 8);
		break;

	      // four byte builtins
	      case ENCODING_UCS_4_1234:
		copyUcs4ReadBuffer (count, 24, 16, 8, 0);
		break;
	      case ENCODING_UCS_4_4321:
		copyUcs4ReadBuffer (count, 0, 8, 16, 24);
		break;
	      case ENCODING_UCS_4_2143:
		copyUcs4ReadBuffer (count, 16, 24, 0, 8);
		break;
	      case ENCODING_UCS_4_3412:
		copyUcs4ReadBuffer (count, 8, 0, 24, 16);
		break;
	    }
	} else
	    readBufferLength = readBufferPos;

	readBufferPos = 0;

	// Filter out all carriage returns if we've seen any
	// (including any saved from a previous read)
	if (sawCR) {
	    filterCR (count >= 0);
	    sawCR = false;

	    // must actively report EOF, lest some CRs get lost.
	    if (readBufferLength == 0 && count >= 0)
		readDataChunk ();
	}

	if (count > 0)
	    currentByteCount += count;
    }


    /**
     * Filter carriage returns in the read buffer.
     * CRLF becomes LF; CR becomes LF.
     * @param moreData true iff more data might come from the same source
     * @see #readDataChunk
     * @see #readBuffer
     * @see #readBufferOverflow
     */
    private void filterCR (boolean moreData)
    {
	int i, j;

	readBufferOverflow = -1;

loop:
	for (i = j = readBufferPos; j < readBufferLength; i++, j++) {
	    switch (readBuffer [j]) {
	    case '\r':
		if (j == readBufferLength - 1) {
		    if (moreData) {
			readBufferOverflow = '\r';
			readBufferLength--;
		    } else 	// CR at end of buffer
			readBuffer [i++] = '\n';
		    break loop;
		} else if (readBuffer [j + 1] == '\n') {
		    j++;
		}
		readBuffer [i] = '\n';
		break;

	    case '\n':
	    default:
		readBuffer [i] = readBuffer [j];
		break;
	    }
	}
	readBufferLength = i;
    }

    /**
     * Convert a buffer of UTF-8-encoded bytes into UTF-16 characters.
     * <p>When readDataChunk () calls this method, the raw bytes are in 
     * rawReadBuffer, and the final characters will appear in 
     * readBuffer.
     * @param count The number of bytes to convert.
     * @see #readDataChunk
     * @see #rawReadBuffer
     * @see #readBuffer
     * @see #getNextUtf8Byte
     */
    private void copyUtf8ReadBuffer (int count)
    throws SAXException, IOException
    {
	int	i = 0;
	int	j = readBufferPos;
	int	b1;
	char	c = 0;

	/*
	// check once, so the runtime won't (if it's smart enough)
	if (count < 0 || count > rawReadBuffer.length)
	    throw new ArrayIndexOutOfBoundsException (Integer.toString (count));
	*/

	while (i < count) {
	    b1 = rawReadBuffer [i++];

	    // Determine whether we are dealing
	    // with a one-, two-, three-, or four-
	    // byte sequence.
	    if (b1 < 0) {
		if ((b1 & 0xe0) == 0xc0) {
		    // 2-byte sequence: 00000yyyyyxxxxxx = 110yyyyy 10xxxxxx
		    c = (char) (((b1 & 0x1f) << 6)
				| getNextUtf8Byte (i++, count));
		} else if ((b1 & 0xf0) == 0xe0) {
		    // 3-byte sequence:
		    // zzzzyyyyyyxxxxxx = 1110zzzz 10yyyyyy 10xxxxxx
		    // most CJKV characters
		    c = (char) (((b1 & 0x0f) << 12) |
				   (getNextUtf8Byte (i++, count) << 6) |
				   getNextUtf8Byte (i++, count));
		} else if ((b1 & 0xf8) == 0xf0) {
		    // 4-byte sequence: 11101110wwwwzzzzyy + 110111yyyyxxxxxx
		    //     = 11110uuu 10uuzzzz 10yyyyyy 10xxxxxx
		    // (uuuuu = wwww + 1)
		    // "Surrogate Pairs" ... from the "Astral Planes"
		    int iso646 = b1 & 07;
		    iso646 = (iso646 << 6) + getNextUtf8Byte (i++, count);
		    iso646 = (iso646 << 6) + getNextUtf8Byte (i++, count);
		    iso646 = (iso646 << 6) + getNextUtf8Byte (i++, count);

		    if (iso646 <= 0xffff) {
			c = (char) iso646;
		    } else {
			if (iso646 > 0x0010ffff)
			    encodingError (
				"UTF-8 value out of range for Unicode",
				iso646, 0);
			iso646 -= 0x010000;
			readBuffer [j++] = (char) (0xd800 | (iso646 >> 10));
			readBuffer [j++] = (char) (0xdc00 | (iso646 & 0x03ff));
			continue;
		    }
		} else {
		    // The five and six byte encodings aren't supported;
		    // they exceed the Unicode (and XML) range.
		    encodingError (
			    "unsupported five or six byte UTF-8 sequence",
			    0xff & b1, i);
		    // NOTREACHED
		    c = 0;
		}
	    } else {
		// 1-byte sequence: 000000000xxxxxxx = 0xxxxxxx
		// (US-ASCII character, "common" case, one branch to here)
		c = (char) b1;
	    }
	    readBuffer [j++] = c;
	    if (c == '\r')
		sawCR = true;
	}
	// How many characters have we read?
	readBufferLength = j;
    }


    /**
     * Return the next byte value in a UTF-8 sequence.
     * If it is not possible to get a byte from the current
     * entity, throw an exception.
     * @param pos The current position in the rawReadBuffer.
     * @param count The number of bytes in the rawReadBuffer
     * @return The significant six bits of a non-initial byte in
     *	 a UTF-8 sequence.
     * @exception EOFException If the sequence is incomplete.
     */
    private int getNextUtf8Byte (int pos, int count)
    throws SAXException, IOException
    {
	int val;

	// Take a character from the buffer
	// or from the actual input stream.
	if (pos < count) {
	    val = rawReadBuffer [pos];
	} else {
	    val = is.read ();
	    if (val == -1) {
		encodingError ("unfinished multi-byte UTF-8 sequence at EOF",
			-1, pos);
	    }
	}

	// Check for the correct bits at the start.
	if ((val & 0xc0) != 0x80) {
	    encodingError ("bad continuation of multi-byte UTF-8 sequence",
		    val, pos + 1);
	}

	// Return the significant bits.
	return (val & 0x3f);
    }


    /**
     * Convert a buffer of US-ASCII or ISO-8859-1-encoded bytes into
     * UTF-16 characters.
     *
     * <p>When readDataChunk () calls this method, the raw bytes are in 
     * rawReadBuffer, and the final characters will appear in 
     * readBuffer.
     *
     * @param count The number of bytes to convert.
     * @param mask For ASCII conversion, 0x7f; else, 0xff.
     * @see #readDataChunk
     * @see #rawReadBuffer
     * @see #readBuffer
     */
    private void copyIso8859_1ReadBuffer (int count, char mask)
    throws IOException
    {
	int i, j;
	for (i = 0, j = readBufferPos; i < count; i++, j++) {
	    char c = (char) (rawReadBuffer [i] & 0xff);
	    if ((c & mask) != 0)
		throw new CharConversionException ("non-ASCII character U+"
						    + Integer.toHexString (c));
	    readBuffer [j] = c;
	    if (c == '\r') {
		sawCR = true;
	    }
	}
	readBufferLength = j;
    }


    /**
     * Convert a buffer of UCS-2-encoded bytes into UTF-16 characters
     * (as used in Java string manipulation).
     *
     * <p>When readDataChunk () calls this method, the raw bytes are in 
     * rawReadBuffer, and the final characters will appear in 
     * readBuffer.
     * @param count The number of bytes to convert.
     * @param shift1 The number of bits to shift byte 1.
     * @param shift2 The number of bits to shift byte 2
     * @see #readDataChunk
     * @see #rawReadBuffer
     * @see #readBuffer
     */
    private void copyUcs2ReadBuffer (int count, int shift1, int shift2)
    throws SAXException
    {
	int j = readBufferPos;

	if (count > 0 && (count % 2) != 0) {
	    encodingError ("odd number of bytes in UCS-2 encoding", -1, count);
	}
	// The loops are faster with less internal brancing; hence two
	if (shift1 == 0) {	// "UTF-16-LE"
	    for (int i = 0; i < count; i += 2) {
		char c = (char) (rawReadBuffer [i + 1] << 8);
		c |= 0xff & rawReadBuffer [i];
		readBuffer [j++] = c;
		if (c == '\r')
		    sawCR = true;
	    }
	} else {	// "UTF-16-BE"
	    for (int i = 0; i < count; i += 2) {
		char c = (char) (rawReadBuffer [i] << 8);
		c |= 0xff & rawReadBuffer [i + 1];
		readBuffer [j++] = c;
		if (c == '\r')
		    sawCR = true;
	    }
	}
	readBufferLength = j;
    }


    /**
     * Convert a buffer of UCS-4-encoded bytes into UTF-16 characters.
     *
     * <p>When readDataChunk () calls this method, the raw bytes are in 
     * rawReadBuffer, and the final characters will appear in 
     * readBuffer.
     * <p>Java has Unicode chars, and this routine uses surrogate pairs
     * for ISO-10646 values between 0x00010000 and 0x000fffff.  An
     * exception is thrown if the ISO-10646 character has no Unicode
     * representation.
     *
     * @param count The number of bytes to convert.
     * @param shift1 The number of bits to shift byte 1.
     * @param shift2 The number of bits to shift byte 2
     * @param shift3 The number of bits to shift byte 2
     * @param shift4 The number of bits to shift byte 2
     * @see #readDataChunk
     * @see #rawReadBuffer
     * @see #readBuffer
     */
    private void copyUcs4ReadBuffer (int count, int shift1, int shift2,
			      int shift3, int shift4)
    throws SAXException
    {
	int j = readBufferPos;
	int value;

	if (count > 0 && (count % 4) != 0) {
	    encodingError (
		    "number of bytes in UCS-4 encoding not divisible by 4",
		    -1, count);
	}
	for (int i = 0; i < count; i += 4) {
	    value = (((rawReadBuffer [i] & 0xff) << shift1) |
		      ((rawReadBuffer [i + 1] & 0xff) << shift2) |
		      ((rawReadBuffer [i + 2] & 0xff) << shift3) |
		      ((rawReadBuffer [i + 3] & 0xff) << shift4));
	    if (value < 0x0000ffff) {
		readBuffer [j++] = (char) value;
		if (value == (int) '\r') {
		    sawCR = true;
		}
	    } else if (value < 0x0010ffff) {
		value -= 0x010000;
		readBuffer [j++] = (char) (0xd8 | ((value >> 10) & 0x03ff));
		readBuffer [j++] = (char) (0xdc | (value & 0x03ff));
	    } else {
		encodingError ("UCS-4 value out of range for Unicode",
			       value, i);
	    }
	}
	readBufferLength = j;
    }


    /**
     * Report a character encoding error.
     */
    private void encodingError (String message, int value, int offset)
    throws SAXException
    {
	String uri;

	if (value != -1) {
	    message = message + " (character code: 0x" +
		      Integer.toHexString (value) + ')';
	}
	if (externalEntity != null) {
	    uri = externalEntity.getURL ().toString ();
	} else {
	    uri = baseURI;
	}
	handler.error (message, uri, -1, offset + currentByteCount);
    }


    //////////////////////////////////////////////////////////////////////
    // Local Variables.
    //////////////////////////////////////////////////////////////////////

    /**
     * Re-initialize the variables for each parse.
     */
    private void initializeVariables ()
    {
	// First line
	line = 1;
	column = 0;

	// Set up the buffers for data and names
	dataBufferPos = 0;
	dataBuffer = new char [DATA_BUFFER_INITIAL];
	nameBufferPos = 0;
	nameBuffer = new char [NAME_BUFFER_INITIAL];

	// Set up the DTD hash tables
	elementInfo = new HashMap ();
	entityInfo = new HashMap ();
	notationInfo = new HashMap ();

	// Set up the variables for the current
	// element context.
	currentElement = null;
	currentElementContent = CONTENT_UNDECLARED;

	// Set up the input variables
	sourceType = INPUT_NONE;
	inputStack = new ArrayList ();
	entityStack = new ArrayList ();
	externalEntity = null;
	tagAttributePos = 0;
	tagAttributes = new String [100];
	rawReadBuffer = new byte [READ_BUFFER_MAX];
	readBufferOverflow = -1;

	inLiteral = false;
	expandPE = false;
	peIsError = false;

	inCDATA = false;

	symbolTable = new Object [SYMBOL_TABLE_LENGTH][];
    }


    /**
     * Clean up after the parse to allow some garbage collection.
     */
    private void cleanupVariables ()
    {
	dataBuffer = null;
	nameBuffer = null;

	elementInfo = null;
	entityInfo = null;
	notationInfo = null;

	currentElement = null;

	inputStack = null;
	entityStack = null;
	externalEntity = null;

	tagAttributes = null;
	rawReadBuffer = null;

	symbolTable = null;
    }

    //
    // The current XML handler interface.
    //
    private SAXDriver	handler;

    //
    // I/O information.
    //
    private Reader	reader; 	// current reader
    private InputStream	is; 		// current input stream
    private int		line; 		// current line number
    private int		column; 	// current column number
    private int		sourceType; 	// type of input source
    private ArrayList	inputStack; 	// stack of input soruces
    private URLConnection externalEntity; // current external entity
    private int		encoding; 	// current character encoding
    private int		currentByteCount; // bytes read from current source

    //
    // Buffers for decoded but unparsed character input.
    //
    private char	readBuffer [];
    private int		readBufferPos;
    private int		readBufferLength;
    private int		readBufferOverflow;  // overflow from last data chunk.


    //
    // Buffer for undecoded raw byte input.
    //
    private final static int READ_BUFFER_MAX = 16384;
    private byte	rawReadBuffer [];


    //
    // Buffer for parsed character data.
    //
    private static int DATA_BUFFER_INITIAL = 4096;
    private char	dataBuffer [];
    private int		dataBufferPos;

    //
    // Buffer for parsed names.
    //
    private static int NAME_BUFFER_INITIAL = 1024;
    private char	nameBuffer [];
    private int		nameBufferPos;


    //
    // HashMaps for DTD information on elements, entities, and notations.
    //
    private HashMap	elementInfo;
    private HashMap	entityInfo;
    private HashMap	notationInfo;


    //
    // Element type currently in force.
    //
    private String	currentElement;
    private int		currentElementContent;

    //
    // Base external identifiers for resolution.
    //
    private String	basePublicId;
    private String	baseURI;
    private int		baseEncoding;
    private Reader	baseReader;
    private InputStream	baseInputStream;
    private char	baseInputBuffer [];
    private int		baseInputBufferStart;
    private int		baseInputBufferLength;

    //
    // Stack of entity names, to detect recursion.
    //
    private ArrayList	entityStack;

    //
    // PE expansion is enabled in most chunks of the DTD, not all.
    // When it's enabled, literals are treated differently.
    //
    private boolean	inLiteral;
    private boolean	expandPE;
    private boolean	peIsError;

    //
    // Symbol table, for caching interned names.
    //
    private final static int SYMBOL_TABLE_LENGTH = 1087;
    private Object	symbolTable [][];

    //
    // Hash table of attributes found in current start tag.
    //
    private String	tagAttributes [];
    private int		tagAttributePos;

    //
    // Utility flag: have we noticed a CR while reading the last
    // data chunk?  If so, we will have to go back and normalise
    // CR or CR/LF line ends.
    //
    private boolean	sawCR;

    //
    // Utility flag: are we in CDATA?  If so, whitespace isn't ignorable.
    // 
    private boolean	inCDATA;
}
