package edu.jhu.cs.oose.fall2011.facemap.gui;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
/**
 * Overlay items for the map including a special marker for the user at specified locations
 * @author Chuan Huang (Kevin)
 */

@SuppressWarnings("rawtypes")
public class MapItemizedOverlay extends ItemizedOverlay
{
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	
	/** The m context. */
	private Context mContext;
	
	public MapItemizedOverlay(Drawable defaultMarker, Context context)
	{
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}
	
	public MapItemizedOverlay(Drawable defaultMarker)
	{
		super(boundCenterBottom(defaultMarker));
	}

	
	/* 
	 * Display a message when the user clicks on icon
	 * @see com.google.android.maps.ItemizedOverlay#onTap(int)
	 */
	protected boolean onTap(int index)
	{
		OverlayItem item = mOverlays.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		return true;
	}


	@Override
	protected OverlayItem createItem(int i)
	{
		return mOverlays.get(i);
	}

	public void addOverlay(OverlayItem overlay)
	{
		mOverlays.add(overlay);
		populate();
	}

	@Override
	public int size()
	{
		return mOverlays.size();
	}
}
