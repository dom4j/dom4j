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
import com.sun.tranquilo.datatype.DataTypeFactory;

import java.util.HashMap;
import java.util.Map;


import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.QName;

/** <p><code>SchemaElementFactory</code> is a factory for a specific Element 
  * in an XML Schema.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class SchemaElementFactory extends DocumentFactory {
    
    private QName elementQName;
    
    
    public SchemaElementFactory(QName elementQName) {
        this.elementQName = elementQName;
        elementQName.setDocumentFactory(this);
    }
    
    /** Cache of <code>DataType</code> instances per 
      * Attribute <code>QName</code> */
    private Map attributeDataTypes = new HashMap();

    
    /** Registers the given <code>DataType</code> for the given 
      * &lt;attribute&gt; schema element
      */
    public DataType getAttributeDataType( QName attributeQName ) {
        return (DataType) attributeDataTypes.get( attributeQName );
    }
    
    /** Registers the given <code>DataType</code> for the given 
      * &lt;attribute&gt; QNames
      */
    public void setAttributeDataType( QName attributeQName, DataType dataType ) {
        System.out.println( "==== Creating DataType for element: " + elementQName.getQualifiedName() + " and attribute: " + attributeQName.getQualifiedName() );
        System.out.println( "### DataType: " + dataType );
        
        attributeDataTypes.put( attributeQName, dataType );
    }
    
    // DocumentFactory methods
    //-------------------------------------------------------------------------
    public Attribute createAttribute(QName qname, String value) {
        System.out.println( "### Creating Attribute for element: " + elementQName.getQualifiedName() + " and attribute: " + qname.getQualifiedName() );
        
        DataType dataType = getAttributeDataType(qname);
        if ( dataType == null ) {
            System.out.println( "no DataType!" );
            return super.createAttribute( qname, value );
        }
        else {
            System.out.println( "### FOUND " + dataType );
            return new SchemaAttribute( qname, dataType, value );
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
