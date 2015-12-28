package edu.jhu.cs.oose.fall2011.facemap.server;

import edu.jhu.cs.oose.fall2011.facemap.domain.Account;
import edu.jhu.cs.oose.fall2011.facemap.domain.AccountImpl;

/**
 * Main data access interface for the application.
 * 
 * @author Daniel Cranford
 *
 */
public interface AccountRepository {
	/**
	 * Looks for an Account with the given phone number or email address.
	 * @param phoneOrEmail "john@gmail.com" or "123-456-7890"
	 * @return Account with given phone number or email address or null if not found
	 */
	AccountImpl findAccount(String phoneOrEmail);

	/**
	 * Updates an existing account
	 * @param account
	 * @throws AccountRepositoryException if saving instance fails
	 */
	void save(AccountImpl account) throws AccountRepositoryException;
	
	/** 
	 * creates and returns a new Account instance with the given parameters
	 * @param email
	 * @param phoneNo
	 * @param name
	 * @param password
	 * @return new Account instance
	 * @throws AccountRepositoryException if creating
	 */
	AccountImpl createAccount(String email, String phoneNo, String name, String password) throws AccountRepositoryException;
}
