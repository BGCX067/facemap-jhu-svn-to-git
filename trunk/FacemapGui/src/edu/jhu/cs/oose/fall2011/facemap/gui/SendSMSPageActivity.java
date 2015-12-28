package edu.jhu.cs.oose.fall2011.facemap.gui;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Send a SMS to one person with desired message.
 * 
 * @author Chuan Huang (Kevin)
 */
public class SendSMSPageActivity extends OptionMenu
{
	/**
	 * send sms button
	 */
	Button sendSMS;
	/**
	 * to input message
	 */
	EditText SMSMessage;
	/**
	 * phone number to send
	 */
	String phoneNumber;

	/**
	 * create view to send_sms.xml
	 * get phone number from intent
	 * set the phone number to the textview
	 */
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.send_sms);

		Intent phone = this.getIntent();
		phoneNumber = phone.getStringExtra("phone");

		TextView number = (TextView) this.findViewById(R.id.txtPhoneNo);
		number.setText(phoneNumber);

		sendSMS = (Button) this.findViewById(R.id.btnSendSMS);
		SMSMessage = (EditText) this.findViewById((R.id.txtMessage));

		/**
		 * Sends text when the user clicks on the send button
		 */
		sendSMS.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				String message = SMSMessage.getText().toString();
				
				if (phoneNumber.length() > 0 && message.length() > 0)
				{
					String parent = SendSMSPageActivity.this.getIntent().getStringExtra("parent");
					Intent tempIntent;
					if (parent.equalsIgnoreCase("groups"))
					{
						tempIntent = new Intent(SendSMSPageActivity.this, GroupMembersPageActivity.class);
						tempIntent.putExtra("groupName", SendSMSPageActivity.this.getIntent().getStringExtra("group"));
					}
					else
						tempIntent = new Intent(SendSMSPageActivity.this, AllContactsPageActivity.class);
					
					PendingIntent pi = PendingIntent.getActivity(SendSMSPageActivity.this, 0, tempIntent, 0);
					android.telephony.SmsManager sms = android.telephony.SmsManager.getDefault();
					sms.sendTextMessage(phoneNumber, null, message, pi, null);
				} else
				{
					Toast.makeText(getBaseContext(), "Please enter message.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		
	}

}
