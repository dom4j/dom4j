package org.dom4j;

import java.io.PrintStream;
import java.io.PrintWriter;

/** <p><code>TreeException</code> is a nested Exception which may be thrown
  * during the processing of a DOM4J document.
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class TreeException extends Exception {

    /** A wrapped <code>Exception</code> */
    private Exception nestedException;
    

    public TreeException() {
        super("Error occurred in DOM4J application.");
    }

    public TreeException(String message) {
        super(message);
    }
    
    public TreeException(Exception nestedException) {
        super(nestedException.getMessage());    
        this.nestedException = nestedException;
    }    

    public TreeException(String message, Exception nestedException) {
        super(message);    
        this.nestedException = nestedException;
    }    

    public Exception getNestedException() {
        return nestedException;
    }
    
    public String getMessage() {
        if (nestedException != null) {
            return super.getMessage() + " Nested exception: " + nestedException.getMessage();
        } else {
            return super.getMessage();
        }
    }

    public void printStackTrace() {
        super.printStackTrace();
        if (nestedException != null) {
            System.err.print("Nested exception: ");
            nestedException.printStackTrace();
        }
    }

    public void printStackTrace(PrintStream out) {
        super.printStackTrace(out);
        if (nestedException != null) {
            out.println("Nested exception: ");
            nestedException.printStackTrace(out);
        }
    }

    public void printStackTrace(PrintWriter writer) {
        super.printStackTrace(writer);
        if (nestedException != null) {
            writer.println("Nested exception: ");
            nestedException.printStackTrace(writer);
        }
    }
}
