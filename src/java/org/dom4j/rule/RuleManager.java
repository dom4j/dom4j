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

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.rule.pattern.NodeTypePattern;


/** <p><code>RuleManager</code> manages a set of rules such that a rule
  * can be found for a given DOM4J Node using the XSLT processing model.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class RuleManager {

    /** Map of modes indexed by mode */
    private HashMap modes = new HashMap();
    
    /** A counter so that rules can be ordered by the order in which they 
     * were added to the rule base 
     */
    private int appearenceCount;
    
    /** Holds value of property valueOfAction. */
    private Action valueOfAction;    
    
    
    public RuleManager() {
    }

    /** @return the Mode instance for the given mode name. If one does not exist
     * then it will be created.
     */
    public Mode getMode( String modeName ) {
        Mode mode = (Mode) modes.get(modeName);
        if ( mode == null ) {
            mode = createMode();
            modes.put(modeName, mode);
        }
        return mode;
    }
    
    public void addRule(Rule rule) {
        rule.setAppearenceCount( ++appearenceCount );
        
        Mode mode = getMode( rule.getMode() );
        Rule[] childRules = rule.getUnionRules();
        if ( childRules != null ) {
            for ( int i = 0, size = childRules.length; i < size; i++ ) {
                mode.addRule( childRules[i] );
            }
        }
        else {
            mode.addRule( rule );
        }
    }
    
    public void removeRule(Rule rule) {
        Mode mode = getMode( rule.getMode() );
        Rule[] childRules = rule.getUnionRules();
        if ( childRules != null ) {
            for ( int i = 0, size = childRules.length; i < size; i++ ) {
                mode.removeRule( childRules[i] );
            }
        }
        else {
            mode.removeRule( rule );
        }
    }

    /** Performs an XSLT processing model match for the rule
      * which matches the given Node the best.
      *
      * @param modeName is the name of the mode associated with the rule if any
      * @param node is the DOM4J Node to match against
      * @return the matching Rule or no rule if none matched
      */
    public Rule getMatchingRule(String modeName, Node node) {
        Mode mode = (Mode) modes.get(modeName);
        if ( mode != null ) {
            return mode.getMatchingRule( node );
        }
        else {
            System.out.println( "Warning: No Mode for mode: " + mode );
            return null;
        }
    }
    
    public void clear() {
        modes.clear();
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
    
    /** A factory method to return a new {@link Mode} instance
     * which should add the necessary default rules
     */
    protected Mode createMode() {
        Mode mode = new Mode();
        addDefaultRules( mode );
        return mode;
    }
    
    /** Adds the default stylesheet rules to the given 
     * {@link Mode} instance
     */
    protected void addDefaultRules(final Mode mode) {
        // add an apply templates rule
        Action applyTemplates = new Action() {
            public void run( Node node ) throws Exception {
                if ( node instanceof Element ) {
                    mode.applyTemplates( (Element) node );
                }
                else if ( node instanceof Document ) {
                    mode.applyTemplates( (Document) node );
                }
            }
        };

        Action valueOfAction = getValueOfAction();
        
        addDefaultRule( mode, NodeTypePattern.ANY_DOCUMENT, applyTemplates );
        addDefaultRule( mode, NodeTypePattern.ANY_ELEMENT, applyTemplates );
        
        if ( valueOfAction != null ) {
            addDefaultRule( mode, NodeTypePattern.ANY_ATTRIBUTE, valueOfAction );
            addDefaultRule( mode, NodeTypePattern.ANY_TEXT, valueOfAction );
        }
    }
    
    protected void addDefaultRule( Mode mode, Pattern pattern, Action action ) {
        Rule rule = createDefaultRule( pattern, action );
        mode.addRule( rule );
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
