package com.esprit.pim.smartsuitecase;

import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.db.chart.Tools;
import com.db.chart.listener.OnEntryClickListener;
import com.db.chart.model.LineSet;
import com.db.chart.model.Point;
import com.db.chart.view.LineChartView;
import com.db.chart.view.XController;
import com.db.chart.view.YController;
import com.esprit.pim.smartsuitecase.R;

public class StatsFragment extends Fragment {

	private String[] mLabelsLineChart;
	private int[] mValuesLineChart;

	private final TimeInterpolator enterInterpolator = new DecelerateInterpolator(
			1.5f);
	private final TimeInterpolator exitInterpolator = new AccelerateInterpolator();
	// LineChart Statistique
	private static LineChartView mLineChart;
	private TextView mLineTooltip;
	private static int mCurrLineEntriesSize;
	private static int mCurrLineSetSize;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);

		View rootView = inflater.inflate(R.layout.stats_fragment, container,
				false);

		mLineChart = (LineChartView) rootView.findViewById(R.id.linechart);

		initLineChart();

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setHasOptionsMenu(true);

		super.onActivityCreated(savedInstanceState);

		mValuesLineChart = new int[4];
		mValuesLineChart[0] = 14;
		mValuesLineChart[1] = 22;
		mValuesLineChart[2] = 18;
		mValuesLineChart[3] = 35;

		mLabelsLineChart = new String[4];
		mLabelsLineChart[0] = "JAN";
		mLabelsLineChart[1] = "FEB";
		mLabelsLineChart[2] = "MAR";
		mLabelsLineChart[3] = "APR";

		updateLineChart(1, mCurrLineEntriesSize = 4);

	}

	/*------------------------------------*
	 *              LINECHART             *
	 *------------------------------------*/

	private void initLineChart() {

		final OnEntryClickListener lineEntryListener = new OnEntryClickListener() {
			public void onClick(int setIndex, int entryIndex, Rect rect) {
				if (mLineTooltip == null)
					showLineTooltip(entryIndex, rect);
				else
					dismissLineTooltip(entryIndex, rect);
			}
		};

		final OnClickListener lineClickListener = new OnClickListener() {

			public void onClick(View v) {
				if (mLineTooltip != null)
					dismissLineTooltip(-1, null);
			}
		};

		// mLineChart.setStep(1);
		mLineChart.setOnEntryClickListener(lineEntryListener);
		mLineChart.setOnClickListener(lineClickListener);

	}

	public void updateLineChart(int nSets, int nPoints) {

		LineSet data;
		mLineChart.reset();
		try {
			for (int i = 0; i < nSets; i++) {
				data = new LineSet();
				// insertion des points dans le diagramme
				for (int j = 0; j < mValuesLineChart.length; j++) {

					data.addPoint(new Point(mLabelsLineChart[j],
							mValuesLineChart[j]));
				}

				// paramètres des points et des lignes
				data.setDots(true)
						.setDotsColor(
								getResources().getColor(R.color.dotsLineChart))
						.setDotsRadius(10)
						// taille du rayon
						.setLineThickness(8)
						// taille de la ligne
						.setLineColor(
								getResources().getColor(R.color.traitLineChart))
						// couleur de la ligne
						.setDashed(false)
						// pointillée ou non
						.setSmooth(false)
						// lisse ou non
						.setFill(Color.parseColor("#3388c6c3"))
						.setDotsStrokeThickness(6)// epaisseur contour des
													// points
						.setDotsStrokeColor(
								getResources().getColor(
										R.color.dotsStrokeLineChart));// couleur
																		// contour
																		// des
																		// points

				mLineChart.addData(data);
			}

			mLineChart.setGrid(DataRetriever.randPaint())
					.setVerticalGrid(DataRetriever.randPaint())
					.setHorizontalGrid(DataRetriever.randPaint())
					// .setThresholdLine(2, randPaint())
					.setYLabels(YController.LabelPosition.NONE)
					// labels des coordonnées de y
					.setYAxis(true)
					// ajouter l'axe des ordonnées
					.setXLabels(XController.LabelPosition.OUTSIDE)
					// labels des coordonnées de x
					.setXAxis(true)
					// ajouter l'axe des abcisses
					.setMaxAxisValue(40, 4)
					.animate(DataRetriever.randAnimation(nPoints));
		} catch (Exception e) {
			Toast.makeText(getActivity(), "vide 2 !!!", Toast.LENGTH_LONG)
					.show();
		}
		// .show();
	}

	@SuppressLint("NewApi")
	private void showLineTooltip(int index, Rect rect) {

		mLineTooltip = (TextView) getActivity().getLayoutInflater().inflate(
				R.layout.circular_tooltip, null);
		mLineTooltip.setText(mValuesLineChart[index] + "");

		LayoutParams layoutParams = new LayoutParams(
				(int) Tools.fromDpToPx(40), (int) Tools.fromDpToPx(40));
		layoutParams.leftMargin = rect.centerX() - layoutParams.width / 2;
		layoutParams.topMargin = rect.centerY() - layoutParams.height / 2;
		mLineTooltip.setLayoutParams(layoutParams);

		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			mLineTooltip.setPivotX(layoutParams.width / 2);
			mLineTooltip.setPivotY(layoutParams.height / 2);
			mLineTooltip.setAlpha(0);
			mLineTooltip.setScaleX(0);
			mLineTooltip.setScaleY(0);
			mLineTooltip.animate().setDuration(150).alpha(1).scaleX(1)
					.scaleY(1).rotation(360).setInterpolator(enterInterpolator);
		}

		mLineChart.showTooltip(mLineTooltip);
	}

	@SuppressLint("NewApi")
	private void dismissLineTooltip(final int index, final Rect rect) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
			mLineTooltip.animate().setDuration(100).scaleX(0).scaleY(0)
					.alpha(0).rotation(-360)

					.setInterpolator(exitInterpolator)
					.withEndAction(new Runnable() {

						public void run() {
							mLineChart.removeView(mLineTooltip);
							mLineTooltip = null;
							if (index != -1)
								showLineTooltip(index, rect);
						}
					});

		} else {
			mLineChart.dismissTooltip(mLineTooltip);
			mLineTooltip = null;
			if (index != -1)
				showLineTooltip(index, rect);
		}
	}

}
