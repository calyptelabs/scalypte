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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import calypte.memory.Memory;
import calypte.memory.RegionMemory;

/**
 *
 * @author Ribeiro
 */
public class BufferedMemoryInputStream extends InputStream{
    
    private int offset;
    
    private int limit;
    
    private RegionMemory buffer;
    
    private int capacity;
    
    private InputStream stream;

    private boolean hasLineFeed;
    
    private Memory memory;
    
    public BufferedMemoryInputStream(int capacity, Memory memory, InputStream stream){
        this.offset   = 0;
        this.limit    = 0;
        this.memory   = memory;
        this.buffer   = this.memory.alloc(capacity);
        this.capacity = capacity;
        this.stream   = stream;
    }

    public String readLine() throws IOException{
        byte[] data = readLineInBytes();
        return data == null? null : new String(data);
    }

    public int read() throws IOException{
    	
        if(this.offset == this.limit && this.checkBuffer() < 0){
        	return -1;
        }
    	
        return this.buffer.get(this.offset++);
    }
    
    public int read(byte[] b, int off, int len) throws IOException{
    	
    	int read  = 0;
    	
    	while(len > 0){
    		
            int maxRead = this.limit - this.offset;
            
            if(len > maxRead){
            	
                if(this.offset == this.limit && this.checkBuffer() < 0){
                	return read;
                }
                
                this.buffer.read(this.offset, b, off, maxRead);
            	//ArraysUtil.arraycopy(this.buffer, this.offset, b, off, maxRead);
            	this.offset += maxRead;
            	off         += maxRead;
            	read        += maxRead;
            	len         -= maxRead;
            }
            else{
                this.buffer.read(this.offset, b, off, len);
            	//ArraysUtil.arraycopy(this.buffer, this.offset, b, off, len);
            	this.offset += len;
            	read        += len;
            	return read; 
            }
            
    	}
    	
    	return read;
    }
    
    private int checkBuffer() throws IOException{
    	
        if(this.limit == this.capacity){
            this.offset = 0;
            this.limit  = 0;
        }
        
        int l    = (int)(this.buffer.size() - this.limit);
        byte[] b = new byte[l];
        int len  = stream.read(b, 0, l);
        this.buffer.write(this.limit, b, 0, len);
        //int len = stream.read(this.buffer, this.limit, this.buffer.length - limit);

        if(len == -1){
        	return -1;
        }
            //throw new EOFException("premature end of data");
        
        this.limit += len;
        return len;
    }
    
    public int readFullLine(byte[] b, int off, int len) throws IOException{
    	
    	int startOff = this.offset;
    	int read     = 0;

		int maxRead;
		while(len > 0){
		
			while(this.offset < this.limit){
				
	            if(this.buffer.get(this.offset++) == '\n'){
	        		maxRead  = this.offset - startOff;
	        		
	        		if(maxRead > len){
	        			throw new IOException("out of memory");
	        		}
	        		
	        		if(this.offset < 2){
	        			if(read == 0 || b[read -1] != '\r'){
	            			throw new IOException("expected \\r");
	        			}
	        			else{
	                        this.buffer.read(startOff, b, off - 1, maxRead - 1);
	                		//ArraysUtil.arraycopy(this.buffer, startOff, b, off - 1, maxRead - 1);
	                    	read+= maxRead - 2;
	        			}
	        		}
	        		else{
	        			if(this.buffer.get(this.offset - 2) != '\r'){
	            			throw new IOException("expected \\r");
	        			}
	        			else{
	                        this.buffer.read(startOff,b, off, maxRead - 2);
	                		//ArraysUtil.arraycopy(this.buffer, startOff, b, off, maxRead - 2);
	                    	read+= maxRead - 2;
	        			}
	        		}
	        		
	            	return read;
	            }
	            
			}
		
			maxRead  = this.offset - startOff;
			
    		if(maxRead > len){
    			throw new IOException("out of memory");
    		}
			
            this.buffer.read(startOff, b, off, maxRead);
    		//ArraysUtil.arraycopy(this.buffer, startOff, b, off, maxRead);
        	
        	len -= maxRead;
        	off += maxRead;
        	read+= maxRead;
        	
        	if(this.checkBuffer() < 0)
        		return read > 0? read : -1;
        	
        	startOff = this.offset;
    		
		}
		
		return read;
    }
    
    public byte[] readLineInBytes() throws IOException{
    	
    	ByteArrayOutputStream bout = new ByteArrayOutputStream(12);
    	int startOff  = this.offset;
    	
    	for(;;){
            if(this.offset == this.limit){
            	
            	int l = this.offset - startOff;
            	byte[] b = new byte[l];
            	this.buffer.read(startOff, b, 0, l);
            	bout.write(b, 0, b.length);
            	
            	//bout.write(this.buffer, startOff, this.offset - startOff);
            	if(this.checkBuffer() < 0)
            		return bout.toByteArray();
            	startOff = this.offset;
            }
            
            //if(this.buffer[this.offset++] == '\n'){
            if(this.buffer.get(this.offset++) == '\n'){
            	
            	int l = this.offset - startOff;
            	byte[] b = new byte[l];
            	this.buffer.read(startOff, b, 0, l);
            	bout.write(b, 0, b.length);
            	
            	//bout.write(this.buffer, startOff, this.offset - startOff);
            	byte[] array = bout.toByteArray();
            	
            	if(array.length > 0){ 
            		if(array[array.length-1] == '\r'){
                    	return Arrays.copyOf(array, array.length - 1);
            		}
            		else{
                        throw new IOException("expected \\r");
            		}
            	}
            	else{
            		return array;
            	}
            }
            
    	}
    	
    }
    
    public void clear(){
        this.offset = 0;
        this.limit  = 0;
    }

    public boolean isHasLineFeed() {
        return hasLineFeed;
    }
    
}