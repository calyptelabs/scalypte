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

public class GetCommandTransactionTest extends TestCase{

	private CalypteServer server;
	
	private ClientHelper client;
	
	@Override
	public void setUp() throws Exception{
		Configuration config = new Configuration();
		config.setProperty("transaction_support", "true");
        config.setProperty(CacheConstants.NODES_BUFFER_SIZE, "512k");
        config.setProperty(CacheConstants.INDEX_BUFFER_SIZE, "512k");
        config.setProperty(CacheConstants.DATA_BUFFER_SIZE,  "512k");
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
	
	/* not exist  */
	
	public void test_keyN_valueE_not_exist() throws IOException {
		client.testRequest(
				">>get key",
				"<<ERROR 1004: Bad command syntax error!"
		);
	}

	public void test_key1_valueE_not_exist() throws IOException {
		client.testRequest(
				">>get k",
				"<<ERROR 1004: Bad command syntax error!"
		);
	}

	public void test_keyN_valueN_not_exist() throws IOException {
		client.testRequest(
				CommandHelper.get_keyN_valueN_not_exist
		);
	}
	
	public void test_keyN_value1_not_exist() throws IOException {
		client.testRequest(
				CommandHelper.get_keyN_value1_not_exist
		);
	}

	public void test_key1_valueN_not_exist() throws IOException {
		client.testRequest(
				CommandHelper.get_key1_valueN_not_exist
		);
	}
	
	public void test_key1_value1_not_exist() throws IOException {
		client.testRequest(
				CommandHelper.get_key1_value1_not_exist
		);
	}

	/* exist */
	
	public void test_keyN_valueN_exist() throws IOException {
		client.testRequest(
				CommandHelper.put_keyN_valueN_not_exist,
				CommandHelper.get_keyN_valueN_exist
		);
	}
	
	public void test_keyN_value1_exist() throws IOException {
		client.testRequest(
				CommandHelper.put_keyN_value1_not_exist,
				CommandHelper.get_keyN_value1_exist
		);
	}

	public void test_key1_valueN_exist() throws IOException {
		client.testRequest(
				CommandHelper.put_key1_valueN_not_exist,
				CommandHelper.get_key1_valueN_exist
		);
	}
	
	public void test_key1_value1_exist() throws IOException {
		client.testRequest(
				CommandHelper.put_key1_value1_not_exist,
				CommandHelper.get_key1_value1_exist
		);
	}

	/* flush */

	public void test_keyN_valueN_flush() throws IOException {
		client.testRequest(
				CommandHelper.put_keyN_valueN_not_exist,
				CommandHelper.get_keyN_valueN_exist,
				CommandHelper.flush,
				CommandHelper.get_keyN_valueN_not_exist
		);
	}
	
	public void test_keyN_value1_flush() throws IOException {
		client.testRequest(
				CommandHelper.put_keyN_value1_not_exist,
				CommandHelper.get_keyN_value1_exist,
				CommandHelper.flush,
				CommandHelper.get_keyN_value1_not_exist
		);
	}

	public void test_key1_valueN_flush() throws IOException {
		client.testRequest(
				CommandHelper.put_key1_valueN_not_exist,
				CommandHelper.get_key1_valueN_exist,
				CommandHelper.flush,
				CommandHelper.get_key1_valueN_not_exist
		);
	}
	
	public void test_key1_value1_flush() throws IOException {
		client.testRequest(
				CommandHelper.put_key1_value1_not_exist,
				CommandHelper.get_key1_value1_exist,
				CommandHelper.flush,
				CommandHelper.get_key1_value1_not_exist
		);
	}
	
	/* remove */

	public void test_keyN_valueN_remove() throws IOException {
		client.testRequest(
				CommandHelper.put_keyN_valueN_not_exist,
				CommandHelper.get_keyN_valueN_exist,
				CommandHelper.remove_keyN_valueN_exist,
				CommandHelper.get_keyN_valueN_not_exist
		);
	}
	
	public void test_keyN_value1_remove() throws IOException {
		client.testRequest(
				CommandHelper.put_keyN_value1_not_exist,
				CommandHelper.get_keyN_value1_exist,
				CommandHelper.remove_keyN_value1_exist,
				CommandHelper.get_keyN_value1_not_exist
		);
	}

	public void test_key1_valueN_remove() throws IOException {
		client.testRequest(
				CommandHelper.put_key1_valueN_not_exist,
				CommandHelper.get_key1_valueN_exist,
				CommandHelper.remove_key1_valueN_exist,
				CommandHelper.get_key1_valueN_not_exist
		);
	}
	
	public void test_key1_value1_remove() throws IOException {
		client.testRequest(
				CommandHelper.put_key1_value1_not_exist,
				CommandHelper.get_key1_value1_exist,
				CommandHelper.remove_key1_value1_exist,
				CommandHelper.get_key1_value1_not_exist
		);
	}
	
	public void testParamsError() throws IOException {
		client.testRequest(
				">>get",
				"<<ERROR 1004: Bad command syntax error!"
			);
	}
	
}
