package org.dom4j.io;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.net.URL;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.TreeException;

/** <p><code>TreeReader</code> is an abstract base class for all readers
  * of DOM4J XML trees.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class TreeReader {

    /** <code>DocumentFactory</code> used to create new document objects */
    private DocumentFactory factory = DocumentFactory.getInstance();
    
    
    
    public TreeReader() {
    }

    public TreeReader(DocumentFactory factory) {
        this.factory = factory;
    }
    
    /** @return the <code>DocumentFactory</code> used to create document objects
      */
    public DocumentFactory getDocumentFactory() {
        return factory;
    }

    /** <p>This sets the <code>DocumentFactory</code> for the <code>Builder</code>.
      * This method allows the building of custom DOM4J tree objects to be implemented
      * easily using a custom derivation of {@link DocumentFactory}</p>
      *
      * @param factory <code>DocumentFactory</code> used to create DOM4J objects
      */
    public void setDocumentFactory(DocumentFactory factory) {
        if (factory == null) {
            factory = DocumentFactory.getInstance();
        }
        this.factory = factory;
    }

    
    /** <p>Reads a Document from the given <code>File</code></p>
      *
      * @param file is the <code>File</code> to read from.
      * @return the newly created Document instance
      * @throws TreeException if an error occurs during parsing.
      * @throws FileNotFoundException if the file could not be found
      */
    public Document read(File file) throws TreeException, FileNotFoundException {
        Document document = read(new BufferedInputStream(new FileInputStream(file)));
        document.setName( file.getAbsolutePath() );
        return document;
    }
    
    
    /** <p>Reads a Document from the given <code>URL</code> using SAX</p>
      *
      * @param url <code>URL</code> to read from.
      * @return the newly created Document instance
      * @throws TreeException if an error occurs during parsing.
      */
    public Document read(URL url) throws TreeException {
        try {
            Document document = read(new BufferedInputStream(url.openStream()));
            document.setName( url.toString() );
            return document;
        }
        catch (IOException e) {
            throw new TreeException(e);
        }
    }
    
    /** <p>Reads a Document from the given stream using SAX</p>
      *
      * @param in <code>InputStream</code> to read from.
      * @return the newly created Document instance
      * @throws TreeException if an error occurs during parsing.
      */
    public abstract Document read(InputStream in) throws TreeException;


    // Implementation methods
    
    protected Document createDocument() {
        return getDocumentFactory().createDocument();
    }
}
