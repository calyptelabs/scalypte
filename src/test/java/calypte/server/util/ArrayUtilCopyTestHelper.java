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

import java.lang.reflect.Field;

import sun.misc.Unsafe;

@SuppressWarnings("restriction")
public class ArrayUtilCopyTestHelper {

	private static final Unsafe UNSAFE;
	
    private static final long BYTE_ARRAY_OFFSET;
    
    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            UNSAFE = (Unsafe) theUnsafe.get(null);
    		BYTE_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(byte[].class);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
	public static void copy1(byte[] src, int srcPos,
			byte[] dest, int destPos, int length){
		UNSAFE.copyMemory(src, BYTE_ARRAY_OFFSET + srcPos, dest, BYTE_ARRAY_OFFSET + destPos, length);		
	}

	public static void copy2(byte[] src, int srcPos,
			byte[] dest, int destPos, int length){
		System.arraycopy(src, srcPos, dest, destPos, length);		
	}
	
}
