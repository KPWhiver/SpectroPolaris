import java.net.Socket;


public class ServerThread extends Thread {

	Socket d_socket;
	
	public ServerThread(Socket socket) {
		d_socket = socket;
	}
	
	@Override
    public void run() {
		
	}

}
