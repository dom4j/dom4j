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

/** <p><code>DocumentDecl</code> represents the collection of element 
  * declarations taken from a DTD.</p>
  *
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DocumentDecl {

    /** The element declarations */
    private ArrayList elementList = new ArrayList();
    /** The name map of the element declarations */
    private HashMap elementMap = new HashMap();
    
    public DocumentDecl() {
    }
    
    /** Adds the given {@link ElementDecl} object.
      * 
      * @param elementDecl the declaration to be added
      */
    public void add(ElementDecl elementDecl) {
        elementList.add(elementDecl);
        elementMap.put(elementDecl.getName(), elementDecl);
    }
    
    /** Removes the given {@link ElementDecl} object.
      * 
      * @param elementDecl the declaration to be removed
      */
    public void remove(ElementDecl elementDecl) {
        elementList.remove(elementDecl);
        elementMap.remove(elementDecl.getName());
    }
    
    /** Removes all {@link ElementDecl} objects 
      */
    public void clear() {
        elementList.clear();
        elementMap.clear();
    }
    
    /** @return an iterator across all {@link ElementDecl} instances
      */
    public Iterator iterator() {
        return elementList.iterator();
    }
    
    /** @return the {@link ElementDecl} instance for the given name or null
      */
    public ElementDecl get(String name) {
        return (ElementDecl) elementMap.get(name);
    }
    
    /** Writes the state of the model to the given writer
      * 
      * @param out is the writer to output to
      */
    public void write( PrintWriter out ) {
        for ( Iterator iter = iterator(); iter.hasNext(); ) {
            ElementDecl elementDecl = (ElementDecl) iter.next();
            elementDecl.write(out);
            out.println();
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
