package org.dom4j.tool.dtd;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** <p><code>ElementDeclaration</code> is a JavaBean representing the element
  * declaration taken from a DTD.</p>
  *
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class AttributeDecl {

    /** The element name. */
    private String elementName;
    
    /** The attribute name. */
    private String attributeName;
    
    /** The type of the attribute . */
    private String type;
    
    /** The default value of the attribute. */
    private String defaultValue;

    /** The value of the attribute. */
    private String value;
    
    /** The element declaration to which this attribute belongs */
    private ElementDecl elementDecl;
    
    public AttributeDecl() {
    }
    
    public AttributeDecl(String elementName, String attributeName, String type, String defaultValue, String value) {
        this.elementName = elementName;
        this.attributeName = attributeName;
        this.type = type;
        this.defaultValue = defaultValue;
        this.value = value;
    }

    
    /** Getter for property attributeName.
      * @return Value of property attributeName.
      */
    public String getAttributeName() {
        return attributeName;
    }
    
    /** Setter for property attributeName.
      * @param attributeName New value of property attributeName.
      */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
    
    /** Getter for property elementName.
      * @return Value of property elementName.
      */
    public String getElementName() {
        return elementName;
    }
    
    /** Setter for property elementName.
      * @param elementName New value of property elementName.
      */
    public void setElementName(String elementName) {
        this.elementName = elementName;
    }
    
    /** Getter for property value.
      * @return Value of property value.
      */
    public String getValue() {
        return value;
    }
    
    /** Setter for property value.
      * @param value New value of property value.
      */
    public void setValue(String value) {
        this.value = value;
    }
    
    /** Getter for property type.
     * @return Value of property type.
     */
    public String getType() {
        return type;
    }
    
    /** Setter for property type.
     * @param type New value of property type.
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /** Getter for property defaultValue.
     * @return Value of property defaultValue.
     */
    public String getDefaultValue() {
        return defaultValue;
    }
    
    /** Setter for property defaultValue.
     * @param defaultValue New value of property defaultValue.
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    /** @return the element declaration this attribute declaration refers to 
      */
    public ElementDecl getElementDecl() {
        return elementDecl;
    }

    protected void setElementDecl(ElementDecl elementDecl) {
        this.elementDecl = elementDecl;
    }
    /** Writes the state of the model to the given writer
      * 
      * @param out is the writer to output to
      */
    public void write( PrintWriter out ) {
        out.print( "  " + getAttributeName() + " " + getType() + " " );
        String value = getValue();
        if ( value != null ) {
            out.print( value );
        }
        else {
            out.print( getDefaultValue() );
        }
        out.println();
    }
    
    /** @return true if this attribute is declared in a namespace 
     */
    public boolean hasNamespace() {
        int idx = getAttributeName().indexOf(':');
        return idx >= 0;
    }
    
    public String getNamespacePrefix() {
        String name = getAttributeName();
        int idx = name.indexOf(':');
        return ( idx >= 0 ) ? name.substring(0, idx) : "";
    }
}
