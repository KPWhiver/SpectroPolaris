package stkl.spectropolarisclient;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MenuActivity extends Activity {
	MediaPlayer mediaPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        
        setContentView(R.layout.activity_menu);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	mediaPlayer.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }
    
    /*
     * Join game button has been pressed. Start the JoinActivity.
     */
    public void joinGame(View view) {
    	mediaPlayer.pause();
    	Intent myIntent = new Intent(MenuActivity.this, JoinActivity.class);
    	MenuActivity.this.startActivity(myIntent);
    }
    
    /*
     * Quit button has been pressed. Stop the activity, closing the application.
     */
    public void quit(View view) {
    	mediaPlayer.stop();
    	mediaPlayer.release();
    	finish();
    }
    
}
