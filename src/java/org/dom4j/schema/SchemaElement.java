/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.schema;

import com.sun.tranquilo.datatype.DataType;
import com.sun.tranquilo.datatype.ValidationContextProvider;

import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.tree.DefaultElement;

import org.xml.sax.Attributes;

/** <p><code>SchemaElement</code> represents an Element which supports the
  * <a href="http://www.w3.org/TR/xmlschema-2/">XML Schema Data Types</a>
  * specification.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class SchemaElement extends DefaultElement implements ValidationContextProvider {

    /** The <code>DataType</code> of the <code>Attribute</code> */
    private DataType dataType;
    
    /** The data (Object) value of the <code>Attribute</code> */
    private Object data;


    
    public SchemaElement(QName qname, DataType dataType) {
        super(qname);
        this.dataType = dataType;
    }

    public SchemaElement(QName qname, Attributes attributes, DataType dataType) { 
        super(qname, attributes);
        this.dataType = dataType;
    }

    
    // ValidationContextProvider interface
    //-------------------------------------------------------------------------    
    public boolean isUnparsedEntity(String entityName) {
        // XXXX: no way to do this yet in dom4j so assume valid
        return true;
    }
    
    public String resolveNamespacePrefix(String prefix) {
        Namespace namespace = getNamespaceForPrefix( prefix );
        if ( namespace != null ) {
            return namespace.getPrefix();
        }
        return null;
    }
    
    
    // Element interface
    //-------------------------------------------------------------------------
    public Object getData() {
        if ( data == null ) {
            data = dataType.convertToValueObject( getTextTrim(), this );
        }
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
        
        // XXXX: when the library supports this
        // dataType.convertToText( data, this );
        if ( data != null ) {
            setText( data.toString() );
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
