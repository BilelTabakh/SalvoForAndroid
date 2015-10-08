package com.esprit.pim.smartsuitecase;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.AlertDialog;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

public class AntiTheftFragment extends Fragment {

	public getPictures asynchGetPictures;
	
	private GridView gridView;
	private GridviewAdapter gridAdapter;
	public String[] itemsImages;	
	public String[] itemsDates;	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.antitheft_fragment,
				container, false);

		gridView = (GridView) rootView.findViewById(R.id.gridView);
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		asynchGetPictures = new getPictures();
		asynchGetPictures.execute();
	}

	public class getPictures extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			String str = "";
			HttpResponse response;
			HttpClient myClient = new DefaultHttpClient();
			HttpGet myConnection = new HttpGet("http://192.168.0.1:8080/security/images");

			try {
				response = myClient.execute(myConnection);
				str = EntityUtils.toString(response.getEntity(), "UTF-8");
				return str;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				JSONArray jPicturesThieves = new JSONArray(result);
				itemsImages = new String[jPicturesThieves.length()];
				itemsDates = new String[jPicturesThieves.length()];
				for (int i = 0; i <= jPicturesThieves.length(); i++) {
					itemsImages[i] = "http://192.168.0.1/security/images/"+jPicturesThieves.get(i);
					itemsDates[i] = jPicturesThieves.get(i).toString().split("\\-----")[0].substring(3).replaceAll("-", "/")+" "+jPicturesThieves.get(i).toString().split("\\-----")[1].substring(0,8).replaceAll("-", ":");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			gridAdapter = new GridviewAdapter(getActivity(),itemsImages,itemsDates);
			gridView.setAdapter(gridAdapter);
			
			
			gridView.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					new asychImg ().execute(itemsImages[position]);
				}
			});
		
			
			
			
			
		}
		

	}
	public Bitmap getBitmapFromURL(String src) {
	    try {
	        java.net.URL url = new java.net.URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url
	                .openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        return myBitmap;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public class asychImg extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			Bitmap img =  getBitmapFromURL(params[0]);
			return img;
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			

			AlertDialog.Builder alertImg = new AlertDialog.Builder(getActivity());
		    ImageView imgViewTheft = new ImageView(getActivity()); 
		    imgViewTheft.setImageBitmap(result);
			// on attribue un titre à notre boite de dialogue
		    alertImg.setTitle("");
			alertImg.setView(imgViewTheft);

			// on affiche la boite de dialogue
			alertImg.show();
		}
	}
}
