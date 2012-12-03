package stkl.spectropolarisclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {
	private static Client instance;
	final int SERVER_PORT = 1337;
	private Socket skt;
	private boolean connected;
	DataOutputStream out;
	
	public Client(String ip) throws UnknownHostException, IOException {
		skt = new Socket();
		skt.connect(new InetSocketAddress(ip, SERVER_PORT), 10000);
		System.out.println(skt.getSoTimeout());
		skt.setSoTimeout(10000);
		connected = true;
		instance = this;
		
		out = new DataOutputStream(skt.getOutputStream());
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
		try {
			DataInputStream in = new DataInputStream(skt.getInputStream());
			
			while(connected) {
				in.read();
				
				// Do stuff with received stuff
				// Redraw
				try {
					sleep(33);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			in.close();
			skt.close();
			
			System.out.println("Socket has been closed");
			
		} catch (IOException e) {
			System.err.println("IOException occured trying to create BufferedReader");
			e.printStackTrace();
		}
	}
	
	public void close() {
		connected = false;
	}
}
