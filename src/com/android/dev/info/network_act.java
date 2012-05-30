
package com.android.dev.info;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

public class network_act extends Activity{
	
	//initialise
	private String network_type_string, show_state_string, roaming_str;
	private TextView show_network_info;
	
	//initialization for TELEPHONY MANAGER & PHONESTATELISTENER 
    // & SERVICESTATE
    TelephonyManager TelephonyMgr;
    mPhoneStateListener state_listener;

  //---------------------------------------------- ON CREATE ----------------------------------------------------------//
    
	public void onCreate(Bundle savedInstanceState) {
    	// call the onCreate function of the superclass Activity 
        super.onCreate(savedInstanceState);
        
        Log.d("hello","Debug");

        setContentView(R.layout.phone_identity_display);
        
      //---------------------------------------------- INITIALISATION ----------------------------------------------------------//
        
        //binding the xml TextView to the activity
        show_network_info = (TextView)findViewById(R.id.phone_ident_text);
	}
	
	//---------------------------------------------- ON START ----------------------------------------------------------//
	public void onStart() {
    	// call the onCreate function of the superclass Activity 
        super.onStart();
        
        TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //CALLING the listening function
        TelephonyMgr.listen(state_listener, PhoneStateListener.LISTEN_SERVICE_STATE|PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        state_listener = new mPhoneStateListener();
        
        //---------------------------------------------- NETWORK TYPE ----------------------------------------------------------//
        
        //converting network type from int to corresponding string
        switch(TelephonyMgr.getNetworkType())
        {
        	case TelephonyManager.NETWORK_TYPE_UNKNOWN:
        		network_type_string = "Network Type Unknown";
        		break;
        		
        	case TelephonyManager.NETWORK_TYPE_GPRS:
        		network_type_string = "GPRS";
        		break;
        		
        	case TelephonyManager.NETWORK_TYPE_EDGE:
        		network_type_string = "EDGE";
        		break;
        		
        	case TelephonyManager.NETWORK_TYPE_UMTS:
        		network_type_string = "UMTS";
        		break;
        		
        	case TelephonyManager.NETWORK_TYPE_CDMA:
        		network_type_string = "CDMA";
        		break;
        		
        	case TelephonyManager.NETWORK_TYPE_EVDO_0:
        		network_type_string = "EVDO revision 0";
        		break;
        		
        	case TelephonyManager.NETWORK_TYPE_EVDO_A:
        		network_type_string = "EVDO revision A";
        		break;
        		
        	case TelephonyManager.NETWORK_TYPE_1xRTT:
        		network_type_string = "1xRTT";
        		break;
        		
        	case TelephonyManager.NETWORK_TYPE_HSDPA:
        		network_type_string = "HSDPA";
        		break;
        		
        	case TelephonyManager.NETWORK_TYPE_HSUPA:
        		network_type_string = "HSUPA";
        		break;
        		
        	case TelephonyManager.NETWORK_TYPE_HSPA:
        		network_type_string = "HSPA";
        		break;
        		
        	default:
        		network_type_string = "Network Type Unknown";
        }
	}
	
	//---------------------------------------------- ON PAUSE ON RESUME ----------------------------------------------------------//
	 /* Called when the application is minimized */
    @Override
    protected void onPause()
    {
      super.onPause();
      TelephonyMgr.listen(state_listener, PhoneStateListener.LISTEN_NONE);
    }

    /* Called when the application resumes */
    @Override
    protected void onResume()
    {
      super.onResume();
      TelephonyMgr.listen(state_listener,PhoneStateListener.LISTEN_SERVICE_STATE|PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }
	
	//---------------------------------------------- PHONE STATE LISTENER ----------------------------------------------------------//
	
	private class mPhoneStateListener extends PhoneStateListener {
		
		//---------------------------------------------- SERVICE STATE & ROAMING ----------------------------------------------------------//
		
		@Override
    	public void onServiceStateChanged(ServiceState serviceState) {
    		
    		//converting state information from int to corresponding string
        	switch(serviceState.getState()) {
	        	case ServiceState.STATE_IN_SERVICE:
	        		show_state_string = "In Service";
	        		break;
	        		
	        	case ServiceState.STATE_OUT_OF_SERVICE:
	        		show_state_string = "Out of Service";
	        		break;
	        		
	        	case ServiceState.STATE_EMERGENCY_ONLY:
	        		show_state_string = "Emergency Only";
	        		break;
	        		
	        	case ServiceState.STATE_POWER_OFF:
	        		show_state_string = "Power Off";
	        		break;
	        		
	        	default:
	        		show_state_string = "State Unknown";
	        		break;
	        }
    		
    		super.onServiceStateChanged(serviceState);
    		
    		//---------------------------------------------- ROAMING STATUS ----------------------------------------------------------//
            
            //roaming status in boolean
            boolean roaming = serviceState.getRoaming();
            
            if(roaming == true)
            {
            	roaming_str = "Roaming";
            }
            else if(roaming == false)
            {
            	roaming_str = "Not Roaming";
            }
            
    	}
		
		//---------------------------------------------- SIGNAL STRENGTH ----------------------------------------------------------//
		
		@Override
		public void onSignalStrengthsChanged(SignalStrength signal){
			
			//in "asu" format
			int signal_int = signal.getGsmSignalStrength();
			
			//formula to convert to dbm from asu [ dBm = (2*ASU)-113 ]
			int signal_dbm = (signal_int*2)-113;
			
			String output_signal = String.valueOf(signal_int);
			String output_dbm = String.valueOf(signal_dbm);
			
			 //retrieving network information
            String sim_serial = TelephonyMgr.getSimSerialNumber();
            String net_operator = TelephonyMgr.getNetworkOperatorName();

            //printing network information
			show_network_info.setText(Html.fromHtml("<big>" + "Network Operator: " + "</big>" + "<br/>" + "<small>" + net_operator +  "</small>" + "<br/><br/>" 
    				+ "<big>" + "Sim Serial Number: " + "</big>" + "<br/>" + "<small>" + sim_serial +  "</small>" + "<br/><br/>"
        			+ "<big>" + "Network Type: " + "</big>" + "<br/>" + "<small>" + network_type_string +  "</small>" + "<br/><br/>" 
    				+ "<big>" + "Service State: " + "</big>" + "<br/>" + "<small>" + show_state_string +  "</small>" + "<br/><br/>"
    				+ "<big>" + "Roaming: " + "</big>" + "<br/>" + "<small>" + roaming_str +  "</small>" + "<br/><br/>"
    				+ "<big>" + "Signal Strength: " + "</big>" + "<br/>" + "<small>" + output_dbm + " dBm  " + output_signal + " asu" + "</small>" + "<br/><br/>" ));
			
			super.onSignalStrengthsChanged(signal);
		}
	}
}
