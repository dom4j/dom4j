package org.dom4j.tree;

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


    /** @return the name model for the given name and namepsace 
      */
    public Namespace get(String prefix, String uri) {
        Map cache = getURICache(uri);
        Namespace answer = (Namespace) cache.get(prefix);
        if (answer == null) {
            answer = createNamespace(prefix, uri);
            cache.put(prefix, answer);
        }
        return answer;
    }
    

    /** @return the cache for the given namespace URI. If one does not
      * currently exist it is created.
      */
    protected Map getURICache(String uri) {
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
        return new DefaultNamespace(prefix, uri);
    }
    /** A factory method to create prefix caches
      * @return a newly created {@link Map} instance.
      */
    protected Map createPrefixMap() {
        return new HashMap();
    }
    
    /** A factory method to create URI caches
      * @return a newly created {@link Map} instance.
      */
    protected Map createURIMap() {
        return new HashMap();
    }
}
