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
