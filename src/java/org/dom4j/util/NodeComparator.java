/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.util;

import java.util.Comparator;

import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.CDATA;
import org.dom4j.CharacterData;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.ProcessingInstruction;
import org.dom4j.QName;
import org.dom4j.Text;

/** <p><code>NodeComparator</code> is a {@link Comparator} of Node instances
  * which is capable of comparing Nodes for equality based on their values.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class NodeComparator implements Comparator {
    
    /**
     * Compares its two arguments for order.  Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.<p>
     *
     * The implementor must ensure that <tt>sgn(compare(x, y)) ==
     * -sgn(compare(y, x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>compare(x, y)</tt> must throw an exception if and only
     * if <tt>compare(y, x)</tt> throws an exception.)<p>
     *
     * The implementor must also ensure that the relation is transitive:
     * <tt>((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0))</tt> implies
     * <tt>compare(x, z)&gt;0</tt>.<p>
     *
     * Finally, the implementer must ensure that <tt>compare(x, y)==0</tt>
     * implies that <tt>sgn(compare(x, z))==sgn(compare(y, z))</tt> for all
     * <tt>z</tt>.<p>
     *
     * It is generally the case, but <i>not</i> strictly required that 
     * <tt>(compare(x, y)==0) == (x.equals(y))</tt>.  Generally speaking,
     * any comparator that violates this condition should clearly indicate
     * this fact.  The recommended language is "Note: this comparator
     * imposes orderings that are inconsistent with equals."
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     *         first argument is less than, equal to, or greater than the
     *         second. 
     * @throws ClassCastException if the arguments' types prevent them from
     *         being compared by this Comparator.
     */
    public int compare(Object o1, Object o2) {
        if ( o1 == o2 ) {
            return 0;
        }
        else if ( o1 == null ) {
            // null is less
            return -1;
        }
        else if ( o2 == null ) {
            return 1;
        }
        if ( o1 instanceof Node ) { 
            if ( o2 instanceof Node ) {
                return compare( (Node) o1, (Node) o2 );
            }
            else {
                // Node implementations are greater
                return 1;
            }
        }
        else {
            if ( o2 instanceof Node ) {
                // Node implementations are greater
                return -1;
            }
            else {
                if ( o1 instanceof Comparable ) {
                    Comparable c1 = (Comparable) o1;
                    return c1.compareTo( o2 );
                }
                else {
                    int answer = o1.getClass().getName().compareTo( 
                        o2.getClass().getName() 
                    );
                    if ( answer == 0 ) {
                        answer = o1.hashCode() - o2.hashCode();
                    }
                    return answer;
                }
            }
        }
    }
    
    public int compare( Node n1, Node n2 ) {
        int nodeType1 = n1.getNodeType();
        int nodeType2 = n2.getNodeType();
        int answer = nodeType1 - nodeType2;
        if ( answer != 0 ) {
            return answer;
        }
        else {
            switch (nodeType1) {
                case Node.ELEMENT_NODE:
                    return compare((Element) n1, (Element) n2);
                case Node.DOCUMENT_NODE:
                    return compare((Document) n1, (Document) n2);
                case Node.ATTRIBUTE_NODE:
                    return compare((Attribute) n1, (Attribute) n2);
                case Node.TEXT_NODE:
                    return compare((Text) n1, (Text) n2);
                case Node.CDATA_SECTION_NODE:
                    return compare((CDATA) n1, (CDATA) n2);
                case Node.ENTITY_REFERENCE_NODE:
                    return compare((Entity) n1, (Entity) n2);
                case Node.PROCESSING_INSTRUCTION_NODE:
                    return compare((ProcessingInstruction) n1, (ProcessingInstruction) n2);
                case Node.COMMENT_NODE:
                    return compare((Comment) n1, (Comment) n2);
                case Node.DOCUMENT_TYPE_NODE:
                    return compare((DocumentType) n1, (DocumentType) n2);
                case Node.NAMESPACE_NODE:
                    return compare((Namespace) n1, (Namespace) n2);
                default:
                    throw new RuntimeException( 
                        "Invalid node types. node1: " + n1 + " and node2: " + n2 
                    );
                }
        }
    }
    
    public int compare( Document n1, Document n2 ) {
        int answer = compare( n1.getDocType(), n2.getDocType() );
        if ( answer == 0 ) {
            answer = compareContent( n1, n2 );
        }
        return answer;
    }
    
    public int compare( Element n1, Element n2 ) {
        int answer = compare( n1.getQName(), n2.getQName() );
        if ( answer == 0 ) {
            // lets compare attributes
            int c1 = n1.attributeCount();
            int c2 = n2.attributeCount();
            answer = c1 - c2;
            if ( answer == 0 ) {
                for ( int i = 0; i < c1; i++ ) {
                    Attribute a1 = n1.attribute(i);
                    Attribute a2 = n2.attribute(i);
                    answer = compare( a1, a2 );
                    if ( answer != 0 ) {
                        return answer;
                    }
                }
                answer = compareContent( n1, n2 );
            }
        }
        return answer;
    }
    
    public int compare( Attribute n1, Attribute n2 ) {
        int answer = compare( n1.getQName(), n2.getQName() );
        if ( answer == 0 ) {
            answer = compare( n1.getValue(), n2.getValue() );
        }
        return answer;
    }
    
    public int compare( QName n1, QName n2 ) {
        int answer = compare( n1.getNamespaceURI(), n2.getNamespaceURI() );
        if ( answer == 0 ) {
            answer = compare( n1.getQualifiedName(), n2.getQualifiedName() );
        }
        return answer;
    }
    
    public int compare( Namespace n1, Namespace n2 ) {
        int answer = compare( n1.getURI(), n2.getURI() );
        if ( answer == 0 ) {
            answer = compare( n1.getPrefix(), n2.getPrefix() );
        }
        return answer;
    }
    
    public int compare( CharacterData t1, CharacterData t2 ) {
        return compare( t1.getText(), t2.getText() );
    }
    
    public int compare( DocumentType o1, DocumentType o2 ) {
        if ( o1 == o2 ) {
            return 0;
        }
        else if ( o1 == null ) {
            // null is less
            return -1;
        }
        else if ( o2 == null ) {
            return 1;
        }
        int answer = compare( o1.getPublicID(), o2.getPublicID() );
        if ( answer == 0 ) {
            answer = compare( o1.getSystemID(), o2.getSystemID() );
            if ( answer == 0 ) {
                answer = compare( o1.getName(), o2.getName() );
            }
        }
        return answer;
    }
    
    public int compare( Entity n1, Entity n2 ) {
        int answer = compare( n1.getName(), n2.getName() );
        if ( answer == 0 ) {
            answer = compare( n1.getText(), n2.getText() );
        }
        return answer;
    }
    
    public int compare( ProcessingInstruction n1, ProcessingInstruction n2 ) {
        int answer = compare( n1.getTarget(), n2.getTarget() );
        if ( answer == 0 ) {
            answer = compare( n1.getText(), n2.getText() );
        }
        return answer;
    }
    
    public int compareContent( Branch b1, Branch b2 ) {
        int c1 = b1.nodeCount();
        int c2 = b2.nodeCount();
        int answer = c1 - c2;
        if ( answer == 0 ) {
            for ( int i = 0; i < c1; i++ ) {
                Node n1 = b1.node(i);
                Node n2 = b2.node(i);
                answer = compare( n1, n2 );
                if ( answer != 0 ) {
                    break;
                }
            }
        }
        return answer;
    }
    
    public int compare( String o1, String o2 ) {
        if ( o1 == o2 ) {
            return 0;
        }
        else if ( o1 == null ) {
            // null is less
            return -1;
        }
        else if ( o2 == null ) {
            return 1;
        }
        return o1.compareTo( o2 );
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
