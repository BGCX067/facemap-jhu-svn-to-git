

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.security.Security;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.UUID;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.sasl.JBossSaslProvider;
import org.junit.Test;

import edu.jhu.cs.oose.fall2011.facemap.client.ClientApp;
import edu.jhu.cs.oose.fall2011.facemap.client.ClientAppException;
import edu.jhu.cs.oose.fall2011.facemap.client.v2.ClientAppImpl;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendGroup;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendRequest;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendRequestImpl;
import edu.jhu.cs.oose.fall2011.facemap.domain.Location;
import edu.jhu.cs.oose.fall2011.facemap.domain.Person;
import edu.jhu.cs.oose.fall2011.facemap.domain.PrivatePerson;
import edu.jhu.cs.oose.fall2011.facemap.entity.RandomLocationRetriever;
import edu.jhu.cs.oose.fall2011.facemap.login.LoginService;
import edu.jhu.cs.oose.fall2011.facemap.server.LocationRetriever;


/**
 * Functional test of client/server interaction
 * 
 * Prereq's: JBoss 7.1 server on localhost started with the FacemapServerApp 
 * deployed to it
 * 
 * Note: you should never use the raw domain model enitity types like this. I'm
 * just using them to make my assertions easier 
 * @author Daniel Cranford
 *
 */
public class TestClient {
	// returns locations around the JHU homewood campus
	LocationRetriever locationRetriever = new RandomLocationRetriever(39.329542, -76.620412, 0.02, 0.02);
	
	
	@Test
	public void test() throws ClientAppException, NamingException {
        // The EJB invocation happens via the JBoss Remoting project, which uses SASL for
        // authentication for client-server authentication. There are various different security algorithms that
        // SASL supports. In this example we use "anonymous" access to the server and for that we register
        // the JBossSaslProvider which provides support for that algorithm. Depending on how which algorithm
        // is used, this piece of code to register JBossSaslProvider, may or may not be required
        Security.addProvider(new JBossSaslProvider());

        Hashtable jndiProperties = new Hashtable();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        InitialContext context = new InitialContext(jndiProperties);
        LoginService loginService = (LoginService) context.lookup("ejb:FacemapServerAppJboss/FacemapEjbJboss/LoginServiceImpl!edu.jhu.cs.oose.fall2011.facemap.login.LoginService");
		
		// generate a new user id
		String unique1 = String.valueOf(UUID.randomUUID());
		String unique2 = String.valueOf(UUID.randomUUID());
		String unique3 = String.valueOf(UUID.randomUUID());
		String unique4 = String.valueOf(UUID.randomUUID());

		ClientApp clientApp1 = new ClientAppImpl(loginService);//new ClientAppImpl("FacemapServerApp", "FacemapEjb", "LoginServiceImpl");
		ClientApp clientApp2 = new ClientAppImpl(loginService);//new ClientAppImpl("FacemapServerApp", "FacemapEjb", "LoginServiceImpl");

		// register users
		clientApp1.register(unique1, unique1, unique1, unique1);
		clientApp1.register(unique2, unique2, unique2, unique2);
		clientApp1.register(unique3, unique3, unique3, unique3);
		clientApp1.register(unique4, unique4, unique4, unique4);

		// try to reregister
		try {
			clientApp1.register(unique1, unique1, unique1, unique1);
			fail("Expected an exception");
		} catch (ClientAppException ex) {
			// expected
		}

		// try bad userid
		try {
			clientApp1.login("asdf", unique1, locationRetriever);
			fail("Expected an exception");
		} catch(ClientAppException ex) {
			// expected
		}
		
		// try bad password
		try {
			clientApp1.login(unique1, "asdf", locationRetriever);
			fail("Expected an exception");
		} catch(ClientAppException ex) {
			// expected
		}
		
		// login
		clientApp1.login(unique1, unique1, locationRetriever);

		// login
		Person person2 = getPrivatePerson(clientApp2, unique2, unique2).getSelf();
		Person person3 = getPrivatePerson(clientApp2, unique3, unique3).getSelf();
		Person person4 = getPrivatePerson(clientApp2, unique4, unique4).getSelf();
		
		
		// check initial values
		PrivatePerson pp = clientApp1.getLoggedInUser();
		assertEquals(unique1, pp.getSelf().getEmail());
		assertEquals(unique1, pp.getSelf().getName());
		assertEquals(unique1, pp.getSelf().getPhoneNumber());
		assertEquals(Collections.EMPTY_SET, pp.getFriendGroups());
		assertEquals(Collections.EMPTY_SET, pp.getFriends());
		assertEquals(Collections.EMPTY_SET, pp.getFriendRequestsReceived());
		assertEquals(Collections.EMPTY_SET, pp.getFriendRequestsSent());
		assertEquals(Collections.EMPTY_SET, pp.getBlockedFriends());
		
		// request three friends
		clientApp1.requestFriend(unique2);
		clientApp1.requestFriend(unique3);
		clientApp1.requestFriend(unique4);
		
		// check requests
		assertEquals(
				new HashSet<FriendRequest>(Arrays.asList(
						new FriendRequestImpl(unique1, unique2),
						new FriendRequestImpl(unique1, unique3),
						new FriendRequestImpl(unique1, unique4))), 
				clientApp1.getLoggedInUser().getFriendRequestsSent());
		assertEquals(Collections.EMPTY_SET, clientApp1.getLoggedInUser().getFriendRequestsReceived());
		
		// login other ppl and accept/reject friend request
		respondToFirstFriendRequest(clientApp2, unique2, true);
		respondToFirstFriendRequest(clientApp2, unique3, true);
		respondToFirstFriendRequest(clientApp2, unique4, false);
		
		// assert results
		assertEquals(
				new HashSet<Person>(Arrays.asList(person2, person3)), 
				clientApp1.getLoggedInUser().getFriends());
		assertEquals(Collections.EMPTY_SET, clientApp1.getLoggedInUser().getFriendRequestsReceived());
		assertEquals(Collections.EMPTY_SET, clientApp1.getLoggedInUser().getFriendRequestsSent());
		

		// create a couple of groups
		Set<Person> justOneSet = Collections.singleton(person2);

		FriendGroup emptyGroup = clientApp1.getLoggedInUser().createFriendGroup("emptyGroup", Collections.EMPTY_SET);
		FriendGroup allGroup = clientApp1.getLoggedInUser().createFriendGroup("allGroup", clientApp1.getLoggedInUser().getFriends());
		FriendGroup justOne = clientApp1.getLoggedInUser().createFriendGroup("justOne", justOneSet);
		
		// assert results
		// check local copies
		checkFriendGroups(clientApp1, justOneSet, emptyGroup, allGroup, justOne);
		// and check server copies
		checkFriendGroups(clientApp1, justOneSet, 
				getFriendGroupByName(clientApp1.getLoggedInUser(), "emptyGroup"), 
				getFriendGroupByName(clientApp1.getLoggedInUser(), "allGroup"), 
				getFriendGroupByName(clientApp1.getLoggedInUser(), "justOne"));
		
		// add a friend to a group
		emptyGroup.addFriendToGroup(person2);
		
		// and assert the results
		assertEquals(Collections.singleton(person2), 
				getFriendGroupByName(clientApp1.getLoggedInUser(), "emptyGroup").getFriendsInGroup());
		
		// remove friend from group
		emptyGroup.removeFriendFromGroup(person2);
		
		// and assert the results
		assertEquals(Collections.EMPTY_SET, 
				getFriendGroupByName(clientApp1.getLoggedInUser(), "emptyGroup").getFriendsInGroup());
		
		
		// test locating friends
		Location location1 = clientApp1.getFriendLocation(person2);
		assertNotNull(location1);
		
		// login unique2 to update his last location
		clientApp2.login(unique2, unique2, locationRetriever);

		// check to see if location is new
		Location location2 = clientApp1.getFriendLocation(person2);
		assertNotNull(location2);
		assertFalse(location1.equals(location2));
		
		// now test blocked
		clientApp2.getLoggedInUser().blockFriend(clientApp1.getLoggedInUser().getSelf());
		
		// should return same location
		Location location3 = clientApp1.getFriendLocation(person2);
		assertEquals(location2, location3);
		
		// and test unblocked
		clientApp2.getLoggedInUser().unblockFriend(clientApp1.getLoggedInUser().getSelf());
		// logout/login cycle to push location to server
		clientApp2.logout();
		clientApp2.login(unique2, unique2, locationRetriever);

		// should return different location
		Location location4 = clientApp1.getFriendLocation(person2);
		assertNotNull(location4);
		assertFalse(location3.equals(location4));
		
		// now check remove friend
		// block other way as well
		clientApp1.getLoggedInUser().blockFriend(person2);
		clientApp2.removeFriend(clientApp1.getLoggedInUser().getSelf());
		clientApp2.logout();
		
		// check results
		assertFalse(clientApp1.getLoggedInUser().getFriends().contains(person2));
		assertFalse(clientApp1.getLoggedInUser().getBlockedFriends().contains(person2));
		for(FriendGroup fg : clientApp1.getLoggedInUser().getFriendGroups()) {
			assertFalse(fg.getFriendsInGroup().contains(person2));
		}
		
		
		// logout
		clientApp1.logout();

	}

	/**
	 * @param clientApp
	 * @param justOneSet
	 * @param emptyGroup
	 * @param allGroup
	 * @param justOne
	 */
	private void checkFriendGroups(ClientApp clientApp, Set<Person> justOneSet,
			FriendGroup emptyGroup, FriendGroup allGroup, FriendGroup justOne) {
		assertEquals("emptyGroup", emptyGroup.getGroupName());
		assertEquals(Collections.EMPTY_SET, emptyGroup.getFriendsInGroup());
		assertEquals("allGroup", allGroup.getGroupName());
		assertEquals(clientApp.getLoggedInUser().getFriends(), allGroup.getFriendsInGroup());
		assertEquals("justOne", justOne.getGroupName());
		assertEquals(justOneSet, justOne.getFriendsInGroup());
	}

	/**
	 * @param username
	 * @param accept
	 * @throws ClientAppException
	 */
	private void respondToFirstFriendRequest(ClientApp clientApp, String username, boolean accept)
			throws ClientAppException {
		clientApp.login(username, username, locationRetriever);
		FriendRequest fr = clientApp.getLoggedInUser().getFriendRequestsReceived().toArray(new FriendRequest[0])[0];
		clientApp.respondToFriendRequest(fr, accept);
		clientApp.logout();
	}
	
	// TODO: method like this should go in PrivatePerson
	private FriendGroup getFriendGroupByName(PrivatePerson privatePerson, String groupName) {
		for(FriendGroup friendGroup : privatePerson.getFriendGroups()) {
			if(friendGroup.getGroupName().equals(groupName)){
				return friendGroup;
			}
		}
		throw new IllegalArgumentException("Invalid group name: " + groupName);
	}
	
	private PrivatePerson getPrivatePerson(ClientApp clientApp, String username, String password) throws ClientAppException {
		clientApp.login(username, username, locationRetriever);
		PrivatePerson result = clientApp.getLoggedInUser();
		clientApp.logout();
		return result;
	}
	
	
	
	
	
//	@Test
//	public void testLoginLogout() throws ClientAppException {
//		clientApp1.login("user1@mail.com", "password1", locationRetriever1);
//		assertEquals("user1@mail.com", clientApp1.getLoggedInUser().getSelf().getEmail());
//		clientApp1.logout();
//		assertNull(clientApp1.getLoggedInUser());
//	}
//	
//	@Test(expected=ClientAppException.class)
//	public void testLoginBadPassword() throws ClientAppException {
//		clientApp1.login("user1@mail.com", "baddPassword", locationRetriever1);
//	}
//	
//	@Test(expected=ClientAppException.class)
//	public void testLoginBadUser() throws ClientAppException {
//		clientApp1.login("baduser1@mail.com", "baddPassword", locationRetriever1);
//	}
//
//	@Test
//	public void testAddFriend() throws ClientAppException {
//		// login and request user
//		clientApp1.login("user1@mail.com", "password1", locationRetriever1);		
//		clientApp1.requestFriend("user2@mail.com");
//		
//		// ensure a friend request has been added to my list
//		FriendRequest[] sentRequests = clientApp1.getLoggedInUser().getFriendRequestsSent().toArray(new FriendRequest[0]);
//		assertEquals(1, sentRequests.length);
//		assertEquals(clientApp1.getLoggedInUser().getSelf().getEmail(), sentRequests[0].getRequestorUserId());
//		assertEquals(person2.getEmail(), sentRequests[0].getRequesteeUserId());
//
//		// login the other user
//		clientApp1.logout();
//		clientApp1.login("user2@mail.com", "password2", locationRetriever2);
//		
//		// ensure friend request has been added to other person's list
//		FriendRequest[] receivedRequests = clientApp1.getLoggedInUser().getFriendRequestsReceived().toArray(new FriendRequest[0]); 
//		assertEquals(1, receivedRequests.length);
//		assertEquals(person1.getEmail(), receivedRequests[0].getRequestorUserId());
//		assertEquals(clientApp1.getLoggedInUser().getSelf().getEmail(), receivedRequests[0].getRequesteeUserId());
//		
//		// respond to friend request
//		clientApp1.respondToFriendRequest(receivedRequests[0], true);
//		
//		// check that request has been removed and user1 and user2 are now friends
//		assertTrue(privatePerson1.getFriendRequestsSent().isEmpty());
//		assertTrue(privatePerson2.getFriendRequestsReceived().isEmpty());
//		assertTrue(privatePerson1.getFriends().contains(person2));
//		assertTrue(privatePerson2.getFriends().contains(person1));
//	}
//	
//	@Test(expected=ClientAppException.class)
//	public void testAddFriendNotFound() throws ClientAppException {
//		// login and request user
//		clientApp1.login("user1@mail.com", "password1", locationRetriever1);		
//		clientApp1.requestFriend("baduser@mail.com");
//	}
//	
//	@Test
//	public void testAcceptFriendRequest() throws ClientAppException {
//		// set up a friend request
//		FriendRequest friendRequest = new FriendRequestImpl(account1.getPrivatePerson().getSelf().getEmail(), account2.getPrivatePerson().getSelf().getEmail());
//		privatePerson1.addFriendRequest(friendRequest);
//		privatePerson2.addFriendRequest(friendRequest);
//		
//		// login the user
//		clientApp1.login("user2@mail.com", "password2", locationRetriever2);
//				
//		// respond to friend request
//		clientApp1.respondToFriendRequest(friendRequest, true);
//		
//		// check that request has been removed and user1 and user2 are now friends
//		assertTrue(privatePerson1.getFriendRequestsSent().isEmpty());
//		assertTrue(privatePerson2.getFriendRequestsReceived().isEmpty());
//		assertTrue(privatePerson1.getFriends().contains(person2));
//		assertTrue(privatePerson2.getFriends().contains(person1));
//	}
//
//	@Test
//	public void testRejectFriendRequest() throws ClientAppException {
//		// set up a friend request
//		FriendRequest friendRequest = new FriendRequestImpl(account1.getPrivatePerson().getSelf().getEmail(), account2.getPrivatePerson().getSelf().getEmail());
//		privatePerson1.addFriendRequest(friendRequest);
//		privatePerson2.addFriendRequest(friendRequest);
//		
//		// login the user and respond to friend request
//		clientApp1.login("user2@mail.com", "password2", locationRetriever2);
//		clientApp1.respondToFriendRequest(friendRequest, false);
//		
//		// check that request has been removed and user1 and user2 are now friends
//		assertTrue(privatePerson1.getFriendRequestsSent().isEmpty());
//		assertTrue(privatePerson2.getFriendRequestsReceived().isEmpty());
//		assertFalse(privatePerson1.getFriends().contains(person2));
//		assertFalse(privatePerson2.getFriends().contains(person1));
//		
//	}
//	
//	@Test
//	public void testRemoveFriend() throws ClientAppException {
//		// set up friendship
//		privatePerson1.addFriend(person2);
//		privatePerson2.addFriend(person1);
//		// and to make things interesting, each user is blocked from the other and part of two groups
//		privatePerson1.blockFriend(person2);
//		privatePerson2.blockFriend(person1);
//		privatePerson1.createFriendGroup("asdf", Collections.singleton((Person)person2));
//		privatePerson1.createFriendGroup("jkl;", Collections.singleton((Person)person2));
//		privatePerson2.createFriendGroup("asdf", Collections.singleton((Person)person1));
//		privatePerson2.createFriendGroup("jkl;", Collections.singleton((Person)person1));
//		
//		// login the user and remove friend
//		clientApp1.login("user1@mail.com", "password1", locationRetriever1);
//		clientApp1.removeFriend(account2.getPrivatePerson().getSelf());
//	
//		// ensure every reference in each account has been cleaned up
//		assertNotFriend(privatePerson1, person2);
//		assertNotFriend(privatePerson2, person1);
//		
//	}
//	
//	/**
//	 * asserts no link exists from person1 to person2 (but not the other way around)
//	 * @param person1
//	 * @param person2
//	 */
//	private void assertNotFriend(PrivatePerson person1, Person person2) {
//		assertFalse(person1.getFriends().contains(person2));
//		assertFalse(person1.getBlockedFriends().contains(person2));
//		for(FriendGroup friendGroup : person1.getFriendGroups()) {
//			assertFalse(friendGroup.getFriendsInGroup().contains(person2));
//		}
//	}
//	
//	@Test
//	public void testBlockUnblock() {
//		privatePerson1.blockFriend(person2);
//		assertTrue(privatePerson1.getBlockedFriends().contains(person2));
//		privatePerson1.unblockFriend(person2);
//		assertFalse(privatePerson1.getBlockedFriends().contains(person2));
//	}
//	
//	@Test
//	public void testAddRemoveGroup() {
//		FriendGroup fg = privatePerson1.createFriendGroup("asdf", Collections.EMPTY_SET);
//		assertTrue(privatePerson1.getFriendGroups().contains(fg));
//		privatePerson1.removeFriendGroup(fg);
//		assertFalse(privatePerson1.getFriendGroups().contains(fg));
//	}
//	
//	@Test
//	public void testGetLocation() throws ClientAppException {
//		// set up friendship
//		privatePerson1.addFriend(person2);
//		privatePerson2.addFriend(person1);
//
//		// simultaneously login both users
//		clientApp1.login("user1@mail.com", "password1", locationRetriever1);
//		clientApp2.login("user2@mail.com", "password2", locationRetriever2);
//		
//		// ensure we can get location for both
//		assertNotNull(clientApp1.getFriendLocation(person2));
//		assertNotNull(clientApp2.getFriendLocation(person1));
//	}
//	
//	@Test
//	public void testBlocked() throws ClientAppException {
//		// TODO: this test shares a lot of setup with above test - refactor!
//		// set up friendship
//		privatePerson1.addFriend(person2);
//		privatePerson2.addFriend(person1);
//
//		// simultaneously login both users
//		clientApp1.login("user1@mail.com", "password1", locationRetriever1);
//		clientApp2.login("user2@mail.com", "password2", locationRetriever2);
//
//		// get initial location
//		Location l1 = clientApp1.getFriendLocation(person2);
//		// get another location
//		Location l2 = clientApp1.getFriendLocation(person2);
//		
//		// ensure that the second location is different
//		assertFalse(l2.equals(l1));
//		
//		// now user2 blocks user1, so the returned location should be l2
//		privatePerson2.blockFriend(person1);
//		Location l3 = clientApp1.getFriendLocation(person2);
//		
//		// ensure returned location was l2 (last available location)
//		assertEquals(l2, l3);
//		
//		// now user2 unblocks user1 so user1 can see updated locations
//		privatePerson2.unblockFriend(person1);
//		Location l4 = clientApp1.getFriendLocation(person2);
//		
//		// ensure that the fourth location is different
//		assertFalse(l4.equals(l2));
//	}
//	
//	@Test
//	public void testRegister() throws ClientAppException {
//		clientApp1.register("user3@mail.com", "123-456-7893", "User Three", "user3");
//		
//		clientApp1.login("user3@mail.com", "user3", locationRetriever1);
//		Person user3 = clientApp1.getLoggedInUser().getSelf();
//		
//		assertEquals("user3@mail.com", user3.getEmail());
//		assertEquals("User Three", user3.getName());
//		assertEquals("123-456-7893", user3.getPhoneNumber());
//	}
	
	//public
}
