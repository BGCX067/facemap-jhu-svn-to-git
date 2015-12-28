package edu.jhu.cs.oose.fall2011.facemap.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * Correspond to "contacts.xml" layout page, nothing dynamic, just a navigation page
 * Allows user to navigate to either all contacts page or groups page to see contacts
 * 
 * @author Chuan Huang, Ying Dou
 *
 */
public class ContactsPageActivity extends OptionMenu {
	/**
	 * Name of the class, used for debugging
	 */
	private final String TAG = "ContactsPageActivity";
	
	/**
	 * create view, set the view to "contacts.xml"
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);
	}
	
	/**
	 * button listener for "All Contacts" button
	 * this method is added to "contacts.xml" code
	 * call the function that navigates to "all_contacts.xml" page
	 * @param view
	 */
	public void ContactsPageAllContactsButtonClickHandler(View view) {
		switch (view.getId()) {
		case R.id.allContactsButton:
			ContactsPageActivity.this.startAllContactsPageActivity();
			break;
		}
	}
	/**
	 * button listener for "Groups" button
	 * this method is added to "contacts.xml" code
	 * call the function that navigates to "groups.xml" page
	 * @param view
	 */
	public void ContactsPageGroupsButtonClickHandler(View view) {
		switch (view.getId()) {
		case R.id.groupsButton:
			ContactsPageActivity.this.startGroupsPageActivity();
			break;
		}
	}
	/** 
	 * navigate to all_contacts.xml page
	 */
	public void startAllContactsPageActivity() {
		Log.d(TAG,"Starting AllContactsPageActivity");
    	Intent intent = new Intent(this,AllContactsPageActivity.class);
    	this.startActivity(intent);
	}
	/**
	 * navigate to groups.xml page
	 */
	public void startGroupsPageActivity() {
		Log.d(TAG,"Starting GroupsPageActivity");
    	Intent intent = new Intent(this,GroupsPageActivity.class);
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
	 * Return to the menu page
	 */
	public void onBackPressed()
	{
		Log.d(TAG, "Detected back pressed");
		Intent back = new Intent(this, MenuPageActivity.class);
		this.startActivity(back);
	}
}
