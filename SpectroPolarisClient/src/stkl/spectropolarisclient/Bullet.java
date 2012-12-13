package stkl.spectropolarisclient;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.FloatMath;

public class Bullet {	
	private float d_x;
	private float d_y;
	
	private float d_direction;
	//private float d_speed;
	
	private boolean d_destroyed;
	
	private Paint d_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	private int d_transparency;
	
	public Bullet() {
		d_transparency = 0;
		d_x = 0;
		d_y = 0;
		d_direction = 0;
		//d_speed = 0;
		d_destroyed = true;		

		d_paint.setColor(Color.YELLOW);
	}
	
	public void instantiate(float x, float y, float direction) {
		d_x = x;
		d_y = y;
		d_direction = direction;
		//d_speed = 5;
		d_destroyed = false;
		d_transparency = 255;
		d_paint.setAlpha(d_transparency);
	}
	
	public void instantiate(Bullet other) {
		d_x = other.d_x;
		d_y = other.d_y;
		d_direction = other.d_direction;
		//d_speed = other.d_speed;
		d_destroyed = false;
		d_transparency = 255;
		d_paint.setAlpha(d_transparency);
	}
	
	public boolean step() {
		if(d_destroyed)
			return false;
		
		d_transparency -= 50;
		
		//if(GameActivity.getInstance().model().collision(potentialX, potentialY, 5) == false)
		//{
			//d_x = potentialX;
			//d_y = potentialY;
			//return false;
		//} else {
	//		destroy();
			//return true;
	//	}
			
		if(d_transparency < 0) {
			destroy();
			return true;
		}
		d_paint.setAlpha(d_transparency);
		
		return false;
	}
	
	public void destroy() {
		d_destroyed = true;
	}
	
	public boolean destroyed() {
		return d_destroyed;
	}

    public void draw(Canvas canvas) {		
    	float potentialX = d_x + FloatMath.sin(d_direction) * 1000;
		float potentialY = d_y + FloatMath.cos(d_direction) * 1000;
    	if(!d_destroyed) {
    		//canvas.drawCircle(d_x, d_y, 1, d_paint);
    		canvas.drawLine(d_x, d_y, potentialX, potentialY, d_paint);
    	}
	}
}
