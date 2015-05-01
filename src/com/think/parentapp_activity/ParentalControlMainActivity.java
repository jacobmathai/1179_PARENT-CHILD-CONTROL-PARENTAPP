package com.think.parentapp_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.think.parentapp.R;
import com.think.parentapp_services.WebServiceManager;
import com.think.parentapp_util.AppVariables;

public class ParentalControlMainActivity extends Activity implements
		OnClickListener {

	private LayoutInflater inflater;
	private View message;
	String response = "";
	private String password;
	private String username;
	private Builder builder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parental_control_main);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		builder = new Builder(ParentalControlMainActivity.this);
		builder.setTitle("Login");

		dialog();
	}

	private void dialog() {
		// TODO Auto-generated method stub

		message = inflater.inflate(R.layout.login, null);

		builder.setView(message);
		builder.setPositiveButton("Enter", this);
		builder.setNegativeButton("Exit", this);
		builder.setCancelable(false);
		AlertDialog dialog = builder.create();
		dialog.show();

	}

	private class PerformBackgroundTask extends AsyncTask<Void, Void, Void> {
		private ProgressDialog dialog = new ProgressDialog(
				ParentalControlMainActivity.this);

		protected void onPreExecute() {
			// here you have place code which you want to show in UI thread like
			// progressbar or dialog or any layout . here i am displaying a
			// progressBar with please wait while uploading......
			dialog.setMessage(" Logging In............");
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
			response = service.loginCheck(username, password);

			return null;
		}

		protected void onPostExecute(Void unused) {
			dialog.dismiss();

			// the background task is completed .You can do the code here for
			// next things
			validate();

		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		if (which == DialogInterface.BUTTON_POSITIVE) {
			EditText uName = (EditText) message.findViewById(R.id.searchByNumberEditText);
			EditText pWord = (EditText) message.findViewById(R.id.editText2);
			username = uName.getText().toString();
			password = pWord.getText().toString();
			new PerformBackgroundTask().execute();
		} else {
			finish();
		}

	}

	public void validate() {
		// TODO Auto-generated method stub
		if (response.equals("false")) {
			Toast.makeText(getApplicationContext(), "Invalid Credentials",
					Toast.LENGTH_SHORT).show();
			builder.setTitle("Retry");
			dialog();
		} else if (response.equals("true")) {
			Toast.makeText(getApplicationContext(), "Login Successful",
					Toast.LENGTH_SHORT).show();
			AppVariables.username = username;
			Intent intent = new Intent(getApplicationContext(),
					HomeActivity.class);
			overridePendingTransition(0, 0);

			intent.addFlags(Intent.FLAG_FROM_BACKGROUND);
			startActivity(intent);
			finish();
		} else {
			Toast.makeText(getApplicationContext(), "Cannot Connect To Server",
					Toast.LENGTH_SHORT).show();
			builder.setTitle("Retry");
			dialog();
		}
	}

}
