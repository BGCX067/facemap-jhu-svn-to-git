package edu.jhu.cs.oose.fall2011.facemap.domainImpl;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import edu.jhu.cs.oose.fall2011.facemap.domain.Account;

/**
 * An Account object represents an account with the Facemap service. It contains
 * Facemap internal information such as user credentials and a link to more
 * public information such as the users's friends and groups.
 * 
 * @author Orhan Ozguner
 */

@Entity
//@NamedNativeQuery(name="AccountImpl.byName" , query="select * from ACCOUNT where = ?" , resultClass=AccountImpl.class)
@Table(name = "ACCOUNT")
public class AccountImpl implements Account {

	@Column(name="PASSWORD")
	private String  password;
	
	@OneToOne(cascade=CascadeType.ALL , fetch=FetchType.EAGER)
	@JoinColumn(name="Private_Person_FK")
	private PrivatePersonImpl privatePerson;
	
	@Id
	@GeneratedValue
	@Column(name="ACCOUNT_FK")
	private int accountId;
	
	
	public int getAccountId() {
		return accountId;
	}
	
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	public PrivatePersonImpl getPrivatePerson() {
		return privatePerson;
	}
	
	public void setPrivatePerson(PrivatePersonImpl privatePerson) {
		this.privatePerson = privatePerson;
	}

}
