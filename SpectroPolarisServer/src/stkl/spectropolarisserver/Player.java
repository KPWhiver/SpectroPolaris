import java.awt.*;
import java.nio.ByteBuffer;

import javax.swing.*;


public class Player extends GameCharacter {

	String d_name;
	
	public Player(float x, float y, float direction, Color color) {
		super(x, y, direction, color);
		
		d_name = new String();
	}
	
    public void drawUI(Graphics2D g2d, int index) {
    	g2d.setColor(color());
    	g2d.fillRect(805, 5 + index * 50, 150, 40);
    	g2d.setColor(Color.BLACK);
    	
    	if(health() > 0)
    		g2d.drawString(d_name, 820, 15 + index * 50 + 10);
    	else
    		g2d.drawString("Dead: " + d_name, 820, 15 + index * 50 + 10);
		
    	g2d.setColor(Color.GRAY);
    	g2d.fillRect(810, 35 + index * 50, 140, 5);
    	g2d.setColor(Color.RED);
    	g2d.fillRect(810, 35 + index * 50, (int) (health() * 1.4), 5);
    	g2d.setColor(Color.BLACK);
    	g2d.drawRect(810, 35 + index * 50, 140, 5);
    	
    	g2d.setColor(Color.GRAY);
    	g2d.fillRect(810, 40 + index * 50, 140, 5);
    	g2d.setColor(Color.YELLOW);
    	g2d.fillRect(810, 40 + index * 50, (int) (ammo() * 1.4), 5);
    	g2d.setColor(Color.BLACK);
    	g2d.drawRect(810, 40 + index * 50, 140, 5);
	}

	public void setName(String name) {
		d_name = name;
	}
}
