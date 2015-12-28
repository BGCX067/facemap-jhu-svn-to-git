package edu.jhu.cs.oose.fall2011.facemap.gui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientApp;

/**
 * The Class ChangePasswordPageActivity should allow user to change the preset password (Function Not Implemented in the Model)
 * @author Ying Dou
 */
public class ChangePasswordPageActivity extends OptionMenu {
	
	/** Name of the class, used for debugging. */
	private final String TAG = "ChangePasswordPageActivity";
	
	/** ClientApp object. */
	private ClientApp clientApp;
	
	/** old password EditText. */
	private EditText oldPasswordText;
	
	/** new password EditText. */
	private EditText newPasswordText;
	
	/** confirm new password EditText. */
	private EditText confirmNewPasswordText;
	
	/**
	 * Called when the activity is first created.
	 * set content view and initialize EditTexts
	 *
	 * @param savedInstanceState the saved instance state
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        
        ClientAppApplication app = (ClientAppApplication)getApplication();
        clientApp = app.getClientApp();
        
        this.oldPasswordText = (EditText)this.findViewById(R.id.oldPasswordText);
        this.newPasswordText = (EditText)this.findViewById(R.id.newPasswordText);
        this.confirmNewPasswordText = (EditText)this.findViewById(R.id.confirmNewPasswordText);
	}
	
	/**
	 * Listens ok button click.
	 *
	 * @param view the view
	 */
	public void OkButtonAtChangePasswordClickHandler(View view) {
		switch (view.getId()) {
		case R.id.okButtonAtChangePasswd:
			// get old password from model???
			String correctOldPassword = null;
			String oldPassword = this.oldPasswordText.getText().toString();
			String newPassword = this.newPasswordText.getText().toString();
			String confirmNewPassword = this.confirmNewPasswordText.getText().toString();
			if (oldPassword.length() == 0) {
				Toast.makeText(this, "Please enter old password", Toast.LENGTH_LONG).show();
				return;
			}
			if (newPassword.length() == 0) {
				Toast.makeText(this, "Please enter new password", Toast.LENGTH_LONG).show();
				return;
			}
			if (confirmNewPassword.length() == 0) {
				Toast.makeText(this, "Please enter new password again", Toast.LENGTH_LONG).show();
				return;
			}
			if (!oldPassword.equals(correctOldPassword)) {
				Toast.makeText(this, "wrong old password", Toast.LENGTH_LONG).show();
				return;
			}
			if (!newPassword.equals(confirmNewPassword)) {
				Toast.makeText(this, "new passwords don't match", Toast.LENGTH_LONG).show();
				return;
			}
			// update password????
			
			break;
		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG,"onDestroy");
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG,"onPause");
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(TAG,"onRestart");
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG,"onResume");
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG,"onStart");
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG,"onStop");
	}

}
