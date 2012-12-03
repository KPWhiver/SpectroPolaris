import java.awt.*;

import javax.swing.*;


public class GameCharacter extends JComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3277124878993422994L;
	private int d_x;
	private int d_y;
	
	private float d_direction;
	
	private Color d_color;
	private int d_id;
	
	public GameCharacter(int x, int y, float direction, Color color) {
		d_x = x;
		d_y = y;
		d_direction = direction;
		d_color = color;
	}
	
	public void step() {
		
	}
	
	@Override
    public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.setColor(Color.red);
		g2d.fillOval(d_x - 10, d_y - 10, 20, 20);
	}
	
	
	
	
}
