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
	private int motionPointerId = -1;
	private int shootPointerId = -1;
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
    	  	
    	for (int i = 0; i < pointerCount; i++) {
    		int id = event.getPointerId(i);
    		int action =  (event.getAction() & MotionEvent.ACTION_MASK);
    		float x = event.getX(i);
    		float y = event.getY(i);
    		
    		switch (action){
    		case MotionEvent.ACTION_DOWN: case MotionEvent.ACTION_POINTER_DOWN:
    			if(x > centerHorizontal) {
    				motionPointerId = id;
    			} else {
    				shootPointerId = id;
    			}
    			originX[id] = x;
				originY[id] = y;
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
    			if (id == motionPointerId) {
    				motionPointerId = -1;
					model.setMotionControls(0, 0);
				} else if (id == shootPointerId) {
					shootPointerId = -1;
					model.setShootControls(0, 0);
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
