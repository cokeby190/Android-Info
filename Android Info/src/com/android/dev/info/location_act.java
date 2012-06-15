package com.android.dev.info;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.widget.TextView;

public class location_act extends Activity{

	LocationManager locationManager;
	
private TextView show_location;
	
	//---------------------------------------------- ON CREATE ----------------------------------------------------------//
	public void onCreate(Bundle savedInstanceState) {
    	// call the onCreate function of the superclass Activity 
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.phone_identity_display);
        
    	//binding the xml TextView to the activity
        show_location = (TextView)findViewById(R.id.phone_ident_text);
        
        // Acquire a reference to the system Location Manager
    	LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
    	
    	boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

		// Check if enabled and if not send user to the GSP settings
		// Better solution would be to display a dialog and suggesting to 
		// go to the settings
		if (!enabled) {
			
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
		}
		else
		{
			// Register the listener with the Location Manager to receive location updates
	    	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		}
	}
	
	//---------------------------------------------- ON RESUME ----------------------------------------------------------//
	public void onResume() {
        super.onResume();
     
	}
	
	//---------------------------------------------- ON RESUME ----------------------------------------------------------//
	public void onPause() {
        super.onPause();
        
  
	}

	// Define a listener that responds to location updates
	LocationListener locationListener = new LocationListener() {
	    
		public void onLocationChanged(Location location) {
	      // Called when a new location is found by the network location provider.
			show_location.setText(Html.fromHtml("<big>" + "Latitude: " + "</big>" + "<br/>" + "<small>" + location.getLatitude() + "</small>" + "<br/><br/>"
            		+ "<big>" +  "Longtitude: " + "</big>" + "<br/>" + "<small>" + location.getLongitude() + "</small>" + "<br/><br/>"
            		+ "<big>" + "Accuracy: " + "</big>" + "<br/>" + "<small>" + location.getAccuracy() + "</small>" + "<br/><br/>"
            		+ "<big>" + "Time: " + "</big>" + "<br/>" + "<small>" + location.getTime() +  "</small>" + "<br/><br/>"));
	    }

	    public void onStatusChanged(String provider, int status, Bundle extras) {}

	    public void onProviderEnabled(String provider) {}

	    public void onProviderDisabled(String provider) {}
	  };

	
}
