/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.tool.generator;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.ContentFactory;

import org.metastuff.coder.*;

/** <p><code>ContentFactoryGenerator</code> generates a {@link ContentFactory} 
  * implementations from a DTD declaration.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class ContentFactoryGenerator extends AbstractGenerator {
    
    /** Whether or not content outside of the DTD should be created */
    private boolean strictMode = false;
    
    /** Holds value of property elementHelperClassName. */
    private String elementHelperClassName = "ElementConstants";
    
    /** Holds value of property attributeHelperClassName. */
    private String attributeHelperClassName = "AttributeConstants";
    
    /** Holds value of property attributeIntCodes. */
    private List attributeIntCodes;
    
    /** Holds value of property attributeClassNames. */
    private List attributeClassNames;
    
    /** Holds value of property elementIntCodes. */
    private List elementIntCodes;
    
    /** Holds value of property elementClassNames. */
    private List elementClassNames;
    
    public ContentFactoryGenerator() {
        super( "SchemaContentFactory" );
    }
    
    public ContentFactoryGenerator(String nameExpression) {
        super( nameExpression );
    }

    
    // Properties
    
    public boolean isStrictMode() {
        return strictMode;
    }

    public void setStrictMode(boolean strictMode) {
        this.strictMode = strictMode;
    }
    
    /** Getter for property elementHelperClassName.
     * @return Value of property elementHelperClassName.
     */
    public String getElementHelperClassName() {
        return elementHelperClassName;
    }
    
    /** Setter for property elementHelperClassName.
     * @param elementHelperClassName New value of property elementHelperClassName.
     */
    public void setElementHelperClassName(String elementHelperClassName) {
        this.elementHelperClassName = elementHelperClassName;
    }
    
    /** Getter for property attributeHelperClassName.
     * @return Value of property attributeHelperClassName.
     */
    public String getAttributeHelperClassName() {
        return attributeHelperClassName;
    }
    
    /** Setter for property attributeHelperClassName.
     * @param attributeHelperClassName New value of property attributeHelperClassName.
     */
    public void setAttributeHelperClassName(String attributeHelperClassName) {
        this.attributeHelperClassName = attributeHelperClassName;
    }

    
    /** Getter for property attributeIntCodes.
     * @return Value of property attributeIntCodes.
     */
    public List getAttributeIntCodes() {
        return attributeIntCodes;
    }
    
    /** Setter for property attributeIntCodes.
     * @param attributeIntCodes New value of property attributeIntCodes.
     */
    public void setAttributeIntCodes(List attributeIntCodes) {
        this.attributeIntCodes = attributeIntCodes;
    }
    
    /** Getter for property attributeClassNames.
     * @return Value of property attributeClassNames.
     */
    public List getAttributeClassNames() {
        return attributeClassNames;
    }
    
    /** Setter for property attributeClassNames.
     * @param attributeClassNames New value of property attributeClassNames.
     */
    public void setAttributeClassNames(List attributeClassNames) {
        this.attributeClassNames = attributeClassNames;
    }
    
    /** Getter for property elementIntCodes.
     * @return Value of property elementIntCodes.
     */
    public List getElementIntCodes() {
        return elementIntCodes;
    }
    
    /** Setter for property elementIntCodes.
     * @param elementIntCodes New value of property elementIntCodes.
     */
    public void setElementIntCodes(List elementIntCodes) {
        this.elementIntCodes = elementIntCodes;
    }
    
    /** Getter for property elementClassNames.
     * @return Value of property elementClassNames.
     */
    public List getElementClassNames() {
        return elementClassNames;
    }
    
    /** Setter for property elementClassNames.
     * @param elementClassNames New value of property elementClassNames.
     */
    public void setElementClassNames(List elementClassNames) {
        this.elementClassNames = elementClassNames;
    }
    
    
    
    // Implementation methods
    
    protected void enrich(JClass jclass) {
        this.jclass = jclass;
        
        jclass.addImportStatement( "org.dom4j.Attribute" );
        jclass.addImportStatement( "org.dom4j.ContentFactory" );
        jclass.addImportStatement( "org.dom4j.Element" );
        jclass.addImportStatement( "org.dom4j.Namespace" );
        
        jclass.setExtendsClass( "ContentFactory" );
        
        addCreateElementMethods();        
        addCreateAttributeMethods();
        addSingletonMethods();
    }
    
    protected void addCreateElementMethods() {
        jclass.addImportStatement( "org.dom4j.Element" );
        
        JMethod method = new JMethod( 
            "createElement", 
            "Element", 
            JModifier.PUBLIC, 
            "<p>A Factory Method to create new <code>Element</code> " 
            + "instances for the given name.</p> "
            + "@param name is the name of the element to create "
            + "@return the newly created <code>Element</code> or "
            + "null if it is an invalid name of an element for this schema" 
        );
        method.addParameter( new JParameter( "name", "String" ) );
        
        String constantClass = getElementHelperClassName() + ".";
        
        method.addStatement( "switch ( " + constantClass + "getIndex( name ) ) {" );

        List intCodes = getElementIntCodes();
        List classNames = getElementClassNames();

        int size = intCodes.size();        
        for ( int i = 0; i < size; i++ ) {
            String intCode = (String) intCodes.get(i);
            String className = (String) classNames.get(i);
            
            method.addStatement( "case " + constantClass + intCode + ":" );
            method.addStatement( "    return " + className + ".create();" );
        }
        method.addStatement( "default:" );
        addElementNotFoundFactoryMethod(method);
        method.addStatement( "}" );
        
        jclass.addMethod( method );
    }
    
    protected void addElementNotFoundFactoryMethod(JMethod method) {
        jclass.addImportStatement( "org.dom4j.tree.DefaultElement" );
        
        if ( isStrictMode() ) {
            method.addStatement( "    return null;" );
        }
        else {
            method.addStatement( "    return new DefaultElement( name );" );
        }
    }
    
    
    protected void addCreateAttributeMethods() {
        jclass.addImportStatement( "org.dom4j.Attribute" );
        
        JMethod method = new JMethod( 
            "createAttribute", 
            "Attribute", 
            JModifier.PUBLIC, 
            "Factory method to create a new {@link Attribute} instance. "
                + "@return the newly created attribute instance" 
        );
        method.setComment( 
            "<p>A Factory Method to create new <code>Attribute</code> " 
            + "instances for the given name.</p> "
            + "@param name is the name of the attribute to create "
            + "@param value is the value of the attribute to create "
            + "@return the newly created <code>Attribute</code> or "
            + "null if it is an invalid name of an attribute for this schema" 
        );
        method.addParameter( new JParameter( "name", "String" ) );
        method.addParameter( new JParameter( "value", "String" ) );
        
        String constantClass = getAttributeHelperClassName() + ".";
        
        method.addStatement( "switch ( " + constantClass + "getIndex( name ) ) {" );

        List intCodes = getAttributeIntCodes();
        List classNames = getAttributeClassNames();

        int size = intCodes.size();        
        for ( int i = 0; i < size; i++ ) {
            String intCode = (String) intCodes.get(i);
            String className = (String) classNames.get(i);
            
            method.addStatement( "case " + constantClass + intCode + ":" );
            method.addStatement( "    return " + className + ".create( value );" );
        }
        method.addStatement( "default:" );
        addAttributeNotFoundFactoryMethod(method);
        method.addStatement( "}" );
        
        jclass.addMethod( method );
    }
    
    protected void addAttributeNotFoundFactoryMethod(JMethod method) {
        jclass.addImportStatement( "org.dom4j.tree.DefaultAttribute" );
        
        if ( isStrictMode() ) {
            method.addStatement( "    return null;" );
        }
        else {
            method.addStatement( "    return new DefaultAttribute( name, value );" );
        }
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
            "ContentFactory",
            JModifier.PUBLIC_STATIC,
            "@return the singleton content factory instance"
        );
        method.addStatement( "return singleton;" );
        jclass.addMethod( method );
    }
    
    protected String createComment() {
        return "Creates the content objects for the schema";
    }
}




/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "DOM4J" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of MetaStuff, Ltd.  For written permission,
 *    please contact dom4j-info@metastuff.com.
 *
 * 4. Products derived from this Software may not be called "DOM4J"
 *    nor may "DOM4J" appear in their names without prior written
 *    permission of MetaStuff, Ltd. DOM4J is a registered
 *    trademark of MetaStuff, Ltd.
 *
 * 5. Due credit should be given to the DOM4J Project
 *    (http://dom4j.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * METASTUFF, LTD. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * $Id$
 */
