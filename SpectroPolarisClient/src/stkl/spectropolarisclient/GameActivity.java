package stkl.spectropolarisclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends Activity {
	private GameThread gameThread;
	private Model model;
	private int centerHorizontal;
	private int motionPointerId = -2;
	private int shootPointerId = -2;
	private float[] originX = new float[2];
	private float[] originY = new float[2];


    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize window
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        // Calculate screen dimension
        Display display = getWindowManager().getDefaultDisplay();
        centerHorizontal = (int)(display.getWidth() /2);
        
        // Start game
        model = new Model();
        
        GameView gameView =  new GameView(this, model);
        //gameView.setModel(model);
        setContentView(gameView);
        
        gameThread = new GameThread(gameView, model);
        gameThread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_game, menu);
        return true;
    }
    
    @Override
	public boolean onTouchEvent(MotionEvent event) {
    	int pointerCount = event.getPointerCount();
    	
    	// Process no more than 2 touches
    	if (pointerCount > 2) {
    		pointerCount = 2;
    	}    	
    	
    	int actionId = event.getActionIndex();
    	
    	for (int i = 0; i < pointerCount; i++) {
    		
    		int id = event.getPointerId(i);
    		int action = (event.getAction() & MotionEvent.ACTION_MASK);
    		float x = event.getX(i);
    		float y = event.getY(i);
    		
    		switch (action){
	    		case MotionEvent.ACTION_DOWN: case MotionEvent.ACTION_POINTER_DOWN:
	    			if (actionId == i) { 
		    			System.out.println("Down: " + event.findPointerIndex(0) + ", " + event.findPointerIndex(1));
		    			if(x > centerHorizontal) {
		    				motionPointerId = id;
		    				model.setMotionOrigin(x, y);
		    			} else {
		    				shootPointerId = id;
		    				model.setShootOrigin(x, y);
		    			}
		    			originX[id] = x;
						originY[id] = y;
	    			}
	    			break;
	
	    		case MotionEvent.ACTION_MOVE:
	    			float deltaX = x - originX[id];
					float deltaY = y - originY[id];
					if (id == motionPointerId) {
						model.setMotionControls(deltaX, deltaY);
					} else if (id == shootPointerId) {
						model.setShootControls(deltaX, deltaY);
					}
	    			break;
	    			
	    		case MotionEvent.ACTION_UP: case MotionEvent.ACTION_POINTER_UP: 
	    			if (actionId == i) { 
		    			System.out.println("Up: " + event.findPointerIndex(0) + ", " + event.findPointerIndex(1));
		    			if (id == motionPointerId) {
		    				//System.out.println("Motion released, event: " + action + " as id: " + id);
		    				motionPointerId = -2;
							model.setMotionControls(0, 0);
							model.setMotionOrigin(-1, -1);
						} else if (id == shootPointerId) {
							//System.out.println("Shoot released, event: " + action + " as id: " + id);
							shootPointerId = -2;
							model.setShootControls(0, 0);
							model.setShootOrigin(-1, -1);
						}
	    			}
	    			break;
    			
    		}
    	}
    	
		return super.onTouchEvent(event);
	}

    @Override
    protected void onStop() {
    	super.onStop();
    	gameThread.close();
    }
}
