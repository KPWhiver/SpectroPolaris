package stkl.spectropolarisclient;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.FloatMath;



public class GameCharacter {
	private float d_x;
	private float d_y;
	
	private float d_direction;
	private float d_speed;
	
	private Paint d_paint;
	private int d_id;
	
	public GameCharacter(float x, float y, float direction, Paint paint) {
		d_x = x;
		d_y = y;
		d_direction = direction;
		d_speed = 0;
		d_paint = paint;
	}
	
	public void update(float x, float y, float direction, float speed) {
		d_x = x;
		d_y = y;
		d_direction = direction;
		d_speed = speed;
	}
	
	public void step() {
		d_x += FloatMath.sin(d_direction) * d_speed;
		d_y += FloatMath.cos(d_direction) * d_speed;
		
		Client.getInstance().sent(d_x, d_y, d_direction, d_speed);
	}

    public void draw(Canvas canvas) {
    	canvas.drawCircle(d_x, d_y, 20, d_paint);
	}
}
