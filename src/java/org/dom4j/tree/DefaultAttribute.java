/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.tree;

import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Namespace;

/** <p><code>DefaultAttribute</code> is the DOM4J default implementation
  * of a singly linked, read-only XML attribute.</p>
  *
  * <p>It implements a singly linked attribute.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DefaultAttribute extends AbstractAttribute {

    /** The <code>NameModel</code> for this element */
    private NameModel nameModel;
    
    /** The value of the <code>Attribute</code> */
    protected String value;

    
    public DefaultAttribute() { 
        this.nameModel = NameModel.EMPTY_NAME;
    }

    public DefaultAttribute(NameModel nameModel) {
        this.nameModel = nameModel;
    }

    public DefaultAttribute(NameModel nameModel, String value) { 
        this.nameModel = nameModel;
        this.value = value;
    }
    
    /** Creates the <code>Attribute</code> with the specified local name
      * and value.
      *
      * @param name is the name of the attribute
      * @param value is the value of the attribute
      */
    public DefaultAttribute(String name, String value) {
        this.nameModel = NameModel.get(name);
        this.value = value;
    }

    /** Creates the <code>Attribute</code> with the specified local name,
      * value and <code>Namespace</code>.
      *
      * @param name is the name of the attribute
      * @param value is the value of the attribute
      * @param namespace is the namespace of the attribute
      */
    public DefaultAttribute(String name, String value, Namespace namespace) {
        this.nameModel = NameModel.get(name, namespace);
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    protected NameModel getNameModel() {
        return nameModel;
    }
    
    /** Allow derived classes to change the name model */
    protected void setNameModel(NameModel nameModel) {
        this.nameModel = nameModel;
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
