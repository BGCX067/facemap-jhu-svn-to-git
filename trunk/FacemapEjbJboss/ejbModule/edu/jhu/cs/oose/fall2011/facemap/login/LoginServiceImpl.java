package edu.jhu.cs.oose.fall2011.facemap.login;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.naming.InitialContext;

import edu.jhu.cs.oose.fall2011.facemap.domain.AccountImpl;
import edu.jhu.cs.oose.fall2011.facemap.server.AccountRepositoryException;

@Stateless
@Remote(LoginService.class)
public class LoginServiceImpl implements LoginService {
	@EJB EjbAccountRepo accountRepository;
	
	@Override
	public LoginSession login(String email, String password) {
		AccountImpl account = accountRepository.findAccount(email);
		if(account == null) {
			throw new RuntimeException("login failed");
		}
		
		if(!account.getPassword().equals(password)) {
			throw new RuntimeException("login failed");
		}
		
		if(account.isLoggedIn()) {
			throw new RuntimeException("Already logged in");
		}
		
		try {
			account.setLoggedIn(true);
			accountRepository.save(account);
			//locationService.setLocationRetriever(userid, clientLocationRetriever);

		
			// THis works!!! (requires an extra local method with a SessionContext call to getBusinessObject in LoginSessionImpl)
			LoginSessionImpl loginSession = InitialContext.doLookup("java:module/" + LoginSessionImpl.class.getSimpleName() + "!" + LoginSessionImpl.class.getName());
			loginSession.setLoggedInUser(account.getPrivatePerson().getSelf().getEmail());

			// same session bean, different proxy interface
			return loginSession.getRemoteBusinessInterface();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void register(String email, String phoneNo, String name,
			String password) {
		
		checkEmailAndPhone(email, phoneNo);

		try {
			accountRepository.createAccount(email, phoneNo, name, password);
		} catch (AccountRepositoryException e) {
			// TODO properly handle (and wrap) exception
			throw new RuntimeException(e);
		}
	}

	
	private void checkEmailAndPhone(String email, String phoneNo) {
		if(accountRepository.findAccount(email) != null) {
			throw new RuntimeException("An account already exists with the given email address: " + email);
		}
		if(accountRepository.findAccount(phoneNo) != null) {
			throw new RuntimeException("An account already exists with the given phone number : " + phoneNo);			
		}
	}
	

}
