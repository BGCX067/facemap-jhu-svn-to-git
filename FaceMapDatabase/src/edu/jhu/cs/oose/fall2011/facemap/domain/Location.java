package edu.jhu.cs.oose.fall2011.facemap.domain;

/**
 * This class contains a set of GPS coordinates.
 * 
 * @author Daniel Cranford
 *
 */
public interface Location {

	/**
	 * @return the latitude as a double
	 */
	double getLatitude();

	/**
	 * @return the longitude as a double
	 */
	double getLongitude();

}