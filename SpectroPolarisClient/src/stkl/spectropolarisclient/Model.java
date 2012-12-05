package stkl.spectropolarisclient;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class Model {
	private ArrayList<GameCharacter> d_characters;
	private ArrayList<Block> d_blocks;
	private Player d_player;
	
	private Point motionOrigin;
	private float motionControlX;
	private float motionControlY;
	private Point shootOrigin;
	private float shootControlX;
	private float shootControlY;	
	
	public Model(Context context) {
		d_player = new Player(10, 10, new Paint(Paint.ANTI_ALIAS_FLAG));
		d_characters = new ArrayList<GameCharacter>();
		motionOrigin = new Point(-1, -1);
		shootOrigin = new Point(-1, -1);
		
		
		InputStream is = context.getResources().openRawResource(R.raw.map);
		DataInputStream file = new DataInputStream(is);
		try {
		int numOfBlocks = file.readInt();
		
		for(int idx = 0; idx != numOfBlocks; ++idx) {
			int x = file.readInt();
			int y = file.readInt();
			int width = file.readInt();
			int height;
			
			height = file.readInt();

			addBlock(new Block(x, y, width, height));
		}
			
		file.close();
		
		} catch (IOException e) {
			System.err.println("Error occured reading from file");
			e.printStackTrace();
		} 
	}
	
	public void addBlock(Block block) {
		d_blocks.add(block);
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
		d_player.update(motionControlX, motionControlY);
		d_player.step();
		
		for(GameCharacter character : d_characters)
			character.step();
	}
	
	public void draw(Canvas canvas) {
		d_player.draw(canvas);
		
		for(GameCharacter character : d_characters)
			character.draw(canvas);
				
		for(Block block : d_blocks)
			block.draw(canvas);
		
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
