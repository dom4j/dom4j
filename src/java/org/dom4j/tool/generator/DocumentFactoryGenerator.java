package org.dom4j.tool.generator;

import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;

import org.metastuff.coder.*;

/** <p><code>DocumentFactoryGenerator</code> generates a {@link DocumentFactory} 
  * implementations from a DTD declaration.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DocumentFactoryGenerator extends AbstractGenerator {
    
    /** Holds value of property documentClassName. */
    private String documentClassName = "SchemaDocument";
    
    public DocumentFactoryGenerator() {
        super( "SchemaDocumentFactory" );
    }
    
    public DocumentFactoryGenerator(String nameExpression) {
        super( nameExpression );
    }

    
    /** Getter for property documentClassName.
     * @return Value of property documentClassName.
     */
    public String getDocumentClassName() {
        return documentClassName;
    }
    
    /** Setter for property documentClassName.
     * @param documentClassName New value of property documentClassName.
     */
    public void setDocumentClassName(String documentClassName) {
        this.documentClassName = documentClassName;
    }

    // Implementation methods
    
    protected void enrich(JClass jclass) {
        this.jclass = jclass;
        
        jclass.addImportStatement( "org.dom4j.Document" );
        jclass.addImportStatement( "org.dom4j.Element" );
        jclass.addImportStatement( "org.dom4j.Namespace" );
        jclass.addImportStatement( "org.dom4j.DocumentFactory" );
        
        jclass.setExtendsClass( "DocumentFactory" );
        
        addCreateDocumentMethod();        
        addSingletonMethods();
    }
    
    protected void addCreateDocumentMethod() {
        JMethod method = new JMethod( 
            "createDocument", 
            "Document", 
            JModifier.PUBLIC, 
            "Factory method to create a new {@link Document} instance. "
                + "@return the newly created document instance" 
        );
        method.addStatement( "return new " + getDocumentClassName() + "();" );
        jclass.addMethod( method );
    }
    
    protected void addSingletonMethods() {
        String typeName = jclass.getName();
        jclass.addMember( 
            new JMember( 
                "singleton", 
                typeName, 
                JModifier.PRIVATE_STATIC_FINAL, 
                "new " + typeName + "()",
                "Singleton instance"
            )
        );
        
        JMethod method = new JMethod(
            "getInstance",
            "DocumentFactory",
            JModifier.PUBLIC_STATIC,
            "@return the singleton content factory instance"
        );
        method.addStatement( "return singleton;" );
        jclass.addMethod( method );
    }
    
    protected String createComment() {
        return "A Factory which creates the {@link Document} objects for this schema";
    }
    
}
