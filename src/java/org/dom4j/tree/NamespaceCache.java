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
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Namespace;

/** <p><code>NamespaceCache</code> caches instances of <code>DefaultNamespace</code>
  * for reuse both across documents and within documents.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class NamespaceCache {

    /** Cache of {@link Map} instances indexed by URI which contain
      * caches of {@link Namespace} for each prefix
      */
    protected static Map cache;

    /** Cache of {@link Namespace} instances indexed by URI
      * for default namespaces with no prefixes
      */
    protected static Map noPrefixCache;


    /** @return the name model for the given name and namepsace
      */
    public synchronized Namespace get(String prefix, String uri) {
        Map cache = getURICache(uri);
        Namespace answer = (Namespace) cache.get(prefix);
        if (answer == null) {
            answer = createNamespace(prefix, uri);
            cache.put(prefix, answer);
        }
        return answer;
    }


    /** @return the name model for the given name and namepsace
      */
    public synchronized Namespace get(String uri) {
        if ( noPrefixCache == null ) {
            noPrefixCache = createURIMap();
        }
        Namespace answer = (Namespace) noPrefixCache.get(uri);
        if (answer == null) {
            answer = createNamespace("", uri);
            noPrefixCache.put(uri, answer);
        }
        return answer;
    }


    /** @return the cache for the given namespace URI. If one does not
      * currently exist it is created.
      */
    protected synchronized Map getURICache(String uri) {
        if (cache == null) {
            cache = createURIMap();
        }
        Map answer = (Map) cache.get(uri);
        if (answer == null) {
            answer = createPrefixMap();
            cache.put(uri, answer);
        }
        return answer;
    }

    /** A factory method to create {@link Namespace} instance
      * @return a newly created {@link Namespace} instance.
      */
    protected Namespace createNamespace(String prefix, String uri) {
        return new Namespace(prefix, uri);
    }
    /** A factory method to create prefix caches
      * @return a newly created {@link Map} instance.
      */
    protected Map createPrefixMap() {
        return Collections.synchronizedMap(new HashMap());
    }

    /** A factory method to create URI caches
      * @return a newly created {@link Map} instance.
      */
    protected Map createURIMap() {
        return Collections.synchronizedMap(new HashMap());
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
