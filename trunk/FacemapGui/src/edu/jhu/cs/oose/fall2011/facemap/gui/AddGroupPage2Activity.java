package edu.jhu.cs.oose.fall2011.facemap.gui;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientApp;
import edu.jhu.cs.oose.fall2011.facemap.domain.Person;

/**
 * Add a new group (add_group_2.xml) activity.
 * Gets group name from the previous activity (AddGroupPage1Activity).
 * Chooses friends to be added to the new group by checking checkboxes.
 * Creates the new group
 * @author Chuan Huang, Ying Dou
 *
 */
public class AddGroupPage2Activity extends OptionMenu {
	/**
	 * Name of the class, used for debugging
	 */
	private final String TAG = "AddGroupPage2Activity";
	
	/**
	 * ClientApp object, phone and model interface
	 */
	private ClientApp clientApp;
	
	/**
	 * group name to be created
	 */
	private String groupName;
	
	/**
	 * A set of all friends of user, used to create checkboxes
	 */
	private Set<Person> friends;
	
	/**
	 * CheckBoxes to select to be added to the new group
	 */
	private CheckBox[] fCheckBoxes;
	
	/**
	 * Creates layout view "add_group_2.xml", 
	 * retrieves groupName got from the previous activity (AddGroupPage1Activity)
	 * dynamically creates CheckBoxes according to the user's friend list,
	 * and listens to the click action of button "Save", if the button is clicked, creates a new group
	 * Return to "groups.xml" after the new group is created.
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_group_2);
        
        // get group name from Intent
        if (getIntent().getExtras() != null) {
        	Log.d(TAG,"getExtras() not empty");
        	groupName = getIntent().getStringExtra("groupName");
        }
        
        // get phone model inteface object to retrieve model information
        ClientAppApplication app = (ClientAppApplication)getApplication();
        clientApp = app.getClientApp();
        
        // get all the friends of the logged in user from the model
        friends = clientApp.getLoggedInUser().getFriends();
        
        // create a linear layout to put CheckBoxes
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.AddAGroupLinearLayout);
        // create an array of CheckBoxes with size of friends, each CheckBox will show a contact name
        fCheckBoxes = new CheckBox[friends.size()];
        int i = 0;
        /* create checkboxes for each friend. 
         * set the text to be the friend name and set the text size to 20
         * add the checkbox to the linear layout
		*/
        for (Person f : friends) {
        	fCheckBoxes[i] = new CheckBox(this);
        	fCheckBoxes[i].setText(f.getName());
        	fCheckBoxes[i].setTextSize((float) 20);
        	linearLayout.addView(fCheckBoxes[i]);
        	i++;
        }
        
        // create save button
        Button saveButton = (Button)findViewById(R.id.AddAGroupSaveButton);
        if (saveButton != null) {
        	// set button listener
        	saveButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// create a set to store friends selected
					Set<Person> friendsInGroup = new HashSet<Person>();
					int j = 0;
					// add checked friends
					for (Person f : friends) {
						if (fCheckBoxes[j].isChecked()) 
						{
							friendsInGroup.add(f);
						}
						j++;
					}
					// create the new group with the groupName and set of friends
					clientApp.getLoggedInUser().createFriendGroup(groupName, friendsInGroup);
					
					// go to groups.xml page
					AddGroupPage2Activity.this.startGroupsActivity();
				}
			});
        }
        
	}
	
	/**
	 * Navigate to the group page("groups.xml"), which will display the newly added group
	 */
	public void startGroupsActivity() {
		Log.d(TAG,"Starting GroupsActivity");
    	Intent intent = new Intent(this,GroupsPageActivity.class);
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
}
