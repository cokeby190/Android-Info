package com.android.dev.info;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class sensor_act extends Activity implements SensorEventListener {

	private TextView show_sensor, show_intro, show_gyroscope, show_magnetometer, show_light
						,show_proximity, show_orientation, show_no_sensor;
	private ImageView show_image;
	
	private SensorManager sensorManager;
	private Sensor mAccelerometer, mGyroscope, mMagnetometer, mLight, mProximity, mOrientation;
	private List<Sensor> msensorList;
	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.sensor_display);
		show_intro = (TextView) findViewById(R.id.intro_text);
		show_image = (ImageView) findViewById(R.id.image_view);
		show_sensor = (TextView) findViewById(R.id.accelerometer_text);
		
		show_gyroscope = (TextView) findViewById(R.id.gyroscope_text);
		show_magnetometer = (TextView) findViewById(R.id.magnetometer_text);
		show_light = (TextView) findViewById(R.id.light_text);
		show_proximity = (TextView) findViewById(R.id.proximity_text);
		show_orientation = (TextView) findViewById(R.id.orientation_text);
		
		show_no_sensor = (TextView) findViewById(R.id.sensor_text);
		
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		mMagnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		mProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		mOrientation = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);	
		
		sensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);
		
		show_intro.setText(Html.fromHtml("<big>" + "Android's Coordinate System" + "</big>" + "<br/><br/>" + "<small>" 
				+ "The X axis is horizontal and points to the right, the Y axis is vertical and points up and the Z axis points towards the outside of the front face of the screen. In this system, coordinates behind the screen have negative Z values."
				+ "</small>" + "<br/><br/>"));
		
		msensorList = sensorManager.getSensorList(SensorManager.SENSOR_ALL);

	}
	
	public void onResume() {
		super.onResume();
		
		sensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);
		
		//------------------------------------- UNSUPPORTED SENSORS -------------------------------------//
		
			if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) {
				show_sensor.setText("\n\nACCELEROMETER: \n\n" + "Not available on device" + "\n");
			}
			if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) == null) {
				show_gyroscope.setText("\n\nGYROSCOPE: \n\n" + "Not available on device" + "\n");
			}
			if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null) {
				show_magnetometer.setText("\n\nMAGNETOMETER: \n\n" + "Not available on device" + "\n");
			}
			if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) == null) {
				show_light.setText("\n\nLIGHT: \n\n" + "Not available on device" + "\n");
			}
			if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) == null) {
				show_proximity.setText("\n\nPROXIMITY: \n\n" + "Not available on device" + "\n");
			}
			if (sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION) == null) {
				show_orientation.setText("\n\nORIENTATION: \n\n" + "Not available on device" + "\n");
			}
		
		//------------------------------------- UNSUPPORTED SENSORS -------------------------------------//
		
		show_no_sensor.setText("\n\nNumber of sensors detected: " + msensorList.size());
		
		//------------------------------------------------SHOW SENSOR MANUFACTURING INFO------------------------------------------------------------//
		
		
			final Button accelerometer = (Button) findViewById(R.id.accelerometer_button);
			
			accelerometer.setOnClickListener(new OnClickListener() {
	
				public void onClick(View button) {
					if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {

						Spanned accelerometer = Html.fromHtml("<big>Name: </big><br/>" + "<small>" + mAccelerometer.getName()+ "</small><br/><br/>"
													+ "<big>Type: </big><br/>" + "<small>" + "Accelerometer" + "</small><br/><br/>"
													+ "<big>Vendor: </big><br/>" + "<small>" + mAccelerometer.getVendor() + "</small><br/><br/>"
													+ "<big>Version: </big><br/>" + "<small>" + mAccelerometer.getVersion() + "</small><br/><br/>"
													+ "<big>Power: </big><br/>" + "<small>" + mAccelerometer.getPower() + " mA</small><br/>");
						
						dialog("Accelerometer Information", accelerometer);
					} else {
						dialog("Accelerometer Information", Html.fromHtml("Sensor Unsupported by Device"));
					}
				}
			});

		
			final Button gyroscope = (Button) findViewById(R.id.gyroscope_button);
			
			gyroscope.setOnClickListener(new OnClickListener() {
	
				public void onClick(View button) {
					
					if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
						Spanned gyroscope = Html.fromHtml("<big>Name: </big><br/>" + "<small>" + mGyroscope.getName()+ "</small><br/><br/>"
													+ "<big>Type: </big><br/>" + "<small>" + "Gyroscope" + "</small><br/><br/>"
													+ "<big>Vendor: </big><br/>" + "<small>" + mGyroscope.getVendor() + "</small><br/><br/>"
													+ "<big>Version: </big><br/>" + "<small>" + mGyroscope.getVersion() + "</small><br/><br/>"
													+ "<big>Power: </big><br/>" + "<small>" + mGyroscope.getPower() + " mA</small><br/>");
						
						dialog("Gyroscope Information", gyroscope);
					} else {
						dialog("Gyroscope Information", Html.fromHtml("Sensor Unsupported by Device"));
					}	
				}
			});	
		
		final Button magnetometer = (Button) findViewById(R.id.magnetometer_button);
		
		magnetometer.setOnClickListener(new OnClickListener() {

			public void onClick(View button) {
				
				if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
					Spanned magnetometer = Html.fromHtml("<big>Name: </big><br/>" + "<small>" + mMagnetometer.getName()+ "</small><br/><br/>"
												+ "<big>Type: </big><br/>" + "<small>" + "Magnetometer" + "</small><br/><br/>"
												+ "<big>Vendor: </big><br/>" + "<small>" + mMagnetometer.getVendor() + "</small><br/><br/>"
												+ "<big>Version: </big><br/>" + "<small>" + mMagnetometer.getVersion() + "</small><br/><br/>"
												+ "<big>Power: </big><br/>" + "<small>" + mMagnetometer.getPower() + " mA</small><br/>");
					
					dialog("Magnetometer Information", magnetometer);
				} else {
					dialog("Proximity Information", Html.fromHtml("Sensor Unsupported by Device"));
				}
			}
		});
		
		final Button light = (Button) findViewById(R.id.light_button);
		
		light.setOnClickListener(new OnClickListener() {

			public void onClick(View button) {
				
				if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
					Spanned light = Html.fromHtml("<big>Name: </big><br/>" + "<small>" + mLight.getName()+ "</small><br/><br/>"
												+ "<big>Type: </big><br/>" + "<small>" + "Light" + "</small><br/><br/>"
												+ "<big>Vendor: </big><br/>" + "<small>" + mLight.getVendor() + "</small><br/><br/>"
												+ "<big>Version: </big><br/>" + "<small>" + mLight.getVersion() + "</small><br/><br/>"
												+ "<big>Power: </big><br/>" + "<small>" + mLight.getPower() + " mA</small><br/>");
					
					dialog("Light Information", light);
				} else {
					dialog("Light Information", Html.fromHtml("Sensor Unsupported by Device"));
				}
			}
		});
		
		
		final Button proximity = (Button) findViewById(R.id.proximity_button);
		
		proximity.setOnClickListener(new OnClickListener() {

			public void onClick(View button) {
				
				if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null) {
					Spanned proximity = Html.fromHtml("<big>Name: </big><br/>" + "<small>" + mProximity.getName()+ "</small><br/><br/>"
												+ "<big>Type: </big><br/>" + "<small>" + "Proximity" + "</small><br/><br/>"
												+ "<big>Vendor: </big><br/>" + "<small>" + mProximity.getVendor() + "</small><br/><br/>"
												+ "<big>Version: </big><br/>" + "<small>" + mProximity.getVersion() + "</small><br/><br/>"
												+ "<big>Power: </big><br/>" + "<small>" + mProximity.getPower() + " mA</small><br/>");
					
					dialog("Proximity Information", proximity);
				} else {
					dialog("Proximity Information", Html.fromHtml("Sensor Unsupported by Device"));
				}
			}
		});
		
		final Button orientation = (Button) findViewById(R.id.orientation_button);
		
		orientation.setOnClickListener(new OnClickListener() {

			public void onClick(View button) {
				
				if (sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION) != null) {
					Spanned orientation = Html.fromHtml("<big>Name: </big><br/>" + "<small>" + mOrientation.getName()+ "</small><br/><br/>"
												+ "<big>Type: </big><br/>" + "<small>" + "Orientation" + "</small><br/><br/>"
												+ "<big>Vendor: </big><br/>" + "<small>" + mOrientation.getVendor() + "</small><br/><br/>"
												+ "<big>Version: </big><br/>" + "<small>" + mOrientation.getVersion() + "</small><br/><br/>"
												+ "<big>Power: </big><br/>" + "<small>" + mOrientation.getPower() + " mA</small><br/>");
					
					dialog("Orientation Information", orientation);
				} else {
					dialog("Orientation Information", Html.fromHtml("Sensor Unsupported by Device"));
				}
			}
		});
		//------------------------------------------------SHOW SENSOR MANUFACTURING INFO------------------------------------------------------------//		
	}
	
	public void onPause() {
		super.onPause();
		
		sensorManager.unregisterListener(this);
	}

	public void onSensorChanged(SensorEvent event) {
		
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			
			show_sensor.setText("\n\nACCELEROMETER: \n\nx-axis: " + x + " (m/s^2) \ny-axis: " + y + " (m/s^2) \nz-axis: " + z + " (m/s^2) \n");
		}
		else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
			
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			
			show_gyroscope.setText("\n\nGYROSCOPE: \n\nx-axis: " + x + " (rad/s) \ny-axis: " + y + " (rad/s) \nz-axis: " + z + " (rad/s) \n");
		}
		
		else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			
			show_magnetometer.setText("\n\nMAGNETOMETER: \n\nx-axis: " + x + " (uT) \ny-axis: " + y + " (uT) \nz-axis: " + z + " (uT) \n");
		}
		
		else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
			
			float light = event.values[0];
			
			show_light.setText("\n\nLIGHT: \n\n" + light + " (lux) \n");
		}
		
		else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
			
			float proximity = event.values[0];

			show_proximity.setText("\n\nPROXIMITY: \n\n" + proximity + " (cm) \n\n");
		}
		
		else if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
	
			float azimuth = event.values[0];
			float pitch = event.values[1];
			float roll = event.values[2];
	
			show_orientation.setText("\n\nORIENTATION: \n\nazimuth: " + azimuth + " (degrees) \npitch: " + pitch + " (degrees) \nroll: " + roll + " (degrees) \n\n");
		}
	}
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	private void dialog(String sensor, Spanned message) {
		
		AlertDialog.Builder sensor_dialog = new AlertDialog.Builder(sensor_act.this);
		
		// set title
		sensor_dialog.setTitle(sensor);

		// set dialog message
		sensor_dialog
			.setMessage(message)
			.setCancelable(false)
			.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			  });
			// create alert dialog
			AlertDialog alertDialog = sensor_dialog.create();

			// show it
			alertDialog.show();
	}
}