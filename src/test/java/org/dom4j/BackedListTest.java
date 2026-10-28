/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j;

import org.dom4j.io.XMLWriter;
import org.dom4j.tree.BackedList;
import org.dom4j.tree.DefaultDocument;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A test harness to test the backed list feature of DOM4J
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision: 1.3 $
 */
public class BackedListTest extends AbstractTestCase {
    // Test case(s)
    // -------------------------------------------------------------------------
    public void testXPaths() throws Exception {
        Element element = (Element) document.selectSingleNode("/root");
        mutate(element);
        element = (Element) document.selectSingleNode("//author");
        mutate(element);
    }

    public void testAddRemove() throws Exception {
        Element parentElement = (Element) document.selectSingleNode("/root");
        List children = parentElement.elements();
        int lastPos = children.size() - 1;
        Element child = (Element) children.get(lastPos);

        try {
            // should throw an exception cause we cannot add same child twice
            children.add(0, child);
            fail();
        } catch (IllegalAddException e) {
        }
    }

    public void testAddWithIndex() throws Exception {
        DocumentFactory factory = DocumentFactory.getInstance();

        Element root = (Element) document.selectSingleNode("/root");
        List children = root.elements(); // return a list of 2 author
        // elements

        assertEquals(2, children.size());

        children.add(1, factory.createElement("dummy1"));
        children = root.elements();

        assertEquals(3, children.size());

        children = root.elements("author");

        assertEquals(2, children.size());

        children.add(1, factory.createElement("dummy2"));

        children = root.elements();

        assertEquals(4, children.size());
        assertEquals("dummy1", ((Node) children.get(1)).getName());
        assertEquals("dummy2", ((Node) children.get(2)).getName());

        /*
         * Some tests for issue reported at http://tinyurl.com/4jxrc
         */
        children.add(children.size(), factory.createElement("dummy3"));
        children = root.elements("author");
        children.add(children.size(), factory.createElement("dummy4"));
    }

    public void testSortElementsPreservesMixedContentSlots() {
        Element root = DocumentHelper.createElement("root");
        Text lead = DocumentHelper.createText("lead");
        Comment comment = DocumentHelper.createComment("middle");
        ProcessingInstruction instruction = DocumentHelper.createProcessingInstruction("keep", "data");
        Text tail = DocumentHelper.createText("tail");
        root.add(lead);
        Element third = rankedElement(root, "item", 3);
        root.add(comment);
        Element first = rankedElement(root, "item", 1);
        root.add(instruction);
        Element second = rankedElement(root, "item", 2);
        root.add(tail);

        List<Element> selected = root.elements();
        selected.sort(byRank());

        assertNodeOrder(selected, first, second, third);
        assertNodeOrder(root.content(), lead, first, comment, second, instruction, third, tail);
        assertElementParents(root, first, second, third);
        assertSame(root, lead.getParent());
        assertSame(root, comment.getParent());
        assertSame(root, instruction.getParent());
        assertSame(root, tail.getParent());
    }

    public void testSortElementsByNamePreservesOtherSlots() {
        assertFilteredSort(false);
    }

    public void testSortElementsByQNamePreservesOtherSlots() {
        assertFilteredSort(true);
    }

    public void testCollectionsSortIsStableForEqualRanks() {
        Element root = DocumentHelper.createElement("root");
        Element third = rankedElement(root, "item", 3);
        Element equalFirst = rankedElement(root, "item", 1);
        Element equalSecond = rankedElement(root, "item", 1);
        Element last = rankedElement(root, "item", 4);
        List<Element> selected = root.elements();

        Collections.sort(selected, byRank());

        assertNodeOrder(selected, equalFirst, equalSecond, third, last);
        assertNodeOrder(root.elements(), equalFirst, equalSecond, third, last);
        assertElementParents(root, equalFirst, equalSecond, third, last);
    }

    public void testSortStaleSelectionLeavesMovedAndNewChildrenAlone() {
        Element root = DocumentHelper.createElement("root");
        Element third = rankedElement(root, "item", 3);
        Element moved = rankedElement(root, "item", 2);
        Element first = rankedElement(root, "item", 1);
        List<Element> selected = root.elements();
        Element other = DocumentHelper.createElement("other");
        moved.detach();
        other.add(moved);
        Element inserted = DocumentHelper.createElement("new");
        root.elements().add(1, inserted);

        selected.sort(byRank());

        assertNodeOrder(selected, first, moved, third);
        assertNodeOrder(root.content(), first, inserted, third);
        assertNodeOrder(other.elements(), moved);
        assertSame(other, moved.getParent());
        assertElementParents(root, first, inserted, third);
    }

    public void testSortUsesCurrentContentAfterClearContent() {
        assertSortAfterContentReplacement(true);
    }

    public void testSortUsesCurrentContentAfterSetContent() {
        assertSortAfterContentReplacement(false);
    }

    public void testSortLocalEntriesDoesNotAttachOrDuplicateChildren() {
        Element root = DocumentHelper.createElement("root");
        Element third = rankedElement(root, "item", 3);
        Element first = rankedElement(root, "item", 1);
        Element second = rankedElement(root, "item", 2);
        BackedList<Element> selected = (BackedList<Element>) root.elements();
        Element detached = DocumentHelper.createElement("local").addAttribute("rank", "0");
        selected.addLocal(third);
        selected.addLocal(detached);

        selected.sort(byRank());

        assertNodeOrder(selected, detached, first, second, third, third);
        assertNodeOrder(root.content(), first, second, third);
        assertElementParents(root, first, second, third);
        assertNull(detached.getParent());
    }

    public void testSortDoesNotReplaceAnEqualButDifferentNamespace() {
        Element root = DocumentHelper.createElement("root");
        Namespace removed = new Namespace("p", "urn:same");
        Namespace remaining = new Namespace("z", "urn:other");
        root.add(remaining);
        root.add(removed);
        List<Namespace> selected = root.declaredNamespaces();
        selected.sort(Comparator.comparing(Namespace::getPrefix));
        assertNodeOrder(selected, removed, remaining);
        assertNodeOrder(root.content(), removed, remaining);
        root.remove(removed);
        Namespace replacement = new Namespace("p", "urn:same");
        assertEquals(removed, replacement);
        assertNotSame(removed, replacement);
        root.add(replacement);

        selected.sort(Comparator.comparing(Namespace::getPrefix));

        assertNodeOrder(selected, removed, remaining);
        assertNodeOrder(root.content(), remaining, replacement);
        assertNull(removed.getParent());
        assertNull(remaining.getParent());
        assertNull(replacement.getParent());
    }

    public void testComparatorFailureDoesNotReorderTheBranch() {
        Element root = DocumentHelper.createElement("root");
        Element third = rankedElement(root, "item", 3);
        Element second = rankedElement(root, "item", 2);
        Element first = rankedElement(root, "item", 1);
        List<Element> selected = root.elements();
        IllegalStateException failure = new IllegalStateException("stop sorting");
        Comparator<Element> comparator = new Comparator<Element>() {
            private int comparisons;

            public int compare(Element left, Element right) {
                if (++comparisons == 2) {
                    throw failure;
                }
                return byRank().compare(left, right);
            }
        };

        try {
            selected.sort(comparator);
            fail("Comparator exception must be propagated");
        } catch (IllegalStateException expected) {
            assertSame(failure, expected);
        }

        assertNodeOrder(root.content(), third, second, first);
        assertElementParents(root, third, second, first);
    }

    public void testSortEmptyAndSingleSelections() {
        Element root = DocumentHelper.createElement("root");
        Comparator<Element> neverCalled = (left, right) -> {
            throw new AssertionError("No comparison needed");
        };
        List<Element> empty = root.elements();
        empty.sort(neverCalled);
        assertNodeOrder(root.content());
        Element only = rankedElement(root, "item", 1);
        List<Element> single = root.elements();
        single.sort(neverCalled);
        assertNodeOrder(single, only);
        assertNodeOrder(root.content(), only);
        assertSame(root, only.getParent());
    }

    public void testSortDocumentProcessingInstructionSelection() {
        DefaultDocument doc = new DefaultDocument();
        Comment lead = DocumentHelper.createComment("lead");
        Comment tail = DocumentHelper.createComment("tail");
        ProcessingInstruction second = DocumentHelper.createProcessingInstruction("z", "second");
        ProcessingInstruction first = DocumentHelper.createProcessingInstruction("a", "first");
        doc.add(lead);
        doc.add(second);
        Element root = doc.addElement("root");
        doc.add(first);
        doc.add(tail);
        BackedList<ProcessingInstruction> selected = new BackedList<ProcessingInstruction>(
                doc, doc.content(), Arrays.asList(second, first));

        selected.sort(Comparator.comparing(ProcessingInstruction::getName));

        assertNodeOrder(selected, first, second);
        assertNodeOrder(doc.content(), lead, first, root, second, tail);
        assertSame(root, doc.getRootElement());
        assertSame(doc, root.getDocument());
        assertNull(root.getParent());
    }

    private void assertFilteredSort(boolean byQName) {
        Element root = DocumentHelper.createElement("root");
        QName selectedName = QName.get("item", "a", "urn:selected");
        QName otherName = QName.get("item", "b", "urn:other");
        Element otherNamespace = root.addElement(otherName).addAttribute("rank", "3");
        Comment comment = DocumentHelper.createComment("keep");
        Element second = root.addElement(selectedName).addAttribute("rank", "2");
        root.add(comment);
        Element untouched = rankedElement(root, "different", 9);
        Element first = root.addElement(selectedName).addAttribute("rank", "1");
        List<Element> selected = byQName ? root.elements(selectedName) : root.elements("item");

        selected.sort(byRank());

        if (byQName) {
            assertNodeOrder(selected, first, second);
            assertNodeOrder(root.content(), otherNamespace, first, comment, untouched, second);
        } else {
            assertNodeOrder(selected, first, second, otherNamespace);
            assertNodeOrder(root.content(), first, second, comment, untouched, otherNamespace);
        }
        assertElementParents(root, otherNamespace, first, untouched, second);
        assertSame(root, comment.getParent());
    }

    private void assertSortAfterContentReplacement(boolean clearFirst) {
        Element root = DocumentHelper.createElement("root");
        Element third = rankedElement(root, "item", 3);
        Element removed = rankedElement(root, "item", 2);
        Element first = rankedElement(root, "item", 1);
        List<Element> selected = root.elements();
        Comment inserted = DocumentHelper.createComment("new content");
        if (clearFirst) {
            root.clearContent();
            root.add(third);
            root.add(inserted);
            root.add(first);
        } else {
            root.setContent(Arrays.<Node>asList(third, inserted, first));
        }

        selected.sort(byRank());

        assertNodeOrder(selected, first, removed, third);
        assertNodeOrder(root.content(), first, inserted, third);
        assertElementParents(root, first, third);
        assertNull(removed.getParent());
        assertSame(root, inserted.getParent());
    }

    private Element rankedElement(Element parent, String name, int rank) {
        return parent.addElement(name).addAttribute("rank", String.valueOf(rank));
    }

    private Comparator<Element> byRank() {
        return Comparator.comparingInt(element -> Integer.parseInt(element.attributeValue("rank")));
    }

    private void assertElementParents(Element parent, Element... children) {
        for (Element child : children) {
            assertSame(parent, child.getParent());
        }
    }

    private void assertNodeOrder(List<? extends Node> actual, Node... expected) {
        assertEquals(expected.length, actual.size());
        for (int i = 0; i < expected.length; i++) {
            assertSame("Node at index " + i, expected[i], actual.get(i));
        }
    }

    // Implementation methods
    // -------------------------------------------------------------------------
    protected void mutate(Element element) throws Exception {
        DocumentFactory factory = DocumentFactory.getInstance();

        List list = element.elements();
        list.add(factory.createElement("last"));
        list.add(0, factory.createElement("first"));

        List list2 = element.elements();

        assertTrue("Both lists should contain same number of elements", list
                .size() == list2.size());

        XMLWriter writer = new XMLWriter(System.out);

        log("Element content is now: " + element.content());
        writer.write(element);
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
