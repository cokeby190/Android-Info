package com.android.dev.info;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AndroidInfoActivity extends ListActivity {
	
	final static String[] list = {"Phone Information" , "Software Information", "Network", "Wifi", "Location", "Sensors", "XML" };
	
	private ListView lv;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// call the onCreate function of the superclass Activity 
        super.onCreate(savedInstanceState);
        
        //setContentView, method in Activity that fills the View
        //R.layout.main => in folder res\layout\main.xml
        //main.xml is the one that defines the View, or UI
        setContentView(R.layout.main);
        
        //ArrayAdapter a special kind of ListAdapter to supply data to ListView.
        // First paramenter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the View to which the data is written
        // Forth - the Array of data
        //ArrayAdapter(Context context, int resource, int textViewResourceId, List<T> objects) <constructor>
        //THEN bind to ArrayAdapter to ListActivity (provide cursor for list view) with setListAdapter
        setListAdapter(new ArrayAdapter<String>(this, R.layout.main, R.id.list_text, list));
        
        lv = (ListView) findViewById(android.R.id.list);

    }
    
  //---------------------------------------------- ON RESUME ----------------------------------------------------------//
    //user interaction, calls other activities based on option chosen
    public void onResume()
    {
    	super.onResume();
    	
    	lv.setTextFilterEnabled(true);
        
        lv.setOnItemClickListener(new OnItemClickListener() {
        
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		
        		switch(position)
        		{
        			case 0:
        				Intent open_phone_ident = new Intent(AndroidInfoActivity.this, phone_ident_act.class);
        				startActivity(open_phone_ident);
        				break;
        				
        			case 1:
        				Intent open_software_info = new Intent(AndroidInfoActivity.this, software_info_act.class);
        				startActivity(open_software_info);
        				break;
        				
        			case 2:
        				Intent open_network = new Intent(AndroidInfoActivity.this, network_act.class);
        				startActivity(open_network);
        				break;
        				
        			case 3:
        				Intent open_wifi = new Intent(AndroidInfoActivity.this, wifi_act.class);
        				startActivity(open_wifi);
        				break;
        				
        			case 4:
        				Intent open_location = new Intent(AndroidInfoActivity.this, location_maps_act.class);
        				startActivity(open_location);
        				break;
        				
        			case 5:
        				Intent open_sensor = new Intent(AndroidInfoActivity.this, sensor_act.class);
        				startActivity(open_sensor);
        				break;
        				
        			case 6:
        				Intent open_xml = new Intent(AndroidInfoActivity.this, xml_act.class);
        				startActivity(open_xml);
        				break;
        			
        		}
        	}
        });
    }
}