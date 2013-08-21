package com.jr.yamba;

import com.jr.bd.StatusData;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatusActivity extends BaseActivity implements OnClickListener,
		TextWatcher {
	private static final String TAG = "StatusActivity";
	TextView titleStatus;
	TextView updateStatus;
	EditText hintText;
	Button buttonUpdate;
	Twitter twitter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.status);

		titleStatus = (TextView) findViewById(R.id.titleYamba);
		hintText = (EditText) findViewById(R.id.hintText);
		buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
		buttonUpdate.setOnClickListener(this);

		updateStatus = (TextView) findViewById(R.id.titleStatus);

		updateStatus.setTextColor(Color.GREEN);
		updateStatus.setText(Integer.toString(140));
		hintText.addTextChangedListener(this);

		// twitter =new Twitter("student","password");
		// twitter.setAPIRootUrl("http://yamba.marakana.com/api");

	}

	class PostToTwitter extends AsyncTask<String, Integer, Twitter.Status> {
		private ProgressDialog Dialog = new ProgressDialog(StatusActivity.this);

		@Override
		protected Twitter.Status doInBackground(String... statuses) {
			try {
				Twitter.Status status = yamba.getTwitter().updateStatus(
						statuses[0]);
				return status;
			} catch (TwitterException e) {
				Log.e(TAG, e.toString());
				return null;
			}

		}

		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPreExecute() {
			Dialog.setMessage("Espere...");
			Dialog.show();
			super.onPreExecute();
		}

		protected void onPostExecute(Twitter.Status status) {
			Dialog.dismiss();
			if (status != null) {
				Toast.makeText(StatusActivity.this, status.text,
						Toast.LENGTH_LONG).show();

				ContentValues values = new ContentValues();
				values.put(StatusData.C_ID, System.currentTimeMillis());
				long createAt = status.getCreatedAt().getTime();
				values.put(StatusData.C_CREATED_AT, createAt);
				values.put(StatusData.C_TEXT, status.getText());
				values.put(StatusData.C_USER, status.getUser().getName());
				Log.d(TAG, "Got update with id " + status.getId() + ". Saving");
				yamba.getStatusData().insertOrIgnore(values);
				

			}

			else {
				Toast.makeText(StatusActivity.this, "Error......",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.buttonUpdate:
			String status = hintText.getText().toString();
			new PostToTwitter().execute(status);
			Log.d(TAG, "onClick- se a presionado el botton");
			break;

		default:
			// code..
			break;
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		int count = 140 - s.length();
		updateStatus.setText(Integer.toString(count));
		updateStatus.setTextColor(Color.GREEN);
		if (count < 10)
			updateStatus.setTextColor(Color.YELLOW);
		if (count < 0)
			updateStatus.setTextColor(Color.RED);

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

}
