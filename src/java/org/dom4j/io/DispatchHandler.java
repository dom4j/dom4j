/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.io;

import java.util.ArrayList;
import java.util.HashMap;

import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;

/**
 * <p>
 * <code>DispatchHandler</code> implements the <code>ElementHandler</code>
 * interface and provides a means to register multiple
 * <code>ElementHandler</code> instances to be used by an event based
 * processor. This is a special <code>ElementHandler</code> in that it's
 * <b>onStart </b> and <b>onEnd </b> implementation methods are called for every
 * element encountered during the parse. It then delegates to other
 * <code>ElementHandler</code> instances registered with it to process the
 * elements encountered.
 * </p>
 * 
 * @author <a href="mailto:dwhite@equipecom.com">Dave White </a>
 * @version $Revision$
 */
class DispatchHandler implements ElementHandler {
    /** Whether the parser is at the root element or not */
    private boolean atRoot;

    /** The current path in the XML tree (i.e. /a/b/c) */
    private String path;

    /** maintains a stack of previously encountered paths */
    private ArrayList pathStack;

    /** maintains a stack of previously encountered handlers */
    private ArrayList handlerStack;

    /**
     * <code>HashMap</code> maintains the mapping between element paths and
     * handlers
     */
    private HashMap handlers;

    /**
     * <code>ElementHandler</code> to use by default for element paths with no
     * handlers registered
     */
    private ElementHandler defaultHandler;

    public DispatchHandler() {
        atRoot = true;
        path = "/";
        pathStack = new ArrayList();
        handlerStack = new ArrayList();
        handlers = new HashMap();
    }

    /**
     * Adds the <code>ElementHandler</code> to be called when the specified
     * path is encounted.
     * 
     * @param handlerPath
     *            is the path to be handled
     * @param handler
     *            is the <code>ElementHandler</code> to be called by the event
     *            based processor.
     */
    public void addHandler(String handlerPath, ElementHandler handler) {
        handlers.put(handlerPath, handler);
    }

    /**
     * Removes the <code>ElementHandler</code> from the event based processor,
     * for the specified path.
     * 
     * @param handlerPath
     *            is the path to remove the <code>ElementHandler</code> for.
     * 
     * @return DOCUMENT ME!
     */
    public ElementHandler removeHandler(String handlerPath) {
        return (ElementHandler) handlers.remove(handlerPath);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param handlerPath
     *            DOCUMENT ME!
     * 
     * @return true when an <code>ElementHandler</code> is registered for the
     *         specified path.
     */
    public boolean containsHandler(String handlerPath) {
        return handlers.containsKey(handlerPath);
    }

    /**
     * Get the registered {@link ElementHandler}for the specified path.
     * 
     * @param handlerPath
     *            XML path to get the handler for
     * 
     * @return the registered handler
     */
    public ElementHandler getHandler(String handlerPath) {
        return (ElementHandler) handlers.get(handlerPath);
    }

    /**
     * Returns the number of {@link ElementHandler}objects that are waiting for
     * their elements closing tag.
     * 
     * @return number of active handlers
     */
    public int getActiveHandlerCount() {
        return handlerStack.size();
    }

    /**
     * When multiple <code>ElementHandler</code> instances have been
     * registered, this will set a default <code>ElementHandler</code> to be
     * called for any path which does <b>NOT </b> have a handler registered.
     * 
     * @param handler
     *            is the <code>ElementHandler</code> to be called by the event
     *            based processor.
     */
    public void setDefaultHandler(ElementHandler handler) {
        defaultHandler = handler;
    }

    /**
     * Used to remove all the Element Handlers and return things back to the way
     * they were when object was created.
     */
    public void resetHandlers() {
        atRoot = true;
        path = "/";
        pathStack.clear();
        handlerStack.clear();
        handlers.clear();
        defaultHandler = null;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return the current path for the parse
     */
    public String getPath() {
        return path;
    }

    // The following methods implement the ElementHandler interface
    public void onStart(ElementPath elementPath) {
        Element element = elementPath.getCurrent();

        // Save the location of the last (i.e. parent) path
        pathStack.add(path);

        // Calculate the new path
        if (atRoot) {
            path = path + element.getName();
            atRoot = false;
        } else {
            path = path + "/" + element.getName();
        }

        if ((handlers != null) && (handlers.containsKey(path))) {
            // The current node has a handler associated with it.
            // Find the handler and save it on the handler stack.
            ElementHandler handler = (ElementHandler) handlers.get(path);
            handlerStack.add(handler);

            // Call the handlers onStart method.
            handler.onStart(elementPath);
        } else {
            // No handler is associated with this node, so use the
            // defaultHandler it it exists.
            if (handlerStack.isEmpty() && (defaultHandler != null)) {
                defaultHandler.onStart(elementPath);
            }
        }
    }

    public void onEnd(ElementPath elementPath) {
        if ((handlers != null) && (handlers.containsKey(path))) {
            // This node has a handler associated with it.
            // Find the handler and pop it from the handler stack.
            ElementHandler handler = (ElementHandler) handlers.get(path);
            handlerStack.remove(handlerStack.size() - 1);

            // Call the handlers onEnd method
            handler.onEnd(elementPath);
        } else {
            // No handler is associated with this node, so use the
            // defaultHandler it it exists.
            if (handlerStack.isEmpty() && (defaultHandler != null)) {
                defaultHandler.onEnd(elementPath);
            }
        }

        // Set path back to its parent
        path = (String) pathStack.remove(pathStack.size() - 1);

        if (pathStack.size() == 0) {
            atRoot = true;
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
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 */
