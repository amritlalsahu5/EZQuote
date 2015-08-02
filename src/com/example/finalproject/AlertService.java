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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.pojo.Security;
import com.example.pojo.StockAlert;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class AlertService extends Service {
	// Binder given to clients
	private final IBinder mBinder = new LocalBinder();
	// Random number generator
	private final Random mGenerator = new Random();
	
	ArrayList<StockAlert> alertList;
	ArrayList<Security> stockList;
	List<ParseObject> alertObjs;
 
	public static final long NOTIFY_INTERVAL = 5 * 1000; // 5 minutes

	// run on another Thread to avoid crash
	private Handler mHandler = new Handler();
	// timer handling
	private Timer mTimer = null;

	@Override
	public void onCreate() {

		Log.d("SERVICE", "THE SERVICE WAS STARTED SUCCESSFULLY");

		// cancel if already existed
		if (mTimer != null) {
			mTimer.cancel();
		} else {
			// recreate new
			mTimer = new Timer();
		}
		// schedule task
		mTimer.scheduleAtFixedRate(new CheckAlertTask(), 0, NOTIFY_INTERVAL);
		//sendAlert();
	}

	class CheckAlertTask extends TimerTask {

		@Override
		public void run() {
			// run on another thread
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					// display toast
				//	sendAlert();
					if(ParseUser.getCurrentUser() != null && isConnected() ){
						checkAlerts();
					}
				}

			});
		}
	}
	


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/**
	 * Class used for the client Binder. Because we know this service always
	 * runs in the same process as its clients, we don't need to deal with IPC.
	 */
	public class LocalBinder extends Binder {
		AlertService getService() {
			// Return this instance of LocalService so clients can call public
			// methods
			return AlertService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	/** method for clients */
	public int getRandomNumber() {
		return mGenerator.nextInt(10000000);
	}

	/**
	 * Go to parse.com and query the users alerts, if one is active, send
	 * notification.
	 */
	public void checkAlerts() {

		alertList = new ArrayList<StockAlert>();
		
		ParseQuery<ParseObject> alertQuery = ParseQuery.getQuery("Alert");
		alertQuery.whereEqualTo("UserName", ParseUser.getCurrentUser().getUsername());
		alertQuery.findInBackground(new FindCallback<ParseObject>() {
			String alertStocks="";
			Set<String> set = new HashSet<String>();
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				// TODO Auto-generated method stub
				alertObjs = objects;
				for(ParseObject obj: objects){
					alertList.add(new StockAlert(obj.getString("oldPrice"), obj.getString("targetPrice"), obj.getString("symbol"),obj.getString("StockState")));
					set.add(obj.getString("symbol"));
				}
				Iterator<String> i = set.iterator();
				
				while(i.hasNext()){
					alertStocks+= i.next()+",";
				}
				
				
				
				if(alertStocks.length() > 0){
					alertStocks = alertStocks.substring(0,alertStocks.length()-1);
				}
				
				if(alertStocks.trim().length()>0){
					new JSONQuoteAsyncTask().execute("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22"+ alertStocks +"%22)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");	
				}
				
			}
		});
		
		
		
		
		
		
	}

	public void sendAlert(String stockSymbol, String targetPrice,String stockState) {
		Notification.Builder mBuilder = new Notification.Builder(this)
				.setSmallIcon(R.drawable.appicon)
				.setContentTitle("EZQuote - '" + stockSymbol + "'")
				.setContentText( stockState + " Target Price " + targetPrice + " reached. Hurry!");
		// Creates an explicit intent for an Activity in your app
		
		Intent resultIntent = new Intent(this, MarketSummaryActivity.class);

		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MarketSummaryActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(getRandomNumber(), mBuilder.build());
		
		//Delete the alarm after it goes off.
		for(ParseObject o : alertObjs){
			if(stockSymbol.equalsIgnoreCase((o.getString("symbol"))) &&
					targetPrice.equals(o.getString("targetPrice"))   &&
					stockState.equals(o.getString("StockState"))){
				
					o.deleteInBackground();
			}
		}
		
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
		protected void onPostExecute(ArrayList<Security> result) {
	
			super.onPostExecute(result);
		
					
			if(result != null){
				stockList = result;
				
				for(StockAlert sa : alertList){
					for(Security s : stockList){
						
						//TODO fix this logic, this is only for gains.
						
						if(sa.getStockSymbol().equalsIgnoreCase(s.getSymbol())){
							//	&& Double.parseDouble(sa.getTargetPrice()) <= Double.parseDouble(s.getAskPrice())){
							
							if(sa.getStockState().equals("GAIN") && 
									 Double.parseDouble(sa.getTargetPrice()) <= Double.parseDouble(s.getAskPrice())){
							
							sendAlert(sa.getStockSymbol(),sa.getTargetPrice() , "GAIN");
							
							}else if(sa.getStockState().equals("LOSS") && 
									 Double.parseDouble(sa.getTargetPrice()) >= Double.parseDouble(s.getAskPrice())){
								
							sendAlert(sa.getStockSymbol(),sa.getTargetPrice() , "LOSS");
							
							}
						}
					}
				}
				
				
				Log.d("DEMO", result.toString());
			}
			
		}
		
		

	}
	
	public boolean isConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			// Toast.makeText(MainActivity.this, "Internet is connected",
			// Toast.LENGTH_SHORT).show();
			return true;
		} else {
			
			return false;
		}
	}
	
	
	

}
