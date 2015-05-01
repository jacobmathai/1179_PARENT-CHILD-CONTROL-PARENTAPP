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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.smartparenting.beans.SMSBean;
import com.think.parentapp.R;
import com.think.parentapp_adapter.SMSAdapter;
import com.think.parentapp_services.WebServiceManager;
import com.think.parentapp_util.AppVariables;
import com.think.parentapp_util.SmsUtil;

public class SmsLogListViewActivity extends Activity implements
		OnItemSelectedListener, OnClickListener {

	EditText fromDateEditText = null;
	EditText toDateEditText = null;
	EditText numberSearchEditText = null;
	ImageView fromCalenderImageView = null;
	ImageView toCalenderImageView = null;
	ImageView serchContactImageView = null;

	Calendar fromCal;
	Calendar toCalendar;

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

	Spinner smsTypeSpinner = null;
	ListView smsLogListView = null;
	private SMSAdapter smsAdapter;

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
				SmsLogListViewActivity.this);
		builder.setTitle("Search By Number");
		builder.setIcon(R.drawable.ic_search);
		final EditText input = new EditText(SmsLogListViewActivity.this);
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
						String count = input.getText().toString();

						if (count.equals("")) {
							input.setError("Please input a valid number");
							Toast.makeText(getApplicationContext(),
									"Enter 	Number", Toast.LENGTH_LONG).show();
						} else {
							new SearchCallsByNumber().execute(imei, "" + count);
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

	private void showDatePickerDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SmsLogListViewActivity.this);
		LayoutInflater inflater = LayoutInflater
				.from(SmsLogListViewActivity.this);
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
						// Toast.makeText(
						// getApplicationContext(),
						// "FROM DATE = " + fromdate + " TO DATE = "
						// + todate, Toast.LENGTH_LONG).show();
						SimpleDateFormat sdf = new SimpleDateFormat(
								"dd-MM-yyyy");
						try {
							Date from = sdf.parse(fromdate);
							Date to = sdf.parse(todate);
							Date date = sdf.parse(sdf.format(new Date()));
							if (from.after(to) || from.after(date)) {
								Toast.makeText(getApplicationContext(),
										"invalid From date", Toast.LENGTH_SHORT)
										.show();
								// fromDateEditText.setError("Invalid From Date");
							} else if (to.before(from) || to.after(date)) {
								Toast.makeText(getApplicationContext(),
										"invalid todate", Toast.LENGTH_SHORT)
										.show();
								// toDateEditText.setError("Invalid To Date");
							} else if (fromdate.equals("")
									&& !todate.equals("")) {
								// fromDateEditText.setError("Please insert from date");
								Toast.makeText(getApplicationContext(),
										"Please insert from date",
										Toast.LENGTH_SHORT).show();
							} else if (!fromdate.equals("") && to.equals("")) {
								Toast.makeText(getApplicationContext(),
										"Please insert to date",
										Toast.LENGTH_SHORT).show();
								// toDateEditText.setError("Please insert to date");
							} else if (fromdate.equals("") && todate.equals("")) {
								Toast.makeText(getApplicationContext(),
										"Please insert from and to date",
										Toast.LENGTH_SHORT).show();
								// fromDateEditText.setError("Please insert from date");
								// toDateEditText.setError("Please insert to date");
							} else {
								new GetSMSDetailsAsyncTask().execute(imei,
										fromdate, todate);
							}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		builder.setCancelable(false);
		builder.show();
	}

	private class GetSMSDetailsAsyncTask extends
			AsyncTask<String, Void, ArrayList<SMSBean>> {
		ProgressDialog dialog = new ProgressDialog(SmsLogListViewActivity.this);

		@Override
		protected ArrayList<SMSBean> doInBackground(String... params) {
			// TODO Auto-generated method stub
			ArrayList<SMSBean> sms = new WebServiceManager(
					SmsLogListViewActivity.this).retreiveSMSbyDate(params[0],
					params[1], params[2]);
			return sms;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialog.setMessage("Loading...!");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();

		}

		@Override
		protected void onPostExecute(ArrayList<SMSBean> result) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			if (result != null) {
				smsAdapter = new SMSAdapter(SmsLogListViewActivity.this, result);
				smsLogListView.setAdapter(smsAdapter);
			}

		}
	}

	private class SearchCallsByNumber extends
			AsyncTask<String, Void, ArrayList<SMSBean>> {

		ProgressDialog dialog = new ProgressDialog(SmsLogListViewActivity.this);

		@Override
		protected ArrayList<SMSBean> doInBackground(String... params) {
			// TODO Auto-generated method stub
			ArrayList<SMSBean> sms = new WebServiceManager(
					SmsLogListViewActivity.this).retreiveSMSbyNumber(params[0],
					params[1]);
			return sms;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialog.setMessage("Loading...!");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();

		}

		@Override
		protected void onPostExecute(ArrayList<SMSBean> result) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			if (result != null) {
				smsAdapter = new SMSAdapter(SmsLogListViewActivity.this, result);
				smsLogListView.setAdapter(smsAdapter);
			}

		}

	}

	private void setUI() {
		smsLogListView = (ListView) findViewById(R.id.callLogListView);
		smsTypeSpinner = (Spinner) findViewById(R.id.callTypeSpinner);

		// setup spinner
		smsTypeSpinner.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getResources()
						.getStringArray(R.array.sms_type)));

		//
		smsTypeSpinner.setOnItemSelectedListener(this);

		setUpListView("All");
	}

	private void setUpListView(String type) {
		// TODO Auto-generated method stub
		String imei = getIntent().getExtras().getString("IMEI");
		new GetSmsDetails().execute(imei);

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		String item = (String) arg0.getItemAtPosition(arg2);
		if (smsAdapter != null) {
			if (item.equals("All")) {
				smsAdapter.filter("All");
			} else if (item.equals("Received")) {
				smsAdapter.filter(SmsUtil.INCOMING_MESSAGES);
			} else {
				smsAdapter.filter(SmsUtil.TYPE_OUTGOING);
			}
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	private class GetSmsDetails extends
			AsyncTask<String, Void, ArrayList<SMSBean>> {
		ProgressDialog dialog = new ProgressDialog(SmsLogListViewActivity.this);

		@Override
		protected ArrayList<SMSBean> doInBackground(String... params) {
			// TODO Auto-generated method stub
			ArrayList<SMSBean> sms = new WebServiceManager(
					SmsLogListViewActivity.this).retreiveSMS(params[0]);
			return sms;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialog.setMessage("Loading...!");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();

		}

		@Override
		protected void onPostExecute(ArrayList<SMSBean> result) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			if (result != null) {
				smsAdapter = new SMSAdapter(SmsLogListViewActivity.this, result);
				smsLogListView.setAdapter(smsAdapter);
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
