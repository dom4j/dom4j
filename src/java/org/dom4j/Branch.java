/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** <p><code>Branch</code> interface defines common behaviour 
  * for XML elements and XML documents so that they can be treated in a 
  * polymorphic manner.</p>
  *
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public interface Branch extends Node {

    /** Returns the <code>Node</code> at the specified index position.
      *
      * @param index the index of the node to return.
      * @return the <code>Node</code> at the specified position.
      * 
      * @throws IndexOutOfBoundsException if the index is out of range (index
      * 		  &lt; 0 || index &gt;= {@link #getNodeCount}).
      */    
    public Node getNode(int index);
    
    /** Returns the number of <code>Node</code> instances that this branch 
      * contains.
      *
      * @return the number of nodes this branch contains
      */    
    public int getNodeCount();
    
    /** Returns the content nodes of this branch as a <code>List</code>. 
      * The <code>List</code> is backed by the <code>Branch</code> so that
      * changes to the list are reflected in the branch and vice-versa
      *
      * @return the nodes that this branch contains as a <code>List</code>
      */    
    public List getContent();    

    /** Returns an iterator through the content nodes of this branch
      * 
      * @return an iterator through the content nodes of this branch
      */
    public Iterator nodeIterator();
    
    /** Sets the contents of this branch as a <code>List</code> of 
      * <code>Node</code> instances.
      *
      * @param content is the list of nodes to use as the content for this 
      *   branch.
      */    
    public void setContent(List content);    
    
    /** Clears the content for this branch, removing any <code>Node</code> 
      * instances this branch may contain.
      */    
    public void clearContent();
    
    public List getProcessingInstructions();
    public List getProcessingInstructions(String target);
    public ProcessingInstruction getProcessingInstruction(String target);    
    public void setProcessingInstructions(List listOfPIs);
    
    
    /** Adds a new <code>Comment</code> node with the given text to this branch
      * and returns a reference to the new node.
      *
      * @param comment is the text for the <code>Comment</code> node.
      * @return the newly added <code>Comment</code> node.
      */    
    public Comment addComment(String comment);
    
    /** Adds a new <code>Element</code> node with the given name to this branch
      * and returns a reference to the new node.
      *
      * @param name is the name for the <code>Element</code> node.
      * @return the newly added <code>Element</code> node.
      */    
    public Element addElement(String name);
    
    /** Adds a new <code>Element</code> node with the given {@link QName} 
      * to this branch and returns a reference to the new node.
      *
      * @param qname is the qualified name for the <code>Element</code> node.
      * @return the newly added <code>Element</code> node.
      */    
    public Element addElement(QName qname);
    
    /** Adds a new <code>Element</code> node with the given name and namespace
      * prefix and URI to this branch and returns a reference to the new node.
      *
      * @param name is the name for the <code>Element</code> node.
      * @param prefix is the namespace prefix for the 
      *   <code>Element</code> node.
      * @param uri is the namespace URI for the 
      *   <code>Element</code> node.
      * @return the newly added <code>Element</code> node.
      */    
    public Element addElement(String name, String prefix, String uri);
    
    /** Adds a new <code>Element</code> node with the given name and namespace
      * to this branch and returns a reference to the new node.
      *
      * @param name is the name for the <code>Element</code> node.
      * @param namespace is the <code>Namespace</code> for the 
      *   <code>Element</code> node.
      * @return the newly added <code>Element</code> node.
      */    
    public Element addElement(String name, Namespace namespace);
    
    public ProcessingInstruction addProcessingInstruction(String target, String data);
    
    public ProcessingInstruction addProcessingInstruction(String target, Map data);

    public boolean removeProcessingInstruction(String target);

    
    
    /** Adds the given <code>Comment</code> to this element.
      * If the given node already has a parent defined then an
      * <code>InvalidAddNodeException</code> will be thrown.
      *
      * @param comment is the comment to be added
      */
    public void add(Comment comment);
    
    /** Adds the given <code>Element</code> to this element.
      * If the given node already has a parent defined then an
      * <code>InvalidAddNodeException</code> will be thrown.
      *
      * @param element is the element to be added
      */
    public void add(Element element);
    
    /** Adds the given <code>ProcessingInstruction</code> to this element.
      * If the given node already has a parent defined then an
      * <code>InvalidAddNodeException</code> will be thrown.
      *
      * @param pi is the processing instruction to be added
      */
    public void add(ProcessingInstruction pi);
        
    /** Removes the given <code>Comment</code> from this element.
      *
      * @param comment is the comment to be removed
      * @return true if the comment was removed
      */
    public boolean remove(Comment comment);
    
    /** Removes the given <code>Element</code> from this element.
      *
      * @param element is the element to be removed
      * @return true if the element was removed
      */
    public boolean remove(Element element);
    
    /** Removes the given <code>ProcessingInstruction</code> from this element.
      *
      * @param pi is the processing instruction to be removed
      * @return true if the processing instruction was removed
      */
    public boolean remove(ProcessingInstruction pi);
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
