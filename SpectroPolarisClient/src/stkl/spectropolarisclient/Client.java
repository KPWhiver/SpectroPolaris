package stkl.spectropolarisclient;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {
	private static Client instance;
	final int SERVER_PORT = 1337;
	private Socket skt;
	private boolean connected;
	
	public Client(String ip) throws UnknownHostException, IOException {
		skt = new Socket();
		skt.connect(new InetSocketAddress(ip, SERVER_PORT), 10000);
		connected = true;
		instance = this;
	}
	
	public static Client getInstance() {
		return instance;
	}
	
	public void sent() {
		// Sent stuff
	}
	
	public void run() {
		try {
			DataInputStream in = new DataInputStream(skt.getInputStream());
			
			while(connected) {
				in.read();
				
				// Do stuff with received stuff
				// Redraw
			}
			
			in.close();
			skt.close();
			
		} catch (IOException e) {
			System.err.println("IOException occured trying to create BufferedReader");
			e.printStackTrace();
		}
	}
	
	public void close() {
		connected = false;
	}
}
