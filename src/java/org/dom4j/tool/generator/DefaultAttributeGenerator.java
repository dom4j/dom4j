package org.dom4j.tool.generator;

import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.tool.dtd.ElementDecl;
import org.dom4j.tool.dtd.AttributeDecl;

import org.metastuff.coder.*;
import org.metastuff.coder.codelet.PropertyCodelet;

/** <p><code>AttributeGenerator</code> generates {@link Attribute} 
  * implementations from a DTD declaration.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DefaultAttributeGenerator extends AttributeGenerator {

    public DefaultAttributeGenerator() {
        super( "${AttributeName}Attribute" );
    }
    
    protected void enrich(JClass jclass) {
        this.jclass = jclass;
        jclass.addImportStatement( "org.dom4j.Attribute" );
        jclass.addImportStatement( "org.dom4j.Element" );
        jclass.addImportStatement( "org.dom4j.tree.AbstractAttribute" );
        jclass.addImportStatement( "org.dom4j.tree.NameModel" );
        
        jclass.setComment( createComment() );
        jclass.setExtendsClass( "AbstractAttribute" );
        
        PropertyCodelet.addProperty( jclass, "value", "String" );
        
        addConstructors();
        addNameModel();
        addCreateHelperMethod();
    }
    
    protected void addConstructors() {
        jclass.addConstructor( new JConstructor() );
        
        JConstructor constructor = new JConstructor();
        constructor.addParameter( new JParameter("value", "String") );
        constructor.addStatement( "this.value = value" );
        jclass.addConstructor(constructor);
    }
    
    protected void addNameModel() {
        jclass.addMember( 
            new JMember(
                "NAME_MODEL", 
                "NameModel", 
                JModifier.PUBLIC_STATIC_FINAL, 
                createNamespaceExpression() 
            )
        );
        
        JMethod method = new JMethod( 
            "getNameModel", 
            "NameModel", 
            JModifier.PROTECTED, 
            "@return the <code>NameModel</code> instance to be used for the <code>Attribute</code>" 
        );
        method.addStatement( "return NAME_MODEL;" );
        jclass.addMethod( method );
    }
    
    protected void addCreateHelperMethod() {
        JMethod method = new JMethod( 
            "create", 
            "Attribute", 
            JModifier.PUBLIC_STATIC, 
            "@return an <code>Attribute</code> instance which may be new or "
                + "could be a cached instance if the attribute is being cached "
                + "or it is an enumeration" 
        );
        method.addParameter( new JParameter("value", "String") );
        method.addStatement( "return new " + jclass.getName() + "( value );" );
        jclass.addMethod( method );
    }
    
}
