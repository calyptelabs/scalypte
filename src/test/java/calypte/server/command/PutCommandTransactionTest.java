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

public class PutCommandTransactionTest extends TestCase{

	private CalypteServer server;
	
	private ClientHelper client;
	
	@Override
	public void setUp() throws Exception{
		Configuration config = new Configuration();
		config.setProperty("transaction_support", "true");
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
		client.testRequest(
				">>put key 0 0 0 0",
				"<<ERROR 1004: Bad command syntax error!"
		);
	}

	public void testStoredK1L0() throws IOException {
		client.testRequest(
				">>put k 0 0 0 0",
				"<<ERROR 1004: Bad command syntax error!"
		);
	}

	public void testStoredK1LX() throws IOException {
		client.testRequest(
				">>put k 0 0 5 0",
				">>teste",
				"<<stored"
		);
	}
	
	public void testStoredKxL1() throws IOException {
		client.testRequest(
				">>put key 0 0 1 0",
				">>a",
				"<<stored"
		);
	}

	public void testStoredKxLx() throws IOException {
		client.testRequest(
				">>put key 0 0 5 0",
				">>teste",
				"<<stored"
		);
	}

	/* -- */
	
	public void testStoredTransactionKXL0() throws IOException {
		client.testRequest(
				">>begin",
				"<<ok",
				">>put key 0 0 0 0",
				"<<ERROR 1004: Bad command syntax error!",
				">>get key 0",
				"<<value key 0 0",
				"<<end",
				">>rollback",
				"<<ok",
				">>get key 0",
				"<<value key 0 0",
				"<<end"
		);
	}
	
	/*  */

	public void testReplacedKXL0() throws IOException {
		client.testRequest(
				">>put key 0 0 0 0",
				"<<ERROR 1004: Bad command syntax error!"
		);
	}
	
	public void testReplacedK1L0() throws IOException {
		client.testRequest(
				">>put k 0 0 0 0",
				"<<ERROR 1004: Bad command syntax error!"
		);
	}

	public void testReplacedK1LX() throws IOException {
		client.testRequest(
				">>put k 0 0 5 0",
				">>teste",
				"<<stored",
				">>put k 0 0 5 0",
				">>teste",
				"<<replaced"
		);
	}
	
	public void testReplacedKxL1() throws IOException {
		client.testRequest(
			">>put key 0 0 1 0",
			">>a",
			"<<stored",
			">>put key 0 0 5 0",
			">>teste",
			"<<replaced"
		);
	}

	public void testReplacedKxLx() throws IOException {
		client.testRequest(
				">>put key 0 0 5 0",
				">>teste",
				"<<stored",
				">>put key 0 0 5 0",
				">>teste",
				"<<replaced"
			);
	}
	
	/*  */

	public void testRemoveStoredKXL0() throws IOException {
		client.testRequest(
				">>put key 0 0 0 0",
				"<<ERROR 1004: Bad command syntax error!"
			);
	}
	
	public void testRemoveStoredK1L0() throws IOException {
		client.testRequest(
				">>put k 0 0 0 0",
				"<<ERROR 1004: Bad command syntax error!"
			);
	}

	public void testRemoveStoredK1LX() throws IOException {
		client.testRequest(
				">>put k 0 0 5 0",
				">>teste",
				"<<stored",
				">>remove k 0",
				"<<ok",
				">>put k 0 0 5 0",
				">>teste",
				"<<stored"
			);
	}
	
	public void testRemoveStoredKxL1() throws IOException {
		client.testRequest(
				">>put key 0 0 1 0",
				">>a",
				"<<stored",
				">>remove key 0",
				"<<ok",
				">>put key 0 0 5 0",
				">>teste",
				"<<stored"
			);
	}

	public void testRemoveStoredKxLx() throws IOException {
		client.testRequest(
				">>put key 0 0 5 0",
				">>teste",
				"<<stored",
				">>remove key 0",
				"<<ok",
				">>put key 0 0 5 0",
				">>teste",
				"<<stored"
			);
	}
	
	/*  */

	public void testFlushStoredKXL0() throws IOException {
		client.testRequest(
				">>put key 0 0 0 0",
				"<<ERROR 1004: Bad command syntax error!"
			);
	}
	
	public void testFlushStoredK1L0() throws IOException {
		client.testRequest(
				">>put k 0 0 0 0",
				"<<ERROR 1004: Bad command syntax error!"
			);
	}

	public void testFlushStoredK1LX() throws IOException {
		client.testRequest(
				">>put k 0 0 5 0",
				">>teste",
				"<<stored",
				">>flush",
				"<<ok",
				">>put k 0 0 5 0",
				">>teste",
				"<<stored"
			);
	}
	
	public void testFlushStoredKxL1() throws IOException {
		client.testRequest(
				">>put key 0 0 1 0",
				">>a",
				"<<stored",
				">>flush",
				"<<ok",
				">>put key 0 0 5 0",
				">>teste",
				"<<stored"
			);
	}

	public void testFlushStoredKxLx() throws IOException {
		client.testRequest(
				">>put key 0 0 5 0",
				">>teste",
				"<<stored",
				">>flush",
				"<<ok",
				">>put key 0 0 5 0",
				">>teste",
				"<<stored"
			);
	}
	
	/*  */
	
	public void testTimeToLiveNeg() throws IOException {
		client.testRequest(
				">>put key -1 0 5 0",
				"<<ERROR 1004: Bad command syntax error!"
			);
	}

	public void testTimeToIdleNeg() throws IOException {
		client.testRequest(
				">>put key 0 -1 5 0",
				">>teste",
				"<<ERROR 1004: Bad command syntax error!"
			);
	}

	public void testSizeNeg() throws IOException {
		client.testRequest(
				">>put key 0 0 -1 0",
				">>teste",
				"<<ERROR 1004: Bad command syntax error!"
			);
	}

	/*  */
	
	public void testTimeToLiveChar() throws IOException {
		client.testRequest(
				">>put key a 0 5 0",
				">>teste",
				"<<ERROR 1004: Bad command syntax error!"
			);
	}

	public void testTimeToIdleChar() throws IOException {
		client.testRequest(
				">>put key 0 a 5 0",
				">>teste",
				"<<ERROR 1004: Bad command syntax error!"
			);
	}

	public void testSizeChar() throws IOException {
		client.testRequest(
				">>put key 0 0 a 0",
				">>teste",
				"<<ERROR 1004: Bad command syntax error!"
			);
	}

	/*  */
	
	public void testParamsError() throws IOException {
		client.testRequest(
				">>put key 0 0 0 0 0",
				"<<ERROR 1004: Bad command syntax error!"
			);
	}
	
}
