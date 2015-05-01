package com.think.parentapp_activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.think.parentapp.R;
import com.think.parentapp_services.WebServiceManager;
import com.think.parentapp_util.AppVariables;

public class HomeActivity extends Activity implements
		android.view.View.OnClickListener,
		android.content.DialogInterface.OnClickListener {

	String response = "";
	RelativeLayout layout;
	private RadioGroup group;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppVariables.children = new ArrayList<String>();
		setContentView(R.layout.activity_home);
		layout = (RelativeLayout) findViewById(R.id.mylayout);

		new PerformBackgroundTask().execute();
	}

	private class PerformBackgroundTask extends AsyncTask<Void, Void, Void> {
		private ProgressDialog dialog = new ProgressDialog(HomeActivity.this);

		protected void onPreExecute() {
			// here you have place code which you want to show in UI thread like
			// progressbar or dialog or any layout . here i am displaying a
			// progressBar with please wait while uploading......
			dialog.setMessage(" Retreiving Child Details");
			dialog.setCancelable(false);
			dialog.show();

		}

		protected Void doInBackground(Void... params) {
			// write here the code to download or any background task.
			// // example call method which uploads files
			// .

			// fetch data
			WebServiceManager service = new WebServiceManager(
					getApplicationContext());
			response = service.retreiveIMEI();

			return null;
		}

		protected void onPostExecute(Void unused) {
			dialog.dismiss();

			// the background task is completed .You can do the code here for
			// next things
			validate();

		}

	}

	public void validate() {
		// TODO Auto-generated method stub
		if (response.equals("false")) {
			Toast.makeText(getApplicationContext(), "Invalid Credentials",
					Toast.LENGTH_SHORT).show();
			// builder.setTitle("Retry");
			// dialog();
		} else if (response.length() > 11) {
			String[] imeis = response.split(";");
			// Toast.makeText(getApplicationContext(), response,
			// Toast.LENGTH_SHORT).show();
			for (String imei : imeis) {

				AppVariables.children.add(imei);
				// /Toast.makeText(getApplicationContext(), imei,
				// Toast.LENGTH_SHORT).show();
				Log.d("TAG", "IMEI : " + imei);

			}
			// layout.setBackgroundResource(R.drawable.background);

			populateValues();

		} else {
			Toast.makeText(getApplicationContext(), "Cannot Connect To Server",
					Toast.LENGTH_SHORT).show();
			// builder.setTitle("Retry");
			// /dialog();
		}
	}

	private void populateValues() {
		// TODO Auto-generated method stub
		group = (RadioGroup) findViewById(R.id.radioGroup1);
		TextView title = (TextView) findViewById(R.id.textView1);
		title.setText("Choose A Child's IMEI Number");
		int i = 101;
		for (String imei : AppVariables.children) {
			// ids.add(i);
			RadioButton button = new RadioButton(getApplicationContext());
			button.setId(i++);
			button.setTextColor(Color.BLACK);
			button.setText(imei);
			button.setOnClickListener(this);
			group.addView(button);
		}

	}

	@Override
	public void onClick(View view) {
		RadioButton button = (RadioButton) view;
		// TODO Auto-generated method stub
		Intent intent = new Intent(getApplicationContext(),
				InfoDisplayActivity.class);
		
		intent.putExtra("IMEI", button.getText());
		startActivity(intent);
		finish();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		dialog();

	}

	protected void dialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
		builder.setMessage("Are you sure you want to exit?");
		Log.d("ID", "message set");
		builder.setCancelable(false);
		builder.setPositiveButton("Yes", this);
		builder.setNegativeButton("No", this);
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub

		if (which == DialogInterface.BUTTON_POSITIVE) {
			finish();
		} else if (which == DialogInterface.BUTTON_NEGATIVE) {
			dialog.cancel();
		}
	}

}
