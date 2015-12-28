package edu.jhu.cs.oose.fall2011.facemap.gui;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
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
 * select_members.xml page activity.
 * Select members to add to a group, delete/block/unblock/remove
 * 
 * @author Ying Dou, Chuan Huang (Kevin)
 * 
 */
public class SelectMembersPageActivity extends OptionMenu
{
	/**
	 * Name of the class, used for debugging
	 */
	private final String TAG = "SelectMembersPageActivity";
	/**
	 * ClientApp object
	 */
	private ClientApp clientApp;
	/**
	 * ADD, DELETE, BLOCK, REMOVE or UNBLOCK
	 */
	private ActionType action;
	/**
	 * contacts to be displayed
	 */
	private Set<Person> actionList;
	/**
	 * group name
	 */
	private String groupName;

	/**
	 * friend group that is being edited
	 */
	private FriendGroup friendGroup;
	/**
	 * CheckBoxes of contact members
	 */
	private CheckBox[] fCheckBoxes;

	/**
	 * creates view to select_members.xml,
	 * gets ClientApp object from application,
	 * gets ActionType from intent and sets the page title
	 * gets friends to be displayed from the model according to the ActionType
	 * creates checkboxes corresponding to friends
	 * set button listener
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_members);

		// get ClientApp object from application
		ClientAppApplication app = (ClientAppApplication) getApplication();
		clientApp = app.getClientApp();

		// get action type and group name
		if (getIntent().getExtras() != null)
		{
			Log.d(TAG, "getExtras() not empty");
			action = (ActionType) getIntent().getSerializableExtra("action");
			groupName = getIntent().getStringExtra("groupName");
		}

		// set title
		TextView groupActionTitleText = (TextView) findViewById(R.id.GroupActionTitle);
		groupActionTitleText.setText(groupName);
		groupActionTitleText.setTextSize((float) 40);

		// list of friends
		actionList = new HashSet<Person>();

		// get the friend group in concern
		friendGroup = Functions.SearchFriendGroupByName(clientApp.getLoggedInUser().getFriendGroups(), groupName);
		// if action is ADD, display members that are not in the group, else, display members in the group
		if (action == ActionType.ADD)
		{
			for (Person p : clientApp.getLoggedInUser().getFriends())
			{
				// check whether p is in this group already
				if (!Functions.isPersonInFriendGroup(friendGroup, p))
				{
					actionList.add(p);
				}
			}
		} else if (action == ActionType.BLOCK || action == ActionType.DELETE || action == ActionType.REMOVE || action == ActionType.UNBLOCK)
		{
			actionList = friendGroup.getFriendsInGroup();
		} else
		{
			Log.d(TAG, "action wrong value....");
		}

		// create checkboxes and add them to the linear layout
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.GroupActionLinearLayout);
		fCheckBoxes = new CheckBox[actionList.size()];
		int i = 0;
		for (Person p : actionList)
		{
			fCheckBoxes[i] = new CheckBox(this);
			fCheckBoxes[i].setText(p.getName());
			fCheckBoxes[i].setTextSize((float) 20);
			linearLayout.addView(fCheckBoxes[i]);
			i++;
		}
		// action button
		Button actionButton = (Button) findViewById(R.id.ActionButton);

		// set button text, and listener
		if (actionButton != null)
		{
			actionButton.setText(action.toString());
			actionButton.setOnClickListener(new OnClickListener()
			{
				/* display alert dialog to confirm first
				 * retrieves the selected friends, and add to intent, pass back to the previous activity
				 * (non-Javadoc)
				 * @see android.view.View.OnClickListener#onClick(android.view.View)
				 */
				public void onClick(View v)
				{
					// dialog
					Builder builder = new AlertDialog.Builder(SelectMembersPageActivity.this);
					builder.setMessage("Are you sure to " + action.toString() + "?");
					builder.setCancelable(true);
					builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
							Set<Person> selected = new HashSet<Person>();
							int j = 0;
							for (Person p : actionList)
							{
								if (fCheckBoxes[j].isChecked())
								{
									selected.add(p);
								}
								j++;
							}
							String[] selectedEmails = new String[selected.size()];
							j = 0;
							for (Person p : selected)
							{
								selectedEmails[j] = p.getEmail();
								j++;
							}
							Intent intent = getIntent();
							intent.putExtra("selected", selectedEmails);
							setResult(RESULT_OK, intent);
							finish(); // return to previous page..
						}
					});
					builder.setNegativeButton("No", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
							String[] selectedEmails = new String[0];
							Intent intent = getIntent();
							intent.putExtra("selected", selectedEmails);
							setResult(RESULT_OK, intent);
							finish(); // return to previous page..
						}
					});
					AlertDialog dialog = builder.create();
					dialog.show();
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
