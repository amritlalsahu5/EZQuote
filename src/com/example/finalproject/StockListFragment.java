package com.example.finalproject;

/*
 * Author: Alexander Pinkerton, Udeep Manchanda, Tianyi Xie
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.example.adapter.StockListAdapter;
import com.example.pojo.Security;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class StockListFragment extends Fragment{

	private StockDelegate mListener;
	SwipeMenuListView lv;
	ArrayAdapter<Security> stockListAdapter;
	ArrayList<Security> securityList;
	String favStocks = "";
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mListener = (StockDelegate) activity;
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
		lv = (SwipeMenuListView) getActivity().findViewById(R.id.lv_stock);
		lv.setChoiceMode(SwipeMenuListView.CHOICE_MODE_SINGLE);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			if(isConnected()){
				lv.setItemChecked(position, true);
				
				mListener.passSymbolToNews(stockListAdapter.getItem(position).getSymbol());
			}
			}
		});
		
		// step 1. create a MenuCreator
				SwipeMenuCreator creator = new SwipeMenuCreator() {

					@Override
					public void create(SwipeMenu menu) {
						// create "open" item
						SwipeMenuItem openItem = new SwipeMenuItem(
								getActivity());
						// set item background
						openItem.setBackground(new ColorDrawable(Color.rgb(0xD0, 0xDF,
								0x00)));
						// set item width
						openItem.setWidth(dp2px(90));
						// set item title
						openItem.setTitle("Open");
						// set item title fontsize
						openItem.setTitleSize(18);
						// set item title font color
						openItem.setTitleColor(Color.WHITE);
						// add to menu
						menu.addMenuItem(openItem);

						// create "delete" item
						SwipeMenuItem deleteItem = new SwipeMenuItem(
								getActivity());
						// set item background
						/*deleteItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
								0xCE)));*/
					//	deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						//		0x3F, 0x25))); 33b5e5 2d9fc9
						deleteItem.setBackground(new ColorDrawable(Color.rgb(0x2d,
									0x9f, 0xc9)));
						// set item width
						deleteItem.setWidth(dp2px(90));
						// set a icon
						deleteItem.setIcon(R.drawable.ic_delete);
						// add to menu
						menu.addMenuItem(deleteItem);
					}
				};
				// set creator
				lv.setMenuCreator(creator);

				// step 2. listener item click event
				lv.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
						
						if(isConnected()){
						Security item = securityList.get(position);
						switch (index) {
						case 0:
							// open
							//open(item);
							
							mListener.securityClicked(stockListAdapter.getItem(position));
							break;
						case 1:
							// delete
//							delete(item);
							removeFromFavorites(securityList.get(position).getSymbol());
							securityList.remove(position);
							stockListAdapter.notifyDataSetChanged();
							break;
						}
						}
						return false;
					}
				});
				
				// set SwipeListener
				lv.setOnSwipeListener(new OnSwipeListener() {
					
					@Override
					public void onSwipeStart(int position) {
						// swipe start
					}
					
					@Override
					public void onSwipeEnd(int position) {
						// swipe end
					}
				});


		favStocks = "";
		ParseQuery<ParseObject> favQuery = ParseQuery.getQuery("Favorites");
		favQuery.whereEqualTo("UserName", ParseUser.getCurrentUser().getUsername());
		favQuery.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				
				if(e == null && objects!= null){
					
					for(ParseObject pObj : objects){
						favStocks+= pObj.getString("StockName")+",";
					}
					if(favStocks.length() > 0){
						favStocks =  favStocks.substring(0,favStocks.length()-1);
						updateSecurities(favStocks);
					}
					
					
					
				}else{
					Log.d("Error in Fav", e.getLocalizedMessage());
				}
				
			}
		});	
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_stocklist, container,false);
		return rootView;
	}

	
	public void updateSecurities(String stocks){
		new JSONQuoteAsyncTask().execute("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22"+ stocks +"%22)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");	
	
	}
	
	
	
	
	
	public class JSONQuoteAsyncTask extends AsyncTask<String, Void, ArrayList<Security>>{
		ProgressDialog pd;
		@Override
		protected ArrayList<Security> doInBackground(String... params) {

			try {
			URL url = new URL(params[0]);
			HttpURLConnection con;	
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.connect();
			int statusCode = con.getResponseCode();
			if(statusCode == HttpURLConnection.HTTP_OK){
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line = reader.readLine();
				
				while(line != null){
					sb.append(line);
					line = reader.readLine();
				}
				
				return JSONUtility.StockJSONParser.parseStocks(sb.toString());
				
			}
			
			} catch (IOException e) {
		
				e.printStackTrace();
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
			
			
			return null;
		}

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			pd = new ProgressDialog(getActivity(),AlertDialog.THEME_HOLO_DARK);
			pd.setTitle("Loading Stocks Data");
			pd.setMessage("Loading...");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected void onPostExecute(ArrayList<Security> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pd.dismiss();
			
			
			if(result != null){
				securityList = result;
				stockListAdapter = new StockListAdapter(getActivity(), R.layout.stock_listview,securityList,StockListFragment.this,"Change");
				lv.setAdapter(stockListAdapter);
			
				Log.d("DEMO", result.toString());
			}
			
		}
		
		

	}
	
	
	
	
	public void refreshListView(String which){
		stockListAdapter = new StockListAdapter(getActivity(), R.layout.stock_listview,securityList,StockListFragment.this,which);
		stockListAdapter.notifyDataSetChanged();
		lv.setAdapter(stockListAdapter);
		
	}
	
	
	
	
	public interface StockDelegate{
		
		public void securityClicked(Security stockObj);
		public void passSymbolToNews(String stockSymbol);
		
	}
	
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
	
private void removeFromFavorites(String stockSymbol) {
		
		ParseQuery<ParseObject> favQuery = ParseQuery.getQuery("Favorites");
		favQuery.whereEqualTo("UserName", ParseUser.getCurrentUser().getUsername());
		favQuery.whereEqualTo("StockName", stockSymbol);
		favQuery.getFirstInBackground(new GetCallback<ParseObject>() {
			
			@Override
			public void done(ParseObject object, ParseException e) {
				
				if(e == null){
					try {
						object.delete();
						Toast.makeText(getActivity(), "Successfully removed from Favorites", Toast.LENGTH_SHORT).show();
					} catch (ParseException e1) {
						
						e1.printStackTrace();
					}
				}else{
					Log.d("Error in Fav",e.getLocalizedMessage());
				}
			}
		});
		
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
