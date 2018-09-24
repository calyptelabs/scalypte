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
import calypte.CacheException;
import calypte.server.Command;
import calypte.server.Parameters;
import calypte.server.Terminal;
import calypte.server.TerminalReader;
import calypte.server.TerminalWriter;
import calypte.server.error.ServerErrorException;
import calypte.server.error.ServerErrors;

public abstract class AbstractCommand 
	implements Command{

	protected byte[] buffer;

	public AbstractCommand(){
		this.buffer = new byte[256];
	}
	
	public void execute(Terminal terminal, Cache cache, TerminalReader reader,
			TerminalWriter writer, Parameters params)
			throws ServerErrorException {

		try{
			this.executeCommand(terminal, cache, reader, writer, params);
		}
        catch(ServerErrorException ex){
            throw ex;
        }
        catch(CacheException ex){
            throw new ServerErrorException(ex, ex.getError(), ex.getParams());
        }
        catch (Throwable ex) {
            throw new ServerErrorException(ex, ServerErrors.ERROR_1005, ServerErrors.ERROR_1005.getString());
        }
		
	}
	
	protected abstract void executeCommand(Terminal terminal, Cache cache, TerminalReader reader,
			TerminalWriter writer, Parameters params)
			throws Throwable;
}
