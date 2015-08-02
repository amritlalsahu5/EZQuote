package com.example.adapter;

/*
 * Author: Alexander Pinkerton, Udeep Manchanda, Tianyi Xie
 */

import java.util.List;

import com.example.finalproject.R;
import com.example.finalproject.R.id;
import com.example.pojo.AboutUs;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutUsAdapter extends ArrayAdapter<AboutUs> {
	
	Context mContext;
	int mResource;
	List<AboutUs> aboutUsList;
	

	public AboutUsAdapter(Context context, int resource,
			List<AboutUs> objects) {
		super(context, resource, objects);
		this.mContext = context;
		this.mResource = resource;
		this.aboutUsList = objects;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return aboutUsList.size();
	}


	@Override
	public AboutUs getItem(int position) {
		// TODO Auto-generated method stub
		return aboutUsList.get(position);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		final AboutUs listItem = getItem(position);
		
		 // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
        	
        // inflate the layout
        	LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        		
        	convertView = inflater.inflate(mResource, parent, false);
        	
        	// Creates a ViewHolder and store references to the two children views we want to bind data to.
          
        	holder = new ViewHolder();
            holder.nameTv = (TextView) convertView.findViewById(R.id.textView1);
            holder.emailTv = (TextView) convertView.findViewById(R.id.textView2);
            
            holder.picIv = (ImageView) convertView.findViewById(R.id.imageView1);
            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView and the ImageView.
        	
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameTv.setText(listItem.getName());
        holder.emailTv.setText(listItem.getEmail());
        //holder.picIv.setImageResource(listItem.getPic());
        
        return convertView;
    }

	
	static class ViewHolder {

		private TextView nameTv,emailTv ;
		private ImageView picIv;
	
	}



	

}
