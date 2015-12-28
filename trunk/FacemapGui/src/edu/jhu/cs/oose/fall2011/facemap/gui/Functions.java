package edu.jhu.cs.oose.fall2011.facemap.gui;

import java.util.HashSet;
import java.util.Set;

import edu.jhu.cs.oose.fall2011.facemap.client.ClientApp;
import edu.jhu.cs.oose.fall2011.facemap.domain.FriendGroup;
import edu.jhu.cs.oose.fall2011.facemap.domain.Person;

/**
 * Some functions that are called in activities
 * @author Ying Dou
 *
 */
public abstract class Functions {
	/**
	 * find FriendGroup with certain GroupName
	 * @param groups
	 * @param name
	 * @return
	 */
	public static FriendGroup SearchFriendGroupByName(Set<FriendGroup> groups, String name) {
		FriendGroup friendGroup = null;
		for (FriendGroup fg: groups) {
			if (fg.getGroupName().equals(name)) {
				friendGroup = fg;
				break;
			}
		}
		return friendGroup;
	}
	
	/**
	 * find Person with certain email
	 * @param contacts
	 * @param email
	 * @return Person with the email
	 */
	public static Person SearchPersonByEmail(Set<Person> contacts, String email) {
		Person person = null;
		for (Person p : contacts) {
			if (p.getEmail().equals(email)) {
				person = p;
				break;
			}
		}
		return person;
	}
	/**
	 * test if a person is in a group
	 * @param fg
	 * @param person
	 * @return true/false
	 */
	public static boolean isPersonInFriendGroup(FriendGroup fg, Person person) {
		for (Person p : fg.getFriendsInGroup()) {
			if (p.getEmail().equals(person.getEmail())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * get set of unclassified(not in user defined groups) friends
	 * @param clientApp ClientApp object
	 * @return unclassified friends
	 */
	public static Set<Person> getUnclassifiedFriends(ClientApp clientApp) {
		Set<Person> unclassifiedFriends = new HashSet<Person>();
		for (Person f :  clientApp.getLoggedInUser().getFriends()) {
			boolean result = true;
			for (FriendGroup fg : clientApp.getLoggedInUser().getFriendGroups()) {
				if (isPersonInFriendGroup(fg, f)) {
					result = false;
					break;
				}
			}
			if (result == true) {
				unclassifiedFriends.add(f);
			}
		}
		return unclassifiedFriends;
	}
	
	/**
	 * test if a person is blocked
	 * @param email email of the person to be tested
	 * @param clientApp
	 * @return true/false
	 */
	public static boolean isBlocked(String email, ClientApp clientApp) {
		for (Person p : clientApp.getLoggedInUser().getBlockedFriends()) {
			if (p.getEmail().equals(email)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * calculate distance between two points in mile
	 * @param lat1 latitude 1
	 * @param lon1 longitude 1
	 * @param lat2 latitude 2
	 * @param lon2 longitude 2
	 * @return the distance in mile
	 */
	public static double findDistanceMile(double lat1, double lon1, double lat2, double lon2)
	{
	    double pk = (180/3.14169);

	    double a1 = (lat1 / pk);
	    double a2 = (lon1 / pk);
	    double b1 = (lat2 / pk);
	    double b2 = (lon2 / pk);

	    double t1 = Math.cos(a1)*Math.cos(a2)*Math.cos(b1)*Math.cos(b2);
	    double t2 = Math.cos(a1)*Math.sin(a2)*Math.cos(b1)*Math.sin(b2);
	    double t3 = Math.sin(a1)*Math.sin(b1);
	    double tt = Math.acos(t1 + t2 + t3);
	   
	    return 3956 * tt;
	}
}
