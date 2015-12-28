package edu.jhu.cs.oose.fall2011.facemap.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;

/**
 * An Account object represents an account with the Facemap service. It contains
 * Facemap internal information such as user credentials and a link to more
 * public information such as the users's friends and groups.
 * 
 * @author Daniel Cranford
 */
@Entity
public class AccountImpl implements Account, Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * the user's password TODO don't store in plaintext
	 */
	private String password;
	/** User's PrivatePerson instance */
	private PrivatePerson privatePerson;
	private boolean loggedIn;
	/** view of user's last retrieved location of his friends */
	private Map<Person, Location> lastRetrievedLocation;
	/** user's location */
	private Location myLocation;
	
	public AccountImpl(String password, PrivatePerson privatePerson) {
		this.password = password;
		this.privatePerson = privatePerson;
		this.lastRetrievedLocation = new HashMap<Person, Location>();
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public PrivatePerson getPrivatePerson() {
		return privatePerson;
	}

	/**
	 * Returns live map view of the last retrieved location of the user's
	 * friends. Method intended to be invoked by server only.
	 * 
	 * @return live map view of the last retrieved location of a user's friends
	 */
	public Map<Person, Location> getLastRetrievedLocation() {
		return lastRetrievedLocation;
	}

	/**
	 * Returns true if user is logged in. Method for server use only.
	 * @return true is user is logged in
	 */
	public boolean isLoggedIn() {
		return loggedIn;
	}

	/**
	 * Set loggedIn value. Method for server use only
	 * @param loggedIn 
	 */
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	
	/**
	 * Get myLocation property. Method for server use only.
	 * @return the myLocation
	 */
	public Location getMyLocation() {
		return myLocation;
	}

	/**
	 * Set myLoaction value. Method for server use only.
	 * @param myLocation the myLocation to set
	 */
	public void setMyLocation(Location myLocation) {
		this.myLocation = myLocation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((lastRetrievedLocation == null) ? 0 : lastRetrievedLocation
						.hashCode());
		result = prime * result + (loggedIn ? 1231 : 1237);
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((privatePerson == null) ? 0 : privatePerson.hashCode());
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
		AccountImpl other = (AccountImpl) obj;
		if (lastRetrievedLocation == null) {
			if (other.lastRetrievedLocation != null)
				return false;
		} else if (!lastRetrievedLocation.equals(other.lastRetrievedLocation))
			return false;
		if (loggedIn != other.loggedIn)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (privatePerson == null) {
			if (other.privatePerson != null)
				return false;
		} else if (!privatePerson.equals(other.privatePerson))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AccountImpl [password=" + password + ", privatePerson="
				+ privatePerson + ", loggedIn=" + loggedIn
				+ ", lastRetrievedLocation=" + lastRetrievedLocation + "]";
	}
	
	
	
	

}
