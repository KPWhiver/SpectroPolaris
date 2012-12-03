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

	private JPanel d_menu;
	private GamePanel d_game;
	
	private CardLayout d_layout;
	private JPanel d_cardPanel;
	
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
		
		
		d_layout = new CardLayout();
		d_cardPanel = new JPanel();
		d_cardPanel.setLayout(d_layout);
		
		d_game = new GamePanel();
		
		d_menu = new JPanel();
		d_menu.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		d_menu.setBackground(Color.black);
		
		GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
		
		JButton hostGame = new JButton("Host Game");
		hostGame.setPreferredSize(new Dimension(256, 64));
		hostGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		hostGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				d_layout.show(d_cardPanel, "game");
			}
		});
		
		JButton quitGame = new JButton("Quit Game");
		quitGame.setPreferredSize(new Dimension(256, 64));
		quitGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		quitGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);	
			}
		});
		
		d_menu.add(hostGame);
		d_menu.add(quitGame);

		d_cardPanel.add(d_menu, "menu");
		d_cardPanel.add(d_game, "game");
		
		add(d_cardPanel);
		
		validate();
	}

	public GamePanel gamePanel() {
		return d_game;
	}

}
