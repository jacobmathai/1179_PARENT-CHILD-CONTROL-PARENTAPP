package com.think.parentapp_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.think.parentapp.R;
import com.think.parentapp_util.SendTextMessage;

public class InfoDisplayActivity extends Activity implements OnClickListener {

	Button callLog;
	Button msgLog;
	Button urlLog;
	private String imei;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_display);

		callLog = (Button) findViewById(R.id.button2);
		msgLog = (Button) findViewById(R.id.button1);
		urlLog = (Button) findViewById(R.id.button3);

		callLog.setOnClickListener(this);
		msgLog.setOnClickListener(this);
		urlLog.setOnClickListener(this);

		imei = getIntent().getExtras().getString("IMEI");
		/*
		 * Toast.makeText(getApplicationContext(), imei, Toast.LENGTH_SHORT)
		 * .show();
		 */

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (view.getId()) {
		case R.id.button2:

			intent = new Intent(getApplicationContext(),
					CallLogListViewActivity.class);
			break;
		case R.id.button1:
			intent = new Intent(getApplicationContext(),
					SmsLogListViewActivity.class);
			break;
		case R.id.button3:
			intent = new Intent(getApplicationContext(),
					URLLogListViewActivity.class);
			break;

		}
		intent.putExtra("IMEI", imei);
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
		startActivity(intent);
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.item_message) {
			dialogue();
		}
		return super.onOptionsItemSelected(item);
	}

	private void dialogue() {
		// TODO Auto-generated method stub
		final EditText editText = new EditText(this);
		editText.setHint("Phone");
		editText.setGravity(Gravity.CENTER);
		editText.setInputType(InputType.TYPE_CLASS_PHONE);
		final AlertDialog alertDialog = new AlertDialog.Builder(this)
				.setView(editText).setTitle("Phone")
				.setPositiveButton("OK", null)
				.setNegativeButton("Cancel", null).create();

		alertDialog.setOnShowListener(new OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				// TODO Auto-generated method stub
				Button okButton = alertDialog
						.getButton(AlertDialog.BUTTON_POSITIVE);

				okButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (editText.getText().toString().equals("")) {
							editText.setError("Enter phone no");
						} else if (editText.getText().toString().length() != 10) {
							editText.setError("Enter a valid no");
						} else {
							new SendTextMessage().send(editText.getText()
									.toString());
							alertDialog.dismiss();
						}
					}
				});

			}
		});
		alertDialog.show();
	}
}
