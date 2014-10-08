package com.christopherpick.huffingtonpost;

import com.christopherpick.huffingtonpost.fragments.ContentFragment;
import com.christopherpick.huffingtonpost.fragments.NewsList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class HuffingtonPostActivity extends FragmentActivity implements NewsList.OnArticleSelectedListener, NewsList.OnRefreshArticlesListener {
    
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_area);

        
        /*
         * Need to check and see if this is a small screen/large screen, if fragment_container != null
         * that means this is a small screen device, and we need to inflate the fragment independently 
         * (xml does it for us for large devices such as tablets - see the layout & layout-large for news_list
         * 
         * We will also need to remember to swap this fragment with the details fragment for small devices, but on 
         * large devices we will just update the content next to the list. 
         */
        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            NewsList smallScreenFragement = new NewsList();
            smallScreenFragement.setArguments(getIntent().getExtras());
            getSupportFragmentManager()
            	.beginTransaction()
                .add(R.id.fragment_container, smallScreenFragement)
                .commit();
        }
        
        updateStories();
        
    }
    
    public void updateStories() {
    	startService(new Intent(HuffingtonPostContract.ACTION_CLEAR));
    	startService(new Intent(HuffingtonPostContract.ACTION_GET));
    }
    
    public void onRefreshArticles() {
    	updateStories();
    }
    
    
    /*
     * Interface for the news story selection updates the appropriate view & updates the content in the view 
     * (non-Javadoc)
     * @see com.christopherpick.huffingtonpost.fragments.NewsList.OnArticleSelectedListener#onArticleSelected(int)
     */
    public void onArticleSelected(long _id) {	
    	/*
    	 * Get the fragment for the content area for large screens, if it is null, then 
    	 * create a new content fragment & swap the fragments in the fragment container. 
    	 * If it is large, just udpate the content fragment w/ the correct article information.
    	 */
		ContentFragment contentFragment = (ContentFragment)
		        getSupportFragmentManager().findFragmentById(R.id.content_fragment);
		
		if (contentFragment != null) {
		    contentFragment.showArticle(_id);
		} else {
		    ContentFragment newContentFragment = new ContentFragment();
		    Bundle args = new Bundle();
		    args.putLong(HuffingtonPostContract.COLUMN_ID, _id);
		    newContentFragment.setArguments(args);
		    getSupportFragmentManager().beginTransaction()
		    	.replace(R.id.fragment_container, newContentFragment)
		    	.addToBackStack(null)
		    	.commit();
		}
    }
    
	/*
	 * Interface to choose the default fragment to show (top item) if we are using a large screen
	 * (non-Javadoc)
	 * @see com.christopherpick.huffingtonpost.fragments.NewsList.OnArticleSelectedListener#onSelectDefault(long)
	 */
    public void onSelectDefault(long _id) { 
    	
		ContentFragment contentFragment = (ContentFragment)
		        getSupportFragmentManager().findFragmentById(R.id.content_fragment);
		
		if (contentFragment != null) {
			if (contentFragment.getCurrentId() == -1) {
				contentFragment.showArticle(_id);
			}
		} 
    }
}