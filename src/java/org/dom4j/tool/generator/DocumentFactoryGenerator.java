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
