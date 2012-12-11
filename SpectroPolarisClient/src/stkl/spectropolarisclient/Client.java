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
		skt.setTcpNoDelay(true);
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
	
	public void sent(float x, float y, float direction, float speed) {
		try {
			ByteBuffer buffer = ByteBuffer.allocate(16);
			
			buffer.putFloat(x);
			buffer.putFloat(y);
			buffer.putFloat(direction);
			buffer.putFloat(speed);
			
			out.write(buffer.array());
		} catch (IOException e) {
			System.out.println("Connection to server lost");
			e.printStackTrace();
			GameActivity.getInstance().finish();
			close();
			try {
				skt.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.exit(1);
		}
	}
	
	public void run() {
		
		byte[] bytes = new byte[4];
		ByteBuffer byteToInt = ByteBuffer.wrap(bytes);
		
		while(connected) {
			if(GameActivity.getInstance() == null)
				continue;
			
			try {
				int numOfBytes = in.read(bytes);
				int messageType = byteToInt.getInt();
				if(messageType != 0)
					continue;
				byteToInt.clear();
				
				numOfBytes = in.read(bytes);
				int numOfCharacters = byteToInt.getInt();
				byteToInt.clear();
				
				System.out.println(numOfCharacters);
				
				ByteBuffer buffer = ByteBuffer.allocate(numOfCharacters * GameCharacter.sendSize());
				
			    numOfBytes = in.read(buffer.array());			    
			    
			    if(numOfBytes < 0)
			        throw new Exception(new String("Connection to server lost"));
			    
			    if(numOfBytes < buffer.capacity()) {
			        System.err.println("Received only " + numOfBytes + " bytes");
			        continue;
			    }
			    
			    GameActivity.getInstance().model().receive(buffer, numOfCharacters);
			    

			} catch (Exception e) {
				System.out.println("Connection to server lost");
				e.printStackTrace();
				GameActivity.getInstance().finish();
				try {
					skt.close();
				} catch (IOException e2) {
					System.err.println(e2.getMessage());
					e2.printStackTrace();
				}
				return;
			}
			
		}
	}
	
	public void close() {
		connected = false;
	}
}
