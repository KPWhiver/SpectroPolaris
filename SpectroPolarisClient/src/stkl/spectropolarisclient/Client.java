package stkl.spectropolarisclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {
	final int SERVER_PORT = 1337;
	private Socket skt;
	private boolean connected;
	
	public Client(String ip) throws UnknownHostException, IOException {
		skt = new Socket();
		skt.connect(new InetSocketAddress(ip, SERVER_PORT), 10000);
		connected = true;
	}
	
	public void sent() {
		// Sent stuff
	}
	
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
			
			while(connected) {
				in.readLine();
				
				// Do stuff with received stuff
				// Redraw
			}
			
			in.close();
			
		} catch (IOException e) {
			System.err.println("IOException occured trying to create BufferedReader");
			e.printStackTrace();
		}
	}
	
}
