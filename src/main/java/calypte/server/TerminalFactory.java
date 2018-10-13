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

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ribeiro
 */
class TerminalFactory {
    
	private static final Logger logger = LoggerFactory.getLogger(TerminalFactory.class);
	
    private int createdInstances;
    
    private final int minInstances;
    
    private final int maxInstances;
    
    private final BlockingQueue<Terminal> instances;

    private int countInstances;
    
    private int currentInstances;
    
    public TerminalFactory(int minInstances, int maxInstances){

        if(minInstances < 0)
            throw new IllegalArgumentException("minInstances");
        
        if(maxInstances < 1)
            throw new IllegalArgumentException("maxInstances");
        
        if(minInstances > maxInstances)
            throw new IllegalArgumentException("minInstances");

        this.minInstances     = minInstances;
        this.maxInstances     = maxInstances;
        this.instances        = new ArrayBlockingQueue<Terminal>(this.maxInstances);
        this.createdInstances = 0;
        
        for(int i=0;i<this.minInstances;i++)
            this.instances.add(new Terminal());
        
    }
    
    public Terminal getInstance() throws InterruptedException {
        
        Terminal terminal = this.instances.poll();
        
        if(terminal == null){
        	synchronized(this) {
	            if(this.createdInstances < this.maxInstances){
	                terminal = new Terminal();
	                this.createdInstances++;
	            }
        	}

        	if(terminal == null) {
        		terminal = this.instances.take();
        	}
        }
        
        this.countInstances++;
        this.currentInstances++;
        return terminal;
    }

    public Terminal tryGetInstance(long l, TimeUnit tu) throws InterruptedException {
        
        Terminal terminal = this.instances.poll();
        
        if(terminal == null){
        	synchronized (this) {
	            if(this.createdInstances < this.maxInstances){
	                terminal = new Terminal();
	                this.createdInstances++;
	            }
        	}
        	
        	if(terminal == null) {
                terminal = this.instances.poll(l, tu);
        	}
        }
        
        this.countInstances++;
        this.currentInstances++;
        return terminal;
    }
    
    public void release(Terminal terminal){
    	synchronized(this) {
	        this.currentInstances--;
	        this.instances.add(terminal);
    	}
    }
    
    public int getMinInstances() {
        return minInstances;
    }

    public int getMaxInstances() {
        return maxInstances;
    }

    public synchronized int getCountInstances() {
        return countInstances;
    }

    public synchronized int getCurrentInstances() {
        return currentInstances;
    }
    
}
