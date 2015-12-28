package edu.jhu.cs.oose.fall2011.facemap.login;

import java.util.Set;

import edu.jhu.cs.oose.fall2011.facemap.domain.FriendGroup;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendRequest;
import edu.jhu.cs.oose.fall2011.facemap.domain.Location;
import edu.jhu.cs.oose.fall2011.facemap.domain.Person;
import edu.jhu.cs.oose.fall2011.facemap.domain.PrivatePerson;

public interface LoginSession {
	/**
	 * @return value object containing current state of this users account
	 */
	PrivatePerson getPrivatePerson();
	
	// account data modification methods
	void addFriendToGroup(FriendGroup friendGroup, Person friend);
	void removeFriendFromGroup(FriendGroup friendGroup, Person friend);
	void setName(String name);
	void setMessage(String message);
	void blockFriend(Person friend);
	void unblockFriend(Person friend);
	// TODO: this may need to return the new FriendGroup
	void createFriendGroup(String name, Set<Person> initialMembers);
	void removeFriendGroup(FriendGroup friendGroup);
	void requestFriend(String phoneOrEmail);
	void removeFriend(Person friend);
	void respondToFriendRequest(FriendRequest friendRequest, boolean accept);
	
	// location methods
	Location getLocationOf(Person friend);
	void setMyLocation(Location location);
	
	// session methods
	void logout();
}
