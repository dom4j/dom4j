/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.tool.generator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.TreeException;
import org.dom4j.tool.dtd.ElementDecl;
import org.dom4j.tool.dtd.AttributeDecl;

import org.metastuff.coder.*;

/** <p><code>DefaultStringConstantsGenerator</code> generates the string constants
  * used to generate custom DOM4J schemas.
  * This implementation uses a Map to resolve Strings into Integers</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DefaultStringConstantsGenerator extends StringConstantsGenerator {

    public DefaultStringConstantsGenerator(String nameExpression) {
        super( nameExpression );
    }
    
    protected void enrich(JClass jclass) {
        super.enrich(jclass);
        
        addStringToIntMap();
    }
    
    protected void addToIntegerMethod() {
        JMethod method = new JMethod( "getIndex", "int", JModifier.PUBLIC_STATIC_FINAL );
        method.setComment( 
            "<p>Converts a string name into an integer index code.</p> "
            + "@param name is the constant represented as a value "
            + "@return the integer index of the given string constant "
            + "or -1 if the string is not a valid" 
        );
        method.addParameter( new JParameter( "code", "String" ) );

        method.addStatement( "Integer i = (Integer) stringToIntMap.get( code );" );
        method.addStatement( "return (i != null) ? i.intValue() : -1;" );
        
        jclass.addMethod( method );
    }
    
    protected void addStringToIntMap() {
        jclass.addImportStatement( "java.util.HashMap" );
        jclass.addImportStatement( "java.util.Map" );
        
        jclass.addMember( 
            new JMember( 
                "stringToIntMap", "Map", 
                JModifier.PRIVATE_STATIC_FINAL, "createStringToIntMap()"
            ) 
        );
        
        JMethod method = new JMethod( 
            "createStringToIntMap", "Map", JModifier.PROTECTED_STATIC_FINAL 
        );
        method.setComment( 
            "<p>Creates an index <code>Map</code> of the string names to "
            + "Integer index codes. This method is used internally to "
            + "initialise the static stringToIntMap variable.</p> " 
            + "@return the newly created <code>Map</code> of string names "
            + "to integer indexes"
        );
        
        method.addStatement( "Map answer = new HashMap();" );

        List intCodes = getIntCodes();
        List textCodes = getTextCodes();
        int size = intCodes.size();        
        for ( int i = 0; i < size; i++ ) {
            String intCode = (String) intCodes.get(i);
            String textCode = (String) textCodes.get(i);
            
            method.addStatement( "answer.put( " + textCode + ", new Integer( " + intCode + " ) );" );
        }
        method.addStatement( "return answer;" );
        
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
