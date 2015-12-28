package edu.jhu.cs.oose.fall2011.facemap.domain;

/**
 * Represents a request from the requestor to the requestee to add the requestor
 * as a friend. This will allow both the requestor and requestee to see each
 * other's location on the map and allow them to communicate with each other.
 * 
 * @author Daniel Cranford
 */
public interface FriendRequest {

	/**
	 * Returns the person who made this request
	 * @return the person who made this request
	 */
	String getRequestorUserId();

	/**
	 * Returns the person to whom this request was made
	 * @return the person to whom this request was made
	 */
	String getRequesteeUserId();

}