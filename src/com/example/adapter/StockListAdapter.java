package com.example.adapter;

/*
 * Author: Alexander Pinkerton, Udeep Manchanda, Tianyi Xie
 */

import java.util.List;

import com.example.finalproject.R;
import com.example.finalproject.StockListFragment;
import com.example.finalproject.R.id;
import com.example.pojo.Security;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class StockListAdapter extends ArrayAdapter<Security> {
	
	Context mContext;
	int mResource;
	List<Security> securityList;
	StockListFragment fragment;
	String which;

	public StockListAdapter(Context context, int resource,
			List<Security> objects,StockListFragment fragment,String which) {
		super(context, resource, objects);
		this.mContext = context;
		this.mResource = resource;
		this.securityList = objects;
		this.fragment = fragment;
		this.which = which;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return securityList.size();
	}


	@Override
	public Security getItem(int position) {
		// TODO Auto-generated method stub
		return securityList.get(position);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		final Security listItem = getItem(position);
		
		 // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
        	
        // inflate the layout
        	LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        		
        	convertView = inflater.inflate(mResource, parent, false);
        	
        	// Creates a ViewHolder and store references to the two children views we want to bind data to.
          
        	holder = new ViewHolder();
            holder.stockNameTv = (TextView) convertView.findViewById(R.id.stockNameTv);
            holder.priceTv = (TextView) convertView.findViewById(R.id.priceTv);
            holder.changeButton = (Button) convertView.findViewById(R.id.changeButton);
            
    
            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView and the ImageView.
        	
            holder = (ViewHolder) convertView.getTag();
        }

        // Bind the data efficiently with the holder.
        holder.stockNameTv.setText(listItem.getSymbol());
        holder.priceTv.setText(listItem.getAskPrice()+"");
        
    	if(which.equals("Change")){
    		   holder.changeButton.setText(listItem.getChange()+"");
		}else if(which.equals("MarketCapitalization")){
			   holder.changeButton.setText(listItem.getMarketCapitalization());
		}else if(which.equals("Percentage")) {
			   holder.changeButton.setText(listItem.getChangePercentage());
		}
        
        if(Double.parseDouble(listItem.getChange()) >= 0){
        	holder.changeButton.setBackgroundColor(Color.GREEN);
        }else{
        	holder.changeButton.setBackgroundColor(Color.RED);
        }
        
        holder.changeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
	
				Button b = (Button) v;
				if(which.equals("Change")){
					//b.setText(listItem.getMarketCapitalization());
					which = "MarketCapitalization";
				}else if(which.equals("MarketCapitalization")){
				//	b.setText(listItem.getChangePercentage()+"");
					which = "Percentage";
				}else if(which.equals("Percentage")) {
				//	b.setText(listItem.getChange()+"");
					which = "Change";
				}
				
				fragment.refreshListView(which);
				
			}
		});
        return convertView;
    }

	
	static class ViewHolder {

		private TextView stockNameTv,priceTv ;
		private Button changeButton;
	//	String which ="";
	
	}



	

}
