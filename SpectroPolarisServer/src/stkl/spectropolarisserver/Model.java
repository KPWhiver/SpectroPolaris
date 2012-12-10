import java.awt.Color;
import java.awt.Graphics2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


public class Model {
	private ArrayList<GameCharacter> d_characters;
	private ArrayList<Player> d_players;
	
	private ArrayList<Block> d_blocks;
	
	public Model() {
		d_characters = new ArrayList<GameCharacter>();
		d_blocks = new ArrayList<Block>();
		d_players = new ArrayList<Player>();

		try {		
			DataInputStream file = new DataInputStream(new FileInputStream("map.dat"));
			
			int numOfBlocks = file.readInt();
			
			for(int idx = 0; idx != numOfBlocks; ++idx) {
				int x = file.readInt();
				int y = file.readInt();
				int width = file.readInt();
				int height = file.readInt();
				
				if(width > 0 || height > 0)
					addBlock(new Block(x, y, width, height));
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
			
			file.writeInt(d_blocks.size());
			
			for(Block block : d_blocks) {
				file.writeInt(block.x());
				file.writeInt(block.y());
				file.writeInt(block.width());
				file.writeInt(block.height());
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
	
	public void addGameCharacter(GameCharacter character) {
		d_characters.add(character);
	}
	
	public void addPlayer(Player player) {
		d_players.add(player);
		
	}
		
	public void addBlock(Block block) {
		d_blocks.add(block);
	}
	
	public void step() {
		
		
		
		for(GameCharacter character : d_characters)
			character.step();
	}
	
	//private static long time = 0;
	
	public void draw(Graphics2D g2d) {
		//System.out.println(System.nanoTime() - time);
		//time = System.nanoTime();
		
		for(GameCharacter character : d_characters)
			character.draw(g2d);
		
		synchronized(d_players) {
			
			for(Player player : d_players)
				player.draw(g2d);
		}
		
		for(Block block : d_blocks)
			block.draw(g2d);
		
		
		g2d.fillRect(800, 0, 224, 768);
		
		for(int index = 0; index != d_players.size(); ++index)
			d_players.get(index).drawUI(g2d, index);
		
		g2d.setColor(Color.WHITE);
		g2d.drawString("Connect to: " + SpectroPolaris.server().ip() + ":" + SpectroPolaris.server().port(), 805, 750);
	}

	public void removeGameCharacter(GameCharacter character) {
		d_characters.remove(character);
	}

	public void removeBlock(int x, int y) {
		Block remove = null;
		for(Block block : d_blocks) {
			if(block.pointCollision(x, y))
				remove = block;
		}
		d_blocks.remove(remove);
	}

	public void removePlayer(Player player) {
		d_players.remove(player);
		
	}


}
