import java.awt.Color;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;


public class ServerThread extends Thread {

	Socket d_socket;
	
	Player d_player = null;
	
	BufferedInputStream d_in;
	OutputStream d_out;
	
	public ServerThread(Socket socket) {
		d_socket = socket;
		try {
			d_in = new BufferedInputStream(d_socket.getInputStream());
			d_out = d_socket.getOutputStream();
		} catch (Exception e) {
			System.out.println(e.getMessage() + ", java... :|");
			return;
		}
		System.out.println("Connected to client");
		d_player = new Player(0, 0, 0, Color.black);
		SpectroPolaris.frame().gamePanel().model().addPlayer(d_player);
	}
	
	@Override
    public void run() {
		if(d_player == null)
			return;
		
		byte[] bytes = new byte[16];
		
		while(true) {
			
			try {
			    int numOfBytes = d_in.read(bytes, 0, 16);
			  			  
			    if(numOfBytes < 0)
			        throw new Exception("Connection to client lost");
			    
			    if(numOfBytes < 16) {
			        System.out.println("Received only " + numOfBytes + " bytes");
			        continue;
			    }
			  
			    ByteBuffer wrapper = ByteBuffer.wrap(bytes);
			  
				float x = wrapper.getFloat();
				float y = wrapper.getFloat();
				float direction = wrapper.getFloat();
				float speed = wrapper.getFloat();
				
				//System.out.println("rcv: " + (int) (x) + " " + (int) (y));
				
				d_player.update(x, y, direction, speed);
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
