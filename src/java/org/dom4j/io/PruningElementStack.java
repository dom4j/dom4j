/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.io;

import org.dom4j.Element;
import org.dom4j.ElementHandler;

/**
 * <p>
 * <code>PruningElementStack</code> is a stack of {@link Element}instances
 * which will prune the tree when a path expression is reached. This is useful
 * for parsing very large documents where children of the root element can be
 * processed individually rather than keeping them all in memory at the same
 * time.
 * </p>
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision$
 */
class PruningElementStack extends ElementStack {
    /** ElementHandler to call when pruning occurs */
    private ElementHandler elementHandler;

    /**
     * the element name path which denotes the node to remove from its parent
     * when it is complete (i.e. when it is popped from the stack). The first
     * entry in the path will be a child of the root node
     */
    private String[] path;

    /**
     * The level at which a path match can occur. We match when we have popped
     * the selected node so the and the lastElementIndex points to its parent so
     * this value should be path.length - 2
     */
    private int matchingElementIndex;

    public PruningElementStack(String[] path, ElementHandler elementHandler) {
        this.path = path;
        this.elementHandler = elementHandler;
        checkPath();
    }

    public PruningElementStack(String[] path, ElementHandler elementHandler,
            int defaultCapacity) {
        super(defaultCapacity);
        this.path = path;
        this.elementHandler = elementHandler;
        checkPath();
    }

    public Element popElement() {
        Element answer = super.popElement();

        if ((lastElementIndex == matchingElementIndex)
                && (lastElementIndex >= 0)) {
            // we are popping the correct level in the tree
            // lets check if the path fits
            //
            // NOTE: this is an inefficient way of doing it - we could
            // maintain a history of which parts matched?
            if (validElement(answer, lastElementIndex + 1)) {
                Element parent = null;

                for (int i = 0; i <= lastElementIndex; i++) {
                    parent = stack[i];

                    if (!validElement(parent, i)) {
                        parent = null;

                        break;
                    }
                }

                if (parent != null) {
                    pathMatches(parent, answer);
                }
            }
        }

        return answer;
    }

    protected void pathMatches(Element parent, Element selectedNode) {
        elementHandler.onEnd(this);
        parent.remove(selectedNode);
    }

    protected boolean validElement(Element element, int index) {
        String requiredName = path[index];
        String name = element.getName();

        if (requiredName == name) {
            return true;
        }

        if ((requiredName != null) && (name != null)) {
            return requiredName.equals(name);
        }

        return false;
    }

    private void checkPath() {
        if (path.length < 2) {
            throw new RuntimeException("Invalid path of length: " + path.length
                    + " it must be greater than 2");
        }

        matchingElementIndex = path.length - 2;
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
