package edu.jhu.cs.oose.fall2011.facemap.gui;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientApp;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientAppException;

/**
 * For an individual contact page (contact_individual.xml) activity, 
 * including send email, send SMS, make phone call, block, unblock, add to group,
 * and delete.
 * 
 * @author Chuan Huang, Ying Dou
 * 
 */
public class ContactIndividualPageActivity extends OptionMenu
{

	/** Name of the class, used for debugging. */
	private final String TAG = "ContactIndividualPageActivity";

	/** ClientApp object. phone model interface object */
	private ClientApp clientApp;

	/** name. */
	private String contactName;

	/** email. */
	private String contactEmail;

	/** phone number. */
	private String contactPhone;

	/**
	 * create the view to "contact_individual"
	 * get the ClientApp object from the application
	 * get the name, email, phone from intent, which is saved from previous activity (AllContactsPageActivity) 
	 * and create buttons, listeners.
	 * 
	 * @param savedInstanceState the saved instance state
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_individual);
		
		// get ClientApp object from application
		ClientAppApplication app = (ClientAppApplication) getApplication();
		this.clientApp = app.getClientApp();
		
		// get friend name, email and phone from intent, which are saved from the previous activity
		if (getIntent().getExtras() != null)
		{
			Log.d(TAG, "getExtras() not empty");
			contactName = getIntent().getStringExtra("name");
			contactEmail = getIntent().getStringExtra("email");
			contactPhone = getIntent().getStringExtra("phone");
		}

		// set the page title to the name of the contact
		TextView contactNameTextView = (TextView) this.findViewById(R.id.contactIndividualName);
		/* a image that shows whether the contact is blocked or not.
		 * If not, the image will be hidden
		 */
		final ImageView contactBlockedView = (ImageView) this.findViewById(R.id.contactBlockedImageView);
		// contact image
		ImageView contactIconImageView = (ImageView) this.findViewById(R.id.contactIconAtTitle);

		// Create buttons to communicate and edit
		Button sendSMSButton = (Button) this.findViewById(R.id.sendSMSButton);
		Button sendEmailButton = (Button) this.findViewById(R.id.sendEmailButton);
		Button callButton = (Button) this.findViewById(R.id.callButton);
		Button addToGroupsButton = (Button) this.findViewById(R.id.addToGroupsButton);
		Button blockButton = (Button) this.findViewById(R.id.blockContactButton);
		Button unblockButton = (Button) this.findViewById(R.id.unblockContactButton);
		Button deleteButton = (Button) this.findViewById(R.id.deleteContactButton);
		
		/* set the title of the page to contact name
		 * Text size 40
		 */
		if (contactNameTextView != null)
		{
			contactNameTextView.setText(contactName);
			contactNameTextView.setTextSize((float) 40);
		}
		// if the contact if not blocked, set the block indicator image to be invisible
		if (!Functions.isBlocked(contactEmail, clientApp))
		{
			contactBlockedView.setVisibility(View.GONE);
		}
		// set the contact image...
		if (contactIconImageView != null)
		{
			// set image...
		}
		// set text, size, listeners to all the buttons
		if (sendSMSButton != null)
		{
			sendSMSButton.setText("Send SMS\n" + contactPhone);
			sendSMSButton.setTextSize((float) 20);
			sendSMSButton.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					ContactIndividualPageActivity.this.startSendSMSPageActivity(contactPhone);
				}
			});
		}
		if (sendEmailButton != null)
		{
			sendEmailButton.setText("Send Email\n" + contactEmail);
			sendEmailButton.setTextSize((float) 20);
			sendEmailButton.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					ContactIndividualPageActivity.this.startSendEmailPageActivity(contactEmail);
				}
			});
		}
		if (callButton != null)
		{
			callButton.setText("Call\n" + contactPhone);
			callButton.setTextSize((float) 20);
			callButton.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					ContactIndividualPageActivity.this.startCallPageActivity(contactPhone);
				}
			});
		}
		if (addToGroupsButton != null)
		{
			addToGroupsButton.setText("Add To Groups");
			addToGroupsButton.setTextSize((float) 20);
			addToGroupsButton.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					ContactIndividualPageActivity.this.startAddToGroupsPageActivity(contactEmail);
				}
			});
		}
		if (blockButton != null)
		{
			blockButton.setText("Block");
			blockButton.setTextSize((float) 20);
			blockButton.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					// test whether the contact is already blocked or not
					if (Functions.isBlocked(contactEmail, clientApp))
					{
						Toast.makeText(ContactIndividualPageActivity.this, "already blocked", Toast.LENGTH_SHORT).show();
					} else
					{
						clientApp.getLoggedInUser().blockFriend(
								Functions.SearchPersonByEmail(clientApp.getLoggedInUser().getFriends(), contactEmail));
						Toast.makeText(ContactIndividualPageActivity.this, contactName + " blocked", Toast.LENGTH_SHORT).show();
						contactBlockedView.setVisibility(View.VISIBLE);
					}
				}
			});
		}
		if (unblockButton != null)
		{
			unblockButton.setText("Unblock");
			unblockButton.setTextSize((float) 20);
			unblockButton.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					// test whether the contact is blocked or not
					if (!Functions.isBlocked(contactEmail, clientApp))
					{
						Toast.makeText(ContactIndividualPageActivity.this, "not blocked", Toast.LENGTH_SHORT).show();
					} else
					{
						clientApp.getLoggedInUser().unblockFriend(
								Functions.SearchPersonByEmail(clientApp.getLoggedInUser().getFriends(), contactEmail));
						Toast.makeText(ContactIndividualPageActivity.this, contactName + " unblocked",
								Toast.LENGTH_SHORT).show();
						contactBlockedView.setVisibility(View.GONE);

					}
				}
			});
		}
		/* for delete action, set an alert dialog to confirm the action
		 * After the contact is deleted, go to the correct previous page
		 */
		if (deleteButton != null)
		{
			deleteButton.setText("Delete");
			deleteButton.setTextSize((float) 20);
			deleteButton.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					Builder builder = new AlertDialog.Builder(ContactIndividualPageActivity.this);
					builder.setMessage("Are you sure to delete " + contactName + "?");
					builder.setCancelable(true);
					builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
							try
							{
								clientApp.removeFriend(Functions.SearchPersonByEmail(
										clientApp.getLoggedInUser().getFriends(), contactEmail));
								Toast.makeText(ContactIndividualPageActivity.this, contactName + " removed",
										Toast.LENGTH_LONG).show();
								/* get information from intent to know the previous page
								 * if previous page is "all_contacts,xml", go back there
								 * else, go to the right group page
								 */
								if (getIntent().getStringExtra("parent").equals("AllContacts"))
								{
									ContactIndividualPageActivity.this.startAllContactsPageActivity();
								} else
								{
									ContactIndividualPageActivity.this.startGroupMembersPageActivity(getIntent().getStringExtra(
											"previous"));
								}
								finish();
							} catch (ClientAppException e)
							{
								Toast.makeText(ContactIndividualPageActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
	 * Navigate to custom SMS sending page.
	 * save phone number, information whether the previous page is group, and the group name if the 
	 * previous page is a group
	 * 
	 * @param phone phone number of contact to send SMS to
	 */
	public void startSendSMSPageActivity(String phone)
	{
		Log.d(TAG, "Starting SendSMSPageActivity");
		Intent intent = new Intent(this, SendSMSPageActivity.class);
		intent.putExtra("phone", phone);
		intent.putExtra("parent", getIntent().getStringExtra("parent"));
		intent.putExtra("group", getIntent().getStringExtra("previous"));
		this.startActivity(intent);
	}

	/**
	 * Send email to contact.
	 * 
	 * @param email email address of contact
	 */
	public void startSendEmailPageActivity(String email)
	{
		Log.d(TAG, "Starting SendEmailPageActivity");

		Intent sendEmail = new Intent(android.content.Intent.ACTION_SEND);
		String emailList[] = { email };

		sendEmail.setType("text/plain");
		sendEmail.putExtra(Intent.EXTRA_EMAIL, emailList);
		sendEmail.putExtra(Intent.EXTRA_SUBJECT, "New Email Subject");
		sendEmail.putExtra(Intent.EXTRA_TEXT, "Hello World :D");
		try
		{
			// this.startActivity(sendEmail);
			this.startActivity(Intent.createChooser(sendEmail, "Send mail..."));
		} catch (android.content.ActivityNotFoundException e)
		{
			Toast.makeText(this, "Error, no client found", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Call the phone number.
	 * 
	 * @param phone number to call
	 */
	public void startCallPageActivity(String phone)
	{
		Log.d(TAG, "Starting CallPageActivity");
		try
		{
			Intent callPhone = new Intent(Intent.ACTION_CALL);
			callPhone.setData(Uri.parse("tel:" + phone));
			this.startActivity(callPhone);
		} catch (android.content.ActivityNotFoundException e)
		{
			Toast.makeText(this, "Error, call failed", Toast.LENGTH_SHORT).show();
		}
	}


	/**
	 * navigate to add_to_groups.xml page, where this contact can be added to several groups
	 * 
	 * @param email the email
	 */
	public void startAddToGroupsPageActivity(String email)
	{
		Log.d(TAG, "Starting AddToGroupsPageActivity");
		Intent intent = new Intent(this, AddToGroupsPageActivity.class);
		intent.putExtra("email", email);
		this.startActivity(intent);
	}

	/**
	 * Navigate to previous page after contact is deleted and the previous page is "all_contacts.xml"
	 */
	public void startAllContactsPageActivity()
	{
		Log.d(TAG, "Starting AllContactsPageActivity");
		Intent intent = new Intent(this, AllContactsPageActivity.class);
		this.startActivity(intent);
	}

	/**
	 * Start group members page activity, which shows the names of the members in the group
	 * Pass group name to the next activity, so the next activity knows which group to show
	 * @param groupName the group name
	 */
	public void startGroupMembersPageActivity(String groupName)
	{
		Log.d(TAG, "Starting GroupMembersPageActivity");
		Intent intent = new Intent(this, GroupMembersPageActivity.class);
		intent.putExtra("groupName", groupName);
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
	 * Return to the correct page when back key is pressed
	 */
	public void onBackPressed()
	{
		Log.d(TAG, "Detected back pressed");

		String parent = getIntent().getStringExtra("parent");
		String group = getIntent().getStringExtra("previous");

		Intent back;
		if (getIntent().getBooleanExtra("nearby", false))
		{
			back = new Intent(this, GroupsPageActivity.class);
		} else if (parent.equalsIgnoreCase("groups"))
		{
			back = new Intent(this, GroupMembersPageActivity.class);
			back.putExtra("groupName", group);
		} else
			back = new Intent(this, AllContactsPageActivity.class);

		this.startActivity(back);
	}
}
