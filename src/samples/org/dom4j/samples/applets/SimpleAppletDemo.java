/*
 * Copyright 2001-2004 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 *
 * $Id: SimpleAppletDemo.java,v 1.4 2005/01/29 14:52:57 maartenc Exp $
 */

package org.dom4j.samples.applets;

import java.applet.Applet;
import java.awt.Graphics;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * This class demonstrate the use of dom4j in Applets. Note that applets are not
 * allowed to read files from client disk, if unsigned.
 * 
 * @author <a href="toby-wan-kenobi@gmx.de">Tobias Rademacher </a>
 * @version $Revision: 1.4 $
 */
public class SimpleAppletDemo extends Applet {

    private static String DEMO_XML = "<?xml version='1.0' encoding='ISO-8859-1'?>\n"
            + "<web-app>\n"
            + "<servlet>\n"
            + "<servlet-name>snoop</servlet-name>\n"
            + "<servlet-class>SnoopServlet</servlet-class>\n"
            + "</servlet>\n"
            + "</web-app>";

    private Document demoDocument;

    private StringBuffer buffer;

    /**
     * Called after init. Demonstrates the simplicity of parsing in applets.
     */
    public void start() {
        try {
            demoDocument = DocumentHelper.parseText(DEMO_XML);
            new XMLWriter(OutputFormat.createPrettyPrint()).write(demoDocument);
        } catch (DocumentException documentEx) {
            documentEx.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        demoXPath();
        repaint();
    }

    /**
     * Demonstrates the use of XPath in Applets
     */
    private void demoXPath() {
        this.buffer = new StringBuffer("The name of the servlet is :");
        this.buffer.append(demoDocument
                .valueOf("/web-app/servlet[1]/servlet-name"));
        this.buffer.append(" and the class is ");
        this.buffer.append(demoDocument
                .valueOf("/web-app/servlet[1]/servlet-class"));
    }

    /**
     * Invoked by repaint() and paints a xpath
     */
    public void paint(Graphics g) {
        g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
        g.drawString(this.buffer.toString(), 5, 15);
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
 * $Id: SimpleAppletDemo.java,v 1.4 2005/01/29 14:52:57 maartenc Exp $
 */

