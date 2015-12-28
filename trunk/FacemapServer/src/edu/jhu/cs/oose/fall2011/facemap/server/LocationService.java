package edu.jhu.cs.oose.fall2011.facemap.server;

import edu.jhu.cs.oose.fall2011.facemap.domain.Location;

/**
 * Retrieves current location for logged in users
 * 
 * @author Daniel Cranford
 * 
 */
public interface LocationService {
	/**
	 * Sets the location retriever that will be used to retrieve the location of
	 * the specified user
	 * 
	 * @param userId
	 * @param locationRetriever
	 */
	void setLocationRetriever(String userId, LocationRetriever locationRetriever);

	/**
	 * Removes the ability to retrieve locations for a given user
	 * 
	 * @param userId
	 */
	void removeLocationRetriever(String userId);

	/**
	 * Retrieves the current location of a given user, or null if no
	 * locationRetirever has been set for that particular user.
	 * 
	 * @param userId
	 * @return
	 */
	Location getLocation(String userId);
}
