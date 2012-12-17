import java.awt.Point;


public class Node implements Comparable<Node> {
	private Point point;
	private Node parent;
	private float pathCost;
	private float heuristicCost; 
		
	public Node(int x, int y, Node parent, float pathCost, float heuristicCost) {
		this.point = new Point(x, y);
		this.parent = parent;
		this.pathCost = pathCost;
		this.heuristicCost = heuristicCost;
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
	
	public float getPathCost() {
		return pathCost;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public void setPathCost(float cost) {
		this.pathCost = cost;
	}
	
	public float getTotalCost() {
		return pathCost + heuristicCost; 
	}

	public float getHeuristicCost() {
		return heuristicCost;
	}

	public void setHeuristicCost(float heuristicCost) {
		this.heuristicCost = heuristicCost;
	}

	@Override
	public int compareTo(Node arg0) {
		return Float.compare(getTotalCost(), arg0.getTotalCost());
	}
	
	@Override
	public String toString() {
		return "(" + point.x + ", " + point.y + ")";
	}

}
