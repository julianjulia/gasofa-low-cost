package com.jr.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class StatusData {

	private static final String TAG = StatusData.class.getSimpleName();

	static final int VERSION = 1;
	static final String DATABASE = "timeline.db";
	static final String TABLE = "timeline";
	static final String TABLE_FRIENDS = "timeline_friends";
	public static final String C_ID = "_id";
	public static final String C_CREATED_AT = "created_at";
	public static final String C_TEXT = "txt";
	public static final String C_USER = "user";

	private static final String GET_ALL_ORDER_BY = C_CREATED_AT + " DESC";

	private static final String[] MAX_CREATED_AT_COLUMNS = { "max("
			+ StatusData.C_CREATED_AT + ")" };

	private static final String[] DB_TEXT_COLUMNS = { C_TEXT };

	private final DbHelper dbhelper;

	public StatusData(Context context) {

		dbhelper = new DbHelper(context);

	}

	public void close() {
		this.dbhelper.close();
	}

	public void insertOrIgnore(ContentValues values) {
		Log.d(TAG, "insertOrIgnore on " + values);
		SQLiteDatabase db = this.dbhelper.getWritableDatabase();
		try {
			db.insertWithOnConflict(TABLE, null, values,
					SQLiteDatabase.CONFLICT_IGNORE);
		} finally {
			db.close();
		}

	}

	public Cursor getStatusUpdates() {
		SQLiteDatabase db = this.dbhelper.getReadableDatabase();
		return db.query(TABLE, null, null, null, null, null, GET_ALL_ORDER_BY);
	}

	public long getLatestStatusCreatedAtTime() {
		SQLiteDatabase db = this.dbhelper.getReadableDatabase();
		try {
			Cursor cursor = db.query(TABLE, MAX_CREATED_AT_COLUMNS, null, null,
					null, null, null);
			try {
				return cursor.moveToNext() ? cursor.getLong(0) : Long.MIN_VALUE;
			} finally {
				cursor.close();
			}
		} finally {
			db.close();
		}
	}

	public String getStatusTextByUser(String user) {
		SQLiteDatabase db = this.dbhelper.getReadableDatabase();
		try {
			Cursor cursor = db.query(TABLE, DB_TEXT_COLUMNS, C_USER + "="
					+ user, null, null, null, null);
			try {
				return cursor.moveToNext() ? cursor.getString(0) : null;
			} finally {
				cursor.close();
			}
		} finally {
			db.close();
		}
	}
	
	public void insertOrIgnoreTimelineFriends(ContentValues values) {
		Log.d(TAG, "insertOrIgnoreTimelineFriends on " + values);
		SQLiteDatabase db = this.dbhelper.getWritableDatabase();
		try {
			db.insertWithOnConflict(TABLE_FRIENDS, null, values,
					SQLiteDatabase.CONFLICT_IGNORE);
		} finally {
			db.close();
		}

	}
	
	public Cursor getStatusUpdatesTimelineFriends() {
		SQLiteDatabase db = this.dbhelper.getReadableDatabase();
		return db.query(TABLE_FRIENDS, null, null, null, null, null, GET_ALL_ORDER_BY);
	}

	
}
