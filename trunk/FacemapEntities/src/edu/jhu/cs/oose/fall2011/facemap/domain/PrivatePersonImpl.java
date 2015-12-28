package edu.jhu.cs.oose.fall2011.facemap.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class PrivatePersonImpl implements PrivatePerson, Serializable {
	
	private static final long serialVersionUID = 1L;
	private Person self;
	private Set<Person> friends;
	/** since FriendGroup's are mutable objects, and Java Set's don't handle 
	 * that, we are using a List instead */
	private List<FriendGroup> friendGroups;
	private Set<Person> blockedFriends;
	private Set<FriendRequest> requestsSent;
	private Set<FriendRequest> requestsRecieved;

	public PrivatePersonImpl(Person self) {
		this.self = self;
		this.friends = new HashSet<Person>();
		this.friendGroups = new ArrayList<FriendGroup>();//new HashSet<FriendGroup>();
		this.blockedFriends = new HashSet<Person>();
		this.requestsSent = new HashSet<FriendRequest>();
		this.requestsRecieved = new HashSet<FriendRequest>();
	}

	@Override
	public Person getSelf() {
		return self;
	}

	@Override
	public Set<Person> getFriends() {
		return Collections.unmodifiableSet(friends);
	}

	/**
	 * Removes a person from this user's friend list, blocked friend list and
	 * friend groups Method only to be called by server code.
	 * 
	 * @param friend person to remove
	 */
	public void removeFriend(Person friend) {
		friends.remove(friend);
		blockedFriends.remove(friend);
		for(FriendGroup friendGroup : friendGroups) {
			friendGroup.removeFriendFromGroup(friend);
		}
	}
	
	/**
	 * Server only method to add friends
	 * @param friend
	 */
	public void addFriend(Person friend) {
		friends.add(friend);
	}

	@Override
	public Set<Person> getBlockedFriends() {
		return Collections.unmodifiableSet(blockedFriends);
	}


	@Override
	public Set<FriendGroup> getFriendGroups() {
		return Collections.unmodifiableSet(new HashSet<FriendGroup>(friendGroups));
	}


	@Override
	public Set<FriendRequest> getFriendRequestsSent() {
		return Collections.unmodifiableSet(requestsSent);
	}
	
	/**
	 * Method to allow server code to modify the friendRequestsSentRecieved sets
	 * @param friendRequest
	 */
	public void addFriendRequest(FriendRequest friendRequest) {
		if(friendRequest.getRequestorUserId().equals(self.getEmail())) {
			requestsSent.add(friendRequest);
		} else {
			requestsRecieved.add(friendRequest);
		}
	}
	
	/**
	 * Method to allow server code to modify the friendRequestsSent set
	 * @param friendRequest
	 */
	public void removeFriendRequest(FriendRequest friendRequest) {
		if(friendRequest.getRequestorUserId().equals(self.getEmail())) {
			requestsSent.remove(friendRequest);
		} else {
			requestsRecieved.remove(friendRequest);
		}
	}


	@Override
	public Set<FriendRequest> getFriendRequestsReceived() {
		return Collections.unmodifiableSet(requestsRecieved);
	}
	

	@Override
	public void blockFriend(Person friend) {
		blockedFriends.add(friend);
	}

	@Override
	public void unblockFriend(Person friend) {
		blockedFriends.remove(friend);
	}

	@Override
	public FriendGroup createFriendGroup(String name, Set<Person> friends)
			throws IllegalArgumentException {
		for(FriendGroup friendGroup : friendGroups) {
			if(friendGroup.getGroupName().equals(name)) {
				throw new IllegalArgumentException("A group by that name already exists:" + name);
			}
		}
		FriendGroup friendGroup = new FriendGroupImpl(name, friends);
		friendGroups.add(friendGroup);
		return friendGroup;
	}

	@Override
	public void removeFriendGroup(FriendGroup friendGroup) {
		friendGroups.remove(friendGroup);
	}

	@Override
	public void blockFriendGroup(FriendGroup friendGroup) {
		for(Person friend : friendGroup.getFriendsInGroup()) {
			blockFriend(friend);
		}
	}

	@Override
	public void unblockFriendGroup(FriendGroup friendGroup) {
		for(Person friend : friendGroup.getFriendsInGroup()) {
			unblockFriend(friend);
		}
	}

	@Override
	public void setName(String name) {
		((PersonImpl)self).setName(name);
	}

	@Override
	public void setMessage(String message) {
		((PersonImpl)self).setMessage(message);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((blockedFriends == null) ? 0 : blockedFriends.hashCode());
		result = prime * result
				+ ((friendGroups == null) ? 0 : friendGroups.hashCode());
		result = prime * result + ((friends == null) ? 0 : friends.hashCode());
		result = prime
				* result
				+ ((requestsRecieved == null) ? 0 : requestsRecieved.hashCode());
		result = prime * result
				+ ((requestsSent == null) ? 0 : requestsSent.hashCode());
		result = prime * result + ((self == null) ? 0 : self.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrivatePersonImpl other = (PrivatePersonImpl) obj;
		if (blockedFriends == null) {
			if (other.blockedFriends != null)
				return false;
		} else if (!blockedFriends.equals(other.blockedFriends))
			return false;
		if (friendGroups == null) {
			if (other.friendGroups != null)
				return false;
		} else if (!friendGroups.equals(other.friendGroups))
			return false;
		if (friends == null) {
			if (other.friends != null)
				return false;
		} else if (!friends.equals(other.friends))
			return false;
		if (requestsRecieved == null) {
			if (other.requestsRecieved != null)
				return false;
		} else if (!requestsRecieved.equals(other.requestsRecieved))
			return false;
		if (requestsSent == null) {
			if (other.requestsSent != null)
				return false;
		} else if (!requestsSent.equals(other.requestsSent))
			return false;
		if (self == null) {
			if (other.self != null)
				return false;
		} else if (!self.equals(other.self))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PrivatePersonImpl [self=" + self + ", friends=" + friends
				+ ", friendGroups=" + friendGroups + ", blockedFriends="
				+ blockedFriends + ", requestsSent=" + requestsSent
				+ ", requestsRecieved=" + requestsRecieved + "]";
	}

	
}
