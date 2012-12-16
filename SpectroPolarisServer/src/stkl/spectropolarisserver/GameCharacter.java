import java.awt.*;
import java.nio.ByteBuffer;

import javax.swing.*;


public class GameCharacter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3277124878993422994L;
	protected float d_x;
	protected float d_y;
	
	protected float d_direction;
	private float d_speed;
	
	protected Color d_color;
	private int d_id;
	
	private final int d_radius = 5;
	
	private static int s_count = 0;
	
	public static int sendSize() {
		
		return 6 * 4;
	}
	
	public void addToBuffer(ByteBuffer buffer) {
		buffer.putFloat(d_x);
		buffer.putFloat(d_y);
		buffer.putFloat(d_direction);
		buffer.putFloat(d_speed);
		buffer.putInt(d_color.getRGB());
		buffer.putInt(d_id);
	}
	
	public GameCharacter(float x, float y, float direction, Color color) {
		d_x = x;
		d_y = y;
		d_direction = direction;
		d_speed = 1;
		d_color = color;
		d_id = s_count;
		++s_count;
	}
	
	public void update(float x, float y, float direction, float speed) {
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

		g2d.setColor(d_color);
		g2d.fillOval((int) (d_x) - d_radius, (int) (d_y) - d_radius, 2 * d_radius, 2 * d_radius);
		
		//System.out.println("draw: " + (int) (d_x) + " " + (int) (d_y));
	}

    public float x() {
    	return d_x;
    }
    
    public float y() {
    	return d_y;
    }
    
    public int radius() {
    	return d_radius;
    }
    
    public int id() {
    	return d_id;
    }
    
    public void setColor(int color) {
    	d_color = new Color(color);
    }
	
	
	
	
}
