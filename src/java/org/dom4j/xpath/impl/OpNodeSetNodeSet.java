
package org.dom4j.xpath.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

class OpNodeSetNodeSet extends Operator 
{
    static Object evaluate( 
        Context context, Op op, Object lhsValue, Object rhsValue 
    ) {        
        Object result = null;
        // We should *know* absolutely that these are both Lists.
        
        List lhs = (List) lhsValue;
        List rhs = (List) rhsValue;
        
        List lhsStrList = new ArrayList(lhs.size());
        String tmpValue = null;
        
        Iterator itemIter = lhs.iterator();
        
        while ( itemIter.hasNext() ) {
            lhsStrList.add( convertToString( itemIter.next() ) );
        }
        
        Collections.sort(lhsStrList);
        
        itemIter = rhs.iterator();
        
        int index = -1;
        
        while ( itemIter.hasNext() ) {
            tmpValue = convertToString( itemIter.next() );
            
            index = Collections.binarySearch(lhsStrList,
            tmpValue);
            
            if ( op == Op.EQUAL ) {
                // We found one, equality achieved
                if (index >= 0) {
                    return Boolean.TRUE;
                }
            }
            else if ( op == Op.NOT_EQUAL ) {
                // We didn't find one
                if (index < 0) {
                    return Boolean.TRUE;
                }
            }
        }        
        return Boolean.FALSE;
    }
}
