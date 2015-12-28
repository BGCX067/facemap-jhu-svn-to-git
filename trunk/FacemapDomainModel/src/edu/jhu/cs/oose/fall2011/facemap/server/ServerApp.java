package edu.jhu.cs.oose.fall2011.facemap.server;

import edu.jhu.cs.oose.fall2011.facemap.domain.FriendRequest;
import edu.jhu.cs.oose.fall2011.facemap.domain.Location;
import edu.jhu.cs.oose.fall2011.facemap.domain.PrivatePerson;

/**
 * Server application interface. Methods drawn from use cases. May need to be
 * refactored to split methods up.
 * 
 * @author Daniel Cranford
 */
public interface ServerApp {

	/**
	 * Logs a user into the the service and returns the PrivatePerson instance
	 * for the logged in user.
	 * 
	 * TODO: Can't just pass in a raw LocationRetriever (over network), but need
	 * some notion to allow the server to query the client for its current
	 * location.
	 * 
	 * @param userid
	 *            email of the user
	 * @param password
	 *            password of the user
	 * @param clientLocationRetriever
	 *            object capable of retrieving the current location of the
	 *            logged in client.
	 * @return Private person instance for the user logging in
	 * @throws ServerAppException
	 *             if login fails
	 */
	PrivatePerson login(String userid, String password,
			LocationRetriever clientLocationRetriever)
			throws ServerAppException;

	/**
	 * Logs a user out
	 * 
	 * @param userid
	 */
	void logout(String userid);

	/**
	 * Searches for a user with the given phone or email and sends a friend
	 * request to them. TODO: should this method (or parts of it) be moved into
	 * the PrivatePerson class?
	 * 
	 * @param requestorUserid
	 *            user id
	 * @param phoneOrEmail
	 *            phone number or email address to search for
	 * @throws ServerAppException
	 *             if requestorUserId is invalid or no account is found with
	 *             phoneOrEmail
	 */
	void requestFriend(String requestorUserid, String phoneOrEmail)
			throws ServerAppException;

	/**
	 * Removes an existing friendship between users with id's removerUserId and
	 * removeeUserId
	 * 
	 * @param removerUserId
	 * @param removeeUserId
	 * @throws ServerAppException
	 */
	void removeFriend(String removerUserId, String removeeUserId)
			throws ServerAppException;

	/**
	 * Registers a new account
	 * 
	 * @param email
	 * @param phoneNo
	 * @param name
	 * @param password
	 * @throws ServerAppException
	 *             if registration fails - this can happen if the email or phone
	 *             number is already in use by another account.
	 */
	void register(String email, String phoneNo, String name, String password)
			throws ServerAppException;

	/**
	 * Respond to a given friend request. TODO: Should this method be moved in
	 * the FriendRequest class?
	 * 
	 * @param request
	 * @param accept
	 *            true to accept the request, false to reject
	 */
	void reposondToFriendRequest(FriendRequest request, boolean accept);

	/**
	 * Retrieves the last known location known for a particular user. The users
	 * must be friends and the requestor must not be blocked by the target user.
	 * May return a location that is out of date if the current location cannot
	 * be fetched. This can be checked by inspecting the timestamp attribute of
	 * the Location. May return null if the location has never been retrieved
	 * and the targeted user is offline.
	 * 
	 * @param requestorId
	 *            id of user requesting location information
	 * @param targetUserId
	 *            id of user to return location of
	 * @return location of targeted user
	 * @throws ServerAppException
	 *             if location cannot be retrieved because users are not friends
	 */
	Location getLocation(String requestorId, String targetUserId)
			throws ServerAppException;
}
