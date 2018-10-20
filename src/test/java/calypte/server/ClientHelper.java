package calypte.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

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
	
	public void close() throws IOException {
		socket.close();
	}
}
