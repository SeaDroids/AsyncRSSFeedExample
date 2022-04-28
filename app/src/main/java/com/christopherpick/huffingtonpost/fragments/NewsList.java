package com.christopherpick.huffingtonpost.fragments;

import android.app.Activity;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.christopherpick.huffingtonpost.HuffingtonPostContract;
import com.christopherpick.huffingtonpost.R;
import com.christopherpick.huffingtonpost.adapters.NewsListAdapter;

public class NewsList extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, OnItemClickListener {

	OnArticleSelectedListener mSelectedArticleCallback;
	OnRefreshArticlesListener mRefreshArticlesListener;
	private NewsListAdapter mAdapter;
	private ListView mListView;
	
    public interface OnArticleSelectedListener {
        public void onArticleSelected(long _id);
        public void onSelectDefault(long _id);
    }
    
	public interface OnRefreshArticlesListener {
		public void onRefreshArticles();
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAdapter = new NewsListAdapter(getActivity(), null, true);
        getLoaderManager().initLoader(0, null, this);
		getActivity().getContentResolver().registerContentObserver(HuffingtonPostContract.RSS_CONTENT_URI, true, contentObserver);
    }

    /*
     * this contentObserver looks for the data in the database to update, so when it first loads, it will show data in the content
     * area on the large screen devices.
     */
    private ContentObserver contentObserver = new ContentObserver(null) {
		/* (non-Javadoc)
		 * @see android.database.ContentObserver#onChange(boolean)
		 */
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
	    	Cursor cursor = mAdapter.getCursor();
			if (cursor != null) {
	    		if (cursor.moveToFirst()) {
			    	mSelectedArticleCallback.onSelectDefault(cursor.getLong(cursor.getColumnIndexOrThrow(HuffingtonPostContract.COLUMN_ID)));
	    		}
	    	}
		}
    	
    };

    /* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.news_list, container, false);
        mListView = (ListView) contentView.findViewById(R.id.news_list);
        
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(contentView.findViewById(R.id.empty_view));
        mListView.setOnItemClickListener(this);

        return contentView;
	}


	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mSelectedArticleCallback = (OnArticleSelectedListener) activity;
            mRefreshArticlesListener = (OnRefreshArticlesListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " you forgot to implement an interface");
        }
    }
    
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mSelectedArticleCallback.onArticleSelected(id);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(
        		getActivity(), 
        		HuffingtonPostContract.RSS_CONTENT_URI,
                HuffingtonPostContract.DEFAULT_PROJECTION, 
                null, 
                null,
                null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (cursor != null) {
			cursor.setNotificationUri(getActivity().getContentResolver(), HuffingtonPostContract.RSS_CONTENT_URI);
	    	mAdapter.swapCursor(cursor);
	    	
	    	if (cursor != null) {
	    		if (cursor.moveToFirst()) {
			    	mSelectedArticleCallback.onSelectDefault(cursor.getLong(cursor.getColumnIndexOrThrow(HuffingtonPostContract.COLUMN_ID)));
	    		}
	    	}
		}
		
	}
	
	
	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.swapCursor(null);		
	}
	
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	if (menu.size() == 0) {
    		inflater.inflate(R.menu.news, menu);
    	}
    }
    
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
	     switch (item.getItemId()) {
	         case R.id.menu_refresh:
	        	 mRefreshArticlesListener.onRefreshArticles();
	             break;
	      }
	      return super.onOptionsItemSelected(item);
	  }


}
