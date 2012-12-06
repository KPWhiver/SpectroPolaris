import java.awt.Color;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;


public class ServerThread extends Thread {

	Socket d_socket;
	
	GameCharacter d_character = null;
	
	InputStream d_in;
	
	public ServerThread(Socket socket) {
		d_socket = socket;
		try {
			d_in = d_socket.getInputStream();
		} catch (Exception e) {
			System.out.println(e.getMessage() + ", java... :|");
			return;
		}
		System.out.println("Connected to client");
		d_character = new GameCharacter(0, 0, 0, Color.black);
		SpectroPolaris.frame().gamePanel().model().addGameCharacter(d_character);
	}
	
	@Override
  public void run() {
		if(d_character == null)
			return;
		
		byte[] bytes = new byte[16];
		
		while(true) {
			try {
			  int numOfBytes = d_in.read(bytes, 0, 16);
			  			  
			  if(numOfBytes < 0) {
			    throw new Exception("Connection to client lost");
			  }
			  if(numOfBytes < 16) {
			    System.out.println("Received only " + numOfBytes + " bytes");
			    continue;
			  }

			  
			  ByteBuffer wrapper = ByteBuffer.wrap(bytes);
			  
				float x = wrapper.getFloat();
				float y = wrapper.getFloat();
				float direction = wrapper.getFloat();
				float speed = wrapper.getFloat();
				
				d_character.update(x, y, direction, speed);
			} catch (Exception e) {
				System.out.println("Connection to client lost");
				SpectroPolaris.frame().gamePanel().model().removeGameCharacter(d_character);
				return;
			}
		}
	}

}
