/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** <p><code>Element</code> interface defines an XML element.
  * An element can have declared namespaces, attributes, child nodes and 
  * textual content.</p>
  *
  * <p>Some of this interface is optional. 
  * Some implementations may be read-only and not support being modified.
  * Some implementations may not support the parent relationship and methods
  * such as {@link #getParent} or {@link #getDocument}.</p>
  *
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public interface Element extends Branch {

    // Name and namespace related methods
    //-------------------------------------------------------------------------        
 
    /** <p>Returns the <code>QName</code> of this element which represents 
      * the local name, the qualified name and the <code>Namespace</code>.</p>
      *
      * @return the <code>QName</code> associated with this element
      */
    public QName getQName();    

    /** <p>Sets the <code>QName</code> of this element which represents 
      * the local name, the qualified name and the <code>Namespace</code>.</p>
      *
      * @param qname is the <code>QName</code> to be associated with this element
      */
    public void setQName(QName qname);    
    
    /** <p>Returns the <code>Namespace</code> of this element if one exists 
      * otherwise <code>Namespace.NO_NAMESPACE</code> is returned.</p>
      *
      * @return the <code>Namespace</code> associated with this element
      */
    public Namespace getNamespace();

    
 
    /** <p>Returns the <code>QName</code> for the given qualified name, using
      * the namespace URI in scope for the given prefix of the qualified name
      * or the default namespace if the qualified name has no prefix.</p>
      *
      * @return the <code>QName</code> for the given qualified name
      */
    public QName getQName(String qualifiedName);

 
    /** <p>Returns the <code>Namespace</code> which is mapped to the given
      * prefix or null if it could not be found.</p>
      *
      * @return the <code>Namespace</code> associated with the given prefix
      */
    public Namespace getNamespaceForPrefix(String prefix);

    /** <p>Returns the <code>Namespace</code> which is mapped to the given
      * URI or null if it could not be found.</p>
      *
      * @return the <code>Namespace</code> associated with the given URI
      */
    public Namespace getNamespaceForURI(String uri);
    
    /** <p>Returns the namespace prefix of this element if one exists 
      * otherwise an empty <code>String</code> is returned.</p>
      *
      * @return the prefix of the <code>Namespace</code> of this element 
      * or an empty <code>String</code>
      */
    public String getNamespacePrefix();

    /** <p>Returns the URI mapped to the namespace of this element 
      * if one exists otherwise an empty <code>String</code> is returned.</p>
      *
      * @return the URI for the <code>Namespace</code> of this element 
      * or an empty <code>String</code>
      */
    public String getNamespaceURI();

    /** <p>Returns the fully qualified name of this element. 
      * This will be the same as the value returned from {@link #getName}
      * if this element has no namespace attached to this element or an
      * expression of the form
      * <pre>
      * getNamespacePrefix() + ":" + getName()
      * </pre>
      * will be returned.
      *
      * @return the fully qualified name of the element.
      */
    public String getQualifiedName();
    
    
   /** <p>Returns any additional namespaces declarations for this element 
      * other than namespace returned via the {@link #getNamespace()} method. 
      * If no additional namespace declarations are present for this
      * element then an empty list will be returned.
      * 
      * The list is backed by the element such that changes to the list will
      * be reflected in the element though the reverse is not the case.</p>
      *
      * @return a list of any additional namespace declarations.
      */
    public List additionalNamespaces();
    
   /** <p>Returns all the namespaces declared by this element. 
      * If no namespaces are declared for this element then 
      * an empty list will be returned.
      * 
      * The list is backed by the element such that changes to the list will
      * be reflected in the element though the reverse is not the case.</p>
      *
      * @return a list of namespaces declared for this element.
      */
    public List declaredNamespaces();


    
    // Builder methods 
    //-------------------------------------------------------------------------        

    /** <p>Adds the attribute value of the given local name. 
      * If an attribute already exists for the given name it will be replaced.
      * Attributes with null values are silently ignored.
      * If the value of the attribute is null then this method call will 
      * remove any attributes with the given name.</p>
      *
      * @param name is the name of the attribute whose value is to be added 
      * or updated
      * @param value is the attribute's value
      * @return this <code>Element</code> instance.
      */
    public Element addAttribute(String name, String value);
    
    /** <p>Adds the attribute value of the given fully qualified name. 
      * If an attribute already exists for the given name it will be replaced.
      * Attributes with null values are silently ignored.
      * If the value of the attribute is null then this method call will 
      * remove any attributes with the given name.</p>
      *
      * @param qName is the fully qualified name of the attribute 
      * whose value is to be added or updated
      * @param value is the attribute's value
      * @return this <code>Element</code> instance.
      */
    public Element addAttribute(QName qName, String value);
    
    /** Adds a new <code>Comment</code> node with the given text to this element.
      *
      * @param comment is the text for the <code>Comment</code> node.
      * @return this <code>Element</code> instance.
      */    
    public Element addComment(String comment);
    
    
    /** Adds a new <code>CDATA</code> node with the given text to this element.
      *
      * @param cdata is the text for the <code>CDATA</code> node.
      * @return this <code>Element</code> instance.
      */    
    public Element addCDATA(String cdata);
    
    /** Adds a new <code>Entity</code> node with the given name and text
      * to this element and returns a reference to the new node.
      *
      * @param name is the name for the <code>Entity</code> node.
      * @param text is the text for the <code>Entity</code> node.
      * @return this <code>Element</code> instance.
      */    
    public Element addEntity(String name, String text);
    
    
    /** Adds a namespace to this element for use by its child content
      *
      * @param prefix is the prefix to use, which should not be null or blank
      * @param uri is the namespace URI
      * @return this <code>Element</code> instance.
      */
    public Element addNamespace(String prefix, String uri);

    /** Adds a processing instruction for the given target
      *
      * @param target is the target of the processing instruction
      * @param text is the textual data (key/value pairs) of the processing instruction
      * @return this <code>Element</code> instance.
      */
    public Element addProcessingInstruction(String target, String text);
    
    /** Adds a processing instruction for the given target
      *
      * @param target is the target of the processing instruction
      * @param data is a Map of the key / value pairs of the processing instruction
      * @return this <code>Element</code> instance.
      */
    public Element addProcessingInstruction(String target, Map data);

    /** Adds a new <code>Text</code> node with the given text to this element.
      *
      * @param text is the text for the <code>Text</code> node.
      * @return this <code>Element</code> instance.
      */    
    public Element addText(String text);    
    
    
    // Typesafe modifying methods
    //-------------------------------------------------------------------------        
    
    
    /** Adds the given <code>Attribute</code> to this element.
      * If the given node already has a parent defined then an
      * <code>InvalidAddNodeException</code> will be thrown.
      * Attributes with null values are silently ignored.
      * If the value of the attribute is null then this method call will 
      * remove any attributes with the QName of this attribute.</p>
      *
      * @param attribute is the attribute to be added
      */
    public void add(Attribute attribute);
    
    
    /** Adds the given <code>CDATA</code> to this element.
      * If the given node already has a parent defined then an
      * <code>InvalidAddNodeException</code> will be thrown.
      *
      * @param cdata is the CDATA to be added
      */
    public void add(CDATA cdata);
    
    /** Adds the given <code>Entity</code> to this element.
      * If the given node already has a parent defined then an
      * <code>InvalidAddNodeException</code> will be thrown.
      *
      * @param entity is the entity to be added
      */
    public void add(Entity entity);
    
    /** Adds the given <code>Text</code> to this element.
      * If the given node already has a parent defined then an
      * <code>InvalidAddNodeException</code> will be thrown.
      *
      * @param text is the text to be added
      */
    public void add(Text text);
    
    /** Adds the given <code>Namespace</code> to this element.
      * If the given node already has a parent defined then an
      * <code>InvalidAddNodeException</code> will be thrown.
      *
      * @param namespace is the namespace to be added
      */
    public void add(Namespace namespace);
        
    /** Removes the given <code>Attribute</code> from this element.
      *
      * @param attribute is the attribute to be removed
      * @return true if the attribute was removed
      */
    public boolean remove(Attribute attribute);
    
    /** Removes the given <code>CDATA</code> if the node is 
      * an immediate child of this element. 
      *
      * If the given node is not an immediate child of this element
      * then the {@link Node#detach()} method should be used instead.
      *
      * @param cdata is the CDATA to be removed
      * @return true if the cdata was removed
      */
    public boolean remove(CDATA cdata);
    
    /** Removes the given <code>Entity</code> if the node is 
      * an immediate child of this element. 
      *
      * If the given node is not an immediate child of this element
      * then the {@link Node#detach()} method should be used instead.
      *
      * @param entity is the entity to be removed
      * @return true if the entity was removed
      */
    public boolean remove(Entity entity);
    
    /** Removes the given <code>Namespace</code> if the node is 
      * an immediate child of this element. 
      *
      * If the given node is not an immediate child of this element
      * then the {@link Node#detach()} method should be used instead.
      *
      * @param namespace is the namespace to be removed
      * @return true if the namespace was removed
      */
    public boolean remove(Namespace namespace);
    
    /** Removes the given <code>Text</code> if the node is 
      * an immediate child of this element. 
      *
      * If the given node is not an immediate child of this element
      * then the {@link Node#detach()} method should be used instead.
      *
      * @param text is the text to be removed
      * @return true if the text was removed
      */
    public boolean remove(Text text);

    
    // Text methods
    //-------------------------------------------------------------------------        
    
    /** Returns the text value of this element without recursing through
      * child elements. 
      * This method iterates through all {@link Text}, {@link CDATA} and 
      * {@link Entity} nodes that this element contains 
      * and appends the text values together.
      * 
      * @return the textual content of this Element. Child elements are not navigated.
      */
    public String getText();    
   
    /** @return the trimmed text value where whitespace is trimmed and
      * normalised into single spaces
      */
    public String getTextTrim();

    
    /** Returns the XPath string-value of this node. 
      * The behaviour of this method is defined in the 
      * <a href="http://www.w3.org/TR/xpath">XPath specification</a>.
      *
      * This method returns the string-value of all the contained 
      * {@link Text}, {@link CDATA}, {@link Entity} and {@link Element} nodes 
      * all appended together.
      * 
      * @return the text from all the child Text and Element nodes appended 
      * together.
      */
    public String getStringValue();    


    /** Accesses the data of this element which may implement data typing 
      * bindings such as XML Schema or 
      * Java Bean bindings or will return the same value as {@link #getText}
      */
    public Object getData();
    
    /** Sets the data value of this element if this element supports data 
      * binding or calls {@link #setText} if it doesn't
      */
    public void setData(Object data);

    
    // Attribute methods
    //-------------------------------------------------------------------------        
    

    /** <p>Returns the {@link Attribute} instances this element contains as 
      * a backed {@link List} so that the attributes may be modified directly 
      * using the {@link List} interface.
      * The <code>List</code> is backed by the <code>Element</code> so that
      * changes to the list are reflected in the element and vice versa.</p>
      *
      * @return the attributes that this element contains as a <code>List</code>
      */    
    public List attributes();
    
    /** Sets the attributes that this element contains
      */
    public void setAttributes(List attributes);

    /** @return the number of attributes this element contains
      */
    public int attributeCount();
    
    /** @returns an iterator over the attributes of this element
      */
    public Iterator attributeIterator();
    
    /** Returns the attribute at the specified indexGets the 
      *
      * @return the attribute at the specified index where 
      * index >= 0 and index < number of attributes or throws
      * an IndexOutOfBoundsException if the index is not within the 
      * allowable range
      */
    public Attribute attribute(int index);
            
    /** Returns the attribute with the given name
      *
      * @return the attribute for the given local name in any namespace.
      * If there are more than one attributes with the given local name 
      * in different namespaces then the first one is returned.
      */
    public Attribute attribute(String name);
    
    /** @param qName is the fully qualified name
      * @return the attribute for the given fully qualified name or null if 
      * it could not be found.
      */
    public Attribute attribute(QName qname);

    /** <p>This returns the attribute value for the attribute with the 
      * given name and any namespace or null if there is no such 
      * attribute or the empty string if the attribute value is empty.</p>
      *
      * @param name is the name of the attribute value to be returnd
      * @return the value of the attribute, null if the attribute does 
      * not exist or the empty string
      */
    public String attributeValue(String name);

    /** <p>This returns the attribute value for the attribute with the 
      * given name and any namespace or the default value if there is 
      * no such attribute value.</p>
      *
      * @param name is the name of the attribute value to be returnd
      * @param defaultValue is the default value to be returned if the 
      *    attribute has no value defined.
      * @return the value of the attribute or the defaultValue if the 
      *    attribute has no value defined.
      */
    public String attributeValue(String name, String defaultValue);

    /** <p>This returns the attribute value for the attribute with the 
      * given fully qualified name or null if there is no such 
      * attribute or the empty string if the attribute value is empty.</p>
      *
      * @param qName is the fully qualified name
      * @return the value of the attribute, null if the attribute does 
      * not exist or the empty string
      */
    public String attributeValue(QName qName);

    /** <p>This returns the attribute value for the attribute with the 
      * given fully qualified name or the default value if 
      * there is no such attribute value.</p>
      *
      * @param qName is the fully qualified name
      * @param defaultValue is the default value to be returned if the 
      *    attribute has no value defined.
      * @return the value of the attribute or the defaultValue if the 
      *    attribute has no value defined.
      */
    public String attributeValue(QName qName, String defaultValue);

    
    /** <p>Sets the attribute value of the given local name.</p>
      *
      * @param name is the name of the attribute whose value is to be added 
      * or updated
      * @param value is the attribute's value
      *
      * @deprecated As of version 0.5. Please use 
      *    {@link #addAttribute(String,String)} instead.
      */
    public void setAttributeValue(String name, String value);
    
    /** <p>Sets the attribute value of the given fully qualified name.</p>
      *
      * @param qName is the fully qualified name of the attribute 
      * whose value is to be added or updated
      * @param value is the attribute's value
      *
      * @deprecated As of version 0.5. Please use 
      *    {@link #addAttribute(QName,String)} instead.
      */
    public void setAttributeValue(QName qName, String value);

    
    // Content methods
    //-------------------------------------------------------------------------        
    
    
    /** Returns the first element for the given local name and any namespace.
      * 
      * @return the first element with the given local name 
      */
    public Element element(String name);
    
    /** Returns the first element for the given fully qualified name.
      * 
      * @param qName is the fully qualified name to search for
      * @return the first element with the given fully qualified name
      */
    public Element element(QName qname);

    /** <p>Returns the elements contained in this element. 
      * If this element does not contain any elements then this method returns
      * an empty list.
      *
      * The list is backed by the element such that changes to the list will
      * be reflected in the element though the reverse is not the case.</p>
      *
      * @return a list of all the elements in this element.
      */
    public List elements();
    
    /** <p>Returns the elements contained in this element with the given 
      * local name and any namespace.
      * If no elements are found then this method returns an empty list.
      *
      * The list is backed by the element such that changes to the list will
      * be reflected in the element though the reverse is not the case.</p>
      *
      * @return a list of all the elements in this element for the given 
      * local name
      */
    public List elements(String name);
    
    /** <p>Returns the elements contained in this element with the given 
      * fully qualified name.
      * If no elements are found then this method returns an empty list.
      *
      * The list is backed by the element such that changes to the list will
      * be reflected in the element though the reverse is not the case.</p>
      *
      * @param qName is the fully qualified name to search for
      * @return a list of all the elements in this element for the 
      * given fully qualified name.
      */
    public List elements(QName qName);
    
    /** Returns an iterator over all this elements child elements.
      *
      * @return an iterator over the contained elements
      */
    public Iterator elementIterator();
    
    /** Returns an iterator over the elements contained in this element
      * which match the given local name and any namespace.
      *
      * @return an iterator over the contained elements matching the given 
      * local name
      */
    public Iterator elementIterator(String name);
    
    /** Returns an iterator over the elements contained in this element
      * which match the given fully qualified name.
      *
      * @param qName is the fully qualified name to search for
      * @return an iterator over the contained elements matching the given 
      * fully qualified name
      */
    public Iterator elementIterator(QName qname);
        
    

    // Helper methods
    //-------------------------------------------------------------------------        
 
    /** @return true if this element is the root element of a document
      * and this element supports the parent relationship else false.
      */
    public boolean isRootElement();

    /** <p>Returns true if this <code>Element</code> has mixed content.
      * Mixed content means that an element contains both textual data and
      * child elements.
      *
      * @return true if this element contains mixed content.
      */
    public boolean hasMixedContent();    
        
    /** <p>Returns true if this <code>Element</code> has text only content.
      *
      * @return true if this element is empty or only contains text content.
      */
    public boolean isTextOnly();    
        
    
    /** Appends the attributes of the given element to me.
      * This method behaves like the {@link Collection#addAll(java.util.Collection)} 
      * method.
      *
      * @param element is the element whose attributes will be added to me.
      */
    public void appendAttributes(Element element);
    
    /** <p>Creates a deep copy of this element 
      * The new element is detached from its parent, and getParent() on the 
      * clone will return null.</p>
      *
      * @return a new deep copy Element
      */
    public Element createCopy();
    
    /** <p>Creates a deep copy of this element with the given local name
      * The new element is detached from its parent, and getParent() on the 
      * clone will return null.</p>
      *
      * @return a new deep copy Element
      */
    public Element createCopy(String name);
    
    /** <p>Creates a deep copy of this element with the given fully qualified name.
      * The new element is detached from its parent, and getParent() on the 
      * clone will return null.</p>
      *
      * @return a new deep copy Element
      */
    public Element createCopy(QName qName);
    
    public String elementText(String name);
    public String elementText(QName qname);
    public String elementTextTrim(String name);
    public String elementTextTrim(QName qname);
    
    /** Returns a node at the given index suitable for an XPath result set.
      * This means the resulting Node will either be null or it will support 
      * the parent relationship.
      *
      * @return the Node for the given index which will support the parent 
      * relationship or null if there is not a node at the given index.
      */
    public Node getXPathResult(int index);
    
    
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
