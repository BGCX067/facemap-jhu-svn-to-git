package ws;

import java.util.Set;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import edu.jhu.cs.oose.fall2011.facemap.domain.FriendGroup;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendRequest;
import edu.jhu.cs.oose.fall2011.facemap.domain.Location;
import edu.jhu.cs.oose.fall2011.facemap.domain.Person;
import edu.jhu.cs.oose.fall2011.facemap.domain.PrivatePerson;
import edu.jhu.cs.oose.fall2011.facemap.login.LoginSession;


//@WebService
public class LoginSessionWebService implements LoginSession {
	LoginSession loginSession;
	@Resource WebServiceContext wsContext;
	
	private void killLoginSession() {
		 MessageContext mc = wsContext.getMessageContext();
	     HttpSession session = ((javax.servlet.http.HttpServletRequest)mc.get(MessageContext.SERVLET_REQUEST)).getSession(true);
	     session.removeAttribute("loginSession");
	}
	
	private void getLoginSession() {
		 MessageContext mc = wsContext.getMessageContext();
	     HttpSession session = ((javax.servlet.http.HttpServletRequest)mc.get(MessageContext.SERVLET_REQUEST)).getSession(true);
	     loginSession = (LoginSession) session.getAttribute("loginSession");
	}
	
	
	@Override
	public PrivatePerson getPrivatePerson() {
		getLoginSession();
		return loginSession.getPrivatePerson();
	}

	@Override
	public void addFriendToGroup(FriendGroup friendGroup, Person friend) {
		getLoginSession();
		loginSession.addFriendToGroup(friendGroup, friend);
	}

	@Override
	public void removeFriendFromGroup(FriendGroup friendGroup, Person friend) {
		getLoginSession();
		loginSession.removeFriendFromGroup(friendGroup, friend);
	}

	@Override
	public void setName(String name) {
		getLoginSession();
		loginSession.setName(name);
	}

	@Override
	public void setMessage(String message) {
		getLoginSession();
		loginSession.setMessage(message);
	}

	@Override
	public void blockFriend(Person friend) {
		getLoginSession();
		loginSession.blockFriend(friend);
	}

	@Override
	public void unblockFriend(Person friend) {
		getLoginSession();
		loginSession.unblockFriend(friend);
	}

	@Override
	public void createFriendGroup(String name, Set<Person> initialMembers) {
		getLoginSession();
		loginSession.createFriendGroup(name, initialMembers);
	}

	@Override
	public void removeFriendGroup(FriendGroup friendGroup) {
		getLoginSession();
		loginSession.removeFriendGroup(friendGroup);
	}

	@Override
	public void requestFriend(String phoneOrEmail) {
		getLoginSession();
		loginSession.requestFriend(phoneOrEmail);
	}

	@Override
	public void removeFriend(Person friend) {
		getLoginSession();
		loginSession.removeFriend(friend);
	}

	@Override
	public void respondToFriendRequest(FriendRequest friendRequest,
			boolean accept) {
		getLoginSession();
		loginSession.respondToFriendRequest(friendRequest, accept);
	}

	@Override
	public Location getLocationOf(Person friend) {
		getLoginSession();
		return loginSession.getLocationOf(friend);
	}

	@Override
	public void setMyLocation(Location location) {
		getLoginSession();
		loginSession.setMyLocation(location);
	}

	@Override
	public void logout() {
		getLoginSession();
		loginSession.logout();
		killLoginSession();
	}

}
