package edu.jhu.cs.oose.fall2011.facemap.domain;

import java.util.Date;

/**
 * This class contains a set of GPS coordinates and a timestamp.
 * 
 * @author Daniel Cranford
 *
 */
public interface Location {

	/**
	 * Returns the latitude as a double
	 * @return the latitude as a double
	 */
	double getLatitude();

	/**
	 * Returns the longitude as a double
	 * @return the longitude as a double
	 */
	double getLongitude();
	
	/**
	 * Returns the time this location was recorded
	 * @return time this location was recorded
	 */
	Date getTimestamp();

}