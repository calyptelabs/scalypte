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

import java.io.InputStream;

import calypte.Cache;
import calypte.CacheErrors;
import calypte.CacheException;
import calypte.CacheInputStream;

import calypte.server.Terminal;
import calypte.server.TerminalConstants;
import calypte.server.TerminalReader;
import calypte.server.TerminalWriter;
import calypte.server.error.ServerErrorException;
import calypte.server.error.ServerErrors;
import calypte.server.util.ArraysUtil;

/**
 * Representa o comando <code>set</code>.
 * Sua sintaxe é:
 * <pre>
 * set &lt;key&gt; &lt;timeToLive&gt; &lt;timeToIdle&gt; &lt;size&gt; &lt;reserved&gt;\r\n
 * &lt;data&gt;\r\n
 * </pre> 
 * @author Ribeiro
 *
 */
public class SetCommand extends AbstractCommand{

	public void executeCommand(Terminal terminal, Cache cache, TerminalReader reader,
			TerminalWriter writer, byte[][] parameters)
			throws Throwable {
		
        int timeToLive;
        int timeToIdle;
        int size;
		String key;

		try{
			key = ArraysUtil.toString(parameters[1]);
			
			if(key == null){
		        throw new NullPointerException();
			}
	    }
	    catch(Throwable e){
	        throw new ServerErrorException(ServerErrors.ERROR_1003, "key");
	    }
		
        try{
        	timeToLive = ArraysUtil.toInt(parameters[2]);
        	if(timeToLive < 0){
        		throw new IllegalStateException();
        	}
        }
        catch(Throwable e){
            throw new ServerErrorException(ServerErrors.ERROR_1003, "timeToLive");
        }

        try{
        	timeToIdle = ArraysUtil.toInt(parameters[3]);
        	if(timeToIdle < 0){
        		throw new IllegalStateException();
        	}
        }
        catch(Throwable e){
            throw new ServerErrorException(ServerErrors.ERROR_1003, "timeToIdle");
        }

        try{
            size = ArraysUtil.toInt(parameters[4]);
        	if(size <= 0){
        		throw new IllegalStateException();
        	}
        }
        catch(Throwable e){
            throw new ServerErrorException(ServerErrors.ERROR_1003, "size");
        }
        
        InputStream stream = reader.getStream(size);
        CacheInputStream result = null;
        Throwable error    = null;
        
        try{
            result = (CacheInputStream)cache.putIfAbsentStream(
                key, 
                stream,
                timeToLive,
                timeToIdle);
        }
        catch(Throwable e){
        	//capturado erro no processamento do fluxo de bytes do item
        	error = e;
        }
        finally{
            if(stream != null){
            	try{
                	//tenta fechar o fluxo
            		stream.close();
            	}
            	catch(Throwable e){
            		//se error for null, a falha a ser considerada é do 
            		//fechamento do fluxo.
            		if(error == null){
            			throw new ServerErrorException(e, ServerErrors.ERROR_1004);
            		}
            	}
            }
            
            //Lança o erro encontrado no processamento da stream.
            if(error != null){
            	//Se for lançada a exceção CacheException com o erro ERROR_1030, significa que
            	//já existe um item e o mesmo expirou na execução do método.
            	if(!(error instanceof CacheException) || ((CacheException)error).getError() != CacheErrors.ERROR_1030){
            	//	throw new ServerErrorException(error, ServerErrors.ERROR_1030);
            	//}
            	//else{
            		throw new ServerErrorException(error, ServerErrors.ERROR_1004);
            	}
            }
        }
        

        writer.sendMessage(result != null || error != null? TerminalConstants.NOT_STORED_DTA : TerminalConstants.STORED_DTA);
        writer.flush();        
	}

}
