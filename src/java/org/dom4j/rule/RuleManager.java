/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.rule;

import java.util.HashMap;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.rule.pattern.NodeTypePattern;
import org.dom4j.rule.pattern.RootNodePattern;


/** <p><code>RuleManager</code> manages a set of rules such that a rule
  * can be found for a given DOM4J Node using the XSLT processing model.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class RuleManager {

    /** Map of RuleSetManagers indexed by mode */
    private HashMap ruleSetManagers = new HashMap();
    
    /** A counter so that rules can be ordered by the order in which they 
     * were added to the rule base 
     */
    private int appearenceCount;
    
    /** Holds value of property valueOfAction. */
    private Action valueOfAction;    
    
    public RuleManager() {
    }

    /** @return the rule set manager for the given mode. If one does not exist
     * then it will be created.
     */
    public RuleSetManager getRuleSetManager( String mode ) {
        RuleSetManager ruleSetManager = (RuleSetManager) ruleSetManagers.get(mode);
        if ( ruleSetManager == null ) {
            ruleSetManager = createRuleSetManager();
            ruleSetManagers.put(mode, ruleSetManager);
        }
        return ruleSetManager;
    }
    
    public void addRule(Rule rule) {
        rule.setAppearenceCount( ++appearenceCount );
        
        RuleSetManager ruleSetManager = getRuleSetManager( rule.getMode() );
        Rule[] childRules = rule.getUnionRules();
        if ( childRules != null ) {
            for ( int i = 0, size = childRules.length; i < size; i++ ) {
                ruleSetManager.addRule( childRules[i] );
            }
        }
        else {
            ruleSetManager.addRule( rule );
        }
    }
    
    public void removeRule(Rule rule) {
        RuleSetManager ruleSetManager = getRuleSetManager( rule.getMode() );
        Rule[] childRules = rule.getUnionRules();
        if ( childRules != null ) {
            for ( int i = 0, size = childRules.length; i < size; i++ ) {
                ruleSetManager.removeRule( childRules[i] );
            }
        }
        else {
            ruleSetManager.removeRule( rule );
        }
    }

    /** Performs an XSLT processing model match for the rule
      * which matches the given Node the best.
      *
      * @param mode is the mode associated with the rule if any
      * @param node is the DOM4J Node to match against
      * @return the matching Rule or no rule if none matched
      */
    public Rule getMatchingRule(String mode, Node node) {
        RuleSetManager ruleSetManager = (RuleSetManager) ruleSetManagers.get(mode);
        if ( ruleSetManager != null ) {
            return ruleSetManager.getMatchingRule( node );
        }
        else {
            System.out.println( "Warning: No RuleSetManager for mode: " + mode );
            return null;
        }
    }
    
    public void clear() {
        ruleSetManagers.clear();
        appearenceCount = 0;
    }

    
    // Properties
    //-------------------------------------------------------------------------                
    
    /** @return the default value-of action which is used 
     * in the default rules for the pattern "text()|@*"
     */
    public Action getValueOfAction() {
        return valueOfAction;
    }
    
    /** Sets the default value-of action which is used 
     * in the default rules for the pattern "text()|@*"
     */
    public void setValueOfAction(Action valueOfAction) {
        this.valueOfAction = valueOfAction;
    }
    
    // Implementation methods
    //-------------------------------------------------------------------------                
    
    /** A factory method to return a new {@link RuleSetManager} instance
     * which should add the necessary default rules
     */
    protected RuleSetManager createRuleSetManager() {
        RuleSetManager manager = new RuleSetManager();
        addDefaultRules( manager );
        return manager;
    }
    
    /** Adds the default stylesheet rules to the given 
     * {@link RuleSetManager} instance
     */
    protected void addDefaultRules(final RuleSetManager manager) {
        // add an apply templates rule
        Action applyTemplates = new Action() {
            public void run( Node node ) {
                if ( node instanceof Element ) {
                    manager.applyTemplates( (Element) node );
                }
                else if ( node instanceof Document ) {
                    manager.applyTemplates( (Document) node );
                }
            }
        };

        Action valueOfAction = getValueOfAction();
        
        addDefaultRule( manager, RootNodePattern.SINGLETON, applyTemplates );
        addDefaultRule( manager, NodeTypePattern.ANY_ELEMENT, applyTemplates );
        
        if ( valueOfAction != null ) {
            addDefaultRule( manager, NodeTypePattern.ANY_ATTRIBUTE, valueOfAction );
            addDefaultRule( manager, NodeTypePattern.ANY_TEXT, valueOfAction );
        }
    }
    
    protected void addDefaultRule( RuleSetManager manager, Pattern pattern, Action action ) {
        Rule rule = createDefaultRule( pattern, action );
        manager.addRule( rule );
    }
    
    protected Rule createDefaultRule( Pattern pattern, Action action ) {
        Rule rule = new Rule( pattern, action );
        rule.setImportPrecedence( -1 );
        return rule;
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
