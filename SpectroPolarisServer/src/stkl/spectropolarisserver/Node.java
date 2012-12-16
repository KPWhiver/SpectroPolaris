import java.awt.Point;


public class Node implements Comparable<Node> {
	private Point point;
	private Node parent;
	private int cost;
		
	public Node(int x, int y, Node parent, int xEnd, int yEnd, int costSoFar) {
		this.point = new Point(x, y);
		this.parent = parent;
		if(parent != null) 
			this.cost = costSoFar + Math.abs(parent.x() - point.x) + Math.abs(parent.y() - point.y) + Math.abs(point.x - xEnd) + Math.abs(point.y - yEnd);
		else 
			this.cost = 0;
	}
	
	public int x() {
		return point.x;
	}
	
	public int y() {
		return point.y;
	}

	public Point getPoint() {
		return point;
	}

	public Node getParent() {
		return parent;
	}
	
	public int getCost() {
		return cost;
	}

	@Override
	public int compareTo(Node arg0) {
		return cost - arg0.getCost();
	}

}
