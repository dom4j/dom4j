/*
 * Copyright 2001-2004 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id: ParseLoop.java,v 1.4 2005/01/29 14:53:13 maartenc Exp $
 */

package org.dom4j.samples.performance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

/**
 * A simple parsing program that loops which makes it easier to profile
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision: 1.4 $
 */
public class ParseLoop {

    private static int bufferSize = 128 * 1024;

    public static void main(String[] args) throws Exception {
        if (args.length <= 0) {
            System.out.println("arguments: <XML file> [<loopCount>]");
            return;
        }
        String xmlFile = args[0];
        int loops = 40;
        if (args.length > 1) {
            loops = Integer.parseInt(args[1]);
        }

        StringBuffer buffer = new StringBuffer(64 * 1024);
        BufferedReader reader = new BufferedReader(new FileReader(xmlFile));
        while (true) {
            String text = reader.readLine();
            if (text == null) {
                break;
            }
            buffer.append(text);
            buffer.append("\n");
        }
        long start = System.currentTimeMillis();

        parse(buffer.toString(), loops);

        long elapsed = System.currentTimeMillis() - start;

        System.out.println("Parsed: " + xmlFile + " " + loops + " times in: "
                + elapsed + " (ms)");
    }

    protected static void parse(String text, int loops) throws Exception {
        SAXReader xmlReader = new SAXReader();
        for (int i = 0; i < loops; i++) {
            Document document = xmlReader.read(new StringReader(text));
        }
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
 * $Id: ParseLoop.java,v 1.4 2005/01/29 14:53:13 maartenc Exp $
 */
