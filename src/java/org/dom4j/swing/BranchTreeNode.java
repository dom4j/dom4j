/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.swing;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import org.dom4j.Branch;
import org.dom4j.CharacterData;
import org.dom4j.Node;

/**
 * <p>
 * <code>BranchTreeNode</code> implements the Swing TreeNode interface to bind
 * dom4j XML Branch nodes (i.e. Document and Element nodes) to a Swing
 * TreeModel.
 * </p>
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @author Jakob Jenkov
 * @version $Revision$
 */
public class BranchTreeNode extends LeafTreeNode {
    /** Stores the child tree nodes */
    protected List children;

    public BranchTreeNode() {
    }

    public BranchTreeNode(Branch xmlNode) {
        super(xmlNode);
    }

    public BranchTreeNode(TreeNode parent, Branch xmlNode) {
        super(parent, xmlNode);
    }

    // TreeNode methods
    // -------------------------------------------------------------------------
    public Enumeration children() {
        return new Enumeration() {
            private int index = -1;

            public boolean hasMoreElements() {
                return (index + 1) < getChildCount();
            }

            public Object nextElement() {
                return getChildAt(++index);
            }
        };
    }

    public boolean getAllowsChildren() {
        return true;
    }

    public TreeNode getChildAt(int childIndex) {
        return (TreeNode) getChildList().get(childIndex);
    }

    public int getChildCount() {
        return getChildList().size();
    }

    public int getIndex(TreeNode node) {
        return getChildList().indexOf(node);
    }

    public boolean isLeaf() {
        return getXmlBranch().nodeCount() <= 0;
    }

    public String toString() {
        return xmlNode.getName();
    }

    // Implementation methods
    // -------------------------------------------------------------------------

    /**
     * Uses Lazy Initialization pattern to create a List of children
     * 
     * @return DOCUMENT ME!
     */
    protected List getChildList() {
        // for now lets just create the children once, the first time they
        // are asked for.
        // XXXX - we may wish to detect inconsistencies here....
        if (children == null) {
            children = createChildList();
        }

        return children;
    }

    /**
     * Factory method to create List of children TreeNodes
     * 
     * @return DOCUMENT ME!
     */
    protected List createChildList() {
        // add attributes and content as children?
        Branch branch = getXmlBranch();
        int size = branch.nodeCount();
        List childList = new ArrayList(size);

        for (int i = 0; i < size; i++) {
            Node node = branch.node(i);

            // ignore whitespace text nodes
            if (node instanceof CharacterData) {
                String text = node.getText();

                if (text == null) {
                    continue;
                }

                text = text.trim();

                if (text.length() <= 0) {
                    continue;
                }
            }

            childList.add(createChildTreeNode(node));
        }

        return childList;
    }

    /**
     * Factory method to create child tree nodes for a given XML node type
     * 
     * @param xmlNode
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    protected TreeNode createChildTreeNode(Node xmlNode) {
        if (xmlNode instanceof Branch) {
            return new BranchTreeNode(this, (Branch) xmlNode);
        } else {
            return new LeafTreeNode(this, xmlNode);
        }
    }

    protected Branch getXmlBranch() {
        return (Branch) xmlNode;
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
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 */
