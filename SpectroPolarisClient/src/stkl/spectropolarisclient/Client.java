package stkl.spectropolarisclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class Client extends Thread {
	private final int SERVER_PORT = 1337;
	private final int TIMEOUT = 10000;
	
	private static Client instance;
	private Socket skt;
	private boolean connected;
	private OutputStream out;
	private InputStream in;
	
	public Client(String ip) throws UnknownHostException, IOException {
		skt = new Socket();
		skt.connect(new InetSocketAddress(ip, SERVER_PORT), TIMEOUT);
		skt.setSoTimeout(TIMEOUT);
		connected = true;
		instance = this;
		
		out = skt.getOutputStream();
		in = skt.getInputStream();
	}
	
	public static Client getInstance() {
		return instance;
	}
	
	public void sent(int x, int y, float direction, float speed) {
		try {
			ByteBuffer buffer = ByteBuffer.allocate(16);
			
			buffer.putInt(x);
			buffer.putInt(y);
			buffer.putFloat(direction);
			buffer.putFloat(speed);
			
			out.write(buffer.array());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		//try {
			while(connected) {
				//in.read();
				
				// Do stuff with received stuff
				// Redraw
				try {
					sleep(33);
				} catch (InterruptedException e) {
					System.err.println("Exception occured while tring to sleep");
					e.printStackTrace();
				}
			}
		//} catch (IOException e) {
		//	System.err.println("IOException occured reading from input stream");
		//	e.printStackTrace();
		//}
		
		try {
			in.close();
			out.close();
			skt.close();
		} catch (IOException e) {
			System.err.println("Exception occured while closing streams and socket.");
			e.printStackTrace();
		}
		
		
		System.out.println("Socket has been closed");
	}
	
	public void close() {
		connected = false;
	}
}
