/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.tree;

import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.dom4j.Branch;
import org.dom4j.CDATA;
import org.dom4j.CharacterData;
import org.dom4j.Comment;
import org.dom4j.ContentFactory;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.IllegalAddNodeException;
import org.dom4j.Node;
import org.dom4j.Namespace;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Text;
import org.dom4j.io.XMLWriter;

/** <p><code>AbstractBranch</code> is an abstract base class for 
  * tree implementors to use for implementation inheritence.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class AbstractBranch extends AbstractNode implements Branch {

    /** The XML writer used by default */
    protected static final XMLWriter writer = new XMLWriter( "  ", false );

    /** The <code>ContentFactory</code> instance used by default */
    private static final ContentFactory CONTENT_FACTORY = ContentFactory.getInstance();

    
    public AbstractBranch() { 
    }

    
    public String getText() {
        List content = getContent();
        if (content != null) {
            int size = content.size();
            if (size >= 1) {
                Object first = content.get(0);
                if (first != null) {
                    // If we hold only a String, return it directly
                    if (size == 1) {
                        if ( first instanceof String) {
                            return (String) first;
                        }
                        else if ( first instanceof CharacterData ) {
                            CharacterData text = (CharacterData) first;
                            return text.getText();
                        }
                        else {
                            return "";
                        }
                    }
                    
                    // Else build String up
                    StringBuffer textContent = new StringBuffer();
                    boolean hasText = false;
                    for ( Iterator i = content.iterator(); i.hasNext(); ) {
                        Object obj = i.next();
                        if (obj instanceof String) {
                            textContent.append((String)obj);
                        } else if (obj instanceof CharacterData) {
                            textContent.append(((CharacterData)obj).getText());
                        }
                    }
                    return textContent.toString();
                }
            }
        }
        return "";
    }

    public String getTextTrim() {
        String text = getText();

        StringBuffer textContent = new StringBuffer();
        StringTokenizer tokenizer = new StringTokenizer(text);
        while (tokenizer.hasMoreTokens()) {
            String str = tokenizer.nextToken();
            textContent.append(str);
            if (tokenizer.hasMoreTokens()) {
                textContent.append(" ");  // separator
            }
        }

        return textContent.toString();
    }

    public boolean hasMixedContent() {
        List content = getContent();
        if (content == null || content.isEmpty() || content.size() < 2) {
            return false;
        }

        Class prevClass = null;
        for ( Iterator iter = content.iterator(); iter.hasNext(); ) {
            Object object = iter.next();
            Class newClass = object.getClass();
            if (newClass != prevClass) {
               if (prevClass != null) {
                  return true;
               }
               prevClass = newClass;
            }
        }
        return false;
    }
    
    public void setProcessingInstructions(List listOfPIs) {
        for ( Iterator iter = listOfPIs.iterator(); iter.hasNext(); ) {
            ProcessingInstruction pi = (ProcessingInstruction) iter.next();
            addNode(pi);
        }
    }
    
    public Comment addComment(String comment) {
        Comment node = getContentFactory().createComment( comment );
        add( node );
        return node;
    }
    
    public Element addElement(String name) {
        Element node = getContentFactory().createElement( name );
        add( node );
        return node;
    }
    
    public Element addElement(String name, String prefix, String uri) {
        Element node = getContentFactory().createElement( name, prefix, uri );
        add( node );
        return node;
    }
    
    public Element addElement(String name, Namespace namespace) {
        Element node = getContentFactory().createElement( name, namespace );
        add( node );
        return node;
    }
    
    public ProcessingInstruction addProcessingInstruction(String target, String data) {
        ProcessingInstruction node = getContentFactory().createProcessingInstruction( target, data );
        add( node );
        return node;
    }
    
    public ProcessingInstruction addProcessingInstruction(String target, Map data) {
        ProcessingInstruction node = getContentFactory().createProcessingInstruction( target, data );
        add( node );
        return node;
    }
    
    

    // typesafe versions using node classes
    public void add(Comment comment) {
        addNode(comment);
    }
    
    public void add(Element element) {
        addNode(element);
    }
    
    public void add(ProcessingInstruction pi) {
        addNode(pi);
    }
    
    public boolean remove(Comment comment) {
        return removeNode(comment);
    }
    
    public boolean remove(Element element) {
        return removeNode(element);
    }
    
    public boolean remove(ProcessingInstruction pi) {
        return removeNode(pi);
    }
    
    
    
    
    // Implementation methods
    
    /** Allows derived classes to override the factory behaviour */
    protected ContentFactory getContentFactory() {
        return CONTENT_FACTORY;
    }

    protected abstract void addNode(Node node);
    
    protected abstract boolean removeNode(Node node);
    
    
    /** Called when a new child node has been added to me
      * to allow any parent relationships to be created or
      * events to be fired.
      */
    protected abstract void childAdded(Node node);
    
    /** Called when a child node has been removed 
      * to allow any parent relationships to be deleted or
      * events to be fired.
      */
    protected abstract void childRemoved(Node node);
    
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
