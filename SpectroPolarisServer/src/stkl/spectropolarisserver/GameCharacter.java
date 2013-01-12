import java.awt.*;
import java.awt.geom.Point2D;
import java.io.DataInputStream;
import java.nio.ByteBuffer;

import javax.swing.*;


public class GameCharacter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3277124878993422994L;
	private Point2D.Float d_coor;
	
	private float d_direction;
	private float d_speed;
	
	private Color d_color;
	private int d_id;
	
	private int d_health;
	private int d_ammo;
	
	private final int d_radius = 5;
	
	private static int s_count = 0;
	
	public static int sendSize() {
		
		return 6 * 4;
	}
	
	public void addToBuffer(ByteBuffer buffer) {
		buffer.putFloat(d_coor.x);
		buffer.putFloat(d_coor.y);
		buffer.putFloat(d_direction);
		buffer.putFloat(d_speed);
		buffer.putInt(d_color.getRGB());
		buffer.putInt(d_id);
	}
	
	public GameCharacter(float x, float y, float direction, Color color) {
		d_direction = direction;
		d_speed = 1;
		d_color = color;
		d_id = s_count;
		++s_count;
		d_health = 100;
		d_ammo = 100;
		d_coor = new Point2D.Float(x, y);
	}
	
	public void update(float x, float y, float direction, float speed, int health) {
		d_coor.x = x;
		d_coor.y = y;
		d_direction = direction;
		d_speed = speed;
		d_health = health;
	}
	
	public void update(DataInputStream in) throws Exception {
		d_coor.x = in.readFloat();
		d_coor.y = in.readFloat();
		d_direction = in.readFloat();
		d_speed = in.readFloat();
		d_health = in.readInt();
		d_ammo = in.readInt();
	}
	
	public void step() {
		d_coor.x += Math.sin(d_direction) * d_speed;
		d_coor.y += Math.cos(d_direction) * d_speed;
	}

    public void draw(Graphics2D g2d) {

		g2d.setColor(d_color);
		g2d.fillOval((int) (d_coor.x) - d_radius, (int) (d_coor.y) - d_radius, 2 * d_radius, 2 * d_radius);
		
		//System.out.println("draw: " + (int) (d_x) + " " + (int) (d_y));
	}

    public float x() {
    	return d_coor.x;
    }
    
    public float y() {
    	return d_coor.y;
    }
    
    public Point2D.Float coor() {
    	return d_coor;
    }
    
    public int radius() {
    	return d_radius;
    }
    
    public int id() {
    	return d_id;
    }
    
    public int health() {
    	return d_health;
    }
    
    public int ammo() {
    	return d_ammo;
    }
    
	public Color color() {
		return d_color;
	}
    
    public float distanceFrom(float x, float y) {
    	return (float) Math.hypot(d_coor.x - x, d_coor.y - y);
    }
    
	public boolean changeHealth(int change) {
		d_health = Math.max(Math.min(d_health + change, 100), 0);
		
		if(d_health == 0)
			return true;
		
		return false;
	}
    
    public void setSpeed(float speed) {
    	d_speed = speed;
    }
    
    public void setDirection(float direction) {
    	d_direction = direction;
    }
    
    public void setColor(int color) {
    	d_color = new Color(color);
    }
	
	
	
	
}
