
import org.dom4j.*;

/** A sample program to demonstrate the use of the Visitor Pattern in DOM4J
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class VisitorDemo extends SAXDemo {
    
    public VisitorDemo() {
    }
        
    /** The program entry point.
      *
      * @param args the command line arguments
      */
    public static void main(String[] args) {
        run( new VisitorDemo(), args );
    }    
    
    protected void process(Document document) throws Exception {
        TreeVisitor visitor = new TreeVisitorSupport() {
            
            public void visit(Document document) {
                println( document.toString() );
            }

            public void visit(DocumentType documentType) {
                println( documentType.toString() );
            }
    
            public void visit(Element node) {
                println( node.toString() );
            }

            public void visit(Attribute node) {
                println( node.toString() );
            }

            public void visit(CDATA node) {
                println( node.toString() );
            }

            public void visit(Comment node) {
                println( node.toString() );
            }

            public void visit(Entity node) {
                println( node.toString() );
            }

            public void visit(Namespace node) {
                println( node.toString() );
            }

            public void visit(ProcessingInstruction node) {
                println( node.toString() );
            }

            public void visit(Text node) {
                println( node.toString() );
            }
        };
        
        document.accept( visitor );
    }
}