package com.christopherpick.huffingtonpost.service;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.res.Resources;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;
import android.util.Xml.Encoding;

import com.christopherpick.huffingtonpost.HuffingtonPostContract;
import com.christopherpick.huffingtonpost.R;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HuffingtonPostGetRss {
	
	
	private static URL mUrl = null;
	//private static final String HUFFINGTON_POST_RSS_FEED_URL = "http://feeds.huffingtonpost.com/huffingtonpost/raw_feed";
	private static final String HUFFINGTON_POST_RSS_FEED_URL = "https://www.huffpost.com/section/front-page/feed";


	private static final String ATOM = "";
			

	public static void get(ContentResolver contentResolver, Resources resource) {
		try {
			getXML(contentResolver, resource);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static void getXML(ContentResolver resolver, Resources resource) throws IOException, SAXException {

		// Loading example XML from resources now, instead of online. Was having
		// issues with loading a stream, and not really necessary for this app.

//		mUrl = new URL(HUFFINGTON_POST_RSS_FEED_URL);
//		final ValuesList items = new ValuesList();
//		InputStream stream = (InputStream) mUrl.getContent();


		final ValuesList items = new ValuesList();

		InputStream stream = resource.openRawResource(R.raw.huffington);

		RootElement root = new RootElement("", "rss");
		// End of each channel add the entire row, and create a new row. 
		root.getChild(ATOM, "channel").getChild(ATOM, "item").setEndElementListener(new EndElementListener() {
			@Override
			public void end() {
				items.rows.add(items.currentRow);
				items.currentRow = new ContentValues();
				android.util.Log.e("CTP", "HMM");
			}
		});
		
		// Add the article title
       	root.getChild(ATOM, "channel").getChild(ATOM, "item").getChild(ATOM, "title").setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				items.currentRow.put(HuffingtonPostContract.COLUMN_TITLE, body);
			}
		});

//		public static final String COLUMN_TITLE = "title";				// Colmun for the title
//		public static final String COLUMN_ARTICLE_ID = "article_id";	// Column for the article ID
//		public static final String COLUMN_ARTICLE_LINK = "link";		// Column for the link to the article -- Missing from spec, but in XML feed
//		public static final String COLUMN_PUBLISHED_DATE = "published";	// Column for the time the article was published
//		public static final String COLUMN_UPDATED_DATE = "updated"; 	// Column for the time the article was last updated
//		public static final String COLUMN_SUMMARY = "summary"; 			// Column for the summary of the article
//		public static final String COLUMN_AUTHOR_NAME = "author_name"; 	// Column for the name of the author
//		public static final String COLUMN_AUTHOR_URI = "author_uri";	// Column for the URI for the author
//		public static final String COLUMN_CONTENT = "content";			// Column for the Content for the article
//		public static final String COLUMN_FAVORITE = "favorite";		// Column for saving favorite items



		// Get the link to the article
       	root.getChild(ATOM, "channel").getChild(ATOM, "item").getChild(ATOM, "link").setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				items.currentRow.put(HuffingtonPostContract.COLUMN_ARTICLE_LINK, body);
			}
		});
       	
       	// Add the article ID
       	root.getChild(ATOM, "channel").getChild(ATOM, "item").getChild(ATOM, "id").setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				items.currentRow.put(HuffingtonPostContract.COLUMN_ARTICLE_ID, body);
			}
       		
       	});
       	
       	// Add the Published Date
       	root.getChild(ATOM, "channel").getChild(ATOM, "item").getChild(ATOM, "pubDate").setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				items.currentRow.put(HuffingtonPostContract.COLUMN_PUBLISHED_DATE, body);
			}
       		
       	});
       	
       	// Add the Updated Date
       	root.getChild(ATOM, "channel").getChild(ATOM, "item").getChild(ATOM, "updated").setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				items.currentRow.put(HuffingtonPostContract.COLUMN_UPDATED_DATE, body);
			}
       		
       	});
       	
       	// Add the Summary
       	root.getChild(ATOM, "channel").getChild(ATOM, "item").getChild(ATOM, "description").setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				if (body != null) {
					items.currentRow.put(HuffingtonPostContract.COLUMN_SUMMARY, body);
				}
			}
       		
       	});
       	
       	// Add the Author Name
       	root.getChild(ATOM, "channel").getChild(ATOM, "item").getChild(ATOM, "author").getChild(ATOM, "name").setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				items.currentRow.put(HuffingtonPostContract.COLUMN_AUTHOR_NAME, "unknown");
			}
       		
       	});
       	
       	// Add the Author Name
       	root.getChild(ATOM, "channel").getChild(ATOM, "item").getChild(ATOM, "content").setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				items.currentRow.put(HuffingtonPostContract.COLUMN_CONTENT, body);
			}
       		
       	});
       	
       	/*
       	 * Bulk insert just loops through the list one at a time, not that fast,
       	 * could use an InsertHelper to speed it up, and direct DB write vs via the 
       	 * content resolver. 
       	 */
       	Xml.parse(stream, Encoding.UTF_8, root.getContentHandler());
       	resolver.bulkInsert(HuffingtonPostContract.RSS_CONTENT_URI, items.getRows());

       	
	}

}
