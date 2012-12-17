import java.awt.Point;
import java.nio.ByteBuffer;



public class Bullet {	
	private float d_x1;
	private float d_y1;
	
	private float d_x2;
	private float d_y2;
	
	private int d_id;
	
	private int d_transparency;
	
	public static int sendSize() {
		
		return 6 * 4;
	}
	
	public void addToBuffer(ByteBuffer buffer) {
		buffer.putFloat(d_x1);
		buffer.putFloat(d_y1);
		buffer.putFloat(d_x2);
		buffer.putFloat(d_y2);
		buffer.putInt(d_id);
		buffer.putInt(d_transparency);
	}
	
	public Bullet() {
		d_transparency = 0;
		d_x1 = 0;
		d_y1 = 0;
    	d_x2 = 0;
		d_y2 = 0;
		d_id = -1;
	}
	
	public void instantiate(float x, float y, float direction, int id) {
		d_x1 = x;
		d_y1 = y;
    	d_x2 = (float) (d_x1 + Math.sin(direction) * 1000);
		d_y2 = (float) (d_y1 + Math.cos(direction) * 1000);
		Point point = SpectroPolaris.frame().gamePanel().model().visible(d_x1, d_y1, d_x2, d_y2);
		
		if(point != null) {
			d_x2 = point.x;
			d_y2 = point.y;
		}
		
		d_id = id;
		d_transparency = 255;
	}
	
	public void instantiate(Bullet other) {
		d_x1 = other.d_x1;
		d_y1 = other.d_y1;
    	d_x2 = other.d_x2;
		d_y2 = other.d_y2;
		
		
		d_id = other.d_id;
		d_transparency = 255;
	}
	
	public boolean step() {
		if(destroyed())
			return false;
		
		d_transparency -= 3000/d_transparency;
			
		if(d_transparency < 0) {
			destroy();
			return true;
		}
		
		return false;
	}
	
	public void destroy() {
		d_id = -1;
	}
	
	public boolean destroyed() {
		return d_id == -1;
	}
}
