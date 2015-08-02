package com.example.adapter;

/*
 * Author: Alexander Pinkerton, Udeep Manchanda, Tianyi Xie
 */


import java.util.List;

import com.example.finalproject.R;
import com.example.finalproject.R.id;
import com.example.pojo.Headline;

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

public class StockNewsAdapter extends ArrayAdapter<Headline> {
	
	Context mContext;
	int mResource;
	List<Headline> securityList;
	

	public StockNewsAdapter(Context context, int resource,
			List<Headline> objects) {
		super(context, resource, objects);
		this.mContext = context;
		this.mResource = resource;
		this.securityList = objects;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return securityList.size();
	}


	@Override
	public Headline getItem(int position) {
		// TODO Auto-generated method stub
		return securityList.get(position);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		final Headline listItem = getItem(position);
		
		 // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
        	
        // inflate the layout
        	LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        		
        	convertView = inflater.inflate(mResource, parent, false);
        	
        	// Creates a ViewHolder and store references to the two children views we want to bind data to.
          
        	holder = new ViewHolder();
            holder.newsContentTv = (TextView) convertView.findViewById(R.id.newsContentTv);
            holder.sourceByTv = (TextView) convertView.findViewById(R.id.sourceByTv);
          
            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView and the ImageView.
        	
            holder = (ViewHolder) convertView.getTag();
        }

        // Bind the data efficiently with the holder.
        holder.newsContentTv.setText(listItem.getTitle());
        holder.sourceByTv.setText(listItem.getSourceBy());
        
        return convertView;
    }

	
	static class ViewHolder {

		private TextView newsContentTv,sourceByTv ;
		
	
	}



	

}
