import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.nio.ByteBuffer;


public class AmmoPickup {
	private static Image s_ammo;
	private static boolean s_initialized = false;
	
	private int d_x;
	private int d_y;
	
	private final int d_width = 10;
	private final int d_heigth = 20;
	
	public AmmoPickup(int x, int y) {
		if(s_initialized == false) {
			s_ammo = Toolkit.getDefaultToolkit().getImage("ammo.png");
			s_initialized = true;
		}
		
		d_x = x;
		d_y = y;
	}
	
	public void draw(Graphics2D g2d) {
		g2d.drawImage(s_ammo, d_x, d_y, d_width, d_heigth, null);
	}

	public boolean collision(Player player) {
		boolean coll = player.x() - player.radius() < d_x + d_width && player.x() + player.radius() > d_x &&
				player.y() - player.radius() < d_y + d_width && player.y() + player.radius() > d_y;
		
		return coll;
	}

	public static int sendSize() {
		return 2 * 4;
	}

	public void addToBuffer(ByteBuffer buffer) {
		buffer.putInt(d_x);
		buffer.putInt(d_y);
	}
}
