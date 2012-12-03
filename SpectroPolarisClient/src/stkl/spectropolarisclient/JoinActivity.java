package stkl.spectropolarisclient;

import java.util.regex.Matcher;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class JoinActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_join);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_join, menu);
        return true;
    }

    /* 
     * Join game button has been pressed. Get server ip from EditText, verify correctness and connect to server.
     */
    public void joinGame(View view) {
    	EditText ipAdressEdit = (EditText)findViewById(R.id.join_ip);
    	String ipAdress = ipAdressEdit.getText().toString();
    	Matcher matcher = Patterns.IP_ADDRESS.matcher(ipAdress);
    	
    	if(matcher.matches()) {
    		try { 
    			Client client = new Client(ipAdress);
    			client.start();
    			
    			// Start the game
    			
    		} catch(Exception e) {
    			e.printStackTrace();
    			
    			
    			// Show toaster to tell user connection could not be setup.
        		Context context = getApplicationContext();
        		CharSequence text = "Connection failed!";
        		int duration = Toast.LENGTH_SHORT;

        		Toast toast = Toast.makeText(context, text, duration);
        		toast.show();
    		}
    		
    	} else {
    		// Show toaster to tell user IP is invalid.
    		Context context = getApplicationContext();
    		CharSequence text = "Invalid IP!";
    		int duration = Toast.LENGTH_SHORT;

    		Toast toast = Toast.makeText(context, text, duration);
    		toast.show();
    	}
    }
    
}
