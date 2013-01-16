package stkl.spectropolarisclient;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.FloatMath;



public class GameCharacter {
	private float d_x;
	private float d_y;
	
	private float d_direction;
	private float d_speed;
	
	private Paint d_paint;
	private int d_id;	
	
	private int d_health;
	
	public static int sendSize() {
		
		return 7 * 4;
	}
		
	public GameCharacter(float x, float y, float direction, float speed, int color, int health, int id) {
		d_x = x;
		d_y = y;
		d_direction = direction;
		d_speed = speed;
		d_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		d_paint.setColor(color);
		
		d_health = health;
		d_id = id;
	}
	
	public void step() {
		//d_x += FloatMath.sin(d_direction) * d_speed;
		//d_y += FloatMath.cos(d_direction) * d_speed;
	}

	private Rect d_rect = new Rect(0, 0, 0, 0);
	private static float s_radius = 2.5f;
	
    public void draw(Canvas canvas) {
    	if(d_id == -1)
    		return;
    	
    	if(d_health > 0)
    		canvas.drawCircle(d_x, d_y, s_radius, d_paint);
    	else {
    		d_rect.bottom = (int) (d_y + s_radius);
    		d_rect.left = (int) (d_x - s_radius);
    		d_rect.right = (int) (d_x + s_radius);
    		d_rect.top = (int) (d_y - s_radius);
    		
    		canvas.drawBitmap(Player.cross(), null, d_rect, null);
    	}
	}

	public void instantiate(float x, float y, float direction, float speed, int color, int health, int id) {
		d_x = x;
		d_y = y;
		d_direction = direction;
		d_speed = speed;
		d_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		d_paint.setColor(color);
		
		d_health = health;
		d_id = id;
	}


}
