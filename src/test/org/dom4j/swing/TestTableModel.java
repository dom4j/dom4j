/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 *
 * $Id$
 */

package org.dom4j.swing;

import javax.swing.table.TableModel;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

/** Tests the Swing TableModel using a dom4j document.
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class TestTableModel extends TestCase
{
    public TestTableModel(String name) {
        super( name );
    }
    
    public static void main(String[] args) {
        TestRunner.run( suite() );
    }
    
    public static Test suite() {
        return new TestSuite( TestTableModel.class );
    }
    
    public void testServletTable() throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read( "xml/web.xml" );
        
        XMLTableDefinition tableDefinition = new XMLTableDefinition();
        tableDefinition.setRowExpression( "/web-app/servlet" );
        tableDefinition.addStringColumn( "Name", "servlet-name" );
        tableDefinition.addStringColumn( "Class", "servlet-class" );
        tableDefinition.addStringColumn( "Mapping", "../servlet-mapping[servlet-name=$Name]/url-pattern" );
        
        XMLTableModel tableModel = new XMLTableModel( tableDefinition, document );

        // now lets test the values come out
        assertEquals( "correct row count", tableModel.getRowCount(), 2 );
        assertEquals( "correct column count", tableModel.getColumnCount(), 3 );
        
        assertColumnNameEquals( tableModel, 0, "Name" );
        assertColumnNameEquals( tableModel, 1, "Class" );
        assertColumnNameEquals( tableModel, 2, "Mapping" );
        
        assertCellEquals( tableModel, 0, 0, "snoop" );
        assertCellEquals( tableModel, 1, 0, "file" );
        assertCellEquals( tableModel, 0, 1, "SnoopServlet" );
        assertCellEquals( tableModel, 1, 1, "ViewFile" );        
        assertCellEquals( tableModel, 0, 2, "/foo/snoop" );
        assertCellEquals( tableModel, 1, 2, "" );        
    }
    
    public void testServletTableViaXMLDescription() throws Exception {
        SAXReader reader = new SAXReader();
        Document definition = reader.read( "xml/swing/tableForWeb.xml" );
        Document document = reader.read( "xml/web.xml" );
        
        XMLTableModel tableModel = new XMLTableModel( definition, document );
        
        // now lets test the values come out
        assertEquals( "correct row count", tableModel.getRowCount(), 2 );
        assertEquals( "correct column count", tableModel.getColumnCount(), 3 );
        
        assertColumnNameEquals( tableModel, 0, "Name" );
        assertColumnNameEquals( tableModel, 1, "Class" );
        assertColumnNameEquals( tableModel, 2, "Mapping" );
        
        assertCellEquals( tableModel, 0, 0, "snoop" );
        assertCellEquals( tableModel, 1, 0, "file" );
        assertCellEquals( tableModel, 0, 1, "SnoopServlet" );
        assertCellEquals( tableModel, 1, 1, "ViewFile" );        
        assertCellEquals( tableModel, 0, 2, "/foo/snoop" );
        assertCellEquals( tableModel, 1, 2, "" );        
    }
    
    protected void assertColumnNameEquals(TableModel tableModel, int columnIndex, String name) {
        assertEquals( "Column name correct for index: " + columnIndex, 
            name, tableModel.getColumnName( columnIndex ) 
        );
    }
    
    protected void assertCellEquals(TableModel tableModel, int rowIndex, int columnIndex, Object value) {
        assertEquals( "Cell value at row: " + rowIndex + " col: " + columnIndex, 
            value, tableModel.getValueAt( rowIndex, columnIndex ) 
        );
    }
}
