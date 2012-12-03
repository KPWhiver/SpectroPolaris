import java.awt.Color;
import java.io.*;
import java.net.*;


public class ServerThread extends Thread {

	Socket d_socket;
	
	GameCharacter d_character = null;
	
	DataInputStream d_in;
	
	public ServerThread(Socket socket) {
		d_socket = socket;
		try {
			d_in = new DataInputStream(d_socket.getInputStream());
		} catch (Exception e) {
			System.out.println(e.getMessage() + ", java... :|");
			return;
		}
		
		d_character = new GameCharacter(100, 100, 0, Color.black);
		SpectroPolaris.frame().gamePanel().model().addGameCharacter(d_character);
	}
	
	@Override
    public void run() {
		if(d_character == null)
			interrupt();
		
		while(true)
		{
			try {
				int x = d_in.readInt();
				int y = d_in.readInt();
				float direction = d_in.readFloat();
				float speed = d_in.readFloat();
				
				d_character.update(x, y, direction, speed);
			} catch (Exception e) {
				System.out.println(e.getMessage() + ", java... :|");
			}
		}
	}

}
