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

    /** The <code>NameModel</code> for this element */
    private NameModel nameModel;
    
    /** The value of the <code>Attribute</code> */
    protected String value;

    
    public DefaultAttribute() { 
        this.nameModel = NameModel.EMPTY_NAME;
    }

    public DefaultAttribute(NameModel nameModel) {
        this.nameModel = nameModel;
    }

    public DefaultAttribute(NameModel nameModel, String value) { 
        this.nameModel = nameModel;
        this.value = value;
    }
    
    /** Creates the <code>Attribute</code> with the specified local name
      * and value.
      *
      * @param name is the name of the attribute
      * @param value is the value of the attribute
      */
    public DefaultAttribute(String name, String value) {
        this.nameModel = NameModel.get(name);
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
        this.nameModel = NameModel.get(name, namespace);
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    protected NameModel getNameModel() {
        return nameModel;
    }
    
    /** Allow derived classes to change the name model */
    protected void setNameModel(NameModel nameModel) {
        this.nameModel = nameModel;
    }
    
}
