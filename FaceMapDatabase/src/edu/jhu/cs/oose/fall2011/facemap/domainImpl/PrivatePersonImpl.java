package edu.jhu.cs.oose.fall2011.facemap.domainImpl;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import edu.jhu.cs.oose.fall2011.facemap.domain.FriendGroup;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendRequest;
import edu.jhu.cs.oose.fall2011.facemap.domain.Person;
import edu.jhu.cs.oose.fall2011.facemap.domain.PrivatePerson;

/**
 * This class contains information about a Facemap user's friends and groups. It
 * is only visible to the user it belongs to.
 * 
 * @author Orhan Ozguner
 * 
 */

@Entity
@Table(name="PRIVATE_PERSON")
public class PrivatePersonImpl implements PrivatePerson {
	@Id
	@GeneratedValue
	@Column(name="PRIVATE_PERSON_ID")
	private int privatePersonId;
	
	@OneToOne(cascade=CascadeType.ALL , fetch=FetchType.EAGER)
	@JoinColumn(name="Person_FK")
	private PersonImpl self;
	
	@OneToMany(targetEntity=PersonImpl.class)
	@JoinTable(name="FRIENDS")
	private Set<Person> friends = new HashSet<Person>();
	
	@OneToMany(targetEntity=PersonImpl.class)
	@JoinTable(name="BLOCKED_FRIENDS")
	private Set<Person> blockedFriends = new HashSet<Person>();
	
	@ManyToMany(targetEntity=FriendGroupImpl.class , mappedBy="privatePerson")
	private Set<FriendGroup> friendGroups = new HashSet<FriendGroup>();
	
	@ManyToMany(targetEntity=FriendRequestImpl.class , mappedBy="privatePerson" , cascade=CascadeType.ALL , fetch=FetchType.EAGER)
	private Set<FriendRequest> requestsISent = new HashSet<FriendRequest>();
	
	@ManyToMany(targetEntity=FriendRequestImpl.class , mappedBy="privatePerson" ,cascade=CascadeType.ALL , fetch=FetchType.EAGER)
	private Set<FriendRequest> requestsAwaitingMyResponse = new HashSet<FriendRequest>();
	

	public void setPrivatePersonId(int privatePersonId) {
		this.privatePersonId = privatePersonId;
	}

	public int getPrivatePersonId() {
		return privatePersonId;
	}

	public void setPersonId(int privatePersonId) {
		this.privatePersonId = privatePersonId;
	}

	
	public PersonImpl getSelf() {
		return self;
	}

	public void setSelf(PersonImpl self) {
		this.self = self;
	}


	public Set<Person> getFriends() {
		return friends;
	}

	public void setFriends(Set<Person> friends) {
		this.friends = friends;
	}


	public Set<FriendGroup> getFriendGroups() {
		return friendGroups;
	}

	public void setFriendGroups(Set<FriendGroup> friendGroups) {
		this.friendGroups = friendGroups;
	}

	
	public Set<Person> getBlockedFriends() {
		return blockedFriends;
	}


	public void setBlockedFriends(Set<Person> blockedFriends) {
		this.blockedFriends = blockedFriends;
	}


	public Set<FriendRequest> getRequestsISent() {
		return requestsISent;
	}


	public void setRequestsISent(Set<FriendRequest> requestsISent) {
		this.requestsISent = requestsISent;
	}

	

	public Set<FriendRequest> getRequestsAwaitingMyResponse() {
		return requestsAwaitingMyResponse;
	}


	public void setRequestsAwaitingMyResponse(
			Set<FriendRequest> requestsAwaitingMyResponse) {
		this.requestsAwaitingMyResponse = requestsAwaitingMyResponse;
	}

	
	
	/*public PrivatePersonImpl(Person self) {
		this.self = self;
		this.friends = new HashSet<Person>();
		this.friendGroups = new HashSet<FriendGroup>();
		this.blockedFriends = new HashSet<Person>();
		this.requestsISent = new HashSet<FriendRequest>();
		this.requestsAwaitingMyResponse = new HashSet<FriendRequest>();
	}*/
}
