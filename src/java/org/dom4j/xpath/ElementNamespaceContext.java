/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */


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
