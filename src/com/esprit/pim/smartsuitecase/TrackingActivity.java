package com.esprit.pim.smartsuitecase;

import com.esprit.pim.smartsuitecase.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.telephony.SmsManager;
import android.text.format.Time;
import android.view.View.OnClickListener;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;



public class TrackingActivity extends Activity implements LocationListener,
		OnClickListener {

	ImageButton buttonSendSMS;
	ImageButton buttonGetLocationSmartSuitecase;
	TextView receivedSMSTextView;
	// Informations SMS
	private String msg = System.currentTimeMillis() + " ca va ? " + Time.getCurrentTimezone();
	private String nbrPhone = "+21652442099";
	//private String nbrPhone = "+21694805416";

	// Informations Maps
	private LocationManager locationManager;
	private GoogleMap gMap;
	private Marker marker;
	private Marker markerSuitecase;
	private double oldLatitude = 0;
	private double oldLongitude = 0;
	private Location location;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tracking_actitivity);

		buttonSendSMS = (ImageButton) findViewById(R.id.btnSendSMS);
		buttonSendSMS.setOnClickListener(this);

		buttonGetLocationSmartSuitecase = (ImageButton) findViewById(R.id.btnGetLocationOfSmartSuitecase);
		buttonGetLocationSmartSuitecase.setOnClickListener(this);
		
		receivedSMSTextView = (TextView) findViewById(R.id.receivedSMS);

		// Map
		gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		try {
			//marker = gMap.addMarker(new MarkerOptions().title("You are here").position(new LatLng(0, 0)));

			gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			gMap.getUiSettings().setCompassEnabled(true);
			gMap.getUiSettings().setMyLocationButtonEnabled(true);
			gMap.getUiSettings().setRotateGesturesEnabled(true);
		} catch (Exception e) {
			// Update Google Maps
			Toast.makeText(this, "Update Google Maps ", Toast.LENGTH_LONG)
					.show();
		}
		
		//Call method receiveSMSMessage
		 receiveSMSMessage();
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onResume() {
		super.onResume();
		try {
			// Obtention de la référence du service
			locationManager = (LocationManager) this
					.getSystemService(LOCATION_SERVICE);
			if (!locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				buildAlertMessageNoGps();
			}

			Criteria criteria = new Criteria();
			String provider = locationManager.getBestProvider(criteria, true);
			location = locationManager.getLastKnownLocation(provider);

			if (location != null) {
				LatLng latLng1 = new LatLng(location.getLatitude(),
						location.getLongitude());

				//marker.setPosition(latLng1);
				gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 15));
			} else {
				//gMap.addMarker(new MarkerOptions().title("Nouveau").position(new LatLng(0, 0)));
			}
			// Si le GPS est disponible, on s'y abonne
			if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				abonnementGPS();
			}
		} catch (Exception e) {
			Toast.makeText(this, "Update Google Map ", 2000).show();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onPause() {
		super.onPause();

		// On appelle la méthode pour se désabonner
		desabonnementGPS();
	}

	/**
	 * Méthode permettant de s'abonner à la localisation par GPS.
	 */
	public void abonnementGPS() {
		// On s'abonne
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000, 3, this);

	}

	/**
	 * Méthode permettant de se désabonner de la localisation par GPS.
	 */
	public void desabonnementGPS() {
		// Si le GPS est disponible, on s'y abonne
		locationManager.removeUpdates(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public void onProviderDisabled(final String provider) {
		// Si le GPS est désactivé on se désabonne
		if ("gps".equals(provider)) {
			desabonnementGPS();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void onProviderEnabled(final String provider) {
		// Si le GPS est activé on s'abonne
		if ("gps".equals(provider)) {
			abonnementGPS();
		}
	}

	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	private void buildAlertMessageNoGps() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Your GPS seems to be disabled, do you want to enable it?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(
									@SuppressWarnings("unused") final DialogInterface dialog,
									@SuppressWarnings("unused") final int id) {
								startActivity(new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							@SuppressWarnings("unused") final int id) {
						dialog.cancel();
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		getActionBar().setHomeButtonEnabled(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent parentIntent = new Intent(this, MainActivity.class);
			startActivity(parentIntent);
			return true;
			}
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View v) {
		if (v == buttonSendSMS) {
			sendSMSMessage(msg, nbrPhone);
		}
		if (v == buttonGetLocationSmartSuitecase) {
			
			/*String filename = "{37.813:40.962}"; 
			String latitude = filename.split("\\:")[0].substring(1);
			String befLongitude = filename.split("\\:")[1];
			String longitude = befLongitude.substring(0, befLongitude.length()-1);
			Toast.makeText(this, latitude + ":"+longitude, Toast.LENGTH_LONG).show();
			
			LatLng latLngSuitecase = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
			markerSuitecase.setPosition(latLngSuitecase);
			gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngSuitecase,15));
			gMap.animateCamera(CameraUpdateFactory.zoomIn());
			//Toast.makeText(getApplicationContext(), "YES", Toast.LENGTH_LONG).show();*/
		}

	}

	protected void sendSMSMessage(String msg, String nbrPhone) {

		try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(nbrPhone, null, msg, null, null);
			Toast.makeText(getApplicationContext(), "SMS sent.",
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"SMS faild, please try again.", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}
	
	protected void receiveSMSMessage () {
		try {
			Intent intent= getIntent();
	        String strMessage0 = intent.getStringExtra("msg");
	        
			receivedSMSTextView.setText(strMessage0);
			
			//String strMessage = "{37.813:40.962}"; 
			
			String strlatitude = strMessage0.split("\\:")[0].substring(1);
			String befLongitude = strMessage0.split("\\:")[1];
			String strLongitude = befLongitude.substring(0, befLongitude.length()-1);
			double latitude = Double.parseDouble(strlatitude);
			double longitude = Double.parseDouble(strLongitude);
			Toast.makeText(this, latitude + ":"+longitude, Toast.LENGTH_LONG).show();
			
			markerSuitecase = gMap.addMarker(new MarkerOptions().title(
					"Smart Suitecase").position(new LatLng(latitude, longitude)));
			
			LatLng latLngSuitecase = new LatLng(latitude,longitude);
			LatLng zoomll = new LatLng(longitude, latitude);
			markerSuitecase.setPosition(latLngSuitecase);
			gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngSuitecase,15));
			//gMap.animateCamera(CameraUpdateFactory.zoomIn());
			//Toast.makeText(getApplicationContext(), "YES", Toast.LENGTH_LONG).show();
	
		} catch (Exception e) {
			//Toast.makeText(this, "You have problem with your suitecase", Toast.LENGTH_LONG).show();
		}
		
		
	}

	

}
