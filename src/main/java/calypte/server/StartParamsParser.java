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

public class StartParamsParser {

	private String configFile;
	
	private String loggerFile;
	
	private String[] params;
	
	public StartParamsParser(String[] params){
		this.params             = params;
		this.configFile         = "./calypte.conf";
		this.loggerFile         = "./log4j.configuration";
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
				
		}
	}

	public String getConfigFile() {
		return configFile;
	}

	public String getLoggerFile() {
		return loggerFile;
	}
	
}
