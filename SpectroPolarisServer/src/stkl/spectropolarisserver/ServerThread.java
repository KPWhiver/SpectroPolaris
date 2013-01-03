import java.awt.Color;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;


public class ServerThread extends Thread {

	Socket d_socket;
	
	Player d_player = null;
	
	DataInputStream d_in;
	OutputStream d_out;
	
	public ServerThread(Socket socket) {
		d_socket = socket;
		try {
			d_in = new DataInputStream(new BufferedInputStream(d_socket.getInputStream()));
			d_out = d_socket.getOutputStream();
		} catch (Exception e) {
			System.out.println(e.getMessage() + ", java... :|");
			return;
		}
		System.out.println("Connected to client");
		d_player = new Player(0, 0, 0, Color.black);
		SpectroPolaris.frame().gamePanel().model().addPlayer(d_player);
		
		sendId();
	}
	
	private void sendId() {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.putInt(Message.ID.value());
		buffer.putInt(d_player.id());
		
		try {
			d_out.write(buffer.array());
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void receivePlayer() throws Exception {
		//ByteBuffer buffer = ByteBuffer.allocate(20 + Bullet.sendSize());
		
		//int numOfBytes = d_in.read(buffer.array());
			  			  
		//if(numOfBytes < 0)
		//	throw new Exception("Connection to client lost");
			    
	    //if(numOfBytes < 16) {
	    //    System.out.println("Received only " + numOfBytes + " bytes in receivePlayer");
	    //    return;
	    //}
	  
		//float x = buffer.getFloat();
		//float y = buffer.getFloat();
		//float direction = buffer.getFloat();
		//float speed = buffer.getFloat();
		//int health = buffer.getInt();
		
		d_player.update(d_in);//x, y, direction, speed, health);
		
		SpectroPolaris.frame().gamePanel().model().addBullet().instantiate(d_in);
	}
	
	private void receiveNamecolor() throws Exception {
		// Read next 4 bytes (int, lengthOfString) from stream
		//d_in.read(intByteBuffer.array());
		int stringLength = d_in.readInt();
		//intByteBuffer.clear();
		
		// Prepare to read string
		ByteBuffer buffer = ByteBuffer.allocate(stringLength);
		
		d_in.readFully(buffer.array());
	  			  
		//if(numOfBytes < 0)
		//	throw new Exception("Connection to client lost");
			    
	    //if(numOfBytes < stringLength) {
	    //    System.out.println("Received only " + numOfBytes + " bytes in receiveNamecolor");
	    //    return;
	    //}
	  
	    String name = new String(buffer.array());
	    
		// Read next 4 bytes (int, lengthOfString) from stream
		//d_in.read(intByteBuffer.array());
		int color = d_in.readInt();
		//intByteBuffer.clear();
		
		d_player.setName(name);
		d_player.setColor(color);
		
		System.out.println("Received new namecolor: " + name + ", " + color);
	}
	
	@Override
	public void run() {
		if(d_player == null)
			return;
		
		//byte[] bytes = new byte[4];
		//ByteBuffer intByteBuffer = ByteBuffer.wrap(bytes);
		
		while(true) {
			try {
				// Read first 4 bytes (int, messageType) from stream
				//d_in.read(bytes);
				int messageType = d_in.readInt();
				//intByteBuffer.clear(); // Clear buffer so we can read from it again
				
				// Receive appropriate message based on received messageType
			    if(messageType == Message.PLAYER.value()) {
					receivePlayer();
				} else if(messageType == Message.NAMECOLOR.value()) {
					receiveNamecolor();
				} else {
					System.err.println("Received message with unkown id: " + messageType);
				}
			    
			} catch (Exception e) {
				System.out.println("Connection to client lost");
				SpectroPolaris.frame().gamePanel().model().removePlayer(d_player);
				try {
					d_socket.close();
				} catch (IOException e2) {
					System.err.println(e2 + ", java made me do it...");
					e2.printStackTrace();
				}
				return;
			}
			
		}
	}

	public boolean send(byte[] message) {
		if(isAlive() == false)
			return false;
		
		try {
			d_out.write(message);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		return true;
	}


}
