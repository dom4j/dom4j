/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.tree;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.IllegalAddNodeException;
import org.dom4j.XPathEngine;
import org.dom4j.XPathHelper;

/** <p><code>DefaultDocument</code> is the default DOM4J default implementation
  * of an XML document.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DefaultDocument extends AbstractDocument {

    /** The name of the document */
    private String name;

    /** The <code>ContentModel</code> for this elemenet */
    private ContentModel contentModel;
    
    /** The root element of this document */
    private Element rootElement;
    
    /** The document type for this document */
    private DocumentType docType;
    
    /** The engine used to evaluate XPath expressions for this document */
    private XPathEngine xpathEngine;
    
    
    public DefaultDocument() { 
    }

    public DefaultDocument(String name) { 
        this.name = name;
    }

    public DefaultDocument(Element rootElement) { 
        this.rootElement = rootElement;
    }

    public DefaultDocument(Element rootElement, DocumentType docType) {
        this.rootElement = rootElement;
        this.docType = docType;
    }

    public DefaultDocument(String name, Element rootElement, DocumentType docType) {
        this.name = name;
        this.rootElement = rootElement;
        this.docType = docType;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    
    public Element getRootElement() {
        return rootElement;
    }
    
    public DocumentType getDocType() {
        return docType;
    }
    
    public void setDocType(DocumentType docType) {
        this.docType = docType;
    }
    
    public void setDocType(String name, String publicId, String systemId) {
        setDocType( createDocType( name, publicId, systemId ) );
    }
    
    public DocumentType createDocType(String name, String publicId, String systemId) {
        return new DefaultDocumentType( name, publicId, systemId );
    }
    
    public XPathEngine getXPathEngine() {
        if ( xpathEngine == null ) {
            xpathEngine = XPathHelper.getInstance();
        }
        return xpathEngine;
    }
    
    public void setXPathEngine(XPathEngine xpathEngine) {
        this.xpathEngine = xpathEngine;
    }
    
    
       
    /** Allows derived classes to override the content model */
    protected ContentModel getContentModel() {
        if ( contentModel == null ) {
            contentModel = createContentModel();
        }
        return contentModel;
    }
    
    /** Allow derived classes to set the <code>ContentModel</code>
      */
    protected void setContentModel(ContentModel contentModel) {
        this.contentModel = contentModel;
    }


    /** A Factory Method pattern which lazily creates 
      * a ContentModel implementation 
      */
    protected ContentModel createContentModel() {
        return new DefaultContentModel();
    }
    
    protected void rootElementAdded(Element element) {
        this.rootElement = element;
        element.setDocument(this);
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
