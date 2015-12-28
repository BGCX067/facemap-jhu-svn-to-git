package edu.jhu.cs.oose.fall2011.facemap.login;

import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;

import edu.jhu.cs.oose.fall2011.facemap.domain.AccountImpl;
import edu.jhu.cs.oose.fall2011.facemap.domain.PersonImpl;
import edu.jhu.cs.oose.fall2011.facemap.domain.PrivatePersonImpl;
import edu.jhu.cs.oose.fall2011.facemap.entity.TestAccountRepositoryImpl;
import edu.jhu.cs.oose.fall2011.facemap.server.AccountRepository;
import edu.jhu.cs.oose.fall2011.facemap.server.AccountRepositoryException;

@Singleton
@LocalBean
public class EjbAccountRepo implements AccountRepository {
	AccountRepository testAccountRepository;
	
	@PostConstruct
	void postConstruct() {
		int size = 10;
		int friends = 4;
		PersonImpl person[] = new PersonImpl[size];
		PrivatePersonImpl privatePerson[] = new PrivatePersonImpl[size];
		AccountImpl account[] = new AccountImpl[size];
		
		// create accounts
		for (int i = 0; i < size; i++) {
			String x = Integer.toString(i);
			person[i] = new PersonImpl(x+x+x, x+"@mail.com", "123456789"+x);
			privatePerson[i] = new PrivatePersonImpl(person[i]);			
			account[i] = new AccountImpl(x, privatePerson[i]);
		}
		
		// link friends
		for (int i = 0; i < size; i++) {
			for(int j = 0; j < friends; j ++) {
				privatePerson[i].addFriend(person[(i-j-1+size)%size]);
			}
		}

		testAccountRepository = new TestAccountRepositoryImpl(Arrays.asList(account));
	}

	@Override
	public AccountImpl findAccount(String phoneOrEmail) {
		return testAccountRepository.findAccount(phoneOrEmail);
	}

	@Override
	public void save(AccountImpl account) throws AccountRepositoryException {
		testAccountRepository.save(account);
	}

	@Override
	public AccountImpl createAccount(String email, String phoneNo, String name,
			String password) throws AccountRepositoryException {
		return testAccountRepository.createAccount(email, phoneNo, name, password);
	}

}
