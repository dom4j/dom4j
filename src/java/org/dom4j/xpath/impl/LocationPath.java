
package org.dom4j.xpath.impl;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.rule.Pattern;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


public class LocationPath extends PathExpr {
    
    private boolean _isAbsolute  = false;
    private List    _steps       = null;
    
    public LocationPath() {        
    }
    
    public void setIsAbsolute(boolean isAbsolute) {
        _isAbsolute = isAbsolute;
    }
    
    public boolean isAbsolute() {
        return _isAbsolute;
    }
    
    public LocationPath addStep(Step step) {
        if ( _steps == null ) {
            _steps = new ArrayList();
        }
        _steps.add(step);        
        return this;
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
                    else if ( each instanceof Element ) {
                        List results = new ArrayList(1);
                        results.add( ((Element)each).getDocument() );
                        return results;
                    }
                }
            }
            else {
                return Collections.EMPTY_LIST;
            }
        }
        
        Iterator stepIter = getSteps().iterator();
        boolean stepped = false;       
        while ( stepIter.hasNext() )  {
            Step eachStep = (Step) stepIter.next();
            
            if ( (!stepped) && isAbsolute() ) {
                eachStep.setIsAbsolute(true);
            }
            stepped = true;
            
            context = eachStep.applyTo( context );
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
