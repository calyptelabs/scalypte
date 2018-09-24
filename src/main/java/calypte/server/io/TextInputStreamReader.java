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

import calypte.server.TerminalConstants;

/**
 *
 * @author Brandao
 */
public class TextInputStreamReader 
	extends AbstractTextInputStreamReader{

    private static final byte[] BOUNDARY = TerminalConstants.BOUNDARY_DTA;
    
    public TextInputStreamReader(BufferedInputStream buffer, int offset){
    	super(buffer, offset);
    }
    
	@Override
	protected byte[] readData(BufferedInputStream buffer) throws IOException {
		
        byte[] line = buffer.readLineInBytes();
        
        if(line.length > 2 && line[0] == BOUNDARY[0] && line[1] == BOUNDARY[1] && line[2] == BOUNDARY[2])
            return null;
        else
        	return line;
	}

	@Override
	protected boolean closeData(BufferedInputStream buffer) throws IOException {
        byte[] line;
        while((line = buffer.readLineInBytes()) != null){

            if(line.length > 2 && line[0] == BOUNDARY[0] && line[1] == BOUNDARY[1] && line[2] == BOUNDARY[2]){
                return true;
            }            
        }
        return false;
	}
	
}
