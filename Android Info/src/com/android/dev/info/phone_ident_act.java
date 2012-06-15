package com.android.dev.info;

import java.lang.reflect.Method;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.widget.TextView;

public class phone_ident_act extends Activity{
	
	//---------------------------------------------- INITIALISATION ----------------------------------------------------------//
	private TextView show_phone_info, show_uptime;
	
	private String imei, imei_sv, phoneNumber, imsi, serial, bt_address;
	private String isCharging;
	
	private int battery_level;
	
	private WifiInfo wifi_info;
    private Handler mHandler = new Handler();
    private TelephonyManager mTelephonyMgr;
    private BluetoothAdapter mBluetoothAdapter;
    //private battery_update battery;
	
  //---------------------------------------------- ON CREATE ----------------------------------------------------------//
	public void onCreate(Bundle savedInstanceState) {
    	// call the onCreate function of the superclass Activity 
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.phone_identity_display);
        
    	//binding the xml TextView to the activity
        show_phone_info = (TextView)findViewById(R.id.phone_ident_text);
        
        //binding the xml TextView to the activity
        show_uptime = (TextView)findViewById(R.id.uptime_text);
        
        //calling this so can access phone information
        mTelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        
        //BATTERY
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
  		this.registerReceiver(this.battery_update, ifilter);

	 }
	
	@Override
    protected void onResume() {
        super.onResume();
        
        //BATTERY
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
  		this.registerReceiver(this.battery_update, ifilter);
    }
	
	@Override
    protected void onPause() {
        super.onPause();
        
        unregisterReceiver(this.battery_update);
    }
	
	//---------------------------------------------- ON START ----------------------------------------------------------//
	public void onStart(){
		
		super.onStart();
		
		//---------------------------------------------- UPTIME UI update ----------------------------------------------------------//
		mHandler.removeCallbacks(update_uptime);
        mHandler.postDelayed(update_uptime, 100);
        
      //---------------------------------------------- PHONE INFO ----------------------------------------------------------//

        //access phone information
        // Requires READ_PHONE_STATE (require user permissions)
        imei = mTelephonyMgr.getDeviceId(); 
        imei_sv = mTelephonyMgr.getDeviceSoftwareVersion();
        phoneNumber = mTelephonyMgr.getLine1Number();
        imsi = mTelephonyMgr.getSubscriberId();
        
      //---------------------------------------------- WIFI MAC ADDRESS ----------------------------------------------------------//
        //WIFI MAC Address
        WifiManager wifiMan = (WifiManager)getSystemService(WIFI_SERVICE);
        wifi_info = wifiMan.getConnectionInfo();
        
      //---------------------------------------------- BLUETOOTH ADDRESS ----------------------------------------------------------//
        //Bluetooth Address
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        
        // Device supports Bluetooth. if returns null means device DOES NOT SUPPORT bluetooth.
        if (mBluetoothAdapter != null) {
        	bt_address = mBluetoothAdapter.getAddress();
        	if(bt_address == null) {
        		bt_address = "Unavailable";
        	}
        } else
        {
        	bt_address = "Bluetooth Address not found or device not supported.";
        }
        
      //---------------------------------------------- SERIAL NUMBER ----------------------------------------------------------//
        //Get Device Serial Number
        serial = null; 

        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception ignored) {
        }
       
	}
	private BroadcastReceiver battery_update = new BroadcastReceiver() {
	//private class battery_update extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			
			String action = intent.getAction();
            if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
				//---------------------------------------------- BATTERY STATUS ----------------------------------------------------------//
				// Are we charging / charged?
				//retrieve extended data from the intent, if no value to be found return -1
				int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
				
				switch(status) {
					case BatteryManager.BATTERY_STATUS_CHARGING:
						isCharging = "Charging! (:";
						break;
					
					case BatteryManager.BATTERY_STATUS_FULL:
						isCharging = "Charged Full! (:";
						break;
						
					case BatteryManager.BATTERY_STATUS_DISCHARGING:
						isCharging = "Discharging";
						break;
						
					case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
						isCharging = "Not Charging";
						break;
						
					case BatteryManager.BATTERY_STATUS_UNKNOWN:
						isCharging = "Unknown";
						break;
				}
				
				battery_level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            }
		}
	};
	
	//---------------------------------------------- UPTIME ----------------------------------------------------------//
	//update UI according to UPTIME
	private Runnable update_uptime = new Runnable() {
        public void run() {
            long millis = SystemClock.uptimeMillis();
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            minutes = minutes % 60;
            seconds = seconds % 60;
            
            show_phone_info.setText(Html.fromHtml("<big>" + "Battery Status: " + "</big>" + "<br/>" + "<small>" + isCharging + "</small>" + "<br/><br/>"
            		+ "<big>" + "Battery Level: " + "</big>" + "<br/>" + "<small>" + battery_level + "%" + "</small>" + "<br/><br/>"
            		+ "<big>" + "Manufacturer: " + "</big>" + "<br/>" + "<small>" + Build.MANUFACTURER + "</small>" + "<br/><br/>"
    				+ "<big>" + "Model: " + "</big>" + "<br/>" + "<small>" + Build.MODEL +  "</small>" + "<br/><br/>" 
    				+ "<big>" + "Device Serial Number: " + "</big>" + "<br/>" + "<small>" + serial +  "</small>" + "<br/><br/>"  
    				+ "<big>" + "IMEI: " + "</big>" + "<br/>" + "<small>" + imei +  "</small>" + "<br/><br/>"  
    				+ "<big>" + "IMEI SV: " + "</big>" + "<br/>" + "<small>" + imei_sv +  "</small>" + "<br/><br/>" 
    				+ "<big>" + "Phone Number: " + "</big>" + "<br/>" + "<small>" + phoneNumber +  "</small>" + "<br/><br/>"
    				+ "<big>" + "IMSI: " + "</big>" + "<br/>" + "<small>" + imsi +  "</small>" + "<br/><br/>"
    				+ "<big>" + "Wi-Fi MAC Address: " + "</big>" + "<br/>" + "<small>" + wifi_info.getMacAddress() +  "</small>" + "<br/><br/>"
    				+ "<big>" + "Bluetooth Address: " + "</big>" + "<br/>" + "<small>" + bt_address +  "</small>" + "<br/><br/>"));

            if (seconds < 10) {
    	        show_uptime.setText(Html.fromHtml("<big>" + "Uptime: " + "</big>" + "<br/>" + "<small>" + millis + "/" + hours + ":" + minutes + ":0" + seconds + "</small>" + "<br/><br/>"));
            } else {
            	if(minutes < 10) {
            		show_uptime.setText(Html.fromHtml("<big>" + "Uptime: " + "</big>" + "<br/>" + "<small>" + millis + "/" + hours + ":0" + minutes + ":" + seconds + "</small>" + "<br/><br/>"));        
            	} else {
            	show_uptime.setText(Html.fromHtml("<big>" + "Uptime: " + "</big>" + "<br/>" + "<small>" + millis + "/" + hours + ":" + minutes + ":" + seconds + "</small>" + "<br/><br/>"));
            	}
            }
            
            //mHandler.postDelayed(this, 1000);
            
            mHandler.postAtTime(this, millis);
        }
     };
}
