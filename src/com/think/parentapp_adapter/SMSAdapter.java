package com.think.parentapp_adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.smartparenting.beans.SMSBean;
import com.think.parentapp.R;

public class SMSAdapter extends ArrayAdapter<SMSBean> {

	private Context context;
	private ArrayList<SMSBean> list;
	private ArrayList<SMSBean> beans;

	public SMSAdapter(Context context, ArrayList<SMSBean> list) {
		super(context, R.layout.sms_log_row, list);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
		beans = new ArrayList<SMSBean>();
		beans.addAll(list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.sms_log_row, parent, false);
			holder = new ViewHolder();
			holder.phoneNumberTextView = (TextView) convertView
					.findViewById(R.id.phoneNumberTextView);
			holder.typeTextView = (TextView) convertView
					.findViewById(R.id.typeTextView);
			holder.dateTextView = (TextView) convertView
					.findViewById(R.id.dateTextView);
			holder.timeTextView = (TextView) convertView
					.findViewById(R.id.timeTextView);
			holder.contentTextView = (TextView) convertView
					.findViewById(R.id.durationTextView);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.phoneNumberTextView.setText(list.get(position).getPhoneNumber());
		holder.typeTextView.setText(list.get(position).getType());
		holder.dateTextView.setText(list.get(position).getDate());
		holder.timeTextView.setText(list.get(position).getTime());
		holder.contentTextView.setText(list.get(position).getBody());
		return convertView;
	}

	private static class ViewHolder {
		TextView phoneNumberTextView;
		TextView typeTextView = null;
		TextView dateTextView = null;
		TextView timeTextView = null;
		TextView contentTextView = null;
	}

	public void filter(String type) {
		// TODO Auto-generated method stub
		list.clear();
		if (type.equals("All")) {
			list.addAll(beans);
		} else {
			for (SMSBean bean : beans) {
				if (bean.getType().equals(type)) {
					list.add(bean);
				}
			}
		}
		notifyDataSetChanged();
	}
}
