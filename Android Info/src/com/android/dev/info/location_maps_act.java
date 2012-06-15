package com.android.dev.info;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

public class location_maps_act extends MapActivity {

	private MapController mapController;
	private LocationManager mlocationManager;
	private MyLocationOverlay map_overlay;
	private MapView mapView;
	
	private String location_name; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.maps_view);
	    
	    //bind mapview to XML UI
	    mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    mapView.setSatellite(false);
	    mapView.displayZoomControls(true);
	    
	    //set default zoomed view
	    mapController = mapView.getController();
	    mapController.setZoom(17);
	    
	    //calling for location update
	    mlocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	    
	    Criteria check_criteria = new Criteria();
	    check_criteria.setAccuracy(Criteria.ACCURACY_FINE);
	    check_criteria.setPowerRequirement(Criteria.POWER_LOW);
	    // let Android select the right location provider for you
	    String myProvider = mlocationManager.getBestProvider(check_criteria, true); 

    	mlocationManager.requestLocationUpdates(myProvider, 0,0, new locationlistener());
		//mlocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,0, new locationlistener());
		//mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, new locationlistener());
		
		//my_location overlay
		map_overlay = new MyLocationOverlay(this, mapView);	
		mapView.getOverlays().add(map_overlay);
		
		map_overlay.runOnFirstFix(new Runnable() {
			public void run() {
				mapView.getController().animateTo(
						map_overlay.getMyLocation());
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();

		Criteria check_criteria = new Criteria();
	    check_criteria.setAccuracy(Criteria.ACCURACY_FINE);
	    check_criteria.setPowerRequirement(Criteria.POWER_LOW);
	    // let Android select the right location provider for you
	    String myProvider = mlocationManager.getBestProvider(check_criteria, true); 
	 
	    mlocationManager.requestLocationUpdates(myProvider, 0,0, new locationlistener());
			
    	//mlocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,0, new locationlistener());
		//mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, new locationlistener());
    	
		map_overlay.enableCompass();
		map_overlay.enableMyLocation();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	
		if (map_overlay.isCompassEnabled()) {
			map_overlay.disableCompass();
		}
		if (map_overlay.isMyLocationEnabled() )
		map_overlay.disableMyLocation();
	}
	
	@Override
	protected boolean isRouteDisplayed() {	
		return false;
	}
	
	private class locationlistener implements LocationListener {

		//listener to call for change when location is changed
		public void onLocationChanged(Location location) {
			
			//int latitude = (int) (1.3443371*1E6);
			//int longtitude = (int) (103.68799585*1E6);
			int latitude = (int) ((location.getLatitude())*1E6);
			int longtitude = (int) (location.getLongitude()*1E6);
			
			//calculate geoPoint of current location, and animate to that point
			GeoPoint curr_location = new GeoPoint(latitude, longtitude);
			mapController.animateTo(curr_location);
			
			getlocation(location.getLatitude(), location.getLongitude());
			
			Toast.makeText( getApplicationContext(), "Current Location: " + location_name ,Toast.LENGTH_LONG).show();
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
		
	}
	
	//output address information of location
	private void getlocation (double latitude, double longtitude) {
		
		Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault()); 
		List<Address> list;
		try {
			list = geocoder.getFromLocation(latitude, longtitude, 1);
			if (list != null & list.size() > 0) {
		        Address address = list.get(0);
		        location_name = "\nCountry: " + address.getCountryName() + "\nAddress: " + address.getAddressLine(0) 
		        		+ "\nLatitude: " + address.getLatitude() + "\nLongtitude: " + address.getLongitude();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public class overlay_marker extends ItemizedOverlay<OverlayItem> {

		public overlay_marker(Drawable defaultMarker) {
			super(defaultMarker);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected OverlayItem createItem(int i) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return 0;
		}

	}
}
