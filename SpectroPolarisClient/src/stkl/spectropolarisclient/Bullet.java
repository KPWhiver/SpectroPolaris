package stkl.spectropolarisclient;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.FloatMath;

public class Bullet {
	private float d_x;
	private float d_y;
	
	private float d_direction;
	private float d_speed;
	
	private boolean destroyed;
	
	private static boolean paintInitialized = false;
	private static Paint d_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public Bullet(float x, float y, float direction) {
		d_x = x;
		d_y = y;
		d_direction = direction;
		d_speed = 5;
		destroyed = false;
		
		if(!paintInitialized) {
			d_paint.setColor(Color.YELLOW);
		}
		GameActivity.getInstance().model().addBullet(this);
	}
	
	public void step() {
		float potentialX = d_x + FloatMath.sin(d_direction) * d_speed;
		float potentialY = d_y + FloatMath.cos(d_direction) * d_speed;
		
		if(GameActivity.getInstance().model().collision(potentialX, potentialY, 5) == false)
		{
			d_x = potentialX;
			d_y = potentialY;
		} else {
			destroy();
		}
	}
	
	public void destroy() {
		destroyed = true;
		// GameActivity.getInstance().model().removeBullet(this);
	}

    public void draw(Canvas canvas) {
    	if(!destroyed) {
    		canvas.drawCircle(d_x, d_y, 1, d_paint);
    	}
	}

}
