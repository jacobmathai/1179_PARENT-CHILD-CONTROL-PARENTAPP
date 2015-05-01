package com.think.parentapp_adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartparenting.beans.CallBean;
import com.think.parentapp.R;

public class CallLogAdapter extends ArrayAdapter<CallBean> {

	private Context context;
	private ArrayList<CallBean> list;
	ArrayList<CallBean> beans = null;
	
	String date = "";
	Date fromdate;
	

	public CallLogAdapter(Context context, ArrayList<CallBean> list) {
		super(context, R.layout.call_log_row, list);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
		beans = new ArrayList<CallBean>();
		beans.addAll(list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater
					.inflate(R.layout.call_log_row, parent, false);
			holder = new ViewHolder();
			holder.phoneNumberTextView = (TextView) convertView
					.findViewById(R.id.phoneNumberTextView);
			holder.typeTextView = (TextView) convertView
					.findViewById(R.id.typeTextView);
			holder.dateTextView = (TextView) convertView
					.findViewById(R.id.dateTextView);
			holder.timeTextView = (TextView) convertView
					.findViewById(R.id.timeTextView);
			holder.durationTextView = (TextView) convertView
					.findViewById(R.id.durationTextView);
			holder.audioImageView = (ImageView) convertView
					.findViewById(R.id.audioPlayImageView);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		date = list.get(position).getDate();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		try {
			fromdate = sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		holder.phoneNumberTextView.setText(list.get(position).getPhoneNumber());
		holder.typeTextView.setText(list.get(position).getType());
		holder.dateTextView.setText(list.get(position).getDate());
		holder.timeTextView.setText(list.get(position).getTime());
		holder.durationTextView.setText(list.get(position).getDuration());
		/*if (list.get(position).getSetAudio().equals("1")) {
			holder.audioImageView.setVisibility(View.INVISIBLE);
		} else {
			holder.audioImageView.setVisibility(View.VISIBLE);
		}*/
		return convertView;
	}

	private static class ViewHolder {
		TextView phoneNumberTextView;
		TextView typeTextView = null;
		TextView dateTextView = null;
		TextView timeTextView = null;
		TextView durationTextView = null;
		ImageView audioImageView = null;
	}

	public void filter(String typeMissed) {
		// TODO Auto-generated method stub
		list.clear();
		if (typeMissed.equals("All")) {
			list.addAll(beans);
		} else {
			for (CallBean bean : beans) {
				if (bean.getType().equals(typeMissed)) {
					list.add(bean);
				}
			}
		}
		notifyDataSetChanged();
	}
}
