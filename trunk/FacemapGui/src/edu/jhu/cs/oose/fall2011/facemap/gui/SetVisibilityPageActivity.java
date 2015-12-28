package edu.jhu.cs.oose.fall2011.facemap.gui;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientApp;
import edu.jhu.cs.oose.fall2011.facemap.domain.Person;

/**
 * set_visibility.xml page activity
 * Sets the user's visibility as either visible to all or invisible to all
 * 
 * @author Ying Dou, Chuan Huang (Kevin)
 */
public class SetVisibilityPageActivity extends OptionMenu
{

	/**
	 * Name of the class, used for debugging
	 */
	private final String TAG = "SetVisibilityPageActivity";
	/**
	 * ClientApp object, phone model interface
	 */
	private ClientApp clientApp;

	/**
	 * save the visibility information
	 */
	private Intent returnMessage;

	/**
	 * create view to set_visibility.xml
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_visibility);

		ClientAppApplication app = (ClientAppApplication) getApplication();
		clientApp = app.getClientApp();
		returnMessage = new Intent();
	}

	/**
	 * Nobody button click listener
	 * Block all contacts
	 * @param view
	 */
	public void VisibleToNobodyButtonClickHandler(View view)
	{
		switch (view.getId())
		{
			case R.id.visibleToNobodyButton:
				for (Person P : clientApp.getLoggedInUser().getFriends())
				{
					clientApp.getLoggedInUser().blockFriend(P);
					Log.d(TAG, "Blocked: " + P.getName());
				}

				returnMessage.putExtra("visibility", "Invisible");
				if (getParent() == null)
				{
					setResult(Activity.RESULT_OK, returnMessage);
				} else
				{
					getParent().setResult(Activity.RESULT_OK, returnMessage);
				}
				this.finish();
				break;
		}
	}

	/**
	 * All button click listener
	 * unblock all contacts
	 * @param view
	 */
	public void VisibleToAllButtonClickHandler(View view)
	{
		switch (view.getId())
		{
			case R.id.visibleToAllButton:
				Set<Person> blocked = new HashSet<Person>(clientApp.getLoggedInUser().getBlockedFriends());

				for (Person P : blocked)
				{
					if (clientApp.getLoggedInUser().getBlockedFriends().contains(P))
					{
						Log.d(TAG, "Unblocked: " + P.getName());
						clientApp.getLoggedInUser().unblockFriend(P);
						Log.d(TAG, "Unblocked Success");
					}
				}

				returnMessage.putExtra("visibility", "Visible");
				if (getParent() == null)
				{
					setResult(Activity.RESULT_OK, returnMessage);
				} else
				{
					getParent().setResult(Activity.RESULT_OK, returnMessage);
				}
				this.finish();
				break;
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
	 * go back to my info page
	 */
	public void onBackPressed()
	{
		Log.d(TAG, "Detected back pressed");
		Intent back = new Intent(this, MyInfoPageActivity.class);
		this.startActivity(back);
	}
}
