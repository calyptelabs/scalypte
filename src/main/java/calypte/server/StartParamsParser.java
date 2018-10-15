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

import java.util.HashMap;
import java.util.Map;

public class StartParamsParser {

	private String configFile;
	
	private String loggerFile;
	
	private String loggerPath;
	
	private String[] params;
	
	private Map<String, String> configuration;
	
	public StartParamsParser(String[] params){
		this.params        = params;
		this.configFile    = "./calypte.conf";
		this.loggerFile    = "./log4j.configuration";
		this.loggerPath    = "/var/log/calypte";
		this.configuration = new HashMap<String, String>();
		this.parser();
	}
	
	private void parser(){

		for(String param: params){
			if(param.startsWith("--default-file")){
				String[] parts = param.split("\\=");
				if(parts.length != 2 || parts[1].trim().isEmpty()){
					throw new IllegalStateException("expected --default-file=<path>");
				}
				this.configFile = parts[1].trim();
			}
			else
			if(param.startsWith("--logger")){
				String[] parts = param.split("\\=");
				if(parts.length != 2 || parts[1].trim().isEmpty()){
					throw new IllegalStateException("expected --logger=<path>");
				}
				this.loggerFile = parts[1].trim();
			}
			else
			if(param.startsWith("--log_path")){
				String[] parts = param.split("\\=");
				if(parts.length != 2 || parts[1].trim().isEmpty()){
					throw new IllegalStateException("expected --log_path=<path>");
				}
				this.loggerPath = parts[1].trim();
			}
			else
			if(param.startsWith("--P")){
				String[] parts = param.split("\\=");
				parts[0] = parts[0].replaceAll("^\\-\\-P", "");
				if(parts.length != 2 || parts[1].trim().isEmpty()){
					throw new IllegalStateException("expected --P<property>=<value>");
				}
				
				this.configuration.put(parts[0], parts[1]);
			}
			
		}
	}

	public String getConfigFile() {
		return configFile;
	}

	public String getLoggerFile() {
		return loggerFile;
	}
	
	public String getLoggerPath() {
		return loggerPath;
	}

	public Map<String, String> getConfiguration(){
		return configuration;
	}
	
}
