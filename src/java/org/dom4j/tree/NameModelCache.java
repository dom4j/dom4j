package org.dom4j.tree;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Namespace;

/** <p><code>NameModelCache</code> caches instances of <code>NameModel</code> 
  * for reuse both across documents and within documents.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class NameModelCache {

    /** Cache of {@link NameModel} instances with no namespace */ 
    protected static Map noNamespaceCache = new HashMap();
    
    /** Cache of {@link Map} instances indexed by namespace which contain 
      * caches of {@link NameModel} for each name
      */ 
    protected static Map namespaceCache = new HashMap();


    /** @return the name model for the given name and no namepsace 
      */
    public NameModel get(String name) {
        NameModel answer = (NameModel) noNamespaceCache.get(name);
        if (answer == null) {
            answer = new NameModel(name);
            noNamespaceCache.put(name, answer);
        }
        return answer;
    }
    
    /** @return the name model for the given name and namepsace 
      */
    public NameModel get(String name, Namespace namespace) {
        Map cache = getNamespaceCache(namespace);
        NameModel answer = (NameModel) cache.get(name);
        if (answer == null) {
            answer = new NameModel(name, namespace);
            cache.put(name, answer);
        }
        return answer;
    }
    

    /** @return the cache for the given namespace. If one does not
      * currently exist it is created.
      */
    protected Map getNamespaceCache(Namespace namespace) {
        if (namespace == Namespace.NO_NAMESPACE) {
            return noNamespaceCache;
        }
        Map answer = (Map) namespaceCache.get(namespace);
        if (answer == null) {
            answer = createMap();
            namespaceCache.put(namespace, answer);
        }
        return answer;
    }
    
    /** A factory method
      * @return a newly created {@link Map} instance.
      */
    protected Map createMap() {
        return new HashMap();
    }
}
