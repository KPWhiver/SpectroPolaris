import java.awt.GraphicsEnvironment;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Frame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5151041547543472432L;

	public Frame() {
		
		initGUI();
	}

	private void initGUI() {
		setTitle("SpectroPolaris");
		setUndecorated(true);
		setResizable(false);
		
		
		GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		JButton hostGame = new JButton("Host Game");
		
		add(hostGame);
		validate();
	}

}
