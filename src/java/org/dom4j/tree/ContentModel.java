/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.tree;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.CDATA;
import org.dom4j.Comment;
import org.dom4j.ContentFactory;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Node;
import org.dom4j.Namespace;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Text;

/** <p><code>ContentModel</code> represents an XML content model for an XML element.
  * This interface is used to decompose an element implementations into smaller
  * resusable units.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public interface ContentModel {

    public Namespace getNamespaceForPrefix(String prefix);
    
    public Namespace getNamespaceForURI(String uri);
    
    /** <p>Returns any additional namespaces declarations for this element 
      * other than namespace returned via the {@link Element#getNamespace()} method. 
      * If no additional namespace declarations are present for this
      * element then {@link Collections.EMPTY_LIST} will be returned.
      *
      * @return a list of any additional namespace declarations.
      */
    public List getAdditionalNamespaces(String defaultNamespaceURI);
    
    // Text API
    
    /** <p>Returns the text of this node.</p>
      *
      * @return the text for this node.
      */
    public String getText();
    


    public String getTextTrim();

    
    // Processing instruction API
    
    public List getProcessingInstructions();
    public List getProcessingInstructions(String target);
    public ProcessingInstruction getProcessingInstruction(String target);    
    public void setProcessingInstructions(List listOfPIs);
    

    
    
    
    /** <p>Returns true if this <code>Element</code> has mixed content.
      * Mixed content means that an element contains both textual data and
      * child elements.
      *
      * @return true if this element contains mixed content.
      */
    public boolean hasMixedContent();

    // this will allow String objects to be used to represent text
    // as the user doesn't mind too much about Nodes
    public List getContent();    
    public void setContent(List content);    
    public void clearContent();
    
    
    // node navigation API - return content as nodes
    // such as Text etc.
    public Node getNode(int index);
    public int getNodeCount();
    public Iterator nodeIterator();
    

    // return the child elements
    public Element getElementByID(String elementID);
    public Element getElement(String name);
    public Element getElement(String name, Namespace namespace);
    
    public List getElements();
    public List getElements(String name);
    public List getElements(String name, Namespace namespace);
    
    public Iterator elementIterator();
    public Iterator elementIterator(String name);
    public Iterator elementIterator(String name, Namespace namespace);
    
    // add API which avoids explicit use of node classes

    public CDATA addCDATA(ContentFactory factory, String cdata);
    public Comment addComment(ContentFactory factory, String comment);
    public Text addText(ContentFactory factory, String text);
    
    public Element addElement(ContentFactory factory, String name);
    public Element addElement(ContentFactory factory, String name, String prefix, String uri);
    public Element addElement(ContentFactory factory, String name, Namespace namespace);
    
    public Entity addEntity(ContentFactory factory, String name);
    public Entity addEntity(ContentFactory factory, String name, String text);
    public Namespace addAdditionalNamespace(ContentFactory factory, String prefix, String uri);
    public ProcessingInstruction addProcessingInstruction(ContentFactory factory, String target, String data);
    public ProcessingInstruction addProcessingInstruction(ContentFactory factory, String target, Map data);

    public void setText(ContentFactory factory, String text);
    
    public boolean removeProcessingInstruction(String target);
    
    public void addNode(Node node);
    public boolean removeNode(Node node);
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
