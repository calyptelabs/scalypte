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

import calypte.Configuration;

/**
 * 
 * @author Ribeiro
 */
public class Main {
    
    public static void main(String[] params) throws IOException{
    	StartParamsParser paramsParser = new StartParamsParser(params);
    	start(paramsParser.getConfigFile());
    }
    
    private static void start(String configFile) throws FileNotFoundException, IOException{
        File f = new File(configFile);
        
        if(!f.exists() || !f.canRead()){
            System.out.println("configuration not found!");
            System.exit(2);
        }
            
        try{
            CalypteServer server = new CalypteServer(loadConfig(f));
            server.start();
        }
        catch(Throwable e){
            e.printStackTrace();
            System.exit(2);
        }
    }

    private static Configuration loadConfig(File f) throws IOException{
    	
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
}
