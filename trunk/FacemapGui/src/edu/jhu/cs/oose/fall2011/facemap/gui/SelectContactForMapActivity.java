/*
 * 
 */
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
import android.widget.TextView;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientApp;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendGroup;
import edu.jhu.cs.oose.fall2011.facemap.domain.Person;

/**
 * Choose friends to be added to the new group and create the map
 * 
 * @author Chuan Huang (Kevin)
 * 
 */
public class SelectContactForMapActivity extends OptionMenu
{
	/**
	 * Name of the class, used for debugging
	 */
	private final String TAG = "SelectContactForMapActivity";
	
	/** Constant to indicate we're selecting contacts from all contacts */
	final int CONTACTS = 1;
	
	/** Cosntant to indicate we're selecting contacts from groups */
	final int GROUPS = 2;

	/**
	 * ClientApp object
	 */
	private ClientApp clientApp;

	/**
	 * all friends of user
	 */
	private Set<Person> friends;

	/** The groups. */
	private Set<FriendGroup> groups;

	/**
	 * CheckBoxes to select to be added to the new group
	 */
	private CheckBox[] fCheckBoxes;

	private int key;
	ClientAppApplication app;

	/**
	 * create layout view, dynamically create CheckBoxes according to the user's friend list, and listens to the click
	 * action of button "Save", if clicks, create a new group Return to "groups.xml"...
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_group_2);

		if (getIntent().getExtras() != null)
		{
			Log.d(TAG, "getExtras() not empty");
			key = getIntent().getIntExtra("key", CONTACTS);
		}

		TextView title = (TextView) this.findViewById(R.id.addGroup);
		if (key == CONTACTS)
			title.setText("Select contacts");
		else if (key == GROUPS)
			title.setText("Select group");

		app = (ClientAppApplication) getApplication();
		clientApp = app.getClientApp();
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.AddAGroupLinearLayout);

		if (key == CONTACTS)
		{
			friends = clientApp.getLoggedInUser().getFriends();
		} else if (key == GROUPS)
		{
			groups = clientApp.getLoggedInUser().getFriendGroups();
		}

		Button saveButton = (Button) findViewById(R.id.AddAGroupSaveButton);
		// Selecting Friends
		if (key == CONTACTS)
		{
			fCheckBoxes = new CheckBox[friends.size()];
			int i = 0;
			for (Person f : friends)
			{
				fCheckBoxes[i] = new CheckBox(this);
				fCheckBoxes[i].setText(f.getName());
				fCheckBoxes[i].setTextSize((float) 20);
				linearLayout.addView(fCheckBoxes[i]);
				i++;
			}

			if (saveButton != null)
			{
				saveButton.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						// create the new group
						Set<Person> friendsInGroup = new HashSet<Person>();
						int j = 0;
						for (Person f : friends)
						{
							if (fCheckBoxes[j].isChecked())
							{
								friendsInGroup.add(f);
							}
							j++;
						}
						app.setPersonList(friendsInGroup);

						SelectContactForMapActivity.this.startMapActivity();
					}
				});
			}
		}
		// Selecting Groups
		else if (key == GROUPS)
		{
			fCheckBoxes = new CheckBox[groups.size()];
			int i = 0;
			for (FriendGroup f : groups)
			{
				fCheckBoxes[i] = new CheckBox(this);
				fCheckBoxes[i].setText(f.getGroupName());
				fCheckBoxes[i].setTextSize((float) 20);
				linearLayout.addView(fCheckBoxes[i]);
				i++;
			}

			if (saveButton != null)
			{
				saveButton.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						// create the new group
						Set<FriendGroup> friendGroups = new HashSet<FriendGroup>();
						int j = 0;
						for (FriendGroup f : groups)
						{
							if (fCheckBoxes[j].isChecked())
							{
								friendGroups.add(f);
							}
							j++;
						}
						app.setGroupList(friendGroups);

						SelectContactForMapActivity.this.startMapActivity();
					}
				});
			}
		}
	}

	/**
	 * 
	 */
	public void startMapActivity()
	{
		Log.d(TAG, "Starting MapActivity");
		Intent intent = new Intent(this, FacemapMapActivity.class);
		intent.putExtra("key", key);
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

	public void onBackPressed()
	{
		Log.d(TAG, "Detected back pressed");
		Intent back = new Intent(this, LocatePageActivity.class);
		this.startActivity(back);
	}
}
