package org.dom4j.tree;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.ContentFactory;
import org.dom4j.Namespace;

/** <p><code>AttributeModel</code> represents an XML attributes model for an 
  * XML element.
  * This interface is used to decompose an element implementations into smaller
  * resusable units.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public interface AttributeModel {

    public List getAttributes();
    public Iterator attributeIterator();
    public void setAttributes(List attributes);


    public Attribute getAttribute(String name);
    public Attribute getAttribute(String name, Namespace namespace);

    /** <p>This returns the attribute value for the attribute with the 
      * given name and within no namespace or null if there is no such 
      * attribute or the empty string if the attribute value is empty.</p>
      *
      * @param name is the name of the attribute value to be returnd
      * @return the value of the attribute, null if the attribute does 
      * not exist or the empty string
      */
    public String getAttributeValue(String name);

    /** <p>This returns the attribute value for the attribute with the 
      * given name and within no namespace or null if there is no such 
      * attribute or the empty string if the attribute value is empty.</p>
      *
      * @param name is the name of the attribute value to be returnd
      * @param namespace is the <code>Namespace</code> of the attribute
      * @return the value of the attribute, null if the attribute does 
      * not exist or the empty string
      */
    public String getAttributeValue(String name, Namespace namespace);

    
    /** <p>Sets the attribute value of the given name.</p>
      *
      * @param factory is the content factory used to create new attribute objects if necessary
      * @param name is the name of the attribute whose value is to be added 
      * or updated
      * @param value is the attribute's value
      */
    public void setAttributeValue(ContentFactory factory, String name, String value);
    
    /** <p>Sets the attribute value of the given name.</p>
      *
      * @param factory is the content factory used to create new attribute objects if necessary
      * @param name is the name of the attribute whose value is to be added 
      * or updated
      * @param value is the attribute's value
      * @param namespace is the <code>Namespace</code> of the attribute
      */
    public void setAttributeValue(ContentFactory factory, String name, String value, Namespace namespace);

    public boolean removeAttribute(String name);
    public boolean removeAttribute(String name, Namespace namespace);
    
    public void add(Attribute attribute);
    public boolean remove(Attribute attribute);

}
