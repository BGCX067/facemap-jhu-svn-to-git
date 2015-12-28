package edu.jhu.cs.oose.fall2011.facemap.domain;

import java.util.Set;

public interface PrivatePerson {

	/**
	 * 
	 * @return the Person instance representing this user
	 */
	Person getSelf();

	/**
	 * Returns a live set view of the user's friends
	 * @return the friend list of this user
	 */
	Set<Person> getFriends();

	/**
	 * Returns a live set view of the user's friends who are blocked
	 * Blocked friends will be unable to see this person's location.
	 * @return a live set view of the user's blocked friends
	 */
	Set<Person> getBlockedFriends();

	/**
	 * Returns a live set view of the FriendGroup's this user has created.
	 * Adding and removeing Friend groups is accomplished directly on the set
	 * like this:
	 * 
	 * <pre>
	 * //Add a new FriendGroup
	 * PrivatePerson pp = ...
	 * pp.getFriendGroups().add(new FriendGroup("myNewGroup",...));
	 * </pre>
	 * 
	 * @return live set of FriendGroup's
	 */
	Set<FriendGroup> getFriendGroups();

	/**
	 * Returns a live set view of the friend requests this user has sent.
	 * 
	 * @return live set view of the friend requests this user has sent
	 */
	Set<FriendRequest> getRequestsISent();
	
	/**
	 * Return a live set view of the friend requests awaiting this users
	 * response.
	 * 
	 * @return live set view of the friend requests awaiting this users response
	 */
	Set<FriendRequest> getRequestsAwaitingMyResponse();

}