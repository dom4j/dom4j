package org.dom4j.tree;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.dom4j.DocumentType;
import org.dom4j.TreeVisitor;

/** <p><code>AbstractDocumentType</code> is an abstract base class for 
  * tree implementors to use for implementation inheritence.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class AbstractDocumentType extends AbstractNode implements DocumentType {

    public AbstractDocumentType() {
    }
    
    public String toString() {
        return super.toString() + " [DocumentType: " + asXML() + "]";
    }

    public String getName() {
        return getElementName();
    }
    
    public void setName(String name) {
        setElementName(name);
    }
    
    public String asXML() {
        StringBuffer buffer = new StringBuffer( "<!DOCTYPE " );
        buffer.append( getElementName() );
        
        boolean hasPublicID = false;
        String publicID = getPublicID();
        
        if ( publicID != null && publicID.length() > 0 ) {
            buffer.append( " PUBLIC \"" );
            buffer.append( publicID );
            buffer.append( "\"" );
            hasPublicID = true;
        }
        
        String systemID = getSystemID();
        if ( systemID != null && systemID.length() > 0 ) {
            if (!hasPublicID) {
                buffer.append(" SYSTEM");
            }
            buffer.append( " \"" );
            buffer.append( systemID );
            buffer.append( "\"" );
        }
        buffer.append(">");
        return buffer.toString();
    }
    
    public void accept(TreeVisitor visitor) {
        visitor.visit(this);
    }
}


