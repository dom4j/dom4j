package org.dom4j.tree;

import java.util.Iterator;

import org.dom4j.Element;
import org.dom4j.Namespace;


/** <p><code>ElementNameIterator</code> is a filtering {@link Iterator} which 
  * filters out objects which do not implement the {@link Element} 
  * interface and are not of the correct element name and namespace.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class ElementNameIterator extends FilterIterator {
    
    private String name;
    private String namespacePrefix;
    private String namespaceURI;
    
    public ElementNameIterator(Iterator proxy, String name, String namespacePrefix, String namespaceURI) {
        super(proxy);
        this.name = name;
        this.namespacePrefix = namespacePrefix;
        this.namespaceURI = namespaceURI;
    }

    public ElementNameIterator(Iterator proxy, String name, Namespace namespace) {
        this(proxy, name, namespace.getPrefix(), namespace.getURI());
    }


    /** @return true if the given element implements the {@link Element} 
      * interface
      */
    protected boolean matches(Object object) {
        if (object instanceof Element) {
            Element element = (Element) object;
            if ( name.equals( element.getName() ) ) {
                if ( namespaceURI.equals( element.getNamespaceURI() ) 
                    && namespacePrefix.equals( element.getNamespacePrefix() ) ) {
                    return true;
                }
            }
        }
        return false;
    }
}
