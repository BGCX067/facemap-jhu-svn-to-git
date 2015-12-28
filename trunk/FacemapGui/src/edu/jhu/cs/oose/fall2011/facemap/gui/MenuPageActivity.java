package edu.jhu.cs.oose.fall2011.facemap.gui;

import edu.jhu.cs.oose.fall2011.facemap.client.ClientApp;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * menu.xml page activity
 * Main menu page where user selects from contacts, locate, my information, and friend requests
 * @author Ying Dou, Chuan Huang (Kevin)
 *
 */
public class MenuPageActivity extends OptionMenu {

	/**
	 * Name of the class, used for debugging
	 */
	private final String TAG = "MenuPageActivity";
	
	/**
	 * ClientApp object. phone model interface object
	 */
	private ClientApp clientApp;
	
	/**
	 * create view to menu.xml
	 * Gets ClientApp object from application
	 * set the friend request button text dynamically according to the number of requests
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        
        ClientAppApplication app = (ClientAppApplication)getApplication();
        this.clientApp = app.getClientApp();
        
        Button friendRequestButton = (Button) findViewById(R.id.friendRequestButton);
        final int numOfRequests = clientApp.getLoggedInUser().getFriendRequestsReceived().size();

        if (friendRequestButton != null) {
        	friendRequestButton.setText("Friend Requests (" + numOfRequests + ")");
        	friendRequestButton.setTextSize((float) 20);
        	
        	friendRequestButton.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (numOfRequests == 0) {
			        		Toast.makeText(MenuPageActivity.this, "no friendRequest", Toast.LENGTH_LONG).show();
			        	} else {
			        		MenuPageActivity.this.startFriendRequestsPageActivity();
			        	}
				}
			});        	
        }       
	}
	/**
	 * Contacts button click listener
	 * calls the function that navigates to contacts.xml page
	 * @param view
	 */
	public void MenuPageContactsButtonClickHandler(View view) {
		switch (view.getId()) {
		case R.id.contactsButtonAtMenu:
			MenuPageActivity.this.startContactsPageActivity();
			break;
		}
	}
	/**
	 * Locate button click listener
	 * calls the function that navigates to locate.xml
	 * @param view
	 */
	public void MenuPageLocateButtonClickHandler(View view) {
		switch (view.getId()) {
		case R.id.locateButtonAtMenu:
			MenuPageActivity.this.startLocatePageActivity();
			break;
		}
	}
	/**
	 * My Info button click listener
	 * calls the funciton that navigates to my_info.xml
	 * @param view
	 */
	public void MenuPageMyInfoButtonClickHandler(View view) {
		switch (view.getId()) {
		case R.id.myInfoButtonAtMenu:
			MenuPageActivity.this.startMyInfoPageActivity();
			break;
		}
	}
	
	/**
	 * navigate to Contacts page
	 */
	public void startContactsPageActivity() {
		Log.d(TAG,"Starting ContactsPageActivity");
    	Intent intent = new Intent(this,ContactsPageActivity.class);
    	this.startActivity(intent);
	}
	/**
	 * navigate to Locate page
	 */
	public void startLocatePageActivity() {
		Log.d(TAG,"Starting LocatePageActivity");
    	Intent intent = new Intent(this,LocatePageActivity.class);
    	this.startActivity(intent);
	}
	/**
	 * navigate to My Info page
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
	 * Return to login page when back is pressed
	 */
	public void onBackPressed()
	{
		Log.d(TAG, "Detected back pressed");
		Intent logout = new Intent(this, LoginPageActivity.class);
		this.startActivity(logout);
	}
}
