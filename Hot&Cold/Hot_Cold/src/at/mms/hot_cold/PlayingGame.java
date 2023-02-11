package at.mms.hot_cold;

import java.text.NumberFormat;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PlayingGame extends Activity{
	private Button enterButton;
	private static final int TEST_DELAY = 10000;				// Alpha delay - 60 seconds interval
	private static long CURRENT_DELAY = TEST_DELAY;				// actual delay variable
	private static boolean isRunning = true;					// var to show if the app is running
	private String Latitude;									// latitude as string
	private String Longitude;									// longitude as string
	private static boolean sent;								// variable for sendCoordinates() 
	private static String currentLocation="";
	private static String requiredLocation="";
	private String start="";
	private String status="";
    Thread background;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_game);

		enterButton= (Button) findViewById(R.id.request);
   	 	Intent in = getIntent();
   	 	requiredLocation= in.getExtras().getString("requiredLocation");
   	 
		enterButton.setOnClickListener(new View.OnClickListener() {
	         @Override
	         public void onClick(View v) {

			        checkLocation();
	         }});
	        	 // if the app was running, stop it, else continue
	        	 if (isRunning==false) {
	        		 background.interrupt();
	        	 }
	        	 // create a messager object and start it, change isRunning to true
	        	 else {
	        		 // create a thread in the background not on UI!
	        		 background=new Thread(new Runnable() {
	        			 @Override
	        			 public void run() {
	        				 isRunning = true;
	        				 Messager m = new Messager();
	        				 m.start();
	        			 }		
	        			 // start the thread
	        		 });
	        		 background.start();        
	        	 }
		}
		// send mail method, using the Email class
		public void sendCoordinates() {

	        getLocation();
	        currentLocation=Latitude +","+ Longitude;

			Toast.makeText(getApplicationContext(),currentLocation+"/"+requiredLocation, Toast.LENGTH_SHORT).show();
		}
		
		// new Thread for sending coordinates
		public class Messager extends Thread {
			
			public void run() {
				while (isRunning) {		
					try {
						runOnUiThread (new Runnable() {
							@Override
							public void run() {	
								if (sent) {
									sent = false;
								}
								// this is where you actually send something
								sendCoordinates();							
							}
						});
						Thread.sleep(CURRENT_DELAY);
					} catch (final Exception e) {
						e.getStackTrace();				
					}
				}
			}
		}
		private void checkLocation(){
			if(currentLocation.matches(requiredLocation)){
				isRunning=false;

				Toast.makeText(getApplicationContext(),"The location matches", Toast.LENGTH_SHORT).show();
				
			}
		}
		private void getLocation(){

		        LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		 		    List<String> providers = locMan.getProviders(true);
		 		    Location bestLocation = null;
		 		    for (String provider : providers) {
		 		        Location location = locMan.getLastKnownLocation(provider);


		 		        if (location == null) {
		 		            continue;
		 		        }
		 		        if (bestLocation == null
		 		                || location.getAccuracy() < bestLocation.getAccuracy()) {
		 		            bestLocation = location;
		 		        }
		 		    }
		 		
		     boolean enabled = locMan.isProviderEnabled(LocationManager.GPS_PROVIDER);

		     // check if enabled and if not send user to the GSP settings
		     // Better solution would be to display a dialog and suggesting to 
		     // go to the settings

		     if (!enabled) {
		       Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		       startActivity(intent);
		     } 
		     // Initialize the location fields

		        if (bestLocation != null) {
		          onLocationChanged(bestLocation);
		        } else {
		          Latitude = "Location not available";
		          Longitude = "Location not available";
		        }
		     //		        checkLocation();
		       }
		    
		       
		       public void onLocationChanged(Location location) {
		     
		     // get latitude
		        double lat = location.getLatitude();
		        
		        // get longitude
		        double longit = location.getLongitude();
		     
		        // format the coordinates
		        NumberFormat coordFormat = NumberFormat.getInstance();
		        coordFormat.setMinimumFractionDigits(2);
		        coordFormat.setMaximumFractionDigits(4);
		        Latitude = coordFormat.format(lat);
		        Longitude = coordFormat.format(longit);
		     
		   }
		       /* If the "back" button is pressed, the user exits the application
		   	 */
		   	public boolean onKeyDown(int keyCode, KeyEvent event) {
		   		if (keyCode == KeyEvent.KEYCODE_BACK) {
		   			isRunning=false;
		   			Intent intent =
		   			           new Intent(getApplicationContext(), Login.class);
		   			           startActivity(intent);
		   			return true;
		   		}
		   		return super.onKeyDown(keyCode, event);		
		   	}




}