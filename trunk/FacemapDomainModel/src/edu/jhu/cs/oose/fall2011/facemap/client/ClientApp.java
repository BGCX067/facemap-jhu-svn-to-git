package edu.jhu.cs.oose.fall2011.facemap.client;

import edu.jhu.cs.oose.fall2011.facemap.domain.FriendRequest;
import edu.jhu.cs.oose.fall2011.facemap.domain.Location;
import edu.jhu.cs.oose.fall2011.facemap.domain.Person;
import edu.jhu.cs.oose.fall2011.facemap.domain.PrivatePerson;
import edu.jhu.cs.oose.fall2011.facemap.server.LocationRetriever;

/**
 * Phone client application interface. Methods drawn from use cases.
 * 
 * @author Daniel Cranford
 */
public interface ClientApp {

	/**
	 * Registers a new user, creates a new account
	 * 
	 * @param email
	 * @param phoneNo
	 * @param name
	 * @param password
	 * @throws ClientAppException
	 *             if register fails, e.g. email is already taken
	 */
	void register(String email, String phoneNo, String name, String password)
			throws ClientAppException;

	/**
	 * Logs a user in
	 * 
	 * @param userid
	 * @param password
	 * @param locationRetriever
	 *            object used to retrieve the location of the user's phone while
	 *            they are logged in
	 * @throws ClientAppException
	 *             if login fails
	 */
	void login(String userid, String password,
			LocationRetriever locationRetriever) throws ClientAppException;

	/**
	 * Logs the current user out
	 */
	void logout();
	
	/**
	 * Returns the location of one of the currently logged-in user's friends
	 * @param friend
	 * @return
	 * @throws ClientAppException 
	 */
	Location getFriendLocation(Person friend) throws ClientAppException;
	

	/**
	 * Returns the currently logged in user or null if no user is logged in
	 * 
	 * @return the currently logged in user
	 */
	PrivatePerson getLoggedInUser();

	/**
	 * Searches for a facemap user with the given phone number or email address
	 * and sends a request from loggedInUser to add as a friend
	 * 
	 * @param phoneOrEmail
	 * @throws ClientAppException
	 *             if no user with given phone or email found
	 */
	void requestFriend(String phoneOrEmail) throws ClientAppException;

	/**
	 * Removes a friend from the currently logged in user's friends
	 * 
	 * @param friend
	 * @throws ClientAppException
	 */
	void removeFriend(Person friend) throws ClientAppException;

	/**
	 * Responds to a request for the currently logged in user
	 * 
	 * @param request
	 * @param accept
	 *            true to accept the request, false to reject
	 * @throws ClientAppException
	 */
	void respondToFriendRequest(FriendRequest request, boolean accept)
			throws ClientAppException;

}
