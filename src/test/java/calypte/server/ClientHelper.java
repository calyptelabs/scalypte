package calypte.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import junit.framework.TestCase;

public class ClientHelper {

	private Socket socket;
	
	private OutputStream o;
	
	private InputStream i;
	
	public ClientHelper() throws UnknownHostException, IOException {
		this.socket = new Socket("localhost", 1044);
		this.o = socket.getOutputStream();
		this.i = socket.getInputStream();
	}
	
	public void send(String cmd) throws IOException {
		cmd += "\r\n";
		o.write(cmd.getBytes());
		o.flush();
	}

	public void send(byte[] dta) throws IOException {
		o.write(dta);
		o.flush();
	}
	
	public String read() throws IOException {
		StringBuilder s = new StringBuilder();
		byte b;
		while((b = (byte)i.read()) != '\n') {
			s.append((char)b);
		}
		
		if(s.charAt(s.length() - 1) != '\r') {
			throw new IOException("expected \r");
		}
		
		s.setLength(s.length() - 1);
		
		return s.toString();
	}

	public byte[] read(int len) throws IOException {
		byte[] r = new byte[len + 2];
		int l = i.read(r, 0, r.length);
		if(l != len + 2) {
			throw new IOException(l + " != " + len);
		}
		if(r[r.length - 1] != '\n' || r[r.length - 2] != '\r') {
			throw new IOException("expected \\r\\n");
		}
		return Arrays.copyOf(r, r.length - 2);
	}
	
	public void testRequest(String[] request, String[] response) throws IOException {
		for(String r: request) {
			this.send(r);
		}
		
		for(int i=0;i<response.length;i++) {
			TestCase.assertEquals(response[i], this.read());
		}
	}

	public void testRequest(String[] ... sequence) throws IOException {
		System.out.println("start request+++++++++++++++++++++++++++++++++++++");
		for(String[] s1: sequence) {
			for(String s: s1) {
				if(s.startsWith(">>")) {
					System.out.println("send...: " + s.substring(2));
					this.send(s.substring(2));
				}
				else
				if(s.startsWith("<<")) {
					System.out.println("receive: " + s.substring(2));
					TestCase.assertEquals(s.substring(2), this.read());
				}
			}
		}
		System.out.println("end request+++++++++++++++++++++++++++++++++++++");
	}
	
	public void testRequest(String ... sequence) throws IOException {
		for(String s: sequence) {
			if(s.startsWith(">>")) {
				this.send(s.substring(2));
			}
			else
			if(s.startsWith("<<")) {
				TestCase.assertEquals(s.substring(2), this.read());
			}
		}
	}
	
	public void close() throws IOException {
		socket.close();
	}
}
