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

package br.com.uoutec.installer.action;

import br.com.uoutec.installer.AbstractInstallerActionExecutor;
import br.com.uoutec.installer.InstallerActionExecuterException;
import br.com.uoutec.installer.entity.Configuration;
import br.com.uoutec.installer.entity.InstallerAction;
import br.com.uoutec.installer.entity.InstallerConfig;
import br.com.uoutec.installer.painel.flow.InstallerPainel;
import br.com.uoutec.installer.painel.flow.UninstallerPanel;
import calypte.installer.action.entity.ServerInstance;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.management.ManagementFactory;
import java.util.Set;

/**
 *
 * @author Ribeiro
 */
public class ServerInstanceActionExecutor extends AbstractInstallerActionExecutor{

    private static final double MB = 1024*1024;
    
    public Class<? extends InstallerAction> getType() {
        return ServerInstanceInstallerAction.class;
    }

    public void install(InstallerConfig installerConfig, 
            InstallerPainel installerPainel, Set<InstallerAction> value, 
            Set<InstallerAction> logInstaller) throws InstallerActionExecuterException {
        
        com.sun.management.OperatingSystemMXBean os = 
                (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        
        double totalMemory     = os.getTotalPhysicalMemorySize();
        double developMemory   = 128*MB;
        double serverMemory    = totalMemory * 0.25;
        double dedicatedMemory = totalMemory * 0.9;

/*        
        double nodes_buffer_size = developMemory * 0.50;
        double index_buffer_size = developMemory * 0.20;
        double data_buffer_size  = developMemory * 0.30;
        int heapMemory           = (int)developMemory;

        ServerInstanceInstallerAction sia = (ServerInstanceInstallerAction) value.toArray()[0];
        
        if(sia.getServerInstance() == ServerInstance.SERVER){
            nodes_buffer_size = serverMemory * 0.50;
            index_buffer_size = serverMemory * 0.20;
            data_buffer_size  = serverMemory * 0.30;
            heapMemory        = (int)serverMemory;
        }
        else
        if(sia.getServerInstance() == ServerInstance.DEDICATED_SERVER){
            nodes_buffer_size = dedicatedMemory * 0.50;
            index_buffer_size = dedicatedMemory * 0.20;
            data_buffer_size  = dedicatedMemory * 0.30;
            heapMemory        = (int)dedicatedMemory;
        }
        
        Configuration config = installerConfig.getConfiguration();
        config.setProperty("heap", String.valueOf(heapMemory));
        config.setProperty("nodes_buffer_size", String.valueOf((int)nodes_buffer_size));
        config.setProperty("index_buffer_size", String.valueOf((int)index_buffer_size));
        config.setProperty("data_buffer_size", String.valueOf((int)data_buffer_size));
        */

        ServerInstanceInstallerAction sia = (ServerInstanceInstallerAction) value.toArray()[0];
        Configuration config = installerConfig.getConfiguration();

        switch (sia.getServerInstance()) {
            case DEVELOPER:
                autoConfig((long)developMemory, config);
                break;
            case SERVER:
                autoConfig((long)serverMemory, config);
                break;
            default:
                autoConfig((long)dedicatedMemory, config);
                break;
        }
        
        logInstaller.add(sia);
        
        String fileData = installerConfig.getConfiguration().getValueProperty("config_file");
        String fileName = installerConfig.getConfiguration().getValue("{app}{appseparator}calypte.conf");
        File f = new File(fileName);
        
        FileOutputStream fout = null;
        
        try{
            fout = new FileOutputStream(f);
            fout.write(fileData.getBytes());
            fout.flush();
        }
        catch(Throwable e){
            throw new InstallerActionExecuterException(e);
        }
        finally{
            if(fout != null){
                try{
                    fout.close();
                }
                catch(Throwable e){
                }
            }
        }
    }

    private static void autoConfig(long memory, Configuration config) {

	/*
	 *   *---------------------------------------------------------------------*
	 *   |                                 memory                              |
	 *   *------------------------------------------------------*--------------*
	 *   |                          -Xmx?mb                     |  -Xss228k    |
	 *   |------------------------------------------------------*--------------|   
	 *   |                           heap                       |    stack     |        
	 *   |______________________________________________________|              |
	 *   |-----------|-------|--------|-------------*-----------|--------------|
	 *   |    node     index    data    free_heap (connections)   threads(con) |
	 *   
	 * stack     = 228k*connections
	 * free_heap = connections*threadUsage
	 * 
	 * connections = stack/228k
	 * connections = free_heap/threadUsage
	 * 
	 * stack/228k = free_heap/threadUsage
	 * 
	 * if threadUsage > 228k
	 * stack = (free_heap/threadUsage)*228k
	 *
	 * if 228k > threadUsage
	 * free_heap = (stack/228k)*threadUsage
	 * 
	*/
    	
    	if(memory < 128L*1024L*1024L) {
            throw new IllegalStateException("max_memory < 128mb");
    	}
    	
    	long keyLength         = 256;
    	long write_buffer_size = 6*1024;
    	long read_buffer_size  = 6*1024;
    	long data_block_size   = 1024;
    	long threadUsage       = write_buffer_size + read_buffer_size + 9*1024 + keyLength;
    	long xmx;
    	long stack;
    	long ext_heap;
    	long connections;
    	long nodes_buffer_size;
    	long index_buffer_size;
    	long data_buffer_size;
    	
    	if(memory >= 16L*1024L*1024L*1024L) {
            stack                 = (long)(1.6*1024L*1024L*1024L);
            connections           = stack/(228L*1024L);
            ext_heap              = connections*threadUsage;

            memory -= stack;
            xmx = memory;

            memory -= ext_heap;

            nodes_buffer_size     = (long)(memory*0.50);
            index_buffer_size     = (long)(memory*0.05);
            data_buffer_size      = (long)(memory*0.45);
    	}
    	else
    	if(memory >= 2L*1024L*1024L*1024L) {
            stack                 = (long)(memory*0.1);
            connections           = stack/(228L*1024L);
            ext_heap              = connections*threadUsage;

            memory -= stack;
            xmx = memory;

            memory -= ext_heap;

            nodes_buffer_size     = (long)(memory*0.50);
            index_buffer_size     = (long)(memory*0.05);
            data_buffer_size      = (long)(memory*0.45);
    	}
    	else
    	if(memory >= 512L*1024L*1024L) {
            stack                 = (long)(memory*0.15);
            connections           = stack/(228L*1024L);
            ext_heap              = connections*threadUsage;

            memory -= stack;
            xmx = memory;

            memory -= ext_heap;

            nodes_buffer_size     = (long)(memory*0.50);
            index_buffer_size     = (long)(memory*0.05);
            data_buffer_size      = (long)(memory*0.45);
    	}
        else{
            stack                 = 45L*1024L*1024L;
            connections           = stack/(228L*1024L);
            ext_heap              = connections*threadUsage;

            memory -= stack;
            xmx = memory;

            memory -= ext_heap;

            nodes_buffer_size     = (long)(memory*0.50);
            index_buffer_size     = (long)(memory*0.05);
            data_buffer_size      = (long)(memory*0.45);
    	}
    	
    	config.setProperty("write_buffer_size",     (write_buffer_size/1024) + "k");
    	config.setProperty("read_buffer_size",      (read_buffer_size/1024) + "k");
	config.setProperty("data_block_size",       (data_block_size/1024) + "k");
	config.setProperty("heap",                  (xmx/1024) + "k");
	config.setProperty("total_xss",             (stack/1024) + "k");
	config.setProperty("nodes_buffer_size",     (nodes_buffer_size/1024) + "k");
	config.setProperty("index_buffer_size",     (index_buffer_size/1024) + "k");
	config.setProperty("data_buffer_size",      (data_buffer_size/1024) + "k");
    	config.setProperty("max_connections",       String.valueOf(connections - 1));
    	
    }
    
    public void uninstall(InstallerConfig installerConfig, UninstallerPanel installerPainel, Set<InstallerAction> value) throws InstallerActionExecuterException {
        String fileName = installerConfig.getConfiguration().getValue("{app}{appseparator}calypte.conf");
        File f = new File(fileName);
        f.delete();
    }
    
}
