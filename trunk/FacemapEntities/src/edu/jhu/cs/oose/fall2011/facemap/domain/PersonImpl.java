package edu.jhu.cs.oose.fall2011.facemap.domain;

import java.io.Serializable;

/**
 * A Person object contains all the account information visible to friends of
 * this person.
 * 
 * @author Daniel Cranford
 *
 */
public class PersonImpl implements Person, Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String email;
	private String phoneNumber;
	private String message;

	public PersonImpl(String name, String email, String phoneNumber) {
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String getPhoneNumber() {
		return phoneNumber;
	}

	@Override
	public String getMessage() {
		return message;
	}
	
	/** should only be visible to application */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/** should only be visible to application */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
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
		PersonImpl other = (PersonImpl) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PersonImpl [name=" + name + ", email=" + email
				+ ", phoneNumber=" + phoneNumber + ", message=" + message + "]";
	}
	
	
}
