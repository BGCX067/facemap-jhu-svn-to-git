package edu.jhu.cs.oose.fall2011.facemap.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
/**
 * Page allows user to select who and how to locate; including nearby friends, friends in groups, friends from contacts
 * @author Ying Dou, Chuan Huang (Kevin)
 *
 */
public class LocatePageActivity extends OptionMenu {
	/**
	 * Name of the class, used for debugging
	 */
	private final String TAG = "LocatePageActivity";
	
	/** Constant to indicate we're selecting contacts from all contacts */
	final int CONTACTS = 1;
	
	/** Cosntant to indicate we're selecting contacts from groups */
	final int GROUPS = 2;
	
	/** Constant for choosing range for map */
	private final int MAP = 3;
	
	
	/**
	 * create view to locate.xml
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locate);
	}
	
	/**
	 * button click handler for locate nearby
	 * @param view
	 */
	public void LocatePageNearbyButtonClickHandler(View view) {
		switch (view.getId()) {
		case R.id.locateNearbyFriendsButton:
			LocatePageActivity.this.startLocateRangePageActivity();
			break;
		}
	}

	/**
	 * button click handler for locate contacts
	 * @param view
	 */
	public void LocatePageContactsButtonClickHandler(View view) {
		switch (view.getId()) {
		case R.id.locateContactsButton:
			LocatePageActivity.this.startSelectContactActivity(CONTACTS);
			break;
		}
	}
	
	/**
	 * button click handler for locating groups
	 * @param view
	 */
	public void LocatePageGroupsButtonClickHandler(View view) {
		switch (view.getId()) {
		case R.id.locateGroupsButton:
			LocatePageActivity.this.startSelectContactActivity(GROUPS);
			break;
		}
	}
	
	/**
	 * navigate to next page select contact activity to allow user to select what to display
	 */
	public void startSelectContactActivity(int key) {
		Log.d(TAG,"Ready to Select Contacts");
    	Intent intent = new Intent(this,SelectContactForMapActivity.class);
    	intent.putExtra("key", key);
    	this.startActivity(intent);
	}
	
	/**
	 * Start the next page locate range page activity to allow user to define initial range.
	 */
	public void startLocateRangePageActivity()
	{
		Log.d(TAG,"Locate Range Activity");
    	Intent intent = new Intent(this,LocateRangePageActivity.class);
    	intent.putExtra("key", MAP);
    	this.startActivity(intent);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG,"onDestroy");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG,"onPause");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(TAG,"onRestart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG,"onResume");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG,"onStart");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG,"onStop");
	}

	/**
	 * Return to menu.xml page when back key is pressed
	 */
	public void onBackPressed()
	{
		Log.d(TAG, "Detected back pressed");
		Intent back = new Intent(this, MenuPageActivity.class);
		this.startActivity(back);
	}
}
