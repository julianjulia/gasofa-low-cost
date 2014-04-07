package com.androidmobile.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReceiverBoot extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		  Intent serviceIntent = new Intent();
		  serviceIntent.setAction("com.androidmobile.service.MyService");
		  arg0.startService(serviceIntent);
		  
		  /*
		  Intent i = new Intent(arg0, MainActivity.class);
		   i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		   arg0.startActivity(i);
		    */  

	}

}
