package edu.jhu.cs.oose.fall2011.facemap.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import edu.jhu.cs.oose.fall2011.facemap.domain.AccountImpl;
import edu.jhu.cs.oose.fall2011.facemap.domain.Person;
import edu.jhu.cs.oose.fall2011.facemap.domain.PersonImpl;
import edu.jhu.cs.oose.fall2011.facemap.domain.PrivatePersonImpl;
import edu.jhu.cs.oose.fall2011.facemap.server.AccountRepository;
import edu.jhu.cs.oose.fall2011.facemap.server.AccountRepositoryException;

public class TestAccountRepositoryImpl implements AccountRepository {
	
	List<AccountImpl> accounts;
	
	public TestAccountRepositoryImpl(List<AccountImpl> accounts) {
		this.accounts = new ArrayList<AccountImpl>(accounts);
	}

	@Override
	public AccountImpl findAccount(String phoneOrEmail) {
		for(AccountImpl a : accounts) {
			Person self = a.getPrivatePerson().getSelf();
			if(self.getEmail().equals(phoneOrEmail) || self.getPhoneNumber().equals(phoneOrEmail)) {
				return a;
			}
		}
		return null;
	}

	

	@Override
	public void save(AccountImpl account) throws AccountRepositoryException {
		ListIterator<AccountImpl> it = accounts.listIterator();
		while(it.hasNext()) {
			// if found an account with the same email, assume its an update
			if(it.next().getPrivatePerson().getSelf().getEmail().equals(account.getPrivatePerson().getSelf().getEmail())) {
				it.set(account);
				return;
			}
		}
		// otherwise add
		accounts.add(account);
	}


	@Override
	public AccountImpl createAccount(String email, String phoneNo, String name,
			String password) throws AccountRepositoryException {
		AccountImpl accountImpl = new AccountImpl(password, new PrivatePersonImpl(new PersonImpl(name, email, phoneNo)));
		accounts.add(accountImpl);
		return accountImpl;
	}
	
}