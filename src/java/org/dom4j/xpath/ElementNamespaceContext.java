
package org.dom4j.xpath;

import org.dom4j.Element;
import org.dom4j.Namespace;

import java.util.Map;
import java.util.HashMap;
import java.util.Stack;
import java.util.List;
import java.util.Iterator;

/** <p>A {@link org.dom4j.xpath.NamespaceContext} which gets it's mappings
 *  from an Element in a DOM4J tree.</p>
 *
 *  <p><b>It currently DOES NOT WORK</b></p>
 * 
 *  @author bob mcwhirter (bob @ werken.com)
 */
public class ElementNamespaceContext implements NamespaceContext
{
    private Element _element          = null;
    private Map     _namespaceMapping = null;

    /** Construct the NamespaceContext from a DOM4J Element
     *
     *  @param element The DOM4J element to use for prefix->nsURI mapping
     */
    public ElementNamespaceContext(Element element)
    {
        _element = element;
    }

    /** Translate a namespace prefix into a URI
     *
     *  Translate the prefix used in a component of an XPath
     *  into its expanded namespace URI.</p>
     *
     *  @param prefix The namespace prefix
     *
     *  @return The URI matching the prefix
     *
     *  @see org.dom4j.xpath.ContextSupport#setNamespaceContext
     */
    public String translateNamespacePrefix(String prefix)
    {
        // Initialize the prefix->URI mapping upon the first
        // call to this method.  Traverse from the <<root>>
        // to the current Element, accumulating namespace
        // declarations.

        if ( prefix == null || "".equals( prefix ) ) 
        {
            return "";
        }

        if ( _namespaceMapping == null )
        {
            _namespaceMapping = new HashMap();

            Stack lineage = new Stack();

            lineage.push(_element);

            Element elem = _element.getParent();

            while (elem != null)
            {
                lineage.push(elem);
                elem = elem.getParent();
            }

            List      nsList = null;
            Iterator  nsIter = null;
            Namespace eachNS = null;

            while ( ! lineage.isEmpty() )
            {
                elem = (Element) lineage.pop();

                nsList = elem.getAdditionalNamespaces();

                if ( ! nsList.isEmpty() )
                {
                    nsIter = nsList.iterator();

                    while (nsIter.hasNext())
                    {
                        eachNS = (Namespace) nsIter.next();

                        _namespaceMapping.put( eachNS.getPrefix(),
                                               eachNS.getURI() );
                    }
                }
            }
        }

        return (String) _namespaceMapping.get( prefix );
    }

    public Namespace getNamespaceByPrefix(String prefix)
    {
        return _element.getNamespaceForPrefix( prefix );
    }
}
