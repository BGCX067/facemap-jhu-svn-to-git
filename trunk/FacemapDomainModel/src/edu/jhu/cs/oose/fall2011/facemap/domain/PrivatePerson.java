package edu.jhu.cs.oose.fall2011.facemap.domain;

import java.util.Set;

/**
 * This class contains information about a Facemap user's friends and groups. It
 * is only visible to the user it belongs to.
 * 
 * @author Daniel Cranford
 * 
 */
public interface PrivatePerson {

	/**
	 * Returns the Person instance with this user's contact information
	 * @return the Person instance with this user's contact information
	 */
	Person getSelf();
	
	/**
	 * Set's the person's full name
	 * @param name the person's full name
	 */
	void setName(String name);

	/**
	 * Sets the user's custom messsage
	 * @param message the user's custom message
	 */
	void setMessage(String message);

	/**
	 * Returns an immutable set view of the user's friends
	 * @return the friend list of this user
	 */
	Set<Person> getFriends();

	/**
	 * Returns an immutable set view of the user's friends who are blocked
	 * Blocked friends will be unable to see this person's location.
	 * @return immutable set view of the user's blocked friends
	 */
	Set<Person> getBlockedFriends();
	
	/**
	 * Adds the friend to this user's blocked list. Blocked friends will be
	 * unable to see this user's location.
	 * @param friend
	 * @see #unblockFriend(Person)
	 */
	void blockFriend(Person friend);
	
	/**
	 * Removes the friend from this user's blocked list so they can see this
	 * user's location again.
	 * @param friend
	 * @see #blockFriend(Person)
	 */
	void unblockFriend(Person friend);

	/**
	 * Returns an immutable set view of the FriendGroup's this user has created.
	 *
	 * @return immutable set view of FriendGroup's
	 */
	Set<FriendGroup> getFriendGroups();
	
	/**
	 * Creates a new FriendGroup with the given name and contents
	 * @param name
	 * @param friends
	 * @return
	 * @throws if name specifies a group that already exists
	 */
	FriendGroup createFriendGroup(String name, Set<Person> friends) throws IllegalArgumentException;
	
	/**
	 * Deletes an existing friend group
	 * @param friendGroup
	 */
	void removeFriendGroup(FriendGroup friendGroup);
	
	/**
	 * Blocks each member of the given friend group. Note that this method 
	 * simply blocks each member of the group. No notion of this group being
	 * blocked is saved. Members of this group my be unblocked individually,
	 * unblocked as part of this group via a call to unblockGroup() with the 
	 * same friendGroup, <b>or</b> as part of another group.
	 * 
	 * For example, consider a user who has no blocked friends
	 * <pre>
	 * PrivatePerson pp = ...;
	 * Person friend1 = ..., friend2 = ..., friend3 = ..., friend4 = ...;
	 * FriendGroup fg1 = ... // {friend1, friend2, friend3}
	 * FriendGroup fg2 = ... // {friend1, friend2, friend4}
	 * pp.getBlockedFriends(); // returns empty set
	 * pp.blockFriendGroup(fg1);
	 * pp.getBlockedFriends(); // returns {friend1, friend2, friend3}
	 * pp.unblockFriend(friend1);
	 * pp.getBlockedFriends(); // returns {friend2, friend3}
	 * pp.unblockFriendGroup(fg2);
	 * pp.getBlockedFriends(); // returns {friend3}
	 * </pre>
	 * 
	 * @param friendGroup
	 * @see #unblockFriendGroup(FriendGroup)
	 */
	void blockFriendGroup(FriendGroup friendGroup);
	
	/**
	 * Unblocks each member of the given friend group
	 * @param friendGroup 
	 * @see #blockFriendGroup(FriendGroup)
	 */
	void unblockFriendGroup(FriendGroup friendGroup);

	/**
	 * Returns an immutable set view of the friend requests this user has sent.
	 * 
	 * @return immutable set view of the friend requests this user has sent
	 */
	Set<FriendRequest> getFriendRequestsSent();
	
	/**
	 * Return an immutable set view of the friend requests awaiting this users
	 * response.
	 * 
	 * @return imutable set view of the friend requests awaiting this users response
	 */
	Set<FriendRequest> getFriendRequestsReceived();
}