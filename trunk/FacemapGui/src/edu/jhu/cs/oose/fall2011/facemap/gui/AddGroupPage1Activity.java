package edu.jhu.cs.oose.fall2011.facemap.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Add a new group (add_group_1.xml) activity step 1.
 * User inputs the group name, and the group name string will be passed to the next activity
 * @author Ying Dou
 *
 */
public class AddGroupPage1Activity extends OptionMenu {
	/**
	 * Name of the class, used for debugging
	 */
	private final String TAG = "AddGroupPage1Activity";
	
	/**
	 * EditText to input group name
	 */
	private EditText groupNameText;
	
	/**
	 * called when class is called and creates content view to "add_group_1"
	 * Sets the content view to add_group_1.xml
	 * Initialize the group name EditText
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_group_1);
        this.groupNameText = (EditText)this.findViewById(R.id.groupNameText);
	}
	
	/**
	 * Handles click action of the "Next" button.
	 * the corresponding Next button has property onClick set to this function
	 * @param view
	 */
	public void AddGroupPageNextButtonClickHandler(View view) {
		switch (view.getId()) {
		case R.id.nextAtAddGroupButton:
			// test whether the user inputs anything or not
			if (this.groupNameText.getText().length() == 0) {
				Toast.makeText(this, "Please enter group name", Toast.LENGTH_LONG).show();
    			return;
			}
			// get the string from the EditText
			String groupName = this.groupNameText.getText().toString();

			// to the next page, where user can choose the contacts he wants to add to the group
			AddGroupPage1Activity.this.startAddGroup2Activity(groupName);
			break;
		}
	}
	
	/**
	 * Navigate to the next page "add_group_2.xml"
	 * @param groupName put to Intent, pass to the next page
	 */
	public void startAddGroup2Activity(String groupName) {
		Log.d(TAG,"Starting AddGroup2Activity");
    	Intent intent = new Intent(this,AddGroupPage2Activity.class);
    	intent.putExtra("groupName", groupName);
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
	
	public void onBackPressed()
	{
		Log.d(TAG, "Detected back pressed");
		Intent back = new Intent(this, GroupsPageActivity.class);
		this.startActivity(back);
	}
}
