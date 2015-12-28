package edu.jhu.cs.oose.fall2011.facemap.gui;

import java.util.Date;

import android.content.Context;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import edu.jhu.cs.oose.fall2011.facemap.domain.Location;
import edu.jhu.cs.oose.fall2011.facemap.domain.LocationImpl;
import edu.jhu.cs.oose.fall2011.facemap.server.LocationRetriever;

/**
 * Class implementing both LocationRetriever and LocationListener to give the current location of the phone.
 * This lives for the duration of the login.
 * @author Chuan Huang
 */
public class CurrentLocation implements LocationRetriever, LocationListener
{
	final String TAG = "CurrentLocation";
	
	private Context context;
	
	/**
	 * Instantiates a new current location.
	 */
	public CurrentLocation(Context mContext)
	{
		Log.d(TAG, "Starting currentLocation Implementation");
		this.context = mContext;
	}

	@Override
	public Location retrieveLocation()
	{
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f, this);

		Criteria criteria = new Criteria();
		String provider = locationManager.getBestProvider(criteria, false);
		android.location.Location locations = (android.location.Location) locationManager.getLastKnownLocation(provider);

		double lat, lng;

		if (locations != null)
		{
			Log.d(TAG, "Provider " + provider + " has been selected.");
			lat = locations.getLatitude();
			lng = locations.getLongitude();
		} else
		{
			lat = 39.323688;
			lng = -76.617157;
		}

		Log.d(TAG, "Location Retriever Success");
		return new LocationImpl(lat, lng, new Date());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.location.LocationListener#onLocationChanged(android.location.Location)
	 */
	public void onLocationChanged(android.location.Location arg0)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.location.LocationListener#onProviderDisabled(java.lang.String)
	 */
	public void onProviderDisabled(String provider)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.location.LocationListener#onProviderEnabled(java.lang.String)
	 */
	public void onProviderEnabled(String provider)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.location.LocationListener#onStatusChanged(java.lang.String, int, android.os.Bundle)
	 */
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
		// TODO Auto-generated method stub

	}
}
