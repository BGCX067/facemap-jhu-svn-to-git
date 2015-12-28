package edu.jhu.cs.oose.fall2011.facemap.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A named group of friends. Facemap users will organize their friends into
 * logical groups using instances of this class.
 * 
 * @author Daniel Cranford
 * 
 */
@XmlRootElement
public class FriendGroupImpl implements FriendGroup, Serializable {

	private static final long serialVersionUID = 1L;
	private String groupName;
	private Set<Person> friendsInGroup;

	public FriendGroupImpl(String groupName, Set<Person> friendsInGroup) {
		this.groupName = groupName;
		this.friendsInGroup = new HashSet<Person>(friendsInGroup.size());
		this.friendsInGroup.addAll(friendsInGroup);
	}

	@Override
	public String getGroupName() {
		return groupName;
	}

	@Override
	public void addFriendToGroup(Person p) {
		friendsInGroup.add(p);
	}

	@Override
	public void removeFriendFromGroup(Person p) {
		friendsInGroup.remove(p);
	}

	@Override
	public Set<Person> getFriendsInGroup() {
		return Collections.unmodifiableSet(friendsInGroup);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((friendsInGroup == null) ? 0 : friendsInGroup.hashCode());
		result = prime * result
				+ ((groupName == null) ? 0 : groupName.hashCode());
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
		FriendGroupImpl other = (FriendGroupImpl) obj;
		if (friendsInGroup == null) {
			if (other.friendsInGroup != null)
				return false;
		} else if (!friendsInGroup.equals(other.friendsInGroup))
			return false;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FriendGroupImpl [groupName=" + groupName + ", friendsInGroup="
				+ friendsInGroup + "]";
	}
	
	
}
