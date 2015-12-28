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
import javax.persistence.Table;

import edu.jhu.cs.oose.fall2011.facemap.domain.FriendRequest;
import edu.jhu.cs.oose.fall2011.facemap.domain.PrivatePerson;

/**
 * Represents a request from the requestor to the requestee to add the requestor
 * as a friend. This will allow both the requestor and requestee to see each
 * other's location on the map and allow them to communicate with each other.
 * 
 * @author Orhan Ozguner
 */

@Entity
@Table(name="FRIEND_REQUEST")
public class FriendRequestImpl implements FriendRequest {
	
	@Id
	@GeneratedValue
	@Column(name="REQUEST_ID")
	private int requestId;
	
	@Column(name="REQUESTOR")
	private String requestorUserId;
	
	@Column(name="REQUESTEE")
	private String requesteeUserId;
	
	@ManyToMany(targetEntity=PrivatePersonImpl.class)
	@JoinTable(name = "JOIN_FRIEND_REQUEST", joinColumns = { @JoinColumn(name = "requestId") }, inverseJoinColumns = { @JoinColumn(name = "privatePersonId") })
	private Set<PrivatePerson> privatePerson = new HashSet<PrivatePerson>();
	
	
	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public Set<PrivatePerson> getPrivatePerson() {
		return privatePerson;
	}

	public void setPrivatePerson(Set<PrivatePerson> privatePerson) {
		this.privatePerson = privatePerson;
	}

	public String getRequestorUserId() {
		return requestorUserId;
	}
	
	public void setRequestorUserId(String requestorUserId) {
		this.requestorUserId = requestorUserId;
	}
	
	public String getRequesteeUserId() {
		return requesteeUserId;
	}
	
	public void setRequesteeUserId(String requesteeUserId) {
		this.requesteeUserId = requesteeUserId;
	}

}
