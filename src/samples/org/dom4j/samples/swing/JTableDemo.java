/*
 * Copyright 2001-2004 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id: JTableDemo.java,v 1.4 2005/01/29 14:53:14 maartenc Exp $
 */

package org.dom4j.samples.swing;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.dom4j.swing.XMLTableDefinition;
import org.dom4j.swing.XMLTableModel;

/**
 * A sample program to build a JTable GUI from a dom4j Document
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision: 1.4 $
 */
public class JTableDemo {

    public static void main(String[] args) throws Exception {
        JTableDemo sample = new JTableDemo();
        sample.run(args);
    }

    public void run(String[] args) throws Exception {
        if (args.length <= 0) {
            System.out
                    .println("This program displays a web.xml document in a Swing JTable");
            System.out.println("Usage: <webXmlFileName>");
            return;
        }

        // parse document
        SAXReader reader = new SAXReader();
        Document document = reader.read(args[0]);

        // build table model
        XMLTableDefinition definition = new XMLTableDefinition();
        definition.setRowExpression("/web-app/servlet");
        definition.addStringColumn("Name", "servlet-name");
        definition.addStringColumn("Class", "servlet-class");
        definition.addStringColumn("Mapping",
                "../servlet-mapping[servlet-name=$Name]");

        XMLTableModel model = new XMLTableModel(definition, document);

        // make the widgets
        JTable table = new JTable(model);

        JFrame frame = new JFrame("JTableDemo: " + document.getName());
        frame.setSize(300, 300);
        frame.setLocation(100, 100);
        frame.getContentPane().add(new JScrollPane(table));
        frame.validate();
        frame.setVisible(true);
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
 * Copyright 2001-2004 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * $Id: JTableDemo.java,v 1.4 2005/01/29 14:53:14 maartenc Exp $
 */
