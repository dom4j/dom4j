/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 *
 * $Id$
 */

package org.dom4j.rule;

import java.util.ArrayList;
import java.util.Collections;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.dom4j.CDATA;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;

/** Tests the ordering of Rules
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class TestRule extends TestCase {
    
    protected DocumentFactory factory = new DocumentFactory();
    
    public TestRule(String name) {
        super( name );
    }
    
    public static void main(String[] args) {
        TestRunner.run( suite() );
    }
    
    public static Test suite() {
        return new TestSuite( TestRule.class );
    }
    
    public void testOrder() throws Exception {
        testGreater( "foo", "*" );
    }
    
    protected void testGreater(String expr1, String expr2) throws Exception {
        System.out.println( "parsing: " + expr1 + " and " + expr2 );
        
        Rule r1 = createRule( expr1 );
        Rule r2 = createRule( expr2 );
        
        System.out.println( "rule1: " + r1 + " rule2: " + r2 );
        
        int value = r1.compareTo( r2 );
        
        System.out.println( "Comparison: " + value );
        
        assertTrue( "r1 > r2", value > 0 );
        
        ArrayList list = new ArrayList();
        list.add( r1 );
        list.add( r2 );
        
        Collections.sort( list );
        
        assertTrue( "r2 should be first", list.get(0) == r2 );
        assertTrue( "r1 should be next", list.get(1) == r1 );

        list = new ArrayList();
        list.add( r2 );
        list.add( r1 );
        
        Collections.sort( list );
        
        assertTrue( "r2 should be first", list.get(0) == r2 );
        assertTrue( "r1 should be next", list.get(1) == r1 );
/*        
        TreeSet set = new TreeSet();
        set.add( r1 );
        set.add( r2 );
        
        assertTrue( "r2 should be first", set.first() == r2 );
        assertTrue( "r1 should be next", set.last() == r1 );
        
        Object[] array = set.toArray();
        
        assertTrue( "r2 should be first", array[0] == r2 );
        assertTrue( "r1 should be next", array[1] == r1 );
        
        set = new TreeSet();
        set.add( r2 );
        set.add( r1 );
        
        assertTrue( "r2 should be first", set.first() == r2 );
        assertTrue( "r1 should be next", set.last() == r1 );
        
        array = set.toArray();
        
        assertTrue( "r2 should be first", array[0] == r2 );
        assertTrue( "r1 should be next", array[1] == r1 );
*/
    }

    public void testDocument() {
        Rule rule = createRule( "/" );
        Document document = factory.createDocument();
        document.addElement( "foo" );
        
        assertTrue( "/ matches document", rule.matches( document ) );
        assertTrue( "/ does not match root element", ! rule.matches( document.getRootElement() ) );
    }
    
    public void testTextMatchesCDATA() {
        CDATA cdata = factory.createCDATA( "<>&" );
        Rule rule = createRule( "text()" );
        
        assertTrue( "text() matches CDATA", rule.matches( cdata ) );
    }
    
    protected Rule createRule(String expr) {
        Pattern pattern = factory.createPattern( expr );
        return new Rule( pattern );
    }
}
