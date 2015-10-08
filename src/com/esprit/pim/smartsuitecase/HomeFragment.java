package com.esprit.pim.smartsuitecase;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.esprit.pim.smartsuitecase.R;
import com.getbase.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

	private Fragment fragment;

	public HomeFragment() {
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.home_fragment, container,
				false);

		// final View actionB = rootView.findViewById(R.id.action_b);
		// final View actionA = rootView.findViewById(R.id.action_a);

		ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
		drawable.getPaint().setColor(getResources().getColor(R.color.white));

		final FloatingActionButton actionA = (FloatingActionButton) rootView
				.findViewById(R.id.action_a);
		final FloatingActionButton actionB = (FloatingActionButton) rootView
				.findViewById(R.id.action_b);
		actionA.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				fragment = new FoundSuitecaseFragment();
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, fragment).commit();
			}
		});

		actionB.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				fragment = new LockUnlockFragment();
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, fragment).commit();
			}
		});

		return rootView;
	}

}
