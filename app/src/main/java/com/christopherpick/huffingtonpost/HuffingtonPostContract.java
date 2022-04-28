package com.christopherpick.huffingtonpost;

/**
 * @author Christopher Pick
 * 
 */

import android.net.Uri;

public class HuffingtonPostContract {
	
	// Content URI for fetching raw feeds from the content provider
	public static final Uri RSS_CONTENT_URI = Uri.parse("content://com.christopherpick.huffingtonpost/raw_feed");
	
	public static final String COLUMN_ID = "_id"; 					// Default column name for the PK in the table
	public static final String COLUMN_TITLE = "title";				// Colmun for the title 
	public static final String COLUMN_ARTICLE_ID = "article_id";	// Column for the article ID
	public static final String COLUMN_ARTICLE_LINK = "link";		// Column for the link to the article -- Missing from spec, but in XML feed
	public static final String COLUMN_PUBLISHED_DATE = "published";	// Column for the time the article was published
	public static final String COLUMN_UPDATED_DATE = "updated"; 	// Column for the time the article was last updated
	public static final String COLUMN_SUMMARY = "summary"; 			// Column for the summary of the article
	public static final String COLUMN_AUTHOR_NAME = "author_name"; 	// Column for the name of the author
//	public static final String COLUMN_AUTHOR_URI = "author_uri";	// Column for the URI for the author
	public static final String COLUMN_CONTENT = "content";			// Column for the Content for the article
	public static final String COLUMN_FAVORITE = "favorite";		// Column for saving favorite items
	
	// Default projection for the Table
	public static final String[] DEFAULT_PROJECTION = {
		COLUMN_ID,
		COLUMN_TITLE,
		COLUMN_ARTICLE_ID,
		COLUMN_ARTICLE_LINK,
		COLUMN_PUBLISHED_DATE,
		COLUMN_UPDATED_DATE,
		COLUMN_SUMMARY,
		COLUMN_AUTHOR_NAME,
	//	COLUMN_AUTHOR_URI,
		COLUMN_CONTENT,
		COLUMN_FAVORITE
	};
	
	// Intent Actions to perform on search results
	public static final String ACTION_CLEAR = "com.christopherpick.huffingtonpost.action.clear";
	public static final String ACTION_CLEAR_ALL = "com.christopherpick.huffingtonpost.action.clear";
	public static final String ACTION_GET = "com.christopherpick.huffingtonpost.action.get";

}
