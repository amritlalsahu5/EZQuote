package com.example.finalproject;

/*
 * Author: Alexander Pinkerton, Udeep Manchanda, Tianyi Xie
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.example.pojo.Headline;
import com.example.pojo.Security;

import android.util.Log;


public class JSONUtility {
	
	static public class StockJSONParser{

		
		static public ArrayList<Security> parseStocks(String in) throws JSONException, IOException{	
			ArrayList<Security> stocks = new ArrayList<Security>();			
			Security stock;


			
			JSONObject root = new JSONObject(in);
			JSONObject query = root.getJSONObject("query");
			JSONObject results = query.getJSONObject("results");
			
			int count = query.getInt("count");
			
			if(count > 1){
				
				JSONArray quotes = results.getJSONArray("quote");
				
				for(int i=0;i<quotes.length();i++){
					JSONObject stockObject = quotes.getJSONObject(i);
					stock = new Security();
					stock.setCompanyName(stockObject.getString("Name"));
					stock.setSymbol(stockObject.getString("symbol"));
					
					if(stockObject.getString("Ask").equals("null")){
						stock.setAskPrice("--");
					}else{
						stock.setAskPrice(stockObject.getString("Ask"));
					}
					
					if(stockObject.getString("Change").equals("null")){
						stock.setChange("--");
					}else{
						stock.setChange(stockObject.getString("Change"));
					}
					
					if(!stockObject.getString("MarketCapitalization").equals("null")){
						stock.setMarketCapitalization(stockObject.getString("MarketCapitalization"));
					}else{
						stock.setMarketCapitalization("--");
					}
					
					String chgPercentage = stockObject.getString("PercentChange");
					
				/*	if(("" + chgPercentage.charAt(0)).equals("+")){
						chgPercentage = stockObject.getString("Change_PercentChange").split("-")[1];
					}else{
						
						chgPercentage = chgPercentage.substring(chgPercentage.length()-6,chgPercentage.length());						
						
					}*/
					stock.setChangePercentage(chgPercentage);
					
					stock.setDaysLow(stockObject.getString("DaysLow"));
					stock.setDaysHigh(stockObject.getString("DaysHigh"));
					stock.setYearLow(stockObject.getString("YearLow"));
					stock.setYearHigh(stockObject.getString("YearHigh"));
					stock.setVolume(stockObject.getString("Volume"));
					stock.setPeRatio(stockObject.getString("PERatio"));
					if(stockObject.getString("DividendYield").equals("null")){
						stock.setYield("--");
					}else{
						stock.setYield(stockObject.getString("DividendYield"));
					}
					
					stock.setAvgVolume(stockObject.getString("AverageDailyVolume"));
					
					
					
					stocks.add(stock);
				}
				
				
			}else if (count == 1){
				
				JSONObject quote = results.getJSONObject("quote");
				stock = new Security();
				stock.setCompanyName(quote.getString("Name"));
				stock.setSymbol(quote.getString("symbol"));
				
				if(quote.getString("Ask").equals("null")){
					stock.setAskPrice("--");
				}else{
					stock.setAskPrice(quote.getString("Ask"));
				}
				
				if(quote.getString("Change").equals("null")){
					stock.setChange("--");
				}else{
					stock.setChange(quote.getString("Change"));
				}
				
				if(!quote.getString("MarketCapitalization").equals("null")){
					stock.setMarketCapitalization(quote.getString("MarketCapitalization"));
				}else{
					stock.setMarketCapitalization("--");
				}
				
				String chgPercentage = quote.getString("PercentChange");
				
			/*	if(("" + chgPercentage.charAt(0)).equals("+")){
					chgPercentage = stockObject.getString("Change_PercentChange").split("-")[1];
				}else{
					
					chgPercentage = chgPercentage.substring(chgPercentage.length()-6,chgPercentage.length());						
					
				}*/
				stock.setChangePercentage(chgPercentage);
				
				stock.setDaysLow(quote.getString("DaysLow"));
				stock.setDaysHigh(quote.getString("DaysHigh"));
				stock.setYearLow(quote.getString("YearLow"));
				stock.setYearHigh(quote.getString("YearHigh"));
				stock.setVolume(quote.getString("Volume"));
				stock.setPeRatio(quote.getString("PERatio"));
				if(quote.getString("DividendYield").equals("null")){
					stock.setYield("--");
				}else{
					stock.setYield(quote.getString("DividendYield"));
				}
				
				stock.setAvgVolume(quote.getString("AverageDailyVolume"));
				
				
				Log.d("ULTRONICS", stock.toString());
				stocks.add(stock);
			}
			
			
			return stocks;
		}

		
		//=================================================================================================
		
		static public ArrayList<Headline> parseNews(String in) throws JSONException, IOException{	
			ArrayList<Headline> headlines = new ArrayList<Headline>();
			Headline headline = new Headline();
			
			JSONObject root = new JSONObject(in);
			JSONObject query = root.getJSONObject("query");
			JSONObject results = query.getJSONObject("results");
			
			int count = query.getInt("count");
			
			if(count > 1){
				
				JSONArray headlinez = results.getJSONArray("a");
				
				for(int i=0;i<headlinez.length();i++){
					headline = new Headline();
					JSONObject headlineObject = headlinez.getJSONObject(i);
					headline.setTitle(headlineObject.getString("content"));
					headline.setLink(headlineObject.getString("href"));
					
					String sourceBy = headlineObject.getString("data-ylk");
					sourceBy = sourceBy.substring(3,sourceBy.length()).split(";")[0];
					headline.setSourceBy(sourceBy);
					
					headlines.add(headline);
					Log.d("ULTRONICS", headline.toString());
				}
				
				
			}else if (count == 1){

				JSONObject headlineObject = results.getJSONObject("a");
				headline.setTitle(headlineObject.getString("content"));
				headline.setLink(headlineObject.getString("href"));
				headlines.add(headline);
				//Log.d("ULTRONICS", headline.toString());
			}
			
			
			return headlines;
		}
		
		
	}
	
	
	
}
