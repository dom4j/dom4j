/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.ContentFactory;
import org.dom4j.Namespace;

/** <p><code>DefaultAttributeModel</code> is a default implementation of 
  * <code>AttributeModel</code>.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DefaultAttributeModel extends AbstractAttributeModel {

    /** Lazily constructed list to store the <code>Attribute</code> instances */
    protected List list;
    
    public DefaultAttributeModel() {
    }
    

    public List getAttributes() {
        if ( list == null ) {
            list = createList();
        }
        return list;
    }
    
    public void setAttributes(List list) {
        this.list = list;
    }
    
    public Attribute getAttribute(String name, Namespace namespace) {
        if ( list != null ) {
            String uri = namespace.getURI();
            int size = list.size();
            for ( int i = 0; i < size; i++ ) {
                Attribute attribute = (Attribute) list.get(i);
                if ( name.equals( attribute.getName() )
                    && uri.equals( attribute.getNamespaceURI() ) ) {
                    return attribute;
                }
            }
        }
        return null;
    }

    public boolean removeAttribute(String name, Namespace namespace) {
        if ( list != null ) {
            String uri = namespace.getURI();
            for ( Iterator iter = list.iterator(); iter.hasNext(); ) {
                Attribute attribute = (Attribute) iter.next();
                if ( name.equals( attribute.getName() )
                    && uri.equals( attribute.getNamespaceURI() ) ) {
                    iter.remove();
                    return true;
                }
            }
        }
        return false;
    }
    
    public void add(Attribute attribute) {
        getAttributes().add(attribute);
    }
    
    public boolean remove(Attribute attribute) {
        if ( list == null ) {
            return false;
        }
        boolean answer = list.remove(attribute);
        if ( answer ) {
            attribute.setParent(null);
        }
        return answer;
    }
    

    /** A Factory Method pattern which lazily creates 
      * a List implementation used to store attributes
      */
    protected List createList() {
        return new ArrayList();
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
