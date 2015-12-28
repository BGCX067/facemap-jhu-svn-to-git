package edu.jhu.cs.oose.fall2011.facemap.server;

import java.util.HashMap;
import java.util.Map;

import edu.jhu.cs.oose.fall2011.facemap.domain.Location;

public class LocationServiceImpl implements LocationService {
	private Map<String, LocationRetriever> locationRetrievers = new HashMap<String, LocationRetriever>();
	
	@Override
	public void setLocationRetriever(String userId,
			LocationRetriever locationRetriever) {
		locationRetrievers.put(userId, locationRetriever);
	}

	@Override
	public void removeLocationRetriever(String userId) {
		locationRetrievers.remove(userId);
	}

	@Override
	public Location getLocation(String userId) {
		LocationRetriever locationRetriever = locationRetrievers.get(userId);
		if(locationRetriever == null) {
			return null;
		} else {
			return locationRetriever.retrieveLocation();
		}
	}

}
