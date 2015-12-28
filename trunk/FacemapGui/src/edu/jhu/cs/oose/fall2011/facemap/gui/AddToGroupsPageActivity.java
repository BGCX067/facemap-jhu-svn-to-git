package edu.jhu.cs.oose.fall2011.facemap.gui;

import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientApp;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendGroup;
import edu.jhu.cs.oose.fall2011.facemap.domain.Person;

/**
 * Add friend to groups (add_to_group.xml) activity.
 * Creates checkboxes corresponding to friend groups.
 * The user selects groups that the friend is to be added, and add them to the specified groups.
 * @author Chuan Huang, Ying Dou
 * 
 */
public class AddToGroupsPageActivity extends OptionMenu
{
	/**
	 * Name of the class, used for debugging
	 */
	private final String TAG = "AllContactsPageActivity";

	/**
	 * ClientApp object, phone and model interface
	 */
	private ClientApp clientApp;
	/**
	 * friend email
	 */
	private String contactEmail;
	/**
	 * friend groups of the user
	 */
	private Set<FriendGroup> friendGroups;

	/**
	 * CheckBoxes correspond to friend groups
	 */
	private CheckBox[] fgCheckboxes;

	/**
	 * Create the layout view "add_to_group".
	 * Get the email address of the friend to be added from the intent, so the corresponding Person object can be found
	 * Get the phone model interface object ClientApp.
	 * Create checkboxes for friend groups
	 * Add "Add" button listener, if it is clicked, add friend to the groups checked
	 * Navigate to groups.xml page when finished.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_to_group);

		// get email address (ID) of the friend to be added
		if (getIntent().getExtras() != null)
		{
			Log.d(TAG, "getExtras() not empty");
			contactEmail = getIntent().getStringExtra("email");
		}

		// get phone model interface object to retrieve model information
		ClientAppApplication app = (ClientAppApplication) getApplication();
		clientApp = app.getClientApp();

		// get friend groups of the logged in user
		friendGroups = clientApp.getLoggedInUser().getFriendGroups();
		
		// create an array of CheckBoxes with size of friend groups, each CheckBox will show a group name
		fgCheckboxes = new CheckBox[friendGroups.size()];
		// create a linear layout to put checkboxes
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.AddToGroupsLinearLayout);
		/* create a check box for each group, 
		 * set the text to be group name
		 * set the text size to 20
		 * add the checkbox to the linear layout
		 */
		int i = 0;
		for (FriendGroup fg : friendGroups)
		{
			fgCheckboxes[i] = new CheckBox(this);
			fgCheckboxes[i].setText(fg.getGroupName());
			fgCheckboxes[i].setTextSize((float) 20);
			linearLayout.addView(fgCheckboxes[i]);
			i++;
		}
		
		// create the add button
		Button addButton = (Button) findViewById(R.id.addToGroupsAddButton);
		if (addButton != null)
		{
			// button listener
			addButton.setOnClickListener(new OnClickListener()
			{

				public void onClick(View v)
				{
					int j = 0;
					/* for each friend group of the logged in user, check whether it is checked
					 * if yes, add the friend to the group
					 */
					for (FriendGroup fg : friendGroups)
					{
						if (fgCheckboxes[j].isChecked())
						{
							Person p = null;
							for (Person pp : clientApp.getLoggedInUser().getFriends())
							{
								if (pp.getEmail().equals(contactEmail))
								{
									p = pp;
									break;
								}
							}
							fg.addFriendToGroup(p);
						}
					}
					// show message
					Toast.makeText(AddToGroupsPageActivity.this, "successfully add contact to groups",
							Toast.LENGTH_LONG).show();
					// navigate to groups.xml page
					AddToGroupsPageActivity.this.startActivity(new Intent(AddToGroupsPageActivity.this,
							GroupsPageActivity.class));
				}
			});
		}
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
