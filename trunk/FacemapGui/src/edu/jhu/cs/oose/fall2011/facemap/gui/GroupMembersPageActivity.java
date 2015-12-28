package edu.jhu.cs.oose.fall2011.facemap.gui;

import java.util.Set;

import edu.jhu.cs.oose.fall2011.facemap.client.ClientApp;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientAppException;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendGroup;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendGroupImpl;
import edu.jhu.cs.oose.fall2011.facemap.domain.Person;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The Class GroupMembersPageActivity- shows all of the members in the current group
 * Can navigate to member individual page
 * @author Chuan Huang (Kevin), Ying Dou
 */
public class GroupMembersPageActivity extends OptionMenu
{

	/** Name of the class, used for debugging. */
	private final String TAG = "GroupMembersPageActivity";

	/** ClientApp object. phone model interface object */
	private ClientApp clientApp;

	/** group name. */
	private String groupName;

	/** friends in the group. */
	private Set<Person> friends;

	/**
	 * show whether the group is nearby group (special group) or not
	 */
	private boolean nearby;

	/**
	 * create the view to "group_members.xml" , get friends' information and set the buttons.
	 * 
	 * @param savedInstanceState the saved instance state
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_members);

		if (getIntent().getExtras() != null)
		{
			groupName = getIntent().getStringExtra("groupName");
			nearby = getIntent().getBooleanExtra("nearby", false);
			Log.d(TAG, "getExtras() not empty " + groupName);
			if (nearby)
			{
				Log.d(TAG, "True");
			}
		}
		
		// set title
		TextView groupNameTitleText = (TextView) this.findViewById(R.id.GroupNameTitle);
		groupNameTitleText.setText(groupName);
		groupNameTitleText.setTextSize((float) 40);

		// get ClientApp object from the application
		ClientAppApplication app = (ClientAppApplication) getApplication();
		this.clientApp = app.getClientApp();

		FriendGroup friendGroup = null;
		// if the group is unclassified group, create a new group
		if (groupName.equalsIgnoreCase("Unclassified"))
		{
			friendGroup = new FriendGroupImpl("Unclassified", clientApp.getLoggedInUser().getFriends());
		}
		// find the right friend group
		for (FriendGroup fg : clientApp.getLoggedInUser().getFriendGroups())
		{
			if (fg.getGroupName().equals(groupName))
			{
				friendGroup = fg;
				break;
			}
		}
		if (friendGroup == null)
		{
			Log.d(TAG, "can't find FriendGroup: " + groupName);
		}
		
		// get friends inside the group
		if (groupName.equalsIgnoreCase("Unclassified"))
		{
			this.friends = Functions.getUnclassifiedFriends(clientApp);
		} else if (nearby)
		{
			Log.d(TAG, "Retrieving from app");
			this.friends = app.getPersonList();
		} else
		{
			Log.d(TAG, "Friend group: " + friendGroup.getGroupName());
			this.friends = friendGroup.getFriendsInGroup();
			
			for (Person p : friends)
			{
				Log.d(TAG, "Friend: " + p.getName());
			}
		}

		// linear layout to put buttons of members
		final LinearLayout friendListLinearLayout = (LinearLayout) findViewById(R.id.groupFriendListLinearLayout);

		/* set a relative layout for each member, which displays name, delete button and image
		 * * ----------------------------------------
		 * image        friend_name        delete     } a relative layout to be added to the linear layout
		 * ----------------------------------------
		 */
		for (final Person f : this.friends)
		{
			Log.d(TAG, "Add friend: " + f.getName());
			// create relative layout and add buttons and image
			final RelativeLayout relativeLayout = new RelativeLayout(this);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			Button contactButton = new Button(this);
			contactButton.setText(f.getName());
			contactButton.setTextSize((float) 30);
			ImageView contactIcon = new ImageView(this);
			contactIcon.setImageResource(R.drawable.icon); // change later!!!
			ImageButton contactDeleteButton = new ImageButton(this);
			contactDeleteButton.setImageResource(android.R.drawable.ic_input_delete);
			relativeLayout.addView(contactButton, lp);
			relativeLayout.addView(contactIcon);
			relativeLayout.addView(contactDeleteButton);
			RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) contactDeleteButton.getLayoutParams();
			lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			contactDeleteButton.setLayoutParams(lp2);
			friendListLinearLayout.addView(relativeLayout, lp);

			// button listeners
			contactButton.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					GroupMembersPageActivity.this.startContactIndividualPageActivity(f);
				}
			});

			// display an alert dialog before deleting
			contactDeleteButton.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					// remove contact.. two-way
					Builder builder = new AlertDialog.Builder(GroupMembersPageActivity.this);
					builder.setMessage("Are you sure to delete " + f.getName() + "?");
					builder.setCancelable(true);
					builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
							try
							{
								clientApp.removeFriend(f);
								friendListLinearLayout.removeView(relativeLayout);
								Toast.makeText(GroupMembersPageActivity.this, f.getName() + " removed",
										Toast.LENGTH_LONG).show();
							} catch (ClientAppException e)
							{
								// TODO Auto-generated catch block
								Toast.makeText(GroupMembersPageActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
							}

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
	 * navigate to contact individual page, save email, name and phone.
	 * 
	 * @param f the f
	 */
	public void startContactIndividualPageActivity(Person f)
	{
		Log.d(TAG, "Starting ContactIndividualPageActivity");
		Intent intent = new Intent(this, ContactIndividualPageActivity.class);
		intent.putExtra("email", f.getEmail());
		intent.putExtra("name", f.getName());
		intent.putExtra("phone", f.getPhoneNumber());
		intent.putExtra("previous", groupName);
		intent.putExtra("parent", "Groups");
		if (nearby)
			intent.putExtra("nearby", true);
		this.startActivity(intent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause()
	{
		super.onPause();
		Log.d(TAG, "onPause");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart()
	{
		super.onRestart();
		Log.d(TAG, "onRestart");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume()
	{
		super.onResume();
		Log.d(TAG, "onResume");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart()
	{
		super.onStart();
		Log.d(TAG, "onStart");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop()
	{
		super.onStop();
		Log.d(TAG, "onStop");
	}

	/**
	 * Return to specific group page when back is pressed
	 */
	public void onBackPressed()
	{
		Log.d(TAG, "Detected back pressed");
		Intent back;
		if (groupName.equalsIgnoreCase("unclassified"))
			back = new Intent(this, GroupUnclassifiedPageActivity.class);
		else if (nearby)
			back = new Intent(this, GroupsPageActivity.class);
		else
			back = new Intent(this, GroupIndividualPageActivity.class);

		back.putExtra("groupName", groupName);
		this.startActivity(back);
	}
}
