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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import calypte.Configuration;

/**
 * 
 * @author Ribeiro
 */
public class Main {
    
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	
    public static void main(String[] params) throws IOException{
    	StartParamsParser paramsParser = new StartParamsParser(params);
    	start(paramsParser);
    }
    
    private static void start(StartParamsParser paramsParser) throws FileNotFoundException, IOException{
    	discoverServerDir(paramsParser);
    	loadLoggerConfig(paramsParser.getLoggerFile());
    	startServer(paramsParser);
    }

    private static void startServer(StartParamsParser paramsParser) {
        try{
        	Configuration config = getConfiguration(paramsParser.getConfigFile());
            CalypteServer server = new CalypteServer(config);
            server.start();
        }
        catch(Throwable e){
            logger.error("fail to start server", e);
            System.exit(2);
        }
    }
    
    private static void discoverServerDir(StartParamsParser paramsParser) {
    	File configFile = new File(paramsParser.getConfigFile());
    	File path = configFile.getParentFile();
    	String[] list = path.list();
    	System.setProperty("server.dir", path.getAbsolutePath());
    }
    
    private static Configuration getConfiguration(String configFile) throws IOException{
    	
    	logger.info("loading configuration file: " + configFile);
    	
    	File f = new File(configFile);

        if(!f.exists() || !f.canRead()){
        	throw new IOException("configuration file not found");
        }
    	
        FileInputStream fin   = new FileInputStream(f);
    	StringBuilder builder = new StringBuilder();
        try{
        	int l;
        	byte[] b = new byte[2048];
        	while((l = fin.read(b)) > 0){
        		builder.append(new String(b, 0, l));
        	}
        	
        }
        finally{
        	fin.close();
        }
        
        String propStr = builder.toString();
        propStr = propStr.replace("\\","\\\\");
        
        Configuration config = new Configuration();
        config.load(new StringReader(propStr));
        
        return config;
    }
    
    private static void loadLoggerConfig(String loggerConfigFile) throws IOException{

    	File f = new File(loggerConfigFile);

        if(!f.exists() || !f.canRead()){
        	throw new IOException("logger file not found");
        }
    	
		PropertyConfigurator.configure(loggerConfigFile);
    }
    
}
