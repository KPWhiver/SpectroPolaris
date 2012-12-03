package stkl.spectropolarisclient;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class Model {
	private ArrayList<GameCharacter> d_characters;
	private Point motionOrigin;
	private float motionControlX;
	private float motionControlY;
	private Point shootOrigin;
	private float shootControlX;
	private float shootControlY;	
	
	public Model() {
		d_characters = new ArrayList<GameCharacter>();
		motionOrigin = new Point(-1, -1);
		shootOrigin = new Point(-1, -1);
	}
	
	public void setMotionOrigin(float x, float y) {
		motionOrigin.set((int)x, (int)y);
	}

	public void setShootOrigin(float x, float y) {
		shootOrigin.set((int)x, (int)y);
	}
	
	public void setMotionControls(float controlX, float controlY) {
		this.motionControlX = controlX;
		this.motionControlY = controlY;
	}
	
	public void setShootControls(float controlX, float controlY) {
		this.shootControlX = controlX;
		this.shootControlY = controlY;
	}
	
	public void addGameCharacter(GameCharacter character) {
		d_characters.add(character);
	}
	
	public void step() {
		for(GameCharacter character : d_characters)
			character.step();
	}
	
	public void draw(Canvas canvas) {
		for(GameCharacter character : d_characters)
			character.draw(canvas);
		
		Paint paint = new Paint();
		paint.setTextSize(20);
		canvas.drawText("Motion Controls: " + motionControlX + ", " + motionControlY, 10, 20, paint);
		canvas.drawText("Shoot Controls: " + shootControlX + ", " + shootControlY, 10, 40, paint);
		
		if(!motionOrigin.equals(-1, -1)) {
			canvas.drawCircle(motionOrigin.x, motionOrigin.y, 5, paint);
		}
		if(!shootOrigin.equals(-1, -1)) {
			canvas.drawCircle(shootOrigin.x, shootOrigin.y, 5, paint);
		}
	}
}
