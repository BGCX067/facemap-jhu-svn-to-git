package edu.jhu.cs.oose.fall2011.facemap.gui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView.BufferType;
import android.widget.Toast;

/**
 * locate_range.xml page activity
 * Page allowing the user to define range to show on the map overlay. 
 * @author Ying Dou, Chuan Huang
 */
public class LocateRangePageActivity extends OptionMenu {
	/**
	 * Name of the class, used for debugging
	 */
	private final String TAG = "LocateRangePageActivity";
	
	/** Constant for choosing range for map */
	private final int MAP = 3;
	
	/** Constant for choosing range for groups */
	private final int GROUPS = 2;
	
	/**
	 * EditText to input range
	 */
	private EditText rangeText;
	/**
	 * key to retrieve range from savedInstanceState
	 */
	private String rangeTextKey;

	private int key;
	
	/**
	 * create view to locate_range.xml
	 * set the editText
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locate_range);
        
        key = this.getIntent().getIntExtra("key", MAP);
        Log.d(TAG, "Transfer key: " + key);
        
        this.rangeText = (EditText)this.findViewById(R.id.locateRangeText);
        this.rangeText.setText("1", BufferType.EDITABLE);
        
        Log.d(TAG, "Default Range: " + rangeText.getText());
	}
	/**
	 * ok button click listener
	 * retrieves the user defined locating rage
	 * navigate to the map page or group nearby page depending on the key
	 * @param view
	 */
	public void LocatePageOkButtonClickHandler(View view) {

		if (this.rangeText.getText().length() == 0) {
			Toast.makeText(this, "Please input range", Toast.LENGTH_LONG).show();
			return;
		}
		String range = this.rangeText.getText().toString();
		if (!isNumeric(range)) {
			Toast.makeText(this, "Please input a number", Toast.LENGTH_LONG).show();
			return;
		}
		
		Intent next = null;
		if (key == MAP)
		{
			next = new Intent(this,FacemapMapActivity.class);
			next.putExtra("key", MAP);
		}
		else if (key == GROUPS)
		{
			next = new Intent(this,GroupNearbyPageActivity.class);
			next.putExtra("key", GROUPS);
		}
		
		int intRange = Integer.parseInt(rangeText.getText().toString());
    	Log.d(TAG,"Passing to groups range: " + intRange);
    	next.putExtra("range", intRange);
    	this.startActivity(next);
	}
	
	/**
	 * test whether input is numeric
	 * @param input
	 * @return true/false
	 */
	private boolean isNumeric(String input) {
		try {
			Float.parseFloat(input);
			return true;
		} catch (Exception e) {
			return false;
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
	protected void onSaveInstanceState(Bundle outState) 
	{
		// this method is not guaranteed to be called, sadly,
		// but assume it will be normally, and when it is called,
		// it will be called before onStop and possibly before onPause
		super.onSaveInstanceState(outState);
		Log.d(TAG,"onSaveInstanceState");
		// take the text from the text box and save it; textboxes
		// and other built-in Views usually have their state saved
		// anyway, but this is just for an example of how to save state
		if (this.rangeText != null)
		{
			outState.putString(this.rangeTextKey, this.rangeText.getText().toString());
		}
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
	 * Return to the right page when back is pressed
	 */
	public void onBackPressed()
	{
		Log.d(TAG, "Detected back pressed");
		Intent back;
		if (key == MAP)
			back = new Intent(this, LocatePageActivity.class);
		else if (key == GROUPS)
			back = new Intent(this, GroupsPageActivity.class);
		else
			back = new Intent(this, MenuPageActivity.class);
		this.startActivity(back);
	}
}
