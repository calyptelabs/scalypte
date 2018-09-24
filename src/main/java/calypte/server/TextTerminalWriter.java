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
import java.io.OutputStream;
import java.net.Socket;

import calypte.server.io.BufferedOutputStream;
import calypte.server.io.StreamFactory;
import calypte.server.util.ArraysUtil;

/**
 *
 * @author Brandao
 */
public class TextTerminalWriter implements TerminalWriter{

    private BufferedOutputStream buffer;
    
    private int writeBufferSize;
    
    public TextTerminalWriter(Socket socket, StreamFactory streamFactory, 
            int writeBufferSize) throws IOException{
        this.writeBufferSize = writeBufferSize;
        this.buffer = new BufferedOutputStream(
                this.writeBufferSize, 
                streamFactory.createOutputStream(socket));
    }

    public void sendMessage(String message) throws WriteDataException {
        try{
            buffer.write(ArraysUtil.toBytes(message));
            buffer.write(TerminalConstants.CRLF_DTA);
        }
        catch(IOException e){
            throw new WriteDataException(TerminalConstants.SEND_MESSAGE_FAIL, e);
        }
    }

    public void sendMessage(byte[] message) throws WriteDataException {
        try{
            buffer.write(message);
            buffer.write(TerminalConstants.CRLF_DTA);
        }
        catch(IOException e){
            throw new WriteDataException(TerminalConstants.SEND_MESSAGE_FAIL, e);
        }
    }

    public void directWrite(byte[] b, int off, int len) throws WriteDataException {
        try{
            buffer.directWrite(b, off, len);
        }
        catch(IOException e){
            throw new WriteDataException(TerminalConstants.SEND_MESSAGE_FAIL, e);
        }
    }
    
    public void write(byte[] b, int off, int len) throws WriteDataException {
        try{
            buffer.write(b, off, len);
        }
        catch(IOException e){
            throw new WriteDataException(TerminalConstants.SEND_MESSAGE_FAIL, e);
        }
    }

    public void write(byte[] b) throws WriteDataException {
        try{
            buffer.write(b, 0, b.length);
        }
        catch(IOException e){
            throw new WriteDataException(TerminalConstants.SEND_MESSAGE_FAIL, e);
        }
    }
    
    public void sendCRLF() throws WriteDataException {
        try{
            buffer.write(TerminalConstants.CRLF_DTA);
        }
        catch(IOException e){
            throw new WriteDataException(TerminalConstants.SEND_MESSAGE_FAIL, e);
        }
            
    }

    public void flush() throws WriteDataException {
        try{
            buffer.flush();
        }
        catch(IOException e){
            throw new WriteDataException(TerminalConstants.SEND_MESSAGE_FAIL, e);
        }
    }
    
    public OutputStream getStream() {
        return buffer;//new TextOutputStream(this.buffer);
    }

}
