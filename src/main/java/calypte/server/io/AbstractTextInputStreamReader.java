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

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import calypte.server.TerminalConstants;
import calypte.server.util.ArraysUtil;

/**
 *
 * @author Brandao
 */
public abstract class AbstractTextInputStreamReader extends InputStream{

    private static final byte[] CRLF = TerminalConstants.CRLF_DTA;
	
    protected BufferedInputStream buffer;
    
    protected byte[] byteBuffer;
    
    private int offsetBuf;
    
    private boolean closed;
    
    private boolean hasLineFeed;
    
    public AbstractTextInputStreamReader(BufferedInputStream buffer, int offset){
        this.buffer      = buffer;
        this.byteBuffer  = null;
        this.offsetBuf   = offset;
        this.closed      = false;
        this.hasLineFeed = false;
    }
    
    @Override
    public int read() throws IOException {
        byte[] buf = new byte[1];
        int result = this.read(buf, 0, 1);
        
        if(result == -1)
            return -1;
        else
            return buf[0] & 0xff;
        
    }
    
    @Override
    public int read(byte[] bytes, int offset, int len) throws IOException {    
        
        if(closed)
            return -1;
        
        int initOffset = offset;
        int limitRead  = offset + len;
        boolean insertlinefeed;
        
        while(!closed && offset < limitRead){
            
            int maxRead  = byteBuffer == null? 0 : byteBuffer.length - this.offsetBuf;
            int maxWrite = limitRead - offset;

            if(maxRead == 0){
                byte[] line = this.readData(this.buffer);
                if(line == null){
                	this.close();
                    return offset - initOffset;
                }
                
                insertlinefeed = this.hasLineFeed;
                this.hasLineFeed = this.buffer.isHasLineFeed();
                
                if(insertlinefeed){
                    this.byteBuffer = Arrays.copyOf(CRLF, CRLF.length + line.length);
                    ArraysUtil.arraycopy(line, 0, this.byteBuffer, CRLF.length, line.length);
                }
                else
                    this.byteBuffer = line;
                            
                this.offsetBuf = 0;
                maxRead = byteBuffer.length - this.offsetBuf;
            }
            
            if(maxWrite > maxRead){
            	ArraysUtil.arraycopy(
                        this.byteBuffer, 
                        this.offsetBuf, 
                        bytes, 
                        offset, 
                        maxRead);
                
                this.offsetBuf += maxRead;
                offset         += maxRead;
            }
            else{
            	ArraysUtil.arraycopy(
                    this.byteBuffer, 
                    this.offsetBuf, 
                    bytes, 
                    offset, 
                    maxWrite);
                
                this.offsetBuf += maxWrite;
                offset         += maxWrite;
            }            
        }
        
        return offset - initOffset;
    }
    
    protected abstract byte[] readData(BufferedInputStream buffer) throws IOException;
    
    @Override
    public void close() throws IOException{
        if(this.closed)
            return;
            
        try{
        	this.closed = this.closeData(this.buffer);
        }
        catch(IOException e){
        	this.closed = true;
        	throw e;
        }
   }
    
    protected abstract boolean closeData(BufferedInputStream buffer) throws IOException;
}
