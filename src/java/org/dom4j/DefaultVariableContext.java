/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j;

import java.util.Map;
import java.util.HashMap;

import org.dom4j.VariableContext;

/** <p>A default {@link VariableContext}
 *  implementation based upon a java.util.Map for simple
 *  name-value mappings.</p>
 *
 *  @author bob mcwhirter (bob @ werken.com)
 */

public class DefaultVariableContext implements VariableContext, org.jaxen.VariableContext {

    /** The map used to resolve variable values */
    private Map map;

    
    public DefaultVariableContext() {
        this.map = new HashMap();
    }
    
    public DefaultVariableContext(Map map) {
        this.map = map;
    }
    
    /** Resolve a variable binding
    *
    *  <p>Retrieve the currently bound value of the named
    *  variable, or null if no such binding exists. 
    *
    *  @param name The name of the variable sought.
    *
    *  @return The currently bound value of the variable, or null.
    *
    *  @see org.dom4j.xpath.ContextSupport#getVariableValue
    *  @see org.dom4j.xpath.ContextSupport#setVariableContext
    */
    public Object getVariableValue(String name) {
        return map.get(name);
    }

    public Object getVariableValue(String prefix, String name) {
        return map.get(name);
    }

    /** Set a variable finding
    *
    *  <p>Set the value of a named variable.
    *
    *  @param name The name of the variable to bind to the value
    *  @param value The value to bind to the variable name.
    */
    public void setVariableValue(String name, Object value) {
        map.put(name, value);
    }
    
    /** @return the {@link Map} instance used to resolve variable values
      */
    public Map getMap() {
        return map;
    }
    
    /** Sets the {@link Map} instance used to resolve variable values 
      */
    public void setMap(Map map) {
        this.map = map;
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
