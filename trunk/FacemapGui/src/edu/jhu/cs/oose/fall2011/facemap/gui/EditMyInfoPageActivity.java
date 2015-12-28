package edu.jhu.cs.oose.fall2011.facemap.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView.BufferType;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientApp;

/**
 * Allow user to edit name, personal message, and picture (picture not implemented)
 * @author Chuan Huang, Ying Dou
 */
public class EditMyInfoPageActivity extends OptionMenu {

	/**
	 * Name of the class, used for debugging
	 */
	private final String TAG = "EditMyInfoPageActivity";
	/** ClientApp object. phone model interface object */
	private ClientApp clientApp;
	/**
	 * EditText for name
	 */
	private EditText nameText;
	/**
	 * EditText for custom message
	 */
	private EditText customMessageText;
	
	/**
	 * create view to "edit_my_info.xml".
	 * retrieves ClientApp object from application.
	 * Initializes edit texts.
	 * gets the name of the logged in user and set the name text
	 * @param savedInstanceState the saved instance state
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_my_info);
        
        ClientAppApplication app = (ClientAppApplication)getApplication();
        clientApp = app.getClientApp();
        
        this.nameText = (EditText)this.findViewById(R.id.nameTextAtEdit);
        this.customMessageText = (EditText)this.findViewById(R.id.customMessageTextAtEdit);      
        
        String name = clientApp.getLoggedInUser().getSelf().getName();
        String customMessage = null;
        
        this.nameText.setText(name, BufferType.EDITABLE);
        this.customMessageText.setText(customMessage, BufferType.EDITABLE);
	}
	/**
	 * button listener for picture button
	 * supposes to browse picture and save picture...
	 * not implemented
	 * @param view
	 */
	public void MyInfoPagePictureButtonAtEditClickHandler(View view) {
		switch (view.getId()) {
		case R.id.myPictureImageButtonAtEdit:
			// browse picture....
			break;
		}
	}
	/**
	 * Save button listener- saves the new information into clientApp and moves back to info page
	 * @param view
	 */
	public void SaveButtonClickHandler(View view) {
		switch (view.getId()) {
		case R.id.saveButtonAtEditMyInfo:
			String name = this.nameText.getText().toString();
			String customMessage = this.customMessageText.getText().toString();
			
			clientApp.getLoggedInUser().setMessage(customMessage);
			clientApp.getLoggedInUser().setName(name);
			
			Log.d(TAG, "Info changed");
			
			Intent data = new Intent();
			data.putExtra("no change", "change");
			data.putExtra("name", name);
			data.putExtra("message", customMessage);
			
			if (getParent() == null) {
			    setResult(Activity.RESULT_OK, data);
			} else {
			    getParent().setResult(Activity.RESULT_OK, data);
			}
			this.finish();
			
			break;
		}
	}
	/**
	 * Cancel button listener- return to the info page and don't save any changes to the clientApp
	 * @param view
	 */
	public void CancelButtonClickHandler(View view) {
		switch (view.getId()) {
		case R.id.cancelButtonAtEditMyInfo:
			Intent data = new Intent();
			data.putExtra("no change", "no change");
			if (getParent() == null) {
			    setResult(Activity.RESULT_OK, data);
			} else {
			    getParent().setResult(Activity.RESULT_OK, data);
			}
			this.finish();
			break;
		}
	}
	/** 
	 * Button listener for "Change Password" (Not Implemented)
	 * @param view
	 */
	public void ChangePasswordButtonClickHandler(View view) {
		switch (view.getId()) {
		case R.id.changePasswordButton:
			EditMyInfoPageActivity.this.startChangePasswordActivity();
			break;
		}
	}
	
	/**
	 * Navigate to "change_password.xml" (Not Implemented)
	 */
	public void startChangePasswordActivity() {
		Log.d(TAG,"Starting ChangePasswordActivity");
    	Intent intent = new Intent(this,ChangePasswordPageActivity.class);
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
	 * navigate to my_info.xml page when the back button is pressed
	 */
	public void onBackPressed()
	{
		Log.d(TAG, "Detected back pressed");
		Intent back = new Intent(this, MyInfoPageActivity.class);
		this.startActivity(back);
	}
}
