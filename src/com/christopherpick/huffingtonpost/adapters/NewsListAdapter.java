package com.christopherpick.huffingtonpost.adapters;

import com.christopherpick.huffingtonpost.HuffingtonPostContract;
import com.christopherpick.huffingtonpost.R;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NewsListAdapter extends CursorAdapter {

	public NewsListAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView articleTitle = (TextView) view.getTag(R.id.article_title);
		TextView articleSummary = (TextView) view.getTag(R.id.article_summary);
		articleTitle.setText(cursor.getString(cursor.getColumnIndex(HuffingtonPostContract.COLUMN_TITLE)));
		articleSummary.setText(cursor.getString(cursor.getColumnIndex(HuffingtonPostContract.COLUMN_SUMMARY)));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewgroup) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.news_list_row, null);
		view.setTag(R.id.article_title, view.findViewById(R.id.article_title));
		view.setTag(R.id.article_summary, view.findViewById(R.id.article_summary));
		return view;
	}

}
