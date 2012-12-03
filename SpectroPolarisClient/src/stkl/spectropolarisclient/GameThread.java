package stkl.spectropolarisclient;

import android.graphics.Paint;

public class GameThread extends Thread {
	private GameView view;
	private Model model;
	private boolean running;
	
	public GameThread(GameView view, Model model) {
		this.view = view;
		this.model = model;
		this.running = true;
		
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		model.addGameCharacter(new GameCharacter(50, 50, 0, paint));
	}

	public void close() {
		running = false;
	}
	
	public void run() {
		while(running) {
			// Update model
			model.step();
			
			// Invalidate View
			view.postInvalidate();
			
			// Sent data
			//Client.getInstance().sent();

			try {
				sleep(33);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Client.getInstance().close();
	}
}
