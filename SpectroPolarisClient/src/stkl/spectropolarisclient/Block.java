package stkl.spectropolarisclient;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
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

	public boolean collision(float x, float y, int radius) {
		
		if(Rect.intersects(rect, new Rect((int)x - radius, (int)y - radius, (int)x + radius, (int)y + radius)))
			return true;
		
		return false;
	}
	
	private float lineXsideCollision(float x1, float y1, float x2, float y2, float x) {
		return y1 + (y2 - y1) * ((x - x1) / (x2 - x1));
	}
	
	private float lineYsideCollision(float x1, float y1, float x2, float y2, float y) {
		return x1 + (x2 - x1) * ((y - y1) / (y2 - y1));
	}
	
	public Point collision(float x1, float y1, float x2, float y2) {
		float minX = Math.min(x1, x2);
		float maxX = Math.max(x1, x2);
		float minY = Math.min(y1, y2);
		float maxY = Math.min(y1, y2);
		
		if(minX < rect.left && maxX >= rect.left) {		//does it cross left edge?
			float intersectionY = lineXsideCollision(x1, y1, x2, y2, rect.left);
			if(intersectionY >= rect.top && intersectionY <= rect.bottom)
				return new Point(rect.left, (int) intersectionY);
		}
		if(minX > rect.right && maxX <= rect.right) {//does it cross right edge?
			float intersectionY = lineXsideCollision(x1, y1, x2, y2, rect.right);
			if(intersectionY >= rect.top && intersectionY <= rect.bottom)
				return new Point(rect.right, (int) intersectionY);
		}
		
		if(minY < rect.top && maxY >= rect.top) {		//does it cross top edge?
			float intersectionX = lineYsideCollision(x1, y1, x2, y2, rect.top);
			if(intersectionX >= rect.left && intersectionX <= rect.right)
				return new Point((int) intersectionX, rect.top);
		}
		if(minY > rect.bottom && maxY <= rect.bottom) {//does it cross bottom edge?
			float intersectionX = lineYsideCollision(x1, y1, x2, y2, rect.bottom);
			if(intersectionX >= rect.left && intersectionX <= rect.right)
				return new Point((int) intersectionX, rect.bottom);
		}
		
		return null;
	}
}

