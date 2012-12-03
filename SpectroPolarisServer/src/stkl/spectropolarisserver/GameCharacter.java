import java.awt.*;

import javax.swing.*;


public class GameCharacter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3277124878993422994L;
	private int d_x;
	private int d_y;
	
	private float d_direction;
	private float d_speed;
	
	private Color d_color;
	private int d_id;
	
	public GameCharacter(int x, int y, float direction, Color color) {
		d_x = x;
		d_y = y;
		d_direction = direction;
		d_speed = 0;
		d_color = color;
	}
	
	public void update(int x, int y, float direction, float speed) {
		d_x = x;
		d_y = y;
		d_direction = direction;
		d_speed = speed;
	}
	
	public void step() {
		d_x += Math.sin(d_direction) * d_speed;
		d_y += Math.cos(d_direction) * d_speed;
	}

    public void draw(Graphics2D g2d) {

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.setColor(Color.red);
		g2d.fillOval(d_x - 10, d_y - 10, 20, 20);
	}
	
	
	
	
}
