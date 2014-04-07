package com.androidmobile.JR;


import com.androidmobile.bd.BdGas;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class _MyAlarmasActivity extends FragmentActivity {
	
	private ListView myMessagesList;
	private TextView inicio;
	private SimpleCursorAdapter adapter;
	static final String[] FROM ={"nombre","condicion", "pvp"};
	static final int[] TO ={R.id.textCreatedAt,R.id.textUser,R.id.textText};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.my_messages);
		//myMessagesList = (ListView) findViewById(R.id.myMessages);
		//inicio = (TextView) findViewById(R.id.id_tt);
		inicio.setText("empezamos");
		BdGas bd = new BdGas(this);
		Cursor c=bd.getStatusAlertas();
		adapter = new SimpleCursorAdapter(this, R.layout.row,c,FROM, TO);
	    myMessagesList.setAdapter(adapter);
	    
	}
	
	public void inicio(View view) {

		Intent intent = new Intent(this, MainActivity.class);
		//Bundle bundle = new Bundle();
		//bundle.putString("UPV", upv);
		//bundle.putString("nombre", "");
		//intent.putExtras(bundle);
		startActivity(intent);
		
	}

}
