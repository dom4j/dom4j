/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.xpath;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.dom4j.AbstractTestCase;
import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/** Test harness for the GetPath() method
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class TestGetPath extends AbstractTestCase {

    public static void main( String[] args ) {
        TestRunner.run( suite() );
    }
    
    public static Test suite() {
        return new TestSuite( TestGetPath.class );
    }
    
    public TestGetPath(String name) {
        super(name);
    }

    // Test case(s)
    //-------------------------------------------------------------------------                    
    public void testGetPath() throws Exception {
        log( "Testing paths" );
        
        //testBranchPath( document );
        
        testPath( document, "/" );
        
        Element root = document.getRootElement();
        
        testPath( root, "/root" );
        
        List elements = root.elements();
        
        testPath( (Node) elements.get(0), "/root/author", "/root/author[1]" );
        
        for ( int i = 0, size = elements.size(); i < size; i++ ) {
            String path = "/root/author";
            String uniquePath = "/root/author";
            String pathRel = "author";
            String uniquePathRel = "author";
            if ( size > 1 ) {
                uniquePath = "/root/author[" + (i + 1) + "]";                
                uniquePathRel = "author[" + (i + 1) + "]";                
            }
            Element element = (Element) elements.get(i);
            testPath( element, path, uniquePath );
            testRelativePath( root, element, pathRel, uniquePathRel );
            
            Attribute attribute = element.attribute( "name" );
            testPath( attribute, path + "/@name", uniquePath + "/@name" );
            testRelativePath( root, attribute, pathRel + "/@name", uniquePathRel + "/@name" );
            
            Element child = element.element( "url" );
            testPath( child, path + "/url", uniquePath + "/url" );
            testRelativePath( root, child, pathRel + "/url", uniquePathRel + "/url" );
        }
    }

    public void testDefaultNamespace() throws Exception {
        SAXReader reader = new SAXReader();
        Document doc = reader.read( "xml/test/defaultNamespace.xml" );
        Element root = doc.getRootElement();
        testPath( root, "/*[name()='a']" ); 
        
        Element child = (Element) root.elements().get(0);
        testPath( child, "/*[name()='a']/*[name()='b']" ); 
        testRelativePath( root, child, "*[name()='b']" ); 
    }
    
        
    protected void testPath(Node node, String value) {
        testPath( node, value, value );
    }
    
    protected void testPath(Node node, String path, String uniquePath) {
        assertEquals( "getPath expression should be what is expected", path, node.getPath() );
        assertEquals( "getUniquePath expression should be what is expected", uniquePath, node.getUniquePath() );
    }
    
    protected void testRelativePath( Element context, Node node, String pathRel ) {
        testRelativePath( context, node, pathRel, pathRel );
    }
    
    protected void testRelativePath( Element context, Node node, String pathRel, String uniquePathRel ) {
        assertEquals( "relative getPath expression should be what is expected", pathRel, node.getPath( context ) );
        assertEquals( "relative getUniquePath expression should be what is expected", uniquePathRel, node.getUniquePath( context ) );
    }
        
        
    protected void testBranchPath(Branch branch) {
        testNodePath( branch );
        
        if ( branch instanceof Element ) {
            Element element = (Element) branch;
            for ( Iterator iter = element.attributeIterator(); iter.hasNext(); ) {
                Node node = (Node) iter.next();
                testNodePath( node );
            }
        }
        
        for ( Iterator iter = branch.nodeIterator(); iter.hasNext(); ) {
            Node node = (Node) iter.next();
            if ( node instanceof Branch ) {
                testBranchPath( (Branch) node );
            }
            else {
                testNodePath( node );
            }
        }
    }
    
    protected void testNodePath(Node node) {
        
        String path = node.getPath();
        
        log( "Path: " + path + " node: " + node );
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
