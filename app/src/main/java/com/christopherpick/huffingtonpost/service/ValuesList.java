package com.christopherpick.huffingtonpost.service;

import android.content.ContentValues;

import java.util.ArrayList;

public class ValuesList {
	public ContentValues currentRow = new ContentValues(); 
	public ArrayList<ContentValues> rows = new ArrayList<ContentValues>();

	public ContentValues[] getRows() {
		return rows.toArray(new ContentValues[rows.size()]);
	}
}
