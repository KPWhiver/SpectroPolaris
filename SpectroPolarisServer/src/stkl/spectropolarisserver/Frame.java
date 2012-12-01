import ;

public class Frame extends JFrame {
	
	public Frame() {
		
		initGUI();
	}
	
	public initGUI() {
		setTitle("SpectroPolaris");
		
		setDefaultClosedOperation(JFrame.HIDE_ON_CLOSE);
		
		JButton hostGame = new JButton("Host Game");
		
		add(hostGame);
		
	}

}
