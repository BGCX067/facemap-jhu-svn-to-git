package edu.jhu.cs.oose.fall2011.facemap.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * global option menu implementation.
 * user can naviages to the menu when pressing the menu button on the phone
 * contains four options: contacts, locate, my info and friend requests
 * @author Ying Dou
 *
 */
public class OptionMenu extends Activity {
	private final String TAG = "OptionMenu";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}
	
	/**
	 * Creates options menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	/**
	 * trigger activities when menu items are pressed
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.contacts:
	    	startContactsPageActivity();
	    	break;
	    case R.id.locate:
	    	startLocatePageActivity();
	    	break;
	    case R.id.myInfo:
	    	startMyInfoPageActivity();
	    	break;
	    case R.id.requests:

	    	startFriendRequestsPageActivity();
	    	break;
	    }
	    return true;
	}
	
	/**
	 * start contacts page
	 */
	public void startContactsPageActivity() {
		Log.d(TAG,"Starting ContactsPageActivity");
    	Intent intent = new Intent(this,ContactsPageActivity.class);
    	this.startActivity(intent);
	}
	
	/**
	 * start locate page
	 */
	public void startLocatePageActivity() {
		Log.d(TAG,"Starting LocatePageActivity");
    	Intent intent = new Intent(this,LocatePageActivity.class);
    	this.startActivity(intent);
	}
	
	/**
	 * start my info page
	 */
	public void startMyInfoPageActivity() {
		Log.d(TAG,"Starting MyInfoPageActivity");
    	Intent intent = new Intent(this,MyInfoPageActivity.class);
    	this.startActivity(intent);
	}
	  
	/**
	 * navigate to Friend Requests Page
	 */
	public void startFriendRequestsPageActivity() {
		Log.d(TAG,"Starting FriendRequestsPageActivity");
    	Intent intent = new Intent(this,FriendRequestsPageActivity.class);
    	this.startActivity(intent);
	}
}
