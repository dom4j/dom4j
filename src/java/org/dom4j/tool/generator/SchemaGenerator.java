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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.dom4j.tool.dtd.AttributeDecl;
import org.dom4j.tool.dtd.DocumentDecl;
import org.dom4j.tool.dtd.DTDReader;
import org.dom4j.tool.dtd.ElementDecl;

import org.metastuff.coder.JClass;
import org.metastuff.coder.JClassHelper;
import org.metastuff.coder.codelet.PropertyCodelet;
import org.metastuff.coder.writer.JClassSourceFileWriter;
import org.metastuff.util.StringUtils;

/** <p><code>SchemaGenerator</code> generates a DOM4J schema for the given
  * document type declaration.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class SchemaGenerator {

    private DefaultAttributeGenerator defaultAttributeGenerator = new DefaultAttributeGenerator();
    private CachingAttributeGenerator cachingAttributeGenerator = new CachingAttributeGenerator();
    private DefaultElementGenerator defaultElementGenerator = new DefaultElementGenerator();
    
    /** The writer of source files */
    private JClassSourceFileWriter writer = new JClassSourceFileWriter();

    /** The variables used in name substitution code */
    private HashMap variables = new HashMap();
    
    /** Contains the names of the attributes which have been generated so that 
      * they are only generated once for any element to use
      */
    private HashSet generatedAttributeNames = new HashSet();
    
    /** stores an ordered list of element names that have been created
      * so that we can pass it on to factories and constant generators
      */
    protected ArrayList elementNames = new ArrayList();
    
    /** stores an ordered list of element class names that have been created
      * so that we can pass it on to factories 
      */
    protected ArrayList elementClassNames = new ArrayList();

    /** stores an ordered list of attribute names that have been created
      * so that we can pass it on to factories and constant generators
      */
    protected ArrayList attributeNames = new ArrayList();
    
    /** stores an ordered list of attribute class names that have been created
      * so that we can pass it on to factories 
      */
    protected ArrayList attributeClassNames = new ArrayList();

    
    public SchemaGenerator() {
    }
        
    public static void main(String[] args) {
        run( new SchemaGenerator(), args );
    }
    
    protected static void run(SchemaGenerator generator, String[] args) {
        if ( args.length < 3 ) {
            System.out.println( "Usage: <xmlFileName> <outputDirectory> <packageName> [xmlReaderClassName]" );
            return;
        }

        try {
            DTDReader reader = new DTDReader();        
            
            String xmlFileName = args[0];

            generator.setOutputDirectory( args[1] );
            generator.setOutputPackage( args[2] );
            
            if ( args.length > 3 ) {
                reader.setXMLReaderClassName( args[3] );
            }
            DocumentDecl model = reader.read( xmlFileName );
            
            generator.generate( model );
        }
        catch (Exception e) {
            System.out.println( "Exception occurred: " + e );
            e.printStackTrace ();
        }
    }
    
    public void generate(DocumentDecl documentDecl) {        
        clear();
        for ( Iterator iter = documentDecl.iterator(); iter.hasNext(); ) {
            ElementDecl elementDecl = (ElementDecl) iter.next();
            generate(elementDecl);
        }
        makeFactories();
    }
    
    public void generate(ElementDecl elementDecl) {
        ElementGenerator generator = getElementGenerator(elementDecl);
        if ( generator != null ) {
            make(generator, elementDecl);
        }
        for ( Iterator iter = elementDecl.iterator(); iter.hasNext(); ) {
            AttributeDecl attributeDecl = (AttributeDecl) iter.next();
            String attributeName = attributeDecl.getAttributeName();
            if ( ! generatedAttributeNames.contains( attributeName ) ) {
                generate(attributeDecl);
                generatedAttributeNames.add( attributeName );
            }
        }
    }
    
    public void generate(AttributeDecl attributeDecl) {
        AttributeGenerator generator = getAttributeGenerator(attributeDecl);
        if ( generator != null ) {
            make(generator, attributeDecl);
        }
    }

    // Properties
    
    /** Getter for property outputDirectory.
     * @return Value of property outputDirectory.
     */
    public String getOutputDirectory() {
        return writer.getDirectoryName();
    }    
    
    /** Setter for property outputDirectory.
     * @param outputDirectory New value of property outputDirectory.
     */
    public void setOutputDirectory(String outputDirectory) {
        writer.setDirectoryName(outputDirectory);
    }
    
    /** Getter for property outputPackage.
     * @return Value of property outputPackage.
     */
    public String getOutputPackage() {
        return (String) variables.get( "package" );
    }    
    
    /** Setter for property outputPackage.
     * @param outputPackage New value of property outputPackage.
     */
    public void setOutputPackage(String outputPackage) {
        variables.put( "package", outputPackage );
    }

    
    // Implementation methods
    
    protected void make(ElementGenerator generator, ElementDecl elementDecl) {
        configureVariables( elementDecl );
        generator.setElementDecl( elementDecl );
        JClass jclass = runGenerator(generator);
        if ( jclass != null ) {
            elementClassNames.add( JClassHelper.getFullName(jclass) );
            elementNames.add( elementDecl.getName() );
            
            writer.handle( jclass );
        }

        // we may wish to add the class name to a factory class
    }
    
    protected void make(AttributeGenerator generator, AttributeDecl attributeDecl) {
        configureVariables( attributeDecl );
        generator.setAttributeDecl( attributeDecl );
        JClass jclass = runGenerator(generator);
        if ( jclass != null ) {
            attributeClassNames.add( JClassHelper.getFullName(jclass) );
            attributeNames.add( attributeDecl.getAttributeName() );
            
            writer.handle( jclass );
        }

        // we may wish to add the class name to a factory class
    }
    
    protected void makeFactories() {
        makeContentFactory();
        makeDocument();
        makeDocumentFactory();
    }
    
    protected void makeContentFactory() {
        ContentFactoryGenerator generator = new ContentFactoryGenerator();
        
        DefaultStringConstantsGenerator elementConstantGenerator 
            = new DefaultStringConstantsGenerator( generator.getElementHelperClassName() );
        
        elementConstantGenerator.setNames( elementNames );
        runGenerator(elementConstantGenerator);
        
        DefaultStringConstantsGenerator attributeConstantGenerator 
            = new DefaultStringConstantsGenerator( generator.getAttributeHelperClassName() );
        
        attributeConstantGenerator.setNames( attributeNames );
        runGenerator(attributeConstantGenerator);
        
        generator.setElementClassNames( elementClassNames );
        generator.setElementIntCodes( elementConstantGenerator.getIntCodes() );
        
        generator.setAttributeClassNames( attributeClassNames );
        generator.setAttributeIntCodes( attributeConstantGenerator.getIntCodes() );
        
        runGenerator(generator);
    }
    
    protected void makeDocument() {
        DocumentGenerator generator = new DocumentGenerator();
        runGenerator(generator);
    }
    
    protected void makeDocumentFactory() {
        DocumentFactoryGenerator generator = new DocumentFactoryGenerator();
        runGenerator(generator);
    }
    
    protected JClass runGenerator(AbstractGenerator generator) {
        generator.setVariables( variables );
        JClass jclass = generator.make();
        if ( jclass != null ) {
            writer.handle( jclass );
        }
        return jclass;
    }
        
    protected ElementGenerator getElementGenerator(ElementDecl elementDecl) {
        return defaultElementGenerator;
    }
    
    protected AttributeGenerator getAttributeGenerator(AttributeDecl attributeDecl) {
        if ( attributeDecl.hasNamespace() ) {
            return null;
        }
        return defaultAttributeGenerator;
    }
    
    protected void configureVariables(ElementDecl elementDecl) {
        variables.remove( "attributeName" );
        variables.remove( "AttributeName" );
        
        String elementName = elementDecl.getName();
        variables.put( "elementName", elementName );
        variables.put( "ElementName", getPropertyName( elementName ) );
    }
    
    protected void configureVariables(AttributeDecl attributeDecl) {
        String attributeName = attributeDecl.getAttributeName();
        String elementName = attributeDecl.getElementName();
        
        variables.put( "attributeName", attributeName );
        variables.put( "AttributeName", getPropertyName( attributeName ) );
        
        variables.put( "elementName", elementName );
        variables.put( "ElementName", getPropertyName( elementName ) );
    }
    
    protected String getPropertyName( String propertyName ) {
        String answer = capitaliseFirstChar( propertyName );
        answer = removeSeperator( answer, '-' );
        return answer;
    }
    
    protected String capitaliseFirstChar( String propertyName ) {
        return PropertyCodelet.getCapitalisedPropertyName( propertyName );
    }
    
    protected String removeSeperator(String answer, char ch) {
        // remove any seperators such as '-'
        int idx = answer.indexOf(ch);
        if (idx < 0) {
            return answer;
        }
        else {
            StringBuffer buffer = new StringBuffer(answer);
            for ( int length = buffer.length(); idx < length; idx++ ) {
                if ( buffer.charAt(idx) == ch ) {
                    do {
                        buffer.deleteCharAt(idx);
                        length--;
                    }
                    while ( buffer.charAt(idx) == ch );
                    
                    if ( idx < length ) {
                        char nextChar = buffer.charAt(idx);
                        nextChar = Character.toUpperCase(nextChar);
                        buffer.setCharAt(idx, nextChar);
                    }
                }
            }
            return buffer.toString();
        }
    }
    
    protected void clear() {
        generatedAttributeNames.clear();
        elementNames.clear();
        elementClassNames.clear();
        attributeNames.clear();
        attributeClassNames.clear();
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
