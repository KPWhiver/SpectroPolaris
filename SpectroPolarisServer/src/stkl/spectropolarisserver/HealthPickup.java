import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;


public class HealthPickup {
	private static Image s_heart;
	private static boolean s_initialized = false;
	
	private int d_x;
	private int d_y;
	
	private final int d_width = 10;
	
	public HealthPickup(int x, int y) {
		if(s_initialized == false) {
			s_heart = Toolkit.getDefaultToolkit().getImage("heart.png");
			s_initialized = true;
		}
		
		d_x = x;
		d_y = y;
	}
	
	public void draw(Graphics2D g2d) {
		g2d.drawImage(s_heart, d_x, d_y, d_width, d_width, null);
	}

	public boolean collision(Player player) {
		boolean coll = player.x() - player.radius() < d_x + d_width && player.x() + player.radius() > d_x &&
				player.y() - player.radius() < d_y + d_width && player.y() + player.radius() > d_y;
		
		return coll;
	}
}
