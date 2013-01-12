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
		d_player.update(d_in);//x, y, direction, speed, health);
		
		int numOfBullets = d_in.readInt();
		for(int idx = 0; idx != numOfBullets; ++idx) {
			SpectroPolaris.frame().gamePanel().model().addBullet().instantiate(d_in);
		}
	}
	
	private void receiveNamecolor() throws Exception {

		int stringLength = d_in.readInt();
		
		// Prepare to read string
		ByteBuffer buffer = ByteBuffer.allocate(stringLength);
		
		d_in.readFully(buffer.array());
	  			  	  
	    String name = new String(buffer.array());
	    

		int color = d_in.readInt();
		
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
