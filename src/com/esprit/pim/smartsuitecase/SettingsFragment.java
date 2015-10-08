package com.esprit.pim.smartsuitecase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsFragment extends Fragment implements OnClickListener{
	
	private Button but0;
	private Button but1; 
	private Button but2;
	private Button but3;
	private Button but4;
	private Button but5;
	private Button but6;
	private Button but7;
	private Button but8;
	private Button but9;
	private Button butA;
	private Button butB;
	private Button butC;
	private Button butD;
	private Button butStar;
	private Button butDiese; 
	
	private String result ="";
	private TextView resultTextView;
	
	private HttpResponse responsePost;
	public setPassword asynchPassword;

	public SettingsFragment() {
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.settings_fragment, container,
				false);
		
		but0 = (Button)rootView.findViewById(R.id.but0);
		but1 = (Button)rootView.findViewById(R.id.but1);
		but2 = (Button)rootView.findViewById(R.id.but2);
		but3 = (Button)rootView.findViewById(R.id.but3);
		but4 = (Button)rootView.findViewById(R.id.but4);
		but5 = (Button)rootView.findViewById(R.id.but5);
		but6 = (Button)rootView.findViewById(R.id.but6);
		but7 = (Button)rootView.findViewById(R.id.but7);
		but8 = (Button)rootView.findViewById(R.id.but8);
		but9 = (Button)rootView.findViewById(R.id.but9);
		butA = (Button)rootView.findViewById(R.id.butA);
		butB = (Button)rootView.findViewById(R.id.butB);
		butC = (Button)rootView.findViewById(R.id.butC);
		butD = (Button)rootView.findViewById(R.id.butD);
		butStar = (Button)rootView.findViewById(R.id.butStar);
		butDiese = (Button)rootView.findViewById(R.id.butDies);
		resultTextView = (TextView)rootView.findViewById(R.id.passwordText);
		
		but0.setOnClickListener(this);
		but1.setOnClickListener(this);
		but2.setOnClickListener(this);
		but3.setOnClickListener(this);
		but4.setOnClickListener(this);
		but5.setOnClickListener(this);
		but6.setOnClickListener(this);
		but7.setOnClickListener(this);
		but8.setOnClickListener(this);
		but9.setOnClickListener(this);
		butA.setOnClickListener(this);
		butB.setOnClickListener(this);
		butC.setOnClickListener(this);
		butD.setOnClickListener(this);
		butStar.setOnClickListener(this);
		butDiese.setOnClickListener(this);
		
		return rootView;

	}


	public class setPassword extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {

			HttpClient client = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://192.168.0.1:8080/security/password");
			try {
				httppost.setHeader(HTTP.CONTENT_TYPE,
						"application/x-www-form-urlencoded;charset=UTF-8");
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("password", result));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				// HttpResponse
				responsePost = client.execute(httppost);

				return EntityUtils.toString(responsePost.getEntity());

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String resulttmp) {
			// TODO Auto-generated method stub
			super.onPostExecute(resulttmp);
			
			Toast.makeText(getActivity(), "Password Changed!",
					Toast.LENGTH_LONG).show();
			result = "";
			resultTextView.setText(result);
		}

	}

	public void onClick(View v) {
		if(result.length()<8){
			if(v == but0){
				result += "0";
			}else if(v == but1){
				result += "1";
			}else if(v == but2){
				result += "2";
			}else if(v == but3){
				result += "3";
			}else if(v == but4){
				result += "4";
			}else if(v == but5){
				result += "5";
			}else if(v == but6){
				result += "6";
			}else if(v == but7){
				result += "7";
			}else if(v == but8){
				result += "8";
			}else if(v == but9){
				result += "9";
			}else if(v == butA){
				result += "A";
			}else if(v == butB){
				result += "B";
			}else if(v == butC){
				result += "C";
			}else if(v == butD){
				result += "D";
			}
			}
			if(v == butStar){
				result = "";
			}else if(v == butDiese){
				if((result.length()>=3) &&(result.length()<=8)){
					asynchPassword = new setPassword();
					asynchPassword.execute(result);
				}else{
					Toast.makeText(getActivity(), "Password must be between 3 and 8 characters", Toast.LENGTH_SHORT).show();
				}
			}
			
			resultTextView.setText(result);
		
	}



}
