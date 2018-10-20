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

public class GetCommandTest extends TestCase{

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

	public void testKXL0() throws IOException {
		client.send("get key ");
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}
	
	public void testK1L0() throws IOException {
		client.send("get k ");
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}

	public void testdK1LX() throws IOException {
		client.send("put k 0 0 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);
		
		client.send("get k 0");
		r = client.read();
		assertEquals("value k 5 0", r);
		r = new String(client.read(5));
		assertEquals("teste", r);
		r = client.read();
		assertEquals("end", r);
	}
	
	public void testKxL1() throws IOException {
		client.send("put key 0 0 1 0");
		client.send("a\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);
		
		client.send("get key 0");
		r = client.read();
		assertEquals("value k 1 0", r);
		r = new String(client.read(1));
		assertEquals("a", r);
		r = client.read();
		assertEquals("end", r);
	}

	public void testKxLx() throws IOException {
		client.send("put key 0 0 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);
		
		client.send("get key 0");
		r = client.read();
		assertEquals("value key 5 0", r);
		r = new String(client.read(5));
		assertEquals("teste", r);
		r = client.read();
		assertEquals("end", r);
	}
	
	/*  */

	public void testRemoveStoredKXL0() throws IOException {
		client.send("get key");
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}
	
	public void testRemoveStoredK1L0() throws IOException {
		client.send("get k");
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}

	public void testStoredK1LX() throws IOException {
		client.send("put k 0 0 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);

		client.send("remove k 0");
		r = client.read();
		assertEquals("ok", r);
		
		client.send("get k 0");
		client.send("value k 0 0\r\n".getBytes());
		r = client.read();
		assertEquals("end", r);
	}
	
	public void testStoredKxL1() throws IOException {
		client.send("put key 0 0 1 0");
		client.send("a\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);
		
		client.send("remove key 0");
		r = client.read();
		assertEquals("ok", r);
		
		client.send("get key 0");
		client.send("value key 0 0\r\n".getBytes());
		r = client.read();
		assertEquals("end", r);
	}

	public void testStoredKxLx() throws IOException {
		client.send("put key 0 0 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);
		
		client.send("remove key 0");
		r = client.read();
		assertEquals("ok", r);
		
		client.send("get key 0");
		client.send("value key 0 0\r\n".getBytes());
		r = client.read();
		assertEquals("end", r);
	}
	
	/*  */

	public void testFlushStoredKXL0() throws IOException {
		client.send("get key");
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}
	
	public void testFlushStoredK1L0() throws IOException {
		client.send("get k");
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}

	public void testFlushStoredK1LX() throws IOException {
		client.send("put k 0 0 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);

		client.send("flush");
		r = client.read();
		assertEquals("ok", r);
		
		client.send("get k 0");
		client.send("value k 0 0\r\n".getBytes());
		r = client.read();
		assertEquals("end", r);
	}
	
	public void testFlushStoredKxL1() throws IOException {
		client.send("put key 0 0 1 0");
		client.send("a\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);
		
		client.send("flush");
		r = client.read();
		assertEquals("ok", r);
		
		client.send("get key 0");
		client.send("value key 0 0\r\n".getBytes());
		r = client.read();
		assertEquals("end", r);
	}

	public void testFlushStoredKxLx() throws IOException {
		client.send("put key 0 0 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);
		
		client.send("flush");
		r = client.read();
		assertEquals("ok", r);
		
		client.send("get key 0");
		client.send("value key 0 0\r\n".getBytes());
		r = client.read();
		assertEquals("end", r);
	}
	
	/*  */
	
	public void testReservedChar() throws IOException {
		client.send("get key a");
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}
	
}
