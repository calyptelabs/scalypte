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

import calypte.CacheConstants;
import calypte.Configuration;
import calypte.server.CalypteServer;
import calypte.server.ClientHelper;
import junit.framework.TestCase;

public class RemoveCommandTest extends TestCase{

	private CalypteServer server;
	
	private ClientHelper client;
	
	@Override
	public void setUp() throws Exception{
		Configuration config = new Configuration();
        config.setProperty(CacheConstants.NODES_BUFFER_SIZE, "1536k");
        config.setProperty(CacheConstants.INDEX_BUFFER_SIZE, "1536k");
        config.setProperty(CacheConstants.DATA_BUFFER_SIZE,  "1536k");
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
	
	public void testNotFoundKXL0() throws IOException {
		client.send("remove");
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}

	public void testNotFoundK1L0() throws IOException {
		client.send("remove");
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}

	public void testNotFoundK1LX() throws IOException {
		client.send("remove k");
		String r = client.read();
		assertEquals("not_found", r);
	}
	
	public void testNotFoundKxL1() throws IOException {
		client.send("remove key");
		String r = client.read();
		assertEquals("not_found", r);
	}

	public void testNotFoundKxLx() throws IOException {
		client.send("remove key");
		String r = client.read();
		assertEquals("not_found", r);
	}

	/*  */

	public void testOkKXL0() throws IOException {
		client.send("remove");
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}
	
	public void testOkK1L0() throws IOException {
		client.send("remove");
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}

	public void testOkK1LX() throws IOException {
		client.send("put k 0 0 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);
		
		client.send("remove k");
		r = client.read();
		assertEquals("ok", r);
	}
	
	public void testOkKxL1() throws IOException {
		client.send("put key 0 0 1 0");
		client.send("a\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);
		
		client.send("remove key");
		r = client.read();
		assertEquals("ok", r);
	}

	public void testOkKxLx() throws IOException {
		client.send("put key 0 0 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);
		
		client.send("remove key");
		r = client.read();
		assertEquals("ok", r);
	}
	
	/*  */

	public void testFlushKXL0() throws IOException {
		client.send("remove");
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}
	
	public void testFlushK1L0() throws IOException {
		client.send("remove");
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}

	public void testFlushK1LX() throws IOException {
		client.send("put k 0 0 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);

		client.send("flush");
		r = client.read();
		assertEquals("ok", r);
		
		client.send("remove k");
		r = client.read();
		assertEquals("not_found", r);
	}
	
	public void testFlushKxL1() throws IOException {
		client.send("put key 0 0 1 0");
		client.send("a\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);
		
		client.send("flush");
		r = client.read();
		assertEquals("ok", r);
		
		client.send("remove key");
		r = client.read();
		assertEquals("not_found", r);
	}

	public void testFlushKxLx() throws IOException {
		client.send("put key 0 0 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);
		
		client.send("flush");
		r = client.read();
		assertEquals("ok", r);
		
		client.send("remove key");
		r = client.read();
		assertEquals("not_found", r);
	}
	
}
