/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.rule;

import org.dom4j.Node;

/** <p><code>Rule</code> matches against DOM4J Node so that some action
  * can be performed such as in the XSLT processing model.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class Rule implements Comparable {

    /** Holds value of property mode. */
    private String mode;
    
    /** Holds value of property importPrecedence. */
    private int importPrecedence;
    
    /** Holds value of property priority. */
    private double priority;
    
    /** Holds value of property appearenceCount. */
    private int appearenceCount;
    
    /** Holds value of property pattern. */
    private Pattern pattern;    
    
    /** Holds value of property action. */
    private Action action;
    
    public Rule() {
        this.priority = Pattern.DEFAULT_PRIORITY;
    }

    public Rule( Pattern pattern ) {
        this.pattern = pattern;
        this.priority = pattern.getPriority();
    }

    public Rule( Pattern pattern, Action action ) {
        this( pattern );
        this.action = action;
    }

    /** Constructs a new Rule with the same instance data
      * as the given rule but a different pattern.
      */
    public Rule(Rule that, Pattern pattern) {
        this.mode = that.mode;
        this.importPrecedence = that.importPrecedence;
        this.priority = that.priority;
        this.appearenceCount = that.appearenceCount;
        this.action = that.action;
        this.pattern = pattern;
    }

    public boolean equals(Object that) {
        if ( that instanceof Rule ) {
            return compareTo( (Rule) that ) == 0;
        }
        return false;
    }
    
    public int compareTo(Object that) {
        if ( that instanceof Rule ) {
            return compareTo((Rule) that);
        }
        return getClass().getName().compareTo( that.getClass().getName() );
    }
    
    /** Compares two rules in XSLT processing model order
      * assuming that the modes are equal.
      */
    public int compareTo(Rule that) {
        int answer = this.importPrecedence - that.importPrecedence;
        if ( answer == 0 ) {
            answer = (int) Math.round( this.priority - that.priority );
            if ( answer == 0 ) {
                answer = this.appearenceCount - that.appearenceCount;
            }
        }
        return answer;
    }
    
    public String toString() {
        return super.toString() + "[ pattern: " + getPattern() + " action: " + getAction() + " ]";
    }
    
    
    
    /** @return true if the pattern matches the given 
      * DOM4J node.
      */
    public final boolean matches( Node node ) {
        return pattern.matches( node );
    }
    
    /** If this rule contains a union pattern then this
      * method should return an array of Rules which
      * describe the union rule, which should contain more than one rule.
      * Otherwise this method should return null.
      *
      * @return an array of the rules which make up this union rule
      * or null if this rule is not a union rule
      */
    public Rule[] getUnionRules() {
        Pattern[] patterns = pattern.getUnionPatterns();
        if ( patterns == null ) {
            return null;
        }
        int size = patterns.length;
        Rule[] answer = new Rule[ size ];
        for ( int i = 0; i < size; i++ ) {
            answer[i] = new Rule( this, patterns[i] );
        }
        return answer;
    }

    
    /** @return the type of node the pattern matches
      * which by default should return ANY_NODE if it can
      * match any kind of node.
      */
    public final short getMatchType() {
        return pattern.getMatchType();
    }


    /** For patterns which only match an ATTRIBUTE_NODE or an 
      * ELEMENT_NODE then this pattern may return the name of the
      * element or attribute it matches. This allows a more efficient
      * rule matching algorithm to be performed, rather than a brute 
      * force approach of evaluating every pattern for a given Node.
      *
      * @return the name of the element or attribute this pattern matches
      * or null if this pattern matches any or more than one name.
      */
    public final String getMatchesNodeName() {
        return pattern.getMatchesNodeName();
    }
    
    
    
    
    
    /** Getter for property mode.
     * @return Value of property mode.
     */
    public String getMode() {
        return mode;
    }
    
    /** Setter for property mode.
     * @param mode New value of property mode.
     */
    public void setMode(String mode) {
        this.mode = mode;
    }
    
    /** Getter for property importPrecedence.
     * @return Value of property importPrecedence.
     */
    public int getImportPrecedence() {
        return importPrecedence;
    }
    
    /** Setter for property importPrecedence.
     * @param importPrecedence New value of property importPrecedence.
     */
    public void setImportPrecedence(int importPrecedence) {
        this.importPrecedence = importPrecedence;
    }
    
    /** Getter for property priority.
     * @return Value of property priority.
     */
    public double getPriority() {
        return priority;
    }
    
    /** Setter for property priority.
     * @param priority New value of property priority.
     */
    public void setPriority(double priority) {
        this.priority = priority;
    }
    
    /** Getter for property appearenceCount.
     * @return Value of property appearenceCount.
     */
    public int getAppearenceCount() {
        return appearenceCount;
    }
    
    /** Setter for property appearenceCount.
     * @param appearenceCount New value of property appearenceCount.
     */
    public void setAppearenceCount(int appearenceCount) {
        this.appearenceCount = appearenceCount;
    }
    
    /** Getter for property pattern.
     * @return Value of property pattern.
     */
    public Pattern getPattern() {
        return pattern;
    }
    
    /** Setter for property pattern.
     * @param pattern New value of property pattern.
     */
    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
    
    /** Getter for property action.
     * @return Value of property action.
     */
    public Action getAction() {
        return action;
    }
    
    /** Setter for property action.
     * @param action New value of property action.
     */
    public void setAction(Action action) {
        this.action = action;
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
