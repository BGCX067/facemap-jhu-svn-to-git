package edu.jhu.cs.oose.fall2011.facemap.gui;

import java.util.Date;

import edu.jhu.cs.oose.fall2011.facemap.client.ClientApp;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientAppException;
import edu.jhu.cs.oose.fall2011.facemap.domain.Location;
import edu.jhu.cs.oose.fall2011.facemap.domain.LocationImpl;
import edu.jhu.cs.oose.fall2011.facemap.server.LocationRetriever;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView.BufferType;
import android.widget.Toast;

/**
 * register.xml page activity Allows user to register a new account with name, email, password
 * 
 * @author Ying Dou, Chuan Huang (Kevin)
 * 
 */
public class RegisterPageActivity extends Activity
{

	/**
	 * Name of the class, used for debugging
	 */
	private final String TAG = "RegisterPageActivity";

	/**
	 * ClientApp object. phone model interface object
	 */
	private ClientApp clientApp;
	/**
	 * EditText to input email
	 */
	private EditText emailText;
	/**
	 * key to retrieve email from savedInstanceState
	 */
	private String emailTextKey = "emailText";
	/**
	 * EditText to input name
	 */
	private EditText nameText;
	/**
	 * key to retrieve name from savedInstanceState
	 */
	private String nameTextKey = "nameText";
	/**
	 * EditText to input password
	 */
	private EditText passwdText;
	/**
	 * key to retrieve password from savedInstanceState
	 */
	private String passwdTextKey = "passwdText";
	/**
	 * EditText to input password again
	 */
	private EditText confirmPasswdText;
	/**
	 * key to retrieve password from savedInstanceState
	 */
	private String confirmPasswdTextKey = "confirmPasswdText";

	/**
	 * Retrieves location of this phone.
	 */
	LocationRetriever locationRetriever = new CurrentLocation(this);

	/**
	 * Called when the activity is first created. sets the view to register.xml gets ClientApp object from application
	 * sets the edit text objects sets the texts if information is entered
	 * */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		ClientAppApplication app = (ClientAppApplication) getApplication();
		this.clientApp = app.getClientApp();

		this.emailText = (EditText) this.findViewById(R.id.emailTextAtRegister);

		this.nameText = (EditText) this.findViewById(R.id.inputYourNameText);

		this.passwdText = (EditText) this.findViewById(R.id.passwdTextAtRegister);

		this.confirmPasswdText = (EditText) this.findViewById(R.id.confirmPasswdText);

		String email = null;
		String name = null;
		String password = null;
		String confirmPassword = null;

		if (savedInstanceState == null)
		{
			Log.d(TAG, "First time launched");
		} else
		{
			Log.d(TAG, "Something was saved");
			email = savedInstanceState.getString(this.emailTextKey);
			name = savedInstanceState.getString(this.nameTextKey);
			password = savedInstanceState.getString(this.passwdTextKey);
			confirmPassword = savedInstanceState.getString(this.confirmPasswdTextKey);
		}
		
		//to speed up testing
		email = "5@mail.com";
		name = "Person5";
		password = "5";
		confirmPassword = "5";

		emailText.setText(email, BufferType.EDITABLE);
		nameText.setText(name, BufferType.EDITABLE);
		passwdText.setText(password, BufferType.EDITABLE);
		confirmPasswdText.setText(confirmPassword, BufferType.EDITABLE);
	}

	/**
	 * button listener. retrieve email, name and password, and register a new account
	 * 
	 * @param view
	 */
	public void registerPageOkButtonClickHandler(View view)
	{
		switch (view.getId())
		{
			case R.id.okButtonAtRegister:
				// test input format
				if (this.emailText.getText().length() == 0)
				{
					Toast.makeText(this, "Please don't leave Email address empty", Toast.LENGTH_LONG).show();
					return;
				} else if (this.nameText.getText().length() == 0)
				{
					Toast.makeText(this, "Please don't leave Name empty", Toast.LENGTH_LONG).show();
					return;
				} else if (this.passwdText.getText().length() == 0)
				{
					Toast.makeText(this, "Please don't leave Password empty", Toast.LENGTH_LONG).show();
					return;
				} else if (this.confirmPasswdText.getText().length() == 0)
				{
					Toast.makeText(this, "Please don't leave Confirm Password empty", Toast.LENGTH_LONG).show();
					return;
				}

				// get information from inputs
				String email = this.emailText.getText().toString();
				String name = this.nameText.getText().toString();
				String password = this.passwdText.getText().toString();
				String confirmPassword = this.confirmPasswdText.getText().toString();
				// test email and pssword
				if (!isValidEmailAddress(email))
				{
					Toast.makeText(this, "Invalid Email", Toast.LENGTH_LONG).show();
					return;
				}
				if (!password.equals(confirmPassword))
				{
					Toast.makeText(this, "Passwords don't match", Toast.LENGTH_LONG).show();
					return;
				}

				// get phoneNo from phone
				String phoneNo = this.getMy10DigitPhoneNumber();
				Log.d(TAG, "phone number: " + phoneNo);

				if (phoneNo == null)
				{
					Toast.makeText(this, "No phone number found", Toast.LENGTH_LONG).show();
					return;
				}
				// register user
				try
				{
					this.clientApp.register(email, phoneNo, name, password);
					Log.d(TAG, "Success Registration");
					// if successful
					Toast.makeText(this, "Account created successfully\nLogin now", Toast.LENGTH_LONG).show();

				} catch (ClientAppException e)
				{
					Log.d(TAG, "Error registration");
					Toast.makeText(this, "Failed to create an account", Toast.LENGTH_LONG).show();
					return;
				}
				
				//duplicate registration
				try
				{
					this.clientApp.register(email, phoneNo, name, password);
					Log.d(TAG, "Success Registration 2");
					// if successful
					Toast.makeText(this, "Account created successfully\nLogin now", Toast.LENGTH_LONG).show();

				} catch (ClientAppException e)
				{
					Log.d(TAG, "Error registration 2");
					Toast.makeText(this, "Failed to create an account", Toast.LENGTH_LONG).show();
					return;
				}
				
				// login the user
				try
				{
					this.clientApp.login(email, password, locationRetriever);
					Toast.makeText(this, "Login successfully", Toast.LENGTH_LONG).show();
					Log.d(TAG, "Success Login");
					RegisterPageActivity.this.startMenuPageActivity();
				} catch (ClientAppException e)
				{
					Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();
					Log.d(TAG, "Failed Login: " + e.getMessage());
				}
				break;

		}
	}

	/**
	 * First step to get phone number from the phone
	 * 
	 * @return intermediate string
	 */
	private String getMyPhoneNumber()
	{
		TelephonyManager mTelephonyMgr;
		mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		return mTelephonyMgr.getLine1Number();
	}

	/**
	 * get phone number
	 * 
	 * @return phone number String
	 */
	private String getMy10DigitPhoneNumber()
	{
		String s = getMyPhoneNumber();
		return s.substring(1);
	}

	/**
	 * navigate to menu.xml
	 */
	public void startMenuPageActivity()
	{
		Log.d(TAG, "Starting MenuPageActivity");
		Intent intent = new Intent(this, MenuPageActivity.class);
		this.startActivity(intent);
	}

	/**
	 * test whether input has valid email format
	 * 
	 * @param email
	 * @return true/false
	 */
	public static boolean isValidEmailAddress(String email)
	{
		String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		return email.matches(EMAIL_REGEX);
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
		if (this.confirmPasswdText != null)
		{
			outState.putString(this.confirmPasswdTextKey, this.confirmPasswdText.getText().toString());
		}
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
	 * Return to main page when back is pressed
	 */
	public void onBackPressed()
	{
		Log.d(TAG, "Detected back pressed");
		Intent back = new Intent(this, MainPageActivity.class);
		this.startActivity(back);
	}
}
