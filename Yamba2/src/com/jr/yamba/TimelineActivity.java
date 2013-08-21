package com.jr.yamba;

import com.jr.bd.DbHelper;
import com.jr.bd.StatusData;

import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class TimelineActivity extends BaseActivity {
	
	private ListView myTimeline;
	private SimpleCursorAdapter adapter;
	static final String[] FROM = { DbHelper.C_CREATED_AT,DbHelper.C_USER, DbHelper.C_TEXT};
	static final int[] TO ={R.id.textCreatedAt,R.id.textUser,R.id.textText};
	
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_messages);
		myTimeline = (ListView) findViewById(R.id.myMessages);
		
		adapter = new SimpleCursorAdapter(this, R.layout.row,yamba.fetchStatusUpdates(),FROM, TO);
	    myTimeline.setAdapter(adapter);
	    Toast.makeText(this, String.format("You have %d new messages",yamba.getCountNewMessages()),Toast.LENGTH_LONG).show();
	}
}
