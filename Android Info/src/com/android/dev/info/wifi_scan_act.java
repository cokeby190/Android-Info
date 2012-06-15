package com.android.dev.info;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class wifi_scan_act extends ListActivity{
	
	private static final int refresh_ID = Menu.FIRST + 1;
	private static final int home_ID = Menu.FIRST;
	
	private ListView wifi_scan;
	private WifiInfo wifi_info;
	private WifiManager wifiMan;
	
	private ArrayList<ScanResult> scans;
	private String[] results;
	private WifiScan new_scan;
	
	private IntentFilter ifilter;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// call the onCreate function of the superclass Activity 
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        //WIFI MAC Address
        wifiMan = (WifiManager)getSystemService(WIFI_SERVICE);
        wifi_info = wifiMan.getConnectionInfo();
        
        //WIFI intent (Scanning)
        IntentFilter ifilter = new IntentFilter();
        ifilter.addAction(wifiMan.SCAN_RESULTS_AVAILABLE_ACTION);
        ifilter.addAction(wifiMan.WIFI_STATE_CHANGED_ACTION);
  	  	this.registerReceiver(new_scan, ifilter); 
  	  	
        new_scan = new WifiScan();
    }
    
  //---------------------------------------------- ON START ----------------------------------------------------------//
  	public void onStart(){
  		
  		super.onStart();
  		
  		//WIFI intent (Scanning)
        ifilter = new IntentFilter();
        ifilter.addAction(wifiMan.SCAN_RESULTS_AVAILABLE_ACTION);
        ifilter.addAction(wifiMan.WIFI_STATE_CHANGED_ACTION);
  	  	this.registerReceiver(new_scan, ifilter); 
  	  	
  	  	if(wifiMan.getWifiState() == wifiMan.WIFI_STATE_ENABLED) {
  	  		wifiMan.startScan();
  	  	}
}
    
  //---------------------------------------------- ON RESUME ----------------------------------------------------------//
    //user interaction, calls other activities based on option chosen
    public void onResume()
    {
    	super.onResume();
    	
    	//WIFI intent (Scanning)
        ifilter = new IntentFilter();
        ifilter.addAction(wifiMan.SCAN_RESULTS_AVAILABLE_ACTION);
        ifilter.addAction(wifiMan.WIFI_STATE_CHANGED_ACTION);
  	  	this.registerReceiver(new_scan, ifilter); 
  	  	
  	  	if(wifiMan.getWifiState() == wifiMan.WIFI_STATE_ENABLED) {
	  		wifiMan.startScan();
	  		Toast.makeText(getApplicationContext(),"Starting Scan...", Toast.LENGTH_SHORT).show();
	  	}
    }
    
    protected void onPause() {
        unregisterReceiver(new_scan);
        super.onPause();
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, refresh_ID, 0, "Refresh");
        menu.add(0, home_ID, 0, "Home");
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	switch (item.getItemId()) {
			case refresh_ID:
				if(wifiMan.getWifiState() == wifiMan.WIFI_STATE_ENABLED) {
			  		wifiMan.startScan();
			  		Toast.makeText(getApplicationContext(),"Scan Completed", Toast.LENGTH_SHORT).show();
			  	}
				break;
				
			case home_ID:
			    //unregisterReceiver(new_scan);
				Intent intent = new Intent(this,AndroidInfoActivity.class);
				startActivity(intent);
				break;
    	}
        return super.onMenuItemSelected(featureId, item);
    }
    
    class scan_sort implements Comparator<ScanResult> {
  	   
		public int compare(ScanResult lhs, ScanResult rhs) {
			Integer LH = lhs.level;
			Integer RH = rhs.level;
			return LH.compareTo(RH);
		}
	}
    
  //---------------------------------------------- WIFI onchanged ----------------------------------------------------------//
  	class WifiScan extends BroadcastReceiver {
  		
  		@Override
  		public void onReceive(Context context, Intent intent) {
  	      //---------------------------------------------- WIFI INFORMATION ----------------------------------------------------------//
  			
  			if(wifiMan.getWifiState() == wifiMan.WIFI_STATE_ENABLED){
  			
  			String action = intent.getAction();
  				//if (action.equals(wifiMan.SCAN_RESULTS_AVAILABLE_ACTION)) {
  				if (action.equals(wifiMan.SCAN_RESULTS_AVAILABLE_ACTION) || action.equals(wifiMan.WIFI_STATE_CHANGED_ACTION)) {
  	        	
  		  			wifiMan.startScan();
  					Toast.makeText(context,"Starting Scan...", Toast.LENGTH_SHORT).show();	
  		        	
  					// List available networks
	    			scans = (ArrayList<ScanResult>) wifiMan.getScanResults();
	    			
	    			if(scans != null)
	    			{	
	    				Collections.sort(scans, new scan_sort());
	    				Collections.sort(scans, Collections.reverseOrder(new scan_sort()));
	    				
//	    				ScanResult temp;
//	    				for (int p = 0; p < scans.size(); ++p) {
//	    					temp = scans.get(p);
//	    					for (int q = 0; q < scans.size(); ++q) {
//	    						if(scans.get(q).SSID.equals(temp.SSID)) {
//	    							if(wifi_info.getSSID()!= scans.get(q).SSID) {
//	    								scans.remove(q);
//	    							}
//	    						}
//	    					}
//	    				}

		    			results = new String[scans.size()];
		    			for (int i = 0; i < scans.size(); ++i) {
		    				for (int j = 0; j < scans.size(); ++j) {
		    					if(wifi_info.getSSID()!= null) {
		    						if(wifi_info.getSSID().equals(scans.get(i).SSID))
			    					{	
			    						results[i] = "SSID: " + scans.get(i).SSID + "\nSignal: " + scans.get(i).level
			    								+ "\nSecurity: " + scans.get(i).capabilities + "\nStatuss: Connected";
			    					}
			    					else
			    					{
			    						results[i] = "SSID: " + scans.get(i).SSID + "\nSignal: " + scans.get(i).level
			    								+ "\nSecurity: " + scans.get(i).capabilities + "\nStatuss: Not Connected";
			    					}
		    					}
		    					else
		    					{
		    						results[i] = "SSID: " + scans.get(i).SSID + "\nSignal: " + scans.get(i).level
		    								+ "\nSecurity: " + scans.get(i).capabilities + "\nStatuss: Not Connected";
		    					}
		    				}
		    			}
		    			
		    			wifi_scan = (ListView) findViewById(android.R.id.list);
		    	        
		    	        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.main, R.id.list_text, results);
		    			setListAdapter(adapter);
		    			
		    			Toast.makeText(context,"Scan Complete!", Toast.LENGTH_SHORT).show();
	    			}
  	            }
  	        } 
  		}
  	};
}