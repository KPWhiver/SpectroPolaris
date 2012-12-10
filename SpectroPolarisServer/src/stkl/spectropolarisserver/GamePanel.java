import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;



public class GamePanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1552746400473185110L;

	private Model d_model;
	
	private int d_xStart = -1;
	private int d_yStart = -1;
	private Block d_edit;
	
	private float d_scale;
	
	public GamePanel() {
		d_model = new Model();
		d_scale = 1;
		setFocusable(true);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
	}
	
	public Model model() {
		return d_model;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		d_scale = getHeight() / 768.0f;
		g2d.scale(d_scale, d_scale);
		
		d_model.draw(g2d);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int eventX = (int) (e.getX() / d_scale);
		int eventY = (int) (e.getY() / d_scale);
		
		if(e.isShiftDown()) {
			d_model.removeBlock(eventX, eventY);
			return;
		}
		
		d_xStart = (int) (10 * Math.floor(eventX / 10.0));
		d_yStart = (int) (10 * Math.floor(eventY / 10.0));
		
		d_edit = new Block(d_xStart, d_yStart, 0, 0);
		d_model.addBlock(d_edit);
	}

	@Override
	public void mouseReleased(MouseEvent e) {	
		if(d_xStart < 0)
			return;		
		
		if(d_edit.height() <= 0 || d_edit.width() <= 0)
			d_model.removeBlock(d_xStart, d_yStart);
			
		d_edit = null;
		d_xStart = -1;
		d_yStart = -1;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int eventX = (int) (e.getX() / d_scale);
		int eventY = (int) (e.getY() / d_scale);
		
		if(d_xStart < 0)
			return;
		
		int x = (int) (10 * Math.floor(eventX / 10.0));
		int y = (int) (10 * Math.floor(eventY / 10.0));
		

	    d_edit.update(d_xStart, d_yStart, x - d_xStart, y - d_yStart);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_S)
			d_model.save();
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
