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
	
	public Client(String ip, String name, int color) throws UnknownHostException, IOException {
		skt = new Socket();
		skt.setTcpNoDelay(true);
		skt.connect(new InetSocketAddress(ip, SERVER_PORT), TIMEOUT);
		skt.setSoTimeout(TIMEOUT);
		connected = true;
		instance = this;
		
		out = skt.getOutputStream();
		in = skt.getInputStream();
	}
	
	public void sentInit(String name, int color) {
		byte[] bName = name.getBytes();
		
		ByteBuffer buffer = ByteBuffer.allocate(12 + bName.length);
		buffer.putInt(Message.NAMECOLOR.value());		
		buffer.putInt(bName.length);
		buffer.put(bName);
		buffer.putInt(color);
		
		try {
			out.write(buffer.array());
		} catch (IOException e) {
			System.err.println("Error occured while sending player info");
			e.printStackTrace();
		}
	}
	
	public static Client getInstance() {
		return instance;
	}
	
	public void sent(float x, float y, float direction, float speed) {
		try {
			ByteBuffer buffer = ByteBuffer.allocate(20);
			
			buffer.putInt(Message.PLAYER.value());
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
	
	private void id(byte[] message) {
		
	}
	
	private void characters(byte[] message) {
		
	}
	
	private void bullets(byte[] message) {
		
	}
	
	
	
	public void run() {
		
		byte[] bytes = new byte[4];
		ByteBuffer byteToInt = ByteBuffer.wrap(bytes);
		
		while(connected) {
			if(GameActivity.getInstance() == null)
				continue;
			
			try {
				// Read first 4 bytes (int) from stream
				in.read(bytes);
				int messageType = byteToInt.getInt();
				byteToInt.clear();
				
				// Read next 4 bytes (int) from stream
				in.read(bytes);
				int secondInteger = byteToInt.getInt();
				byteToInt.clear();
				
				
				ByteBuffer buffer = ByteBuffer.allocate(numOfCharacters * GameCharacter.sendSize());
				
			    numOfBytes = in.read(buffer.array());			    
			    
			    if(numOfBytes < 0)
			        throw new Exception(new String("Connection to server lost"));
			    
			    if(numOfBytes < buffer.capacity()) {
			        System.err.println("Received only " + numOfBytes + " bytes");
			        continue;
			    }
			    
			    if(messageType == Message.CHARACTERS.value()) {
					characters(numOfBytes);
				} else if(messageType == Message.ID.value()) {
					
				} else if(messageType == Message.BULLETS.value()) {
					
				} else {
					System.err.println("Received message with unkown id: " + messageType);
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
