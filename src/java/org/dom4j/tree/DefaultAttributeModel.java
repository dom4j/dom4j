package org.dom4j.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.ContentFactory;
import org.dom4j.Namespace;

/** <p><code>DefaultAttributeModel</code> is a default implementation of 
  * <code>AttributeModel</code>.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DefaultAttributeModel extends AbstractAttributeModel {

    /** Lazily constructed list to store the <code>Attribute</code> instances */
    protected List list;
    
    public DefaultAttributeModel() {
    }
    

    public List getAttributes() {
        if ( list == null ) {
            list = createList();
        }
        return list;
    }
    
    public void setAttributes(List list) {
        this.list = list;
    }
    
    public Attribute getAttribute(String name, Namespace namespace) {
        if ( list != null ) {
            String uri = namespace.getURI();
            int size = list.size();
            for ( int i = 0; i < size; i++ ) {
                Attribute attribute = (Attribute) list.get(i);
                if ( name.equals( attribute.getName() )
                    && uri.equals( attribute.getNamespaceURI() ) ) {
                    return attribute;
                }
            }
        }
        return null;
    }

    public boolean removeAttribute(String name, Namespace namespace) {
        if ( list != null ) {
            String uri = namespace.getURI();
            for ( Iterator iter = list.iterator(); iter.hasNext(); ) {
                Attribute attribute = (Attribute) iter.next();
                if ( name.equals( attribute.getName() )
                    && uri.equals( attribute.getNamespaceURI() ) ) {
                    iter.remove();
                    return true;
                }
            }
        }
        return false;
    }
    
    public void add(Attribute attribute) {
        getAttributes().add(attribute);
    }
    
    public boolean remove(Attribute attribute) {
        if ( list == null ) {
            return false;
        }
        boolean answer = list.remove(attribute);
        if ( answer ) {
            attribute.setParent(null);
        }
        return answer;
    }
    

    /** A Factory Method pattern which lazily creates 
      * a List implementation used to store attributes
      */
    protected List createList() {
        return new ArrayList();
    }
}
