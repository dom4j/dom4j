/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.tree;

import java.util.Collections;
import java.util.Map;

import org.dom4j.Element;
import org.dom4j.Node;

/** <p><code>FlyweightProcessingInstruction</code> is a Flyweight pattern implementation
  * of a singly linked, read-only XML Processing Instruction.</p>
  *
  * <p>This node could be shared across documents and elements though 
  * it does not support the parent relationship.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class FlyweightProcessingInstruction extends AbstractProcessingInstruction {

    /** The target of the PI */
    protected String target;

    /** The values for the PI as a String */
    protected String text;

    /** The values for the PI in name/value pairs */
    protected Map values;

    /** A default constructor for implementors to use.
      */
    public FlyweightProcessingInstruction() { 
    }

    /** <p>This will create a new PI with the given target and values</p>
      *
      * @param target is the name of the PI
      * @param values is the <code>Map</code> of the values for the PI
      */
    public FlyweightProcessingInstruction(String target,Map values) {
        this.target = target;
        this.values = values;
        this.text = toString(values);
    }

    /** <p>This will create a new PI with the given target and values</p>
      *
      * @param target is the name of the PI
      * @param text is the values for the PI as text
      */
    public FlyweightProcessingInstruction(String target,String text) {
        this.target = target;
        this.text = text;
        this.values = parseValues(text);
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        throw new UnsupportedOperationException( "This PI is read-only and cannot be modified" );
    }

    public String getText() {
        return text;
    }
    
    public String getValue(String name) {
        String answer = (String) values.get(name);
        if (answer == null) {
            return "";
        }
        return answer;
    }
    
    public Map getValues() {
        return Collections.unmodifiableMap( values );
    }
    
    protected Node createXPathResult(Element parent) {
        return new DefaultProcessingInstruction( parent, getTarget(), getText() );
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
