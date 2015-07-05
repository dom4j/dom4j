/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.rule;

import java.util.List;

import org.dom4j.*;

/**
 * <p>
 * <code>Stylesheet</code> implements an XSLT stylesheet such that rules can
 * be added to the stylesheet and the stylesheet can be applied to a source
 * document or node.
 * </p>
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision: 1.14 $
 */
public class Stylesheet {
    private RuleManager ruleManager = new RuleManager();

    /** Holds value of property mode. */
    private String modeName;

    /**
     * Creates a new empty stylesheet.
     */
    public Stylesheet() {
    }

    /**
     * Add a rule to this stylesheet.
     * 
     * @param rule
     *            the rule to add
     */
    public void addRule(Rule rule) {
        ruleManager.addRule(rule);
    }

    /**
     * Removes the specified rule from this stylesheet.
     * 
     * @param rule
     *            the rule to remove
     */
    public void removeRule(Rule rule) {
        ruleManager.removeRule(rule);
    }

    public void run(List<Node> list) throws Exception {
        run(list, this.modeName);
    }

    public void run(List<Node> list, String mode) throws Exception {
        for (Node node : list) {
            run(node, mode);
        }
    }

    public void run(Node node) throws Exception {
        run(node, this.modeName);
    }

    public void run(Node node, String mode) throws Exception {
        Mode mod = ruleManager.getMode(mode);
        mod.fireRule(node);
    }

    /**
     * Processes the result of the xpath expression. The xpath expression is
     * evaluated against the provided input object.
     * 
     * @param input
     *            the input object
     * @param xpath
     *            the xpath expression
     * @throws Exception
     *             if something goes wrong
     */
    public void applyTemplates(Object input, XPath xpath) throws Exception {
        applyTemplates(input, xpath, this.modeName);
    }

    /**
     * Processes the result of the xpath expression in the given mode. The xpath
     * expression is evaluated against the provided input object.
     * 
     * @param input
     *            the input object
     * @param xpath
     *            the xpath expression
     * @param mode
     *            the mode
     * @throws Exception
     *             if something goes wrong
     */
    public void applyTemplates(Object input, XPath xpath, String mode)
            throws Exception {
        Mode mod = ruleManager.getMode(mode);

        List<Node> list = xpath.selectNodes(input);
        for (Node current : list) {
            mod.fireRule(current);
        }
    }

    /**
     * If input is a <code>Node</code>, this will processes all of the
     * children of that node. If input is a <code>List</code> of
     * <code>Nodes</code>s, these nodes will be iterated and all children of
     * each node will be processed.
     * 
     * @param node the input object
     * @throws Exception
     *             if something goes wrong
     */
    public void applyTemplates(Node node) throws Exception {
        applyTemplates(node, this.modeName);
    }

    /**
     * If input is a <code>Node</code>, this will processes all of the
     * children of that node. If input is a <code>List</code> of
     * <code>Nodes</code>s, these nodes will be iterated and all children of
     * each node will be processed.
     *
     * @param element the input object
     * @throws Exception
     *             if something goes wrong
     */
    public void applyTemplates(Element element) throws Exception {
        applyTemplates(element, this.modeName);
    }

    /**
     * If input is a <code>Node</code>, this will processes all of the
     * children of that node. If input is a <code>List</code> of
     * <code>Nodes</code>s, these nodes will be iterated and all children of
     * each node will be processed.
     *
     * @param document the input object
     * @throws Exception
     *             if something goes wrong
     */
    public void applyTemplates(Document document) throws Exception {
        applyTemplates(document, this.modeName);
    }

    /**
     * If input is a <code>Node</code>, this will processes all of the
     * children of that node. If input is a <code>List</code> of
     * <code>Nodes</code>s, these nodes will be iterated and all children of
     * each node will be processed.
     *
     * @param list the input object
     * @throws Exception
     *             if something goes wrong
     */
    public void applyTemplates(List<Node> list) throws Exception {
        applyTemplates(list, this.modeName);
    }

    /**
     * Processes the input object in the given mode. If input is a
     * <code>Node</code>, this will processes all of the children of that
     * node. If input is a <code>List</code> of <code>Nodes</code>s, these
     * nodes will be iterated and all children of each node will be processed.
     * 
     * @param node the input object
     * @param mode
     *            the mode
     * @throws Exception
     *             if something goes wrong
     */
    public void applyTemplates(Node node, String mode) throws Exception {
        if (node instanceof Element) {
            applyTemplates((Element) node, mode);
        } else if (node instanceof Document) {
            applyTemplates((Document) node, mode);
        }
    }

    /**
     * Processes the input object in the given mode. If input is a
     * <code>Node</code>, this will processes all of the children of that
     * node. If input is a <code>List</code> of <code>Nodes</code>s, these
     * nodes will be iterated and all children of each node will be processed.
     *
     * @param element the input object
     * @param mode
     *            the mode
     * @throws Exception
     *             if something goes wrong
     */
    public void applyTemplates(Element element, String mode) throws Exception {
        Mode mod = ruleManager.getMode(mode);

        // iterate through all children
        for (int i = 0, size = element.nodeCount(); i < size; i++) {
            Node node = element.node(i);
            mod.fireRule(node);
        }
    }

    /**
     * Processes the input object in the given mode. If input is a
     * <code>Node</code>, this will processes all of the children of that
     * node. If input is a <code>List</code> of <code>Nodes</code>s, these
     * nodes will be iterated and all children of each node will be processed.
     *
     * @param document the input object
     * @param mode
     *            the mode
     * @throws Exception
     *             if something goes wrong
     */
    public void applyTemplates(Document document, String mode) throws Exception {
        Mode mod = ruleManager.getMode(mode);

        // iterate through all children
        for (int i = 0, size = document.nodeCount(); i < size; i++) {
            Node node = document.node(i);
            mod.fireRule(node);
        }
    }

    /**
     * Processes the input object in the given mode. If input is a
     * <code>Node</code>, this will processes all of the children of that
     * node. If input is a <code>List</code> of <code>Nodes</code>s, these
     * nodes will be iterated and all children of each node will be processed.
     *
     * @param list list of Elements or Documents
     * @param mode
     *            the mode
     * @throws Exception
     *             if something goes wrong
     */
    public void applyTemplates(List<? extends Node> list, String mode) throws Exception {
        for (Node node : list) {
            if (node instanceof Element) {
                applyTemplates((Element) node, mode);
            } else if (node instanceof Document) {
                applyTemplates((Document) node, mode);
            }
        }
    }

    public void clear() {
        ruleManager.clear();
    }

    // Properties
    // -------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @return the name of the mode the stylesheet uses by default
     */
    public String getModeName() {
        return modeName;
    }

    /**
     * Sets the name of the mode that the stylesheet uses by default.
     * 
     * @param modeName
     *            DOCUMENT ME!
     */
    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return the default value-of action which is used in the default rules
     *         for the pattern "text()|&#64;"
     */
    public Action getValueOfAction() {
        return ruleManager.getValueOfAction();
    }

    /**
     * Sets the default value-of action which is used in the default rules for
     * the pattern "text()|&#64;"
     * 
     * @param valueOfAction
     *            DOCUMENT ME!
     */
    public void setValueOfAction(Action valueOfAction) {
        ruleManager.setValueOfAction(valueOfAction);
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
