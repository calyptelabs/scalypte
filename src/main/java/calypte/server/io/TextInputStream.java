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

import calypte.server.TerminalConstants;
import calypte.server.util.ArraysUtil;

public class TextInputStream extends LimitedSizeInputStream{

    private static final byte[] BOUNDARY = TerminalConstants.CRLF_DTA;
	
    private byte[] closeBuffer;
    
	public TextInputStream(BufferedInputStream buffer, int size) {
		super(buffer, size);
		this.closeBuffer = new byte[2];
	}

    public void close() throws IOException{
    	super.close();
    	
        this.buffer.read(this.closeBuffer, 0, 2);

        if(!ArraysUtil.equals(BOUNDARY, this.closeBuffer)){
        	throw new EOFException("premature end of data");
        }
    }
	
}
