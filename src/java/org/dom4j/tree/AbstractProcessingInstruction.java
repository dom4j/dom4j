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
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.dom4j.Element;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Visitor;

/** <p><code>AbstractProcessingInstruction</code> is an abstract base class for 
  * tree implementors to use for implementation inheritence.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class AbstractProcessingInstruction extends AbstractNode implements ProcessingInstruction {

    public AbstractProcessingInstruction() {
    }
    
    public short getNodeType() {
        return PROCESSING_INSTRUCTION_NODE;
    }

    public String getPath(Element context) {
        Element parent = getParent();
        return ( parent != null && parent != context ) 
            ? parent.getPath( context ) + "/processing-instruction()"
            : "processing-instruction()";
    }
    
    public String getUniquePath(Element context) {
        Element parent = getParent();
        return ( parent != null && parent != context ) 
            ? parent.getUniquePath( context ) + "/processing-instruction()"
            : "processing-instruction()";
    }
    
    public String toString() {
        return super.toString() + " [ProcessingInstruction: &" + getName() + ";]";
    }

    public String asXML() {
        return "<?" + getName() + " " + getText() + "?>";
    }
    
    public void write(Writer writer) throws IOException {
        writer.write( "<?" );
        writer.write( getName() );
        writer.write( " " );
        writer.write( getText() );
        writer.write( "?>" );
    }
    
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void setValue(String name, String value) {
        throw new UnsupportedOperationException( 
            "This PI is read-only and cannot be modified" 
        );
    }
    
    public void setValues(Map data) {
        throw new UnsupportedOperationException( 
            "This PI is read-only and cannot be modified" 
        );
    }
    
    public String getName() {
        return getTarget();
    }
    
    public void setName(String name) {
        setTarget(name);
    }
    
    public boolean removeValue(String name) {
        return false;
    }
    
    
    // Helper methods
    
    /** <p>This will convert the Map to a string representation.</p>
      *
      * @param values is a <code>Map</code> of PI data to convert
      */
    protected String toString(Map values) {
        StringBuffer buffer = new StringBuffer();
        
        for ( Iterator iter = values.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iter.next();
            String name = (String) entry.getKey();
            String value = (String) entry.getValue();
            
            buffer.append(name);
            buffer.append("=\"");
            buffer.append(value);
            buffer.append("\" ");
        }
        // remove the last space
        buffer.setLength(buffer.length() - 1);
        return buffer.toString();
    }

    /**<p>Parses the raw data of PI as a <code>Map</code>.</p>
      *
      * @param text <code>String</code> PI data to parse
      */
    protected Map parseValues(String text) {
        Map data = new HashMap();

        // Break up name/value pairs
        StringTokenizer s =
            new StringTokenizer(text);

        // Iterate through the pairs
        while (s.hasMoreTokens()) {
            // Now break up pair on the = and (" or ') characters
            StringTokenizer t =
                new StringTokenizer(s.nextToken(), "='\"");

            if (t.countTokens() >= 2) {
                String name = t.nextToken();
                String value = t.nextToken();

                data.put(name, value);
            }
        }
        return data;
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
