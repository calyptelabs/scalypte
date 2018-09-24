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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import calypte.Cache;

import calypte.server.Terminal;
import calypte.server.TerminalConstants;
import calypte.server.TerminalReader;
import calypte.server.TerminalVars;
import calypte.server.TerminalWriter;

/**
 * Representa o comando <code>show_vars</code>.
 * Sua sintaxe é:
 * <pre>
 * show_vars
 * </pre> 
 * @author Ribeiro
 *
 */
public class ShowVarsCommand 
	extends AbstractCommand{

    private Runtime runtime;

    public ShowVarsCommand(){
    	this.runtime = Runtime.getRuntime();
    }
    
	public void executeCommand(Terminal terminal, Cache cache, TerminalReader reader,
			TerminalWriter writer, byte[][] parameters)
			throws Throwable {
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		TerminalVars vars = terminal.getTerminalVars();

        for(String prop: vars.keySet()){
        	map.put(prop, vars.get(prop));
        }

        map.put("read_entry",   cache.getCountRead());
        map.put("read_data",    cache.getCountReadData());
        map.put("write_entry",  cache.getCountWrite());
        map.put("removed_data", cache.getCountRemoved());
        map.put("total_memory", runtime.totalMemory());
        map.put("free_memory",  runtime.freeMemory());
        map.put("used_memory",  runtime.totalMemory() - runtime.freeMemory());
        
		StringBuilder result = new StringBuilder();
        
		List<String> keys = new ArrayList<String>(map.keySet());
		Collections.sort(keys);
        for(String prop: keys){
            result
            	.append(prop).append(": ")
            	.append(map.get(prop)).append(TerminalConstants.CRLF);
        }
        
        result.append(TerminalConstants.BOUNDARY);
        writer.sendMessage(result.toString());
        writer.flush();
		
	}

}
