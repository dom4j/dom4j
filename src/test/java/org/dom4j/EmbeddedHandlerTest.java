/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j;

import org.dom4j.io.SAXReader;
import org.testng.Assert;
import org.testng.AssertJUnit;

import java.io.File;

/**
 * TestEmbeddedHandler
 *
 * @author <a href="mailto:franz.beil@temis-group.com">FB </a>
 * @author Filip Jirsák
 */
public class EmbeddedHandlerTest extends AbstractTestCase {
	protected String[] testDocuments = {"xml/test/FranzBeilMain.xml"};

	public String testMainReader() throws Exception {
		return readDocuments(ReaderType.MAIN_READER);
	}

	public String testOnEndReader() throws Exception {
		return readDocuments(ReaderType.ON_END_READER);
	}

	public void testBothReaders() throws Exception {
		String main = testMainReader();
		String onEnd = testOnEndReader();

		Assert.assertEquals(main, onEnd, "Results of tests should be equal.");
	}

	private String readDocuments(ReaderType readerType) throws Exception {
		StringBuilder result = new StringBuilder();
		for (String testDocument : testDocuments) {
			File testDoc = getFile(testDocument);
			String mainDir = testDoc.getParent();
			SAXReader reader = new SAXReader();
			MainHandler mainHandler = new MainHandler(readerType, mainDir);
			reader.addHandler("/main/import", mainHandler);
			getDocument(testDocument, reader);
			result.append(mainHandler.getResult());
		}
		return result.toString();
	}

	class MainHandler implements ElementHandler {
		private final SAXReader mainReader;
		private final String mainDir;
		private final StringBuilder result;
		private final ReaderType readerType;

		public MainHandler(ReaderType readerType, String dir) {
			this.readerType = readerType;
			mainReader = new SAXReader();
			mainDir = dir;
			result = new StringBuilder();
			mainReader.addHandler("/import/stuff", new EmbeddedHandler(result));
		}

		public void onStart(ElementPath path) {
		}

		public void onEnd(ElementPath path) {
			String href = path.getCurrent().attribute("href").getValue();
			Element importRef = path.getCurrent();
			Element parentElement = importRef.getParent();
			SAXReader onEndReader = new SAXReader();
			onEndReader.addHandler("/import/stuff", new EmbeddedHandler(result));

			File file = new File(mainDir + File.separator + href);
			Element importElement = null;

			try {
				switch (readerType) {
					case MAIN_READER:
						importElement = mainReader.read(file).getRootElement();
						break;
					case ON_END_READER:
						importElement = onEndReader.read(file).getRootElement();
						break;
					default:
						throw new IllegalArgumentException(String.format("Illegal type of reader: %s", readerType));
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}

			// prune and replace
			importRef.detach();
			parentElement.add(importElement);
		}

		public StringBuilder getResult() {
			return result;
		}
	}

	class EmbeddedHandler implements ElementHandler {
		private final StringBuilder result;

		EmbeddedHandler(StringBuilder result) {
			this.result = result;
		}

		public void onStart(ElementPath path) {
			result.append(path.getCurrent().attribute("name").getValue());
			result.append('\n');
		}

		public void onEnd(ElementPath path) {
		}
	}

	private enum ReaderType {
		MAIN_READER,
		ON_END_READER,;
	}
}

/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * 3. The name "DOM4J" must not be used to endorse or promote products derived
 * from this Software without prior written permission of MetaStuff, Ltd. For
 * written permission, please contact dom4j-info@metastuff.com.
 * 
 * 4. Products derived from this Software may not be called "DOM4J" nor may
 * "DOM4J" appear in their names without prior written permission of MetaStuff,
 * Ltd. DOM4J is a registered trademark of MetaStuff, Ltd.
 * 
 * 5. Due credit should be given to the DOM4J Project - http://www.dom4j.org
 * 
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL METASTUFF, LTD. OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 */
