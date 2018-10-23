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

public class RollbackCommandTransactionTest extends TestCase{

	private CalypteServer server;
	
	private ClientHelper client;
	
	@Override
	public void setUp() throws Exception{
		Configuration config = new Configuration();
		config.setProperty("transaction_support", "true");
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

	public void testPutNotExist() throws IOException {
		client.testRequest(
				CommandHelper.begin,
				CommandHelper.put_keyN_valueN_not_exist,
				CommandHelper.get_keyN_valueN_exist,
				CommandHelper.rollback,
				CommandHelper.get_keyN_valueN_not_exist
				);
	}

	public void testPutExist() throws IOException {
		client.testRequest(
				CommandHelper.begin,
				CommandHelper.put_keyN_valueN_not_exist,
				CommandHelper.get_keyN_valueN_exist,
				CommandHelper.put_keyN_value1_exist,
				CommandHelper.rollback,
				CommandHelper.get_keyN_value1_not_exist
				);
	}

	public void testReplaceNotExist() throws IOException {
		client.testRequest(
				CommandHelper.begin,
				CommandHelper.replace_keyN_valueN_not_exist,
				CommandHelper.get_keyN_valueN_not_exist,
				CommandHelper.rollback,
				CommandHelper.get_keyN_valueN_not_exist
				);
	}

	public void testReplaceExist() throws IOException {
		client.testRequest(
				CommandHelper.begin,
				CommandHelper.put_keyN_valueN_not_exist,
				CommandHelper.replace_keyN_value1_exist,
				CommandHelper.get_keyN_value1_exist,
				CommandHelper.rollback,
				CommandHelper.get_keyN_value1_not_exist
				);
	}

	public void testSetNotExist() throws IOException {
		client.testRequest(
				CommandHelper.begin,
				CommandHelper.set_keyN_valueN_not_exist,
				CommandHelper.get_keyN_valueN_exist,
				CommandHelper.rollback,
				CommandHelper.get_keyN_valueN_not_exist
				);
	}

	public void testSetExist() throws IOException {
		client.testRequest(
				CommandHelper.begin,
				CommandHelper.put_keyN_valueN_not_exist,
				CommandHelper.set_keyN_value1_exist,
				CommandHelper.get_keyN_valueN_exist,
				CommandHelper.rollback,
				CommandHelper.get_keyN_valueN_not_exist
				);
	}
	
	public void testRemoveNotExist() throws IOException {
		client.testRequest(
				CommandHelper.begin,
				CommandHelper.remove_keyN_valueN_not_exist,
				CommandHelper.get_keyN_valueN_not_exist,
				CommandHelper.rollback,
				CommandHelper.get_keyN_valueN_not_exist
				);
	}

	public void testRemoveExist() throws IOException {
		client.testRequest(
				CommandHelper.begin,
				CommandHelper.put_keyN_valueN_not_exist,
				CommandHelper.remove_keyN_valueN_exist,
				CommandHelper.get_keyN_valueN_not_exist,
				CommandHelper.rollback,
				CommandHelper.get_keyN_valueN_not_exist
				);
	}
	
}
