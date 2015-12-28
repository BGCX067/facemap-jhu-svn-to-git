package edu.jhu.cs.oose.fall2011.facemap.entity;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.jhu.cs.oose.fall2011.facemap.client.ClientApp;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientAppException;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientAppImpl;
import edu.jhu.cs.oose.fall2011.facemap.domain.AccountImpl;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendGroup;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendRequest;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendRequestImpl;
import edu.jhu.cs.oose.fall2011.facemap.domain.Location;
import edu.jhu.cs.oose.fall2011.facemap.domain.Person;
import edu.jhu.cs.oose.fall2011.facemap.domain.PersonImpl;
import edu.jhu.cs.oose.fall2011.facemap.domain.PrivatePerson;
import edu.jhu.cs.oose.fall2011.facemap.domain.PrivatePersonImpl;
import edu.jhu.cs.oose.fall2011.facemap.server.LocationRetriever;
import edu.jhu.cs.oose.fall2011.facemap.server.LocationServiceImpl;
import edu.jhu.cs.oose.fall2011.facemap.server.ServerApp;
import edu.jhu.cs.oose.fall2011.facemap.server.ServerAppImpl;

/**
 * In the entity package for easy access to the internal of the entities
 * @author Daniel Cranford
 *
 */
public class TestClient {
	
	ClientApp clientApp1;
	AccountImpl account1;
	AccountImpl account2;
	PrivatePersonImpl privatePerson1;
	PrivatePersonImpl privatePerson2;
	PersonImpl person1;
	PersonImpl person2;
	LocationRetriever locationRetriever1;
	LocationRetriever locationRetriever2;
	ServerApp serverApp;
	private ClientApp clientApp2;
	
	
	@Before
	public void setup() {
		person1 = new PersonImpl("John Doe", "user1@mail.com", "123-456-7891");
		person2 = new PersonImpl("Jane Doe", "user2@mail.com", "123-456-7892");
		privatePerson1 = new PrivatePersonImpl(person1);
		privatePerson2 = new PrivatePersonImpl(person2);
		account1 = new AccountImpl("password1", privatePerson1);
		account2 = new AccountImpl("password2", privatePerson2);
		// returns locations near the JHU Homewood campus
		locationRetriever1 = new RandomLocationRetriever(39.329542, -76.620412, 0.02, 0.02);
		locationRetriever2 = new RandomLocationRetriever(39.329542, -76.620412, 0.02, 0.02);
		// setup and inject dependencies
		List<AccountImpl> accounts = Arrays.asList(account1, account2);
		serverApp = new ServerAppImpl(new TestAccountRepositoryImpl(accounts), new LocationServiceImpl());
		clientApp1 = new ClientAppImpl(serverApp);
		clientApp2 = new ClientAppImpl(serverApp);
	}
	
	@Test
	public void testLoginLogout() throws ClientAppException {
		clientApp1.login("user1@mail.com", "password1", locationRetriever1);
		assertEquals("user1@mail.com", clientApp1.getLoggedInUser().getSelf().getEmail());
		clientApp1.logout();
		assertNull(clientApp1.getLoggedInUser());
	}
	
	@Test(expected=ClientAppException.class)
	public void testLoginBadPassword() throws ClientAppException {
		clientApp1.login("user1@mail.com", "baddPassword", locationRetriever1);
	}
	
	@Test(expected=ClientAppException.class)
	public void testLoginBadUser() throws ClientAppException {
		clientApp1.login("baduser1@mail.com", "baddPassword", locationRetriever1);
	}

	@Test
	public void testAddFriend() throws ClientAppException {
		// login and request user
		clientApp1.login("user1@mail.com", "password1", locationRetriever1);		
		clientApp1.requestFriend("user2@mail.com");
		
		// ensure a friend request has been added to my list
		FriendRequest[] sentRequests = clientApp1.getLoggedInUser().getFriendRequestsSent().toArray(new FriendRequest[0]);
		assertEquals(1, sentRequests.length);
		assertEquals(clientApp1.getLoggedInUser().getSelf().getEmail(), sentRequests[0].getRequestorUserId());
		assertEquals(person2.getEmail(), sentRequests[0].getRequesteeUserId());

		// login the other user
		clientApp1.logout();
		clientApp1.login("user2@mail.com", "password2", locationRetriever2);
		
		// ensure friend request has been added to other person's list
		FriendRequest[] receivedRequests = clientApp1.getLoggedInUser().getFriendRequestsReceived().toArray(new FriendRequest[0]); 
		assertEquals(1, receivedRequests.length);
		assertEquals(person1.getEmail(), receivedRequests[0].getRequestorUserId());
		assertEquals(clientApp1.getLoggedInUser().getSelf().getEmail(), receivedRequests[0].getRequesteeUserId());
		
		// respond to friend request
		clientApp1.respondToFriendRequest(receivedRequests[0], true);
		
		// check that request has been removed and user1 and user2 are now friends
		assertTrue(privatePerson1.getFriendRequestsSent().isEmpty());
		assertTrue(privatePerson2.getFriendRequestsReceived().isEmpty());
		assertTrue(privatePerson1.getFriends().contains(person2));
		assertTrue(privatePerson2.getFriends().contains(person1));
	}
	
	@Test(expected=ClientAppException.class)
	public void testAddFriendNotFound() throws ClientAppException {
		// login and request user
		clientApp1.login("user1@mail.com", "password1", locationRetriever1);		
		clientApp1.requestFriend("baduser@mail.com");
	}
	
	@Test
	public void testAcceptFriendRequest() throws ClientAppException {
		// set up a friend request
		FriendRequest friendRequest = new FriendRequestImpl(account1.getPrivatePerson().getSelf().getEmail(), account2.getPrivatePerson().getSelf().getEmail());
		privatePerson1.addFriendRequest(friendRequest);
		privatePerson2.addFriendRequest(friendRequest);
		
		// login the user
		clientApp1.login("user2@mail.com", "password2", locationRetriever2);
				
		// respond to friend request
		clientApp1.respondToFriendRequest(friendRequest, true);
		
		// check that request has been removed and user1 and user2 are now friends
		assertTrue(privatePerson1.getFriendRequestsSent().isEmpty());
		assertTrue(privatePerson2.getFriendRequestsReceived().isEmpty());
		assertTrue(privatePerson1.getFriends().contains(person2));
		assertTrue(privatePerson2.getFriends().contains(person1));
	}

	@Test
	public void testRejectFriendRequest() throws ClientAppException {
		// set up a friend request
		FriendRequest friendRequest = new FriendRequestImpl(account1.getPrivatePerson().getSelf().getEmail(), account2.getPrivatePerson().getSelf().getEmail());
		privatePerson1.addFriendRequest(friendRequest);
		privatePerson2.addFriendRequest(friendRequest);
		
		// login the user and respond to friend request
		clientApp1.login("user2@mail.com", "password2", locationRetriever2);
		clientApp1.respondToFriendRequest(friendRequest, false);
		
		// check that request has been removed and user1 and user2 are now friends
		assertTrue(privatePerson1.getFriendRequestsSent().isEmpty());
		assertTrue(privatePerson2.getFriendRequestsReceived().isEmpty());
		assertFalse(privatePerson1.getFriends().contains(person2));
		assertFalse(privatePerson2.getFriends().contains(person1));
		
	}
	
	@Test
	public void testRemoveFriend() throws ClientAppException {
		// set up friendship
		privatePerson1.addFriend(person2);
		privatePerson2.addFriend(person1);
		// and to make things interesting, each user is blocked from the other and part of two groups
		privatePerson1.blockFriend(person2);
		privatePerson2.blockFriend(person1);
		privatePerson1.createFriendGroup("asdf", Collections.singleton((Person)person2));
		privatePerson1.createFriendGroup("jkl;", Collections.singleton((Person)person2));
		privatePerson2.createFriendGroup("asdf", Collections.singleton((Person)person1));
		privatePerson2.createFriendGroup("jkl;", Collections.singleton((Person)person1));
		
		// login the user and remove friend
		clientApp1.login("user1@mail.com", "password1", locationRetriever1);
		clientApp1.removeFriend(account2.getPrivatePerson().getSelf());
	
		// ensure every reference in each account has been cleaned up
		assertNotFriend(privatePerson1, person2);
		assertNotFriend(privatePerson2, person1);
		
	}
	
	/**
	 * asserts no link exists from person1 to person2 (but not the other way around)
	 * @param person1
	 * @param person2
	 */
	private void assertNotFriend(PrivatePerson person1, Person person2) {
		assertFalse(person1.getFriends().contains(person2));
		assertFalse(person1.getBlockedFriends().contains(person2));
		for(FriendGroup friendGroup : person1.getFriendGroups()) {
			assertFalse(friendGroup.getFriendsInGroup().contains(person2));
		}
	}
	
	@Test
	public void testBlockUnblock() {
		privatePerson1.blockFriend(person2);
		assertTrue(privatePerson1.getBlockedFriends().contains(person2));
		privatePerson1.unblockFriend(person2);
		assertFalse(privatePerson1.getBlockedFriends().contains(person2));
	}
	
	@Test
	public void testAddRemoveGroup() {
		FriendGroup fg = privatePerson1.createFriendGroup("asdf", Collections.EMPTY_SET);
		assertTrue(privatePerson1.getFriendGroups().contains(fg));
		privatePerson1.removeFriendGroup(fg);
		assertFalse(privatePerson1.getFriendGroups().contains(fg));
	}
	
	@Test
	public void testGetLocation() throws ClientAppException {
		// set up friendship
		privatePerson1.addFriend(person2);
		privatePerson2.addFriend(person1);

		// simultaneously login both users
		clientApp1.login("user1@mail.com", "password1", locationRetriever1);
		clientApp2.login("user2@mail.com", "password2", locationRetriever2);
		
		// ensure we can get location for both
		assertNotNull(clientApp1.getFriendLocation(person2));
		assertNotNull(clientApp2.getFriendLocation(person1));
	}
	
	@Test
	public void testBlocked() throws ClientAppException {
		// TODO: this test shares a lot of setup with above test - refactor!
		// set up friendship
		privatePerson1.addFriend(person2);
		privatePerson2.addFriend(person1);

		// simultaneously login both users
		clientApp1.login("user1@mail.com", "password1", locationRetriever1);
		clientApp2.login("user2@mail.com", "password2", locationRetriever2);

		// get initial location
		Location l1 = clientApp1.getFriendLocation(person2);
		// get another location
		Location l2 = clientApp1.getFriendLocation(person2);
		
		// ensure that the second location is different
		assertFalse(l2.equals(l1));
		
		// now user2 blocks user1, so the returned location should be l2
		privatePerson2.blockFriend(person1);
		Location l3 = clientApp1.getFriendLocation(person2);
		
		// ensure returned location was l2 (last available location)
		assertEquals(l2, l3);
		
		// now user2 unblocks user1 so user1 can see updated locations
		privatePerson2.unblockFriend(person1);
		Location l4 = clientApp1.getFriendLocation(person2);
		
		// ensure that the fourth location is different
		assertFalse(l4.equals(l2));
	}
	
	@Test
	public void testRegister() throws ClientAppException {
		clientApp1.register("user3@mail.com", "123-456-7893", "User Three", "user3");
		
		clientApp1.login("user3@mail.com", "user3", locationRetriever1);
		Person user3 = clientApp1.getLoggedInUser().getSelf();
		
		assertEquals("user3@mail.com", user3.getEmail());
		assertEquals("User Three", user3.getName());
		assertEquals("123-456-7893", user3.getPhoneNumber());
	}
	
	//public
}
