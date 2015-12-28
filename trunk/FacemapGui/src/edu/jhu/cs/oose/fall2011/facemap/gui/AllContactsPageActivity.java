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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientApp;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientAppException;
import edu.jhu.cs.oose.fall2011.facemap.domain.Person;

/**
 * All contacts page (all_contacts.xml) activity
 * Show buttons of all contacts of the currently logged in user
 * The user can click on a button to navigate to corresponding friend page,
 * or click a delete button to delete a contact,
 * or click add button to add a new contact
 * @author Chuan Huang, Ying Dou
 * 
 */
public class AllContactsPageActivity extends OptionMenu
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
	 * friends of user
	 */
	private Set<Person> friends;

	/**
	 * Create the layout view "all_contacts" 
	 * Get the phone model interface object ClientApp.
	 * get friend information from the interface object and set the layouts and buttons
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_contacts);

		// get phone model interface object to retrieve model information
		ClientAppApplication app = (ClientAppApplication) getApplication();
		this.clientApp = app.getClientApp();
		
		// get friends of the logged in user from the model
		this.friends = clientApp.getLoggedInUser().getFriends();

		// get the linear layout from the all_contacts.xml layout page
		final LinearLayout friendListLinearLayout = (LinearLayout) findViewById(R.id.friendListLinearLayout);

		/* for each friend, create two buttons, friend name button, friend delete button,
		 * and a image of the friend
		 * add the buttons and the image to a relative layout,
		 * 
		 * ----------------------------------------
		 * image        friend_name        delete     } a relative layout to be added to the linear layout
		 * ----------------------------------------
		 * add add the relative layout to the linear layout friendListLinearLayout
		 */
		for (final Person f : this.friends)
		{
			// create relative layout
			final RelativeLayout relativeLayout = new RelativeLayout(this);
			// set the relatvie layout width and height
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			// create friend name button, set the text to friend name and text size to 30
			Button contactButton = new Button(this);
			contactButton.setText(f.getName());
			contactButton.setTextSize((float) 30);
			// create image
			ImageView contactIcon = new ImageView(this);
			// set the image
			contactIcon.setImageResource(R.drawable.icon); // change later!!!
			// create delete button and set the button image
			ImageButton contactDeleteButton = new ImageButton(this);
			contactDeleteButton.setImageResource(android.R.drawable.ic_input_delete);
			// add buttons and image to the relative layout in the default position
			relativeLayout.addView(contactButton, lp);
			relativeLayout.addView(contactIcon);
			relativeLayout.addView(contactDeleteButton);
			// get the delete button layout parameters and set it to aligh right and buttom
			RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) contactDeleteButton.getLayoutParams();
			lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			contactDeleteButton.setLayoutParams(lp2);
			// add the relativelayout to the linear layout
			friendListLinearLayout.addView(relativeLayout, lp);
			
			// set friend name button listener, if it is clicked, navigate to friend page
			contactButton.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					AllContactsPageActivity.this.startContactIndividualPageActivity(f);
				}
			});
			/* set delete button listener
			 * when the button is clicked, show a alert dialog to confirm the user really wants to delete
			 * the contact.
			 * If yes, delete the contact and remove the relative layout from the linear layout to update the view
			 */
			contactDeleteButton.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					Builder builder = new AlertDialog.Builder(AllContactsPageActivity.this);
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
								Toast.makeText(AllContactsPageActivity.this, f.getName() + " removed",
										Toast.LENGTH_LONG).show();
							} catch (ClientAppException e)
							{
								Toast.makeText(AllContactsPageActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
	 * Handler that listens to click action of the add "+" button
	 * The function is added to the layout xml code.
	 * call the function that navigates to add contact page
	 * 
	 * @param view
	 */
	public void AddContactAtAllContactsButtonClickHandler(View view)
	{
		switch (view.getId())
		{
			case R.id.addContactButton:
				AllContactsPageActivity.this.startAddContactPageActivity();
				break;
		}
	}

	/**
	 * Navigate to the add new contacts page "add_contact.xml"
	 */
	public void startAddContactPageActivity()
	{
		Log.d(TAG, "Starting AddContactPageActivity");
		Intent intent = new Intent(this, AddContactPageActivity.class);
		this.startActivity(intent);
	}

	/**
	 * navigate to individual contact page, saves email, name and phone to set up buttons at 
	 * individual page
	 * @param f, contact Person object
	 */
	public void startContactIndividualPageActivity(Person f)
	{
		Log.d(TAG, "Starting ContactIndividualPageActivity");
		Intent intent = new Intent(this, ContactIndividualPageActivity.class);
		intent.putExtra("email", f.getEmail());
		intent.putExtra("name", f.getName());
		intent.putExtra("phone", f.getPhoneNumber());
		intent.putExtra("parent", "AllContacts");
		this.startActivityForResult(intent, 1);
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
		Intent back = new Intent(this, ContactsPageActivity.class);
		this.startActivity(back);
	}
}
