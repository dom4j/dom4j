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
