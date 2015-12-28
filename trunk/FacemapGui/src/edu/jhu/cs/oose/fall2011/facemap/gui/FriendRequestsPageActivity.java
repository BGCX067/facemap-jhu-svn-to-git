package edu.jhu.cs.oose.fall2011.facemap.gui;

import edu.jhu.cs.oose.fall2011.facemap.client.ClientApp;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientAppException;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendRequest;
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
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Show friend requests from others. user can accept and deny requests.
 * @author Chuan Huang, Ying Dou
 *
 */
public class FriendRequestsPageActivity extends OptionMenu {
	/**
	 * Name of the class, used for debugging
	 */
	private final String TAG = "FriendRequestsPageActivity";
	
	/**
	 * ClientApp object. phone model interface object
	 */
	private ClientApp clientApp;
	
	/**
	 * create view to "friend_requests.xml", 
	 * create buttons that display requester names
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_requests);
        
        // get ClientApp object from the application
        ClientAppApplication app = (ClientAppApplication)getApplication();
        this.clientApp = app.getClientApp();
        
        // create linear layout to put the buttons
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.friendRequestListLinearLayout);
        if (clientApp.getLoggedInUser().getFriendRequestsReceived().size() == 0) {
        	Toast.makeText(this, "no friend request", Toast.LENGTH_LONG).show();
        	//this.finish();
        }
        
        // for each friend request, create the button, set the text, size and listener
        for (final FriendRequest fr : clientApp.getLoggedInUser().getFriendRequestsReceived()) {
        	Button requestButton  = new Button(this);
        	requestButton.setText(fr.getRequestorUserId());
        	requestButton.setTextSize((float) 20);
        	
        	linearLayout.addView(requestButton);
        	
        	requestButton.setOnClickListener(new OnClickListener() {
        		/* show a alert dialog to ask the user to accept or deny the request
        		 * (non-Javadoc)
        		 * @see android.view.View.OnClickListener#onClick(android.view.View)
        		 */
				@Override
				public void onClick(View v) {
					// dialog
					Builder builder = new AlertDialog.Builder(FriendRequestsPageActivity.this);
		            builder.setMessage("Accept Friend Request?");
		            builder.setCancelable(true);
		            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int which) {
		                	try {
		                		clientApp.respondToFriendRequest(fr, true);
		                		FriendRequestsPageActivity.this.startActivity(new android.content.Intent(FriendRequestsPageActivity.this, FriendRequestsPageActivity.class));
		                	} catch (ClientAppException e) {
		                		// show the error message
		                		Toast.makeText(FriendRequestsPageActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
		                	}
		                }
		            });
		            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int which) {
		                	try {
		                		clientApp.respondToFriendRequest(fr, false);
		                		FriendRequestsPageActivity.this.startActivity(new android.content.Intent(FriendRequestsPageActivity.this, FriendRequestsPageActivity.class));
		                	} catch (ClientAppException e) {
		                		// show the error message
		                		Toast.makeText(FriendRequestsPageActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
		                	}
		                }
		            });
		            AlertDialog dialog = builder.create();
		            dialog.show();
				}
			});
        }
	}
		
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG,"onDestroy");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG,"onPause");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(TAG,"onRestart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG,"onResume");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG,"onStart");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG,"onStop");
	}
	
	/**
	 * Return to menu page when back is pressed
	 */
	public void onBackPressed()
	{
		Log.d(TAG, "Detected back pressed");
		Intent back = new Intent(this, MenuPageActivity.class);
		this.startActivity(back);
	}
}
