package org.dom4j;

/** <p><code>XPathHelper</code> contains some helper methods for using 
  * <code>{@link XPathEngine}</code> instances.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class XPathHelper {

    
    /** <p><code>getDefaultXPathEngine</code> returns the default 
      * implementation of XPathEngine using the class name that is 
      * specified in the <code>org.dom4j.XPathEngine</code> 
      * system property.</p>
      *
      * @return a new <code>XPathEngine</code> instance
      */
    public static XPathEngine getDefaultXPathEngine() {
        // let's try and class load an implementation?
        String className = System.getProperty( "org.dom4j.XPathEngine", "com.werken.xpath.DOM4JXPathEngine" );
        try {
            // I'll use the current class loader
            // that loaded me to avoid problems in J2EE and web apps
            Class theClass = Class.forName( 
                className, 
                true, 
                XPathHelper.class.getClassLoader() 
            );
            return (XPathEngine) theClass.newInstance();
        }
        catch (Throwable e) {
            System.out.println( "WARNING: Cannot load XPathEngine: " + className );
            return null;
        }
    }    
}
