/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.swing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.jaxen.VariableContext;

/** <p><code>XMLTableDefinition</code> represents a
  * table definition based on XPath expression evaluated
  * on an XML document.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$ 
  */
public class XMLTableDefinition implements Serializable, VariableContext {

    /** Holds value of property rowXPath. */
    private XPath rowXPath;
    
    /** The columns to display in this table */
    private List columns = new ArrayList();
    
    /** integer index array cache */
    private XMLTableColumnDefinition[] columnArray;
    /** name index cache */
    private Map columnNameIndex;
    
    /** for cross-row variables */
    private VariableContext variableContext;
    
    /** stores the current row value for the variableContext */
    private Object rowValue;
    
    public XMLTableDefinition() {
    }

    /** Loads an XML table definition from an XML definition document */
    public static XMLTableDefinition load(Document definition) {
        return load( definition.getRootElement() );
    }
    
    /** Loads an XML table definition from an XML definition document */
    public static XMLTableDefinition load(Element definition) {
        XMLTableDefinition answer = new XMLTableDefinition();
        answer.setRowExpression( definition.attributeValue( "select" ) );
        for (Iterator iter = definition.elementIterator( "column" ); iter.hasNext(); ) {
            Element element = (Element) iter.next();
            String expression = element.attributeValue( "select" );
            String name = element.getText();
            String typeName = element.attributeValue( "type", "string" ); 
            String columnNameXPath = element.attributeValue( "columnNameXPath" );
            int type = XMLTableColumnDefinition.parseType( typeName );
            if ( columnNameXPath != null ) {
                answer.addColumnWithXPathName( columnNameXPath, expression, type );
            }
            else {
                answer.addColumn( name, expression, type );
            }
        }
        return answer;
    }
    
    
    public Class getColumnClass(int columnIndex) {
        return getColumn(columnIndex).getColumnClass();
    }
    
    public int getColumnCount() {
        return columns.size();
    }
    
    /**
     * @return the static column name. This is used if there is no columnNameXPath
     */
    public String getColumnName(int columnIndex) {
        return getColumn(columnIndex).getName();
    }
    
    /**
     * @return the XPath expression used to evaluate the value of cells in this column
     */ 
    public XPath getColumnXPath(int columnIndex) {
        return getColumn(columnIndex).getXPath();
    }
     
    /**
     * @return the XPath expresssion used to create the column name, if there is one
     * or null if there is no XPath expression to name the column.
     */
    public XPath getColumnNameXPath(int columnIndex) {
        return getColumn(columnIndex).getColumnNameXPath();
    }
     
    public synchronized Object getValueAt(Object row, int columnIndex) {
        XMLTableColumnDefinition column = getColumn(columnIndex);
        Object answer = null;
        synchronized (this) {
            this.rowValue = row;
            answer = column.getValue(row);
            this.rowValue = null;
        }
        return answer;
    }
     

    public void addColumn(String name, String expression) {
        addColumn( name, expression, XMLTableColumnDefinition.OBJECT_TYPE );
    }
    
    public void addColumn(String name, String expression, int type) {
        XPath xpath = createColumnXPath( expression );
        addColumn( new XMLTableColumnDefinition( name, xpath, type ) );
    }
    
    public void addColumnWithXPathName(String columnNameXPathExpression, String expression, int type) {
        XPath columnNameXPath = createColumnXPath( columnNameXPathExpression );
        XPath xpath = createColumnXPath( expression );
        addColumn( new XMLTableColumnDefinition( columnNameXPath, xpath, type ) );
    }
    
    public void addStringColumn(String name, String expression) {
        addColumn( name, expression, XMLTableColumnDefinition.STRING_TYPE );
    }
    
    public void addNumberColumn(String name, String expression) {
        addColumn( name, expression, XMLTableColumnDefinition.NUMBER_TYPE );
    }
    
    public void addColumn(XMLTableColumnDefinition column) {
        clearCaches();
        columns.add( column );
    }
    
    public void removeColumn(XMLTableColumnDefinition column) {
        clearCaches();
        columns.remove( column );
    }
    
    public void clear() {
        clearCaches();
        columns.clear();
    }
    
    public XMLTableColumnDefinition getColumn(int index) {
        if ( columnArray == null ) {
            columnArray = new XMLTableColumnDefinition[ columns.size() ];
            columns.toArray( columnArray );
        }
        return columnArray[index];
    }
    
    public XMLTableColumnDefinition getColumn(String columnName) {
        if ( columnNameIndex == null ) {
            columnNameIndex = new HashMap();
            for (Iterator iter = columns.iterator(); iter.hasNext(); ) {
                XMLTableColumnDefinition column = (XMLTableColumnDefinition) iter.next();
                columnNameIndex.put( column.getName(), column );
            }
        }
        return (XMLTableColumnDefinition) columnNameIndex.get(columnName);
    }
    
    /** Getter for property rowXPath.
     * @return Value of property rowXPath.
     */
    public XPath getRowXPath() {
        return rowXPath;
    }
    
    /** Setter for property rowXPath.
     * @param rowXPath New value of property rowXPath.
     */
    public void setRowXPath(XPath rowXPath) {
        this.rowXPath = rowXPath;
    }
    
    public void setRowExpression(String xpath) {
        setRowXPath( createXPath( xpath ) );
    }
    
    
    // VariableContext interface
    //-------------------------------------------------------------------------                
    public Object getVariableValue(
        String namespaceURI,
        String prefix,
        String localName
    ) {
        XMLTableColumnDefinition column = getColumn(localName);
        if ( column != null ) {
            return column.getValue( rowValue );
        }
        return null;
    }
    
    // Implementation methods
    //-------------------------------------------------------------------------                
    protected XPath createXPath(String expression) {
        return DocumentHelper.createXPath(expression);
    }
    
    protected XPath createColumnXPath(String expression) {
        XPath xpath = createXPath( expression );
        // associate my variable context
        xpath.setVariableContext( this );
        return xpath;
    }

    
    protected void clearCaches() {
        columnArray = null;
        columnNameIndex = null;
    }
    
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
