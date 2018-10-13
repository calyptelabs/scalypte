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
import java.util.Map.Entry;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import calypte.Configuration;

/**
 * Classe de lançamento da aplicação.
 * 
 * @author Ribeiro
 */
public class Main {
    
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	
	/**
	 * Método principal da aplicação.
	 * @param params Configuração de linha de comando.
	 * @throws IOException Lançada se ocorre alguma falha na leitura dos arquivos de configuração.
	 */
    public static void main(String[] params) throws IOException{
    	StartParamsParser paramsParser = new StartParamsParser(params);
    	start(paramsParser);
    }
    
    /**
     * Configura e inicia a aplicação.
     * @param paramsParser Lista de parâmetros recebidos via linha de comando.
     * @throws FileNotFoundException Lançada se o arquivo de configuração ou logger não forem encontrados.
     * @throws IOException Lançada se ocorre alguma falha na leitura dos arquivos de configuração.
     */
    private static void start(StartParamsParser paramsParser) throws FileNotFoundException, IOException{
    	discoverServerDir(paramsParser);
    	loadLoggerConfig(paramsParser);
    	Configuration config = getConfiguration(paramsParser);
    	//autoConfig(config);
    	startServer(config);
    }

    /**
     * Inicia o servidor.
     * @param config Configuração do servidor.
     */
    private static void startServer(Configuration config) {
        try{
            CalypteServer server = new CalypteServer(config);
            server.start();
        }
        catch(Throwable e){
            logger.error("fail to start server", e);
            System.exit(2);
        }
    }
    
    /**
     * Faz a descoberta do diretório do servidor.
     * @param paramsParser Configuração de linha de comando.
     */
    private static void discoverServerDir(StartParamsParser paramsParser) {
    	File configFile = new File(paramsParser.getConfigFile());
    	File path = configFile.getParentFile();
    	System.setProperty("server.dir", path.getAbsolutePath());
    }
    
    /**
     * Faz a auto configuração do servidor baseada no limite de memória informado. 
     * @param memory Limite de memória que o servidor poderá usar.
     */
    private static void autoConfig(Configuration config) {
    	
    	long memory = config.getLong("max_memory", "0b");
    	
    	if(memory == 0) {
    		return;
    	}
    	
    	long write_buffer_size = config.getLong("write_buffer_size", (6*1024) + "b");
    	long read_buffer_size  = config.getLong("read_buffer_size",  (6*1024) + "b");
    	long data_block_size   = config.getLong("data_block_size",   1024 + "b");
    	long xmx;
    	long ext_heap;
    	long connections_data_size;
    	long connections;
    	long nodes_buffer_size;
    	long index_buffer_size;
    	long data_buffer_size;
    	
    	if(memory >= 16L*1024L*1024L*1024L) {
    		memory = memory - 512L*1024L*1024L;
    		connections = 3000L;
    		ext_heap = (long)(1.6*1024L*1024L*1024L);
    	}
    	else
    	if(memory >= 2L*1024L*1024L*1024L) {
    		memory = memory - 256L*1024L*1024L;
    		connections = 1000L;
    		ext_heap = (long)(0.1*memory);
    	}
    	else
    	if(memory >= 512L*1024L*1024L) {
    		memory = memory - 50L*1024L*1024L;
    		connections = 400L;
    		ext_heap = (long)(0.1*memory);
    	}
    	else
    	if(memory >= 512000000) {
    		memory = memory - 50L*1024L*1024L;
    		connections = 200L;
    		ext_heap = 45L*1024L*1024L;
    	}
    	else
    		return;
    	
    	connections_data_size = (long)(connections*229L*1024L);
    	memory -= connections_data_size;
    	
    	xmx = memory;
    	
    	memory -= ext_heap;
    	
    	if(memory >= 16L*1024L*1024L*1024L) {
    		nodes_buffer_size = (long)(memory*0.35);
    		index_buffer_size = (long)(memory*0.15);
    		data_buffer_size  = (long)(memory*0.50);
    	}
    	else
    	if(memory >= 2L*1024L*1024L*1024L) {
    		nodes_buffer_size = (long)(memory*0.45);
    		index_buffer_size = (long)(memory*0.20);
    		data_buffer_size  = (long)(memory*0.35);
    	}
    	else
    	if(memory >= 512L*1024L*1024L) {
    		nodes_buffer_size = (long)(memory*0.45);
    		index_buffer_size = (long)(memory*0.20);
    		data_buffer_size  = (long)(memory*0.35);
    	}
    	else
    	if(memory >= 512000000L) {
    		nodes_buffer_size = (long)(memory*0.45);
    		index_buffer_size = (long)(memory*0.20);
    		data_buffer_size  = (long)(memory*0.35);
    	}
    	else
    		return;
    	
    	config.setProperty("write_buffer_size",     write_buffer_size + "b");
    	config.setProperty("read_buffer_size", 		read_buffer_size + "b");
		config.setProperty("data_block_size",       data_block_size + "b");
		config.setProperty("xmx",                   xmx + "b");
		config.setProperty("nodes_buffer_size",     nodes_buffer_size + "b");
		config.setProperty("index_buffer_size",     index_buffer_size + "b");
		config.setProperty("data_buffer_size",      data_buffer_size + "b");
    	config.setProperty("max_connections",       String.valueOf(connections));
    }
    
    /**
     * Obtém a configuração do arquivo a partir do arquivo de configuração. 
     * @param paramsParser Configuração de linha de comando.
     * @return Configuração do servidor.
     * @throws IOException Lançada se ocorre alguma falha ao tentar obter os dados do arquivo de
     * configuração. 
     */
    private static Configuration getConfiguration(StartParamsParser paramsParser) throws IOException{
    	
    	String configFile = paramsParser.getConfigFile();
    	
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
        
        for(Entry<String,String> p: paramsParser.getConfiguration().entrySet()) {
        	config.setProperty(p.getKey(), p.getValue());
        }
        
        return config;
    }
    
    /**
     * Configura o gerador de log da aplicação.
     * @return Configuração do servidor.
     * @throws IOException Lançada se ocorre alguma falha ao tentar obter os dados do arquivo de
     * configuração. 
     */
    private static void loadLoggerConfig(StartParamsParser paramsParser) throws IOException{

    	String loggerConfigFile = paramsParser.getLoggerFile();
    	
    	File f = new File(loggerConfigFile);

        if(!f.exists() || !f.canRead()){
        	throw new IOException("logger file not found");
        }
    	
		PropertyConfigurator.configure(loggerConfigFile);
    }
    
}
