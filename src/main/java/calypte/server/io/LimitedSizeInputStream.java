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

package calypte.server.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class LimitedSizeInputStream 
	extends InputStream{

	protected BufferedInputStream buffer;
	
	protected int size;
	
	protected int read;
	
	public LimitedSizeInputStream(BufferedInputStream buffer, int size){
		this.buffer     = buffer;
		this.size       = size;
		this.read       = 0;
	}
	
    public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
		this.read = 0;
	}

	public int read() throws IOException{
    	
    	if(this.size == this.read ){
    		return -1;
    	}
    	
    	int val = this.buffer.read();
    	this.read++;
    	return val;
    }
	
    public int read(byte[] b, int off, int len) throws IOException{
    	
    	if(this.size == this.read ){
    		return -1;
    	}
    	
    	int maxRead = this.size - this.read; 
    	int toRead  = len > maxRead? maxRead : len;
    	int l       = this.buffer.read(b, off, toRead);
    	this.read  += l;
    	return l;
    }
    
    public void close() throws IOException{
    	int toRead = size - read;
    	if(toRead > 0){
    	    byte[] closeBuffer = new byte[1024];
	    	while(toRead > 0){
	    	    int maxRead = toRead > closeBuffer.length? closeBuffer.length : toRead;
	    		int r       = this.buffer.read(closeBuffer, 0, maxRead);
	    		
	    		if(r < 0){
	            	throw new EOFException("premature end of data");
	    		}
	    		
	    		read   += r;
	    		toRead -= r;
	    	}
    	}
    }
    
}
