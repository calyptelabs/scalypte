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

package calypte.server.command;

import java.io.OutputStream;

import calypte.Cache;
import calypte.CacheInputStream;
import calypte.tx.TXCache;

import calypte.server.Terminal;
import calypte.server.TerminalConstants;
import calypte.server.TerminalReader;
import calypte.server.TerminalWriter;
import calypte.server.error.ServerErrorException;
import calypte.server.error.ServerErrors;
import calypte.server.util.ArraysUtil;

/**
 * Representa o comando <code>get</code>.
 * Sua sintaxe é:
 * <pre>
 * get &lt;key&gt; &lt;update&gt; &lt;reserved&gt;\r\n
 * </pre> 
 * @author Brandao
 *
 */
public class GetCommand extends AbstractCommand{

	private static final byte[] FALSE                 = new byte[]{'0'};
	
	private static final byte[] PREFIX                = "value ".getBytes();

	private static final byte[] SUFFIX                = " 0".getBytes();

	private static final byte[] FULL_SUFFIX           = " 0\r\n".getBytes();
	
	private static final byte[] EMPTY_SUFFIX          = " 0 0".getBytes();

	private static final byte[] FULL_EMPTY_SUFFIX     = " 0 0\r\n".getBytes();
	
	private static final byte[] SEPARATOR_COMMAND_DTA = TerminalConstants.SEPARATOR_COMMAND_DTA;
	
	private static final byte[] CRLF_DTA              = TerminalConstants.CRLF_DTA;
	
	private static final byte[] BOUNDARY_DTA          = TerminalConstants.FULL_BOUNDARY_DTA;
	
	public void executeCommand(Terminal terminal, Cache cache, TerminalReader reader,
			TerminalWriter writer, byte[][] parameters)
			throws Throwable {

		String key;
		boolean forUpdate;
		
		try{
			key = ArraysUtil.toString(parameters[1]);
			
			if(key == null){
		        throw new NullPointerException();
			}
		
            forUpdate = parameters[2][0] != '0';
			//forUpdate = !ArraysUtil.equals(parameters[2], FALSE);
	    }
	    catch(Throwable e){
	        throw new ServerErrorException(ServerErrors.ERROR_1004, e);
	    }
		
        CacheInputStream in = null;
        
        try{
        	if(forUpdate){
        		if(!(cache instanceof TXCache)){
        			throw new ServerErrorException(ServerErrors.ERROR_1009);
        		}
        		else{
        			TXCache txCache = (TXCache)cache;
        			in = (CacheInputStream)txCache.getStream(key, forUpdate);
        		}
        	}
        	else{
        		in = (CacheInputStream)cache.getStream(key);
        	}
        	
            if(in != null){
            	writer.write(PREFIX, 0, PREFIX.length);
            	writer.write(ArraysUtil.toBytes(key));
            	writer.write(SEPARATOR_COMMAND_DTA, 0, SEPARATOR_COMMAND_DTA.length);
            	writer.write(ArraysUtil.toBytes(in.getSize()));
            	writer.write(FULL_SUFFIX, 0, FULL_SUFFIX.length);
            	
                OutputStream out = null;
                try{
                    out = writer.getStream();
                	in.writeTo(out);
                }
                finally{
                    if(out != null){
                        try{
                            out.close();
                        }
                        catch(Throwable e){
                        }
                    }
                	writer.write(CRLF_DTA, 0, CRLF_DTA.length);
                }
            }
            else{
            	writer.write(PREFIX, 0, PREFIX.length);
            	writer.write(ArraysUtil.toBytes(key));
            	writer.write(FULL_EMPTY_SUFFIX, 0, FULL_EMPTY_SUFFIX.length);
            }
        }
        finally{
            if(in != null){
                in.close();
            }
        }

        writer.write(BOUNDARY_DTA, 0, BOUNDARY_DTA.length);
        writer.flush();
	}

}
