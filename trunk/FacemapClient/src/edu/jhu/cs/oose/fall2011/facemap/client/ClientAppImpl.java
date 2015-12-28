package edu.jhu.cs.oose.fall2011.facemap.client;

import edu.jhu.cs.oose.fall2011.facemap.domain.FriendRequest;
import edu.jhu.cs.oose.fall2011.facemap.domain.Location;
import edu.jhu.cs.oose.fall2011.facemap.domain.Person;
import edu.jhu.cs.oose.fall2011.facemap.domain.PrivatePerson;
import edu.jhu.cs.oose.fall2011.facemap.server.LocationRetriever;
import edu.jhu.cs.oose.fall2011.facemap.server.ServerApp;
import edu.jhu.cs.oose.fall2011.facemap.server.ServerAppException;

public class ClientAppImpl implements ClientApp {
	private ServerApp serverApp;
	private PrivatePerson loggedInUser;
	
	public ClientAppImpl(ServerApp serverApp) {
		this.serverApp = serverApp;
	}
	
	@Override
	public void login(String userid, String password, LocationRetriever locationRetriever) throws ClientAppException {
		try {
			loggedInUser = serverApp.login(userid, password, locationRetriever);
		} catch (ServerAppException ex) {
			throw new ClientAppException("Exception logging in", ex);
		}
	}

	@Override
	public void logout() {
		serverApp.logout(loggedInUser.getSelf().getEmail());
		loggedInUser = null;
	}

	@Override
	public PrivatePerson getLoggedInUser() {
		return loggedInUser;
	}

	@Override
	public void requestFriend(String phoneOrEmail) throws ClientAppException {
		try {
			serverApp.requestFriend(loggedInUser.getSelf().getEmail(), phoneOrEmail);
		} catch (ServerAppException ex) {
			throw new ClientAppException(ex);
		}
	}

	@Override
	public void register(String email, String phoneNo, String name,
			String password) throws ClientAppException {
		try {
			serverApp.register(email, phoneNo, name, password);
		} catch (ServerAppException ex) {
			throw new ClientAppException("Exception registering", ex);
		}
		
	}

	@Override
	public void respondToFriendRequest(FriendRequest request, boolean accept)
			throws ClientAppException {
		// ensure this friend request is valid
		if(!loggedInUser.getFriendRequestsReceived().contains(request)) {
			throw new ClientAppException("Invalid request");
		}
		
		serverApp.reposondToFriendRequest(request, accept);
		
	}

	@Override
	public void removeFriend(Person friend) throws ClientAppException {
		try {
			serverApp.removeFriend(loggedInUser.getSelf().getEmail(), friend.getEmail());
		} catch (ServerAppException ex) {
			throw new ClientAppException(ex);
		}
	}

	@Override
	public Location getFriendLocation(Person friend) throws ClientAppException {
		try {
			// TODO: throws exception and returns null ??? fix interface to make sense
			return serverApp.getLocation(loggedInUser.getSelf().getEmail(), friend.getEmail());
		} catch (ServerAppException ex) {
			throw new ClientAppException(ex);
		}
	}

	
}
