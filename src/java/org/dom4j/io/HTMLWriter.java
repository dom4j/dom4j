/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;
   
import org.dom4j.CDATA;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Node;

import org.xml.sax.SAXException;

/** <p><code>HTMLWriter</code> takes a DOM4J tree and formats it to a
  * stream as HTML.  
  * This formatter is similar to XMLWriter but outputs the text of CDATA 
  * and Entity sections rather than the serialised format as in XML and
  * also supports certain element which have no corresponding close tag such 
  * as for &lt;BR&gt; and &lt;P&gt;.
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a> (james.strachan@metastuff.com)
  * @version $Revision$ 
 */
public class HTMLWriter extends XMLWriter {

    /** Used to store the qualified element names which 
      * should have no close element tag 
      */
    private Set omitElementCloseSet;

    
    public HTMLWriter() {
    }

    public HTMLWriter(Writer writer) {
        super( writer );
    }
    
    public HTMLWriter(OutputStream out) throws UnsupportedEncodingException {
        super( out );
    }
    
    public HTMLWriter(Writer writer, OutputFormat format) {
        super( writer, format );
    }
    
    public HTMLWriter(OutputStream out, OutputFormat format) throws UnsupportedEncodingException {
        super( out, format );
    }
    
    public HTMLWriter(OutputFormat format) {
        super( format );
    }

    
    public void startCDATA() throws SAXException {
    }
    
    public void endCDATA() throws SAXException {
    }
    
    
    
    /** Writes the given {@link CDATA}.
      *
      * @param cdata <code>CDATA</code> to output.
      */
    public void write(CDATA cdata) throws IOException {
        writer.write(cdata.getText());
        lastOutputNodeType = Node.CDATA_SECTION_NODE;
    }
    
    /** Writes the given {@link Entity}.
      *
      * @param entity <code>Entity</code> to output.
      */
    public void write(Entity entity) throws IOException {
        writer.write(entity.getText());
        lastOutputNodeType = Node.ENTITY_REFERENCE_NODE;
    }
    
    protected void writeDeclaration() throws IOException {
    }
    
    
    
    /** Overriden method to not close certain element names to avoid
      * wierd behaviour from browsers for versions up to 5.x
      */
    protected void writeClose(String qualifiedName) throws IOException {
        if ( ! getOmitElementCloseSet().contains( qualifiedName ) ) {
            super.writeClose(qualifiedName);
        }
    }

    protected void writeEmptyElementClose(String qualifiedName) throws IOException {
        if ( ! getOmitElementCloseSet().contains( qualifiedName ) ) {
            super.writeEmptyElementClose(qualifiedName);
        }
        else {
            writer.write(">");
        }
    }
    
    protected Set getOmitElementCloseSet() {
        if (omitElementCloseSet == null) {
            omitElementCloseSet = new HashSet();
            loadOmitElementCloseSet(omitElementCloseSet);
        }
        return omitElementCloseSet;
    }
    
    protected void loadOmitElementCloseSet(Set set) {
        set.add( "p" );
        set.add( "P" );
        set.add( "br" );
        set.add( "BR" );
        set.add( "input" );
        set.add( "INPUT" );
        set.add( "hr" );
        set.add( "HR" );
        set.add( "img" );
        set.add( "IMG" );
    }
    
    protected String getPadText() {
        return " ";
    }
    
    protected boolean isExpandEmptyElements() {
        return true;
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
