package com.christopherpick.huffingtonpost.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.christopherpick.huffingtonpost.HuffingtonPostContract;
import com.christopherpick.huffingtonpost.base.BaseTable;

public class HuffingtonPostTable extends BaseTable {
	
	/* (non-Javadoc)
	 * @see com.christopherpick.huffingtonpost.base.BaseTable#insert(android.database.sqlite.SQLiteDatabase, android.net.Uri, android.content.ContentValues)
	 */
	@Override
	public long insert(SQLiteDatabase db, Uri uri, ContentValues values) {
		
		String[] shortProjection = new String[] { 
				HuffingtonPostContract.COLUMN_ID, 
				HuffingtonPostContract.COLUMN_ARTICLE_ID,
				HuffingtonPostContract.COLUMN_FAVORITE
		};

		String selection = HuffingtonPostContract.COLUMN_ARTICLE_ID + " like ? and " 
						 + HuffingtonPostContract.COLUMN_FAVORITE + " = ?";
		String[] selectionArgs = new String[] 
				{ values.getAsString(HuffingtonPostContract.COLUMN_ARTICLE_ID),
				  "1" };
		Cursor checkCursor = db.query(getTableName(), shortProjection, selection, selectionArgs, null, null, null);

		if (checkCursor != null) {
			checkCursor.moveToFirst();
			if (checkCursor.getCount() > 0) {
				// This item already exists in the db as a favorite, need to just update it vs inserting it. 
				Long _id = checkCursor.getLong(checkCursor.getColumnIndex(HuffingtonPostContract.COLUMN_ID));
				String updateSelection = HuffingtonPostContract.COLUMN_ID + "=?";
				String[] updateSelectionArgs = new String[] { String.valueOf(_id) };
				update(db, uri, values, updateSelection, updateSelectionArgs);
				checkCursor.close();
				return _id;
			} 
			checkCursor.close();
		}
		return super.insert(db, uri, values);
	}

	private static final String TABLE_NAME = "raw_feed";
	
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// rss create sql
		String sql = "CREATE TABLE " + TABLE_NAME + " (" +
		HuffingtonPostContract.COLUMN_ID + " integer primary key autoincrement, " +
		HuffingtonPostContract.COLUMN_TITLE + " text, " +
		HuffingtonPostContract.COLUMN_ARTICLE_ID + " text, " +
		HuffingtonPostContract.COLUMN_ARTICLE_LINK + " text, " +
		HuffingtonPostContract.COLUMN_PUBLISHED_DATE + " text, " +
		HuffingtonPostContract.COLUMN_UPDATED_DATE + " text, " +
		HuffingtonPostContract.COLUMN_SUMMARY + " text, " +
		HuffingtonPostContract.COLUMN_AUTHOR_NAME + " text, " +
		HuffingtonPostContract.COLUMN_AUTHOR_URI + " text, " +
		HuffingtonPostContract.COLUMN_CONTENT + " text, " +
		HuffingtonPostContract.COLUMN_FAVORITE + " integer default 0" +
		");";
		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
}
