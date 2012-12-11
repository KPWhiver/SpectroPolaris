package stkl.spectropolarisclient;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.FloatMath;



public class GameCharacter {
	private float d_x;
	private float d_y;
	
	private float d_direction;
	private float d_speed;
	
	private Paint d_paint;
	private int d_id;	
	
	public static int sendSize() {
		
		return 6 * 4;
	}
		
	public GameCharacter(float x, float y, float direction, float speed, int color, int id) {
		d_x = x;
		d_y = y;
		d_direction = direction;
		d_speed = speed;
		d_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		d_paint.setColor(color);
		d_id = id;
	}
	
	public void step() {
		//d_x += FloatMath.sin(d_direction) * d_speed;
		//d_y += FloatMath.cos(d_direction) * d_speed;
	}

    public void draw(Canvas canvas) {
    	if(d_id != -1)
    		canvas.drawCircle(d_x, d_y, 20, d_paint);
	}

	public void instantiate(float x, float y, float direction, float speed, int color, int id) {
		d_x = x;
		d_y = y;
		d_direction = direction;
		d_speed = speed;
		d_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		d_paint.setColor(color);
		d_id = id;
	}


}
