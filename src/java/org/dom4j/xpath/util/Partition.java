/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */


package org.dom4j.xpath.util;

import org.dom4j.Element;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;


public class Partition {

    public static List descendants(Element node) {
        List results = new ArrayList();

        List children = node.elements();

        results.addAll(children);
        
        Iterator childIter = children.iterator();
        
        while (childIter.hasNext()) {
            results.addAll( Partition.descendants( (Element) childIter.next() ) );
        }
        if (results.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return results;
    }

    public static List documentOrderDescendants(Element node) {
        List results = new ArrayList();

        List children = node.elements();

        if (children.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        Iterator childIter = children.iterator();

        Element each = null;

        while (childIter.hasNext()) {
            each = (Element) childIter.next();
            results.add(each);
            results.addAll( documentOrderDescendants( each ) );
        }

        if (results.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return results;
    }

    public static List followingSiblings(Element node) {
        Element parent = node.getParent();

        if (parent == null) {
            return Collections.EMPTY_LIST;
        }
        List siblings = parent.elements();
        int selfIndex = siblings.indexOf(node);

        if (selfIndex < 0) {
            return Collections.EMPTY_LIST;
        }

        int total = siblings.size();

        if (selfIndex == (total - 1)) {
            return Collections.EMPTY_LIST;
        }

        return new ArrayList( 
            siblings.subList( (selfIndex + 1), siblings.size() ) 
        );
    }

    public static List preceedingSiblings(Element node) {
        Element parent = node.getParent();

        if (parent == null) {
            return Collections.EMPTY_LIST;
        }

        List siblings = parent.elements();

        int selfIndex = siblings.indexOf(node);

        if ( (selfIndex < 1) || (siblings.size() == 1) ) {
            return Collections.EMPTY_LIST;
        }

        List results = new ArrayList();

        results = siblings.subList(0, selfIndex);

        return results;
    }

    public static List following(Element node) {
        List results = new ArrayList();

        List followingSiblings = Partition.followingSiblings(node);

        results.addAll(followingSiblings);

        Iterator sibIter = followingSiblings.iterator();
        Element each = null;

        while (sibIter.hasNext()) {
            each = (Element) sibIter.next();

            results.addAll( Partition.descendants( each ) );
        }

        Element parent = node.getParent();

        if (parent != null) {
            results.addAll( Partition.following( parent ) );
        }

        if (results.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        return results;
    }

    public static List preceeding(Element node) {
        List results = new ArrayList();

        List preceedingSiblings = Partition.preceedingSiblings(node);

        results.addAll(preceedingSiblings);

        Iterator sibIter = preceedingSiblings.iterator();
        Element each = null;

        while (sibIter.hasNext()) {
            each = (Element) sibIter.next();

            results.addAll( Partition.descendants( each ) );
        }

        Element parent = node.getParent();

        if (parent != null) {
            results.addAll( Partition.preceeding( parent ) );
        }

        if (results.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        
        return results;
    }
}




/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *        statements and notices.    Redistributions must also contain a
 *        copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *        above copyright notice, this list of conditions and the
 *        following disclaimer in the documentation and/or other
 *        materials provided with the distribution.
 *
 * 3. The name "DOM4J" must not be used to endorse or promote
 *        products derived from this Software without prior written
 *        permission of MetaStuff, Ltd.    For written permission,
 *        please contact dom4j-info@metastuff.com.
 *
 * 4. Products derived from this Software may not be called "DOM4J"
 *        nor may "DOM4J" appear in their names without prior written
 *        permission of MetaStuff, Ltd. DOM4J is a registered
 *        trademark of MetaStuff, Ltd.
 *
 * 5. Due credit should be given to the DOM4J Project
 *        (http://dom4j.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.    IN NO EVENT SHALL
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
