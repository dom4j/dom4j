package org.dom4j;

import org.dom4j.tree.DefaultNamespace;

/** <p><code>Namespace</code> defines an XML namespace.</p>
  * 
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public interface Namespace extends Node {

    /** <p>Returns the prefix for this namespace</p>
      *
      * @return the prefix which is mapped to this namespace
      */
    public String getPrefix();
    
    /** <p>Returns the URI for this namespace</p>
      *
      * @return the URI for this namespace
      */
    public String getURI();    
    
    /** XML Namespace */
    public static final Namespace XML_NAMESPACE 
        = new DefaultNamespace("xml", "http://www.w3.org/XML/1998/namespace");
    
    /** No Namespace present */
    public static final Namespace NO_NAMESPACE 
        = new DefaultNamespace("", "");
}
