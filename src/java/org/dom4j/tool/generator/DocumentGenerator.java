package org.dom4j.tool.generator;

import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.tool.dtd.ElementDecl;
import org.dom4j.tool.dtd.ElementDecl;

import org.metastuff.coder.*;

/** <p><code>DocumentGenerator</code> generates a {@link Document} 
  * implementation from a DTD declaration.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DocumentGenerator extends AbstractGenerator {

    /** Holds value of property contentFactoryClassName. */
    private String contentFactoryClassName = "SchemaContentFactory";
    
    public DocumentGenerator() {
        super( "SchemaDocument" );
    }
    
    public DocumentGenerator(String nameExpression) {
        super( nameExpression );
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
        
        jclass.addImportStatement( "org.dom4j.Document" );
        jclass.addImportStatement( "org.dom4j.Element" );
        jclass.addImportStatement( "org.dom4j.tree.ContentFactory" );
        jclass.addImportStatement( "org.dom4j.tree.DefaultDocument" );
        
        jclass.setComment( createComment() );
        jclass.setExtendsClass( "DefaultDocument" );
        
        addConstructors();
        addContentFactory();
    }
    
    protected void addConstructors() {
        jclass.addConstructor( new JConstructor() );
        
/*        
        JConstructor constructor = new JConstructor();
        constructor.addParameter( new JParameter( "element", "Element" ) );
        constructor.addStatement( "super( element );" );
        jclass.addConstructor( constructor );
*/
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
    
    protected String createComment() {
        return "An {@link Document} implementation for this schema";
    }    
}
