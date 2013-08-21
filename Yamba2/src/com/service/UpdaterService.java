package com.service;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

import com.jr.yamba.YambaApplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdaterService extends Service {

	private static final String TAG = "UpdaterService";

	static final int DELAY = 60000; // UN MINUTO
	private boolean runflag = false;
	private Updater updater;
	private YambaApplication yamba;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.yamba = (YambaApplication)getApplication();
		this.updater = new Updater();
		Log.d(TAG, "onCreated");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);

		this.runflag = true;
		this.updater.start();
		// llamada a al metodo setServiceRunning en yambaaplication		
		((YambaApplication) super.getApplication()).setServiceRunning(true);
		Log.d(TAG, "onStarted");
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		this.runflag = false;
		this.updater.interrupt();
		this.updater = null;
		((YambaApplication) super.getApplication()).setServiceRunning(false);
		Log.d(TAG, "onDestroyed");
	}

	private class Updater extends Thread {
		List<Twitter.Status> timeline;
		public Updater() {
			super("UpdaterService-Updater");
		}

		public void run() {
			UpdaterService updaterService = UpdaterService.this;
			while (updaterService.runflag) {
				// some work goes here .....
				Log.d(TAG, "Updater running");
				try {
					try{
						timeline =yamba.getTwitter().getFriendsTimeline();
					}catch(TwitterException e){
						Log.e(TAG,"Failed to connect to twitter sercie",e);
					}
					for (Twitter.Status status: timeline){
						Log.d(TAG,String.format("%s: %s",status.user.name,status.text));
					}
				
					Log.d(TAG, "Updater ran");
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					updaterService.runflag = false;
				}
			}
		}

	}

}
