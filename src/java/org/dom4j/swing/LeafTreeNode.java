/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.swing;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import org.dom4j.Node;

/** <p><code>LeafTreeNode</code> implements the Swing TreeNode interface
  * to bind a leaf XML nodes to a Swing TreeModel.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a> (james.strachan@metastuff.com)
  * @author Jakob Jenkov
  * @version $Revision$ 
  */
public class LeafTreeNode implements TreeNode {

    protected static final Enumeration EMPTY_ENUMERATION = new Enumeration() {
        public boolean hasMoreElements() {
            return false;
        }
        public Object nextElement() {
            return null;
        }
    };

    /** The parent node of this TreeNode */
    private TreeNode parent;
    
    /** The dom4j Node which contains the */
    protected Node xmlNode;

    
    public LeafTreeNode() {
    }
    
    public LeafTreeNode(Node xmlNode) {
        this.xmlNode = xmlNode;
    }
    
    public LeafTreeNode(TreeNode parent, Node xmlNode) {
        this.parent = parent;
        this.xmlNode = xmlNode;
    }
    

    // TreeNode methods
    //-------------------------------------------------------------------------                
    public Enumeration children() {
        return EMPTY_ENUMERATION;
    }
    
    public boolean getAllowsChildren() {
        return false;
    }
    
    public TreeNode getChildAt(int childIndex) {
        return null;
    }
    
    public int getChildCount() {
        return 0;
    }
    
    public int getIndex(TreeNode node) {
        return -1;
    }
    
    public TreeNode getParent() {
        return parent;
    }
    
    public boolean isLeaf() {
        return true;
    }
    
    public String toString() {
        // should maybe do things differently based on content?
        String text = xmlNode.getText();
        return (text != null) ? text.trim() : "";
    }
    
    // Properties
    //-------------------------------------------------------------------------                
    
    /** Sets the parent of this node but doesn't change the parents children */
    public void setParent(LeafTreeNode parent) {
        this.parent = parent;
    }
    
    public Node getXmlNode() {
        return xmlNode;
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
