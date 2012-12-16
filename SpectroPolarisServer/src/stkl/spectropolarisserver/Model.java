import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;


public class Model {
	private ArrayList<GameCharacter> d_characters;
	private ArrayList<Player> d_players;
	
	// tileMap with blocks (true means block, false means noblock)
	private boolean[][] d_tileMap;
	private final int d_tileSize = 10;
	
	private final int d_mapWidth = 800;
	private final int d_mapHeight = 768;
	
	// this rectangle is here to temporarily draw a block when creating them
	private Rectangle d_tmpBlock;
	
	// hill related
	private Rectangle d_hill;
	private int d_points;
	
	// pickup related
	private ArrayList<HealthPickup> d_health;
	private final int d_maxNumOfHealth = 3;
	private int d_timeSinceHealthPlacement;
	private Random d_randGenerator;
	
	public int tileSize() {
		return d_tileSize;
	}
	
	public Model() {
		d_characters = new ArrayList<GameCharacter>();
		d_players = new ArrayList<Player>();
		
		d_hill = new Rectangle(200, 200, 100, 100);
		
		d_health = new ArrayList<HealthPickup>();
		d_timeSinceHealthPlacement = 0;
		d_randGenerator = new Random();
		
		d_tileMap = new boolean[d_mapHeight / d_tileSize][d_mapWidth / d_tileSize];
		d_tmpBlock = null;

		try {		
			DataInputStream file = new DataInputStream(new FileInputStream("map.dat"));
			
			//int numOfBlocks = file.readInt();
			
			//for(int idx = 0; idx != numOfBlocks; ++idx) {
			//	int x = file.readInt();
			//	int y = file.readInt();
			//	int width = file.readInt();
			//	int height = file.readInt();
				
			//	if(width > 0 && height > 0 && x + width < d_mapWidth && y + height < d_mapHeight)
			//		addBlock(x, y, width, height);
			//}
			
			for(int y = 0; y != d_mapHeight / d_tileSize; ++y) {
				for(int x = 0; x != d_mapWidth / d_tileSize; ++x) {
					d_tileMap[y][x] = file.readBoolean();
				}
			}
			
			file.close();
			
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage() + ", java... :|");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e.getMessage() + ", java... :|");
			e.printStackTrace();
		}
	}
	
	public void save() {
		try {		
			DataOutputStream file = new DataOutputStream(new FileOutputStream("map.dat"));
			
			for(int y = 0; y != d_mapHeight / d_tileSize; ++y) {
				for(int x = 0; x != d_mapWidth / d_tileSize; ++x) {
					file.writeBoolean(d_tileMap[y][x]);
				}
			}
			
			//file.writeInt(d_blocks.size());
			
			//for(Block block : d_blocks) {
			//	file.writeInt(block.x());
			//	file.writeInt(block.y());
			//	file.writeInt(block.width());
			//	file.writeInt(block.height());
			//}
			
			file.close();
			
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage() + ", java... :|");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e.getMessage() + ", java... :|");
			e.printStackTrace();
		}
	}
	
	public void addGameCharacter(GameCharacter character) {
		d_characters.add(character);
	}
	
	public void addPlayer(Player player) {
		synchronized(d_players) {
			d_players.add(player);
		}
	}
	
	public Rectangle tmpBlock() {
		return d_tmpBlock;
	}
	
	public void setTmpBlock(Rectangle rect) {
		d_tmpBlock = rect;
	}
		
	public void addBlock(int x, int y, int width, int height) {
		// add blocks to the effected part of the map
		for(int yIdx = y / d_tileSize; yIdx != (height + y) / d_tileSize; ++yIdx) {
			for(int xIdx = x / d_tileSize; xIdx != (width + x) / d_tileSize; ++xIdx)
				d_tileMap[yIdx][xIdx] = true;
		}
	}
	
	public void step() {
		if(d_health.size() < d_maxNumOfHealth) {
			++d_timeSinceHealthPlacement;
			
			// find free spot to place health
			while(d_timeSinceHealthPlacement > 330) {
				int x = d_randGenerator.nextInt(d_mapWidth);
				int y = d_randGenerator.nextInt(d_mapHeight);
				
				synchronized(d_health) {
					if(d_tileMap[y / d_tileSize][x / d_tileSize] == false) {
						d_health.add(new HealthPickup(x, y));
						d_timeSinceHealthPlacement = 0;
						break;
					}
				}
			}
		}
			
		boolean hillCaptured = false;
		
		for(Player player : d_players) {
			if(d_hill.contains(player.x(), player.y()))
				hillCaptured = true;
			
			Iterator<HealthPickup> iter = d_health.iterator();
			while(iter.hasNext()) {
				if(iter.next().collision(player))
					iter.remove();
			}
		}
		
		for(GameCharacter character : d_characters) {
			character.step();
			if(d_hill.contains(character.x(), character.y()))
				hillCaptured = false;
		}
		
		if(hillCaptured)
			d_points += 1;
		
		int numOfCharacters = d_players.size() + d_characters.size();
		ByteBuffer buffer = ByteBuffer.allocate(4 + 4 + numOfCharacters * GameCharacter.sendSize());
		buffer.putInt(Message.CHARACTERS.value());
		buffer.putInt(numOfCharacters);
		
		for(Player player : d_players)
			player.addToBuffer(buffer);
		
		for(GameCharacter character : d_characters)
			character.addToBuffer(buffer);
				
		SpectroPolaris.server().send(buffer.array());
	}
	
	//private static long time = 0;
	
	public void draw(Graphics2D g2d) {
		//System.out.println(System.nanoTime() - time);
		//time = System.nanoTime();
    	
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
		
		synchronized(d_health) {
			for(HealthPickup health : d_health)
				health.draw(g2d);
		}
		
		g2d.setColor(Color.YELLOW);
		g2d.fill(d_hill);
		
		for(GameCharacter character : d_characters)
			character.draw(g2d);
		
		synchronized(d_players) {
			
			for(Player player : d_players)
				player.draw(g2d);
		}
		
		g2d.setColor(Color.BLACK);
		
		if(d_tmpBlock != null)
			g2d.fill(d_tmpBlock);
		
		for(int y = 0; y != d_mapHeight / d_tileSize; ++y) {
			for(int x = 0; x != d_mapWidth / d_tileSize; ++x) {
				if(d_tileMap[y][x] == true) {
					g2d.fillRect(x * d_tileSize, y * d_tileSize, d_tileSize, d_tileSize);
				}
			}
		}
		
		
		g2d.fillRect(800, 0, 224, 768);
		
		for(int index = 0; index != d_players.size(); ++index)
			d_players.get(index).drawUI(g2d, index);
		
		g2d.setColor(Color.WHITE);
		g2d.drawString("Points: " + d_points, 805, 730);
		g2d.drawString("Connect to: " + SpectroPolaris.server().ip(), 805, 750);
	}

	public void removeGameCharacter(GameCharacter character) {
		d_characters.remove(character);
	}

	public void removeBlock(int x, int y) {
		d_tileMap[y / d_tileSize][x / d_tileSize] = false;
	}

	public void removePlayer(Player player) {
		synchronized(d_players) {
			d_players.remove(player);
		}
		
	}


}
