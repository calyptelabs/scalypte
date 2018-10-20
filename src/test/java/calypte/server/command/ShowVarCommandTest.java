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

import java.awt.EventQueue;
import java.io.IOException;

import calypte.Configuration;
import calypte.server.CalypteServer;
import calypte.server.ClientHelper;
import junit.framework.TestCase;

public class ShowVarCommandTest extends TestCase{

	private CalypteServer server;
	
	private ClientHelper client;
	
	@Override
	public void setUp() throws Exception{
		Configuration config = new Configuration();
		this.server = new CalypteServer(config);
		EventQueue.invokeLater(new Runnable(){

			public void run() {
				try{
					server.start();
				}
				catch(Throwable e){
					e.printStackTrace();
				}
				
			}
			
		});

		Thread.sleep(1000);
		
		client = new ClientHelper();
		
	}

	@Override
	public void tearDown() throws Exception{
		this.server.stop();
		this.server = null;
		this.client.close();
		this.client = null;
		System.gc();
	}

	/*  */

	public void testShow() throws IOException {
		client.send("show_var curr_connections");
		String r = client.read();
		assertTrue(r.matches("^curr_connections\\:\\s.+$"));
	}

	public void testShowEmpty() throws IOException {
		client.send("show_var dddddddd");
		String r = client.read();
		assertTrue(r.matches("^dddddddd\\: empty$"));
	}
	
	
}
