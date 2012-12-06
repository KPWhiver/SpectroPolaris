import java.awt.*;

import javax.swing.*;


public class GameCharacter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3277124878993422994L;
	private float d_x;
	private float d_y;
	
	private float d_direction;
	private float d_speed;
	
	private Color d_color;
	private int d_id;
	
	public GameCharacter(float x, float y, float direction, Color color) {
		d_x = x;
		d_y = y;
		d_direction = direction;
		d_speed = 0;
		d_color = color;
	}
	
	public void update(float x, float y, float direction, float speed) {
		d_x = x;
		d_y = y;
		d_direction = direction;
		d_speed = speed;
	}
	
	public void step() {
		//d_x += Math.sin(d_direction) * d_speed;
		//d_y += Math.cos(d_direction) * d_speed;
	}

    public void draw(Graphics2D g2d) {

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.setColor(Color.red);
		g2d.fillOval((int) (d_x) - 5, (int) (d_y) - 5, 10, 10);
		
		System.out.println((int) (d_x) + " " + (int) (d_y));
	}
	
	
	
	
}
