/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.rule;

import java.util.ArrayList;
import java.util.Collections;

import org.dom4j.Node;

/**
 * <p>
 * <code>RuleSet</code> manages a set of rules which are sorted in order of
 * relevance according to the XSLT defined conflict resolution policy. This
 * makes finding the correct rule for a DOM4J Node using the XSLT processing
 * model efficient as the rules can be evaluated in order of priority.
 * </p>
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision$
 */
public class RuleSet {
    /** An unordered list of Rule objects */
    private ArrayList rules = new ArrayList();

    /** A lazily evaluated and cached array of rules sorted */
    private Rule[] ruleArray;

    public RuleSet() {
    }

    public String toString() {
        return super.toString() + " [RuleSet: " + rules + " ]";
    }

    /**
     * Performs an XSLT processing model match for the rule which matches the
     * given Node the best.
     * 
     * @param node
     *            is the DOM4J Node to match against
     * 
     * @return the matching Rule or no rule if none matched
     */
    public Rule getMatchingRule(Node node) {
        Rule[] matches = getRuleArray();

        for (int i = matches.length - 1; i >= 0; i--) {
            Rule rule = matches[i];

            if (rule.matches(node)) {
                return rule;
            }
        }

        return null;
    }

    public void addRule(Rule rule) {
        rules.add(rule);
        ruleArray = null;
    }

    public void removeRule(Rule rule) {
        rules.remove(rule);
        ruleArray = null;
    }

    /**
     * Adds all the rules to this RuleSet from the given other rule set.
     * 
     * @param that
     *            DOCUMENT ME!
     */
    public void addAll(RuleSet that) {
        rules.addAll(that.rules);
        ruleArray = null;
    }

    /**
     * Returns an array of sorted rules.
     * 
     * @return the rules as a sorted array in ascending precendence so that the
     *         rules at the end of the array should be used first
     */
    protected Rule[] getRuleArray() {
        if (ruleArray == null) {
            Collections.sort(rules);

            int size = rules.size();
            ruleArray = new Rule[size];
            rules.toArray(ruleArray);
        }

        return ruleArray;
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
