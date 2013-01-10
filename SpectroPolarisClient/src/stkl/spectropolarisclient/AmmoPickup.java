package stkl.spectropolarisclient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;




public class AmmoPickup {
	private Rect d_rect;
	
	private final int d_width = 10;
	private final int d_heigth = 10;
	
	private static Bitmap s_bitmap;
	private static boolean s_initialized = false;
	
	public AmmoPickup(int x, int y) {
		d_rect = new Rect(x, y, x + d_width, y + d_heigth);
		if(s_initialized == false) {
			s_bitmap = BitmapFactory.decodeResource(GameActivity.getInstance().getResources(), R.drawable.ammo);
			s_initialized = true;
		}
	}
	
	public void draw(Canvas canvas) {
		if(d_rect.left == -1)
			return;
			
		canvas.drawBitmap(s_bitmap, null, d_rect, null);
	}
	
	public static int sendSize() {
		return 2 * 4;
	}

	public void instantiate(int x, int y) {
		d_rect.left = x;
		d_rect.top = y;
		d_rect.right = x + d_width;
		d_rect.bottom = y + d_heigth;
	}
}
