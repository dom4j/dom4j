package org.dom4j;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** <p><code>Element</code> interface defines an XML element.
  * An element can have declared namespaces, attributes, child nodes and 
  * textual content.</p>
  *
  * <p>Some of this interface is optional. 
  * Some implementations may be read-only and not support being modified.
  * Some implementations may not support the parent relationship and methods
  * such as {@link #getParent} or {@link #getDocument}.</p>
  *
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public interface Element extends Branch {

 
    /** @return true if this element is the root element of a document
      * and this element supports upward navigation else false.
      */
    public boolean isRootElement();

    /** <p>Returns the <code>Namespace</code> of this element if one exists 
      * otherwise <code>Namespace.NO_NAMESPACE</code> is returned.</p>
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
    
    
 
    /** <p>Returns the <code>Namespace</code> which is mapped to the given
      * prefix or null if it could not be found.</p>
      *
      * @return the <code>Namespace</code> associated with the given prefix
      */
    public Namespace getNamespaceForPrefix(String prefix);

    /** <p>Returns the <code>Namespace</code> which is mapped to the given
      * URI or null if it could not be found.</p>
      *
      * @return the <code>Namespace</code> associated with the given URI
      */
    public Namespace getNamespaceForURI(String uri);
    
   /** <p>Returns any additional namespaces declarations for this element 
      * other than namespace returned via the {@link #getNamespace()} method. 
      * If no additional namespace declarations are present for this
      * element then {@link Collections.EMPTY_LIST} will be returned.
      *
      * @return a list of any additional namespace declarations.
      */
    public List getAdditionalNamespaces();
    
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
    
    
    // Text API
    
    public String getTextTrim();

    // always returns nodes which support
    // the parent relationship including Text, Namespace etc.
    public Node getXPathNode(int index);
    public int getXPathNodeCount();
    

    
    // Attributes API
    
    
    public List getAttributes();
    public void setAttributes(List attributes);


    public Attribute getAttribute(String name);
    public Attribute getAttribute(String name, Namespace ns);

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
      * @param name is the name of the attribute whose value is to be added 
      * or updated
      * @param value is the attribute's value
      */
    public void setAttributeValue(String name, String value);
    
    /** <p>Sets the attribute value of the given name.</p>
      *
      * @param name is the name of the attribute whose value is to be added 
      * or updated
      * @param value is the attribute's value
      * @param namespace is the <code>Namespace</code> of the attribute
      */
    public void setAttributeValue(String name, String value, Namespace namespace);

    public boolean removeAttribute(String name);
    public boolean removeAttribute(String name, Namespace namespace);
    

    
    
    /** <p>Returns true if this <code>Element</code> has mixed content.
      * Mixed content means that an element contains both textual data and
      * child elements.
      *
      * @return true if this element contains mixed content.
      */
    public boolean hasMixedContent();    
    
    // return the child elements
    public Element getElementByID(String elementID);
    public Element getElement(String name);
    public Element getElement(String name, Namespace namespace);
    
    public List getElements();
    public List getElements(String name);
    public List getElements(String name, Namespace namespace);
    
    // helper methods
    public String getElementText(String name);
    public String getElementText(String name, Namespace namespace);
    public String getElementTextTrim(String name);
    public String getElementTextTrim(String name, Namespace namespace);
    

    public CDATA addCDATA(String cdata);
    public Text addText(String text);    
    public Entity addEntity(String name);
    public Entity addEntity(String name, String text);
    public Namespace addAdditionalNamespace(String prefix, String uri);

    // typesafe versions using node classes
    public void add(Attribute attribute);
    public void add(CDATA cdata);
    public void add(Entity entity);
    public void add(Text text);
    public void add(Namespace namespace);
        
    public boolean remove(Attribute attribute);
    public boolean remove(CDATA cdata);
    public boolean remove(Entity entity);
    public boolean remove(Namespace namespace);
    
    
    
    // add to me content from another element
    // analagous to the addAll(collection) methods in Java 2 collections
    public void addAttributes(Element element);
    public void addContent(Element element);
    public void addAddtionalNamespaces(Element element);

    // creates a copy
    public Element createCopy();
    public Element createCopy(String name);
    public Element createCopy(String name, Namespace namespace);
}
