package org.dom4j;

/**<p><code>Attribute</code> defines an XML attribute.
  * An attribute may have a name, an optional namespace and a value.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public interface Attribute extends Node {
    
    /** <p>Returns the <code>Namespace</code> of this element if one exists 
      * otherwise null is returned returned.</p>
      *
      * @return the <code>Namespace</code> associated with this node
      */
    public Namespace getNamespace();

    /** <p>Sets the <code>Namespace</code> of this element or if this element
      * is read only then an <code>UnsupportedOperationException</code> 
      * is thrown.</p>
      *
      * @param namespace is the <code>Namespace</code> to associate with this 
      * element
      */
    public void setNamespace(Namespace namespace);
    
    /** <p>Returns the namespace prefix of this element if one exists 
      * otherwise an empty <code>String</code> is returned.</p>
      *
      * @return the prefix of the <code>Namespace</code> of this element 
      * or an empty <code>String</code>
      */
    public String getNamespacePrefix();

    /** <p>Returns the URI mapped to the namespace of this element 
      * if one exists otherwise an empty <code>String</code> is returned.</p>
      *
      * @return the URI for the <code>Namespace</code> of this element 
      * or an empty <code>String</code>
      */
    public String getNamespaceURI();

    /** <p>Returns the fully qualified name of this element. 
      * This will be the same as the value returned from {@link #getName}
      * if this element has no namespace attached to this element or an
      * expression of the form
      * <pre>
      * getNamespacePrefix() + ":" + getName()
      * </pre>
      * will be returned.
      *
      * @return the fully qualified name of the element.
      */
    public String getQualifiedName();
    
    /** <p>Returns the value of the attribute. This method 
      * returns the same value as the {@link #getText} method.
      *
      * @return the value of the attribute.
      */
    public String getValue();
    
   /** <p>Sets the value of this attribute or this method will 
     * throw an <code>UnsupportedOperationException</code> if it is 
     * read-only.</p>
     *
     * @param value is the new value of this attribute
     */
    public void setValue(String value);
    
}
