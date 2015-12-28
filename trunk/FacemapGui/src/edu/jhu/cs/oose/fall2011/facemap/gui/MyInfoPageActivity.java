package edu.jhu.cs.oose.fall2011.facemap.gui;

import edu.jhu.cs.oose.fall2011.facemap.client.ClientApp;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * my_info.xml page.
 * Display current logged in user's information: including name, phone, email, custom message, and image
 * 
 * @author Ying Dou & Chuan Huang
 * 
 */
public class MyInfoPageActivity extends OptionMenu
{

	/**
	 * Name of the class, used for debugging
	 */
	private final String TAG = "MyInfoActivity";
	
	/**
	 * requestCode for startActivityForResult
	 */
	private int EDIT_INFO = 1;
	
	/**
	 * requestCode for startActivityForResult
	 */
	private int SET_VISIBILITY = 2;

	/**
	 * ClientApp object. phone model interface object
	 */
	private ClientApp clientApp;

	/**
	 * display the user name
	 */
	private TextView nameDisplay;
	/**
	 * display phone number
	 */
	private TextView phoneDisplay;
	/**
	 * display email
	 */
	private TextView emailDisplay;
	/**
	 * display custom message
	 */
	private TextView customMessageDisplay;
	/**
	 * display visibility
	 */
	private TextView visibilityDisplay;

	/**
	 * creates view to my_info.xml.
	 * gets ClientApp object from application.
	 * get user information from model and set the texts
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_info);

		ClientAppApplication app = (ClientAppApplication) getApplication();
		clientApp = app.getClientApp();

		// get information from model
		String phone = clientApp.getLoggedInUser().getSelf().getPhoneNumber();
		String email = clientApp.getLoggedInUser().getSelf().getEmail();
		String name = clientApp.getLoggedInUser().getSelf().getName();
		String customMessage = clientApp.getLoggedInUser().getSelf().getMessage();

		if (customMessage == null || customMessage.equals(""))
			customMessage = "Hello world, I am " + name;

		nameDisplay = (TextView) this.findViewById(R.id.myNameString);
		phoneDisplay = (TextView) this.findViewById(R.id.phoneDisplay);
		emailDisplay = (TextView) this.findViewById(R.id.emailDisplay);
		customMessageDisplay = (TextView) this.findViewById(R.id.customMessageDisplay);
		visibilityDisplay = (TextView) this.findViewById(R.id.visibility);

		// set the texts
		nameDisplay.setText(name);
		phoneDisplay.setText(phone);
		emailDisplay.setText(email);
		customMessageDisplay.setText(customMessage);
		phoneDisplay.setTextColor(Color.BLUE);
		emailDisplay.setTextColor(Color.BLUE);
		customMessageDisplay.setTextColor(Color.BLUE);
	}

	/**
	 * logout button click listener
	 * calls the method that log user out
	 * @param view
	 */
	public void MyInfoLogoutButtonClickHandler(View view)
	{
		switch (view.getId())
		{
			case R.id.logoutButton:
				MyInfoPageActivity.this.startLogoutActivity();
				break;
		}
	}

	/**
	 * picture button click listener
	 * (not implemented)
	 * @param view
	 */
	public void MyInfoPagePictureButtonClickHandler(View view)
	{
		switch (view.getId())
		{
			case R.id.myPictureImageButton:
				// browse picture
				break;
		}
	}

	/**
	 * Edit button click listener
	 * calls the function that start edit_my_info.xml page
	 * @param view
	 */
	public void EditButtonClickHandler(View view)
	{
		switch (view.getId())
		{
			case R.id.editMyInfoButton:
				MyInfoPageActivity.this.startEditMyInfoActivity();
				break;
		}
	}

	/**
	 * Set Visibility button click listener
	 * calls the method that navigates to set_visibility.xml
	 * @param view
	 */
	public void SetVisibilityClickHandler(View view)
	{
		switch (view.getId())
		{
			case R.id.setVisibilityButton:
				MyInfoPageActivity.this.startSetVisibilityActivity();
				break;
		}
	}

	/**
	 * navigate to LogoutActivity
	 */
	public void startLogoutActivity()
	{
		Log.d(TAG, "Starting LogoutActivity");
		Intent intent = new Intent(this, LoginPageActivity.class);
		this.startActivity(intent);
	}

	/**
	 * navigate to edit_my_info.xml
	 * wait for results
	 */
	public void startEditMyInfoActivity()
	{
		Log.d(TAG, "Starting EditMyInfoActivity");
		Intent intent = new Intent(this, EditMyInfoPageActivity.class);
		this.startActivityForResult(intent, EDIT_INFO);
	}

	/**
	 * navigate to set_visibility.xml
	 * wait for result
	 */
	public void startSetVisibilityActivity()
	{
		Log.d(TAG, "Starting SetVisibilityActivity");
		Intent intent = new Intent(this, SetVisibilityPageActivity.class);
		this.startActivityForResult(intent, SET_VISIBILITY);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		Log.d(TAG, "onPause");
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
		Log.d(TAG, "onRestart");
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		Log.d(TAG, "onResume");
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		Log.d(TAG, "onStart");
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		Log.d(TAG, "onStop");
	}

	/**
	 * get information from edit my info page or set visibility page and update the current page
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		//coming from edit info
		if (requestCode == EDIT_INFO)
		{
			String noChange = data.getStringExtra("no change");
			if (noChange.equals("change"))
			{
				String name = data.getStringExtra("name");
				String customMessage = data.getStringExtra("message");

				nameDisplay.setText(name);
				customMessageDisplay.setText(customMessage);
			}
		}
		//coming from set visibility
		else if (requestCode == SET_VISIBILITY)
		{
			String visibleStatus = data.getStringExtra("visibility");
			this.visibilityDisplay.setText(visibleStatus);
		}
		Log.d(TAG, "Returned");
	}

	/**
	 * go back to menu page when back button pressed
	 */
	public void onBackPressed()
	{
		Log.d(TAG, "Detected back pressed");
		Intent back = new Intent(this, MenuPageActivity.class);
		this.startActivity(back);
	}
}
