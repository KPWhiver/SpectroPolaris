import java.awt.*;

import javax.swing.*;


public class Player extends GameCharacter {

	String d_name;
	
	public Player(float x, float y, float direction, Color color) {
		super(x, y, direction, color);
		
		d_name = new String();
	}
	
    public void drawUI(Graphics2D g2d, int index) {
    	g2d.setColor(Color.LIGHT_GRAY);
    	g2d.fillRect(805, 5 + index * 50, 214, 40);
		
	}

	public void setName(String name) {
		d_name = name;
	}
}
