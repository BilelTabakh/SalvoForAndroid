package com.esprit.pim.smartsuitecase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.net.wifi.ScanResult;

import com.esprit.pim.smartsuitecase.R;
import com.skyfishjy.library.RippleBackground;

public class FoundSuitecaseFragment extends Fragment {

	//Wifi Info
	TextView mainText;
    WifiManager mainWifi;
    WifiReceiver receiverWifi;
    List<ScanResult> wifiList;
    StringBuilder sb = new StringBuilder();

    String ITEM_KEY = "key";
    ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
    SimpleAdapter adapter;
	
	//Animation Found
	private ImageView foundDevice;
	private boolean isFound=false;
	private static int cc=-1;
	private boolean testWifiToShowMsg = false;
	
	public FoundSuitecaseFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.found_suitecase_fragment,
				container, false);
		final RippleBackground rippleBackground = (RippleBackground) rootView
				.findViewById(R.id.contentf);
		final Handler handler = new Handler();
		
		//Detect Wifi
	    // Initiate wifi service manager
	       mainWifi = (WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);
		
	    // Check for wifi is disabled
	       if (mainWifi.isWifiEnabled() == false)
	            {   
	                // If wifi disabled then enable it
	                Toast.makeText(getActivity().getApplicationContext(), "wifi is disabled..making it enabled", 
	                Toast.LENGTH_LONG).show();
	                //Enable Wifi
	                mainWifi.setWifiEnabled(true);
	                testWifiToShowMsg = true;
	            } 
	    // wifi scaned value broadcast receiver 
	       receiverWifi = new WifiReceiver();
	   
	    // Register broadcast receiver 
	    // Broacast receiver will automatically call when number of wifi connections changed
	       getActivity().registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
	       //mainWifi.startScan();
	       //mainText.setText("Starting Scan...");
	       //Toast.makeText(getActivity(), "Starting Scan 2...", 2500).show();
	       
	       
	    //ImageView Suitecase
		foundDevice = (ImageView) rootView.findViewById(R.id.foundDevice);
		foundDevice.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				//Toast.makeText(getActivity(), "Hello Suite Case", 2000).show();
				
				final AlertDialog.Builder alertPassword = new AlertDialog.Builder(getActivity());
				final EditText passwordTextField = new EditText(getActivity());
				alertPassword.setView(passwordTextField);
				alertPassword.setTitle("Please enter the correct password");
				alertPassword.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int whichButton) {
			           
			        	if(passwordTextField.getText().toString().equals("abc12345678")){
			        		//Connect To Wifi Of The Suitecase
							// WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
						        // setup a wifi configuration
						        WifiConfiguration wc = new WifiConfiguration();
						        wc.SSID = "\"topnetDD1C\"";
						        wc.preSharedKey = "\"abc12345678\"";
						        wc.status = WifiConfiguration.Status.ENABLED;
						        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
						        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
						        wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
						        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
						        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
						        wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
						        // connect to and enable the connection
						        int netId = mainWifi.addNetwork(wc);
						        mainWifi.enableNetwork(netId, true);
						        mainWifi.setWifiEnabled(true);
							 Toast.makeText(getActivity(), "connected", 1500).show();
							 
							HomeFragment hmFrg = new HomeFragment();
							 FragmentManager fragmentManager = getFragmentManager();
								fragmentManager.beginTransaction()
										.replace(R.id.frame_container, hmFrg).commit();
							 
			        	}else{
			        		
			        		//alertPassword.show();
			        		Toast.makeText(getActivity(), "The password is wrong..try again..", 1500).show();
			        	}
			        	
			        }
			    });
				alertPassword.show();
				

			}
		});

		ImageView button = (ImageView) rootView.findViewById(R.id.centerImage);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				rippleBackground.startRippleAnimation();
				handler.postDelayed(new Runnable() {
					public void run() {
						foundDevice();
					}
				}, 3000);
				
				//Starting Scan
				mainWifi.startScan();
				Toast.makeText(getActivity(), "Starting Scan ...", Toast.LENGTH_SHORT).show();
		        
			}
		});
		
           
		

		
		return rootView;
	}

	private void foundDevice() {
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setDuration(400);
		animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
		ArrayList<Animator> animatorList = new ArrayList<Animator>();
		ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(foundDevice,
				"ScaleX", 0f, 1.2f, 1f);
		animatorList.add(scaleXAnimator);
		ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(foundDevice,
				"ScaleY", 0f, 1.2f, 1f);
		animatorList.add(scaleYAnimator);
		animatorSet.playTogether(animatorList);
		if(isFound){
		foundDevice.setVisibility(View.VISIBLE);
		}
		animatorSet.start();
	}
	
	   // Broadcast receiver class called its receive method 
    // when number of wifi connections changed
     
    class WifiReceiver extends BroadcastReceiver {
         
        // This method call when number of wifi connections changed
		@Override
		public void onReceive(Context context, Intent intent) {
            sb = new StringBuilder();
            wifiList = mainWifi.getScanResults(); 
            sb.append("\n        Number Of Wifi connections :"+wifiList.size()+"\n\n");
             
            for(int i = 0; i < wifiList.size(); i++){
                 
               /* sb.append(new Integer(i+1).toString() + ". ");
                sb.append((wifiList.get(i)).toString());
                sb.append("\n\n");   
           */
                sb.append("Name: "+wifiList.get(i).SSID);
                sb.append("Adress: "+wifiList.get(i).BSSID);
               
                if(wifiList.get(i).SSID.equals("MySmartSuitcase")){
                	cc++;
                	isFound = true;
                	if((cc == 1) && (testWifiToShowMsg == true)){
                		Toast.makeText(getActivity(), "SmartSuitecase is found..", 2500).show();
                	}else if((cc == 0) && (testWifiToShowMsg == false)){
                		Toast.makeText(getActivity(), "SmartSuitecase is found..", 2500).show();
                	}
                }
                
            }
            
      
            //mainText.setText(sb);  
            //Toast.makeText(getActivity(), "Wifi : "+sb, Toast.LENGTH_LONG).show();
			
		}
         
    }
}
