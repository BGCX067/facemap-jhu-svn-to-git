package edu.jhu.cs.oose.fall2011.facemap.domain;

import java.io.Serializable;

/**
 * Represents a request from the requestor to the requestee to add the requestor
 * as a friend. This will allow both the requestor and requestee to see each
 * other's location on the map and allow them to communicate with each other.
 * 
 * @author Daniel Cranford
 */
public class FriendRequestImpl implements FriendRequest, Serializable {

	private static final long serialVersionUID = 1L;
	private String requestorUserId;
	private String requesteeUserId;

	public FriendRequestImpl(String requestorUserId, String requesteeUserId) {
		this.requestorUserId = requestorUserId;
		this.requesteeUserId = requesteeUserId;
	}

	@Override
	public String getRequestorUserId() {
		return requestorUserId;
	}

	@Override
	public String getRequesteeUserId() {
		return requesteeUserId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((requesteeUserId == null) ? 0 : requesteeUserId.hashCode());
		result = prime * result
				+ ((requestorUserId == null) ? 0 : requestorUserId.hashCode());
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
		FriendRequestImpl other = (FriendRequestImpl) obj;
		if (requesteeUserId == null) {
			if (other.requesteeUserId != null)
				return false;
		} else if (!requesteeUserId.equals(other.requesteeUserId))
			return false;
		if (requestorUserId == null) {
			if (other.requestorUserId != null)
				return false;
		} else if (!requestorUserId.equals(other.requestorUserId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FriendRequestImpl [requestorUserId=" + requestorUserId
				+ ", requesteeUserId=" + requesteeUserId + "]";
	}
	
	
	
	
}
