package stkl.spectropolarisclient;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

public class Model {
	private ArrayList<GameCharacter> d_characters;
	private List<Bullet> d_bullets;
	private ArrayList<Block> d_blocks;
	private Player d_player;
	
	private GameActivity d_context;
	
	private Point d_motionOrigin;
	private float d_motionControlX;
	private float d_motionControlY;
	private Point d_shootOrigin;
	private float d_shootControlX;
	private float d_shootControlY;	
	
	public Model(GameActivity context) {
		d_player = new Player(10, 10, new Paint(Paint.ANTI_ALIAS_FLAG));
		d_characters = new ArrayList<GameCharacter>();
		d_bullets = Collections.synchronizedList(new ArrayList<Bullet>());
		d_blocks = new ArrayList<Block>();
		d_motionOrigin = new Point(-1, -1);
		d_shootOrigin = new Point(-1, -1);
		
		d_context = context;
		
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
		d_motionOrigin.set((int)x, (int)y);
	}

	public void setShootOrigin(float x, float y) {
		d_shootOrigin.set((int)x, (int)y);
	}
	
	public void setMotionControls(float controlX, float controlY) {
		d_motionControlX = controlX;
		d_motionControlY = controlY;
	}
	
	public void setShootControls(float controlX, float controlY) {
		d_shootControlX = controlX;
		d_shootControlY = controlY;
	}
	
	public void addGameCharacter(GameCharacter character) {
		d_characters.add(character);
	}
	
	public void addBullet(Bullet bullet) {
		d_bullets.add(bullet);
	}
	
	public void removeBullet(Bullet bullet) {
		synchronized(d_bullets) {
			d_bullets.remove(bullet);
		}
	}
	
	public void step() {
		d_player.update(d_motionControlX, d_motionControlY, d_shootControlX, d_shootControlY);
		d_player.step();
		
		for(GameCharacter character : d_characters)
			character.step();
		
		synchronized(d_bullets) {
			//Iterator<Bullet> i = d_bullets.iterator();
			//while (i.hasNext())
			//	i.next().step();
			
			for(Bullet bullet : d_bullets)
				bullet.step();
		}
	}
	
	public void draw(Canvas canvas) {
		canvas.save();
		
		canvas.scale(4, 4, d_context.centerHorizontal(), d_context.centerVertical());
		
		d_player.draw(canvas, d_context.centerHorizontal(), d_context.centerVertical());
		
		canvas.translate(-d_player.xOffset() + d_context.centerHorizontal(),
				 -d_player.yOffset() + d_context.centerVertical());
		
		for(GameCharacter character : d_characters)
			character.draw(canvas);
		
		for(Bullet bullet : d_bullets)
			bullet.draw(canvas);
				
		for(Block block : d_blocks)
			block.draw(canvas);
		
		canvas.restore();
		
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.CYAN);
		paint.setTextSize(20);
		canvas.drawText("Motion Controls: " + d_motionControlX + ", " + d_motionControlY, 10, 20, paint);
		canvas.drawText("Shoot Controls: " + d_shootControlX + ", " + d_shootControlY, 10, 40, paint);
		
		if(!d_motionOrigin.equals(-1, -1)) {
			canvas.drawCircle(d_motionOrigin.x, d_motionOrigin.y, 5, paint);
		}
		if(!d_shootOrigin.equals(-1, -1)) {
			canvas.drawCircle(d_shootOrigin.x, d_shootOrigin.y, 5, paint);
		}
	}

	public boolean collision(float potentialX, float potentialY, int radius) {
				
		for(Block block : d_blocks)
		{
			if(block.collision(potentialX, potentialY, radius))
				return true;
		}
		
		
		return false;
	}
}
