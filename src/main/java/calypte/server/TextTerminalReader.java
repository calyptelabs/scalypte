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

package calypte.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import calypte.server.io.BufferedInputStream;
import calypte.server.io.LimitedSizeInputStream;
import calypte.server.io.StreamFactory;
import calypte.server.io.TextInputStream;
import calypte.server.io.TextInputStreamReader;

/**
 *
 * @author Ribeiro
 */
public class TextTerminalReader implements TerminalReader{

    @SuppressWarnings("unused")
	private Socket socket;
    
    private InputStream stream;
    
    private BufferedInputStream buffer;
    
    private LimitedSizeInputStream textContentInputStream;
    
    private int offset;
    
    public TextTerminalReader(Socket socket, StreamFactory streamFactory, int readBufferSize) throws IOException{
        this.socket = socket;
        this.stream = streamFactory.createInpuStream(socket);
        this.buffer = new BufferedInputStream(readBufferSize, this.stream);
        this.textContentInputStream = new TextInputStream(buffer, 0);
        this.offset = 0;
    }
    
    public InputStream getStream() {
        return new TextInputStreamReader(buffer, offset);
    }

    public InputStream getStream(int size) {
    	textContentInputStream.setSize(size);
    	return textContentInputStream;
    }
    
    public int getOffset() {
        return offset;
    }

    public int readMessage(byte[] b, int off, int len) throws ReadDataException{
    	try{
    		return buffer.readFullLine(b, off, len);
		}
		catch(IOException e){
			throw new ReadDataException(e);
		}
    }
    
	public String getMessage() throws ReadDataException {
		try{
			return buffer.readLine();
		}
		catch(IOException e){
			throw new ReadDataException(e);
		}
	}

	public byte[] getMessageBytes() throws ReadDataException {
		try{
			return buffer.readLineInBytes();
		}
		catch(IOException e){
			throw new ReadDataException(e);
		}
	}
	
}
