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

public class ReplaceCommandTest extends TestCase{

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
	
	public void testStoredKXL0() throws IOException {
		client.send("replace key 0 0 0 0");
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}

	public void testStoredK1L0() throws IOException {
		client.send("replace k 0 0 0 0");
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}

	public void testStoredK1LX() throws IOException {
		client.send("replace k 0 0 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("not_stored", r);
	}
	
	public void testStoredKxL1() throws IOException {
		client.send("replace key 0 0 1 0");
		client.send("a\r\n".getBytes());
		String r = client.read();
		assertEquals("not_stored", r);
	}

	public void testStoredKxLx() throws IOException {
		client.send("replace key 0 0 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("not_stored", r);
	}

	/*  */

	public void testReplacedKXL0() throws IOException {
		client.send("replace key 0 0 0 0");
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}
	
	public void testReplacedK1L0() throws IOException {
		client.send("replace k 0 0 0 0");
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}

	public void testReplacedK1LX() throws IOException {
		client.send("put k 0 0 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);
		
		client.send("replace k 0 0 5 0");
		client.send("teste\r\n".getBytes());
		r = client.read();
		assertEquals("replaced", r);
	}
	
	public void testReplacedKxL1() throws IOException {
		client.send("put key 0 0 1 0");
		client.send("a\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);
		
		client.send("replace key 0 0 5 0");
		client.send("teste\r\n".getBytes());
		r = client.read();
		assertEquals("replaced", r);
	}

	public void testReplacedKxLx() throws IOException {
		client.send("put key 0 0 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);
		
		client.send("replace key 0 0 5 0");
		client.send("teste\r\n".getBytes());
		r = client.read();
		assertEquals("replaced", r);
	}
	
	/*  */

	public void testRemoveStoredKXL0() throws IOException {
		client.send("replace key 0 0 0 0");
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}
	
	public void testRemoveStoredK1L0() throws IOException {
		client.send("replace k 0 0 0 0");
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}

	public void testRemoveStoredK1LX() throws IOException {
		client.send("put k 0 0 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);

		client.send("remove k 0");
		r = client.read();
		assertEquals("ok", r);
		
		client.send("replace k 0 0 5 0");
		client.send("teste\r\n".getBytes());
		r = client.read();
		assertEquals("not_stored", r);
	}
	
	public void testRemoveStoredKxL1() throws IOException {
		client.send("put key 0 0 1 0");
		client.send("a\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);
		
		client.send("remove key 0");
		r = client.read();
		assertEquals("ok", r);
		
		client.send("replace key 0 0 5 0");
		client.send("teste\r\n".getBytes());
		r = client.read();
		assertEquals("not_stored", r);
	}

	public void testRemoveStoredKxLx() throws IOException {
		client.send("put key 0 0 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);
		
		client.send("remove key 0");
		r = client.read();
		assertEquals("ok", r);
		
		client.send("replace key 0 0 5 0");
		client.send("teste\r\n".getBytes());
		r = client.read();
		assertEquals("not_stored", r);
	}
	
	/*  */

	public void testFlushStoredKXL0() throws IOException {
		client.send("replace key 0 0 0 0");
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}
	
	public void testFlushStoredK1L0() throws IOException {
		client.send("replace k 0 0 0 0");
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
		
		client.send("replace k 0 0 5 0");
		client.send("teste\r\n".getBytes());
		r = client.read();
		assertEquals("not_stored", r);
	}
	
	public void testFlushStoredKxL1() throws IOException {
		client.send("put key 0 0 1 0");
		client.send("a\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);
		
		client.send("flush");
		r = client.read();
		assertEquals("ok", r);
		
		client.send("replace key 0 0 5 0");
		client.send("teste\r\n".getBytes());
		r = client.read();
		assertEquals("not_stored", r);
	}

	public void testFlushStoredKxLx() throws IOException {
		client.send("put key 0 0 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);
		
		client.send("flush");
		r = client.read();
		assertEquals("ok", r);
		
		client.send("replace key 0 0 5 0");
		client.send("teste\r\n".getBytes());
		r = client.read();
		assertEquals("not_stored", r);
	}
	
	/*  */
	
	public void testTimeToLiveNeg() throws IOException {
		client.send("replace key -1 0 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}

	public void testTimeToIdleNeg() throws IOException {
		client.send("replace key 0 -1 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}

	public void testSizeNeg() throws IOException {
		client.send("replace key 0 0 -1 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}

	/*  */
	
	public void testTimeToLiveChar() throws IOException {
		client.send("replace key a 0 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}

	public void testTimeToIdleChar() throws IOException {
		client.send("replace key 0 a 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}

	public void testSizeChar() throws IOException {
		client.send("replace key 0 0 a 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}

	/*  */
	
	public void testParamsError() throws IOException {
		client.send("replace key 0 0 0 0 0");
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}
	
}
