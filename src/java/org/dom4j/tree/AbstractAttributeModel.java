package org.dom4j.tree;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.ContentFactory;
import org.dom4j.Namespace;

/** <p><code>AbstractAttributeModel</code> is an abstract base class for 
  * tree implementors to use for implementation inheritence.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class AbstractAttributeModel implements AttributeModel {
    
    public Iterator attributeIterator() {
        return getAttributes().iterator();
    }
    
    public Attribute getAttribute(String name) {
        return getAttribute(name, Namespace.NO_NAMESPACE);
    }
    
    public boolean removeAttribute(String name) {
        return removeAttribute(name, Namespace.NO_NAMESPACE);
    }
    

    public String getAttributeValue(String name) {
        return getAttributeValue(name, Namespace.NO_NAMESPACE);
    }
    
    public String getAttributeValue(String name, Namespace ns) {
        Attribute attrib = getAttribute(name, ns);
        if (attrib == null) {
            return null;
        } 
        else {
            return attrib.getValue();
        }
    }

    public void setAttributeValue(ContentFactory factory, String name, String value) {
        Attribute attribute = getAttribute(name);
        if (attribute == null ) {
            add(factory.createAttribute(name, value));
        }
        else if (attribute.isReadOnly()) {
            remove(attribute);
            add(factory.createAttribute(name, value));
        }
        else {
            attribute.setValue(value);
        }
    }

    public void setAttributeValue(ContentFactory factory, String name, String value, Namespace namespace) {
        Attribute attribute = getAttribute(name, namespace);
        if (attribute == null ) {
            add(factory.createAttribute(name, value, namespace));
        }
        else if (attribute.isReadOnly()) {
            remove(attribute);
            add(factory.createAttribute(name, value, namespace));
        }
        else {
            attribute.setValue(value);
        }
    }
}
