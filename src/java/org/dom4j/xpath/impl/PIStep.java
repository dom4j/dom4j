
package org.dom4j.xpath.impl;

import org.dom4j.Node;
import org.dom4j.ProcessingInstruction;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

public class PIStep extends NodeTypeStep {
    
    private String _target;
    
    public PIStep(String axis, String target) {
        super(axis, null);
        _target = target;
    }
    
    protected boolean matches(Object node) {        
        if ( node instanceof ProcessingInstruction ) {
            if ( _target == null ) {
                return true;
            }
            else {
                ProcessingInstruction pi = (ProcessingInstruction) node;
                return _target.equals( pi.getTarget() );
            }
        }
        return false;
    }
    
    public boolean matches( Context context, Node node ) {
        if ( node instanceof ProcessingInstruction ) {
            if ( _target == null ) {
                return true;
            }
            else {
                ProcessingInstruction pi = (ProcessingInstruction) node;
                return _target.equals( pi.getTarget() );
            }
        }
        return false;
    }

    public short getMatchType() {
        return Node.PROCESSING_INSTRUCTION_NODE;
    }

    public double getPriority() {
        // If the pattern has the form of a QName preceded by a 
        // ChildOrAttributeAxisSpecifier or has the form 
        // processing-instruction(Literal) preceded by a 
        // ChildOrAttributeAxisSpecifier, then the priority is 0.
        return 0;
    }
    
    public String toString() {
        return "[PIStep [ target: " + _target + " ]]";
    }
}
