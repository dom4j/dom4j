package org.dom4j.tool.dtd;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** <p><code>ElementDecl</code> represents the element declaration model
  * declaration taken from a DTD.</p>
  *
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class ElementDecl {

    /** The name of the element. */
    private String name;
    /** The model of the element taken from the SAX {@link org.xml.sax.ext.DeclHandler} class. */
    private String model;
    /** The attribute declarations */
    private ArrayList attributeList = new ArrayList();
    /** The name map of the attribute declarations */
    private HashMap attributeMap = new HashMap();
    
    public ElementDecl() {
    }
    
    public ElementDecl(String name, String model) {
        this.name = name;
        this.model = model;
    }

    /** @return the name of the element
      */
    public String getName() {
        return name;
    }
    
    /** Sets the name of the element
      * @param name New name value 
      */
    public void setName(String name) {
        this.name = name;
    }
    
    /** @return the model of the element
      */
    public String getModel() {
        return model;
    }
    
    /** Sets the model of the element
      * @param model New model value 
      */
    public void setModel(String model) {
        this.model = model;
    }

    
    /** Adds the given {@link AttributeDecl} object.
      * 
      * @param attributeDecl the declaration to be added
      */
    public void add(AttributeDecl attributeDecl) {
        attributeList.add(attributeDecl);
        attributeMap.put(attributeDecl.getAttributeName(), attributeDecl);
        attributeDecl.setElementDecl(this);        
    }
    
    /** Removes the given {@link AttributeDecl} object.
      * 
      * @param attributeDecl the declaration to be removed
      */
    public void remove(AttributeDecl attributeDecl) {
        attributeList.remove(attributeDecl);
        attributeMap.remove(attributeDecl.getAttributeName());
        attributeDecl.setElementDecl(null);        
    }
    
    /** Removes all {@link AttributeDecl} objects 
      */
    public void clear() {
        attributeList.clear();
        attributeMap.clear();
    }
    
    /** @return an iterator across all {@link AttributeDecl} instances
      */
    public Iterator iterator() {
        return attributeList.iterator();
    }
    
    /** @return the {@link AttributeDecl} instance for the given name or null
      */
    public AttributeDecl get(String name) {
        return (AttributeDecl) attributeMap.get(name);
    }
    
    /** Writes the state of the model to the given writer
      * 
      * @param out is the writer to output to
      */
    public void write( PrintWriter out ) {
        out.println( "<!ELEMENT " + getName() + " " + getModel() + ">" );
        if ( ! attributeList.isEmpty() ) {
            out.println( "<!ATTLIST " + getName() );
            for ( Iterator iter = iterator(); iter.hasNext(); ) {
                AttributeDecl attributeDecl = (AttributeDecl) iter.next();
                attributeDecl.write(out);
            }
            out.println( "  >" );
        }
    }
    
    /** @return true if this attribute is declared in a namespace 
     */
    public boolean hasNamespace() {
        int idx = getName().indexOf(':');
        return idx >= 0;
    }
    
    public String getNamespacePrefix() {
        String name = getName();
        int idx = name.indexOf(':');
        return ( idx >= 0 ) ? name.substring(0, idx) : "";
    }
}
