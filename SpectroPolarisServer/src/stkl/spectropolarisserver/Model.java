import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;


public class Model {
	private ArrayList<GameCharacter> d_characters;
	private ArrayList<Player> d_players;

	// tileMap with blocks (true means block, false means noblock)
	private boolean[][] d_tileMap;
	private final int d_tileSize = 10;
	
	// this rectangle is here to temporarily draw a block when creating them
	private Rectangle d_tmpBlock;
	
	private Rectangle d_hill;
	private int d_points;
	
	private final int d_mapWidth = 800;
	private final int d_mapHeight = 768;
	
	public int tileSize() {
		return d_tileSize;
	}
	
	public Model() {
		d_characters = new ArrayList<GameCharacter>();
		d_players = new ArrayList<Player>();
		
		d_hill = new Rectangle(200, 200, 100, 100);

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
		synchronized(this) {
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
		boolean hillCaptured = false;
		
		for(Player player : d_players) {
			if(d_hill.contains(player.x(), player.y()))
				hillCaptured = true;
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
		
		g2d.setColor(Color.YELLOW);
		g2d.fill(d_hill);
		
		for(GameCharacter character : d_characters)
			character.draw(g2d);
		
		synchronized(this) {
			
			for(Player player : d_players)
				player.draw(g2d);
		}
		
		g2d.setColor(Color.BLACK);
		
		if(d_tmpBlock != null)
			g2d.fill(d_tmpBlock);
		
		for(int y = 0; y != d_mapHeight / d_tileSize; ++y) {
			for(int x = 0; x != d_mapWidth / d_tileSize; ++x) {
				if(d_tileMap[y][x]) {
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
		synchronized(this) {
			d_players.remove(player);
		}
		
	}
	
	
	public Stack<Node> findPath(int xStart, int yStart, int xEnd, int yEnd) {
		int maxY = d_mapHeight / d_tileSize;
		int maxX = d_mapWidth / d_tileSize;
		// Used to store which nodes have been visisted
		boolean[][] visited = new boolean[maxY][maxX];
		// Used to store references to nodes so they can be updated
		Node[][] nodes =  new Node[maxY][maxX];
		// Used to store nodes ordered by cost
		PriorityQueue<Node> queue = new PriorityQueue<Node>();
		
		Node current = new Node(xStart, yStart, null, 0);
		
		queue.add(current);
		nodes[xStart][yStart] = current;
		
		
		while(true) {
			current = queue.poll();
			
			visited[current.y()][current.x()] = true;
			
			if(current.x() == xEnd && current.y() == yEnd) {
				Stack<Node> path = new Stack<Node>();
				
				while(current != null) {
					path.push(current);
					current = current.getParent();
				}
				
				return path;
			}
				
			for(int x=-1; x<2; ++x) {
				for(int y=-1; y<2; ++y) {
					// Remove diagonal options as temporary solution
					if(!(x==y) && (x+y) != 0) {
						// Add all neighbors to the PriorityQueue that haven't been visited and aren't blocked
						int newX = current.x() + x;
						int newY = current.y() + y;
						// Check if the new node is still within the map, is not blocked and not already visited
						if(newX >= 0 && newX < maxX && newY >= 0 && newY < maxY && !d_tileMap[newY][newX] && !visited[newY][newX]) {
							int cost = costFunction(newX, newY, current, xEnd, yEnd);
							
							if(nodes[newY][newX] == null) {
								// The node has not been visited yet
								Node newNode = new Node(newX, newY, current, cost);
								queue.add(newNode);
								nodes[newY][newX] =  newNode;
								
								
							} else if(cost < nodes[newY][newX].getCost()) {
								// The node has been visited but this path is better
								queue.remove(nodes[newY][newX]);
								nodes[newY][newX].setParent(current);
								nodes[newY][newX].setCost(cost);
								
							}
						}
					}
				}
			}
			
		}
		
		
		
		
	}
	
	private int costFunction(int newX, int newY, Node current, int xEnd, int yEnd) {
		// Cost so far + Manhattan distance from current to new position + Manhattan distance from new position to end node
		return current.getCost() + Math.abs(current.x() - newX) + Math.abs(current.y() - newY) + Math.abs(newX - xEnd) + Math.abs(newY - yEnd);
	}
	
}
