package edu.jhu.cs.oose.fall2011.facemap.domainImpl;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import edu.jhu.cs.oose.fall2011.facemap.domain.FriendGroup;
import edu.jhu.cs.oose.fall2011.facemap.domain.Person;

/**
 * A named group of friends. Facemap users will organize their friends into
 * logical groups using instances of this class.
 * 
 * @author Orhan Ozguner
 * 
 */

@Entity
@Table(name="FRIEND_GROUP")
public class FriendGroupImpl implements FriendGroup {
	
	@Id
	@GeneratedValue
	@Column(name="GROUP_ID")
	private int groupId;
	
	@Column(name="GROUP_NAME")
	private String groupName;
	
	@ManyToMany(targetEntity=PersonImpl.class)
	@JoinTable(name = "FRIENDS_IN_GROUP", joinColumns = { @JoinColumn(name = "groupId") }, inverseJoinColumns = { @JoinColumn(name = "personId") })
	private Set<Person> friendsInGroup = new HashSet<Person>();
	
	@ManyToOne(targetEntity=PrivatePersonImpl.class)
	@JoinTable(name = "GROUPS", joinColumns = { @JoinColumn(name = "groupId") }, inverseJoinColumns = { @JoinColumn(name = "privatePersonId") })
	private PrivatePersonImpl privatePerson;
	
	
	
	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public PrivatePersonImpl getPrivatePerson() {
		return privatePerson;
	}

	public void setPrivatePerson(PrivatePersonImpl privatePerson) {
		this.privatePerson = privatePerson;
	}

	
	public String getGroupName() {
		return groupName;
	}
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	

	public Set<Person> getFriendsInGroup() {
		return friendsInGroup;
	}
	
	public void setFriendsInGroup(Set<Person> friendsInGroup) {
		this.friendsInGroup = friendsInGroup;
	}

	@Override
	public void addFriendToGroup(Person p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeFriendFromGroup(Person p) {
		// TODO Auto-generated method stub
		
	}

}
