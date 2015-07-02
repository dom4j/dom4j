/*
 * Copyright 2001-2004 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id: SAXValidatorDemo.java,v 1.4 2005/01/29 14:53:14 maartenc Exp $
 */

package org.dom4j.samples.validate;

import org.dom4j.samples.AbstractDemo;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXValidator;
import org.dom4j.io.XMLWriter;
import org.dom4j.util.XMLErrorHandler;
import org.xml.sax.SAXException;

/**
 * A sample program demonstrating the use of validation using SAXValidator
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision: 1.4 $
 */
public class SAXValidatorDemo extends AbstractDemo {

    public static void main(String[] args) {
        run(new SAXValidatorDemo(), args);
    }

    public SAXValidatorDemo() {
    }

    public void run(String[] args) throws Exception {
        if (args.length < 1) {
            printUsage("<xmlFileNameOrURL> <onParse>");
        }

        String fileName = args[0];
        boolean validateOnParse = false;
        if (args.length > 1) {
            String boolText = args[1];
            if (boolText.equalsIgnoreCase("true")) {
                validateOnParse = true;
            }
        }

        validate(fileName, validateOnParse);
    }

    protected void validate(String url, boolean validateOnParse)
            throws Exception {
        println("Parsing: " + url + " with validation mode: " + validateOnParse);

        XMLErrorHandler errorHandler = new XMLErrorHandler();

        if (validateOnParse) {
            // validate as we parse
            SAXReader reader = new SAXReader(true);
            reader.setErrorHandler(errorHandler);

            try {
                Document document = reader.read(url);
                println("Document: " + url + " is valid!");
            } catch (DocumentException e) {
                println("Document: " + url + " is not valid");
                println("Exception: " + e);
            }
        } else {
            // parse without validating, then do that later
            SAXReader reader = new SAXReader();
            Document document = reader.read(url);

            println("Document URI: " + document.getName());

            // now lets set a doc type if one isn't set
            DocumentType docType = document.getDocType();
            if (docType == null) {
                println("Adding an NITF doc type");
                document.addDocType("nitf", null, "nitf.dtd");
            }

            // now lets validate
            try {
                SAXValidator validator = new SAXValidator();
                validator.setErrorHandler(errorHandler);
                validator.validate(document);

                println("Document: " + url + " is valid!");
            } catch (SAXException e) {
                println("Document: " + url + " is not valid");
                println("Exception: " + e);
            }
        }

        // now lets output any errors as XML
        Element errors = errorHandler.getErrors();
        if (errors.hasContent()) {
            XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint());
            writer.write(errors);
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
 * $Id: SAXValidatorDemo.java,v 1.4 2005/01/29 14:53:14 maartenc Exp $
 */
