package org.dom4j.tree;

import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Namespace;

/** <p><code>DefaultAttribute</code> is the DOM4J default implementation
  * of a singly linked, read-only XML attribute.</p>
  *
  * <p>It implements a singly linked attribute.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DefaultAttribute extends AbstractAttribute {

    /** The <code>{@link Namespace}</code> of the <code>Attribute</code> */
    protected transient Namespace namespace;

    /** The local name of the <code>Attribute</code> */
    protected String name;

    /** The value of the <code>Attribute</code> */
    protected String value;

    /** A default constructor for implementors to use.
      */
    protected DefaultAttribute() {
    }

    /** Creates the <code>Attribute</code> with the specified local name
      * and value.
      *
      * @param name is the name of the attribute
      * @param value is the value of the attribute
      */
    public DefaultAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    /** Creates the <code>Attribute</code> with the specified local name,
      * value and <code>Namespace</code>.
      *
      * @param name is the name of the attribute
      * @param value is the value of the attribute
      * @param namespace is the namespace of the attribute
      */
    public DefaultAttribute(String name, String value, Namespace namespace) {
        this.name = name;
        this.value = value;
        this.namespace = namespace;
    }
    
    public String getName() {
        return name;
    }

    public Namespace getNamespace() {
        return namespace;
    }

    public String getValue() {
        return value;
    }
    
    protected Node createXPathNode(Element parent) {
        return new XPathAttribute(parent, name, value, namespace);
    }
}
