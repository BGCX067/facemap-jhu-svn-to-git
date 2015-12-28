package edu.jhu.cs.oose.fall2011.facemap.gui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientApp;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientAppException;

/**
 * Add a contact page (add_contact.xml) activity. 
 * User input Phone or Email, or choose from phone contact, if the contact is found in the database,
 * a friend request will be sent to the other user
 * @author Ying Dou, Chuan Huang (Kevin)
 */
public class AddContactPageActivity extends OptionMenu
{
	/**
	 * Name of the class, used for debugging
	 */
	private final String TAG = "AddContactPageActivity";

	/**
	 * ClientApp object, phone and model interface
	 */
	private ClientApp clientApp;

	/**
	 * EditText to input phone number
	 */
	private EditText phoneInputEditText;
	/**
	 * EditText to input email
	 */
	private EditText emailInputEditText;
	
	/**
	 * requestCode for startActivityForResult when navigating to phone contact page
	 */
	final int PICK_CONTACT = 1;

	/**
	 * called when class is called and creates content view to "add_contact"
	 * Sets the content view to add_contact.xml
	 * Retrieves ClientApp from the application (getApplication())
	 * Initializes phone and email input editText variables
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_contact);

		ClientAppApplication app = (ClientAppApplication) getApplication();
		clientApp = app.getClientApp();

		phoneInputEditText = (EditText) this.findViewById(R.id.phoneInputEditText);
		
		emailInputEditText = (EditText) this.findViewById(R.id.emailInputEditText);
	}

	/**
	 * Handles click action of the "Choose From Phone Contact" button.
	 * the corresponding button has property onClick set to this function
	 * Lets user select a contact to be added directly from phone's contact list.
	 * Opens the phone contact page
	 * 
	 * @param view
	 */
	public void chooseButtonAtAddContactClickHandler(View view)
	{
		switch (view.getId())
		{
			case R.id.chooseFromContactButton:
				Log.d(TAG, "Choosing from phone contact");
				Intent loadContact = new Intent(Intent.ACTION_GET_CONTENT);
				loadContact.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
				this.startActivityForResult(loadContact, PICK_CONTACT);
				break;
		}
	}

	/**
	 * Uses the returned intent from the phone's contact page to set who to add. 
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		Log.d(TAG, "Returned from contacts");
		
		if (intent != null)
		{
			Uri uri = intent.getData();
			if (uri != null)
			{
				Cursor c = null;
				try {
	                c = getContentResolver().query(uri, new String[]{ 
	                            ContactsContract.CommonDataKinds.Phone.NUMBER,  
	                            ContactsContract.CommonDataKinds.Phone.TYPE },
	                        null, null, null);

	                if (c != null && c.moveToFirst()) {
	                    String number = c.getString(0);
	                    String[] phoneNumber = number.split("-");
	                    number = "";
	                    for (String s : phoneNumber)
	                    {
	                    	number += s;
	                    }
	                    
	                    phoneInputEditText.setText(number);
	                }
	            } finally {
	                if (c != null) {
	                    c.close();
	                }
	            }
			}
		}
	}

	/**
	 * Handles click action of the "Request" button.
	 * the corresponding button has property onClick set to this function
	 * Retrieves phone or email from EditText and sends request to server
	 * 
	 * @param view
	 */
	public void requestButtonAtAddContactClickHandler(View view)
	{
		switch (view.getId())
		{
			case R.id.requestFriendButton:
				String phone = phoneInputEditText.getText().toString();
				
				String email = emailInputEditText.getText().toString();
								
				if (phone.length() == 0 && email.length() == 0)
				{
					Toast.makeText(this, "Please input phone or email or choose from contact", Toast.LENGTH_LONG).show();
					return;
				}
				// check valid phone or email
				Log.d(TAG,phone);
				if (phone.length() != 0) {
					if (!isValidPhoneNumber(phone)) {
						Toast.makeText(this, "invalid phone", Toast.LENGTH_LONG).show();
						return;
					}
					phone = processPhoneNumber(phone);
				} else if (email.length() != 0) {
					if (!RegisterPageActivity.isValidEmailAddress(email)) {
						Toast.makeText(this, "invalid email", Toast.LENGTH_LONG).show();
						return;
					}
				}
				String phoneOrEmail =  null;
				if (!phone.equals("")) {
					phoneOrEmail = phone;
				} else if (!email.equals("")) {
					phoneOrEmail = email;
				}
				// request friend
				try
				{
					clientApp.requestFriend(phoneOrEmail);
					Toast.makeText(this, "request sent", Toast.LENGTH_LONG).show();
					
					Intent prev = new Intent(this, AllContactsPageActivity.class);
					this.startActivity(prev);
				} catch (ClientAppException e)
				{
					// possible error message may be the user is not in the database
					Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
				}
				
		}
	}

	/**
	 * Test whether the string is a valid phone number
	 * 
	 * @param phone input to be tested
	 * @return test result
	 */
	public boolean isValidPhoneNumber(String phone)
	{
		boolean isValid = false;
		String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";    
		Pattern pattern = Pattern.compile(expression);  
		Matcher matcher = pattern.matcher(phone);  
		if(matcher.matches()){  
			isValid = true;  
		}  
		return isValid; 
	}
	
	/**
	 * Removes dash "-" or whitespace from phone input
	 * Only phone number is kept
	 * @param phone
	 * @return
	 */
	String processPhoneNumber(String phone) {
		String phoneNumber = "";
		for (int i = 0; i < phone.length(); i++) {
			if (phone.charAt(i) != '-' || phone.charAt(i) != ' ') {
				phoneNumber += phone.charAt(i);
			}
		}
		return phoneNumber;
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
	 * Return to all contacts page when back key is pressed
	 */
	public void onBackPressed()
	{
		Log.d(TAG, "Detected back pressed");
		Intent back = new Intent(this, AllContactsPageActivity.class);
		this.startActivity(back);
	}
}
