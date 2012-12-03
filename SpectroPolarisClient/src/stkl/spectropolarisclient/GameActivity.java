package stkl.spectropolarisclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends Activity {
	private GameThread gameThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        Model model = new Model();
        
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
    protected void onStop() {
    	super.onStop();
    	System.out.println("onStop has been called");
    	gameThread.close();
    }
}
