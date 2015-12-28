package edu.jhu.cs.oose.fall2011.facemap.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * reg_login.xml page activity.
 * Main page allows user to select between registering a new account or log into existing account
 * 
 * @author Ying Dou
 * 
 */
public class MainPageActivity extends Activity
{

	/**
	 * Name of the class, used for debugging
	 */
	private final String TAG = "MainPageActivity";

	/**
	 * Called when the activity is first created.
	 * set view to reg_login.xml
	 * gets buttons from layout and set listeners
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reg_login);

		Button registerButton = (Button) this.findViewById(R.id.regButton);
		Button loginButton = (Button) this.findViewById(R.id.loginButton);

		if (registerButton != null)
		{
			registerButton.setOnClickListener(new OnClickListener()
			{

				public void onClick(View v)
				{
					MainPageActivity.this.startRegisterPageActivity();
				}
			});
		}

		if (loginButton != null)
		{
			loginButton.setOnClickListener(new OnClickListener()
			{

				public void onClick(View v)
				{
					MainPageActivity.this.startLoginPageActivity();
				}
			});
		}
	}

	/**
	 * navigate to register page
	 */
	public void startRegisterPageActivity()
	{
		Log.d(TAG, "Starting RegisterPageActivity");
		Intent intent = new Intent(this, RegisterPageActivity.class);

		// intent.putExtra

		this.startActivity(intent);
	}

	/**
	 * navigate to login page
	 */
	public void startLoginPageActivity()
	{
		Log.d(TAG, "Starting LoginPageActivity");
		Intent intent = new Intent(this, LoginPageActivity.class);
		this.startActivity(intent);
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

}