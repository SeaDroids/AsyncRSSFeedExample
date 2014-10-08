package com.christopherpick.huffingtonpost.base;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public abstract class BaseProvider extends ContentProvider {
	
	private DbHelper helper;
	private BaseLookup lookup;

	@Override
	public boolean onCreate() {
		lookup = new BaseLookup();
		addTables(lookup);
		helper = new DbHelper(getContext(), getDatabaseName(), null, getDatabaseVersion());
		return true;
	}

	protected abstract String getDatabaseName(); 
	protected abstract int getDatabaseVersion();
	protected abstract void addTables(BaseLookup lookup);

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// use *writable* instead of readable db because helper might need to create or upgrade
		SQLiteDatabase db = helper.getWritableDatabase();
		BaseTable table = lookup.locate(uri);
		if (table != null) {
			return table.query(db, uri, projection, selection, selectionArgs, sortOrder);
		} else {
			return null;
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = helper.getWritableDatabase();
		BaseTable table = lookup.locate(uri);
		return table.update(db, uri, values, selection, selectionArgs);
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = helper.getWritableDatabase();
		BaseTable table = lookup.locate(uri);
		long id = table.insert(db, uri, values);
		return Uri.withAppendedPath(uri, String.valueOf(id));
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = helper.getWritableDatabase();
		BaseTable table = lookup.locate(uri);
		return table.delete(db, uri, selection, selectionArgs);
	}

	@Override
	public String getType(Uri uri) {
		BaseTable table = lookup.locate(uri);
		return table.getType(uri);
	}

	private class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			for(BaseTable table : lookup)
				table.onCreate(db);		
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			for(BaseTable table : lookup)
				table.onUpgrade(db, oldVersion, newVersion);
		}
	}

}
