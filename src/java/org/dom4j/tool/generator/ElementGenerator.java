package org.dom4j.tool.generator;

import java.util.Iterator;

import org.dom4j.Element;
import org.dom4j.tool.dtd.ElementDecl;

import org.metastuff.coder.*;

/** <p><code>ElementGenerator</code> generates {@link Element} 
  * implementations from a DTD declaration.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class ElementGenerator extends AbstractGenerator {

    protected ElementDecl elementDecl;

    /** Holds value of property contentFactoryClassName. */
    private String contentFactoryClassName = "SchemaContentFactory";
    
    public ElementGenerator() {
        super( "${ElementName}Element" );
    }
    
    public ElementGenerator(String nameExpression) {
        super( nameExpression );
    }
    
    
    public void setElementDecl(ElementDecl elementDecl) {
        this.elementDecl = elementDecl;
    }

    /** Getter for property contentFactoryClassName.
     * @return Value of property contentFactoryClassName.
     */
    public String getContentFactoryClassName() {
        return contentFactoryClassName;
    }
    
    /** Setter for property contentFactoryClassName.
     * @param contentFactoryClassName New value of property contentFactoryClassName.
     */
    public void setContentFactoryClassName(String contentFactoryClassName) {
        this.contentFactoryClassName = contentFactoryClassName;
    }
    
    
    
    
    protected void enrich(JClass jclass) {
        super.enrich(jclass);
        
        jclass.addImportStatement( "org.dom4j.ContentFactory" );
        jclass.addImportStatement( "org.dom4j.Element" );
        jclass.addImportStatement( "org.dom4j.tree.AbstractElement" );
        jclass.addImportStatement( "org.dom4j.tree.NameModel" );
        
        jclass.setComment( createComment() );
        jclass.setExtendsClass( "AbstractElement" );
        
        addConstructors();
        addNameModel();
        addAttributeModel();
        addContentModel();
        addContentFactory();
        addCreateHelperMethod();
    }
    
    protected void addConstructors() {
        jclass.addConstructor( new JConstructor() );
    }
    
    protected void addCreateHelperMethod() {
        JMethod method = new JMethod( 
            "create", 
            "Element", 
            JModifier.PUBLIC_STATIC, 
            "@return an <code>Element</code> instance which may be new or "
                + "could be shared for some empty elements such as "
                + "&lt;p&gt; and &lt;br&gt; in HTML" 
        );
        method.addStatement( "return new " + jclass.getName() + "();" );
        jclass.addMethod( method );
    }
    
    protected void addNameModel() {
        jclass.addMember( 
            new JMember(
                "NAME_MODEL", 
                "NameModel", 
                JModifier.PROTECTED_STATIC_FINAL, 
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
    
    protected void addAttributeModel() {
    }
    
    protected void addContentModel() {
    }
    
    protected void addContentFactory() {
        jclass.addMember( 
            new JMember(
                "CONTECT_FACTORY", 
                "ContentFactory", 
                JModifier.PROTECTED_STATIC_FINAL, 
                getContentFactoryClassName() + ".getInstance()",
                "The <code>ContentFactory</code> instance used by default"
            )
        );
        
        JMethod method = new JMethod( 
            "getContentFactory", 
            "ContentFactory", 
            JModifier.PROTECTED, 
            "@return the <code>ContentFactory</code> instance to be used for the <code>Element</code>" 
        );
        method.addStatement( "return CONTECT_FACTORY;" );
        jclass.addMethod( method );
    }
    
    protected String getElementName() {
        return elementDecl.getName();
    }
    
    protected String getContentModel() {
        return elementDecl.getModel();
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
        buffer.append( getElementName() );        
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
        return "An {@link Element} implementation of the &lt;" 
            + getElementName() + "/&gt; tag.";
    }
    
}
