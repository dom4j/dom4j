import org.dom4j.TreeException;

/** An abstract base class for the demo programs.
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class AbstractDemo {

    public AbstractDemo() {
    }
    
    public abstract void run(String[] args) throws Exception;
    
    
    protected static void run(AbstractDemo demo, String[] args) {
        try {
            demo.run(args);
        }
        catch (TreeException e) {
            System.out.println( "Exception occurred: " + e );
            Exception nestedException = e.getNestedException();
            if ( nestedException != null ) {
                System.out.println( "NestedException: " + nestedException );
                nestedException.printStackTrace();
            }
            else {
                e.printStackTrace();
            }
        }
        catch (Throwable t) {
            System.out.println( "Exception occurred: " + t );
            t.printStackTrace();
        }
    }
    
    protected void println( String text ) {
        System.out.println( text );
    }
    
    protected void printUsage( String text ) {
        println( "Usage: java " + getClass().getName() + " " + text );
    }

}