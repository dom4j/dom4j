/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.io;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;

/**
 * This {@link org.dom4j.ElementHandler}is used to trigger {@link
 * ElementModifier} objects in order to modify (parts of) the Document on the
 * fly.
 * 
 * <p>
 * When an element is completely parsed, a copy is handed to the associated (if
 * any) {@link ElementModifier}that on his turn returns the modified element
 * that has to come in the tree.
 * </p>
 * 
 * @author Wonne Keysers (Realsoftware.be)
 */
class SAXModifyElementHandler implements ElementHandler {
    private ElementModifier elemModifier;

    private Element modifiedElement;

    public SAXModifyElementHandler(ElementModifier elemModifier) {
        this.elemModifier = elemModifier;
    }

    public void onStart(ElementPath elementPath) {
        this.modifiedElement = elementPath.getCurrent();
    }

    public void onEnd(ElementPath elementPath) {
        try {
            Element origElement = elementPath.getCurrent();
            Element currentParent = origElement.getParent();

            if (currentParent != null) {
                // Clone sets parent + document to null
                Element clonedElem = (Element) origElement.clone();

                // Ask for modified element
                modifiedElement = elemModifier.modifyElement(clonedElem);

                if (modifiedElement != null) {
                    // Restore parent + document
                    modifiedElement.setParent(origElement.getParent());
                    modifiedElement.setDocument(origElement.getDocument());

                    // Replace old with new element in parent
                    int contentIndex = currentParent.indexOf(origElement);
                    currentParent.content().set(contentIndex, modifiedElement);
                }

                // Remove the old element
                origElement.detach();
            } else {
                if (origElement.isRootElement()) {
                    // Clone sets parent + document to null
                    Element clonedElem = (Element) origElement.clone();

                    // Ask for modified element
                    modifiedElement = elemModifier.modifyElement(clonedElem);

                    if (modifiedElement != null) {
                        // Restore parent + document
                        modifiedElement.setDocument(origElement.getDocument());

                        // Replace old with new element in parent
                        Document doc = origElement.getDocument();
                        doc.setRootElement(modifiedElement);
                    }

                    // Remove the old element
                    origElement.detach();
                }
            }

            // Put the new element on the ElementStack, it might get pruned by
            // the PruningDispatchHandler
            if (elementPath instanceof ElementStack) {
                ElementStack elementStack = ((ElementStack) elementPath);
                elementStack.popElement();
                elementStack.pushElement(modifiedElement);
            }
        } catch (Exception ex) {
            throw new SAXModifyException(ex);
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @return Returns the modified Element.
     */
    protected Element getModifiedElement() {
        return modifiedElement;
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
