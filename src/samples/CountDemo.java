
import org.dom4j.*;

/** A sample program to count the number of various kinds of DOM4J node types
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class CountDemo extends SAXDemo {
    
    private int numCharacters;
    private int numComments;
    private int numElements;
    private int numAttributes;
    private int numProcessingInstructions;
    
    
    public CountDemo() {
    }
        
    /** The program entry point.
      *
      * @param args the command line arguments
      */
    public static void main(String[] args) {
        run( new CountDemo(), args );
    }    
    
    protected void process(Document document) throws Exception {
        numCharacters = 0;
        numComments = 0;
        numElements = 0;
        numAttributes = 0;
        numProcessingInstructions = 0;
        
        
        TreeVisitor visitor = new TreeVisitorSupport() {
            
            public void visit(Element node) {
                ++numElements;
            }

            public void visit(Attribute node) {
                ++numAttributes;
            }

            public void visit(Comment node) {
                ++numComments;
            }

            public void visit(ProcessingInstruction node) {
                ++numProcessingInstructions;
            }

            public void visit(Text node) {
                String text = node.getText();
                if ( text != null ) {
                    numCharacters += text.length();
                }
            }
        };
        
        println( "Document: " + document.getName() + " has the following" );
        println(
            "Elements\tAttributes\tComments\tPIs\tCharacters"
        );

        document.accept( visitor );
        
        println( 
            numElements + "\t\t" + numAttributes + "\t\t" + numComments + "\t\t" 
            + numProcessingInstructions + "\t" + numCharacters
        );
    }
}