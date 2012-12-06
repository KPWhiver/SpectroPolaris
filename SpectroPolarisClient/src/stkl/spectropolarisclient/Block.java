package stkl.spectropolarisclient;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Block {
	private Rect rect;
	private static Paint paint = new Paint();
	
	public Block(int x, int y, int width, int height) {
		this.rect = new Rect(x, y, x+width, y+height);
	}
	
	public void update(int x, int y, int width, int height) {
		rect.left = x;
		rect.top = y;
		rect.right = x + width;
		rect.bottom = y + height;
	}
	
	public int x() {
		return rect.left;
	}
	
	public int y() {
		return rect.top;
	}
	
	public int width() {
		return rect.right - rect.left;
	}
	
	public int height() {
		return rect.bottom - rect.top;
	}
	
    public boolean pointCollision(int x, int y) {
    	return x > rect.left && y > rect.top && x <  rect.right && y < rect.bottom;
    }

	public void draw(Canvas canvas) {
		canvas.drawRect(rect, paint);
	}
}

