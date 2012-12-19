package stkl.spectropolarisclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;

import stkl.spectropolarisclient.mColorPicker.ColorPickerActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class JoinActivity extends Activity {
	private ArrayList<String> adresses;
	private Client client;
	
	private String username;
	private int color = 0xff000000;

    @SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_join);
        
        // Read ip adresses from memory
        try {
        	ObjectInputStream in = new ObjectInputStream(
        			openFileInput(getResources().getString(R.string.ip_adresses)));
			adresses = (ArrayList<String>) in.readObject();
			username = (String) in.readObject();
			color = in.readInt();
			in.close();
		} catch (IOException e) {
			System.err.println("Error reading ip adresses from file");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        
        if(adresses == null)
        	adresses = new ArrayList<String>();
        
        // Add ip adresses to AutoCompleteTextView
        CustomAutoCompleteTextView textView = (CustomAutoCompleteTextView) findViewById(R.id.join_ip);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, adresses);
        textView.setAdapter(adapter);
        
        // Set name in username EditText
        ((EditText)findViewById(R.id.username)).setText(username);
        
        // Set color in ColorChooser
        ((LinearLayout)findViewById(R.id.join_pickColorBar)).setBackgroundColor(color);
    }
    
    @Override
    protected void onPause() {
    	// Store the ip adresses, username and color
    	try {
			ObjectOutputStream out =  new ObjectOutputStream(
					openFileOutput(getResources().getString(R.string.ip_adresses), MODE_PRIVATE));
			out.writeObject(adresses);
			out.writeObject(username);
			out.writeInt(color);
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
     * Pick color button has been pressed. Start ColorPickerActivity.
     */
    public void pickColor(View view) {
    	Intent color_intent = new Intent(this, ColorPickerActivity.class);
    	color_intent.putExtra("color", color);
    	startActivityForResult(color_intent, 1);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
    		color = data.getIntExtra("color", 0xff000000);
    		((LinearLayout)findViewById(R.id.join_pickColorBar)).setBackgroundColor(color);
    	} else if(requestCode == 2) { // GameActivity has ended, stop the Client if needed
    		if(client.isAlive()) {
    			client.close();
    		}
    	}
    	super.onActivityResult(requestCode, resultCode, data);
    }

    /* 
     * Join game button has been pressed. Get server ip from EditText, verify correctness and connect to server.
     */
    public void joinGame(View view) {
    	EditText ipAdressEdit = (EditText)findViewById(R.id.join_ip);
    	final String ipAdress = ipAdressEdit.getText().toString();
    	Matcher matcher = Patterns.IP_ADDRESS.matcher(ipAdress);
    	
    	EditText nameEdit = (EditText)findViewById(R.id.username);
    	
    	if(!(nameEdit.length() > 0)) {
    		CharSequence text = "Please enter a name!";
    		int duration = Toast.LENGTH_SHORT;

    		Toast toast = Toast.makeText(this, text, duration);
    		toast.show();
    		return;
    	}
    	
    	username = nameEdit.getText().toString();
    	
    	if(matcher.matches()) {
    		
    		try {     			
    			// Start the GameActivity
    			Runnable runnable = new Runnable() {
    				public void run() {
    					try {
							client = new Client(ipAdress, username, color);
						} catch (IOException e) {
							System.err.println("Failed to setup connection to host " + ipAdress);
							e.printStackTrace();
						}
    	    			client.start();
    				};
    			};
    			Thread connector = new Thread(runnable);
    			connector.start();
    			connector.join();
    			
    			Intent intent = new Intent(this, GameActivity.class);
    			intent.putExtra("stkl.spectropolarisclient.color", color);
    	    	startActivityForResult(intent, 2);
    			
    			if(!adresses.contains(ipAdress)) {
    				adresses.add(ipAdress);
    			}    			
    			

    		} catch(Exception e) {
    			e.printStackTrace();
    			
    			
    			// Show toaster to tell user connection could not be setup.
        		CharSequence text = "Connection failed!\n" + e.toString();
        		int duration = Toast.LENGTH_LONG;

        		Toast toast = Toast.makeText(this, text, duration);
        		toast.show();
    		}
    		
    	} else {
    		// Show toaster to tell user IP is invalid.
    		CharSequence text = "Invalid IP!";
    		int duration = Toast.LENGTH_SHORT;

    		Toast toast = Toast.makeText(this, text, duration);
    		toast.show();
    	}
    }
    
}
