package org.dom4j.tree;

import org.dom4j.Element;
import org.dom4j.Namespace;

/** <p><code>XPathAttribute</code> implements a doubly linked node which 
  * supports the parent relationship and is mutable.
  * It is useful when evalutating XPath expressions.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class XPathAttribute extends DefaultAttribute {

    /** The parent of this node */
    private Element parent;

    /** Creates the <code>Attribute</code> with the specified local name
      * and value.
      *
      * @param name is the name of the attribute
      * @param value is the value of the attribute
      */
    public XPathAttribute(String name, String value) {
        super(name, value);
    }
    
    /** Creates the <code>Attribute</code> with the specified local name,
      * value and <code>Namespace</code>.
      *
      * @param name is the name of the attribute
      * @param value is the value of the attribute
      * @param namespace is the namespace of the attribute
      */
    public XPathAttribute(String name, String value, Namespace namespace) {
        super(name, value, namespace);
    }
    
    /** Creates the <code>Attribute</code> with the specified local name,
      * value and <code>Namespace</code>.
      *
      * @param parent is the parent element
      * @param name is the name of the attribute
      * @param value is the value of the attribute
      * @param namespace is the namespace of the attribute
      */
    public XPathAttribute(Element parent, String name, String value, Namespace namespace) {
        super(name, value, namespace);
        this.parent = parent;
    }

    public void setName(String name) {
	this.name = name;
    }
    
    public void setValue(String value) {
	this.value = value;
    }
    
    public void setNamespace(Namespace namespace) {
        this.namespace = namespace;
    }
    
    public Element getParent() {
        return parent;
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }
    
    public boolean supportsParent() {
        return true;
    }

}
