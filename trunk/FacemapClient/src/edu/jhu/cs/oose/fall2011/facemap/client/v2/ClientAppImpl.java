package edu.jhu.cs.oose.fall2011.facemap.client.v2;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import edu.jhu.cs.oose.fall2011.facemap.client.ClientApp;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientAppException;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendGroup;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendRequest;
import edu.jhu.cs.oose.fall2011.facemap.domain.Location;
import edu.jhu.cs.oose.fall2011.facemap.domain.Person;
import edu.jhu.cs.oose.fall2011.facemap.domain.PrivatePerson;
import edu.jhu.cs.oose.fall2011.facemap.login.LoginService;
import edu.jhu.cs.oose.fall2011.facemap.login.LoginSession;
import edu.jhu.cs.oose.fall2011.facemap.server.LocationRetriever;

/**
 * ClientApp implementation which depends on the new login interface. Provides a
 * convenience constructor for the common case of performing the remote EJB
 * lookup.
 * <p>
 * Note: Care should be taken to logout for each login. Otherwise, SFSB
 * LoginSession instances will accumulate on the server.
 * 
 * @author Daniel Cranford
 * 
 */
public class ClientAppImpl implements ClientApp {

	private final LoginService loginService;
	private volatile LoginSession session;
	private volatile LocationRetriever locationRetriever;
	private Timer timer;
	private LocationUpdateTask task;
	private final long updatePeriodMs;

	public ClientAppImpl(LoginService loginService) {
		this.loginService = loginService;
		this.timer = new Timer(true);
		this.updatePeriodMs = 5 * 60 * 1000;	// 5 minutes
	}

	/**
	 * 
	 * @param appName
	 *            name of the facemap application
	 * @param deploymentName
	 *            name of the deployment containing the LoginService
	 * @param ejbName
	 *            name of the EJB implementing LoginService
	 * @throws NamingException
	 */
//	public ClientAppImpl(String appName, String deploymentName, String ejbName)
//			throws NamingException {
//		this((LoginService)InitialContext.doLookup(jndiNameFor(appName, deploymentName, ejbName, LoginService.class.getName())));
//	}

	/**
	 * Forms jndi name from components.
	 * 
	 * @param appName
	 * @param deploymentName
	 * @param ejbName
	 * @param ejbInterface
	 * @return
	 */
	private static String jndiNameFor(String appName, String deploymentName,
			String ejbName, String ejbInterface) {
		return "java:global/" + appName + "/" + deploymentName + "/" + ejbName
				+ "!" + ejbInterface;
	}

	@Override
	public void register(String email, String phoneNo, String name,
			String password) throws ClientAppException {
		try {
			loginService.register(email, phoneNo, name, password);
		} catch (Exception ex) {
			throw new ClientAppException("system level failure", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Also pushes my location to the server.
	 */
	@Override
	public void login(String userid, String password, LocationRetriever locationRetriever) throws ClientAppException {
		this.locationRetriever = locationRetriever;
		try {
			session = loginService.login(userid, password);
			session.setMyLocation(locationRetriever.retrieveLocation());
			this.task = new LocationUpdateTask();
			timer.scheduleAtFixedRate(task, updatePeriodMs, updatePeriodMs);
		} catch (Exception ex) {
			throw new ClientAppException("system level failure", ex);
		}
	}

	@Override
	public void logout() {
		task.cancel();
		task = null;
		session.logout();
		session = null;
		locationRetriever = null;
	}

	@Override
	public Location getFriendLocation(Person friend) throws ClientAppException {
		// TODO: could locally verify parameter instead of just delegating that
		// to the server
		try {
			return session.getLocationOf(friend);
		} catch (Exception ex) {
			throw new ClientAppException("system level failure", ex);
		}
	}

	@Override
	public PrivatePerson getLoggedInUser() {
		if(session == null) {
			return null;
		} else {
			// TODO: should cache this value?
			// TODO: catch Exception?
			return new PrivatePersonWrapper(session.getPrivatePerson());
		}
	}

	@Override
	public void requestFriend(String phoneOrEmail) throws ClientAppException {
		try {
			session.requestFriend(phoneOrEmail);
		} catch (Exception ex) {
			throw new ClientAppException("system level failure", ex);
		}
	}

	@Override
	public void removeFriend(Person friend) throws ClientAppException {
		try {
			session.removeFriend(friend);
		} catch (Exception ex) {
			throw new ClientAppException("system level failure", ex);
		}
	}

	@Override
	public void respondToFriendRequest(FriendRequest request, boolean accept)
			throws ClientAppException {
		try {
			session.respondToFriendRequest(request, accept);
		} catch (Exception ex) {
			throw new ClientAppException("system level failure", ex);
		}
	}

	/**
	 * This is a Decorator and Observer smashed into one class. Could decouple
	 * it and make more classes. But it's fine for now.
	 * 
	 * @author Daniel Cranford
	 * 
	 */
	private class PrivatePersonWrapper implements PrivatePerson {
		// the instance we are wrapping
		private final PrivatePerson privatePerson;

		public PrivatePersonWrapper(PrivatePerson privatePerson) {
			this.privatePerson = privatePerson;
		}

		@Override
		public Person getSelf() {
			return privatePerson.getSelf();
		}

		@Override
		public void setName(String name) {
			session.setName(name); // update server
			privatePerson.setName(name); // and local copy
		}

		@Override
		public void setMessage(String message) {
			session.setMessage(message); // update server
			privatePerson.setMessage(message); // and local copy
		}

		@Override
		public Set<Person> getFriends() {
			// Person is immutable, so no need to wrap
			return privatePerson.getFriends();
		}

		@Override
		public Set<Person> getBlockedFriends() {
			// Person is immutable, so no need to wrap
			return privatePerson.getBlockedFriends();
		}

		@Override
		public void blockFriend(Person friend) {
			session.blockFriend(friend); // update server
			privatePerson.blockFriend(friend); // update local copy
		}

		@Override
		public void unblockFriend(Person friend) {
			session.unblockFriend(friend); // update server
			privatePerson.unblockFriend(friend); // update local copy
		}

		@Override
		public Set<FriendGroup> getFriendGroups() {
			Set<FriendGroup> friendGroups = privatePerson.getFriendGroups();
			Set<FriendGroup> wrappedFriendGroups = new HashSet<FriendGroup>(
					friendGroups.size());
			for (FriendGroup friendGroup : friendGroups) {
				wrappedFriendGroups.add(new FriendGroupWrapper(friendGroup));
			}
			return Collections.unmodifiableSet(wrappedFriendGroups);
		}

		@Override
		public FriendGroup createFriendGroup(String name, Set<Person> friends)
				throws IllegalArgumentException {
			session.createFriendGroup(name, friends); // update server
			// update local copy and save locally returned VO
			FriendGroup newFriendGroup = privatePerson.createFriendGroup(name,
					friends);
			return new FriendGroupWrapper(newFriendGroup);
		}

		@Override
		public void removeFriendGroup(FriendGroup friendGroup) {
			FriendGroup unwrapped = ((FriendGroupWrapper)friendGroup).friendGroup;
			session.removeFriendGroup(unwrapped ); // update server
			privatePerson.removeFriendGroup(unwrapped); // update local copy

		}

		@Override
		public void blockFriendGroup(FriendGroup friendGroup) {
			// TODO: could avoid code duplication by making this a LoginSession
			// method
			for (Person friend : friendGroup.getFriendsInGroup()) {
				// blockFriend updates both server and local copy
				blockFriend(friend);
			}
		}

		@Override
		public void unblockFriendGroup(FriendGroup friendGroup) {
			// TODO: could avoid code duplication by making this a LoginSession
			// method
			for (Person friend : friendGroup.getFriendsInGroup()) {
				// unblockFriend updates both server and local copy
				unblockFriend(friend);
			}
		}

		@Override
		public Set<FriendRequest> getFriendRequestsSent() {
			// FriendRequest is immutable so no need to wrap
			return privatePerson.getFriendRequestsSent();
		}

		@Override
		public Set<FriendRequest> getFriendRequestsReceived() {
			// FriendRequest is immutable so no need to wrap
			return privatePerson.getFriendRequestsReceived();
		}

	}

	/**
	 * This is a Decorator and Observer smashed into one class. Could decouple
	 * it and make more classes. But it's fine for now.
	 * 
	 * @author Daniel Cranford
	 * 
	 */
	private class FriendGroupWrapper implements FriendGroup {
		// the FriendGroup value object we are wrapping and delegating to
		private final FriendGroup friendGroup;

		public FriendGroupWrapper(FriendGroup friendGroup) {
			this.friendGroup = friendGroup;
		}

		@Override
		public String getGroupName() {
			return friendGroup.getGroupName();
		}

		@Override
		public void addFriendToGroup(Person p) {
			session.addFriendToGroup(friendGroup, p); // update server
			friendGroup.addFriendToGroup(p); // update local copy
		}

		@Override
		public void removeFriendFromGroup(Person p) {
			session.removeFriendFromGroup(friendGroup, p); // update server
			friendGroup.removeFriendFromGroup(p); // update local copy
		}

		@Override
		public Set<Person> getFriendsInGroup() {
			// Person is immutable don't have to wrap
			return friendGroup.getFriendsInGroup();
		}
	}
	
	/**
	 * Using my own thread instead of Time so that I can make the thread a daemon thread 
	 * @author Daniel Cranford
	 *
	 */
	private class LocationUpdateTask extends TimerTask {
		@Override
		public void run() {
			session.setMyLocation(locationRetriever.retrieveLocation());			
		}
	}

}
