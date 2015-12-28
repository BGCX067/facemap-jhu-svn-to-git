package edu.jhu.cs.oose.fall2011.facemap.gui;

import java.util.Set;

import edu.jhu.cs.oose.fall2011.facemap.client.ClientApp;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendGroup;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
/**
 * 
 * GroupPage ("groups.xml") Activity 
 * shows you all of the currently created groups as well as the nearby and unclassified groups
 * @author Chuan Huang, Ying Dou
 *
 */
public class GroupsPageActivity extends OptionMenu {

	/**
	 * Name of the class, used for debugging 
	 */
	private final String TAG = "GroupsPageActivity";
	
	/** Constant for choosing range for groups */
	private final int GROUPS = 2;
	/**
	 * ClientApp object. phone model interface object
	 */
	private ClientApp clientApp;
	
	/**
	 * friend groups of the logged in user
	 */
	private Set<FriendGroup> friendGroups;
	/**
	 * create view to "groups.xml".
	 * Gets ClientApp object from application.
	 * adds friend group buttons to a linear layout.
	 * Set button listeners.
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups);
        // get ClientApp object from application
        ClientAppApplication app = (ClientAppApplication)getApplication();
        clientApp = app.getClientApp();
        // get friend groups
        friendGroups = clientApp.getLoggedInUser().getFriendGroups();
        
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.groupsLinearLayout);
        // add buttons for friend groups and set listeners
        for (final FriendGroup fg : friendGroups) {
        	Button groupButton = new Button(this);
        	groupButton.setText(fg.getGroupName());
        	groupButton.setTextSize((float) 30);
        	linearLayout.addView(groupButton);
        	
        	groupButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					GroupsPageActivity.this.startGroupIndividualActivity(fg.getGroupName());
				}
			});
        }
        
        // these two buttons are pre-created on layout
        Button nearbyFriendsButton = (Button)findViewById(R.id.nearbyFriendsButton);
        Button unclassifiedButton = (Button)findViewById(R.id.unclassifiedGroupButton);
        
        // set button listeners
        if (nearbyFriendsButton != null) {
        	nearbyFriendsButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					GroupsPageActivity.this.startGroupNearbyActivity();
				}
			});
        }
        if (unclassifiedButton != null) {
        	unclassifiedButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					GroupsPageActivity.this.startGroupUnclassifiedActivity();
				}
			});
        }
	}
	
	/**
	 * "+" add a group button listener
	 * call function that navigates to add_group_1.xml
	 * @param view
	 */
	public void addGroupButtonClickHandler(View view) {
		switch (view.getId()) {
		case R.id.addGroupButton:
			GroupsPageActivity.this.startAddGroup1Activity();
			break;
		}
	}
	
	/**
	 * navigate to add_group_1.xml
	 */
	public void startAddGroup1Activity() {
		Log.d(TAG,"Starting AddGroup1Activity");
    	Intent intent = new Intent(this,AddGroupPage1Activity.class);
    	this.startActivity(intent);
	}
	
	/**
	 * navigate to group_individual.xml page
	 * pass the group name to the next activity
	 * @param groupName
	 */
	public void startGroupIndividualActivity(String groupName) {
		Log.d(TAG,"Starting GroupIndividualActivity");
    	Intent intent = new Intent(this,GroupIndividualPageActivity.class);
    	intent.putExtra("groupName", groupName);
    	this.startActivity(intent);
	}
	
	/**
	 * navigate to group_nearby.xml
	 * pass the range of the nearby group to the next activity
	 */
	public void startGroupNearbyActivity() {
		Log.d(TAG,"Starting GroupNearbyActivity");
		Intent intent = new Intent(this,LocateRangePageActivity.class);
		intent.putExtra("key", GROUPS);
    	this.startActivity(intent);
	}
	
	/**
	 * navigate to group_unclassified.xml
	 */
	public void startGroupUnclassifiedActivity() {
		Log.d(TAG,"Starting GroupUnclassifiedActivity");
		Intent intent = new Intent(this,GroupUnclassifiedPageActivity.class);
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
	 * Return to contacts page when back is pressed
	 */
	public void onBackPressed()
	{
		Log.d(TAG, "Detected back pressed");
		Intent back = new Intent(this, ContactsPageActivity.class);
		this.startActivity(back);
	}
}
