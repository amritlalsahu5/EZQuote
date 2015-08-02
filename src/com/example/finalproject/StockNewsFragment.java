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

import org.json.JSONException;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adapter.StockNewsAdapter;
import com.example.pojo.Headline;

public class StockNewsFragment extends Fragment{
	
	private NewsDelegate mListener;
	ArrayList<Headline> newsItems;
	ListView newsListerLV;
	ArrayAdapter<Headline> adapter;
	
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mListener = (NewsDelegate) activity;
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		newsListerLV = (ListView) getView().findViewById(R.id.lv_news);
		
		newsListerLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					if(isConnected()){
					
					String url = newsItems.get(position).getLink();
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
					startActivity(i);
					}
					
				}
			});
		 
		
		updateNewsList(); // default google news on start
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_newslist, container,false);
		return rootView;
	}
	
	
	public void updateNewsList(){
		//newsItems = news;
		new JSONNewsAsyncTask().execute("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20html%20where%20url%3D'http%3A%2F%2Ffinance.yahoo.com%2Fq%3Fs%3DGOOG'%20and%20xpath%3D'%2F%2Fdiv%5B%40id%3D%22yfi_headlines%22%5D%2Fdiv%5B2%5D%2Ful%2Fli%2Fa'&format=json&diagnostics=true&callback=");
	}
	
	
	public void test(){
		newsListerLV.invalidateViews();
	}
	
	
	public void refresh(String stockSymbol){
		
		Log.d("gotit",stockSymbol);
		//Toast.makeText(getActivity(), stockSymbol, 3000).show();
		
			new JSONNewsAsyncTask().execute("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20html%20where%20url%3D'http%3A%2F%2Ffinance.yahoo.com%2Fq%3Fs%3D"+ stockSymbol +"'%20and%20xpath%3D'%2F%2Fdiv%5B%40id%3D%22yfi_headlines%22%5D%2Fdiv%5B2%5D%2Ful%2Fli%2Fa'&format=json&diagnostics=true&callback=");
		
	}
	
	
	
	public class JSONNewsAsyncTask extends AsyncTask<String, Void, ArrayList<Headline>>{

		@Override
		protected ArrayList<Headline> doInBackground(String... params) {
			// TODO Auto-generated method stub
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
				
				return JSONUtility.StockJSONParser.parseNews(sb.toString());
				
			}
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
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
		protected void onPostExecute(ArrayList<Headline> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(result != null){
				Log.d("NEWS", result.size()+"");
				newsItems = result;
				adapter = new StockNewsAdapter(getActivity(), R.layout.news_listview, newsItems);
				adapter.setNotifyOnChange(true);
				newsListerLV.setAdapter(adapter);	
			}
			
			
			
		}
		
		

	}
	

	
	public interface NewsDelegate{
		
		public void updateNews();
		
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
