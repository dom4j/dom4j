/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.xpath.util;

import org.dom4j.Document;
import org.dom4j.Element;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collections;

import java.io.Serializable;

public class DocumentOrderComparator implements Serializable {
    
    private static final Map EMPTY_MAP = new HashMap(0);
    
    private Map _orderings = null;
    
    public DocumentOrderComparator(Document document) {
        
        Element rootElement = document.getRootElement();
        
        if (rootElement != null) {            
            List orderedElements = Partition.documentOrderDescendants( rootElement );
            
            _orderings = new HashMap(orderedElements.size() + 1);
            
            _orderings.put(rootElement, new Integer(0));
            
            Iterator elemIter = orderedElements.iterator();
            int counter = 1;            
            while (elemIter.hasNext()) {
                _orderings.put( elemIter.next(),
                new Integer(counter) );
                ++counter;
            }
        }
        else {
            _orderings = EMPTY_MAP;
        }
        
    }
    
    public int compare(Object lhsIn, Object rhsIn) throws ClassCastException {
        Element lhs = (Element) lhsIn;
        Element rhs = (Element) rhsIn;
        
        if (lhs.equals(rhs)) {
            return 0;
        }
        
        int lhsIndex = ((Integer)_orderings.get(lhs)).intValue();
        int rhsIndex = ((Integer)_orderings.get(rhs)).intValue();
        
        if (lhsIndex < rhsIndex) {
            return -1;
        }
        else if (lhsIndex > rhsIndex) {
            return 1;
        }        
        return 0;
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
