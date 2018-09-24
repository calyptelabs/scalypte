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

import calypte.Configuration;

public class Bootstrap {

	private static CalypteServer server;
	
	public static void start(String[] args) throws FileNotFoundException, IOException{
    	StartParamsParser paramsParser = new StartParamsParser(args);
    	
        File f = new File(paramsParser.getConfigFile());
        
        if(!f.exists() || !f.canRead()){
            System.out.println("configuration not found!");
            System.exit(2);
        }
            
        Configuration config = new Configuration();
        config.load(new FileInputStream(f));
     
        try{
            server = new CalypteServer(config);
            server.start();
        }
        catch(Throwable e){
            e.printStackTrace();
            System.exit(2);
        }
    	
	}
	
	public static void stop(String[] args) throws IOException{
		server.stop();
        System.exit(0);
	}
	
}
