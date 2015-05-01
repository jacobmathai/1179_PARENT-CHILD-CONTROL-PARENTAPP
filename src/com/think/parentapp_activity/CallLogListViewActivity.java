package com.think.parentapp_activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.smartparenting.beans.CallBean;
import com.think.parentapp.R;
import com.think.parentapp.R.style;

import com.think.parentapp_adapter.CallLogAdapter;
import com.think.parentapp_services.WebServiceManager;
import com.think.parentapp_util.AppConstants;
import com.think.parentapp_util.AppVariables;
import com.think.parentapp_util.CallUtils;

public class CallLogListViewActivity extends Activity implements
		OnItemSelectedListener, OnItemClickListener, OnClickListener {

	EditText fromDateEditText = null;
	EditText toDateEditText = null;
	EditText numberSearchEditText = null;
	ImageView fromCalenderImageView = null;
	ImageView toCalenderImageView = null;
	ImageView serchContactImageView = null;

	Calendar fromCal;
	Calendar toCalendar;
	Calendar activeDate;
	
	
	 private Calendar currentDate;
	  private Calendar fromDate;

	String imei = "";

	String fromdate = "";
	String todate = "";

	int fromday;
	int frommonth;
	int fromyear;
	int today;
	int tomonth;
	int toyear;
	int id;

	Spinner callTypeSpinner = null;
	ListView calLogListView = null;
	CallLogAdapter callLogAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_log);
		setUI();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.callmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		/*
		 * case R.id.action_serch: return true;
		 */
		case R.id.byDate:

			showDatePickerDialog();
			/*
			 * Intent intent = new Intent(CallLogListViewActivity.this,
			 * FilteredLogActivity.class); startActivity(intent);
			 */
			return true;

		case R.id.byname:

			searchByNumberDialog();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void searchByNumberDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(
				CallLogListViewActivity.this);
		builder.setTitle("Search By Number");
		builder.setIcon(R.drawable.ic_search);
		final EditText input = new EditText(CallLogListViewActivity.this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		input.setLayoutParams(lp);
		input.setHint("Enter Phone Number");
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		builder.setView(input);
		builder.setPositiveButton("Search",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String count = input.getText()
								.toString();

						if (count.length() != 10) {
							input.setError("Please input a valid number");
							Toast.makeText(getApplicationContext(),
									"Enter valid count", Toast.LENGTH_LONG)
									.show();
						} else {
							new SearchCallsByNumber().execute(imei, count);
						}
					}
				});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();

					}
				});
		builder.show();
	}

	private class SearchCallsByNumber extends
			AsyncTask<String, Void, ArrayList<CallBean>> {

		ProgressDialog dialog = new ProgressDialog(CallLogListViewActivity.this);

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialog.setMessage("Loading");
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected ArrayList<CallBean> doInBackground(String... params) {
			// TODO Auto-generated method stub
			ArrayList<CallBean> calls = new WebServiceManager(
					CallLogListViewActivity.this).retreiveCallsByNumber(
					params[0], params[1]);
			Log.d("LIST", "" + calls);
			return calls;
		}

		@Override
		protected void onPostExecute(ArrayList<CallBean> result) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			if (result != null) {
				callLogAdapter = new CallLogAdapter(
						CallLogListViewActivity.this, result);
				calLogListView.setAdapter(callLogAdapter);
			} else {
				Toast.makeText(getApplicationContext(), "Network error",
						Toast.LENGTH_LONG).show();
			}

		}

	}

	private void showDatePickerDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(
				CallLogListViewActivity.this);
		LayoutInflater inflater = LayoutInflater
				.from(CallLogListViewActivity.this);
		View view = inflater.inflate(R.layout.list_log_new, null);
		fromDateEditText = (EditText) view
				.findViewById(R.id.selectFromDateEditText);
		toDateEditText = (EditText) view
				.findViewById(R.id.selectToDateEditText);
		numberSearchEditText = (EditText) view
				.findViewById(R.id.phoneNumberTextView);
		fromCalenderImageView = (ImageView) view
				.findViewById(R.id.selectFromDateCalender);
		toCalenderImageView = (ImageView) view
				.findViewById(R.id.selectToDateCalender);

		//
		fromCal = Calendar.getInstance();
		toCalendar = Calendar.getInstance();
		fromday = fromCal.get(Calendar.DAY_OF_MONTH);
		frommonth = fromCal.get(Calendar.MONTH);
		fromyear = fromCal.get(Calendar.YEAR);
		//
		today = toCalendar.get(Calendar.DAY_OF_MONTH);
		tomonth = toCalendar.get(Calendar.MONTH);
		toyear = toCalendar.get(Calendar.YEAR);
		//
		builder.setView(view);
		fromCalenderImageView.setOnClickListener(this);
		toCalenderImageView.setOnClickListener(this);

		builder.setPositiveButton("Get Details",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Toast.makeText(
								getApplicationContext(),
								"FROM DATE = " + fromdate + " TO DATE = "
										+ todate, Toast.LENGTH_LONG).show();
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
						try {
							Date from = sdf.parse(fromdate);
							Date to = sdf.parse(todate);
							Date date = sdf.parse(sdf.format(new Date()));
							if (from.after(to) || from.after(date)) {
								Toast.makeText(getApplicationContext(), "invalid From date", Toast.LENGTH_SHORT).show();
								//fromDateEditText.setError("Invalid From Date");
							} else if (to.before(from) || to.after(date)) {
								Toast.makeText(getApplicationContext(), "invalid todate", Toast.LENGTH_SHORT).show();
								//toDateEditText.setError("Invalid To Date");
							} else if(fromdate.equals("") && !todate.equals("")){
		//						fromDateEditText.setError("Please insert from date");
								Toast.makeText(getApplicationContext(), "Please insert from date", Toast.LENGTH_SHORT).show();
							} else if(!fromdate.equals("") && to.equals("")) {
								Toast.makeText(getApplicationContext(), "Please insert to date", Toast.LENGTH_SHORT).show();
								//toDateEditText.setError("Please insert to date");
							} else if (fromdate.equals("") && todate.equals("")) {
								Toast.makeText(getApplicationContext(), "Please insert from and to date", Toast.LENGTH_SHORT).show();
//								fromDateEditText.setError("Please insert from date");
//								toDateEditText.setError("Please insert to date");
							} else {
								new GetCallDetailsAsyncTask().execute(imei, fromdate,
										todate);
							}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						
						
						Log.d("TAG", imei+" "+ fromdate+" "+todate);
					}
				});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		builder.show();
	}

	private class GetCallDetailsAsyncTask extends
			AsyncTask<String, Void, ArrayList<CallBean>> {
		ProgressDialog dialog = new ProgressDialog(CallLogListViewActivity.this);

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialog.setMessage("Loading");
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected ArrayList<CallBean> doInBackground(String... params) {
			// TODO Auto-generated method stub
			ArrayList<CallBean> calls = new WebServiceManager(
					CallLogListViewActivity.this).retreiveCallsByDate(
					params[0], params[1], params[2]);
			Log.d("LIST", "" + calls);
			return calls;
		}

		@Override
		protected void onPostExecute(ArrayList<CallBean> result) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			if (result != null) {
				callLogAdapter = new CallLogAdapter(
						CallLogListViewActivity.this, result);
				calLogListView.setAdapter(callLogAdapter);
			} else {
				Toast.makeText(getApplicationContext(), "Network error",
						Toast.LENGTH_LONG).show();
			}

		}

	}

	private void setUI() {

		//

		callTypeSpinner = (Spinner) findViewById(R.id.callTypeSpinner);
		calLogListView = (ListView) findViewById(R.id.callLogListView);
		// set up spinner
		callTypeSpinner.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getResources()
						.getStringArray(R.array.call_type)));
		callTypeSpinner.setOnItemSelectedListener(this);
		calLogListView.setOnItemClickListener(this);

		setUpListView("All");
	}

	private void setUpListView(String type) {
		imei = getIntent().getExtras().getString("IMEI");

		// call webservices
		new GetCallDetails().execute(imei);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		String item = (String) arg0.getItemAtPosition(arg2);
		if (fromdate.equals("") && todate.equals("")) {
			if (callLogAdapter != null) {
				if (item.equals("All")) {
					callLogAdapter.filter("All");
				} else if (item.equals("Missed Call")) {
					callLogAdapter.filter(CallUtils.TYPE_MISSED);
				} else if (item.equals("Receive Call")) {
					callLogAdapter.filter(CallUtils.TYPE_RECEIVED);
				} else if (item.equals("Rejected Call")) {
					callLogAdapter.filter(CallUtils.TYPE_REJECTED);
				} else {
					callLogAdapter.filter(CallUtils.TYPE_OUTGOING);
				}
			}/*
			 * else if (fromdate.equals("")) {
			 * //fromDateEditText.setError("Select From date"); } else if
			 * (todate.equals("")) {
			 * //toDateEditText.setError("Select To date"); }
			 */else {
				Toast.makeText(getApplicationContext(), "hai",
						Toast.LENGTH_LONG).show();
			}

		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	private class GetCallDetails extends
			AsyncTask<String, Void, ArrayList<CallBean>> {
		ProgressDialog dialog = new ProgressDialog(CallLogListViewActivity.this);

		@Override
		protected ArrayList<CallBean> doInBackground(String... params) {
			// TODO Auto-generated method stub
			ArrayList<CallBean> calls = new WebServiceManager(
					CallLogListViewActivity.this).retreiveCalls(params[0]);
			Log.d("LIST", "" + calls);
			return calls;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialog.setMessage("Loading...!");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected void onPostExecute(ArrayList<CallBean> result) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			if (result != null) {
				callLogAdapter = new CallLogAdapter(
						CallLogListViewActivity.this, result);
				calLogListView.setAdapter(callLogAdapter);
			} else {
				Toast.makeText(getApplicationContext(), "Network error",
						Toast.LENGTH_LONG).show();
			}

		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		Object itemAtPosition = arg0.getItemAtPosition(position);
		if (itemAtPosition instanceof CallBean) {
			CallBean callBean = (CallBean) itemAtPosition;
			if (!callBean.getSetAudio().equals("1")) {
				Intent intent = new Intent();
				intent.setAction(android.content.Intent.ACTION_VIEW);

				intent.setDataAndType(
						Uri.parse(AppConstants.STREAM_URL
								+ callBean.getSetAudio()), "audio/*");
				startActivity(intent);
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == fromCalenderImageView.getId()) {
			showDialog(0);
		} else if (v.getId() == toCalenderImageView.getId()) {
			showDialog(1);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		if(id == 0){
			Log.d("TAG", "id = 0");
		return new DatePickerDialog(this, datePickerListener, fromyear,
				frommonth, fromday);
		}else {
			Log.d("TAG", "id = ");
			return new DatePickerDialog(this, datePickerListener1, fromyear,
					frommonth, fromday);
		}
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			fromDateEditText.setText(selectedDay + "-" + (selectedMonth + 1)
					+ "-" + selectedYear);
			fromdate = fromDateEditText.getText().toString();
			AppVariables.TODATE = 0;
			Log.d("DATE", "fromdate " + fromdate);

			

		}
	};
	private DatePickerDialog.OnDateSetListener datePickerListener1 = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			
			toDateEditText.setText(selectedDay + "-" + (selectedMonth + 1)
					+ "-" + selectedYear);

			todate = toDateEditText.getText().toString();
			Log.d("DATE", "todate " + todate);

		}
	};


}
