/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */


package org.dom4j.xpath.impl;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.rule.Pattern;
import org.dom4j.xpath.impl.Context;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


public class LocationPath extends PathExpr implements org.jaxen.expr.LocationPath {
    
    private boolean _isAbsolute;
    private List    _steps;
    
    public LocationPath() {        
    }
    
    public LocationPath(boolean isAbsolute) {
        _isAbsolute = isAbsolute;
    }
    
    
    public void setAbsolute(boolean isAbsolute) {
        _isAbsolute = isAbsolute;
    }
    
    public boolean isAbsolute() {
        return _isAbsolute;
    }

    public void addStep(org.jaxen.expr.Step step) {
        if ( _steps == null ) {
            _steps = new ArrayList();
        }
        _steps.add(step);        
    }
    
    public void addStep(Step step) {
        if ( _steps == null ) {
            _steps = new ArrayList();
        }
        _steps.add(step);        
    }
    
    public boolean isEmpty() {
        return _steps == null || _steps.isEmpty();
    }
    
    public List getSteps() {
        if ( _steps == null ) {
            return Collections.EMPTY_LIST;
        }        
        return _steps;
    }
    
    public Object evaluate(Context context) {
        return applyTo(context);
    }
    
    public Object applyTo(Context context) {        
        if ( getSteps().isEmpty() ) {
            if ( isAbsolute() ) {
                Iterator nodeIter = context.getNodeSet().iterator();
                while ( nodeIter.hasNext() ) {
                    Object each = nodeIter.next();  
                    
                    if ( each instanceof Document ) {
                        List results = new ArrayList(1);
                        results.add( each );
                        return results;
                    }
                    else if ( each instanceof Node ) {
                        List results = new ArrayList(1);
                        results.add( ((Node) each).getDocument() );
                        return results;
                    }
                }
            }
            return Collections.EMPTY_LIST;
        }
        
        if ( isAbsolute() ) {
            Iterator nodeIter = context.getNodeSet().iterator();
            while ( nodeIter.hasNext() ) {
                Object each = nodeIter.next();  
                if ( each instanceof Document ) {
                    context.setNodeSet( (Document) each );
                    break;
                }
                else if ( each instanceof Node ) {
                    Node node = (Node) each;
                    context.setNodeSet( node.getDocument() );
                    break;
                }
            }
        }
        
        Iterator stepIter = getSteps().iterator();
        boolean stepped = false;       
        while ( stepIter.hasNext() )  {
            Step eachStep = (Step) stepIter.next();
            if ( eachStep != null ) {
                if ( (!stepped) && isAbsolute() ) {
                    eachStep.setIsAbsolute(true);
                }
                stepped = true;

                context = eachStep.applyTo( context );
            }
        }
        
        if (stepped) {
            return context.getNodeSet();
        }        
        return Collections.EMPTY_LIST;
    }

    

    // Pattern methods
    
    public boolean matches( Context context, Node node ) {
        boolean absolute = isAbsolute();
        if ( getSteps().isEmpty() ) {
            return absolute && node instanceof Document;
        }
        
        boolean stepped = false;       
        for ( int i = _steps.size() - 1; i >= 0; i-- ) {
            Step step = (Step) _steps.get(i);
            
            if ( !stepped ) {
                if ( absolute ) {
                    step.setIsAbsolute(true);
                }
                stepped = true;
            }
            if ( context == null || node == null 
                || ! step.matches( context, node ) ) {
                return false;
            }
            context = step.nextMatchContext( context, node );
            node = (context != null ) ? context.getContextNode() : null;
        }
        return true;
    }

    public double getPriority() {
        Step step = getLastStep();
        if ( step != null ) {
            return step.getPriority();
        }
        return Pattern.DEFAULT_PRIORITY;
    }

    public String getMatchesNodeName() {
        Step step = getLastStep();
        if ( step != null ) {
            return step.getMatchesNodeName();
        }
        return null;
    }
    
    public short getMatchType() {
        Step step = getLastStep();
        if ( step != null ) {
            return step.getMatchType();
        }
        return Pattern.ANY_NODE;
    }

    
    public String toString() {
        return "[LocationPath: " + _steps + " ]";
    }
    
    // Implementation methods
    
    protected Step getLastStep() {
        if ( _steps != null ) {
            int index = _steps.size() - 1;
            if ( index >= 0 ) {
                return (Step) _steps.get(index);
            }
        }
        return null;
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
