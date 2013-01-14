import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.Random;
import java.util.Stack;


public class Enemy extends GameCharacter {
	private Stack<Node> path;
	private Node goal;
	private int lastPlayerPath;
	private long d_timeSinceLastBullet = 0;
	private final int SHOOTING_DISTANCE = 60;
	private final float MAX_SPEED = 0.8f;
	private final int PATROL_DISTANCE = 150;
	private static Random random = new Random();
	private static float d_spread = 0.2f;

	public Enemy(float x, float y, Color color) {
		super(x, y, 0, color);
		Model model = SpectroPolaris.frame().gamePanel().model();
		
		// Go to random point with x is between: hill.left - 100, hill.right + 100 and y between: hill.top - 100, hill.bottom + 100
		path = model.findPath(coor().x, coor().y, model.hill().x + (random.nextInt(model.hill().width + 2*PATROL_DISTANCE)) - PATROL_DISTANCE,
							  model.hill().y + (random.nextInt(model.hill().height + 2*PATROL_DISTANCE)) - PATROL_DISTANCE);

		goal = path.pop();
		lastPlayerPath = 0;
	}

	
	public void step() {
		Model model = SpectroPolaris.frame().gamePanel().model();
		
		setSpeed(MAX_SPEED);
		
		Player player = model.closestPlayer(coor().x, coor().y, 100);
		if(player != null && lastPlayerPath < 0) {			
			
			if(player.distanceFrom(coor().x, coor().y) < SHOOTING_DISTANCE && model.visible(coor().x, coor().y, player.coor().x, player.coor().y) == null) {				
				if(System.nanoTime() - d_timeSinceLastBullet > 250000000) {
					model.addBullet().instantiate(coor().x, coor().y, 
							(float) Math.atan2(player.coor().x - coor().x, player.coor().y - coor().y) + (random.nextFloat() - 0.5f) * d_spread, id());
					d_timeSinceLastBullet = System.nanoTime();
				}

				setSpeed(0);
				return;
			}
			

			synchronized(path) {
				path = model.findPath(coor().x, coor().y, player.x(), player.y());
				path.pop();
			}
			lastPlayerPath = 5;
		} else {
			lastPlayerPath--;
		}
		
		if(model.inTile(coor().x, coor().y, goal.x(), goal.y())) {
			synchronized(path) {
				if(path.isEmpty()) {
					// Go to random point with x is between: hill.left - 50, hill.right + 50 and y between: hill.top - 50, hill.bottom + 50
					path = model.findPath(coor().x, coor().y, model.hill().x + (random.nextInt(model.hill().width + 100)) - 50,
							  			  model.hill().y + (random.nextInt(model.hill().height + 100)) - 50);
				}
				
				goal = path.pop();
				
				setDirection((float) Math.atan2(goal.x() * model.tileSize() + 5 - coor().x, goal.y() * model.tileSize() + 5 - coor().y));
				// System.out.println("New goal: from " + d_x + ", " + d_y + " to " + goal.x() * 10 + ", " + goal.y() * 10 + ". New direction: " + d_direction);
			}
		} 
		
		super.step();
	}
	
	@Override
	public void draw(Graphics2D g2d) {
		if(SpectroPolaris.frame().gamePanel().model().isDrawPaths()) {
			g2d.setColor(Color.GREEN);
			Node array[] = new Node[path.size()];
			path.copyInto(array);
			for(Node node: array) {
				g2d.drawRect(node.x() * 10, node.y() * 10, 10, 10);
			}
		}
		
		super.draw(g2d);
	}
	
	public void forceNewPath() {
		synchronized(path) {
			path.clear();
		}
	}


	public boolean checkIfShot(Bullet bullet) {
		if(bullet.line().ptSegDistSq(coor()) < radius() * radius())
			return changeHealth(-50);
		
		return false;
	} 
}
