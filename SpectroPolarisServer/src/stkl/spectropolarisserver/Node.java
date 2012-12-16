import java.awt.Point;


public class Node implements Comparable<Node> {
	private Point point;
	private Node parent;
	private int cost;
		
	public Node(int x, int y, Node parent, int cost) {
		this.point = new Point(x, y);
		this.parent = parent;
		this.cost = cost;
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

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	@Override
	public int compareTo(Node arg0) {
		return cost - arg0.getCost();
	}

}
