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
// Copyright (c) 1998 by Microstar Software Ltd.
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

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.NamespaceSupport;


// $Id$

/**
 * An enhanced SAX2 version of Microstar's &AElig;lfred XML parser.
 * The enhancements primarily relate to significant improvements in
 * conformance to the XML specification, and SAX2 support.  Performance
 * has been improved.  However, the &AElig;lfred proprietary APIs are
 * no longer public.  See the package level documentation for more
 * information.
 *
 * <table border="1" width='100%' cellpadding='3' cellspacing='0'>
 * <tr bgcolor='#ccccff'>
 *  <th><font size='+1'>Name</font></th>
 *  <th><font size='+1'>Notes</font></th></tr>
 *
 * <tr><td colspan=2><center><em>Features ... URL prefix is
 * <b>http://xml.org/sax/features/</b></em></center></td></tr>
 *
 * <tr><td>(URL)/external-general-entities</td>
 *  <td>Value is fixed at <em>true</em></td></tr>
 * <tr><td>(URL)/external-parameter-entities</td>
 *  <td>Value is fixed at <em>true</em></td></tr>
 * <tr><td>(URL)/namespace-prefixes</td>
 *  <td>Value defaults to <em>false</em> (but XML 1.0 names are
 *      always reported)</td></tr>
 * <tr><td>(URL)/namespaces</td>
 *  <td>Value defaults to <em>true</em></td></tr>
 * <tr><td>(URL)/string-interning</td>
 *  <td>Value is fixed at <em>true</em></td></tr>
 * <tr><td>(URL)/validation</td>
 *  <td>Value is fixed at <em>false</em></td></tr>
 *
 * <tr><td colspan=2><center><em>Handler Properties ... URL prefix is
 * <b>http://xml.org/sax/properties/</b></em></center></td></tr>
 *
 * <tr><td>(URL)/declaration-handler</td>
 *  <td>A declaration handler may be provided.  Declaration of general
 *  entities is exposed, but not parameter entities; none of the entity
 *  names reported here will begin with "%". </td></tr>
 * <tr><td>(URL)/lexical-handler</td>
 *  <td>A lexical handler may be provided.  Entity boundaries and
 *  comments are not exposed; only CDATA sections and the start/end of
 *  the DTD (the internal subset is not detectible). </td></tr>
 * </table>
 *
 * <p> Note that the declaration handler doesn't suffice for showing all
 * the logical structure
 * of the DTD; it doesn't expose the name of the root element, or the values
 * that are permitted in a NOTATIONS attribute.  (The former is exposed as
 * lexical data, and SAX2 beta doesn't expose the latter.)
 *
 * <p> Although support for several features and properties is "built in"
 * to this parser, it support all others by storing the assigned values
 * and returning them.
 *
 * <p>This parser currently implements the SAX1 Parser API, but
 * it may not continue to do so in the future.
 *
 * @author Written by David Megginson &lt;dmeggins@microstar.com&gt;
 *  (version 1.2a from Microstar)
 * @author Updated by David Brownell &lt;david-b@pacbell.net&gt;
 * @version $Date$
 * @see org.xml.sax.Parser
 */
final public class SAXDriver
    implements Locator, Attributes, XMLReader, Parser, AttributeList
{
    private final DefaultHandler    base = new DefaultHandler ();
    private XmlParser           parser;

    private EntityResolver      entityResolver = base;
    private ContentHandler      contentHandler = base;
    private DTDHandler          dtdHandler = base;
    private ErrorHandler        errorHandler = base;
    private DeclHandler         declHandler = base;
    private LexicalHandler      lexicalHandler = base;

    private String          elementName = null;
    private ArrayList           entityStack = new ArrayList ();

    private ArrayList           attributeNames = new ArrayList ();
    private ArrayList           attributeNamespaces = new ArrayList ();
    private ArrayList           attributeLocalNames = new ArrayList ();
    private ArrayList           attributeValues = new ArrayList ();

    private boolean         namespaces = true;
    private boolean         xmlNames = false;
    private boolean         nspending = false; // indicates an attribute was read before its
                                               // namespace declaration

    private int             attributeCount = 0;
    private String          nsTemp [] = new String [3];
    private NamespaceSupport        prefixStack = new NamespaceSupport ();

    private HashMap             features;
    private HashMap             properties;


    //
    // Constructor.
    //

    /** Constructs a SAX Parser.  */
    public SAXDriver () {}


    //
    // Implementation of org.xml.sax.Parser.
    //

    /**
     * <b>SAX1</b>: Sets the locale used for diagnostics; currently,
     * only locales using the English language are supported.
     * @param locale The locale for which diagnostics will be generated
     */
    public void setLocale (Locale locale)
    throws SAXException
    {
    if ("en".equals (locale.getLanguage ()))
        return ;

    throw new SAXException ("AElfred only supports English locales.");
    }


    /**
     * <b>SAX2</b>: Returns the object used when resolving external
     * entities during parsing (both general and parameter entities).
     */
    public EntityResolver getEntityResolver ()
    {
    return entityResolver;
    }

    /**
     * <b>SAX1, SAX2</b>: Set the entity resolver for this parser.
     * @param handler The object to receive entity events.
     */
    public void setEntityResolver (EntityResolver resolver)
    {
    if (resolver == null)
        resolver = base;
    this.entityResolver = resolver;
    }


    /**
     * <b>SAX2</b>: Returns the object used to process declarations related
     * to notations and unparsed entities.
     */
    public DTDHandler getDTDHandler ()
    {
    return dtdHandler;
    }

    /**
     * <b>SAX1, SAX2</b>: Set the DTD handler for this parser.
     * @param handler The object to receive DTD events.
     */
    public void setDTDHandler (DTDHandler handler)
    {
    if (handler == null)
        handler = base;
    this.dtdHandler = handler;
    }


    /**
     * <b>SAX1</b>: Set the document handler for this parser.  If a
     * content handler was set, this document handler will supplant it.
     * The parser is set to report all XML 1.0 names rather than to
     * filter out "xmlns" attributes (the "namespace-prefixes" feature
     * is set to true).
     *
     * @deprecated SAX2 programs should use the XMLReader interface
     *  and a ContentHandler.
     *
     * @param handler The object to receive document events.
     */
    public void setDocumentHandler (DocumentHandler handler)
    {
    contentHandler = new Adapter (handler);
    xmlNames = true;
    }

    /**
     * <b>SAX2</b>: Returns the object used to report the logical
     * content of an XML document.
     */
    public ContentHandler getContentHandler ()
    {
    return contentHandler;
    }

    /**
     * <b>SAX2</b>: Assigns the object used to report the logical
     * content of an XML document.  If a document handler was set,
     * this content handler will supplant it (but XML 1.0 style name
     * reporting may remain enabled).
     */
    public void setContentHandler (ContentHandler handler)
    {
        if (handler == null)
            handler = base;
        contentHandler = handler;
    }

    /**
     * <b>SAX1, SAX2</b>: Set the error handler for this parser.
     * @param handler The object to receive error events.
     */
    public void setErrorHandler (ErrorHandler handler)
    {
        if (handler == null)
            handler = base;
        this.errorHandler = handler;
    }

    /**
     * <b>SAX2</b>: Returns the object used to receive callbacks for XML
     * errors of all levels (fatal, nonfatal, warning); this is never null;
     */
    public ErrorHandler getErrorHandler ()
    {
        return errorHandler;
    }


    /**
     * <b>SAX1, SAX2</b>: Auxiliary API to parse an XML document, used mostly
     * when no URI is available.
     * If you want anything useful to happen, you should set
     * at least one type of handler.
     * @param source The XML input source.  Don't set 'encoding' unless
     *  you know for a fact that it's correct.
     * @see #setEntityResolver
     * @see #setDTDHandler
     * @see #setContentHandler
     * @see #setErrorHandler
     * @exception SAXException The handlers may throw any SAXException,
     *  and the parser normally throws SAXParseException objects.
     * @exception IOException IOExceptions are normally through through
     *  the parser if there are problems reading the source document.
     */
     
    public void parse (InputSource source) throws SAXException, IOException
    {
        synchronized (base) {
            parser = new XmlParser ();
            parser.setHandler (this);

            try {
                String  systemId = source.getSystemId ();

                // duplicate first entry, in case startDocument handler
                // needs to use Locator.getSystemId(), before entities
                // start to get reported by the parser

                if (systemId != null)
                    entityStack.add (systemId);
                else
                    entityStack.add ("illegal:unknown system ID");

                parser.doParse (systemId,
                          source.getPublicId (),
                          source.getCharacterStream (),
                          source.getByteStream (),
                          source.getEncoding ());
            } catch (SAXException e) {
                throw e;
            } catch (IOException e) {
                throw e;
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new SAXException (e.getMessage (), e);
            } finally {
                contentHandler.endDocument ();
                entityStack.clear ();
            }
        }
    }


    /**
     * <b>SAX1, SAX2</b>: Preferred API to parse an XML document, using a
     * system identifier (URI).
     */
     
    public void parse (String systemId) throws SAXException, IOException
    {
        parse (new InputSource (systemId));
    }

    //
    // Implementation of SAX2 "XMLReader" interface
    //
    static final String FEATURE = "http://xml.org/sax/features/";
    static final String HANDLER = "http://xml.org/sax/properties/";

    /**
     * <b>SAX2</b>: Tells the value of the specified feature flag.
     *
     * @exception SAXNotRecognizedException thrown if the feature flag
     *  is neither built in, nor yet assigned.
     */
    public boolean getFeature (String featureId)
    throws SAXNotRecognizedException
    {
    if ((FEATURE + "validation").equals (featureId))
        return false;

    // external entities (both types) are currently always included
    if ((FEATURE + "external-general-entities").equals (featureId)
        || (FEATURE + "external-parameter-entities")
        .equals (featureId))
        return true;

    // element/attribute names are as written in document; no mangling
    if ((FEATURE + "namespace-prefixes").equals (featureId))
        return xmlNames;

    // report element/attribute namespaces?
    if ((FEATURE + "namespaces").equals (featureId))
        return namespaces;

    // XXX always provides a locator ... removed in beta

    // always interns
    if ((FEATURE + "string-interning").equals (featureId))
        return true;

    if (features != null && features.containsKey (featureId))
        return ((Boolean)features.get (featureId)).booleanValue ();

    throw new SAXNotRecognizedException (featureId);
    }

    /**
     * <b>SAX2</b>:  Returns the specified property.
     *
     * @exception SAXNotRecognizedException thrown if the property value
     *  is neither built in, nor yet stored.
     */
    public Object getProperty (String propertyId)
    throws SAXNotRecognizedException
    {
    if ((HANDLER + "declaration-handler").equals (propertyId))
        return declHandler;

    if ((HANDLER + "lexical-handler").equals (propertyId))
        return lexicalHandler;
    
    if (properties != null && properties.containsKey (propertyId))
        return properties.get (propertyId);

    // unknown properties
    throw new SAXNotRecognizedException (propertyId);
    }

    /**
     * <b>SAX2</b>:  Sets the state of feature flags in this parser.  Some
     * built-in feature flags are mutable; all flags not built-in are
     * motable.
     */
    public void setFeature (String featureId, boolean state)
    throws SAXNotRecognizedException, SAXNotSupportedException
    {
    boolean value;
    
    try {
        // Features with a defined value, we just change it if we can.
        value = getFeature (featureId);

        if (state == value)
        return;

        if ((FEATURE + "namespace-prefixes").equals (featureId)) {
        // in this implementation, this only affects xmlns reporting
        xmlNames = state;
        return;
        }

        if ((FEATURE + "namespaces").equals (featureId)) {
        // XXX if not currently parsing ...
        if (true) {
            namespaces = state;
            return;
        }
        // if in mid-parse, critical info hasn't been computed/saved
        }

        // can't change builtins
        if (features == null || !features.containsKey (featureId))
        throw new SAXNotSupportedException (featureId);

    } catch (SAXNotRecognizedException e) {
        // as-yet unknown features
        if (features == null)
        features = new HashMap (5);
    }

    // record first value, or modify existing one
    features.put (featureId, 
        state
        ? Boolean.TRUE
        : Boolean.FALSE);
    }

    /**
     * <b>SAX2</b>:  Assigns the specified property.  Like SAX1 handlers,
     * these may be changed at any time.
     */
    public void setProperty (String propertyId, Object property)
    throws SAXNotRecognizedException, SAXNotSupportedException
    {
    Object  value;
    
    try {
        // Properties with a defined value, we just change it if we can.
        value = getProperty (propertyId);

        if ((HANDLER + "declaration-handler").equals (propertyId)) {
            if (property == null)
                declHandler = base;
            else if (! (property instanceof DeclHandler))
                throw new SAXNotSupportedException (propertyId);
            else
                declHandler = (DeclHandler) property;
            return ;
        }

        if ((HANDLER + "lexical-handler").equals (propertyId)) {
            if (property == null)
                lexicalHandler = base;
            else if (! (property instanceof LexicalHandler))
                throw new SAXNotSupportedException (propertyId);
            else
                lexicalHandler = (LexicalHandler) property;
            return ;
        }

        // can't change builtins
        if (properties == null || !properties.containsKey (propertyId))
        throw new SAXNotSupportedException (propertyId);

    } catch (SAXNotRecognizedException e) {
        // as-yet unknown properties
        if (properties == null)
        properties = new HashMap (5);
    }

    // record first value, or modify existing one
    properties.put (propertyId, property);
    }


    //
    // This is where the driver receives AElfred callbacks and translates
    // them into SAX callbacks.  Some more callbacks have been added for
    // SAX2 support.
    //

    // NOTE:  in some cases, local copies of handlers are
    // created and used, to work around codegen bugs in at
    // least one snapshot version of GCJ.

    void startDocument () throws SAXException
    {
        contentHandler.setDocumentLocator (this);
        contentHandler.startDocument ();
        attributeNames.clear ();
        attributeValues.clear ();
    }

    void endDocument () throws SAXException
    {
        // SAX says endDocument _must_ be called (handy to close
        // files etc) so it's in a "finally" clause
    }

    Object resolveEntity (String publicId, String systemId)
    throws SAXException, IOException
    {
        InputSource source = entityResolver.resolveEntity (publicId,
                     systemId);

        if (source == null) {
            return null;
        } else if (source.getCharacterStream () != null) {
            return source.getCharacterStream ();
        } else if (source.getByteStream () != null) {
            if (source.getEncoding () == null)
            return source.getByteStream ();
            else try {
            return new InputStreamReader (
                source.getByteStream (),
                source.getEncoding ()
                );
            } catch (IOException e) {
            return source.getByteStream ();
            }
        } else {
            return source.getSystemId ();
        }
        // XXX no way to tell AElfred about new public
        // or system ids ... so relative URL resolution
        // through that entity could be less than reliable.
    }


    void startExternalEntity (String systemId)
    throws SAXException
    {
        entityStack.add (systemId);
    }

    void endExternalEntity (String systemId)
    throws SAXException
    {
        entityStack.remove ( entityStack.size() - 1 );
    }

    void doctypeDecl (String name, String publicId, String systemId)
    throws SAXException
    {
        lexicalHandler.startDTD (name, publicId, systemId);
    
        // ... the "name" is a declaration and should be given
        // to the DeclHandler (but sax2 beta doesn't).

        // the IDs for the external subset are lexical details,
        // as are the contents of the internal subset; but sax2
        // beta only provides the external subset "pre-parse"
    }

    void endDoctype () throws SAXException
    {
        // NOTE:  some apps may care that comments and PIs,
        // are stripped from their DTD declaration context,
        // and that those declarations are themselves quite
        // thoroughly reordered here.

        deliverDTDEvents ();
        lexicalHandler.endDTD ();
    }


    void attribute (String aname, String value, boolean isSpecified)
    throws SAXException
    {
        if (attributeCount++ == 0) {
            if (namespaces)
                prefixStack.pushContext ();
        }

        // set nsTemp [0] == namespace URI (or empty)
        // set nsTemp [1] == local name (or empty)
        if (value != null) {
            if (namespaces) {
                int index = aname.indexOf (':');

                // prefixed name?
                if (index > 0) {
                    // prefix declaration?
                    if (index == 5 && aname.startsWith ("xmlns")) {
                        String      prefix = aname.substring (index + 1);

                        if (value.length () == 0) {
                            errorHandler.error (new SAXParseException (
                                "missing URI in namespace decl attribute: "
                                + aname,
                                this));
                        } else {
                            prefixStack.declarePrefix (prefix, value);
                            contentHandler.startPrefixMapping (prefix, value);
                        }
                        if (!xmlNames)
                            return;
                        nsTemp [0] = "";
                        nsTemp [1] = aname;

                    // prefix reference
                    } else {
                        if (prefixStack.processName (aname, nsTemp, true)
                            == null) {
                            // start of MHK code
                                nsTemp[0] = "";
                                nsTemp[1] = aname;    // defer checking till later
                                nspending = true;
                            // end of MHK code
                            // start of previous code    
                            // errorHandler.error (new SAXParseException (
                            //    "undeclared name prefix in: " + aname,
                            //    this));
                            // nsTemp [0] = nsTemp [1] = "";
                            // end of previous code    
                        } // else nsTemp [0, 1] received { uri, local }
                    }

                // no prefix
                } else {
                    // default declaration?
                    if ("xmlns".equals (aname)) {
                    prefixStack.declarePrefix ("", value);
                    contentHandler.startPrefixMapping ("", value);
                    if (!xmlNames)
                        return;
                    }
                    nsTemp [0] = "";
                    nsTemp [1] = aname;
                }
            } else
                nsTemp [0] = nsTemp [1] = "";

            attributeNamespaces.add (nsTemp [0]);
            attributeLocalNames.add (nsTemp [1]);
            attributeNames.add (aname);
            // attribute type comes from querying parser's DTD records
            attributeValues.add (value);
        }
    }

    void startElement (String elname)
    throws SAXException
    {
        ContentHandler handler = contentHandler;

        //
        // NOTE:  this implementation of namespace support adds something
        // like six percent to parsing CPU time, in a large (~50 MB)
        // document that doesn't use namespaces at all.  (Measured by PC
        // sampling.)
        //
        // It ought to become notably faster in such cases.  Most
        // costs are the prefix stack calling Hashtable.get() (2%),
        // String.hashCode() (1.5%) and about 1.3% each for pushing
        // the context, and two chunks of name processing.
        //

        if (attributeCount == 0)
            prefixStack.pushContext ();

        // save element name so attribute callbacks work
        elementName = elname;
        if (namespaces) {
            // START MHK CODE
            // Check that namespace prefixes for attributes are valid
            if (attributeCount > 0 && nspending) {
                for (int i=0; i<attributeLocalNames.size(); i++) {
                    String aname = (String)attributeLocalNames.get(i);
                    if (aname.indexOf(':')>0) {

                        if (prefixStack.processName (aname, nsTemp, true) == null) {
                            errorHandler.error (new SAXParseException (
                                "undeclared name prefix in: " + aname,
                                this));
                        } else {
                            attributeNamespaces.set(i, nsTemp[0]);
                            attributeLocalNames.set(i, nsTemp[1]);
                        }
                    }
                }
            }
            // END MHK CODE

            if (prefixStack.processName (elname, nsTemp, false) == null) {
            errorHandler.error (new SAXParseException (
                "undeclared name prefix in: " + elname,
                this));
            nsTemp [0] = nsTemp [1] = "";
            }

            handler.startElement (nsTemp [0], nsTemp [1], elname, this);
        } else
            handler.startElement ("", "", elname, this);
        // elementName = null;

        // elements with no attributes are pretty common!
        if (attributeCount != 0) {
            attributeNames.clear ();
            attributeNamespaces.clear ();
            attributeLocalNames.clear ();
            attributeValues.clear ();
            attributeCount = 0;
        }
        nspending = false;
    }

    void endElement (String elname)
    throws SAXException
    {
        ContentHandler  handler = contentHandler;

        handler.endElement ("", "", elname);

        if (!namespaces)
            return;

        Enumeration prefixes = prefixStack.getDeclaredPrefixes ();

        while (prefixes.hasMoreElements ())
            handler.endPrefixMapping ((String) prefixes.nextElement ());
        prefixStack.popContext ();
    }

    void startCDATA ()
    throws SAXException
    {
        lexicalHandler.startCDATA ();
    }

    void charData (char ch[], int start, int length)
    throws SAXException
    {
        contentHandler.characters (ch, start, length);
    }

    void endCDATA ()
    throws SAXException
    {
        lexicalHandler.endCDATA ();
    }

    void ignorableWhitespace (char ch[], int start, int length)
    throws SAXException
    {
        contentHandler.ignorableWhitespace (ch, start, length);
    }

    void processingInstruction (String target, String data)
    throws SAXException
    {
        // XXX if within DTD, perhaps it's best to discard
        // PIs since the decls to which they (usually)
        // apply get significantly rearranged

        contentHandler.processingInstruction (target, data);
    }

    void comment (char ch[], int start, int length)
    throws SAXException
    {
        // XXX if within DTD, perhaps it's best to discard
        // comments since the decls to which they (usually)
        // apply get significantly rearranged

        if (lexicalHandler != base)
            lexicalHandler.comment (ch, start, length);
    }

        // AElfred only has fatal errors
        void error (String message, String url, int line, int column)
        throws SAXException
        {
        SAXParseException fatal;
    
        fatal = new SAXParseException (message, null, url, line, column);
        errorHandler.fatalError (fatal);

        // Even if the application can continue ... we can't!
        throw fatal;
    }


    //
    // Before the endDtd event, deliver all non-PE declarations.
    //
    private void deliverDTDEvents ()
    throws SAXException
    {
    String  ename;
    String  nname;
    String publicId;
    String  systemId;

    // First, report all notations.
    if (dtdHandler != base) {
        Iterator    notationNames = parser.declaredNotations ();

        while (notationNames.hasNext ()) {
        nname = (String) notationNames.next ();
        publicId = parser.getNotationPublicId (nname);
        systemId = parser.getNotationSystemId (nname);
        dtdHandler.notationDecl (nname, publicId, systemId);
        }
    }

    // Next, report all entities.
    if (dtdHandler != base || declHandler != base) {
        Iterator    entityNames = parser.declaredEntities ();
        int type;

        while (entityNames.hasNext ()) {
        ename = (String) entityNames.next ();
        type = parser.getEntityType (ename);

        if (ename.charAt (0) == '%')
            continue;

        // unparsed
        if (type == XmlParser.ENTITY_NDATA) {
            publicId = parser.getEntityPublicId (ename);
            systemId = parser.getEntitySystemId (ename);
            nname = parser.getEntityNotationName (ename);
            dtdHandler.unparsedEntityDecl (ename,
                            publicId, systemId, nname);

            // external parsed
        }
        else if (type == XmlParser.ENTITY_TEXT) {
            publicId = parser.getEntityPublicId (ename);
            systemId = parser.getEntitySystemId (ename);
            declHandler.externalEntityDecl (ename,
                             publicId, systemId);

            // internal parsed
        }
        else if (type == XmlParser.ENTITY_INTERNAL) {
            // filter out the built-ins; even if they were
            // declared, they didn't need to be.
            if ("lt".equals (ename) || "gt".equals (ename)
                || "quot".equals (ename)
                || "apos".equals (ename)
                || "amp".equals (ename))
            continue;
            declHandler.internalEntityDecl (ename,
                 parser.getEntityValue (ename));
        }
        }
    }

    // elements, attributes
    if (declHandler != base) {
        Iterator    elementNames = parser.declaredElements ();
        Iterator    attNames;

        while (elementNames.hasNext ()) {
        String model = null;

        ename = (String) elementNames.next ();
        switch (parser.getElementContentType (ename)) {
            case XmlParser.CONTENT_ANY:
            model = "ANY";
            break;
            case XmlParser.CONTENT_EMPTY:
            model = "EMPTY";
            break;
            case XmlParser.CONTENT_MIXED:
            case XmlParser.CONTENT_ELEMENTS:
            model = parser.getElementContentModel (ename);
            break;
            case XmlParser.CONTENT_UNDECLARED:
            default:
            model = null;
            break;
        }
        if (model != null)
            declHandler.elementDecl (ename, model);

        attNames = parser.declaredAttributes (ename);
        while (attNames != null && attNames.hasNext ()) {
            String aname = (String) attNames.next ();
            String type;
            String valueDefault;
            String value;

            switch (parser.getAttributeType (ename, aname)) {
            case XmlParser.ATTRIBUTE_CDATA:
            type = "CDATA";
            break;
            case XmlParser.ATTRIBUTE_ENTITY:
            type = "ENTITY";
            break;
            case XmlParser.ATTRIBUTE_ENTITIES:
            type = "ENTITIES";
            break;
            case XmlParser.ATTRIBUTE_ENUMERATED:
            type = parser.getAttributeIterator (ename, aname);
            break;
            case XmlParser.ATTRIBUTE_ID:
            type = "ID";
            break;
            case XmlParser.ATTRIBUTE_IDREF:
            type = "IDREF";
            break;
            case XmlParser.ATTRIBUTE_IDREFS:
            type = "IDREFS";
            break;
            case XmlParser.ATTRIBUTE_NMTOKEN:
            type = "NMTOKEN";
            break;
            case XmlParser.ATTRIBUTE_NMTOKENS:
            type = "NMTOKENS";
            break;

            // XXX SAX2 beta doesn't have a way to return the
            // enumerated list of permitted notations ... SAX1
            // kluged it as NMTOKEN, but that won't work for
            // the sort of apps that really use the DTD info
            case XmlParser.ATTRIBUTE_NOTATION:
            type = "NOTATION";
            break;

            default:
            errorHandler.fatalError (new SAXParseException (
                  "internal error, att type", this));
            type = null;
            }

            switch (parser.getAttributeDefaultValueType (
                 ename, aname)) {
            case XmlParser.ATTRIBUTE_DEFAULT_IMPLIED:
            valueDefault = "#IMPLIED";
            break;
            case XmlParser.ATTRIBUTE_DEFAULT_REQUIRED:
            valueDefault = "#REQUIRED";
            break;
            case XmlParser.ATTRIBUTE_DEFAULT_FIXED:
            valueDefault = "#FIXED";
            break;
            case XmlParser.ATTRIBUTE_DEFAULT_SPECIFIED:
            valueDefault = null;
            break;

            default:
            errorHandler.fatalError (new SAXParseException (
                    "internal error, att default", this));
            valueDefault = null;
            }

            value = parser.getAttributeDefaultValue (ename, aname);

            declHandler.attributeDecl (ename, aname,
                        type, valueDefault, value);
        }
        }
    }
    }


    //
    // Implementation of org.xml.sax.Attributes.
    //

    /**
     * <b>SAX1 AttributeList, SAX2 Attributes</b> method
     * (don't invoke on parser);
     */
    public int getLength ()
    {
        return attributeNames.size ();
    }

    /**
     * <b>SAX2 Attributes</b> method (don't invoke on parser);
     */
    public String getURI (int index)
    {
        return (String) (attributeNamespaces.get (index));
    }

    /**
     * <b>SAX2 Attributes</b> method (don't invoke on parser);
     */
    public String getLocalName (int index)
    {
        return (String) (attributeLocalNames.get (index));
    }

    /**
     * <b>SAX2 Attributes</b> method (don't invoke on parser);
     */
    public String getQName (int i)
    {
        return (String) (attributeNames.get (i));
    }

    /**
     * <b>SAX1 AttributeList</b> method (don't invoke on parser);
     */
    public String getName (int i)
    {
        return (String) (attributeNames.get (i));
    }

    /**
     * <b>SAX1 AttributeList, SAX2 Attributes</b> method
     * (don't invoke on parser);
     */
    public String getType (int i)
    {
    switch (parser.getAttributeType (elementName, getQName (i))) {

    case XmlParser.ATTRIBUTE_UNDECLARED:
    case XmlParser.ATTRIBUTE_CDATA:
        return "CDATA";
    case XmlParser.ATTRIBUTE_ID:
        return "ID";
    case XmlParser.ATTRIBUTE_IDREF:
        return "IDREF";
    case XmlParser.ATTRIBUTE_IDREFS:
        return "IDREFS";
    case XmlParser.ATTRIBUTE_ENTITY:
        return "ENTITY";
    case XmlParser.ATTRIBUTE_ENTITIES:
        return "ENTITIES";
    case XmlParser.ATTRIBUTE_ENUMERATED:
        // XXX doesn't have a way to return permitted enum values,
        // though they must each be a NMTOKEN 
    case XmlParser.ATTRIBUTE_NMTOKEN:
        return "NMTOKEN";
    case XmlParser.ATTRIBUTE_NMTOKENS:
        return "NMTOKENS";
    case XmlParser.ATTRIBUTE_NOTATION:
        // XXX doesn't have a way to return the permitted values,
        // each of which must be name a declared notation
        return "NOTATION";

    }

    return null;
    }


    /**
     * <b>SAX1 AttributeList, SAX2 Attributes</b> method
     * (don't invoke on parser);
     */
    public String getValue (int i)
    {
        return (String) (attributeValues.get (i));
    }


    /**
     * <b>SAX2 Attributes</b> method (don't invoke on parser);
     */
    public int getIndex (String uri, String local)
    {
        int length = getLength ();

        for (int i = 0; i < length; i++) {
            if (!getURI (i).equals (uri))
            continue;
            if (getLocalName (i).equals (local))
            return i;
        }
        return -1;
    }


    /**
     * <b>SAX2 Attributes</b> method (don't invoke on parser);
     */
    public int getIndex (String xmlName)
    {
        int length = getLength ();

        for (int i = 0; i < length; i++) {
            if (getQName (i).equals (xmlName))
            return i;
        }
        return -1;
    }


    /**
     * <b>SAX2 Attributes</b> method (don't invoke on parser);
     */
    public String getType (String uri, String local)
    {
        int index = getIndex (uri, local);

        if (index < 0)
            return null;
        return getType (index);
    }


    /**
     * <b>SAX1 AttributeList, SAX2 Attributes</b> method
     * (don't invoke on parser);
     */
    public String getType (String xmlName)
    {
        int index = getIndex (xmlName);

        if (index < 0)
            return null;
        return getType (index);
    }


    /**
     * <b>SAX Attributes</b> method (don't invoke on parser);
     */
    public String getValue (String uri, String local)
    {
        int index = getIndex (uri, local);

        if (index < 0)
            return null;
        return getValue (index);
    }


    /**
     * <b>SAX1 AttributeList, SAX2 Attributes</b> method
     * (don't invoke on parser);
     */
    public String getValue (String xmlName)
    {
        int index = getIndex (xmlName);

        if (index < 0)
            return null;
        return getValue (index);
    }


    //
    // Implementation of org.xml.sax.Locator.
    //

    /**
     * <b>SAX Locator</b> method (don't invoke on parser);
     */
    public String getPublicId ()
    {
        return null;        // XXX track public IDs too
    }

    /**
     * <b>SAX Locator</b> method (don't invoke on parser);
     */
    public String getSystemId ()
    {
        return (String) entityStack.get ( entityStack.size() - 1 );
    }

    /**
     * <b>SAX Locator</b> method (don't invoke on parser);
     */
    public int getLineNumber ()
    {
        return parser.getLineNumber ();
    }

    /**
     * <b>SAX Locator</b> method (don't invoke on parser);
     */
    public int getColumnNumber ()
    {
        return parser.getColumnNumber ();
    }

    // adapter between content handler and document handler callbacks
    
    private static class Adapter implements ContentHandler
    {
        private DocumentHandler     docHandler;

        Adapter (DocumentHandler dh)
            { docHandler = dh; }


        public void setDocumentLocator (Locator l)
            { docHandler.setDocumentLocator (l); }
    
        public void startDocument () throws SAXException
            { docHandler.startDocument (); }
    
        public void processingInstruction (String target, String data)
        throws SAXException
            { docHandler.processingInstruction (target, data); }
    
        public void startPrefixMapping (String prefix, String uri)
            { /* ignored */ }

        public void startElement (
            String  namespace,
            String  local,
            String  name,
            Attributes  attrs ) throws SAXException
        {
            docHandler.startElement (name, (AttributeList) attrs);
        }

        public void characters (char buf [], int offset, int len)
        throws SAXException
        {
            docHandler.characters (buf, offset, len);
        }

        public void ignorableWhitespace (char buf [], int offset, int len)
        throws SAXException
        {
            docHandler.ignorableWhitespace (buf, offset, len);
        }

        public void skippedEntity (String name)
            { /* ignored */ }

        public void endElement (String u, String l, String name)
        throws SAXException
            { docHandler.endElement (name); }

        public void endPrefixMapping (String prefix)
            { /* ignored */ }

        public void endDocument () throws SAXException
            { docHandler.endDocument (); }
    }
}

