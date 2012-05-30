package com.android.dev.info;

import java.lang.reflect.Method;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class software_info_act extends Activity{
	
	TextView show_software_info;
	
	//---------------------------------------------- ON CREATE ----------------------------------------------------------//
	 public void onCreate(Bundle savedInstanceState) {
    	// call the onCreate function of the superclass Activity 
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.phone_identity_display);
        
        //binding the xml TextView to the activity
        show_software_info = (TextView)findViewById(R.id.phone_ident_text);
 
	}
	 
	//---------------------------------------------- ON START ----------------------------------------------------------//
	 public void onStart() {
		 
		 super.onStart();
		 
		 String browser="";
	        
	        //Get Device Baseband Version
	        String baseband = null; 

	        try {
	            Class<?> c = Class.forName("android.os.SystemProperties");
	            Method get = c.getMethod("get", String.class);
	            baseband = (String) get.invoke(c, "gsm.version.baseband");
	        } catch (Exception ignored) {
	        }
	        
	        //Get Device Kernel Version
	        String kernel = null; 

	        try {
	            Class<?> c = Class.forName("android.os.SystemProperties");
	            Method get = c.getMethod("get", String.class);
	            kernel = (String) get.invoke(c, "ro.kernel.id");
	        } catch (Exception ignored) {
	        }
	        
	        //Get Device Build Version
	        String build_vr = null; 

	        try {
	            Class<?> c = Class.forName("android.os.SystemProperties");
	            Method get = c.getMethod("get", String.class);
	            build_vr = (String) get.invoke(c, "ro.build.description");
	        } catch (Exception ignored) {
	        }
	        
	        //Get Device Software Version
	        String software_vr = null; 

	        try {
	            Class<?> c = Class.forName("android.os.SystemProperties");
	            Method get = c.getMethod("get", String.class);
	            software_vr = (String) get.invoke(c, "ro.product.version");
	        } catch (Exception ignored) {
	        }
	        
	        //displaying software information 
	        show_software_info.setText(Html.fromHtml("<big>" + "Firmware Version: " + "</big>" + "<br/>" + "<small>" + Build.VERSION.RELEASE + "</small>" + "<br/><br/>"
				        				+ "<big>" + "Baseband Version: " + "</big>" + "<br/>" + "<small>" + baseband +  "</small>" + "<br/><br/>" 
				        				+ "<big>" + "Kernel Version: " + "</big>" + "<br/>" + "<small>" + kernel +  "</small>" + "<br/><br/>"  
				        				+ "<big>" + "Build Number: " + "</big>" + "<br/>" + "<small>" + build_vr +  "</small>" + "<br/><br/>"  
				        				+ "<big>" + "Software Version: " + "</big>" + "<br/>" + "<small>" + software_vr +  "</small>" + "<br/><br/>" 
				        				+ "<big>" + "Browser Version: " + "</big>" + "<br/>" + "<small>" + browser +  "</small>" + "<br/><br/>"));
		 
	 }
}
