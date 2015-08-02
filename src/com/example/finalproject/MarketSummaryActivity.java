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
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.adapter.AlertDialogAdapter;
import com.example.finalproject.QuoteViewFragment.QuoteViewDelegate;
import com.example.finalproject.StockListFragment.StockDelegate;
import com.example.finalproject.StockNewsFragment.NewsDelegate;
import com.example.pojo.Security;
import com.example.pojo.StockAlert;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MarketSummaryActivity extends Activity implements NewsDelegate, StockDelegate, 
													QuoteViewDelegate, SearchView.OnQueryTextListener{

	private SearchView mSearchView;
	private long lastSearchTime=0;
	ArrayAdapter<StockAlert> arrayAdapter;
	ListView lv;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (savedInstanceState == null) {
			getFragmentManager()
			.beginTransaction()
			.add(R.id.stockscontainer, new StockListFragment(),"stocklist")
			.add(R.id.newscontainer, new StockNewsFragment(),"newslist")
			.commit();
			
		}
		
	
	/*	StockListFragment stockLister = (StockListFragment) getFragmentManager().findFragmentByTag("stocklist");
		StockNewsFragment newsLister = (StockNewsFragment) getFragmentManager().findFragmentByTag("newslist");
	*/
		startService(new Intent(this, AlertService.class));
		
	}

	
	
	

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		 	MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.menuquotesearch, menu);
	        MenuItem searchItem = menu.findItem(R.id.action_search);
	        mSearchView = (SearchView) searchItem.getActionView();
	        setupSearchView(searchItem);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		if(isConnected()){
		int id = item.getItemId();
		if (id == R.id.logout) {
			ParseUser.logOut();
			Intent i = new Intent(MarketSummaryActivity.this,LoginActivity.class);
			startActivity(i);
			finish();
			return true;
		}else if (id == R.id.manageAlerts) {
			
			final AlertDialog.Builder builderSingle = new AlertDialog.Builder(
					MarketSummaryActivity.this, AlertDialog.THEME_HOLO_DARK);
			builderSingle.setTitle("Manage Alerts");
			final LinearLayout LL = new LinearLayout(this);
			LL.setOrientation(LinearLayout.VERTICAL);
			final ArrayList<StockAlert> aList = new ArrayList<StockAlert>();
			builderSingle.setCancelable(false);
			ParseQuery<ParseObject> alertQuery = ParseQuery.getQuery("Alert");
			alertQuery.whereEqualTo("UserName", ParseUser.getCurrentUser()
					.getUsername());
			alertQuery.findInBackground(new FindCallback<ParseObject>() {

				@Override
				public void done(List<ParseObject> arg0, ParseException arg1) {

					if (arg1 == null) {
						for (ParseObject obj : arg0) {
							StockAlert a = new StockAlert();
							a.setStockSymbol(obj.getString("symbol"));
							a.setOldPrice(obj.getString("oldPrice"));
							a.setTargetPrice(obj.getString("targetPrice"));
							a.setStockState(obj.getString("StockState"));
							aList.add(a);
						}

						LayoutParams LLParams = new LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.MATCH_PARENT);

						LL.setLayoutParams(LLParams);

						lv = new ListView(MarketSummaryActivity.this);

						arrayAdapter = new AlertDialogAdapter(
								MarketSummaryActivity.this,
								R.layout.alert_list_view, aList);
						lv.setAdapter(arrayAdapter);
						LL.addView(lv);
						builderSingle.setView(LL);
						builderSingle.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
						builderSingle.show();

					} else {
						arg1.printStackTrace();
					}
				}
			});
			
			
			
			return true;
		}
		}
		return super.onOptionsItemSelected(item);
		
	}

	
	


	public void getQuote(Security stockObj){
		
		FragmentManager manager = getFragmentManager();
		
		if(manager.findFragmentByTag("quoteview") != null && manager.findFragmentByTag("quoteview").isAdded()){
			manager.beginTransaction()
			.replace(R.id.detailcontainer, new QuoteViewFragment(stockObj), "quoteview")
			.addToBackStack(null)
			.commit();
		}else{
			manager
			.beginTransaction()
			.remove(getFragmentManager().findFragmentByTag("stocklist"))
			.remove(getFragmentManager().findFragmentByTag("newslist"))
			.add(R.id.detailcontainer, new QuoteViewFragment(stockObj), "quoteview")
			.addToBackStack(null)
			.commit();
			Log.d("ASLKJASDLKJSADLJ", "THIS HAPPENED");
		}
		
		manager.executePendingTransactions();
		
	}



	@Override
	public void securityClicked(Security stockObj) {
		getQuote(stockObj);	
	}



	@Override
	public void updateNews() {
		
		
	}


	@Override
	public void delegateFromQuoteView() {
		// TODO Auto-generated method stub
		
	}




	 private void setupSearchView(MenuItem searchItem) {

	        if (isAlwaysExpanded()) {
	            mSearchView.setIconifiedByDefault(false);
	        } else {
	            searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
	                    | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
	        }

	       /* SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	        if (searchManager != null) {
	            List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();

	            SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
	            for (SearchableInfo inf : searchables) {
	                if (inf.getSuggestAuthority() != null
	                        && inf.getSuggestAuthority().startsWith("applications")) {
	                    info = inf;
	                }
	            }
	            mSearchView.setSearchableInfo(info);
	        }
*/
	        
	        mSearchView.setOnQueryTextListener(this);
	    }

	    public boolean onQueryTextChange(String newText) {

	        return false;
	    }

	    public boolean onQueryTextSubmit(String stockSymbol) {
	    	
	    	//This should fix the bug. Only one search per second and a half.
	    	if(isConnected()){
	    	stockSymbol = stockSymbol.toUpperCase();
	    	long searchTime = System.currentTimeMillis();
	    	if(searchTime > lastSearchTime + 1500){
		    	new JSONQuoteSearchAsyncTask().execute("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22"+ stockSymbol +"%22)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");	
	    		lastSearchTime = searchTime;
	    	}
	    	}
	        return true;
	    }

	    public boolean onClose() {

	        return false;
	    }

	    protected boolean isAlwaysExpanded() {
	        return false;
	    }







		@Override
		public void passSymbolToNews(String stockSymbol) {
			 StockNewsFragment snf = (StockNewsFragment) getFragmentManager().findFragmentByTag("newslist");
				
				snf.refresh(stockSymbol);
			
		}





		public class JSONQuoteSearchAsyncTask extends AsyncTask<String, Void, ArrayList<Security>>{

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
				// TODO Auto-generated method stub
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(ArrayList<Security> result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				
				if(result != null && !result.get(0).getCompanyName().equals("null")
						&& !result.get(0).getSymbol().equals("null")){
					getQuote(result.get(0));
					Log.d("DEMO", result.toString());
				}else{
					Toast.makeText(MarketSummaryActivity.this, "No stock found in Yahoo API", Toast.LENGTH_SHORT).show();
				}
				
			
				
				}
				
			}





		public void refreshReminders(List<StockAlert> alertList) {

			arrayAdapter.notifyDataSetChanged();
			Toast.makeText(this, "Alert Removed!", Toast.LENGTH_SHORT).show();
			
		}
			
		public boolean isConnected() {
			ConnectivityManager connMgr = (ConnectivityManager) getSystemService(MarketSummaryActivity.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				// Toast.makeText(MainActivity.this, "Internet is connected",
				// Toast.LENGTH_SHORT).show();
				return true;
			} else {
				Toast.makeText(MarketSummaryActivity.this, "No Internet Connection",
						Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		


}
