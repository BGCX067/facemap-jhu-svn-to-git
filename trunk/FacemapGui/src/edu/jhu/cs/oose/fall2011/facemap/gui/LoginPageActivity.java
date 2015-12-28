package edu.jhu.cs.oose.fall2011.facemap.gui;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView.BufferType;
import android.widget.Toast;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientApp;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientAppException;
import edu.jhu.cs.oose.fall2011.facemap.domain.Location;
import edu.jhu.cs.oose.fall2011.facemap.domain.LocationImpl;
import edu.jhu.cs.oose.fall2011.facemap.server.LocationRetriever;

/**
 * Log in page (login.xml) which allows users to log in with registered email and passwords
 * 
 * @author Chuan Huang, Ying Dou
 * 
 */
public class LoginPageActivity extends Activity
{

	/** Name of the class, used for debugging. */
	private final String TAG = "LoginPageActivity";

	/** ClientApp object. phone model interface object. */
	private ClientApp clientApp;

	/** EditText to input email(id). */
	private EditText emailText;

	/** key to retrieve email from savedInstanceState. */
	private String emailTextKey = "emailText";

	/** EditText to input password. */
	private EditText passwdText;

	/** key to retrieve password from savedInstanceState. */
	private String passwdTextKey = "passwdText";

	/** The app. */
	private ClientAppApplication app;

	/**
	 * Retrieves location of this phone. TODO: make it really retrieve the phone's location instead of using the test
	 * setup. This LocationRetriever instance returns random locations around the JHU homewood campus
	 */
	// LocationRetriever locationRetriever = new RandomLocationRetriever(39.329542, -76.620412, 0.02, 0.02);
	LocationRetriever locationRetriever;

	/**
	 * Called when the activity is first created.
	 * Sets the view to login.xml
	 * Gets the ClientApp object from application
	 * Gets email and password editTexts from layout
	 * Retrieves saved information and sets the edit texts
	 * @param savedInstanceState the saved instance state
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		// get global client app interface
		app = (ClientAppApplication) getApplication();

		this.clientApp = app.getClientApp();
		Log.d(TAG, "Got clientApp");
		
		if (clientApp.getLoggedInUser() != null)
			clientApp.logout();

		Log.d(TAG, "No one logged in");
		
		this.emailText = (EditText) this.findViewById(R.id.emailTextAtLogin);

		this.passwdText = (EditText) this.findViewById(R.id.passwdTextAtLogin);
		
		String email = null;
		String password = null;
		if (savedInstanceState == null)
		{
			Log.d(TAG, "First time launched");
		} else
		{
			Log.d(TAG, "Something was saved");
			email = savedInstanceState.getString(this.emailTextKey);
			password = savedInstanceState.getString(this.passwdTextKey);
		}

		//speed up testing
		email = "5@mail.com";
		password = "5";
		
		emailText.setText(email, BufferType.EDITABLE);
		passwdText.setText(password, BufferType.EDITABLE);
	}

	/**
	 * button click listener
	 * Logs the user in if the email and password match a registered user, else make a notice that the log in failed
	 * 
	 * @param view the view
	 */
	public void LoginPageOkButtonClickHandler(View view)
	{
		switch (view.getId())
		{
			case R.id.okButtonAtLogin:
				// check input
				if (this.emailText.getText().length() == 0)
				{
					Toast.makeText(this, "Please don't leave Email address empty", Toast.LENGTH_LONG).show();
					return;
				} else if (this.passwdText.getText().length() == 0)
				{
					Toast.makeText(this, "Please don't leave Password empty", Toast.LENGTH_LONG).show();
					return;
				}
				String email = this.emailText.getText().toString();
				String password = this.passwdText.getText().toString();

				if (!RegisterPageActivity.isValidEmailAddress(email))
				{
					Toast.makeText(this, "invalid email", Toast.LENGTH_LONG).show();
				}

				// if successful, retrieves the location
				// login the user, navigate to menu.xml page
				try
				{
					locationRetriever = new CurrentLocation(this);
					this.clientApp.login(email, password, locationRetriever);
					Toast.makeText(this, "Login successfully", Toast.LENGTH_LONG).show();
					LoginPageActivity.this.startMenuPageActivity();
				} catch (ClientAppException e)
				{
					Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();
				}
				break;
		}
	}

	/**
	 * Moves the user to the menu page
	 */
	public void startMenuPageActivity()
	{
		Log.d(TAG, "Starting MenuPageActivity");
		Intent intent = new Intent(this, MenuPageActivity.class);
		this.startActivityForResult(intent, 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause()
	{
		super.onPause();
		Log.d(TAG, "onPause");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart()
	{
		super.onRestart();
		Log.d(TAG, "onRestart");
		clientApp.logout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume()
	{
		super.onResume();
		Log.d(TAG, "onResume");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		// this method is not guaranteed to be called, sadly,
		// but assume it will be normally, and when it is called,
		// it will be called before onStop and possibly before onPause
		super.onSaveInstanceState(outState);
		Log.d(TAG, "onSaveInstanceState");
		// take the text from the text box and save it; textboxes
		// and other built-in Views usually have their state saved
		// anyway, but this is just for an example of how to save state
		if (this.emailText != null)
		{
			outState.putString(this.emailTextKey, this.emailText.getText().toString());
		}
		if (this.passwdText != null)
		{
			outState.putString(this.passwdTextKey, this.passwdText.getText().toString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart()
	{
		super.onStart();
		Log.d(TAG, "onStart");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop()
	{
		super.onStop();
		Log.d(TAG, "onStop");
	}
	
	/**
	 * Return to main page when back is pressed
	 */
	public void onBackPressed()
	{
		Log.d(TAG, "Detected back pressed");
		Intent back = new Intent(this, MainPageActivity.class);
		this.startActivity(back);
	}

	
}
