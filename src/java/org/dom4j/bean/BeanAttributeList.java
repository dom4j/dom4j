/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.bean;

import java.beans.PropertyDescriptor;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;

/** <p><code>BeanAttributeList</code> implements a list of Attributes
  * which are the properties of a JavaBean.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class BeanAttributeList extends AbstractList {

    /** The BeanElement that this */
    private BeanElement parent;

    /** The BeanElement that this */
    private BeanMetaData beanMetaData;

    /** The attributes */
    private BeanAttribute[] attributes;
  
    
    public BeanAttributeList(BeanElement parent) { 
        this.parent = parent;
        
        Object data = parent.getData();
        Class beanClass = (data != null) ? data.getClass() : null;
        this.beanMetaData = BeanMetaData.get( beanClass );
        this.attributes = new BeanAttribute[ beanMetaData.getAttributeCount() ];
    }
    
    public int size() {
        return attributes.length;
    }
    
    public Object get(int index) {
        BeanAttribute attribute = attributes[index];
        if ( attribute == null ) {
            attribute = beanMetaData.createAttribute( parent, index );
            attributes[index] = attribute;
        }
        return attribute;
    }
    
    public boolean add(Object object) {
        throw new UnsupportedOperationException( "add(int, Object) is not supported" );
    }
    
    public void add(int index, Object object) {
        throw new UnsupportedOperationException( "add(int, Object) is not supported" );
    }
    
    public Object set(int index, Object object) {
        throw new UnsupportedOperationException( "set(int, Object) is not supported" );
    }
    
    public boolean remove(Object object) {
        return false;
    }

    public Object remove(int index) {
        BeanAttribute attribute = (BeanAttribute) get(index);
        Object oldValue = attribute.getValue();
        attribute.setValue(null);
        return oldValue;
    }

    public void clear() {
        for ( int i = 0, size = attributes.length; i < size; i++ ) {
            BeanAttribute attribute = attributes[i];
            if ( attribute != null ) {
                attribute.setValue( null );
            }
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
