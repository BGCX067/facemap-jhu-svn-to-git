package edu.jhu.cs.oose.fall2011.facemap.domain;

import javax.persistence.Entity;

/**
 * An Account object represents an account with the Facemap service. It contains
 * Facemap internal information such as user credentials and a link to more
 * public information such as the users's friends and groups.
 * 
 * @author Daniel Cranford
 */
@Entity
public interface Account {

	/**
	 * @return the string used to authenticate this account
	 */
	String getPassword();

	/**
	 * @return PrivatePerson instance that contains the users friends, groups,
	 *         etc
	 */
	PrivatePerson getPrivatePerson();

}