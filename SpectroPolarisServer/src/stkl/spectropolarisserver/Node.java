import java.awt.Point;


public class Node {
	private Point point;
	private Point parent;
	
	public Node(int x, int y, int parentX, int parentY) {
		point = new Point(x,y);
		parent = new Point(parentX, parentY);
	}

}
