package edu.jhu.cs.oose.fall2011.facemap.domainImpl;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;


/**
 * This class contains all the testing.
 * It tests all the tables that is created and also testing all the 
 * relationships between tables.
 * 
 * @author Orhan OZGUNER
 *
 */
public class Test {
	public static void main(String[] args) {
		//creates an annotation configuration object
		AnnotationConfiguration config = new AnnotationConfiguration();
		
		//adds all the annotates classes
		//note: I could add them in the cfg.xml but in order to test each 
		//relationship separately , add them one by one 
		config.addAnnotatedClass(AccountImpl.class);
		config.addAnnotatedClass(PrivatePersonImpl.class);
		config.addAnnotatedClass(PersonImpl.class);
		config.addAnnotatedClass(FriendGroupImpl.class);
		config.addAnnotatedClass(FriendRequestImpl.class);
		config.addAnnotatedClass(LocationImpl.class);
	
		//add the configuration xml file
		config.configure("hibernate.cfg.xml");
		
		//create new schema out of configuration
		new SchemaExport(config).create(true, true);

		//create Session Factory object and build the session
		SessionFactory factory = config.buildSessionFactory();
		//get the current session to a session object
		Session session = factory.getCurrentSession();
		//starts the transaction
		session.beginTransaction();
		
		// Adding an account and its related private person
		// and information as Person to private person 
		AccountImpl account1 = new AccountImpl();
		account1.setPassword("1");
		
		PrivatePersonImpl pPerson1 = new PrivatePersonImpl();
		
		PersonImpl person1 = new PersonImpl();
		person1.setName("Person 1");
		person1.setEmail("person1@jhu.edu");
		person1.setPhoneNumber("1111111");
		
		//sets the person to private person and
		//private person to the account
		pPerson1.setSelf(person1);
		account1.setPrivatePerson(pPerson1);
		
		
		AccountImpl account2 = new AccountImpl();
		account2.setPassword("2");
		
		PrivatePersonImpl pPerson2 = new PrivatePersonImpl();
		
		PersonImpl person2 = new PersonImpl();
		person2.setName("Person 2");
		person2.setEmail("person2@jhu.edu");
		person2.setPhoneNumber("2222222");
		
		pPerson2.setSelf(person2);
		account2.setPrivatePerson(pPerson2);
		
		
		AccountImpl account3 = new AccountImpl();
		account3.setPassword("3");		
		
		PrivatePersonImpl pPerson3 = new PrivatePersonImpl();
		
		PersonImpl person3 = new PersonImpl();
		person3.setName("Person 3");
		person3.setEmail("person3@jhu.edu");
		person3.setPhoneNumber("3333333");
		
		pPerson3.setSelf(person3);
		account3.setPrivatePerson(pPerson3);
	
		//make an account and a person for blocking
		AccountImpl blockedAccount = new AccountImpl();
		blockedAccount.setPassword("4");		
		
		PrivatePersonImpl blockedPrivatePerson = new PrivatePersonImpl();
		
		PersonImpl blockedPerson = new PersonImpl();
		blockedPerson.setName("Blocked Person");
		blockedPerson.setEmail("blockedPerson3@jhu.edu");
		blockedPerson.setPhoneNumber("4444444");
		
		blockedPrivatePerson.setSelf(blockedPerson);
		blockedAccount.setPrivatePerson(blockedPrivatePerson);
		
		//testing FriendGroup. created groups and added groups in to
		// a set and make a groups list for a private person
		//this test allows me to test the relation between private person
		//and friendGroup. Also it allows me to test the relation
		//between group and person
		FriendGroupImpl group1 = new FriendGroupImpl();
		group1.setGroupName("Group-1");
		
		FriendGroupImpl group2 = new FriendGroupImpl();
		group2.setGroupName("Group-2");
		
		FriendGroupImpl group3 = new FriendGroupImpl();
		group3.setGroupName("Group-3");
		
		FriendGroupImpl group4 = new FriendGroupImpl();
		group4.setGroupName("Group-4");
		
		
		//adding person to groups
		group1.getFriendsInGroup().add(person1);
		person1.getGroup().add(group1);
		
		group1.getFriendsInGroup().add(person2);
		person2.getGroup().add(group1);

		group2.getFriendsInGroup().add(person3);
		person3.getGroup().add(group2);
		
		group2.getFriendsInGroup().add(person1);
		person1.getGroup().add(group2);
		
		group3.getFriendsInGroup().add(person3);
		person3.getGroup().add(group3);
		
		group4.getFriendsInGroup().add(person1);
		person1.getGroup().add(group4);
		
		group4.getFriendsInGroup().add(person2);
		person2.getGroup().add(group4);
		
		//adding blockedPerson to private person 1	
		pPerson1.getBlockedFriends().add(blockedPerson);
		
		//creating requests that I sent
	    FriendRequestImpl requestISent1 = new FriendRequestImpl();
	    FriendRequestImpl requestISent2 = new FriendRequestImpl();
		
	    //creating requests that waiting my response 
	    FriendRequestImpl requestWaitingMyResponse1 = new FriendRequestImpl();
	    FriendRequestImpl requestWaitingMyResponse2 = new FriendRequestImpl();
	    
	    //adding requests
	    pPerson1.getRequestsISent().add(requestISent1);
	    pPerson1.getRequestsISent().add(requestISent2);
	    
	    //adding requests
	    pPerson2.getRequestsAwaitingMyResponse().add(requestWaitingMyResponse1);
	    pPerson2.getRequestsAwaitingMyResponse().add(requestWaitingMyResponse2);
	    
		//now I am testing the location	
		LocationImpl location1 = new LocationImpl();
		location1.setLatitude(0);
		location1.setLongitude(0);
		
		LocationImpl location2 = new LocationImpl();
		location2.setLatitude(100);
		location2.setLongitude(100);
		
		
		session.save(account1);
		session.save(account2);
		session.save(account3);
		session.save(blockedAccount);
		
		session.save(pPerson1);
		session.save(pPerson2);
		session.save(pPerson3);
		session.save(blockedPrivatePerson);
		
		session.save(person1);
		session.save(person2);
		session.save(person3);
		session.save(blockedPerson);
		
		session.save(group1);
		session.save(group2);
		session.save(group3);
		session.save(group4);
		
		session.save(requestISent1);
		session.save(requestISent2);
		session.save(requestWaitingMyResponse1);
		session.save(requestWaitingMyResponse2);
		
		session.save(location1);
		session.save(location2);

		session.getTransaction().commit();
		
		//session = factory.openSession();
		//session.beginTransaction();
		//int x = orhan.getPersonId();
		//orhan = (PersonImpl) session.get(PersonImpl.class, x);
		
		//System.out.println(orhan.getEmail());
		
		//orhan.setEmail("degisti");
		//x = orhan.getPersonId();
		
		
		//orhan = (PersonImpl) session.get(PersonImpl.class, x);
		
		//System.out.println(orhan.getEmail());
		
		//session.getTransaction().commit();
		
	}

}
