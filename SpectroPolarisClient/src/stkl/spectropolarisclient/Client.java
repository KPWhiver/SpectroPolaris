package stkl.spectropolarisclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {
	private final int SERVER_PORT = 1337;
	private final int TIMEOUT = 10000;
	
	private static Client instance;
	private Socket skt;
	private boolean connected;
	private DataOutputStream out;
	private DataInputStream in;
	
	public Client(String ip) throws UnknownHostException, IOException {
		skt = new Socket();
		skt.connect(new InetSocketAddress(ip, SERVER_PORT), TIMEOUT);
		skt.setSoTimeout(TIMEOUT);
		connected = true;
		instance = this;
		
		out = new DataOutputStream(skt.getOutputStream());
		in = new DataInputStream(skt.getInputStream());
	}
	
	public static Client getInstance() {
		return instance;
	}
	
	public void sent(int x, int y, float direction, float speed) {
		try {
			out.writeInt(x);
			out.writeInt(y);
			out.writeFloat(direction);
			out.writeFloat(speed);
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
