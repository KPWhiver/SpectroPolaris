import java.io.IOException;
import java.net.*;
import java.util.*;

public class Server extends Thread {
	
	private ServerSocket d_server;
	
	private ArrayList<ServerThread> d_threads;
	
	public Server(int port)
	{
		d_threads = new ArrayList<ServerThread>();
		
		d_server = null;
		try {
			d_server = new ServerSocket(port); 
			System.out.println("Server up and running!");
		}
		catch(Exception e) {
			System.out.println(e.getMessage() + ", java... :|");
			System.exit(1);
		}
	}
	
    @Override
    public void run() {
    	while(true)
    	{
    		if(d_server.isClosed())
    			return;
    		
    		try {
				Socket socket = d_server.accept();
				ServerThread thread = new ServerThread(socket);
				thread.start();
				
				d_threads.add(thread);
			} catch (Exception e) {
				System.out.println(e.getMessage() + ", java... :|");
			}
    	}
    }
    
    public InetAddress ip() {
    	return d_server.getInetAddress();
    }

	public void shutdown() {		
		try {
			d_server.close();
		} catch (Exception e) {
			System.out.println(e.getMessage() + ", java... :|");
		}
	}
	
}
