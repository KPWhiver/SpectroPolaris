public class SpectroPolaris 
{
	private static Frame s_frame;
	private static Server s_server;
	
	public SpectroPolaris() {
		
	}
	
	public static Frame frame() {
		return s_frame;
	}
	
	public static Server server() {
		return s_server;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		s_frame = new Frame();
		
		s_server = new Server(1337);
		s_server.start();
		
		while(s_frame.isVisible())
		{
			s_frame.gamePanel().model().step();
			try {
				Thread.sleep(17);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			s_frame.gamePanel().repaint();
		}
	}

}
