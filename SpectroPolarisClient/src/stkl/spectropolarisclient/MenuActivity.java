package stkl.spectropolarisclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MenuActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_menu);
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
    	Intent myIntent = new Intent(MenuActivity.this, JoinActivity.class);
    	MenuActivity.this.startActivity(myIntent);
    }
    
    /*
     * Quit button has been pressed. Stop the activity, closing the application.
     */
    public void quit(View view) {
    	finish();
    }
    
}
