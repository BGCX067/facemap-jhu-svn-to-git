package edu.jhu.cs.oose.fall2011.facemap.gui;


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
import android.widget.Toast;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientApp;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientAppException;
import edu.jhu.cs.oose.fall2011.facemap.domain.Person;

/**
 * group_unclassified.xml page activiy.
 * Group with all of the members who are unclassified into a customized group.
 * @author Chuan Huang, Ying Dou
 *
 */
public class GroupUnclassifiedPageActivity extends OptionMenu {
	
	/** Name of the class, used for debugging. */
	private final String TAG = "GroupIndividualPageActivity";
	
	/** ClientApp object. phone model interface object. */
	private ClientApp clientApp;
	
	/**
	 * create view to "group_unclassified.xml"
	 * Gets ClientApp object from application
	 * find buttons on the layout and set button listeners that edit this group
	 *
	 * @param savedInstanceState the saved instance state
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_unclassified);
        
        // gets ClientApp object from application
        ClientAppApplication app = (ClientAppApplication)getApplication();
        clientApp = app.getClientApp();
        
        // get buttons from the layout
        Button membersButton = (Button)this.findViewById(R.id.unclassifiedMembersButton);
        Button deleteMembersButton = (Button)this.findViewById(R.id.deleteUnclassifiedMembersButton);
        Button blockMembersButton = (Button)this.findViewById(R.id.blockUnclassifiedMembersButton);
        Button unblockMembersButton = (Button)this.findViewById(R.id.unblockUnclassifiedMembersButton);
        Button sendGroupEmailButton = (Button)this.findViewById(R.id.sendGroupUnclassifiedEmailButton);
        Button blockGroupButton = (Button)this.findViewById(R.id.blockGroupUnclassifiedButton);
        Button unblockGroupButton = (Button)this.findViewById(R.id.unblockGroupUnclassifiedButton);
        
        // set button listeners
        if (membersButton != null) {
        	membersButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					GroupUnclassifiedPageActivity.this.startMembersPageActivity("Unclassified");
				}
			});
        }
        /*for DELETE, BLOCK, UNBLOCK actions, they all navigate a page to select members.
         * the selected member emails will be sent to back, then action will be performed
         */
        if (deleteMembersButton != null) {
        	deleteMembersButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					GroupUnclassifiedPageActivity.this.startSelectMembersPageActivity(ActionType.DELETE);
				}
			});
        }
        if (blockMembersButton != null) {
        	blockMembersButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					GroupUnclassifiedPageActivity.this.startSelectMembersPageActivity(ActionType.BLOCK);
				}
			});
        }
        if (unblockMembersButton != null) {
        	unblockMembersButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					GroupUnclassifiedPageActivity.this.startSelectMembersPageActivity(ActionType.UNBLOCK);
				}
			});
        }
        // send group email
        if (sendGroupEmailButton != null) {
        	sendGroupEmailButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String[] email = new String[Functions.getUnclassifiedFriends(clientApp).size()];
					int i = 0;
					for (Person p : Functions.getUnclassifiedFriends(clientApp)) {
						email[i] = p.getEmail();
						i++;
					}
					GroupUnclassifiedPageActivity.this.startSendGroupEmailPageActivity(email);
				}
			});
        }
        // block members in the group, display alert dialog to confirm first
        if (blockGroupButton != null) {
        	blockGroupButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Builder builder = new AlertDialog.Builder(GroupUnclassifiedPageActivity.this);
					builder.setMessage("Are you sure to block all members in the group?");
					builder.setCancelable(true);
					builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
							Set<Person> f = Functions.getUnclassifiedFriends(clientApp);
							for (Person p : f) {
								clientApp.getLoggedInUser().blockFriend(p);
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
        // unblock members in the group, display alert dialog to confirm first
        if (unblockGroupButton != null) {
        	unblockGroupButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Builder builder = new AlertDialog.Builder(GroupUnclassifiedPageActivity.this);
					builder.setMessage("Are you sure to unblock all members in the group?");
					builder.setCancelable(true);
					builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
							Set<Person> f = Functions.getUnclassifiedFriends(clientApp);
							for (Person p : f) {
								if (clientApp.getLoggedInUser().getBlockedFriends().contains(p))
									clientApp.getLoggedInUser().unblockFriend(p);
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
	 * Navigate to specific member page
	 * group name is passed, so when an individual member is deleted, it can go back to this page
	 * @param groupName the group name
	 */
	public void startMembersPageActivity(String groupName)
	{
		Log.d(TAG, "Starting MembersPageActivity");
		Intent intent = new Intent(this, GroupMembersPageActivity.class);
		intent.putExtra("groupName", groupName);
		this.startActivity(intent);
	}
	
	/**
	 * Lets the user select members for specific action
	 *
	 * @param action DELETE, BLOCK, UNBLOCK
	 */
	public void startSelectMembersPageActivity(ActionType action) {
		Log.d(TAG, "Starting SelectMembersPageActivity");
		Intent intent = new Intent(this, SelectUnclassifiedMembersPageActivity.class);
		intent.putExtra("action",action);
		this.startActivityForResult(intent, 1);
	}
	
	/**
	 * Starts android email client to send email out to all of the people in the parameter
	 *
	 * @param email email addresses of group members
	 */
	public void startSendGroupEmailPageActivity(String[] email){
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
	 * Get selected contact emails and edit group according to the action type
	 *
	 * @param requestCode the request code
	 * @param resultCode the result code
	 * @param data the data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK && requestCode==1){
			String [] selectedEmails = data.getStringArrayExtra("selected");
			ActionType action = (ActionType)data.getSerializableExtra("action");
			switch (action) {
			case BLOCK:
				for (int i = 0; i < selectedEmails.length; i++) {
					clientApp.getLoggedInUser().blockFriend(Functions.SearchPersonByEmail(clientApp.getLoggedInUser().getFriends(), selectedEmails[i]));
				}
				break;
			case DELETE:
				for (int i = 0; i < selectedEmails.length; i++) {
					try {
						clientApp.removeFriend(Functions.SearchPersonByEmail(clientApp.getLoggedInUser().getFriends(), selectedEmails[i]));
					} catch (ClientAppException e) {
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), "fail to remove friend", Toast.LENGTH_LONG).show();
					}
				}
				break;
			case UNBLOCK:
				for (int i = 0; i < selectedEmails.length; i++) {
					clientApp.getLoggedInUser().unblockFriend(Functions.SearchPersonByEmail(clientApp.getLoggedInUser().getFriends(), selectedEmails[i]));
				}
				break;
			}
				
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
	
	/**
	 * Return to he Groups Page Activity when back key is pressed
	 */
	public void onBackPressed()
	{
		Log.d(TAG, "Detected back pressed");
		Intent back = new Intent(this, GroupsPageActivity.class);
		this.startActivity(back);
	}
}
