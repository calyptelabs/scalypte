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

import junit.framework.TestCase;

public class ArraysUtilTest extends TestCase{

	public void testToInt() {
		assertEquals(Integer.MIN_VALUE, ArraysUtil.toInt(String.valueOf(Integer.MIN_VALUE).getBytes()));
		assertEquals(0, ArraysUtil.toInt(String.valueOf(0).getBytes()));
		assertEquals(Integer.MAX_VALUE, ArraysUtil.toInt(String.valueOf(Integer.MAX_VALUE).getBytes()));
	}

	public void testToLong() {
		assertEquals(Long.MIN_VALUE, ArraysUtil.toLong(String.valueOf(Long.MIN_VALUE).getBytes()));
		assertEquals(0, ArraysUtil.toLong(String.valueOf(0).getBytes()));
		assertEquals(Long.MAX_VALUE, ArraysUtil.toLong(String.valueOf(Long.MAX_VALUE).getBytes()));
	}
	
}
