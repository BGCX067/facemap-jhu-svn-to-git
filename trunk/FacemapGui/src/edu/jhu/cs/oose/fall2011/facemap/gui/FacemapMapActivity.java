package edu.jhu.cs.oose.fall2011.facemap.gui;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

import edu.jhu.cs.oose.fall2011.facemap.client.ClientApp;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientAppException;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendGroup;
import edu.jhu.cs.oose.fall2011.facemap.domain.Location;
import edu.jhu.cs.oose.fall2011.facemap.domain.Person;
import edu.jhu.cs.oose.fall2011.facemap.entity.RandomLocationRetriever;
import edu.jhu.cs.oose.fall2011.facemap.server.LocationRetriever;

/**
 * Map Activity for the FaceMap App - locates own phone's location and displays it onto the map with an itemized
 * overlay.
 * 
 * @author Chuan Huang (Kevin)
 */
public class FacemapMapActivity extends MapActivity implements LocationListener
{
	final String TAG = "FacemapMapActivity";

	/** Constant to indicate we're selecting contacts from all contacts */
	final int CONTACTS = 1;

	/** Constant to indicate we're selecting contacts from groups */
	final int GROUPS = 2;

	/** Constant for mapping out nearby friends */
	final int NEARBY = 3;

	/** The map controller. */
	private MapController mapController;

	/** The map view. */
	private MapView mapView;

	/** The point. */
	GeoPoint point;

	/** The range. */
	int range;
	private ClientAppApplication app;

	private ClientApp clientApp;

	private MapItemizedOverlay itemizedoverlay;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		app = (ClientAppApplication) getApplication();
		this.clientApp = app.getClientApp();

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setVisibility(View.VISIBLE);
		mapView.setBuiltInZoomControls(true);

		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.smiley);

		itemizedoverlay = new MapItemizedOverlay(drawable, this);
		range = getIntent().getIntExtra("range", 1);

		int mylat, mylng;

		CurrentLocation currLocation = new CurrentLocation(this);
		Location myCurrentLocation = currLocation.retrieveLocation();
		mylat = (int) (myCurrentLocation.getLatitude() * 1000000);
		mylng = (int) (myCurrentLocation.getLongitude() * 1000000);
		Log.d(TAG, "Lat: " + mylat);
		Log.d(TAG, "Lon: " + mylng);

		// display own location on the map
		point = new GeoPoint(mylat, mylng);
		String myLocation = "Latitude: " + mylat + "\n Longitude: " + mylng;
		OverlayItem overlayitem = new OverlayItem(point, "My current Location", myLocation);
		itemizedoverlay.addOverlay(overlayitem);

		// display contacts(key == 1) or groups(key == 2) on the map
		int key = getIntent().getIntExtra("key", CONTACTS);
		Location friendLocation;
		
		//for testing
		LocationRetriever randomLocation;
		
		if (key == CONTACTS)
		{
			Log.d(TAG, "Finding Contacts");
			Set<Person> friends = app.getPersonList();
			for (Person friend : friends)
			{
				Log.d(TAG, "Contacts: " + friend.getName());
			}

			for (Person f : friends)
			{
				if (f.getName().equals("111") || f.getName().equals("222"))
				{
					try
					{
						friendLocation = clientApp.getFriendLocation(f);
						Log.d(TAG, "Location Found");

					} catch (ClientAppException e)
					{
						continue;
					}
				}
				else
				{
					randomLocation = new RandomLocationRetriever(39.323688, -76.617157, 0.02, 0.02);
					friendLocation = randomLocation.retrieveLocation();
				}
				
				this.addPoint(friendLocation, f);
			}
		} else if (key == GROUPS)
		{
			Log.d(TAG, "Finding Groups");
			Set<FriendGroup> groups = app.getGroupList();
			for (FriendGroup f : groups)
			{
				Log.d(TAG, "Group: " + f.getGroupName());
			}
			Set<Person> added = new HashSet<Person>();
			
			for (FriendGroup g : groups)
			{
				Log.d(TAG, "Found Groups, finding friends");
				Set<Person> friends = g.getFriendsInGroup();
				for (Person friend : friends)
				{
					Log.d(TAG, "Contacts in group: " + friend.getName());
				}

				for (Person f : friends)
				{
					if (added.contains(f))
						continue;
					else
						added.add(f);
					
					if (f.getName().equals("111") || f.getName().equals("222"))
					{
						try
						{
							friendLocation = clientApp.getFriendLocation(f);
							Log.d(TAG, "Location Found");

						} catch (ClientAppException e)
						{
							continue;
						}
					}
					else
					{
						randomLocation = new RandomLocationRetriever(39.323688, -76.617157, 0.02, 0.02);
						friendLocation = randomLocation.retrieveLocation();
					}
					
					this.addPoint(friendLocation, f);
				}
			}
		} else if (key == NEARBY)
		{
			Log.d(TAG, "Finding Nearby Contacts");
			Set<Person> friends = clientApp.getLoggedInUser().getFriends();
			for (Person friend : friends)
			{
				Log.d(TAG, "Contacts: " + friend.getName());
			}

			for (Person f : friends)
			{
				//for testing, only 111 and 222 have locations
				if (f.getName().equals("111") || f.getName().equals("222"))
				{
					try
					{
						friendLocation = clientApp.getFriendLocation(f);
						Log.d(TAG, "Location Found");

					} catch (ClientAppException e)
					{
						continue;
					}
				}
				else
				{
					randomLocation = new RandomLocationRetriever(39.323688, -76.617157, 0.02, 0.02);
					friendLocation = randomLocation.retrieveLocation();
				}

				double dist = Functions.findDistanceMile((double)mylat/1000000, (double)mylng/1000000, friendLocation.getLatitude(), friendLocation.getLongitude());
				Log.d(TAG, "Distance: " + dist);
				if (dist <= range)
				{
					this.addPoint(friendLocation, f);
					Log.d(TAG, "Added Person: " + f.getName());
				}
			}
		}

		mapOverlays.add(itemizedoverlay);

		// add support for drawing range on map only for nearby
		if (key == NEARBY)
		{
			// default range is approximately 10 miles
			Overlay rangeDisplay = new RangeOverlay(range);
			mapOverlays.add(rangeDisplay);
		}

		mapController = mapView.getController();
		mapController.setZoom(15);
		mapController.setCenter(point);
	}

	private void addPoint(Location L, Person friend)
	{
		GeoPoint P = new GeoPoint((int)(L.getLatitude()*1000000), (int)(L.getLongitude()*1000000));
		String message = "Longitude: " + P.getLatitudeE6() + "\n Latitude: " + P.getLongitudeE6();
		itemizedoverlay.addOverlay(new OverlayItem(P, friend.getName() + "'s Location", message));
		Log.d(TAG, "Friend " + friend.getName() + " Lat: " + P.getLatitudeE6() + " Lon: " + P.getLongitudeE6());
	}

	/**
	 * The Overlays a range onto the map
	 */
	private class RangeOverlay extends Overlay
	{

		/** The range. */
		int range;

		/** The lon. */
		int lat, lon;

		/**
		 * Instantiates a new range overlay.
		 * 
		 * @param setRange the set range
		 */
		public RangeOverlay(int setRange)
		{
			super();
			this.range = setRange;
			Log.d("RangeOverlay", "Initiated: " + range);
		}

		/*
		 * Select range for circle
		 */
		@Override
		public boolean onTap(GeoPoint geoPoint, MapView mapView)
		{
			lat = geoPoint.getLatitudeE6();
			lon = geoPoint.getLongitudeE6();
			return super.onTap(geoPoint, mapView);
		}

		/*
		 * draw circle on the map
		 */
		@Override
		public void draw(Canvas canvas, MapView mapV, boolean shadow)
		{
			// Log.d("RangeOverlay", "Drawing started");
			if (shadow)
			{
				Projection projection = mapV.getProjection();
				Point pt = new Point();
				projection.toPixels(FacemapMapActivity.this.point, pt);

				lat = FacemapMapActivity.this.point.getLatitudeE6();
				lon = FacemapMapActivity.this.point.getLongitudeE6();

				GeoPoint newGeos = new GeoPoint(lat + (int) 1000000 * range / 69, lon);
				Point pt2 = new Point();
				projection.toPixels(newGeos, pt2);
				float circleRadius = Math.abs(pt2.y - pt.y);

				Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

				circlePaint.setColor(0x30000000);
				circlePaint.setStyle(Style.FILL_AND_STROKE);
				canvas.drawCircle((float) pt.x, (float) pt.y, circleRadius, circlePaint);

				circlePaint.setColor(0x99000000);
				circlePaint.setStyle(Style.STROKE);
				canvas.drawCircle((float) pt.x, (float) pt.y, circleRadius, circlePaint);

				// Bitmap markerBitmap =
				// BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.smiley);
				// canvas.drawBitmap(markerBitmap,pt.x,pt.y-markerBitmap.getHeight(),null);

				super.draw(canvas, mapV, shadow);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.android.maps.MapActivity#isRouteDisplayed()
	 */
	@Override
	protected boolean isRouteDisplayed()
	{
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.location.LocationListener#onLocationChanged(android.location.Location)
	 */
	public void onLocationChanged(Location location)
	{
		if (location != null)
		{
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			point = new GeoPoint((int) lat * 1000000, (int) lng * 1000000);
			mapController.animateTo(point);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.location.LocationListener#onProviderDisabled(java.lang.String)
	 */
	@Override
	public void onProviderDisabled(String provider)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.location.LocationListener#onProviderEnabled(java.lang.String)
	 */
	@Override
	public void onProviderEnabled(String provider)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.location.LocationListener#onStatusChanged(java.lang.String, int, android.os.Bundle)
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(android.location.Location arg0)
	{
		// TODO Auto-generated method stub

	}
}