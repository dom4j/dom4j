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
import java.util.StringTokenizer;

import org.dom4j.Attribute;
import org.dom4j.CDATA;
import org.dom4j.CharacterData;
import org.dom4j.Comment;
import org.dom4j.ContentFactory;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Node;
import org.dom4j.Namespace;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Text;

/** <p><code>AbstractContentModel</code> is an abstract base class for 
  * tree implementors to use for implementation inheritence.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class AbstractContentModel implements ContentModel {

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

    public void setText(ContentFactory factory, String text) {
        clearContent();
        addText(factory, text);
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
    
    public Element getElement(String name) {
        return getElement(name, Namespace.NO_NAMESPACE);
    }
    
    public List getElements(String name) {
        return getElements(name, Namespace.NO_NAMESPACE);
    }
    
    public Iterator elementIterator(String name) {
        return elementIterator(name, Namespace.NO_NAMESPACE);
    }
        
    public CDATA addCDATA(ContentFactory factory, String cdata) {
        CDATA node = factory.createCDATA( cdata );
        addNode( node );
        return node;
    }
    
    public Comment addComment(ContentFactory factory, String comment) {
        Comment node = factory.createComment( comment );
        addNode( node );
        return node;
    }
    
    public Text addText(ContentFactory factory, String text) {
        Text node = factory.createText( text );
        addNode( node );
        return node;
    }
    
    public Element addElement(ContentFactory factory, String name) {
        Element node = factory.createElement( name );
        addNode( node );
        return node;
    }
    
    public Element addElement(ContentFactory factory, String name, String prefix, String uri) {
        Element node = factory.createElement( name, prefix, uri );
        addNode( node );
        return node;
    }
    
    public Element addElement(ContentFactory factory, String name, Namespace namespace) {
        Element node = factory.createElement( name, namespace );
        addNode( node );
        return node;
    }
    
    public Entity addEntity(ContentFactory factory, String name) {
        Entity node = factory.createEntity( name );
        addNode( node );
        return node;
    }
    
    public Entity addEntity(ContentFactory factory, String name, String text) {
        Entity node = factory.createEntity( name, text );
        addNode( node );
        return node;
    }
    
    public Namespace addAdditionalNamespace(ContentFactory factory, String prefix, String uri) {
        Namespace node = factory.createNamespace( prefix, uri );
        addNode( node );
        return node;
    }
    
    public ProcessingInstruction addProcessingInstruction(ContentFactory factory, String target, String data) {
        ProcessingInstruction node = factory.createProcessingInstruction( target, data );
        addNode( node );
        return node;
    }
    
    public ProcessingInstruction addProcessingInstruction(ContentFactory factory, String target, Map data) {
        ProcessingInstruction node = factory.createProcessingInstruction( target, data );
        addNode( node );
        return node;
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
