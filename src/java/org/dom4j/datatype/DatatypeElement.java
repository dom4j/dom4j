/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.datatype;

import com.sun.msv.datatype.DatabindableDatatype;
import com.sun.msv.datatype.SerializationContext;
import com.sun.msv.datatype.xsd.XSDatatype;

import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.QName;
import org.dom4j.tree.DefaultElement;
import org.relaxng.datatype.DatatypeException;
import org.relaxng.datatype.ValidationContext;

/** <p><code>DatatypeElement</code> represents an Element which supports the
  * <a href="http://www.w3.org/TR/xmlschema-2/">XML Schema Data Types</a>
  * specification.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DatatypeElement extends DefaultElement implements SerializationContext, ValidationContext {

    /** The <code>XSDatatype</code> of the <code>Attribute</code> */
    private XSDatatype datatype;
    
    /** The data (Object) value of the <code>Attribute</code> */
    private Object data;


    
    public DatatypeElement(QName qname,XSDatatype datatype) {
        super(qname);
        this.datatype = datatype;
    }

    public DatatypeElement(QName qname,int attributeCount,XSDatatype datatype) { 
        super(qname, attributeCount);
        this.datatype = datatype;
    }

    public String toString() {
        return getClass().getName() + hashCode() 
            + " [Element: <" + getQualifiedName() 
            + " attributes: " + attributeList() 
            + " data: " + getData() + " />]";
    }
    
    /** Returns the MSV XSDatatype for this node */
    public XSDatatype getXSDatatype() {
        return datatype;
    }
    
    // SerializationContext interface
    //-------------------------------------------------------------------------  
    public String getNamespacePrefix(String uri) {
        Namespace namespace = getNamespaceForURI(uri);
        return (namespace != null) ? namespace.getPrefix() : null;
    }
    
    // ValidationContext interface
    //-------------------------------------------------------------------------    
    public String getBaseUri() {
        // XXXX: could we use a Document for this?
        return null;
    }
    
    public boolean isNotation(String notationName) {
        // XXXX: no way to do this yet in dom4j so assume false
        return false;
    }
        
    public boolean isUnparsedEntity(String entityName) {
        // XXXX: no way to do this yet in dom4j so assume valid
        return true;
    }
    
    public String resolveNamespacePrefix(String prefix) {
        Namespace namespace = getNamespaceForPrefix( prefix );
        if ( namespace != null ) {
            return namespace.getURI();
        }
        return null;
    }
    
    
    // Element interface
    //-------------------------------------------------------------------------
    public Object getData() {
        if ( data == null ) {
            String text = getTextTrim();
            if ( text != null && text.length() > 0 ) {
                if ( datatype instanceof DatabindableDatatype ) {
                    DatabindableDatatype bindable = (DatabindableDatatype) datatype;
                    data = bindable.createJavaObject( text, this );
                }
                else {
                    data = datatype.createValue( text, this );
                }
            }
        }
        return data;
    }
    
    public void setData(Object data) {
        String s = datatype.convertToLexicalValue( data, this );
        validate(s);
        this.data = data;
        setText( s );
    }    

    public Element addText(String text) {
        validate(text);
        return super.addText(text);
    }
    
    public void setText(String text) {
        validate(text);
        super.setText(text);
    }
    // Implementation methods
    //-------------------------------------------------------------------------    
    /** Override to force lazy recreation of data object */
    protected void childAdded(Node node) {
        data = null;
        super.childAdded(node);
    }
    
    /** Override to force lazy recreation of data object */    
    protected void childRemoved(Node node) {
        data = null;
        super.childRemoved(node);
    }

    protected void validate(String text) throws IllegalArgumentException {
        try {
            datatype.checkValid(text, this);
        }
        catch (DatatypeException e) {
            throw new IllegalArgumentException( e.getMessage() );
        }
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
