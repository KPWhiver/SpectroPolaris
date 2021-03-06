package stkl.spectropolarisclient;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Client extends Thread {
	private final int SERVER_PORT = 1337;
	private final int TIMEOUT = 10000;
	
	private String d_ip;
	private String d_name;
	private int d_color;
	
	private static Client instance;
	private Socket skt;
	private boolean connected;
	private OutputStream out;
	private DataInputStream in;
	
	public Client(String ip, String name, int color) {
		skt = new Socket();
		
		d_ip = ip;
		d_name = name;
		d_color = color;
	}
	
	public void connect() throws IOException {
		skt.setTcpNoDelay(true);
		skt.connect(new InetSocketAddress(d_ip, SERVER_PORT), TIMEOUT);
		skt.setSoTimeout(TIMEOUT);
		connected = true;
		instance = this;
		
		out = skt.getOutputStream();
		in = new DataInputStream(new BufferedInputStream(skt.getInputStream()));
		
		sentInit(d_name, d_color);
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
			close();
		}
	}
	
	public static Client getInstance() {
		return instance;
	}
	
	public void sent(float x, float y, float direction, float speed, int health, int ammo, ArrayList<Bullet> bullets) {
		if(!connected)
			return;
		
		try {
			int allocSize = 32;
			allocSize += Bullet.sendSize() * bullets.size();
			
			ByteBuffer buffer = ByteBuffer.allocate(allocSize);
			
			buffer.putInt(Message.PLAYER.value());
			buffer.putFloat(x);
			buffer.putFloat(y);
			buffer.putFloat(direction);
			buffer.putFloat(speed);
			buffer.putInt(health);
			buffer.putInt(ammo);
			
			// send a "boolean" telling us if a bullet is being sent
			/*if(bullet == null)
				buffer.putInt(0);
			else {
				buffer.putInt(1);
				bullet.addToBuffer(buffer);
			}*/
			
			buffer.putInt(bullets.size());
			for(Bullet bullet: bullets) 
				bullet.addToBuffer(buffer);
			
				
			out.write(buffer.array());
		} catch (IOException e) {
			System.out.println("Connection to server lost");
			e.printStackTrace();
			GameActivity.getInstance().finish();
			close();
		}
	}
	
	private void receiveCharacters() throws Exception {
		
		// Read amount of characters from stream
		//in.readFully(intByteBuffer.array(), 0, 4);
		int numOfCharacters = in.readInt();
		//intByteBuffer.clear();
		
		//assert numOfBytes == 4 || numOfBytes < 0;
		
		// Read amount of bullets from stream
 		//in.readFully(intByteBuffer.array(), 0, 4);
 		int numOfBullets = in.readInt();
 		//intByteBuffer.clear();
 		
 		//assert numOfBytes == 4 || numOfBytes < 0;
		
		// Read amount of pickups from stream
 		//in.readFully(intByteBuffer.array(), 0, 4);
 		int numOfHealthPickups = in.readInt();
 		
 		int numOfAmmoPickups = in.readInt();
 		//intByteBuffer.clear();
 		
 		//assert numOfBytes == 4 || numOfBytes < 0;
		
		//int numOfBytes = 0;
		// characters
		//numOfBytes += numOfCharacters * GameCharacter.sendSize();
		// bullets
		//numOfBytes += numOfBullets * Bullet.sendSize();
		// pickups
		//numOfBytes += numOfPickups * HealthPickup.sendSize();
 		
		// Prepare byte buffer
		//ByteBuffer buffer = ByteBuffer.allocate(numOfBytes);	 
		
		//numOfBytes = in.read(buffer.array());
	    
	    //if(numOfBytes < 0)
	    //    throw new Exception(new String("Connection to server lost"));
	    
	    //if(numOfBytes < buffer.capacity()) {
	    //    System.err.println("Received only " + numOfBytes + " bytes in receiveCharacters");
	    //    return;
	    //}
	    
	    GameActivity.getInstance().model().receive(in, numOfCharacters, numOfBullets, numOfHealthPickups, numOfAmmoPickups);
	}
	
	private void receiveId() throws Exception {
		
		// Read next 4 bytes (int, id) from stream
		//int numOfBytes = in.read(intByteBuffer.array(), 0, 4);
		int id = in.readInt();
		//intByteBuffer.clear();
		
	    //if(numOfBytes < 0)
	    //    throw new Exception("Connection to server lost");
	    
	    //if(numOfBytes < 4) {
	    //    System.err.println("Received only " + numOfBytes + " bytes in receiveId");
	    //    return;
	    //}
	    
	    GameActivity.getInstance().model().player().setId(id);
	    
	    System.out.println("id: " + GameActivity.getInstance().model().player().id());
	}
	
	@Override
	public void run() {
		
		//byte[] bytes = new byte[4];
		//ByteBuffer intByteBuffer = ByteBuffer.wrap(bytes);
		
		while(connected) {
			if(GameActivity.getInstance() == null)
				continue;
			
			try {
				// Read first 4 bytes (int, messageType) from stream
				//in.read(bytes, 0, 4);
				int messageType = in.readInt();
				//intByteBuffer.clear(); // Clear buffer so we can read from it again
				
				// Receive appropriate message based on received messageType
			    if(messageType == Message.CHARACTERS.value()) {
					receiveCharacters();
				} else if(messageType == Message.ID.value()) {
					receiveId();
				} else {
					System.err.println("Received message with unknown id: " + messageType);
				}
			    
			} catch (Exception e) {
				System.out.println("Connection to server lost");
				e.printStackTrace();
				close();
			}
			
		}
	}
	
	public void close() {
		connected = false;
		try {
			skt.close();
		} catch (IOException e) {
			System.err.println("Socket already closed");
		}

		if(GameActivity.getInstance() != null){
			GameActivity.getInstance().finish();
		}

	}
}
