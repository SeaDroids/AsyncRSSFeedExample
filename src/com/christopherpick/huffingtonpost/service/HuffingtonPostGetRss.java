package com.christopherpick.huffingtonpost.service;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.christopherpick.huffingtonpost.HuffingtonPostContract;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;
import android.util.Xml.Encoding;

public class HuffingtonPostGetRss {
	
	
	private static URL mUrl = null;
	private static final String HUFFINGTON_POST_RSS_FEED_URL = "http://feeds.huffingtonpost.com/huffingtonpost/raw_feed";
	private static final String ATOM = "http://www.w3.org/2005/Atom";
			

	public static void get(ContentResolver contentResolver) {
		try {
			getXML(contentResolver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static void getXML(ContentResolver resolver) throws IOException, SAXException {

		mUrl = new URL(HUFFINGTON_POST_RSS_FEED_URL);
		final ValuesList items = new ValuesList(); 
		InputStream stream = (InputStream) mUrl.getContent();	
		RootElement root = new RootElement(ATOM, "feed");
       	   	
		// End of each entry add the entire row, and create a new row. 
		root.getChild(ATOM, "entry").setEndElementListener(new EndElementListener() {
			@Override
			public void end() {
				items.rows.add(items.currentRow);
				items.currentRow = new ContentValues();
			}
		});
		
		// Add the article title
       	root.getChild(ATOM, "entry").getChild(ATOM, "title").setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				items.currentRow.put(HuffingtonPostContract.COLUMN_TITLE, body);
			}
		});
       	
       	// Get the link to the article
       	root.getChild(ATOM, "entry").getChild(ATOM, "link").setStartElementListener(new StartElementListener() {
			@Override
			public void start(Attributes attributes) {
				items.currentRow.put(HuffingtonPostContract.COLUMN_ARTICLE_LINK, attributes.getValue("href"));
			}
		});
       	
       	// Add the article ID
       	root.getChild(ATOM, "entry").getChild(ATOM, "id").setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				items.currentRow.put(HuffingtonPostContract.COLUMN_ARTICLE_ID, body);
			}
       		
       	});
       	
       	// Add the Published Date
       	root.getChild(ATOM, "entry").getChild(ATOM, "published").setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				items.currentRow.put(HuffingtonPostContract.COLUMN_PUBLISHED_DATE, body);
			}
       		
       	});
       	
       	// Add the Updated Date
       	root.getChild(ATOM, "entry").getChild(ATOM, "updated").setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				items.currentRow.put(HuffingtonPostContract.COLUMN_UPDATED_DATE, body);
			}
       		
       	});
       	
       	// Add the Summary
       	root.getChild(ATOM, "entry").getChild(ATOM, "summary").setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				items.currentRow.put(HuffingtonPostContract.COLUMN_SUMMARY, body);
			}
       		
       	});
       	
       	// Add the Author Name
       	root.getChild(ATOM, "entry").getChild(ATOM, "author").getChild(ATOM, "name").setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				items.currentRow.put(HuffingtonPostContract.COLUMN_AUTHOR_NAME, body);
			}
       		
       	});
       	
       	// Add the Author Name
       	root.getChild(ATOM, "entry").getChild(ATOM, "content").setEndTextElementListener(new EndTextElementListener() {
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
