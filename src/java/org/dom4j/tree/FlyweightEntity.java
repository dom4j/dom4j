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

/** <p><code>FlyweightEntity</code> is a Flyweight pattern implementation
  * of a singly linked, read-only XML entity.</p>
  *
  * <p>This node could be shared across documents and elements though 
  * it does not support the parent relationship.</p>
  *
  * <p>Often this node needs to be created and then the text content added
  * later (for example in SAX) so this implementation allows a call to 
  * {@link #setText} providing the entity has no text already.
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class FlyweightEntity extends AbstractEntity {

    /** The name of the <code>Entity</code> */
    protected String name;

    /** The text of the <code>Entity</code> */
    protected String text;

    /** A default constructor for implementors to use.
      */
    protected FlyweightEntity() {
    }

    /** Creates the <code>Entity</code> with the specified name
      *
      * @param name is the name of the entity
      */
    public FlyweightEntity(String name) {
        this.name = name;
    }

    /** Creates the <code>Entity</code> with the specified name
      * and text.
      *
      * @param name is the name of the entity
      * @param text is the text of the entity
      */
    public FlyweightEntity(String name,String text) {
        this.name = name;
        this.text = text;
    }

    /** @return the name of the entity
      */
    public String getName() {
        return name;
    }

    /** @return the text of the entity
      */
    public String getText() {
        return text;
    }
    
    /** sets the value of the entity if it is not defined yet
      * otherwise an <code>UnsupportedOperationException</code> is thrown
      * as this class is read only.
      */
    public void setText(String text) {
        if (this.text != null) {
            this.text = text;
        }
        else {
            throw new UnsupportedOperationException( 
                "This Entity is read-only. It cannot be modified" 
            );
        }
    }
    
    protected Node createXPathResult(Element parent) {
        return new DefaultEntity( parent, getName(), getText() );
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
