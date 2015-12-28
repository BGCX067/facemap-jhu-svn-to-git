package edu.jhu.cs.oose.fall2011.facemap.gui;

import java.util.HashSet;
import java.util.Set;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientApp;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientAppException;
import edu.jhu.cs.oose.fall2011.facemap.domain.Location;
import edu.jhu.cs.oose.fall2011.facemap.domain.Person;
import edu.jhu.cs.oose.fall2011.facemap.entity.RandomLocationRetriever;
import edu.jhu.cs.oose.fall2011.facemap.server.LocationRetriever;

/**
 * Shows all of people within a certain range of the user. Default range set at 1000 miles
 * 
 * @author Chuan Huang (Kevin)
 */
public class GroupNearbyPageActivity extends OptionMenu
{

	/** The TAG. */
	final String TAG = "GroupNearbyPageActivity";

	/** The friend group. */
	Set<Person> nearbyFriends;

	/**
	 * ClientApp object. phone model interface object
	 */
	private ClientApp clientApp;

	/** The range definition for nearby friends in miles */
	private int range;

	/**
	 * global application
	 */
	private ClientAppApplication app;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_nearby);

		Button membersButton = (Button) this.findViewById(R.id.nearbyMembersButton);
		Button blockMembersButton = (Button) this.findViewById(R.id.blockNearbyMembersButton);
		Button unblockMembersButton = (Button) this.findViewById(R.id.unblockNearbyMembersButton);
		Button emailMembersButton = (Button) this.findViewById(R.id.sendNearbyGroupEmailButton);

		app = (ClientAppApplication) getApplication();
		clientApp = app.getClientApp();

		range = this.getIntent().getIntExtra("range", 1);
		Log.d(TAG, "Range: " + range);

		this.findNearbyFriendList();

		if (membersButton != null)
		{
			membersButton.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					GroupNearbyPageActivity.this.startMembersPageActivity("Nearby");
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
					GroupNearbyPageActivity.this.PerformAction(ActionType.BLOCK);
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
					GroupNearbyPageActivity.this.PerformAction(ActionType.UNBLOCK);
				}
			});
		}
		if (emailMembersButton != null)
		{
			emailMembersButton.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					String[] email = new String[nearbyFriends.size()];
					int i = 0;
					for (Person p : nearbyFriends)
					{
						email[i] = p.getEmail();
						i++;
					}
					GroupNearbyPageActivity.this.startEmailMembersPageActivity(email);
				}
			});
		}
	}

	private void findNearbyFriendList()
	{
		Log.d(TAG, "Finding nearby friends");
		nearbyFriends = new HashSet<Person>();

		int myLat, myLon;
		CurrentLocation myCurrentLocation = new CurrentLocation(this);
		Location myLocation = myCurrentLocation.retrieveLocation();
		myLat = (int) (myLocation.getLatitude() * 1000000);
		myLon = (int) (myLocation.getLongitude() * 1000000);

		Location friendLocation;
		// for testing
		LocationRetriever randomLocation = new RandomLocationRetriever(39.323688, -76.617157, 0.02, 0.02);

		int lat, lon;
		for (Person f : this.clientApp.getLoggedInUser().getFriends())
		{
			// for testing, only 111 and 222 have locations
			if (f.getName().equals("111") || f.getName().equals("222"))
			{
				try
				{
					friendLocation = clientApp.getFriendLocation(f);

				} catch (ClientAppException e)
				{
					continue;
				}
			} else
			{
				friendLocation = randomLocation.retrieveLocation();
			}

			lat = (int) (friendLocation.getLatitude() * 1000000);
			lon = (int) (friendLocation.getLongitude() * 1000000);
			int temp = 1000000;
			double dist = Functions.findDistanceMile((double)myLat/temp, (double)myLon/temp, (double)lat/temp, (double)lon/temp);
			Log.d(TAG, "Distance: " + dist);
			if (dist <= range)
			{
				nearbyFriends.add(f);
				Log.d(TAG, "Added Person: " + f.getName());
			}
		}
	}

	/**
	 * Start members page activity.
	 * 
	 * @param groupName the group name
	 */
	protected void startMembersPageActivity(String groupName)
	{
		Log.d(TAG, "Starting MembersPageActivity");
		Intent intent = new Intent(this, GroupMembersPageActivity.class);
		intent.putExtra("groupName", "Nearby");
		intent.putExtra("nearby", true);
		app.setPersonList(nearbyFriends);
		Log.d(TAG, "Transferring to app");
		this.startActivity(intent);
	}

	/**
	 * Perform desired option on nearby group
	 * 
	 * @param action the action
	 */
	protected void PerformAction(ActionType action)
	{
		if (action == ActionType.BLOCK)
		{
			for (Person p : nearbyFriends)
			{
				clientApp.getLoggedInUser().blockFriend(p);
				Log.d(TAG, "Blocked: " + p.getName());
			}
			Toast.makeText(this, "Nearby Friends Blocked", Toast.LENGTH_LONG).show();
		} else if (action == ActionType.UNBLOCK)
		{
			for (Person p : nearbyFriends)
			{
				if (clientApp.getLoggedInUser().getBlockedFriends().contains(p))
				{
					clientApp.getLoggedInUser().unblockFriend(p);
					Log.d(TAG, "Unblocked: " + p.getName());
				}
			}
			Toast.makeText(this, "Nearby Friends Unblocked", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Start email members page activity.
	 * 
	 * @param email the email
	 */
	protected void startEmailMembersPageActivity(String[] email)
	{
		Log.d(TAG, "Starting SendGroupEmailPageActivity");
		Intent sendEmail = new Intent(android.content.Intent.ACTION_SEND);

		Log.d(TAG, "Emails: " + email);

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
	 * Return to the group page activity
	 */
	public void onBackPressed()
	{
		Log.d(TAG, "Detected back pressed");
		Intent back = new Intent(this, GroupsPageActivity.class);
		this.startActivity(back);
	}
}