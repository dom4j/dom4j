/*
 * Andreas Rudert, andreas@rudert-home.de
 */

package org.dom4j.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

import junit.framework.*;
import junit.textui.TestRunner;

import org.dom4j.*;

import java.io.StringWriter;
import java.io.StringReader;

/** A simple test harness to check that the XML Writer works
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class TestWriteUnmergedText extends AbstractTestCase {


    private String inputText = "<?xml version = \"1.0\"?><TestEscapedEntities><TEXT>Test using &lt; &amp; &gt;</TEXT></TestEscapedEntities>";
    protected static final boolean VERBOSE = true;
    
    
    public static void main( String[] args ) {
        TestRunner.run( suite() );
    }
    
    public static Test suite() {
        return new TestSuite( TestWriteUnmergedText.class );
    }
    
    public TestWriteUnmergedText(String name) {
        super(name);
    }

    // Test case(s)
    //-------------------------------------------------------------------------                    
    public String readwriteText(OutputFormat outFormat, boolean mergeAdjacentText) throws Exception {
        
        StringWriter out = new StringWriter();
        StringReader in = new StringReader(inputText);
        SAXReader reader = new SAXReader();
 
        //reader.setValidation(true);
        reader.setMergeAdjacentText(mergeAdjacentText);

        Document document = reader.read(in);

        XMLWriter writer = outFormat == null ? new XMLWriter(out) : new XMLWriter(out, outFormat);
        writer.write(document);
        writer.close(); 
        
        String outText = out.toString();
        return outText;
    }        
    
    
    public void testWithoutFormatNonMerged() throws Exception {

        String outText = readwriteText(null, false);
            
        if ( VERBOSE ) {
            log( "Text output is ["  );
            log( outText );
            log( "]. Done" );
        }
        
        // should contain &amp; and &lt;
        assertTrue("Output text contains \"&amp;\"", outText.lastIndexOf("&amp;") >= 0);
        assertTrue("Output text contains \"&lt;\"", outText.lastIndexOf("&lt;") >= 0);
    } 
           
    public void testWithCompactFormatNonMerged() throws Exception {

        String outText = readwriteText(OutputFormat.createCompactFormat(), false);
            
        if ( VERBOSE ) {
            log( "Text output is ["  );
            log( outText );
            log( "]. Done" );
        }
        
        // should contain &amp; and &lt;
        assertTrue("Output text contains \"&amp;\"", outText.lastIndexOf("&amp;") >= 0);
        assertTrue("Output text contains \"&lt;\"", outText.lastIndexOf("&lt;") >= 0);
    }
            
    public void testWithPrettyPrintFormatNonMerged() throws Exception {

        String outText = readwriteText(OutputFormat.createPrettyPrint(), false);
            
        if ( VERBOSE ) {
            log( "Text output is ["  );
            log( outText );
            log( "]. Done" );
        }
        
        // should contain &amp; and &lt;
        assertTrue("Output text contains \"&amp;\"", outText.lastIndexOf("&amp;") >= 0);
        assertTrue("Output text contains \"&lt;\"", outText.lastIndexOf("&lt;") >= 0);
    }        
   
    public void testWithoutFormatMerged() throws Exception {

        String outText = readwriteText(null, true);
            
        if ( VERBOSE ) {
            log( "Text output is ["  );
            log( outText );
            log( "]. Done" );
        }
        
        // should contain &amp; and &lt;
        assertTrue("Output text contains \"&amp;\"", outText.lastIndexOf("&amp;") >= 0);
        assertTrue("Output text contains \"&lt;\"", outText.lastIndexOf("&lt;") >= 0);
    } 
           
    public void testWithCompactFormatMerged() throws Exception {

        String outText = readwriteText(OutputFormat.createCompactFormat(), true);
            
        if ( VERBOSE ) {
            log( "Text output is ["  );
            log( outText );
            log( "]. Done" );
        }
        
        // should contain &amp; and &lt;
        assertTrue("Output text contains \"&amp;\"", outText.lastIndexOf("&amp;") >= 0);
        assertTrue("Output text contains \"&lt;\"", outText.lastIndexOf("&lt;") >= 0);
    }
            
    public void testWithPrettyPrintFormatMerged() throws Exception {

        String outText = readwriteText(OutputFormat.createPrettyPrint(), true);
            
        if ( VERBOSE ) {
            log( "Text output is ["  );
            log( outText );
            log( "]. Done" );
        }
        
        // should contain &amp; and &lt;
        assertTrue("Output text contains \"&amp;\"", outText.lastIndexOf("&amp;") >= 0);
        assertTrue("Output text contains \"&lt;\"", outText.lastIndexOf("&lt;") >= 0);
    }        
}

