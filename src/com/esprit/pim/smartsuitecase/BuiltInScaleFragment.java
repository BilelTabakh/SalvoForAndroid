package com.esprit.pim.smartsuitecase;

import com.esprit.pim.smartsuitecase.R;
import com.triggertrap.seekarc.SeekArc;
import com.triggertrap.seekarc.SeekArc.OnSeekArcChangeListener;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class BuiltInScaleFragment extends Fragment implements OnClickListener {

	private SeekArc mSeekArc;
	public int i = 0;

	private TextView mSeekArcProgressText, resultOfWeightText;
	private Button changeBtnListener;

	TextView textViewtext2; 

	public BuiltInScaleFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.built_in_scale_fragment,
				container, false);
		changeBtnListener = (Button) rootView.findViewById(R.id.changeBtn);
		changeBtnListener.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		mSeekArc = (SeekArc) getActivity().findViewById(R.id.seekArc);
		mSeekArcProgressText = (TextView) getActivity().findViewById(
				R.id.seekArcProgress);
		resultOfWeightText = (TextView) getActivity().findViewById(
				R.id.resultOfWeight);

		mSeekArc.setTouchInSide(false);

		mSeekArc.setOnSeekArcChangeListener(new OnSeekArcChangeListener() {

			public void onStopTrackingTouch(SeekArc seekArc) {

			}

			public void onStartTrackingTouch(SeekArc seekArc) {

			}

			public void onProgressChanged(SeekArc seekArc, int progress,
					boolean fromUser) {
				mSeekArcProgressText.setText(String.valueOf(i) + " Kg");

			}
		});
	}

	public void onClick(View v) {
		if (v == changeBtnListener) {

			if (v == changeBtnListener) {

				double D = Math.random() * 100;
				i = (int) D;
				mSeekArc.setProgress(i);
				mSeekArc.invalidate();

				if (i > 50) {
					resultOfWeightText.setText(getResources().getString(
							R.string.goodWeight));
				} else {
					resultOfWeightText.setText(getResources().getString(
							R.string.badWeight));
				}
			}

		}

	}

}