package edu.jhu.cs.oose.fall2011.facemap.client.v3;

import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import edu.jhu.cs.oose.fall2011.facemap.client.webservice.Command;
import edu.jhu.cs.oose.fall2011.facemap.client.webservice.Serializer;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendGroup;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendRequest;
import edu.jhu.cs.oose.fall2011.facemap.domain.Location;
import edu.jhu.cs.oose.fall2011.facemap.domain.Person;
import edu.jhu.cs.oose.fall2011.facemap.domain.PrivatePerson;
import edu.jhu.cs.oose.fall2011.facemap.login.LoginSession;

public class RestLoginSession implements LoginSession {
	private String url;
	private ServerInvoker serverInvoker;
	
	public RestLoginSession(String sessionId, String host, String port) {
		this.url = "http://" + host + ":" + port +"/FacemapWeb/rest/LoginSession/" + sessionId;
		serverInvoker = new ServerInvoker();
	}

	@Override
	public PrivatePerson getPrivatePerson() {
		PrivatePerson p;
		try {
			p = (PrivatePerson) Serializer.fromBytes(this.serverInvoker.postToServer(url, new Command("getPrivatePerson").toBytes()));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		
		return p;
	}

	@Override
	public void addFriendToGroup(FriendGroup friendGroup, Person friend) {
		try {
			this.serverInvoker.postToServer(url, new Command("addFriendToGroup", friendGroup, friend).toBytes());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}	
	}

	@Override
	public void removeFriendFromGroup(FriendGroup friendGroup, Person friend) {
		try {
			this.serverInvoker.postToServer(url, new Command("removeFriendFromGroup", friendGroup, friend).toBytes());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}	
	}

	@Override
	public void setName(String name) {
		try {
			this.serverInvoker.postToServer(url, new Command("setName", name).toBytes());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}	
	}

	@Override
	public void setMessage(String message) {
		try {
			this.serverInvoker.postToServer(url, new Command("setMessage", message).toBytes());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}	
	}

	@Override
	public void blockFriend(Person friend) {
		try {
			this.serverInvoker.postToServer(url, new Command("blockFriend", friend).toBytes());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}	
	}

	@Override
	public void unblockFriend(Person friend) {
		try {
			this.serverInvoker.postToServer(url, new Command("unblockFriend", friend).toBytes());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}	
	}

	@Override
	public void createFriendGroup(String name, Set<Person> initialMembers) {
		try {
			this.serverInvoker.postToServer(url, new Command("createFriendGroup", name, initialMembers).toBytes());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}	
	}

	@Override
	public void removeFriendGroup(FriendGroup friendGroup) {
		try {
			this.serverInvoker.postToServer(url, new Command("removeFriendGroup", friendGroup).toBytes());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}	
	}

	@Override
	public void requestFriend(String phoneOrEmail) {
		try {
			this.serverInvoker.postToServer(url, new Command("requestFriend", phoneOrEmail).toBytes());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}	
	}

	@Override
	public void removeFriend(Person friend) {
		try {
			this.serverInvoker.postToServer(url, new Command("removeFriend", friend).toBytes());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}	
	}

	@Override
	public void respondToFriendRequest(FriendRequest friendRequest, boolean accept) {
		try {
			this.serverInvoker.postToServer(url, new Command("respondToFriendRequest", friendRequest, accept).toBytes());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}	
	}

	@Override
	public Location getLocationOf(Person friend) {
		try {
			return (Location) Serializer.fromBytes(this.serverInvoker.postToServer(url, new Command("getLocationOf", friend).toBytes()));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void setMyLocation(Location location) {
		try {
			this.serverInvoker.postToServer(url, new Command("setMyLocation", location).toBytes());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}	
	}

	@Override
	public void logout() {
		try {
			this.serverInvoker.postToServer(url, new Command("logout").toBytes());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}	
	}
}
