package edu.jhu.cs.oose.fall2011.facemap.gui;

import java.util.HashSet;
import java.util.Set;

import edu.jhu.cs.oose.fall2011.facemap.client.ClientApp;
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
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * select_unclassified.xml page activity
 * Select members for unclassified group for a particular action
 * 
 * @author Ying Dou, Chuan Huang (Kevin)
 * 
 */
public class SelectUnclassifiedMembersPageActivity extends OptionMenu
{
	/**
	 * Name of the class, used for debugging
	 */
	private final String TAG = "SelectUnclassifiedMembersPageActivity";
	/**
	 * ClientApp object. phone model interface object
	 */
	private ClientApp clientApp;
	/**
	 * DELETE, BLOCK or UNBLOCK, no ADD for unclassified
	 */
	private ActionType action;
	/**
	 * contacts to be displayed
	 */
	private Set<Person> actionList;
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

		// get action type
		if (getIntent().getExtras() != null)
		{
			Log.d(TAG, "getExtras() not empty");
			action = (ActionType) getIntent().getSerializableExtra("action");
		}

		// set title
		TextView groupActionTitleText = (TextView) findViewById(R.id.GroupActionTitle);
		groupActionTitleText.setText("Unclassified");
		groupActionTitleText.setTextSize((float) 40);

		// store friend list
		actionList = Functions.getUnclassifiedFriends(clientApp);

		// create checkboxes and add them to linear layout
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
		if (actionButton != null)
		{
			actionButton.setText(action.toString());
			actionButton.setOnClickListener(new OnClickListener()
			{
				/*
				 * set listener, display alert dialog first to confirm
				 * pass the email addresses of selected members and pass them back to the previous activity
				 * (non-Javadoc)
				 * @see android.view.View.OnClickListener#onClick(android.view.View)
				 */
				@Override
				public void onClick(View v)
				{
					// dialog
					Builder builder = new AlertDialog.Builder(SelectUnclassifiedMembersPageActivity.this);
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
