package calypte.server.command;

import java.awt.EventQueue;
import java.io.IOException;

import calypte.Configuration;
import calypte.server.CalypteServer;
import calypte.server.ClientHelper;
import junit.framework.TestCase;

public class PutCommandTest extends TestCase{

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
	
	public void testStoredKXL0() throws IOException {
		client.send("put key 0 0 0 0");
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}

	public void testStoredK1L0() throws IOException {
		client.send("put k 0 0 0 0");
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}

	public void testStoredK1LX() throws IOException {
		client.send("put k 0 0 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);
	}
	
	public void testStoredKxL1() throws IOException {
		client.send("put key 0 0 1 0");
		client.send("a\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);
	}

	public void testStoredKxLx() throws IOException {
		client.send("put key 0 0 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);
	}

	/*  */

	public void testReplacedKXL0() throws IOException {
		client.send("put key 0 0 0 0");
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}
	
	public void testReplacedK1L0() throws IOException {
		client.send("put k 0 0 0 0");
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}

	public void testReplacedK1LX() throws IOException {
		client.send("put k 0 0 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);
		
		client.send("put k 0 0 5 0");
		client.send("teste\r\n".getBytes());
		r = client.read();
		assertEquals("replaced", r);
	}
	
	public void testReplacedKxL1() throws IOException {
		client.send("put key 0 0 1 0");
		client.send("a\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);
		
		client.send("put key 0 0 5 0");
		client.send("teste\r\n".getBytes());
		r = client.read();
		assertEquals("replaced", r);
	}

	public void testReplacedKxLx() throws IOException {
		client.send("put key 0 0 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("stored", r);
		
		client.send("put key 0 0 5 0");
		client.send("teste\r\n".getBytes());
		r = client.read();
		assertEquals("replaced", r);
	}
	
	/*  */
	
	public void testTimeToLiveNeg() throws IOException {
		client.send("put key -1 0 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}

	public void testTimeToIdleNeg() throws IOException {
		client.send("put key 0 -1 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}

	public void testSizeNeg() throws IOException {
		client.send("put key 0 0 -1 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}

	/*  */
	
	public void testTimeToLiveChar() throws IOException {
		client.send("put key a 0 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}

	public void testTimeToIdleChar() throws IOException {
		client.send("put key 0 a 5 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}

	public void testSizeChar() throws IOException {
		client.send("put key 0 0 a 0");
		client.send("teste\r\n".getBytes());
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}

	/*  */
	
	public void testParamsError() throws IOException {
		client.send("put key 0 0 0 0 0");
		String r = client.read();
		assertEquals("ERROR 1004: Bad command syntax error!", r);
	}
	
}
