import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.*;



public class GamePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1552746400473185110L;

	private Model d_model;
	
	public GamePanel() {
		d_model = new Model();
	}
	
	public Model model() {
		return d_model;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		d_model.draw(g2d);
	}
	
	
}
