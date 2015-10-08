package com.esprit.pim.smartsuitecase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class GridviewAdapter extends BaseAdapter {

	private Context context;
	private String[] itemsImages;
	private String[] itemsDates;

	public GridviewAdapter(Context context, String[] items,String[] items2) {
		super();
		this.context = context;
		this.itemsImages = items;
		this.itemsDates = items2;
	}

	public int getCount() {
		return itemsImages.length;
	}

	public Object getItem(int position) {
		return itemsImages[position];
	}

	public long getItemId(int position) {
		return position;
	}

	
	 public View getView(int position, View convertView, ViewGroup parent) {
	  LayoutInflater inflater = (LayoutInflater) context
	    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	   View gridView;
	   if (convertView == null) {
	    
		gridView = new View(context);
	    gridView = inflater.inflate(R.layout.gridlayout, null);
	     
	    TextView textView = (TextView) gridView.findViewById(R.id.textThieve);
	    textView.setText(itemsDates[position]);
	    
	    ImageView imageView = (ImageView) gridView.findViewById(R.id.imageThieve);
	    imageView.setPadding(5, 5, 5, 5);
	    Picasso.with(context).load(itemsImages[position])
	     		.placeholder(R.drawable.img_antitheft)
	     		.error(R.drawable.img_antitheft)
	     		.resize(200, 300)
	     		.into(imageView);
	     
	   } else {
	    gridView = (View) convertView;
	   }
	   return gridView;
	 }


}
