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
