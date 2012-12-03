package stkl.spectropolarisclient;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Player {
	private final int MAX_OFFSET = 50;
	private final int MIN_OFFSET = -MAX_OFFSET;
	private final double SPEED_MOD = 0.1;
	
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

    public void draw(Canvas canvas) {
    	canvas.drawCircle(d_x, d_y, 20, d_paint);
	}
}
