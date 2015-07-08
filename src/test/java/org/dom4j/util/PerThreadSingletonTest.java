/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.util;

import org.dom4j.AbstractTestCase;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * PerThreadSingleton Tester.
 *
 * @author ddlucas
 * @version 1.0
 * @since <pre>
 * 01 / 05 / 2005
 * </pre>
 */
@Test
public class PerThreadSingletonTest extends AbstractTestCase {

	private static SingletonStrategy<Map<String, String>> singleton;

	private static ThreadLocal<Map<String, String>> reference = new ThreadLocal<Map<String, String>>();

	static {
		singleton = new PerThreadSingleton<Map<String, String>>();
		singleton.setSingletonClassName(HashMap.class.getName());
	}

	@Test(threadPoolSize = 5, invocationCount = 1000, timeOut = 100000L)
	public void testPerThreadSingleton() throws Exception {
		String tid = Thread.currentThread().getName();
		Map<String, String> map = singleton.instance();

		String expected = "new value";
		if (!map.containsKey(tid) && reference.get() != null) {
			System.out.println("tid=" + tid + " map=" + map);
			System.out.println("reference=" + reference);
			System.out.println("singleton=" + singleton);
			fail("created singleton more than once");
		} else {
			map.put(tid, expected);
			reference.set(map);
		}

		String actual = map.get(tid);
		// System.out.println("tid="+tid+ " map="+map);
		assertEquals("testInstance", expected, actual);

		map = singleton.instance();
		expected = "new value";
		actual = map.get(tid);
		// System.out.println("tid="+tid+ " map="+map);
		// System.out.println("reference="+reference);
		// System.out.println("singleton="+singleton);
		assertEquals("testInstance", expected, actual);
		assertEquals("testInstance reference", reference.get(), map);

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

