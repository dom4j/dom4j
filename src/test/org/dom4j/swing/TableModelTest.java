/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.swing;

import junit.textui.TestRunner;

import javax.swing.table.TableModel;

import org.dom4j.AbstractTestCase;
import org.dom4j.Document;

/**
 * Tests the Swing TableModel using a dom4j document.
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision$
 */
public class TableModelTest extends AbstractTestCase {
    public static void main(String[] args) {
        TestRunner.run(TableModelTest.class);
    }

    public void testServletTable() throws Exception {
        Document document = getDocument("/xml/web.xml");

        XMLTableDefinition tableDefinition = new XMLTableDefinition();
        tableDefinition.setRowExpression("/web-app/servlet");
        tableDefinition.addStringColumn("Name", "servlet-name");
        tableDefinition.addStringColumn("Class", "servlet-class");

        String mapping = "../servlet-mapping[servlet-name=$Name]/url-pattern";
        tableDefinition.addStringColumn("Mapping", mapping);

        XMLTableModel tableModel = new XMLTableModel(tableDefinition, document);

        // now lets test the values come out
        assertEquals("correct row count", tableModel.getRowCount(), 2);
        assertEquals("correct column count", tableModel.getColumnCount(), 3);

        assertColumnNameEquals(tableModel, 0, "Name");
        assertColumnNameEquals(tableModel, 1, "Class");
        assertColumnNameEquals(tableModel, 2, "Mapping");

        assertCellEquals(tableModel, 0, 0, "snoop");
        assertCellEquals(tableModel, 1, 0, "file");
        assertCellEquals(tableModel, 0, 1, "SnoopServlet");
        assertCellEquals(tableModel, 1, 1, "ViewFile");
        assertCellEquals(tableModel, 0, 2, "/foo/snoop");
        assertCellEquals(tableModel, 1, 2, "");
    }

    public void testServletTableViaXMLDescription() throws Exception {
        Document definition = getDocument("/xml/swing/tableForWeb.xml");
        Document document = getDocument("/xml/web.xml");

        XMLTableModel tableModel = new XMLTableModel(definition, document);

        // now lets test the values come out
        assertEquals("correct row count", tableModel.getRowCount(), 2);
        assertEquals("correct column count", tableModel.getColumnCount(), 3);

        assertColumnNameEquals(tableModel, 0, "Name");
        assertColumnNameEquals(tableModel, 1, "Class");
        assertColumnNameEquals(tableModel, 2, "Mapping");

        assertCellEquals(tableModel, 0, 0, "snoop");
        assertCellEquals(tableModel, 1, 0, "file");
        assertCellEquals(tableModel, 0, 1, "SnoopServlet");
        assertCellEquals(tableModel, 1, 1, "ViewFile");
        assertCellEquals(tableModel, 0, 2, "/foo/snoop");
        assertCellEquals(tableModel, 1, 2, "");
    }

    protected void assertColumnNameEquals(TableModel tableModel,
            int columnIndex, String name) {
        assertEquals("Column name correct for index: " + columnIndex, name,
                tableModel.getColumnName(columnIndex));
    }

    protected void assertCellEquals(TableModel tableModel, int rowIndex,
            int columnIndex, Object value) {
        assertEquals("Cell value at row: " + rowIndex + " col: " + columnIndex,
                value, tableModel.getValueAt(rowIndex, columnIndex));
    }
}

/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * 3. The name "DOM4J" must not be used to endorse or promote products derived
 * from this Software without prior written permission of MetaStuff, Ltd. For
 * written permission, please contact dom4j-info@metastuff.com.
 * 
 * 4. Products derived from this Software may not be called "DOM4J" nor may
 * "DOM4J" appear in their names without prior written permission of MetaStuff,
 * Ltd. DOM4J is a registered trademark of MetaStuff, Ltd.
 * 
 * 5. Due credit should be given to the DOM4J Project - http://www.dom4j.org
 * 
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL METASTUFF, LTD. OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 */
