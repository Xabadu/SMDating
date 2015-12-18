package com.supermanket.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AutoCompleteDbAdapter {

	private static String DATABASE_PATH = "";
	private static final String DATABASE_NAME = "Ciudades.sqlite";
	private static final String TABLE_NAME = "ciudad";
	private static final int DATABASE_VERSION = 1;

	private class DatabaseHelper extends SQLiteOpenHelper {

		private final Context context;
		private SQLiteDatabase database;

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			this.context = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

		}

		public void createDatabase() throws IOException{

	    	boolean dbExist = checkDatabase();

	    	if(dbExist){
	    	} else {
	        	this.getReadableDatabase();
	        	try {
	    			copyDatabase();
	    		} catch (IOException e) {
	        		throw new Error("Error copying database " + e.toString());
	        	}
	    	}
	    }

		private boolean checkDatabase() {
			//SQLiteDatabase checkDb = null;
			File checkDb = null;
			try {
				String path = DATABASE_PATH + DATABASE_NAME;
				checkDb = new File(path);
			} catch(SQLiteException e) {
				e.printStackTrace();
			}

			return checkDb.exists();
		}

		private void copyDatabase() throws IOException {
			InputStream input = context.getAssets().open(DATABASE_NAME);
			String outFileName = DATABASE_PATH + DATABASE_NAME;
			OutputStream output = new FileOutputStream(outFileName);
			byte[] buffer = new byte[1024];
			int length;
			while((length = input.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			output.flush();
			output.close();
			input.close();
		}

		public SQLiteDatabase openDatabase() throws SQLException {
			String path = DATABASE_PATH + DATABASE_NAME;
			database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
			return database;
		}

		public void close() {
			if(database != null) {
				database.close();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
	}

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private final Activity mActivity;

	public AutoCompleteDbAdapter(Activity activity) {
	    this.mActivity = activity;
	    mDbHelper = this.new DatabaseHelper(activity);
	    try {
			mDbHelper.createDatabase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    mDb = mDbHelper.openDatabase();
	}

	public void close() {
	    mDbHelper.close();
	}

	public Cursor getLocations(String constraint) throws SQLException {

	    String queryString =
	            "SELECT _id as _id, nombre, ciudad FROM " + TABLE_NAME;

	    if (constraint != null) {
	        constraint = constraint.trim() + "%";
	        queryString += " WHERE nombre LIKE ? LIMIT 10";
	    }
	    String params[] = { constraint };

	    if (constraint == null) {
	        params = null;
	    }
	    try {
	        Cursor cursor = mDb.rawQuery(queryString, params);
	        if (cursor != null) {
	            this.mActivity.startManagingCursor(cursor);
	            cursor.moveToFirst();
	            return cursor;
	        }
	    }
	    catch (SQLException e) {
	        Log.e("AutoCompleteDbAdapter", e.toString());
	        throw e;
	    }

	    return null;
	}

}
