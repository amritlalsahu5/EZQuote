package com.example.finalproject;

/*
 * Author: Alexander Pinkerton, Udeep Manchanda, Tianyi Xie
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pojo.Security;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class QuoteViewFragment extends Fragment implements OnClickListener {
	CombinedChart chart;
	String stockSymbol;
	Security stockObj;
	TextView openTv, mktCapTv, highTv, lowTv, wHighTv, wLowTv, volTv, avgVolTv,
			peTv, yieldTv, stockNameTv;
	TextView openValTv, mktCapValTv, highValTv, lowValTv, wHighValTv, wLowValTv, volValTv, avgVolValTv,
	peValTv, yieldValTv;
	TextView oneMonth, threeMonth, sixMonth, oneYear, threeYear;
	TextView favTv, notFavTv;
	ImageView favIcon, notFavIcon, alertSet;
	LinearLayout favLL, notFavLL;
	

	public QuoteViewFragment(Security stockObj) {
		this.stockObj = stockObj;
		this.stockSymbol = stockObj.getSymbol();
	}

	public void stockhistory(String stock) {

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		chart = (CombinedChart) getView().findViewById(R.id.chart);
		new getCsv().execute(stockSymbol, "oneYear");
		favIcon = (ImageView) getView().findViewById(R.id.favIcon);
		notFavIcon = (ImageView) getView().findViewById(R.id.notFavIcon);
		alertSet = (ImageView) getView().findViewById(R.id.alertset);
		favLL = (LinearLayout) getView().findViewById(R.id.favLL);
		notFavLL = (LinearLayout) getView().findViewById(R.id.notFavLL);
		favTv = (TextView) getView().findViewById(R.id.favTv);
		notFavTv = (TextView) getView().findViewById(R.id.notFavTv);
		
		ParseQuery<ParseObject> isFavQuery = ParseQuery.getQuery("Favorites");
		isFavQuery.whereEqualTo("UserName", ParseUser.getCurrentUser()
				.getUsername());
		isFavQuery.whereEqualTo("StockName", stockSymbol);

		isFavQuery.getFirstInBackground(new GetCallback<ParseObject>() {

			@Override
			public void done(ParseObject object, ParseException e) {

				if (e == null) {
					if (object != null) {
						notFavTv.setVisibility(View.GONE);
						notFavIcon.setVisibility(View.GONE);
						notFavLL.setVisibility(View.GONE);
						favLL.setVisibility(View.VISIBLE);
						favIcon.setVisibility(View.VISIBLE);
						favTv.setVisibility(View.VISIBLE);

					} else {
						favTv.setVisibility(View.GONE);
						favIcon.setVisibility(View.GONE);
						favLL.setVisibility(View.GONE);
						notFavLL.setVisibility(View.VISIBLE);
						notFavIcon.setVisibility(View.VISIBLE);
						notFavTv.setVisibility(View.VISIBLE);
					}
				} else {
					favTv.setVisibility(View.GONE);
					favIcon.setVisibility(View.GONE);
					favLL.setVisibility(View.GONE);
					notFavLL.setVisibility(View.VISIBLE);
					notFavIcon.setVisibility(View.VISIBLE);
					notFavTv.setVisibility(View.VISIBLE);
					Log.d("Error in Fav", e.getLocalizedMessage());
				}

			}
		});


		openValTv = (TextView) getView().findViewById(R.id.openValTv);
		openTv = (TextView) getView().findViewById(R.id.openTv);
		openTv.setOnClickListener(this);
		mktCapValTv = (TextView) getView().findViewById(R.id.mktCapVal);
		mktCapTv = (TextView) getView().findViewById(R.id.mktCapTv);
		mktCapTv.setOnClickListener(this);
		highValTv = (TextView) getView().findViewById(R.id.highValTv);
		highTv = (TextView) getView().findViewById(R.id.highTv);
		highTv.setOnClickListener(this);
		lowValTv = (TextView) getView().findViewById(R.id.lowValTv);
		lowTv = (TextView) getView().findViewById(R.id.lowTv);
		lowTv.setOnClickListener(this);
		wHighValTv = (TextView) getView().findViewById(R.id.wHighValTv);
		wHighTv = (TextView) getView().findViewById(R.id.wHighTv);
		wHighTv.setOnClickListener(this);
		wLowValTv = (TextView) getView().findViewById(R.id.wLowValTv);
		wLowTv = (TextView) getView().findViewById(R.id.wlowTv);
		wLowTv.setOnClickListener(this);
		volValTv = (TextView) getView().findViewById(R.id.volValTv);
		volTv = (TextView) getView().findViewById(R.id.volTv);
		volTv.setOnClickListener(this);
		avgVolValTv = (TextView) getView().findViewById(R.id.avgVolValTv);
		avgVolTv = (TextView) getView().findViewById(R.id.avgVolTv);
		avgVolTv.setOnClickListener(this);
		peValTv = (TextView) getView().findViewById(R.id.peValTv);
		peTv = (TextView) getView().findViewById(R.id.peTV);
		peTv.setOnClickListener(this);
		yieldValTv = (TextView) getView().findViewById(R.id.yieldValTv);
		yieldTv = (TextView) getView().findViewById(R.id.yieldTv);
		yieldTv.setOnClickListener(this);
		
		stockNameTv = (TextView) getView().findViewById(R.id.stockNameTv);
		stockNameTv.setOnClickListener(this);

		oneMonth = (TextView) getView().findViewById(R.id.oneMonth);
		threeMonth = (TextView) getView().findViewById(R.id.threeMonth);
		sixMonth = (TextView) getView().findViewById(R.id.sixMonth);
		oneYear = (TextView) getView().findViewById(R.id.oneYear);
		threeYear = (TextView) getView().findViewById(R.id.threeYear);


		oneMonth.setOnClickListener(this);
		threeMonth.setOnClickListener(this);
		sixMonth.setOnClickListener(this);
		oneYear.setOnClickListener(this);
		threeYear.setOnClickListener(this);

		openValTv.setText(stockObj.getAskPrice());
		mktCapValTv.setText(stockObj.getMarketCapitalization());
		highValTv.setText(stockObj.getDaysHigh());
		lowValTv.setText(stockObj.getDaysLow());
		wHighValTv.setText(stockObj.getYearHigh());
		wLowValTv.setText(stockObj.getYearLow());
		volValTv.setText(stockObj.getVolume());
		avgVolValTv.setText(stockObj.getAvgVolume());
		peValTv.setText(stockObj.getPeRatio());
		yieldValTv.setText(stockObj.getYield());
		stockNameTv.setText(stockObj.getCompanyName());

		favIcon.setOnClickListener(this);
		notFavIcon.setOnClickListener(this);
		alertSet.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		
		if(isConnected()){
		
		switch (v.getId()) {

		case R.id.alertset:
			setPriceAlertDialog();
			break;

		case R.id.favIcon:

			removeFromFavorites();

			break;

		case R.id.notFavIcon:
			saveInFavorites();
			break;

		case R.id.oneMonth:
			// saveInFavorites();
			oneMonth.setTypeface(oneMonth.getTypeface(), Typeface.BOLD);
			oneMonth.setTextColor(Color.WHITE);
			threeMonth.setTextColor(Color.GRAY);
			sixMonth.setTextColor(Color.GRAY);
			oneYear.setTextColor(Color.GRAY);
			threeYear.setTextColor(Color.GRAY);
			threeMonth.setTypeface(Typeface.SANS_SERIF);
			sixMonth.setTypeface(Typeface.SANS_SERIF);
			oneYear.setTypeface(Typeface.SANS_SERIF);
			threeYear.setTypeface(Typeface.SANS_SERIF);
			new getCsv().execute(stockSymbol, "oneMonth");
			break;
		case R.id.threeMonth:
			// saveInFavorites();
			threeMonth.setTypeface(threeMonth.getTypeface(), Typeface.BOLD);
			threeMonth.setTextColor(Color.WHITE);
			oneMonth.setTextColor(Color.GRAY);
			sixMonth.setTextColor(Color.GRAY);
			oneYear.setTextColor(Color.GRAY);
			threeYear.setTextColor(Color.GRAY);
			oneMonth.setTypeface(Typeface.SANS_SERIF);
			sixMonth.setTypeface(Typeface.SANS_SERIF);
			oneYear.setTypeface(Typeface.SANS_SERIF);
			threeYear.setTypeface(Typeface.SANS_SERIF);
			new getCsv().execute(stockSymbol, "threeMonth");
			break;
		case R.id.sixMonth:
			// saveInFavorites();
			sixMonth.setTypeface(sixMonth.getTypeface(), Typeface.BOLD);
			sixMonth.setTextColor(Color.WHITE);
			threeMonth.setTextColor(Color.GRAY);
			oneMonth.setTextColor(Color.GRAY);
			oneYear.setTextColor(Color.GRAY);
			threeYear.setTextColor(Color.GRAY);
			threeMonth.setTypeface(Typeface.SANS_SERIF);
			oneMonth.setTypeface(Typeface.SANS_SERIF);
			oneYear.setTypeface(Typeface.SANS_SERIF);
			threeYear.setTypeface(Typeface.SANS_SERIF);
			new getCsv().execute(stockSymbol, "sixMonth");
			break;
		case R.id.oneYear:
			// saveInFavorites();
			threeMonth.setTextColor(Color.GRAY);
			sixMonth.setTextColor(Color.GRAY);
			oneMonth.setTextColor(Color.GRAY);
			threeYear.setTextColor(Color.GRAY);
			oneYear.setTypeface(oneYear.getTypeface(), Typeface.BOLD);
			oneYear.setTextColor(Color.WHITE);
			threeMonth.setTypeface(Typeface.SANS_SERIF);
			sixMonth.setTypeface(Typeface.SANS_SERIF);
			oneMonth.setTypeface(Typeface.SANS_SERIF);
			threeYear.setTypeface(Typeface.SANS_SERIF);
			new getCsv().execute(stockSymbol, "oneYear");
			break;
		case R.id.threeYear:
			// saveInFavorites();
			threeMonth.setTextColor(Color.GRAY);
			sixMonth.setTextColor(Color.GRAY);
			oneYear.setTextColor(Color.GRAY);
			oneMonth.setTextColor(Color.GRAY);
			threeYear.setTypeface(threeYear.getTypeface(), Typeface.BOLD);
			threeYear.setTextColor(Color.WHITE);
			threeMonth.setTypeface(Typeface.SANS_SERIF);
			sixMonth.setTypeface(Typeface.SANS_SERIF);
			oneYear.setTypeface(Typeface.SANS_SERIF);
			oneMonth.setTypeface(Typeface.SANS_SERIF);
			new getCsv().execute(stockSymbol, "threeYear");
			break;

			
		case R.id.openTv:
			showHelpDialog("The price at which a security first trades upon the opening of an exchange on a given trading day. A security's opening price is an important marker for that day's trading activity, especially for those interested in measuring short-term results, such as day traders.");
			break;
		case R.id.openValTv:
			
		case R.id.mktCapTv:
			showHelpDialog("Market capitalization (or market cap) is the total dollar market value of the shares outstanding of a publicly traded company; it is equal to the share price times the number of shares outstanding.");
			break;
		case R.id.mktCapVal:
			
		case R.id.highValTv:
		case R.id.highTv:
			showHelpDialog("The highest and lowest prices that a stock has traded at during the previous day.");
			break;
		case R.id.lowValTv:
		case R.id.lowTv:
			showHelpDialog("The highest and lowest prices that a stock has traded at during the previous day.");
			break;
		case R.id.wHighTv:
			showHelpDialog("The highest and lowest prices that a stock has traded at during the previous year. Many traders and investors view the 52-week high or low as an important factor in determining a stock's current value and predicting future price movement.");
			break;
		case R.id.wHighValTv:
			
		case R.id.wlowTv:
			showHelpDialog("The highest and lowest prices that a stock has traded at during the previous year. Many traders and investors view the 52-week high or low as an important factor in determining a stock's current value and predicting future price movement.");
			break;
		case R.id.wLowValTv:
			
		case R.id.volValTv:
		case R.id.volTv:
			showHelpDialog("Volume is commonly reported as the number of shares that changed hands during a given day.");
			break;
		case R.id.avgVolValTv:
		case R.id.avgVolTv:
			showHelpDialog("The average volume of a security over a longer period of time is the total amount traded in that period, divided by the length of the period. Therefore, the unit of measurement for average volume is shares per unit of time, typically per day.");
			break;
		case R.id.peValTv:
		case R.id.peTV:
			showHelpDialog("The price-to-earnings ratio, or P/E ratio, is an equity valuation multiple. It is defined as market price per share divided by annual earnings per share.");
			break;
		case R.id.yieldValTv:
		case R.id.yieldTv:
			showHelpDialog("Yield is the income return on an investment. This refers to the interest or dividends received from a security and is usually expressed annually as a percentage based on the investment's cost, its current market value or its face value.");
			break;

		}
	}

	}
	
	
	private void showHelpDialog(String term){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_DARK);
		builder.setTitle("EZ INFO");
		builder.setMessage(term);

		// Set up the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		

		builder.show();
	}
	

	private void removeFromFavorites() {

		ParseQuery<ParseObject> favQuery = ParseQuery.getQuery("Favorites");
		favQuery.whereEqualTo("UserName", ParseUser.getCurrentUser()
				.getUsername());
		favQuery.whereEqualTo("StockName", stockSymbol);
		favQuery.getFirstInBackground(new GetCallback<ParseObject>() {

			@Override
			public void done(ParseObject object, ParseException e) {

				if (e == null) {
					try {
						object.delete();
						favIcon.setVisibility(View.GONE);
						favTv.setVisibility(View.GONE);
						favLL.setVisibility(View.GONE);
						notFavLL.setVisibility(View.VISIBLE);
						notFavIcon.setVisibility(View.VISIBLE);
						notFavTv.setVisibility(View.VISIBLE);
						Toast.makeText(getActivity(),
								"Successfully removed from Favorites",
								Toast.LENGTH_SHORT).show();
					} catch (ParseException e1) {

						e1.printStackTrace();
					}
				} else {
					Log.d("Error in Fav", e.getLocalizedMessage());
				}
			}
		});

	}

	private void saveInFavorites() {
		ParseObject favObj = new ParseObject("Favorites");
		favObj.put("UserName", ParseUser.getCurrentUser().getUsername());
		favObj.put("StockName", stockSymbol);

		favObj.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {

				if (e == null) {
					notFavIcon.setVisibility(View.GONE);
					notFavTv.setVisibility(View.GONE);
					notFavLL.setVisibility(View.GONE);
					favLL.setVisibility(View.VISIBLE);
					favIcon.setVisibility(View.VISIBLE);
					favTv.setVisibility(View.VISIBLE);
					Toast.makeText(getActivity(),
							"Successfully saved in Favorites",
							Toast.LENGTH_SHORT).show();
				} else {
					Log.d("Error in Fav", e.getLocalizedMessage());
				}
			}
		});

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_quoteview,
				container, false);

		return rootView;
	}

	public void setPriceAlertDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
				AlertDialog.THEME_HOLO_DARK);
		builder.setTitle("Set the alert price");

		// Set up the input
		LinearLayout LL = new LinearLayout(getActivity());
	    LL.setOrientation(LinearLayout.VERTICAL);

	    LayoutParams LLParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
	    
	    LL.setLayoutParams(LLParams);
		final EditText input = new EditText(getActivity());
		input.setInputType(InputType.TYPE_CLASS_NUMBER
				| InputType.TYPE_NUMBER_FLAG_DECIMAL
				| InputType.TYPE_NUMBER_FLAG_SIGNED);
		input.setTextColor(Color.parseColor("#33b5e5"));
		int maxLength = 9;    
		input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
		
		final Switch sw = new Switch(getActivity());
		sw.setText("Alert Type");
		sw.setTextSize(20);
		sw.setPadding(0, 10, 5, 10);
		sw.setTextOff("Loss");
		sw.setTextOn("Gain");
		sw.setGravity(Gravity.CENTER);
		
	
		sw.setTextColor(Color.parseColor("#33b5e5"));
		LL.setPadding(5, 5, 5, 5);
		LL.addView(input);
		LL.addView(sw);
		// Specify the type of input expected; this, for example, sets the input
		// as a password, and will mask the text
		// input.setInputType(InputType.TYPE_CLASS_TEXT |
		// InputType.TYPE_TEXT_VARIATION_PASSWORD);
		builder.setView(LL);
		//builder.setView(sw);
		// Set up the buttons
		builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if (input.getText().toString().trim().length() == 0) {
					Toast.makeText(getActivity(), "Input is required",
							Toast.LENGTH_SHORT).show();
					return;
				}

				ParseObject alertObj = new ParseObject("Alert");
				alertObj.put("UserName", ParseUser.getCurrentUser()
						.getUsername());
				alertObj.put("targetPrice", input.getText().toString());
				alertObj.put("oldPrice", stockObj.getAskPrice());
				alertObj.put("symbol", stockSymbol);
				if(sw.isChecked()){
					alertObj.put("StockState", "GAIN");
				}else{
					alertObj.put("StockState", "LOSS");
				}
				
				alertObj.saveInBackground(new SaveCallback() {

					@Override
					public void done(ParseException e) {

						if (e == null) {
							Toast.makeText(getActivity(), "Alert Saved.",
									Toast.LENGTH_SHORT).show();
						} else {
							Log.d("Error in Alert Setting",
									e.getLocalizedMessage());
						}
					}
				});
			}
		});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		builder.show();
	}

	public interface QuoteViewDelegate {

		public void delegateFromQuoteView();

	}

	class getCsv extends AsyncTask<String, Integer, CombinedData> {
		ProgressDialog pd;

		@Override
		protected CombinedData doInBackground(String... params) {
			// TODO Auto-generated method stub
			ArrayList<String> stocklist = new ArrayList<String>();
			ArrayList<String> stockDate = new ArrayList<String>();
			ArrayList<Float> stockVolume = new ArrayList<Float>();
			ArrayList<Float> stockClose = new ArrayList<Float>();

			ArrayList<Entry> valsPrice = new ArrayList<Entry>();
			ArrayList<BarEntry> valsVolume = new ArrayList<BarEntry>();
			LineData lineData = new LineData();
			BarData barData = new BarData();
			
			ArrayList<String> xVals = new ArrayList<String>();

			String quoteCompany = params[0];// Company Stock Code!!!!
			String timePeriod = params[1];

			Calendar cal = Calendar.getInstance();
			int presentYear = cal.get(Calendar.YEAR);
			int presentMonth = cal.get(Calendar.MONTH);
			int presentDay = cal.get(Calendar.DATE);

			int pastMonth = 0, pastYear = 0;

			if (timePeriod.equals("oneMonth")) {
				if (presentMonth == 0) {
					pastMonth = 11;
					pastYear = presentYear - 1;
				} else {
					pastMonth = presentMonth - 1;
					pastYear = presentYear;
				}
			} else if (timePeriod.equals("threeMonth")) {
				if (presentMonth >= 3) {
					pastMonth = presentMonth - 3;
					pastYear = presentYear;
				} else {
					pastMonth = presentMonth + 9;
					pastYear = presentYear - 1;
				}

			} else if (timePeriod.equals("sixMonth")) {
				if (presentMonth >= 6) {
					pastMonth = presentMonth - 6;
					pastYear = presentYear;
				} else {
					pastMonth = presentMonth + 6;
					pastYear = presentYear - 1;
				}

			} else if (timePeriod.equals("oneYear")) {
				pastYear = presentYear - 1;
				pastMonth = presentMonth;
			} else if (timePeriod.equals("threeYear")) {
				pastYear = presentYear - 3;
				pastMonth = presentMonth;
			}

			URL url = null;
			try {
				if (presentMonth == 1 && presentDay == 29) {
					url = new URL(
							"http://ichart.finance.yahoo.com/table.csv?s="
									+ quoteCompany + "&d=1&e=29&f="
									+ presentYear + "&g=d&a=1&b=28&c="
									+ (presentYear - 1) + "&ignore.csv");
				} else {
					url = new URL(
							"http://ichart.finance.yahoo.com/table.csv?s="
									+ quoteCompany + "&d=" + presentMonth
									+ "&e=" + presentDay + "&f=" + presentYear
									+ "&g=d&a=" + pastMonth + "&b="
									+ presentDay + "&c=" + pastYear
									+ "&ignore.csv");
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				stocklist = getText(url);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}




			for (int i = 1; i < stocklist.size(); i++) {
				String[] stock = stocklist.get(i).split(",");
				stockDate.add(stock[0]);
				stockVolume.add(Float.parseFloat(stock[5]));
				stockClose.add(Float.parseFloat(stock[6]));
			}

			Collections.reverse(stockDate);
			Collections.reverse(stockVolume);
			Collections.reverse(stockClose);


	        YAxis rightAxis = chart.getAxisRight();
			
			if(Collections.max(stockVolume) > 1500000){
				for(int i = 0; i < stockVolume.size();i++){
					stockVolume.set(i, stockVolume.get(i)/1000000);
				}
				rightAxis.setValueFormatter(new FormatterM());
			}else if(Collections.max(stockVolume) > 1500){
				for(int i = 0; i < stockVolume.size();i++){
					stockVolume.set(i, stockVolume.get(i)/1000);
				}
				rightAxis.setValueFormatter(new FormatterK());
			}
			
			
			for (int i = 0; i < stockDate.size(); i++) {
				Entry temp = new Entry(stockClose.get(i), i);
				valsPrice.add(temp);
				BarEntry temp2 = new BarEntry(stockVolume.get(i),i);
				valsVolume.add(temp2);
			}
			
			
	        chart.setDrawOrder(new DrawOrder[] {
	                DrawOrder.BAR, DrawOrder.LINE
	        });

	        rightAxis.setDrawGridLines(false);
	        rightAxis.setAxisMaxValue((float) (Collections.max(stockVolume)*2));
	        

	        YAxis leftAxis = chart.getAxisLeft();
	        leftAxis.setAxisMinValue((float) (Collections.min(stockClose)*0.8));
	        leftAxis.setDrawGridLines(false);

	        XAxis xAxis = chart.getXAxis();
	        xAxis.setPosition(XAxisPosition.BOTH_SIDED);
			
	        CombinedData data = new CombinedData(stockDate);

	        LineDataSet lineSet = new LineDataSet(valsPrice, "Price");
	        lineSet.setValueTextSize(0);
	        lineSet.setCircleSize(1f);
	        lineSet.setColor(Color.WHITE);
	        lineSet.setCircleColor(Color.WHITE);
	        lineSet.setAxisDependency(YAxis.AxisDependency.LEFT);
	        lineData.addDataSet(lineSet);
	        
	        BarDataSet barSet = new BarDataSet(valsVolume, "Volume");
	        
	        barSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
	        barSet.setValueTextSize(0);
	        barData.addDataSet(barSet);
	        

	        
	        data.setData(lineData);
	        data.setData(barData);
			

			

			return data;
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			pd = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_DARK);
			pd.setTitle("Loading Chart");
			pd.setMessage("Loading chart data...");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected void onPostExecute(CombinedData result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			chart.getXAxis().setTextColor(Color.WHITE);
			chart.getXAxis().setPosition(XAxisPosition.TOP);
			chart.getAxisLeft().setTextColor(Color.WHITE);
			chart.getAxisRight().setTextColor(Color.WHITE);
			chart.getLegend().setPosition(LegendPosition.BELOW_CHART_CENTER);
			chart.getLegend().setXEntrySpace(50f);
			chart.getLegend().setTextColor(Color.WHITE);
			chart.getAxisLeft().setStartAtZero(false);
			chart.getAxisRight().setStartAtZero(true);
			chart.setDescription("");
			chart.setGridBackgroundColor(Color.DKGRAY);
			chart.setBackgroundColor(Color.BLACK);
			chart.setData(result);
			chart.invalidate();
			pd.dismiss();

		}

	}

	public static ArrayList<String> getText(URL website) throws Exception {
		URLConnection connection = website.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));

		ArrayList<String> response = new ArrayList<String>();
		String inputLine;

		while ((inputLine = in.readLine()) != null)
			response.add(inputLine);

		in.close();
		return response;
	}

	public class FormatterK implements ValueFormatter {

	    private DecimalFormat mFormat;

	    public FormatterK() {
	        mFormat = new DecimalFormat("###,###,###"); // use one decimal
	    }

	    @Override
	    public String getFormattedValue(float value) {
	        return mFormat.format(value) + "k"; // append a dollar-sign
	    }
	}
	
	public class FormatterM implements ValueFormatter {

	    private DecimalFormat mFormat;

	    public FormatterM() {
	        mFormat = new DecimalFormat("###,###,###.0"); // use one decimal
	    }

	    @Override
	    public String getFormattedValue(float value) {
	        return mFormat.format(value) + "m"; // append a dollar-sign
	    }
	}
	
	public boolean isConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			// Toast.makeText(MainActivity.this, "Internet is connected",
			// Toast.LENGTH_SHORT).show();
			return true;
		} else {
			Toast.makeText(getActivity(), "No Internet Connection",
					Toast.LENGTH_SHORT).show();
			return false;
		}
	}
	
}
