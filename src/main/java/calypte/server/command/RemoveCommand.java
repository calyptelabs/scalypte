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

import calypte.Cache;

import calypte.server.Terminal;
import calypte.server.TerminalConstants;
import calypte.server.TerminalReader;
import calypte.server.TerminalWriter;
import calypte.server.error.ServerErrorException;
import calypte.server.error.ServerErrors;
import calypte.server.util.ArraysUtil;

/**
 * Representa o comando <code>remove</code>.
 * Sua sintaxe é:
 * <pre>
 * remove &lt;key&gt; &lt;reserved&gt;\r\n
 * </pre> 
 * @author Brandao
 *
 */
public class RemoveCommand extends AbstractCommand{

	public void executeCommand(Terminal terminal, Cache cache, TerminalReader reader,
			TerminalWriter writer, byte[][] parameters)
			throws Throwable {

		boolean result;
		
		String name = ArraysUtil.toString(parameters[1]);
		
        if(name == null){
        	throw new ServerErrorException(ServerErrors.ERROR_1003, "name");        	
        }
		
        result = cache.remove(name);
    	
	    if(result){
	    	writer.sendMessage(TerminalConstants.SUCCESS_DTA);
	    }
	    else{
	    	writer.sendMessage(TerminalConstants.NOT_FOUND_DTA);
	    }
	    writer.flush();

    }

}
