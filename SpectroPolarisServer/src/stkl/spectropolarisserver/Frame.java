import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class Frame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5151041547543472432L;

	private GamePanel d_game;
	
	public Frame() {
		
		initGUI();
	}

	private void initGUI() {
		setTitle("SpectroPolaris");
		setUndecorated(true);
		setResizable(false);
		
		KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
		Action escapeAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			// close the frame when the user presses escape
		    public void actionPerformed(ActionEvent e) {
		    	SpectroPolaris.server().shutdown();
		        System.exit(1);
		    }
		}; 
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
		getRootPane().getActionMap().put("ESCAPE", escapeAction);
		
		d_game = new GamePanel();
		
		GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
		
		add(d_game);
		
		validate();
	}

	public GamePanel gamePanel() {
		return d_game;
	}

}
