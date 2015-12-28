package edu.jhu.cs.oose.fall2011.facemap.domain;

/**
 * A Person object contains all the account information visible to friends of
 * this person.
 * 
 * @author Daniel Cranford
 *
 */
public interface Person {

	/**
	 * Returns this person's full name
	 * @return this person's full name
	 */
	String getName();

	/**
	 * Returns this person's email address
	 * @return this person's email address
	 */
	String getEmail();

	/**
	 * Returns this person's phone number
	 * @return this person's phone number
	 */
	String getPhoneNumber();

	/**
	 * @return the current location of this person. This involves a network 
	 * call to the server and a call from the server to this person's phone
	 * to discover the current location.
	 */
	Location getLocation();

}