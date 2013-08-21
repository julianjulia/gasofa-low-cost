package com.jr.yamba;

import java.util.List;
import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import winterwell.jtwitter.TwitterException;
import android.app.Application;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import com.jr.bd.StatusData;

public class YambaApplication extends Application implements
		OnSharedPreferenceChangeListener {
	private static final String TAG = YambaApplication.class.getSimpleName();
	public Twitter twitter;
	private SharedPreferences prefs;
	private StatusData statusData;
	private int countNewMessajes;

	private boolean serviceRunning;
	
	
	
	public boolean isServiceRunning() {
		return serviceRunning;
	}

	public void setServiceRunning(boolean serviceRunning) {
		this.serviceRunning = serviceRunning;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		this.prefs.registerOnSharedPreferenceChangeListener(this);
		this.statusData = new StatusData(this);
		new FetchFromTwitter().execute();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		Log.i(TAG, "OnTerminate");
	}

	public Twitter getTwitter() {
		if (twitter == null) {
			String username, password, apiRoot;
			username = prefs.getString("username", "");
			password = prefs.getString("password", "");
			apiRoot = prefs.getString("apiRoot",
					"http://yamba.marakana.com/api");

			twitter = new Twitter(username, password);
			twitter.setAPIRootUrl(apiRoot);
		}
		return twitter;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		twitter = null;

	}

	public StatusData getStatusData() {
		return statusData;
	}

	public SharedPreferences getPrefs() {
		return prefs;
	}

	public Cursor fetchStatusUpdates() {
		
		Log.d(TAG,"Fetching status updates");
		Twitter twitter = this.getTwitter();
		if(twitter==null){
			Log.d(TAG,"Twitter connection info not initialized");
			return null;
		}
		try{
			List<Status> statusUpdates = getFriendsTimeline();
			countNewMessajes = statusUpdates.size();
			long latesStatusCreatedAtTime = this.getStatusData().getLatestStatusCreatedAtTime();
			int count =0;
			ContentValues values = new ContentValues();
			for (Status status : statusUpdates){
				values.put(StatusData.C_ID,status.getId());
				long createdAt = status.getCreatedAt().getTime();
				values.put(StatusData.C_CREATED_AT,createdAt);
				values.put(StatusData.C_TEXT,status.getText());
				values.put(StatusData.C_USER, status.getUser().getName());
				Log.d(TAG,"Got update with id " + status.getId() + ".Saving");
				this.getStatusData().insertOrIgnoreTimelineFriends(values);
				if (latesStatusCreatedAtTime < createdAt){
					count++;
				}
			}
				Log.d(TAG, count > 0 ? "Got "+ count + " status updates " : "No new status updates");
				return this.getStatusData().getStatusUpdatesTimelineFriends();
		}catch (RuntimeException e) {
			Log.e(TAG,"Failed to fetch status updates",e);
			return null;
		
			}
		}
		
		
	

	public int getCountNewMessages() {
		// TODO Auto-generated method stub
		return countNewMessajes;
	}

	private List<Status> status;

	private List<Status> getFriendsTimeline(){
		return status;		
	}
	
	public void setFriendsTimeline(List<Twitter.Status> status){
	this.status = status;
	}
	
	class FetchFromTwitter extends AsyncTask<String,Integer,List<Twitter.Status>> {
		//private ProgressDialog Dialog = new ProgressDialog(getBaseContext());
		protected List<Twitter.Status> doInBackground(String... statuses){
			try{
				List<Twitter.Status> status = getTwitter().getFriendsTimeline();
				Log.d(TAG,String.format("fetching %d messages", status.size()));
				return status;
			}catch (TwitterException e){
				Log.e(TAG,e.toString());
				e.printStackTrace();
				return null;
			}
		}
		protected void onPreExecute() {
			//Dialog.setMessage("Espere...");
			//Dialog.show();
			super.onPreExecute();
		}

		protected void onPostExecute(List<Twitter.Status> status){
			//Dialog.dismiss();
			setFriendsTimeline(status);
		}
	
}

	public int getCountNewMessajes() {
		return countNewMessajes;
	}

	public void setCountNewMessajes(int countNewMessajes) {
		this.countNewMessajes = countNewMessajes;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
