/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.tree;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.dom4j.IllegalAddException;
import org.dom4j.Node;

/**
 * <p>
 * <code>ContentListFacade</code> represents a facade of the content of a
 * {@link org.dom4j.Branch} which is returned via calls to the {@link
 * org.dom4j.Branch#content}  method to allow users to modify the content of a
 * {@link org.dom4j.Branch} directly using the {@link List} interface. This list
 * is backed by the branch such that changes to the list will be reflected in
 * the branch and changes to the branch will be reflected in this list.
 * </p>
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision$
 */
public class ContentListFacade extends AbstractList {
    /** The content of the Branch which is modified if I am modified */
    private List branchContent;

    /** The <code>AbstractBranch</code> instance which owns the content */
    private AbstractBranch branch;

    public ContentListFacade(AbstractBranch branch, List branchContent) {
        this.branch = branch;
        this.branchContent = branchContent;
    }

    public boolean add(Object object) {
        branch.childAdded(asNode(object));

        return branchContent.add(object);
    }

    public void add(int index, Object object) {
        branch.childAdded(asNode(object));
        branchContent.add(index, object);
    }

    public Object set(int index, Object object) {
        branch.childAdded(asNode(object));

        return branchContent.set(index, object);
    }

    public boolean remove(Object object) {
        branch.childRemoved(asNode(object));

        return branchContent.remove(object);
    }

    public Object remove(int index) {
        Object object = branchContent.remove(index);

        if (object != null) {
            branch.childRemoved(asNode(object));
        }

        return object;
    }

    public boolean addAll(Collection collection) {
        int count = branchContent.size();

        for (Iterator iter = collection.iterator(); iter.hasNext(); count++) {
            add(iter.next());
        }

        return count == branchContent.size();
    }

    public boolean addAll(int index, Collection collection) {
        int count = branchContent.size();

        for (Iterator iter = collection.iterator(); iter.hasNext(); count--) {
            add(index++, iter.next());
        }

        return count == branchContent.size();
    }

    public void clear() {
        for (Iterator iter = iterator(); iter.hasNext();) {
            Object object = iter.next();
            branch.childRemoved(asNode(object));
        }

        branchContent.clear();
    }

    public boolean removeAll(Collection c) {
        for (Iterator iter = c.iterator(); iter.hasNext();) {
            Object object = iter.next();
            branch.childRemoved(asNode(object));
        }

        return branchContent.removeAll(c);
    }

    public int size() {
        return branchContent.size();
    }

    public boolean isEmpty() {
        return branchContent.isEmpty();
    }

    public boolean contains(Object o) {
        return branchContent.contains(o);
    }

    public Object[] toArray() {
        return branchContent.toArray();
    }

    public Object[] toArray(Object[] a) {
        return branchContent.toArray(a);
    }

    public boolean containsAll(Collection c) {
        return branchContent.containsAll(c);
    }

    public Object get(int index) {
        return branchContent.get(index);
    }

    public int indexOf(Object o) {
        return branchContent.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return branchContent.lastIndexOf(o);
    }

    protected Node asNode(Object object) {
        if (object instanceof Node) {
            return (Node) object;
        } else {
            throw new IllegalAddException(
                    "This list must contain instances of "
                            + "Node. Invalid type: " + object);
        }
    }

    protected List getBackingList() {
        return branchContent;
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
