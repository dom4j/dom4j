/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.swing;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.XPath;

/** <p><code>XMLTableDefinition</code> repro.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$ 
  */
public class XMLTableModel extends AbstractTableModel {

    
    /** Holds value of property definition. */
    private XMLTableDefinition definition;
    
    /** Holds value of property source. */
    private Object source;
    
    /** The rows evaluated from the row XPath expression */
    private List rows;
    
    /** Creates a TableModel from an XML table definition document 
     * and an XML source 
     */
    public XMLTableModel(Element tableDefinition, Object source) {
        this( XMLTableDefinition.load( tableDefinition ), source );
    }
    
    /** Creates a TableModel from an XML table definition document 
     * and an XML source 
     */
    public XMLTableModel(Document tableDefinition, Object source) {
        this( XMLTableDefinition.load( tableDefinition ), source );
    }
    
    public XMLTableModel(XMLTableDefinition definition, Object source) {
        this.definition = definition;
        this.source = source;
    }
    
    public Object getRowValue(int rowIndex) {
        return getRows().get(rowIndex);
    }
    
    public List getRows() {
        if ( rows == null ) {
            rows = definition.getRowXPath().selectNodes( source );
        }
        return rows;
    }
    
        
    // TableModel interface
    //-------------------------------------------------------------------------                
    public Class getColumnClass(int columnIndex) {
        return definition.getColumnClass(columnIndex);
    }
    
    public int getColumnCount() {
        return definition.getColumnCount();
    }
    
    public String getColumnName(int columnIndex) {
        XPath xpath = definition.getColumnNameXPath(columnIndex);
        if ( xpath != null ) {
            System.out.println("Evaluating column xpath: " + xpath + " value: " + xpath.valueOf(source) );
            return xpath.valueOf( source );
        }
        return definition.getColumnName(columnIndex);
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            Object row = getRowValue(rowIndex);
            return definition.getValueAt(row, columnIndex);
        }
        catch (Exception e) {
            handleException(e);
            return null;
        }
    }
    
    public int getRowCount() {
        return getRows().size();
    }
    
    // Properties
    //-------------------------------------------------------------------------                
    
    /** Getter for property definition.
     * @return Value of property definition.
     */
    public XMLTableDefinition getDefinition() {
        return definition;
    }
    
    /** Setter for property definition.
     * @param definition New value of property definition.
     */
    public void setDefinition(XMLTableDefinition definition) {
        this.definition = definition;
    }
    
    /** Getter for the XML source, which is usually a Node or List of nodes.
     * @return Value of property source.
     */
    public Object getSource() {
        return source;
    }
    
    /** Setter for the XML source, which is usually a Node or List of nodes.
     * @param source New value of property source.
     */
    public void setSource(Object source) {
        this.source = source;
        this.rows = null;
    }
    
    
    // Implementation methods
    //-------------------------------------------------------------------------                
    
    protected void handleException(Exception e) {
        // #### should use jakarta commons-logging
        System.out.println( "Caught: " + e );
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
