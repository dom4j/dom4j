/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */


package org.dom4j.xpath.function;

import org.dom4j.xpath.impl.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import java.util.ArrayList;
import java.util.Enumeration;

/** <p><b>Extension Function</b> <code><i>boolean</i> matrix-concat(<i>nodeSet</i>,<i>nodeSet</i>,<i>nodeSet*</i>)</code> 
  * 
  * @author James Pereira (JPereira@CT.BBD.CO.ZA)
  */

public class MatrixConcatFunction implements Function {
    
    public Object call(Context context, List args) {
        if ( args.size() >= 2 ) {
            return evaluate(args);
        }
        return null;
    }
    
    public static Object evaluate(List list) {        
        ArrayList matrix = new ArrayList();       
        
        Iterator argIter = list.iterator();

        while (argIter.hasNext()) {
            ArrayList v = new ArrayList();
            Object obj = argIter.next();
            if (obj instanceof List) {
                List args = (List) obj;
                for ( int i = 0, size = args.size(); i < size; i++ ) {
                    v.add( StringFunction.evaluate( args.get(i) ) );
                }
            }
            else {
                v.add( StringFunction.evaluate( obj ) );
            }
            matrix.add(v);
        }
        
        ArrayList result = new ArrayList();
        Enumeration elemList = new MatrixEnum( matrix );
        while (elemList.hasMoreElements()) {
            Object obj = elemList.nextElement();
            if (obj instanceof List) {
                StringBuffer text = new StringBuffer(127);
                List args = (List) obj;
                for (Iterator it = args.iterator(); it.hasNext(); ) {
                    text.append(it.next());
                }
                result.add( text.toString() );
            }
            else {
                result.add( obj );
            }
        }
        return result;
    }
    
    public static class MatrixEnum implements Enumeration {
        private ArrayList m_source;
        private int m_maxSize = 0;
        private int m_currIdx = -1;
        
        public MatrixEnum (ArrayList _source) {
            m_source = _source;
            
            for ( Iterator iter = m_source.iterator(); iter.hasNext(); ) {
                ArrayList element = (ArrayList) iter.next();
                int size = element.size();
                if (size > m_maxSize) {
                    m_maxSize = size;
                }
            }
        }
        
        public MatrixEnum (ArrayList _source, int _maxSize) {
            m_source = _source;
            m_maxSize = _maxSize;
        }
        
        public boolean hasMoreElements() {
            if ((m_maxSize != 0) && (++m_currIdx < m_maxSize)) {
                return true;
            }
            else {
                return false;
            }
        }
        
        public Object nextElement() {
            ArrayList result = new ArrayList();
            for ( Iterator iter = m_source.iterator(); iter.hasNext(); ) {
                ArrayList element = (ArrayList) iter.next();
                int size = element.size();
                if ( m_currIdx < size ) {
                    result.add( element.get( m_currIdx ) );
                }
                else {
                    if ( size > 0 ) {
                        result.add( element.get( size - 1 ) );
                    }
                    else {
                        // XXXX: what to do now?
                        result.add( "" );
                    }
                }
            }
            return result;
        }
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
