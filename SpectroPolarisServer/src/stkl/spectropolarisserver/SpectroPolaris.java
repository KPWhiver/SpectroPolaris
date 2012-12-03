public class SpectroPolaris 
{
	public SpectroPolaris() {
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Frame frame = new Frame();
		
		Server server = new Server(1337);
		server.start();
	}

}
