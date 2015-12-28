package edu.jhu.cs.oose.fall2011.facemap.gui;

import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientApp;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientAppException;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendGroup;
import edu.jhu.cs.oose.fall2011.facemap.domain.Person;

/**
 * Individual Group Page (group_individual.xml) - 
 * contains options for the specific group including show member, add/remove/block/unblock
 * members, and send group email
 * 
 * @author Chuan Huang, Ying Dou
 * 
 */
public class GroupIndividualPageActivity extends OptionMenu
{
	/**
	 * Name of the class, used for debugging
	 */
	private final String TAG = "GroupIndividualPageActivity";
	/**
	 * ClientApp object. phone model interface object
	 */
	private ClientApp clientApp;
	/**
	 * group name
	 */
	private String groupName;

	/**
	 * The friend group.
	 */
	private FriendGroup friendGroup;

	/**
	 * creates view to "group_individual.xml"
	 * Gets ClientApp object from the application
	 * gets the group name from getIntent(), and finds the group object
	 * Sets the page title to the group name, sets the text size to 40
	 * Creates buttons and adds them to a linear layout
	 * Sets button listeners
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_individual);

		ClientAppApplication app = (ClientAppApplication) getApplication();
		clientApp = app.getClientApp();

		if (getIntent().getExtras() != null)
		{
			groupName = getIntent().getStringExtra("groupName");
			Log.d(TAG, "getExtras() not empty: " + groupName);
		}

		// get friend group object
		friendGroup = Functions.SearchFriendGroupByName(clientApp.getLoggedInUser().getFriendGroups(), groupName);

		// set title
		TextView groupIndividualTitleText = (TextView) this.findViewById(R.id.groupIndividualTitle);
		groupIndividualTitleText.setText(groupName);
		groupIndividualTitleText.setTextSize((float) 40);

		// get the linear layout from layout file and creat buttons
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.groupIndividualLinearLayout);
		Button membersButton = new Button(this);
		Button addMembersButton = new Button(this);
		Button removeMembersButton = new Button(this);
		Button deleteMembersButton = new Button(this);
		Button blockMembersButton = new Button(this);
		Button unblockMembersButton = new Button(this);
		Button sendGroupEmailButton = new Button(this);
		Button blockGroupButton = new Button(this);
		Button unblockGroupButton = new Button(this);
		Button deleteGroupButton = new Button(this);
		// set button texts
		membersButton.setText("Members");
		addMembersButton.setText("Add Members");
		removeMembersButton.setText("Remove Members");
		deleteMembersButton.setText("Delete Members");
		blockMembersButton.setText("Block Members");
		unblockMembersButton.setText("Unblock Members");
		sendGroupEmailButton.setText("Send Group Email");
		blockGroupButton.setText("Block Group");
		unblockGroupButton.setText("Unblock Group");
		deleteGroupButton.setText("Delete Group");
		// set text sizes
		membersButton.setTextSize((float) 20);
		addMembersButton.setTextSize((float) 20);
		removeMembersButton.setTextSize((float) 20);
		deleteMembersButton.setTextSize((float) 20);
		blockMembersButton.setTextSize((float) 20);
		unblockMembersButton.setTextSize((float) 20);
		sendGroupEmailButton.setTextSize((float) 20);
		blockGroupButton.setTextSize((float) 20);
		unblockGroupButton.setTextSize((float) 20);
		deleteGroupButton.setTextSize((float) 20);

		// add buttons to the linear layout
		if (linearLayout != null)
		{
			linearLayout.addView(membersButton);
			linearLayout.addView(addMembersButton);
			linearLayout.addView(removeMembersButton);
			linearLayout.addView(deleteMembersButton);
			linearLayout.addView(blockMembersButton);
			linearLayout.addView(unblockMembersButton);
			linearLayout.addView(sendGroupEmailButton);
			linearLayout.addView(blockGroupButton);
			linearLayout.addView(unblockGroupButton);
			linearLayout.addView(deleteGroupButton);
		}
		// set button listeners for all the buttons
		if (membersButton != null)
		{
			membersButton.setOnClickListener(new OnClickListener()
			{
				// navigate to page that display member names
				public void onClick(View v)
				{
					GroupIndividualPageActivity.this.startMembersPageActivity(groupName);
				}
			});
		}
		/*
		 * For add, remove, delete, block, unblock buttons, they all navigate to select_members.xml,
		 * where the user can select members that he wants to perform the action
		 * ActionType is also passed to the SelectMembersPageAcitivy
		 */
		if (addMembersButton != null)
		{
			addMembersButton.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					GroupIndividualPageActivity.this.startSelectMembersPageActivity(ActionType.ADD, groupName);
				}
			});
		}
		if (removeMembersButton != null)
		{
			removeMembersButton.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					GroupIndividualPageActivity.this.startSelectMembersPageActivity(ActionType.REMOVE, groupName);
				}
			});
		}
		if (deleteMembersButton != null)
		{
			deleteMembersButton.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					GroupIndividualPageActivity.this.startSelectMembersPageActivity(ActionType.DELETE, groupName);
				}
			});
		}
		if (blockMembersButton != null)
		{
			blockMembersButton.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					GroupIndividualPageActivity.this.startSelectMembersPageActivity(ActionType.BLOCK, groupName);
				}
			});
		}
		if (unblockMembersButton != null)
		{
			unblockMembersButton.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					GroupIndividualPageActivity.this.startSelectMembersPageActivity(ActionType.UNBLOCK, groupName);
				}
			});
		}
		// send group email
		if (sendGroupEmailButton != null)
		{
			sendGroupEmailButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					String[] email = new String[friendGroup.getFriendsInGroup().size()];
					int i = 0;
					for (Person p : friendGroup.getFriendsInGroup())
					{
						email[i] = p.getEmail();
						i++;
					}
					GroupIndividualPageActivity.this.startSendGroupEmailPageActivity(email);
				}
			});
		}
		/* block all the members in the group
		 * set an alert dialog to confirm first
		 */
		if (blockGroupButton != null)
		{
			blockGroupButton.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					Builder builder = new AlertDialog.Builder(GroupIndividualPageActivity.this);
					builder.setMessage("Are you sure to block all members in the group?");
					builder.setCancelable(true);
					builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
							FriendGroup fg = Functions.SearchFriendGroupByName(
									clientApp.getLoggedInUser().getFriendGroups(), groupName);
							// block all
							clientApp.getLoggedInUser().blockFriendGroup(fg);
						}
					});
					builder.setNegativeButton("No", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
							return;
						}
					});
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			});
		}
		/*
		 * unblock all the members in the group
		 * set an alert dialog to confirm the action
		 */
		if (unblockGroupButton != null)
		{
			unblockGroupButton.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					Builder builder = new AlertDialog.Builder(GroupIndividualPageActivity.this);
					builder.setMessage("Are you sure to block all members in the group?");
					builder.setCancelable(true);
					builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
							FriendGroup fg = Functions.SearchFriendGroupByName(
									clientApp.getLoggedInUser().getFriendGroups(), groupName);
							clientApp.getLoggedInUser().unblockFriendGroup(fg);
						}
					});
					builder.setNegativeButton("No", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
							return;
						}
					});
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			});
		}
		/* delete the group, keep members inside the group
		 * show an alert dialog to confirm first
		 */
		if (deleteGroupButton != null)
		{
			deleteGroupButton.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					Builder builder = new AlertDialog.Builder(GroupIndividualPageActivity.this);
					builder.setMessage("Are you sure to delete group " + groupName + "?");
					builder.setCancelable(true);
					builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
							FriendGroup fg = Functions.SearchFriendGroupByName(
									clientApp.getLoggedInUser().getFriendGroups(), groupName);
							clientApp.getLoggedInUser().removeFriendGroup(fg);
							GroupIndividualPageActivity.this.startGroupsPageActivity(groupName);

						}
					});
					builder.setNegativeButton("No", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
							return;
						}
					});
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			});
		}

	}

	/**
	 * navigate to members page, "group_members.xml", where member names are displayed
	 * pass the group name to the next activity
	 */
	public void startMembersPageActivity(String groupName)
	{
		Log.d(TAG, "Starting MembersPageActivity");
		Intent intent = new Intent(this, GroupMembersPageActivity.class);
		intent.putExtra("groupName", groupName);
		this.startActivity(intent);
	}

	/**
	 * navigate to select_members.xml, where the user can select members that he wants to perform the action
	 * 
	 * @param action, ADD, BLOCK, DELETE, REMOVE, UNBLOCK
	 * @param groupName
	 */
	public void startSelectMembersPageActivity(ActionType action, String groupName)
	{
		Log.d(TAG, "Starting SelectMembersPageActivity");
		Intent intent = new Intent(this, SelectMembersPageActivity.class);
		intent.putExtra("action", action);
		intent.putExtra("groupName", groupName);
		this.startActivityForResult(intent, 1);
	}

	/**
	 * Send group email
	 * 
	 * @param email email addresses of group members
	 */
	public void startSendGroupEmailPageActivity(String[] email)
	{
		Log.d(TAG, "Starting SendGroupEmailPageActivity");
		Intent sendEmail = new Intent(android.content.Intent.ACTION_SEND);

		Log.d(TAG, "Emails: " + email[0]);

		sendEmail.setType("text/plain");
		sendEmail.putExtra(Intent.EXTRA_EMAIL, email);
		sendEmail.putExtra(Intent.EXTRA_SUBJECT, "New Email Subject");
		sendEmail.putExtra(Intent.EXTRA_TEXT, "Hello Friends :D");
		try
		{
			this.startActivity(Intent.createChooser(sendEmail, "Send mail..."));
		} catch (android.content.ActivityNotFoundException e)
		{
			Toast.makeText(this, "Error, no client found", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * return to groups page (groups.xml) after the group is deleted
	 * groups page will be refreshed, and no longer display the deleted group
	 * @param groupName
	 */
	public void startGroupsPageActivity(String groupName)
	{
		Log.d(TAG, "Starting GroupsPageActivity");
		Intent intent = new Intent(this, GroupsPageActivity.class);
		intent.putExtra("groupName", groupName);
		this.startActivity(intent);
	}

	/**
	 * get selected contact emails and edit group
	 * and perform the action: ADD, DELETE, REMOVE, BLOCK, UNBLOCK
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == 1)
		{
			String[] selectedEmails = data.getStringArrayExtra("selected");
			ActionType action = (ActionType) data.getSerializableExtra("action");

			for (int i = 0; i < selectedEmails.length; i++)
			{
				Person p = Functions.SearchPersonByEmail(clientApp.getLoggedInUser().getFriends(), selectedEmails[i]);
				switch (action)
				{
					case ADD:
						friendGroup.addFriendToGroup(p);
						break;
					case BLOCK:
						clientApp.getLoggedInUser().blockFriend(p);
						break;
					case DELETE:
						if (clientApp.getLoggedInUser().getFriends().contains(p))
							try
							{
								clientApp.removeFriend(p);
							} catch (ClientAppException e)
							{
								continue;
							}
						break;
					case REMOVE:
						friendGroup.removeFriendFromGroup(p);
						break;
					case UNBLOCK:
						if (clientApp.getLoggedInUser().getBlockedFriends().contains(p))
							clientApp.getLoggedInUser().unblockFriend(p);
						break;
				}
			}

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

	/**
	 * Return to he groups page when back is pressed
	 */
	public void onBackPressed()
	{
		Log.d(TAG, "Detected back pressed");
		Intent back = new Intent(this, GroupsPageActivity.class);
		this.startActivity(back);
	}

}
