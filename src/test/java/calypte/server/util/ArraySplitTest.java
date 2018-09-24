/*
 * Calypte http://calypte.uoutec.com.br/
 * Copyright (C) 2018 UoUTec. (calypte@uoutec.com.br)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package calypte.server.util;

import java.util.Arrays;

import calypte.server.util.ArraySplit;
import junit.framework.TestCase;

public class ArraySplitTest extends TestCase{

	private ArraySplit header;
	
	private int l;
	
	private byte[] b;
	
	public void setUp(){
		header = new ArraySplit();
		header.setSeparator((byte)' ');
		b = new byte[256];
	}
	
	public void tearDown(){
		header = null;
		b      = null;
	}
	
	public void testEmpty(){
		header.setData("".getBytes());

		l = header.readNext(b, 0, b.length);
		assertTrue(Arrays.equals(ArraySplit.EMPTY, Arrays.copyOf(b, l)));
	}
	
	public void testText(){
		header.setData("get".getBytes());
		
		l = header.readNext(b, 0, b.length);
		assertTrue(Arrays.equals("get".getBytes(), Arrays.copyOf(b, l)));
	}

	public void testSingleChar(){
		header.setData("g".getBytes());
		
		l = header.readNext(b, 0, b.length);
		assertTrue(Arrays.equals("g".getBytes(), Arrays.copyOf(b, l)));
	}

	public void testMultipleLastText(){
		header.setData("get key".getBytes());
		
		l = header.readNext(b, 0, b.length);
		assertTrue(Arrays.equals("get".getBytes(), Arrays.copyOf(b, l)));
		
		l = header.readNext(b, 0, b.length);
		assertTrue(Arrays.equals("key".getBytes(), Arrays.copyOf(b, l)));

		l = header.readNext(b, 0, b.length);
		assertTrue(Arrays.equals(ArraySplit.EMPTY, Arrays.copyOf(b, l)));
	}

	public void testMultipleLastChar(){
		
		header.setData("get k".getBytes());

		l = header.readNext(b, 0, b.length);
		assertTrue(Arrays.equals("get".getBytes(), Arrays.copyOf(b, l)));

		l = header.readNext(b, 0, b.length);
		assertTrue(Arrays.equals("k".getBytes(), Arrays.copyOf(b, l)));
		
		l = header.readNext(b, 0, b.length);
		assertTrue(Arrays.equals(ArraySplit.EMPTY, Arrays.copyOf(b, l)));
	}

	public void testMultiple(){
		
		header.setData("get key 0 120 0".getBytes());
		
		l = header.readNext(b, 0, b.length);
		assertTrue(Arrays.equals("get".getBytes(), Arrays.copyOf(b, l)));
		
		l = header.readNext(b, 0, b.length);
		assertTrue(Arrays.equals("key".getBytes(), Arrays.copyOf(b, l)));

		l = header.readNext(b, 0, b.length);
		assertTrue(Arrays.equals("0".getBytes(), Arrays.copyOf(b, l)));

		l = header.readNext(b, 0, b.length);
		assertTrue(Arrays.equals("120".getBytes(), Arrays.copyOf(b, l)));

		l = header.readNext(b, 0, b.length);
		assertTrue(Arrays.equals("0".getBytes(), Arrays.copyOf(b, l)));

		l = header.readNext(b, 0, b.length);
		assertTrue(Arrays.equals(ArraySplit.EMPTY, Arrays.copyOf(b, l)));
	}
	
}
