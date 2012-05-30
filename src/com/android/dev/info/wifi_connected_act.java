package com.android.dev.info;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class wifi_connected_act extends Activity{
	
	private static final int home_ID = Menu.FIRST;
	
	private TextView show_wifi;
	private WifiInfo wifi_info;
	private WifiManager wifiMan;
	
	private String wifi_ssid, wifi_ip_string;
	private int wifi_ip, wifi_link_speed, wifi_signal;
	
	private IntentFilter ifilter;
	
	//---------------------------------------------- ON CREATE ----------------------------------------------------------//
	public void onCreate(Bundle savedInstanceState) {
    	// call the onCreate function of the superclass Activity 
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.phone_identity_display);
        
    	//binding the xml TextView to the activity
        show_wifi = (TextView)findViewById(R.id.phone_ident_text);
        
        //WIFI MAC Address
        wifiMan = (WifiManager)getSystemService(WIFI_SERVICE);
        wifi_info = wifiMan.getConnectionInfo();
	 }
	
	//---------------------------------------------- ON START ----------------------------------------------------------//
	public void onStart(){
		
		super.onStart();
		
		//WIFI intent (RSSI / Signal change)
        ifilter = new IntentFilter(wifiMan.RSSI_CHANGED_ACTION);
        ifilter.addAction(wifiMan.WIFI_STATE_CHANGED_ACTION);
  		this.registerReceiver(this.wifi_update, ifilter); 	
	}
	
    public void onResume(){
		
		super.onResume();
		
		//WIFI intent (RSSI / Signal change)
        ifilter = new IntentFilter(wifiMan.RSSI_CHANGED_ACTION);
        ifilter.addAction(wifiMan.WIFI_STATE_CHANGED_ACTION);
  		this.registerReceiver(this.wifi_update, ifilter); 
	}
    
    public void onPause(){
		
		super.onPause();
		
		unregisterReceiver(this.wifi_update); 
	}
    
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, home_ID, 0, "Home");
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	switch (item.getItemId()) {
			case home_ID:	
				//unregisterReceiver(this.wifi_update);
				Intent intent = new Intent(this,AndroidInfoActivity.class);
				startActivity(intent);
				break;
    	}
        return super.onMenuItemSelected(featureId, item);
    }
	
	//---------------------------------------------- WIFI onchanged ----------------------------------------------------------//
	private BroadcastReceiver wifi_update = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
	      //---------------------------------------------- WIFI INFORMATION ----------------------------------------------------------//
			
			if(wifiMan.isWifiEnabled()){
			String action = intent.getAction();
				if (action.equals(wifiMan.WIFI_STATE_CHANGED_ACTION) || action.equals(wifiMan.RSSI_CHANGED_ACTION)) {
	        	
	        	
		        	wifi_ssid = wifi_info.getSSID();
			        wifi_ip = wifi_info.getIpAddress();
			        wifi_ip_string = android.text.format.Formatter.formatIpAddress(wifi_ip);
			        wifi_link_speed = wifi_info.getLinkSpeed();
			        wifi_signal = intent.getIntExtra(wifiMan.EXTRA_NEW_RSSI, wifi_info.getRssi());

		            show_wifi.setText(Html.fromHtml("<big>" + "Wifi SSID: " + "</big>" + "<br/>" + "<small>" + wifi_ssid + "</small>" + "<br/><br/>"
		            		+ "<big>" +  "IP Address: " + "</big>" + "<br/>" + "<small>" + wifi_ip_string + "</small>" + "<br/><br/>"
		            		+ "<big>" + "Link Speed: " + "</big>" + "<br/>" + "<small>" + wifi_link_speed + " Mbps" + "</small>" + "<br/><br/>"
		            		+ "<big>" + "Signal Strength: " + "</big>" + "<br/>" + "<small>" + wifi_signal + " dBm" + "</small>" + "<br/><br/>"));
	            }
	        	else {
		        	int state = wifiMan.getWifiState();
		        	if((state == wifiMan.WIFI_STATE_DISABLED) || (state == wifiMan.WIFI_STATE_DISABLING) || (state == wifiMan.WIFI_STATE_ENABLING) || (state == wifiMan.WIFI_STATE_ENABLED)) {
			        	show_wifi.setText(Html.fromHtml("<big>" + "Wifi SSID: " + "</big>" + "<br/>" + "<small>" + "Invalid (Wifi not enabled)" + "</small>" + "<br/><br/>"
			            		+ "<big>" + "IP Address: " + "</big>" + "<br/>" + "<small>" + "Invalid (Wifi not enabled)" + "</small>" + "<br/><br/>"
			            		+ "<big>" + "Link Speed: " + "</big>" + "<br/>" + "<small>" + "Invalid (Wifi not enabled)" + "</small>" + "<br/><br/>"
			            		+ "<big>" + "Signal Strength: " + "</big>" + "<br/>" + "<small>" + "Invalid (Wifi not enabled)" + "</small>" + "<br/><br/>"));
		        	}
		        }
	        } 
		}
	};
}