package com.esprit.pim.smartsuitecase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LockUnlockFragment extends Fragment implements OnClickListener {

	private TextView textResultLock;
	private ImageButton imageBtnLockUnlock;
	private boolean isOpen = true;
	public String state = "";
	public getLockState asynch;
	public changeStateUnlock asynchUnlock;
	public changeStateLock asynchlock;

	private HttpResponse responsePost;
	private ProgressBar spinner;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		asynch = new getLockState();
		asynch.execute();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.lock_unlock_fragment,
				container, false);

		imageBtnLockUnlock = (ImageButton) rootView
				.findViewById(R.id.btnLockUnlock);
		imageBtnLockUnlock.setOnClickListener(this);
		textResultLock = (TextView) rootView.findViewById(R.id.resultLock);

		spinner = (ProgressBar) rootView.findViewById(R.id.progressBar1);
		
		
		
		
		
		return rootView;
	}

	public void onClick(View v) {

		if (v == imageBtnLockUnlock && isOpen) {
			asynchlock = new changeStateLock();
			asynchlock.execute();
			textResultLock.setText(getResources().getString(R.string.locked));
			imageBtnLockUnlock.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_btn_lock));
			isOpen = false;
		} else if (v == imageBtnLockUnlock && isOpen == false) {
			asynchUnlock = new changeStateUnlock();
			asynchUnlock.execute();
			textResultLock.setText(getResources().getString(R.string.unlocked));
			imageBtnLockUnlock.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_btn_unlock));
			isOpen = true;
		}

	}

	public void getStateSuitcase() {

	}

	public class getLockState extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			try {
				HttpClient client = new DefaultHttpClient();
				String getURL = "http://192.168.0.1:8080/state/locked";
				HttpGet get = new HttpGet(getURL);
				HttpResponse responseGet = client.execute(get);
				HttpEntity resEntityGet = responseGet.getEntity();
				if (resEntityGet != null) {
					// do something with the response
					state = EntityUtils.toString(resEntityGet);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return state;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			spinner.setVisibility(View.INVISIBLE);
			Log.i("GET RESPONSE", state);
			if (state.trim().equals("{1}")) 
			{
				textResultLock.setText(getResources().getString(R.string.locked));
				imageBtnLockUnlock.setImageDrawable(getResources().getDrawable(
						R.drawable.ic_btn_lock));
				isOpen = false;
			}
			else if (state.trim().equals("{0}"))
			{
				textResultLock.setText(getResources().getString(R.string.unlocked));
				imageBtnLockUnlock.setImageDrawable(getResources().getDrawable(
						R.drawable.ic_btn_unlock));
				isOpen = true;
			}
			imageBtnLockUnlock.setVisibility(View.VISIBLE);
			Toast.makeText(getActivity(), (state.trim() == "{1}") + "", Toast.LENGTH_LONG)
					.show();
		}

	}

	public class changeStateUnlock extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {

			HttpClient client = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://192.168.0.1:8080/state/locked");
			try {
				httppost.setHeader(HTTP.CONTENT_TYPE,
						"application/x-www-form-urlencoded;charset=UTF-8");
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("locked", "0"));
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
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			Toast.makeText(getActivity(), "Unlock :" + result,
					Toast.LENGTH_LONG).show();
			
			
			
		}

	}

	public class changeStateLock extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {

			HttpClient client = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://192.168.0.1:8080/state/locked");
			try {
				httppost.setHeader(HTTP.CONTENT_TYPE,
						"application/x-www-form-urlencoded;charset=UTF-8");
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("locked", "1"));
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
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(getActivity(), "Unlock :" + result,
					Toast.LENGTH_LONG).show();
		}

	}

}
