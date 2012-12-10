package stkl.spectropolarisclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

public class JoinActivity extends Activity {
	private ArrayList<String> adresses;

    @SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_join);
        
        // Read ip adresses from memory
        try {
        	ObjectInputStream in = new ObjectInputStream(openFileInput("ip_adresses"));
			adresses = (ArrayList<String>) in.readObject();
			in.close();
		} catch (IOException e) {
			System.err.println("Error reading ip adresses from file");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        
        if(adresses == null)
        	System.out.println("adresses equals null");
        	adresses = new ArrayList<String>();
        
        System.out.println(adresses.toString());
        
        CustomAutoCompleteTextView textView = (CustomAutoCompleteTextView) findViewById(R.id.join_ip);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, adresses);
        textView.setAdapter(adapter);
    }
    
    @Override
    protected void onPause() {
    	// Store the ip adresses
    	System.out.println("Storing adresses");
    	System.out.println(adresses.toString());
    	try {
			ObjectOutputStream out =  new ObjectOutputStream(openFileOutput("ip_adresses", MODE_PRIVATE));
			out.writeObject(adresses);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.err.println("Error writing ip adresses to file");
			e.printStackTrace();
		}
    	
    	super.onPause();
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
    			
    			adresses.add(ipAdress);
    			
    			
    			// Start the GameActivity
    			Intent intent = new Intent(JoinActivity.this, GameActivity.class);
    	    	JoinActivity.this.startActivity(intent);

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
