/*
 * (c) Copyright 2001 MyCorporation.
 * All Rights Reserved.
 */
package org.dom4j;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.dom4j.io.SAXReader;

/** 
 * TestEmbeddedHandler     
 * 
 * 
 * Created: Thu Mar 21 15:45:59 2002
 * 
 * @author <a href="mailto:franz.beil@temis-group.com">FB</a>
 * @version
 */
public class TestEmbeddedHandler extends AbstractTestCase {

   protected String[] testDocuments = { "xml/test/FranzBeilMain.xml", };
   final int MAIN_READER = 0;
   final int ON_END_READER = 1;
   private StringBuffer[] _results =
      { new StringBuffer(), new StringBuffer()};
   protected int _test;

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestEmbeddedHandler.class);
   }

   /**
    * Constructor for TestEmbeddedHandler.
    * @param name
    */
   public TestEmbeddedHandler(String name) {
      super(name);
   }

   //---------------------------------------------
   // Handler classes
   //---------------------------------------------

   class MainHandler implements ElementHandler {

      SAXReader _mainReader;
      String _mainDir;

      public MainHandler(String dir) {
         _mainReader = new SAXReader();
         _mainDir = dir;
         _mainReader.addHandler("/import/stuff", new EmbeddedHandler());
      }

      public void onStart(ElementPath path) {}

      public void onEnd(ElementPath path) {
         String href = path.getCurrent().attribute("href").getValue();
         Element importRef = path.getCurrent();
         Element parentElement = importRef.getParent();
         SAXReader onEndReader = new SAXReader();
         onEndReader.addHandler("/import/stuff", new EmbeddedHandler());

         File file = new File(_mainDir + File.separator + href);
         Element importElement = null;
         try {
            if (_test == MAIN_READER)
               importElement = _mainReader.read(file).getRootElement();
            else if (_test == ON_END_READER)
               importElement = onEndReader.read(file).getRootElement();
         } catch (Exception e) {
            // too bad that it's not possible to throw the exception at the caller
            e.printStackTrace();
         }

         // prune and replace
         importRef.detach();
         parentElement.add(importElement);
      }
   }

   public class EmbeddedHandler implements ElementHandler {
      public void onStart(ElementPath path) {
         _results[_test].append(
            path.getCurrent().attribute("name").getValue() + "\n");
      }
      public void onEnd(ElementPath path) {}
   }

   //---------------------------------------------
   // Test case(s)
   //---------------------------------------------

   public void testMainReader() throws Exception {
      _test = MAIN_READER;
      readDocuments();
      //        System.out.println("testMainReader()\n"+_results[_test].toString());
   }

   public void testOnEndReader() throws Exception {
      _test = ON_END_READER;
      readDocuments();
      //        System.out.println("testOnEndReader()\n"+_results[_test].toString());
   }

/* 
  
   TEMPORARILY DISABLED UNTIL THE BUG IS FIXED-----
 
   public void testBothReaders() throws Exception {
      testMainReader();
      testOnEndReader();
      if (!_results[MAIN_READER]
         .toString()
         .equals(_results[ON_END_READER].toString())) {
         StringBuffer msg = new StringBuffer();
         msg.append("Results of tests should be equal!\n");
         msg.append("Results testMainReader():\n" + _results[MAIN_READER].toString());
         msg.append(
            "Results testOnEndReader():\n" + _results[ON_END_READER].toString());
         throw new Exception(msg.toString());
      }
   }
*/

      //---------------------------------------------
      // Implementation methods
      //---------------------------------------------

      private void readDocuments() throws Exception {
         for (int i = 0; i < testDocuments.length; i++) {
            String mainDir = new File(testDocuments[i]).getParent();
            SAXReader reader = new SAXReader();
            ElementHandler mainHandler = new MainHandler(mainDir);
            reader.addHandler("/main/import", mainHandler);
            reader.read(testDocuments[i]);
         }
      }

   }
