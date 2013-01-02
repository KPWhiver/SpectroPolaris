import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.Stack;


public class Enemy extends GameCharacter {
	private Stack<Node> path;
	private Node goal;
	private int lastPlayerPath;
	private long d_timeSinceLastBullet = 0;

	public Enemy(float x, float y, Color color) {
		super(x, y, 0, color);
		Model model = SpectroPolaris.frame().gamePanel().model();
		path = model.findPath(coor().x, coor().y, model.hill().x, model.hill().y);
		goal = path.pop();
		lastPlayerPath = 0;
	}

	
	public void step() {
		Model model = SpectroPolaris.frame().gamePanel().model();
		
		setSpeed(1);
		
		Player player = model.closestPlayer(coor().x, coor().y, 100);
		if(player != null && lastPlayerPath < 0) {			
			
			if(player.distanceFrom(coor().x, coor().y) < 80 && model.visible(coor().x, coor().y, player.coor().x, player.coor().y) == null) {				
				if(System.nanoTime() - d_timeSinceLastBullet > 250000000) {
					model.addBullet().instantiate(coor().x, coor().y, (float) Math.atan2(player.coor().x - coor().x, player.coor().y - coor().y), id());
					d_timeSinceLastBullet = System.nanoTime();
				}

				setSpeed(0);
				return;
			}
			

			
			path = model.findPath(coor().x, coor().y, player.x(), player.y());
			path.pop();
			lastPlayerPath = 5;
		} else {
			lastPlayerPath--;
		}
		
		if(model.inTile(coor().x, coor().y, goal.x(), goal.y())) {
			if(path.isEmpty()) {
				path = model.findPath(coor().x, coor().y, model.hill().x, model.hill().y);
			}
			goal = path.pop();
			setDirection((float) Math.atan2(goal.x() * model.tileSize() + 5 - coor().x, goal.y() * model.tileSize() + 5 - coor().y));
			// System.out.println("New goal: from " + d_x + ", " + d_y + " to " + goal.x() * 10 + ", " + goal.y() * 10 + ". New direction: " + d_direction);
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


	public void checkIfShot(Bullet bullet) {
		if(bullet.line().ptSegDistSq(coor()) < radius() * radius())
			changeHealth(-50);
	} 
}
