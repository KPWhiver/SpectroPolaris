package stkl.spectropolarisclient;

public class GameThread extends Thread {
	private GameView view;
	
	public GameThread(GameView view) {
		this.view = view;
	}
	
	public void run() {
		// Update model
		
		// Invalidate View
		view.postInvalidate();
		
		// Sent data
		Client.getInstance().sent();
	}
}
