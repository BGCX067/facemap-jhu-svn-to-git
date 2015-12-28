package edu.jhu.cs.oose.fall2011.facemap.login;

import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;

import edu.jhu.cs.oose.fall2011.facemap.domain.AccountImpl;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendGroup;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendRequest;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendRequestImpl;
import edu.jhu.cs.oose.fall2011.facemap.domain.Location;
import edu.jhu.cs.oose.fall2011.facemap.domain.Person;
import edu.jhu.cs.oose.fall2011.facemap.domain.PrivatePerson;
import edu.jhu.cs.oose.fall2011.facemap.domain.PrivatePersonImpl;
import edu.jhu.cs.oose.fall2011.facemap.server.AccountRepositoryException;


@Stateful
@LocalBean
@Remote(LoginSession.class)
public class LoginSessionImpl implements LoginSession {
	@EJB private EjbAccountRepo accountRepository;
	// Used to provide the remote business interface to this session instance from the local business interface
	@Resource private SessionContext sessionContext;
	private String loggedInUser;

	private AccountImpl account;
	
	// return remote business interface to this SFSB
	public LoginSession getRemoteBusinessInterface() {
		return sessionContext.getBusinessObject(LoginSession.class);
	}

	// part of the no-interface local view that LoginService sees and uses to inject the specified logged in user
	public void setLoggedInUser(String loggedInUser) {
		this.loggedInUser = loggedInUser;
	}
	
	/**
	 * Fills in the account field with the most recent values from the database
	 */
	private void loadAccount() {
		// TODO: this may be inefficient to load the account each time and unecessary
		account = accountRepository.findAccount(loggedInUser);
	}

	@Override
	public PrivatePerson getPrivatePerson() {
		loadAccount();
		return account.getPrivatePerson();
	}
	

	// @Remove indicates this session bean is to be removed after the invocation of this method
	@Remove
	@Override
	public void logout() {
		loadAccount();
		account.setLoggedIn(false);
		updateDatabase();
	}
	
	

	@Override
	public void addFriendToGroup(FriendGroup friendGroup, Person friend) {
		loadAccount();
		// assert that value objects from LoginSession remote interface are valid
		checkFriendGroup(friendGroup);
		checkFriend(friend);
		// perform update
		// find correct FriendGroup instance to modify
		// TODO: would be easier to remove and add friendGroup
		for(FriendGroup fg : account.getPrivatePerson().getFriendGroups()) {
			if(fg.equals(friendGroup)) {
				fg.addFriendToGroup(friend);
			}
		}
		// update database
		updateDatabase();
	}

	private void checkFriend(Person friend) {
		if(!account.getPrivatePerson().getFriends().contains(friend)) {
			throw new IllegalArgumentException("Invalid friend");
		}
	}

	private void checkFriendGroup(FriendGroup friendGroup) {
		if(!account.getPrivatePerson().getFriendGroups().contains(friendGroup)) {
			for(FriendGroup fg : account.getPrivatePerson().getFriendGroups()) {
				if(fg.equals(friendGroup) || friendGroup.equals(fg)) {
					System.out.println("Well this is strange");
					System.out.println(fg);
					System.out.println(friendGroup);
					System.out.println(fg.equals(friendGroup));
					System.out.println(friendGroup.equals(fg));
					System.out.println(account.getPrivatePerson().getFriendGroups().contains(friendGroup));
					System.out.println(account.getPrivatePerson().getFriendGroups().contains(fg));
				}
			}
			System.out.println(account.getPrivatePerson().getFriendGroups());
			throw new IllegalArgumentException("Invalid friendGroup");
		}
	}

	/**
	 * Saves the account field to the database
	 */
	private void updateDatabase() {
		try{
			accountRepository.save(account);
		} catch (AccountRepositoryException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void removeFriendFromGroup(FriendGroup friendGroup, Person friend) {
		loadAccount();
		// assert that value objects from LoginSession remote interface are valid
		checkFriendGroup(friendGroup);
		checkFriend(friend);
		// perform update
		// find correct FriendGroup instance to modify
		// TODO: would be easier to remove and add friendGroup
		for(FriendGroup fg : account.getPrivatePerson().getFriendGroups()) {
			if(fg.equals(friendGroup)) {
				fg.removeFriendFromGroup(friend);
			}
		}
		// update database
		updateDatabase();
	}

	@Override
	public void setName(String name) {
		loadAccount();
		account.getPrivatePerson().setName(name);
		updateDatabase();
	}

	@Override
	public void setMessage(String message) {
		loadAccount();
		account.getPrivatePerson().setMessage(message);
		updateDatabase();
	}

	@Override
	public void blockFriend(Person friend) {
		loadAccount();
		checkFriend(friend);
		account.getPrivatePerson().blockFriend(friend);
		updateDatabase();
	}

	@Override
	public void unblockFriend(Person friend) {
		loadAccount();
		checkFriend(friend);
		account.getPrivatePerson().unblockFriend(friend);
		updateDatabase();
	}

	@Override
	public void createFriendGroup(String name, Set<Person> initialMembers) {
		loadAccount();
		checkFriendSet(initialMembers);
		account.getPrivatePerson().createFriendGroup(name, initialMembers);
		updateDatabase();
	}

	private void checkFriendSet(Set<Person> friends) {
		for(Person friend : friends) {
			checkFriend(friend);
		}		
	}

	@Override
	public void removeFriendGroup(FriendGroup friendGroup) {
		loadAccount();
		checkFriendGroup(friendGroup);
		account.getPrivatePerson().removeFriendGroup(friendGroup);
		updateDatabase();
	}

	@Override
	public void requestFriend(String phoneOrEmail) {
		loadAccount();
		AccountImpl requesteeAccount = accountRepository.findAccount(phoneOrEmail);
		if(requesteeAccount == null) {
			throw new RuntimeException("Could not find account with phoneOrEmail: " + phoneOrEmail);
		}
		
		// create the friend request
		FriendRequest friendRequest = new FriendRequestImpl(account.getPrivatePerson().getSelf().getEmail(), requesteeAccount.getPrivatePerson().getSelf().getEmail());
		// add the friend request to requestor and requestee
		((PrivatePersonImpl)account.getPrivatePerson()).addFriendRequest(friendRequest);
		((PrivatePersonImpl)requesteeAccount.getPrivatePerson()).addFriendRequest(friendRequest);
		// TODO: do we need to send out notification to requestee here, or is that a function of adding a friend request to the requestee?
		
		try {
			accountRepository.save(account);
			accountRepository.save(requesteeAccount);
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void removeFriend(Person friend) {
		loadAccount();
		checkFriend(friend);
		
		AccountImpl requesteeAccount = accountRepository.findAccount(friend.getEmail());
		// TODO: check null condition?
		
		PrivatePersonImpl requestorPrivatePerson = (PrivatePersonImpl) account.getPrivatePerson();
		PrivatePersonImpl requesteePrivatePerson = (PrivatePersonImpl) requesteeAccount.getPrivatePerson();

		requestorPrivatePerson.removeFriend(requesteePrivatePerson.getSelf());
		requesteePrivatePerson.removeFriend(requestorPrivatePerson.getSelf());
		
		try {
			accountRepository.save(account);
			accountRepository.save(requesteeAccount);
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void respondToFriendRequest(FriendRequest friendRequest,
			boolean accept) {
		loadAccount();
		checkReceivedFriendRequest(friendRequest);
		
		AccountImpl requesteeAccount = accountRepository.findAccount(friendRequest.getRequesteeUserId());
		AccountImpl requestorAccount = accountRepository.findAccount(friendRequest.getRequestorUserId());
		// TODO: check null condition? Shouldn't happen since Friend request should be valid
		((PrivatePersonImpl)requesteeAccount.getPrivatePerson()).removeFriendRequest(friendRequest);
		((PrivatePersonImpl)requestorAccount.getPrivatePerson()).removeFriendRequest(friendRequest);
		
		// only link friends if accept is true
		if(accept) {
			((PrivatePersonImpl)requesteeAccount.getPrivatePerson()).addFriend(requestorAccount.getPrivatePerson().getSelf());
			((PrivatePersonImpl)requestorAccount.getPrivatePerson()).addFriend(requesteeAccount.getPrivatePerson().getSelf());
		}
		
		try{
		accountRepository.save(requesteeAccount);
		accountRepository.save(requestorAccount);
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private void checkReceivedFriendRequest(FriendRequest friendRequest) {
		if(!account.getPrivatePerson().getFriendRequestsReceived().contains(friendRequest)) {
			throw new IllegalArgumentException("Invalid friend request:" + friendRequest);
		}
	}

	@Override
	public Location getLocationOf(Person friend) {
		loadAccount();
		checkFriend(friend);
		
		AccountImpl targetAccount = accountRepository.findAccount(friend.getEmail());
		
		// TODO: decide if we only return locations of ppl who are logged in
		// kindof a moot point since we are pushing locations to the server instead of pulling them
		if(!targetAccount.getPrivatePerson().getBlockedFriends().contains(account.getPrivatePerson().getSelf())) {
			// TODO: implement
			Location location = targetAccount.getMyLocation();
			if(location != null) {
				account.getLastRetrievedLocation().put(targetAccount.getPrivatePerson().getSelf(), location);
				updateDatabase();
				return location;
			}
		}
		// If we fail to retrieve current location or requestor is blocked, return last known location
		return account.getLastRetrievedLocation().get(targetAccount.getPrivatePerson().getSelf());
	}

	@Override
	public void setMyLocation(Location location) {
		loadAccount();
		account.setMyLocation(location);
		updateDatabase();
	}


}
