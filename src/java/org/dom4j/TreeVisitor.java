package org.dom4j;

/** <p><code>TreeVisitor</code> is used to implement the <code>Visitor</code> 
  * pattern in DOM4J.
  * An object of this interface can be passed to a <code>Node</code> which will 
  * then call its typesafe methods.
  * Please refer to the <i>Gang of Four</i> book of Design Patterns
  * for more details on the <code>Visitor</code> pattern.</p>
  *
  * <p> This 
  * <a href="http://rampages.onramp.net/~huston/dp/patterns.html">article</a>
  * has further discussion on design patterns and links to the GOF book.
  * This <a href="http://rampages.onramp.net/~huston/dp/visitor.html">link</a>
  * describes the Visitor pattern in detail.
  * </p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public interface TreeVisitor {

    /** <p>Visits the given <code>Document</code></p>
      *
      * @param node is the <code>Document</code> node to visit.
      */
    public void visit(Document document);

    /** <p>Visits the given <code>DocumentType</code></p>
      *
      * @param node is the <code>DocumentType</code> node to visit.
      */
    public void visit(DocumentType documentType);

    /** <p>Visits the given <code>Element</code></p>
      *
      * @param node is the <code>Element</code> node to visit.
      */
    public void visit(Element node);

    /** <p>Visits the given <code>Attribute</code></p>
      *
      * @param node is the <code>Attribute</code> node to visit.
      */
    public void visit(Attribute node);

    /** <p>Visits the given <code>CDATA</code></p>
      *
      * @param node is the <code>CDATA</code> node to visit.
      */
    public void visit(CDATA node);

    /** <p>Visits the given <code>Comment</code></p>
      *
      * @param node is the <code>Comment</code> node to visit.
      */
    public void visit(Comment node);

    /** <p>Visits the given <code>Entity</code></p>
      *
      * @param node is the <code>Entity</code> node to visit.
      */
    public void visit(Entity node);

    /** <p>Visits the given <code>Namespace</code></p>
      *
      * @param node is the <code>Namespace</code> node to visit.
      */
    public void visit(Namespace namespace);

    /** <p>Visits the given <code>ProcessingInstruction</code>
     * </p>
     *
     * @param node is the <code>ProcessingInstruction</code> node to visit.
     */
    public void visit(ProcessingInstruction node);

    /** <p>Visits the given <code>Text</code>
     * </p>
     *
     * @param node is the <code>Text</code> node to visit.
     */
    public void visit(Text node);

}
