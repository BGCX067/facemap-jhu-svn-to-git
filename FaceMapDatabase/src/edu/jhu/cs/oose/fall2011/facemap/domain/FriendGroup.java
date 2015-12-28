package edu.jhu.cs.oose.fall2011.facemap.domain;

import java.util.Set;

/**
 * A named group of friends. Facemap users will organize their friends into
 * logical groups using instances of this class.
 * 
 * @author Daniel Cranford
 * 
 */
public interface FriendGroup {

	/**
	 * @return the name of this group
	 */
	String getGroupName();

	/**
	 * Adds a friend to the group
	 * @param p the friend to add
	 */
	void addFriendToGroup(Person p);

	/**
	 * Removes a friend from this group
	 * @param p the friend to remove
	 */
	void removeFriendFromGroup(Person p);

	/**
	 * @return an immutable snapshot view of the friends in this group
	 */
	Set<Person> getFriendsInGroup();

}