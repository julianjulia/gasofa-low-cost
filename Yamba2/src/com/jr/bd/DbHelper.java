package com.jr.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
	static final String TAG ="DbHelper";
	static final String DB_NAME ="timeline.db";
	static final int    DB_VERSION =2;
	static final String TABLE = "timeline";
	static final String TABLE_FRIENDS = "timeline_friends";
	static final String C_ID =BaseColumns._ID;
	public static final String C_CREATED_AT ="created_at";
	static final String C_SOURCE="source";
	public static final String C_TEXT="txt";
	public static final String C_USER="user";
	Context context;
	
	public DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql ="create table " + TABLE + " ("+ C_ID + " int primay key, "
	+ C_CREATED_AT + " int, " + C_USER + " text, " + C_TEXT + " text )";
		
		db.execSQL(sql);
		
		sql ="create table " + TABLE_FRIENDS + " ("+ C_ID + " int primay key, "
				+ C_CREATED_AT + " int, " + C_USER + " text, " + C_TEXT + " text )";
					
		db.execSQL(sql);
		Log.d(TAG, "onCreated sql "+ sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int versionAnterior,int versionNueva) {
		
		db.execSQL("drop table if exists " + TABLE);
		db.execSQL("drop table if exists " + TABLE_FRIENDS);
		Log.d(TAG,"onUpdated");
		onCreate(db);

	}

}
