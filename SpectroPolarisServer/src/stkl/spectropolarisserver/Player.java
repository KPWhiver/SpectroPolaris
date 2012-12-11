import java.awt.*;

import javax.swing.*;


public class Player extends GameCharacter {

	String d_name;
	
	public Player(float x, float y, float direction, Color color) {
		super(x, y, direction, color);
		
		d_name = new String();
	}
	
    public void drawUI(Graphics2D g2d, int index) {
    	g2d.setColor(d_color);
    	g2d.fillRect(805, 5 + index * 50, 214, 40);
    	g2d.setColor(Color.BLACK);
    	g2d.drawString(d_name, 820, 5 + index * 50 + 10);
		
	}

	public void setName(String name) {
		d_name = name;
	}
}
