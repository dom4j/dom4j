package org.dom4j.tool.generator;

import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.tool.dtd.ElementDecl;
import org.dom4j.tool.dtd.AttributeDecl;

import org.metastuff.coder.*;

/** <p><code>AttributeGenerator</code> generates {@link Attribute} 
  * implementations from a DTD declaration.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class AttributeGenerator extends AbstractGenerator {

    protected AttributeDecl attributeDecl;

    public AttributeGenerator() {
        super( "${elementName}_${attributeName}_Attribute" );
    }
    
    public AttributeGenerator(String nameExpression) {
        super( nameExpression );
    }
    
    
    public void setAttributeDecl(AttributeDecl attributeDecl) {
        this.attributeDecl = attributeDecl;
    }

    protected String getAttributeName() {
        return attributeDecl.getAttributeName();
    }
    
    protected String getElementName() {
        return attributeDecl.getElementName();
    }
    
    protected boolean hasNamespace() {
        return false;
    }
    
    protected String getNamespacePrefix() {
        return "";
    }
    
    protected String getNamespaceURI() {
        return "";
    }
    
    protected String createNamespaceExpression() {
        StringBuffer buffer = new StringBuffer( "NameModel.get( \"" );
        buffer.append( getAttributeName() );        
        if ( hasNamespace() ) {
            buffer.append( ", " );
            buffer.append( getNamespacePrefix() );        
            buffer.append( ", " );
            buffer.append( getNamespaceURI() );        
        }
        buffer.append( "\" )" );
        return buffer.toString();
    }
        
    protected String createComment() {
        return "An implementation of {@link Attribute} for the &lt;" 
            + getElementName() + "/&gt; element and attribute name '" 
            + getAttributeName() + "'";
    }
}
