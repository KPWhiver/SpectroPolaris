package stkl.spectropolarisclient;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Player {
	private final int MAX_OFFSET = 50;
	private final int MIN_OFFSET = -MAX_OFFSET;
	private final double SPEED_MOD = 0.05;
	
	private int d_x;
	private int d_y;
	
	private float d_xOffset;
	private float d_yOffset;
	
	private Paint d_paint;
	
	public Player(int x, int y, Paint paint) {
		d_x = x;
		d_y = y;
		d_xOffset = 0;
		d_yOffset = 0;
		d_paint = paint;
		d_paint.setColor(Color.RED);
	}
	
	public void update(float xOffset, float yOffset) {
		d_xOffset = (float) (Math.max(Math.min(xOffset, MAX_OFFSET), MIN_OFFSET) * SPEED_MOD);
		d_yOffset = (float) (Math.max(Math.min(yOffset, MAX_OFFSET), MIN_OFFSET) * SPEED_MOD);
	}
	
	public void step() {
		d_x += d_xOffset;
		d_y += d_yOffset;
		
		Client.getInstance().sent(d_x, d_y, d_xOffset, d_yOffset);
	}
	
	public float xOffset() {
		return d_x;
	}
	
	public float yOffset() {
		return d_y;
	}

    public void draw(Canvas canvas, int centerHorizontal, int centerVertical) {
    	canvas.drawCircle(centerHorizontal, centerVertical, 5, d_paint);
	}
}
