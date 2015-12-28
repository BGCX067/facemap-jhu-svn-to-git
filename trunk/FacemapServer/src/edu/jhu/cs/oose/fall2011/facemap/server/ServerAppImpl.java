package edu.jhu.cs.oose.fall2011.facemap.server;

import edu.jhu.cs.oose.fall2011.facemap.domain.Account;
import edu.jhu.cs.oose.fall2011.facemap.domain.AccountImpl;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendRequest;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendRequestImpl;
import edu.jhu.cs.oose.fall2011.facemap.domain.Location;
import edu.jhu.cs.oose.fall2011.facemap.domain.PrivatePerson;
import edu.jhu.cs.oose.fall2011.facemap.domain.PrivatePersonImpl;

/**
 * Main application class for the server
 * @author Daniel Cranford
 *
 */
public class ServerAppImpl implements ServerApp {
	private AccountRepository accountRepository;
	private LocationService locationService;

	public ServerAppImpl(AccountRepository accountRepository, LocationService locationService) {
		this.accountRepository = accountRepository;
		this.locationService = locationService;
	}

	@Override
	public PrivatePerson login(String userid, String password, LocationRetriever clientLocationRetriever) throws ServerAppException {
		AccountImpl account = accountRepository.findAccount(userid);
		if(account == null) {
			throw new ServerAppException("login failed");
		}
		
		if(account.isLoggedIn()) {
			throw new ServerAppException("Already logged in");
		}
		
		if(!account.getPassword().equals(password)) {
			throw new ServerAppException("login failed");
		}
		
		account.setLoggedIn(true);
		locationService.setLocationRetriever(userid, clientLocationRetriever);
		return account.getPrivatePerson();

	}

	@Override
	public void requestFriend(String requestorUserid, String phoneOrEmail) throws ServerAppException {
		Account requestorAccount = accountRepository.findAccount(requestorUserid);
		if(requestorAccount == null) {
			throw new ServerAppException("Invalid requestorUserId:" + requestorUserid);
		}
		Account requesteeAccount = accountRepository.findAccount(phoneOrEmail);
		if(requesteeAccount == null) {
			throw new ServerAppException("Could not find account with phoneOrEmail: " + phoneOrEmail);
		}
		
		// create the friend request
		FriendRequest friendRequest = new FriendRequestImpl(requestorAccount.getPrivatePerson().getSelf().getEmail(), requesteeAccount.getPrivatePerson().getSelf().getEmail());
		// add the friend request to requestor and requestee
		((PrivatePersonImpl)requestorAccount.getPrivatePerson()).addFriendRequest(friendRequest);
		((PrivatePersonImpl)requesteeAccount.getPrivatePerson()).addFriendRequest(friendRequest);
		// TODO: do we need to send out notification to requestee here, or is that a function of adding a friend request to the requestee?
	}

	@Override
	public void register(String email, String phoneNo, String name,
			String password) throws ServerAppException {
		// ensure account doesn't already exist
		checkEmailAndPhone(email, phoneNo);
		
		// create new account
		createNewAccount(email, phoneNo, name, password);
	}

	private void checkEmailAndPhone(String email, String phoneNo)
			throws ServerAppException {
		if(accountRepository.findAccount(email) != null) {
			throw new ServerAppException("An account already exists with the given email address: " + email);
		}
		if(accountRepository.findAccount(phoneNo) != null) {
			throw new ServerAppException("An account already exists with the given phone number : " + phoneNo);			
		}
	}

	private void createNewAccount(String email, String phoneNo, String name,
			String password) throws ServerAppException {
		try {
			accountRepository.save(accountRepository.createAccount(email, phoneNo, name, password));
		} catch (AccountRepositoryException ex) {
			throw new ServerAppException("Error saving new account", ex);
		}
	}

	@Override
	public void reposondToFriendRequest(FriendRequest request, boolean accept) {
		Account requesteeAccount = accountRepository.findAccount(request.getRequesteeUserId());
		Account requestorAccount = accountRepository.findAccount(request.getRequestorUserId());
		// TODO: check null condition? Shouldn't happen since Friend request should be valid
		((PrivatePersonImpl)requesteeAccount.getPrivatePerson()).removeFriendRequest(request);
		((PrivatePersonImpl)requestorAccount.getPrivatePerson()).removeFriendRequest(request);
		
		// only link friends if accept is true
		if(accept) {
			((PrivatePersonImpl)requesteeAccount.getPrivatePerson()).addFriend(requestorAccount.getPrivatePerson().getSelf());
			((PrivatePersonImpl)requestorAccount.getPrivatePerson()).addFriend(requesteeAccount.getPrivatePerson().getSelf());
		}
	}

	@Override
	public void removeFriend(String removerUserId, String removeeUserId)
			throws ServerAppException {
		Account requesteeAccount = accountRepository.findAccount(removerUserId);
		Account requestorAccount = accountRepository.findAccount(removeeUserId);
		// TODO: check null condition?
		
		PrivatePersonImpl requesteePrivatePerson = (PrivatePersonImpl) requesteeAccount.getPrivatePerson();
		PrivatePersonImpl requestorPrivatePerson = (PrivatePersonImpl) requestorAccount.getPrivatePerson();

		requesteePrivatePerson.removeFriend(requestorPrivatePerson.getSelf());
		requestorPrivatePerson.removeFriend(requesteePrivatePerson.getSelf());		
	}

	@Override
	public Location getLocation(String requestorId, String targetUserId)
			throws ServerAppException {
		AccountImpl requestorAccount = accountRepository.findAccount(requestorId);
		AccountImpl targetAccount = accountRepository.findAccount(targetUserId);
		
		// sufficient to check friendship one way, since two way friendships
		// are enforced by the system
		if(!requestorAccount.getPrivatePerson().getFriends().contains(targetAccount.getPrivatePerson().getSelf())) {
			throw new ServerAppException(targetUserId + " is not a friend of " + requestorId);
		}
		
		if(targetAccount.isLoggedIn() && !targetAccount.getPrivatePerson().getBlockedFriends().contains(requestorAccount.getPrivatePerson().getSelf())) {
			Location location = locationService.getLocation(targetUserId);
			if(location != null) {
				requestorAccount.getLastRetrievedLocation().put(targetAccount.getPrivatePerson().getSelf(), location);
				return location;
			}
		}
		// If we fail to retrieve current location or requestor is blocked, return last known location
		return requestorAccount.getLastRetrievedLocation().get(targetAccount.getPrivatePerson().getSelf());
		
	}

	@Override
	public void logout(String userid) {
		AccountImpl account = accountRepository.findAccount(userid);
		account.setLoggedIn(false);
		locationService.removeLocationRetriever(userid);
	}
}
