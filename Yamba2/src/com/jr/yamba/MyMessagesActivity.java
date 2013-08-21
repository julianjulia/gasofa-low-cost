package com.jr.yamba;

import com.jr.bd.DbHelper;
import com.jr.bd.StatusData;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ListView;

public class MyMessagesActivity extends BaseActivity {
	
	private ListView myMessagesList;
	private SimpleCursorAdapter adapter;
	static final String[] FROM ={DbHelper.C_CREATED_AT,DbHelper.C_USER, DbHelper.C_TEXT};
	static final int[] TO ={R.id.textCreatedAt,R.id.textUser,R.id.textText};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_messages);
		myMessagesList = (ListView) findViewById(R.id.myMessages);
		
		adapter = new SimpleCursorAdapter(this, R.layout.row,yamba.getStatusData().getStatusUpdates(),FROM, TO);
	    myMessagesList.setAdapter(adapter);
	}
	


}
