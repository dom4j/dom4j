package org.dom4j.tool.generator;

import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.tool.dtd.ElementDecl;
import org.dom4j.tool.dtd.AttributeDecl;

import org.metastuff.coder.*;

/** <p><code>CachingAttributeGenerator</code> generates {@link Attribute} 
  * implementations from a DTD declaration, caching instances for reuse.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class CachingAttributeGenerator extends DefaultAttributeGenerator {

    protected void addCreateHelperMethod() {
        jclass.addImportStatement( "java.util.HashMap" );
        jclass.addImportStatement( "java.util.Map" );
        
        jclass.addMember( 
            new JMember(
                "cache", 
                "Map", 
                JModifier.PROTECTED_STATIC, 
                "new HashMap()",
                "The cache of <code>Attribute</code> instances returned by "
                    + "the {@link #create} method"
            )
        );
        
    
        JMethod method = new JMethod( 
            "create", 
            "Attribute", 
            JModifier.PUBLIC_STATIC, 
            "@return an <code>Attribute</code> instance which will be taken "
                + "from the cache or created and added to the cache" 
        );
        method.addParameter( new JParameter("value", "String") );
        method.addStatement( "Attribute answer = (Attribute) cache.get( value );" );
        method.addStatement( "if ( answer == null ) {" );
        method.addStatement( "    answer = new " + jclass.getName() + "( value );" );
        method.addStatement( "    cache.put( value, answer );" );
        method.addStatement( "}" );
        method.addStatement( "return answer;" );
        jclass.addMethod( method );
    }
    
}
