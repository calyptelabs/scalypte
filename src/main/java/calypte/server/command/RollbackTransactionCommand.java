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
import calypte.tx.CacheTransaction;
import calypte.tx.TXCache;

import calypte.server.Terminal;
import calypte.server.TerminalConstants;
import calypte.server.TerminalReader;
import calypte.server.TerminalWriter;
import calypte.server.error.ServerErrorException;
import calypte.server.error.ServerErrors;

/**
 * Representa o comando <code>rollback</code>.
 * Sua sintaxe é:
 * <pre>
 * rollback
 * </pre> 
 * @author Brandao
 *
 */
public class RollbackTransactionCommand 
	extends AbstractCommand{

	public void executeCommand(Terminal terminal, Cache cache, TerminalReader reader,
			TerminalWriter writer, byte[][] parameters)
			throws Throwable {

		if(!(cache instanceof TXCache)){
			throw new ServerErrorException(ServerErrors.ERROR_1009);
		}
		
		TXCache txCahe = (TXCache)cache;
		CacheTransaction tx = txCahe.getTransactionManager().getCurrrent();
		tx.rollback();
		
        writer.sendMessage(TerminalConstants.SUCCESS_DTA);
        writer.flush();
		
	}

}
