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
import java.util.Arrays;

import sun.misc.Unsafe;

/**
 * Provê métodos auxiliares de manipulação de arranjo de bytes.
 * @author Ribeiro
 *
 */
@SuppressWarnings("restriction")
public class ArraysUtil {

	
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
    
	
	private static final int NEGATIVE_INT   = 0xffffffff;
	
	private static final long NEGATIVE_LONG = 0xffffffffffffffffL;
	
	private static final byte ZERO          = '0';

	private static final byte NEGATIVE      = '-';

	private static final byte POSITIVE      = '+';

	private static final byte TRUE          = 1;

	private static final byte FALSE         = 0;
	
	public static void arraycopy(byte[] src, int srcPos,
            byte[] dest, int destPos,
            int length){
		UNSAFE.copyMemory(src, BYTE_ARRAY_OFFSET + srcPos, dest, BYTE_ARRAY_OFFSET + destPos, length);
		//System.arraycopy(src, srcPos, dest, destPos, length);
	}
	
	/**
	 * Verifica se um arranjo de bytes inicia com os mesmos bytes de outro arranjo de bytes.
	 * @param array arranjo de bytes que será verificado.
	 * @param value arranjo de bytes usado na comparação
	 * @return <code>true</code> se array iniciar com value. Caso contrário <code>false</code>
	 */
	public static boolean startsWith(byte[] array, byte[] value){
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

	/**
	 * Verifica se dois arranjos de bytes são iguais.
	 * @param a arranjo a ser comparado.
	 * @param b arranjo a ser comparado.
	 * @return <code>true</code> se forem iguais. Caso contrário, <code>false</code> 
	 */
	public static boolean equals(byte[] a, byte[] b){
		try{
			int alen = a.length;
			
			if(alen != b.length){
				return false;
			}
			
			int r = 0;
			for(int i=0;i<alen;i++){
				r = a[i] - b[i];
			}
			
			return r == 0;
		}
		catch(Throwable e){
			return false;
		}
	}
	
	/**
	 * Fragmento um arranjo usando um byte como delimitador.
	 * @param array arranjo
	 * @param len tamanho do arranjo.
	 * @param value delimitador.
	 * @param index índice inicial.
	 * @return fragmentos.
	 */
	public static byte[][] split(byte[] array, int index, int len, byte value){
		int maxIndex    = index + len;
		int start       = index;
		int end         = 0;
		byte[][] result = new byte[10][];
		int resultIndex = 0;
		
		int limit = maxIndex -1;
		
		for(int i=index;i<maxIndex;i++){
			
			if(array[i] == value){
				end = i;
				byte[] item = copy(array, start, end);
				
				if(resultIndex >= result.length){
					result = Arrays.copyOf(result, result.length*2);
				}
				result[resultIndex++] = item;
				
				start = end + 1;
				end = start;
			}
			
		}
		
		if((limit > start || (start == limit && array[limit] != value)) ){
			byte[] item = copy(array, start, limit + 1);
			
			if(resultIndex >= result.length){
				result = Arrays.copyOf(result, result.length*2);
			}
			result[resultIndex++] = item;
		}
		
		return result;//Arrays.copyOf(result, resultIndex);
	}
	
	/**
	 * Converte um texto representado por um arranjo de bytes em um inteiro.
	 * @param value arranjo.
	 * @return inteiro.
	 */
	public static int toInt(byte[] value){
		int limit      = value.length - 1;
		byte signal    = value[0] == NEGATIVE? FALSE : TRUE;
		byte hasSignal = value[0] == NEGATIVE || value[0] == POSITIVE? TRUE : FALSE;
		
		int start  = hasSignal == TRUE? 1 : 0;
		int result = 0;
		int mult   = 1;
		
		while(limit>=start){
			result += (value[limit--] - ZERO)*mult;
			mult   *= 10;
		}
		
		if(signal == FALSE){
			result = (result ^ NEGATIVE_INT) + 1;
		}
		
		return result;
	}

	/**
	 * Converte um valor numérico em um texto no formato de arranjo de bytes.
	 * <p>É equivalente ao trecho abaixo:</p>
	 * <pre>
	 * String value = Long.toString(longValue);
	 * byte[] bytes = value.getBytes();
	 * </pre>
	 * 
	 * @param value valor. 
	 * @return arranjo de bytes.
	 */
	public static byte[] toBytes(long value){

		if(value == 0){
			return new byte[]{ZERO};
		}
		
		boolean negative = false;
		
		if(value < 0){
			negative = true;
			value = value ^ NEGATIVE_LONG - 1;
		}
		
		long tmp = value;
		byte[] r = new byte[20];
		int i    = 20;
		
		while(tmp > 0){
			r[--i] = (byte)(ZERO + (tmp % 10));
			tmp = tmp / 10;
		}
		
		if(negative){
			r[--i] = NEGATIVE;
		}

		return i > 0? Arrays.copyOfRange(r, i, r.length) : r;
	}
	
	/**
	 * Converte um valor numérico em um texto no formato de arranjo de bytes.
	 * <p>É equivalente ao trecho abaixo:</p>
	 * <pre>
	 * String value = Long.toString(longValue);
	 * byte[] bytes = value.getBytes();
	 * </pre>
	 * 
	 * @param value valor. 
	 * @return arranjo de bytes.
	 */
	public static byte[] toBytes(int value){

		if(value == 0){
			return new byte[]{ZERO};
		}
		
		boolean negative = false;
		
		if(value < 0){
			negative = true;
			value = value ^ NEGATIVE_INT - 1;
		}
		
		long tmp = value;
		byte[] r = new byte[20];
		int i    = 20;
		
		while(tmp > 0){
			r[--i] = (byte)(ZERO + (tmp % 10));
			tmp = tmp / 10;
		}
		
		if(negative){
			r[--i] = NEGATIVE;
		}

		return i > 0? Arrays.copyOfRange(r, i, r.length) : r;
	}
	
	/**
	 * Converte uma string em um arranjo de bytes.
	 * 
	 * @param value valor. 
	 * @return arranjo de bytes.
	 */
	public static byte[] toBytes(String value){
		
		char[] chars = value.toCharArray();
		byte[] bytes = new byte[chars.length];
		int len      = chars.length;
		int i        = 0;
		
		while(i<len){
			bytes[i] = (byte)chars[i++];
		}
		
		return bytes;
	}
	
	/**
	 * Converte um texto representado por um arranjo de bytes em um inteiro.
	 * @param value arranjo.
	 * @return inteiro.
	 */
	public static long toLong(byte[] value){
		int limit      = value.length - 1;
		byte signal    = value[0] == NEGATIVE? FALSE : TRUE;
		byte hasSignal = value[0] == NEGATIVE || value[0] == POSITIVE? TRUE : FALSE;
		
		int start   = hasSignal == TRUE? 1 : 0;
		long result = 0;
		int mult    = 1;
		
		for(int i=limit;i>=start;i--){
			//int tmp = value[i] - ZERO;
			result += (value[i] - ZERO)*mult;
			mult   *= 10;
		}
		
		if(signal == FALSE){
			result = (result ^ NEGATIVE_LONG) + 1;
		}
		
		return result;
	}
	
	/**
	 * Converte um um arranjo de bytes em texto.
	 * @param value arranjo
	 * @return texto.
	 */
	public static String toString(byte[] value){
		char[] chars = new char[value.length];
		int len      = value.length;
		int i        = 0;
		
		while(i<len){
			chars[i] = (char)value[i++];
		}
		
		return new String(chars);
	}
	
	public static byte[] concat(byte[][] value){
		
		int size = 0;
		
		for(byte[] dta: value){
			size += dta.length;
		}
		
		byte[] result = new byte[size];
		int index = 0;
		
		for(byte[] dta: value){
			arraycopy(dta, 0, result, index, dta.length);
			index += dta.length;
		}
		
		return result;
	}
	
	private static byte[] copy(byte[] origin, int start, int end){
		int len = end-start;
		byte[] item = new byte[len];
		arraycopy(origin, start, item, 0, len);
		return item;
	}

}
