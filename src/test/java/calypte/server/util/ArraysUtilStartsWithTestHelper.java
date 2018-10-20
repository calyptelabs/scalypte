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

public class ArraysUtilStartsWithTestHelper {

	public static boolean startsWith1(byte[] array, byte[] value){
		
		if(array.length < value.length)
			return false;
		
		for(int i=0;i<value.length;i++){
			
			if(array[i] != value[i]){
				return false;
			}
				
		}
		
		return true;
	}

	public static boolean startsWith2(byte[] array, byte[] value){
		try{
			int len = value.length;
			int r   = 0;
			for(int i=0;i<len;i++){
				r += array[i] - value[i];
			}
			return r == 0;
		}
		catch(Throwable e){
			return false;
		}
	}
	
}
