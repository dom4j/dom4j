/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.rule;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;


/** <p><code>Stylesheet</code> implements an XSLT stylesheet
  * such that rules can be added to the stylesheet and the 
  * stylesheet can be applied to a source document or node.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class Stylesheet {

    private RuleManager ruleManager = new RuleManager();
    
    /** Holds value of property mode. */
    private String mode;    

    
    public Stylesheet() {
    }

    public void addRule( Rule rule ) {
        ruleManager.addRule( rule );
    }
    
    public void removeRule( Rule rule ) {
        ruleManager.addRule( rule );
    }

    /** Runs this stylesheet on the given input which should be 
      * either a Node or a List of Node objects.
      */
    public void run( Object input ) {
        if ( input instanceof Node ) {
            run ( (Node) input );
        }
        else if ( input instanceof List ) {
            run( (List) input );
        }
    }
    
    public void run( List list ) {
        for ( int i = 0, size = list.size(); i < size; i++ ) {
            Object object = list.get(i);
            if ( object instanceof Node ) {
                run( (Node) object );
            }
        }
    }
    
    public void run( Node node ) {
        RuleSetManager ruleManager = getRuleSetManager();
        if ( ruleManager != null ) {
            ruleManager.fireRule( node );
        }
    }
    
    
    public void applyTemplates( Object input, XPath xpath ) {
        List list = xpath.selectNodes( input );
        run( list );
    }
    
    public void applyTemplates( Object input ) {
        // iterate through all children
        RuleSetManager ruleManager = getRuleSetManager();
        if ( ruleManager != null ) {
            if ( input instanceof Element ) {
                ruleManager.applyTemplates( (Element) input );
            }
            else if ( input instanceof Document ) { 
                ruleManager.applyTemplates( (Document) input );
            }
            else if ( input instanceof List ) {
                List list = (List) input;
                for ( int i = 0, size = list.size(); i < size; i++ ) {
                    Object object = list.get(i);
                    if ( object instanceof Element ) {
                        ruleManager.applyTemplates( (Element) object );
                    }
                    else if ( object instanceof Document ) { 
                        ruleManager.applyTemplates( (Document) object );
                    }
                }
            }
        }
    }

    public void clear() {
        ruleManager.clear();
    }

    
    // Properties
    //-------------------------------------------------------------------------                
    
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
    
    /** @return the default value-of action which is used 
     * in the default rules for the pattern "text()|@*"
     */
    public Action getValueOfAction() {
        return ruleManager.getValueOfAction();
    }
    
    /** Sets the default value-of action which is used 
     * in the default rules for the pattern "text()|@*"
     */
    public void setValueOfAction(Action valueOfAction) {
        ruleManager.setValueOfAction( valueOfAction );
    }
    

    // Implementation methods
    //------------------------------------------------------------------------- 
    protected RuleSetManager getRuleSetManager() {
        return ruleManager.getRuleSetManager( mode );
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
