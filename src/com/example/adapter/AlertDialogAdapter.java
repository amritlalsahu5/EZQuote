package com.example.adapter;
/*
 * Author: Alexander Pinkerton, Udeep Manchanda, Tianyi Xie
 */
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.MarketSummaryActivity;
import com.example.finalproject.R;
import com.example.pojo.StockAlert;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class AlertDialogAdapter extends ArrayAdapter<StockAlert> {

	Context mContext;
	int mResource;
	List<StockAlert> alertList;

	public AlertDialogAdapter(Context context, int resource, List<StockAlert> objects) {
		super(context, resource, objects);
		this.mContext = context;
		this.mResource = resource;
		this.alertList = objects;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return alertList.size();
	}

	@Override
	public StockAlert getItem(int position) {
		// TODO Auto-generated method stub
		return alertList.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		final StockAlert listItem = getItem(position);

		// When convertView is not null, we can reuse it directly, there is no
		// need
		// to reinflate it. We only inflate a new View when the convertView
		// supplied
		// by ListView is null.
		if (convertView == null) {

			// inflate the layout
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

			convertView = inflater.inflate(mResource, parent, false);

			// Creates a ViewHolder and store references to the two children
			// views we want to bind data to.

			holder = new ViewHolder();
			holder.symbolTv = (TextView) convertView
					.findViewById(R.id.textView1);
			holder.targetPriceTv = (TextView) convertView
					.findViewById(R.id.textView2);
			holder.stockStateTv = (TextView) convertView.findViewById(R.id.textView3);

			holder.deleteIv = (ImageView) convertView
					.findViewById(R.id.imageView1);
			convertView.setTag(holder);
		} else {
			// Get the ViewHolder back to get fast access to the TextView and
			// the ImageView.

			holder = (ViewHolder) convertView.getTag();
		}

		holder.symbolTv.setText(listItem.getStockSymbol());
		holder.targetPriceTv.setText("Target Price: "+ listItem.getTargetPrice());
		holder.stockStateTv.setText("Stock State: " + listItem.getStockState());

		holder.deleteIv.setImageResource(R.drawable.redcancel);

		holder.deleteIv.setTag(new Integer(position));

		holder.deleteIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ParseQuery<ParseObject> deleteQuery = ParseQuery
						.getQuery("Alert");
				deleteQuery.whereEqualTo("UserName", ParseUser
						.getCurrentUser().getUsername());
				final int position = (Integer) v.getTag();

				deleteQuery.whereEqualTo("symbol", alertList.get(position).getStockSymbol());
				deleteQuery.whereEqualTo("targetPrice", alertList.get(position).getTargetPrice());
				deleteQuery.whereEqualTo("StockState", alertList.get(position).getStockState());

				deleteQuery.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> objects, ParseException e) {

						if (e == null) {

							for (ParseObject obj : objects) {

								obj.deleteInBackground();
							}
							alertList.remove(position);
							((MarketSummaryActivity) mContext)
									.refreshReminders(alertList);
						} else {
							e.printStackTrace();
							Log.d("DeleteCityError", e.getLocalizedMessage());
						}
					}
				});

			}
		});

		return convertView;
	}

	static class ViewHolder {

		private TextView symbolTv, targetPriceTv,stockStateTv;
		private ImageView deleteIv;

	}

}
