/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.tool.dtd;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** <p><code>ElementDeclaration</code> is a JavaBean representing the element
  * declaration taken from a DTD.</p>
  *
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class AttributeDecl {

    /** The element name. */
    private String elementName;
    
    /** The attribute name. */
    private String attributeName;
    
    /** The type of the attribute . */
    private String type;
    
    /** The default value of the attribute. */
    private String defaultValue;

    /** The value of the attribute. */
    private String value;
    
    /** The element declaration to which this attribute belongs */
    private ElementDecl elementDecl;
    
    public AttributeDecl() {
    }
    
    public AttributeDecl(String elementName, String attributeName, String type, String defaultValue, String value) {
        this.elementName = elementName;
        this.attributeName = attributeName;
        this.type = type;
        this.defaultValue = defaultValue;
        this.value = value;
    }

    
    /** Getter for property attributeName.
      * @return Value of property attributeName.
      */
    public String getAttributeName() {
        return attributeName;
    }
    
    /** Setter for property attributeName.
      * @param attributeName New value of property attributeName.
      */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
    
    /** Getter for property elementName.
      * @return Value of property elementName.
      */
    public String getElementName() {
        return elementName;
    }
    
    /** Setter for property elementName.
      * @param elementName New value of property elementName.
      */
    public void setElementName(String elementName) {
        this.elementName = elementName;
    }
    
    /** Getter for property value.
      * @return Value of property value.
      */
    public String getValue() {
        return value;
    }
    
    /** Setter for property value.
      * @param value New value of property value.
      */
    public void setValue(String value) {
        this.value = value;
    }
    
    /** Getter for property type.
     * @return Value of property type.
     */
    public String getType() {
        return type;
    }
    
    /** Setter for property type.
     * @param type New value of property type.
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /** Getter for property defaultValue.
     * @return Value of property defaultValue.
     */
    public String getDefaultValue() {
        return defaultValue;
    }
    
    /** Setter for property defaultValue.
     * @param defaultValue New value of property defaultValue.
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    /** @return the element declaration this attribute declaration refers to 
      */
    public ElementDecl getElementDecl() {
        return elementDecl;
    }

    protected void setElementDecl(ElementDecl elementDecl) {
        this.elementDecl = elementDecl;
    }
    /** Writes the state of the model to the given writer
      * 
      * @param out is the writer to output to
      */
    public void write( PrintWriter out ) {
        out.print( "  " + getAttributeName() + " " + getType() + " " );
        String value = getValue();
        if ( value != null ) {
            out.print( value );
        }
        else {
            out.print( getDefaultValue() );
        }
        out.println();
    }
    
    /** @return true if this attribute is declared in a namespace 
     */
    public boolean hasNamespace() {
        int idx = getAttributeName().indexOf(':');
        return idx >= 0;
    }
    
    public String getNamespacePrefix() {
        String name = getAttributeName();
        int idx = name.indexOf(':');
        return ( idx >= 0 ) ? name.substring(0, idx) : "";
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
