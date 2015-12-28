package edu.jhu.cs.oose.fall2011.facemap.accountRep;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;

import edu.jhu.cs.oose.fall2011.facemap.domainImpl.AccountImpl;
import edu.jhu.cs.oose.fall2011.facemap.domainImpl.FriendGroupImpl;
import edu.jhu.cs.oose.fall2011.facemap.domainImpl.FriendRequestImpl;
import edu.jhu.cs.oose.fall2011.facemap.domainImpl.LocationImpl;
import edu.jhu.cs.oose.fall2011.facemap.domainImpl.PersonImpl;
import edu.jhu.cs.oose.fall2011.facemap.domainImpl.PrivatePersonImpl;
import edu.jhu.cs.oose.fall2011.facemap.server.AccountRepositoryException;

public class AccountRepositoryImpl {

	/**
	 * Looks for an Account with the given phone number or email address.
	 * 
	 * @param phoneOrEmail
	 *            "john@gmail.com" or "123-456-7890"
	 * @return Account with given phone number or email address or null if not
	 *         found
	 */
	AccountImpl findAccount(String phoneOrEmail) {

		AnnotationConfiguration config = new AnnotationConfiguration();
		config.addAnnotatedClass(AccountImpl.class);
		config.addAnnotatedClass(LocationImpl.class);
		config.addAnnotatedClass(PrivatePersonImpl.class);
		config.addAnnotatedClass(PersonImpl.class);
		config.addAnnotatedClass(FriendRequestImpl.class);
		config.addAnnotatedClass(FriendGroupImpl.class);

		config.configure("hibernate.cfg.xml");
		// new SchemaExport(config).create(true, true);

		SessionFactory factory = config.buildSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();

		// Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			List accounts = session.createQuery("from AccountImpl").list();
			// System.out.println(accounts.toString());
			for (Iterator iterator = accounts.iterator(); iterator.hasNext();) {
				AccountImpl account = (AccountImpl) iterator.next();
				if (account.getPrivatePerson().getSelf().getPhoneNumber()
						.equals(phoneOrEmail)
						|| account.getPrivatePerson().getSelf().getEmail()
								.equals(phoneOrEmail))
					return account;
			}
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Updates an existing account
	 * 
	 * @param account
	 * @throws AccountRepositoryException
	 *             if saving instance fails
	 */
	void save(AccountImpl account) throws AccountRepositoryException {

		AnnotationConfiguration config = new AnnotationConfiguration();
		config.addAnnotatedClass(AccountImpl.class);
		config.addAnnotatedClass(LocationImpl.class);
		config.addAnnotatedClass(PrivatePersonImpl.class);
		config.addAnnotatedClass(PersonImpl.class);
		config.addAnnotatedClass(FriendRequestImpl.class);
		config.addAnnotatedClass(FriendGroupImpl.class);

		config.configure("hibernate.cfg.xml");

		// new SchemaExport(config).create(true, true);

		int tempId = account.getAccountId();

		SessionFactory factory = config.buildSessionFactory();
		Session session = factory.getCurrentSession();

		session.beginTransaction();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			session.merge(account);
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}

	}

	/**
	 * creates and returns a new Account instance with the given parameters
	 * 
	 * @param email
	 * @param phoneNo
	 * @param name
	 * @param password
	 * @return new Account instance
	 * @throws AccountRepositoryException
	 *             if creating
	 */
	AccountImpl createAccount(String email, String phoneNo, String name,
			String password) {

		AnnotationConfiguration config = new AnnotationConfiguration();
		config.addAnnotatedClass(AccountImpl.class);
		config.addAnnotatedClass(LocationImpl.class);
		config.addAnnotatedClass(PrivatePersonImpl.class);
		config.addAnnotatedClass(PersonImpl.class);
		config.addAnnotatedClass(FriendRequestImpl.class);
		config.addAnnotatedClass(FriendGroupImpl.class);

		config.configure("hibernate.cfg.xml");

		// for the first time running comment out then
		// the next time comment out in order to prevent data lost
		// new SchemaExport(config).create(true, true);

		SessionFactory factory = config.buildSessionFactory();
		Session session = factory.getCurrentSession();

		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			// lists all accounts on the data base
			List accounts = session.createQuery("from AccountImpl").list();

			// actual account that we want to store
			AccountImpl account = new AccountImpl();

			for (Iterator iterator = accounts.iterator(); iterator.hasNext();) {
				// makes a temporary account object to hold account
				AccountImpl tempAccount = (AccountImpl) iterator.next();

				// check whether account is already in the database
				if (!(tempAccount.getPrivatePerson().getSelf().getPhoneNumber()
						.equals(phoneNo) && tempAccount.getPrivatePerson()
						.getSelf().getEmail().equals(email)))
					// sets password
					account.setPassword(password);
				// creates private person
				PrivatePersonImpl pPerson = new PrivatePersonImpl();
				// links private person to the account
				account.setPrivatePerson(pPerson);
				// creates person
				PersonImpl person = new PersonImpl();
				// links private person to person
				pPerson.setSelf(person);

				// sets person's informations
				person.setEmail(email);
				person.setName(name);
				person.setPhoneNumber(phoneNo);

				// saves objects
				session.save(account);
				session.save(pPerson);
				session.save(person);
				// returns account
				return account;
			}
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}

		session.getTransaction().commit();
		return null;
	}

	/*// testing methods
	// in order to test i made classes as static
	// in real case make it without static
	public static void main(String[] args) throws AccountRepositoryException {
		String email = "yyyy@jhu.edu";
		String phone = "1111";
		String name = "yyy";
		String pass = "y";

		String email1 = "xxxx@jhu.edu";
		String phone1 = "2222";
		String name1 = "xxx";
		String pass1 = "x";

		//create user 1
		if(findAccount("yyyy@jhu.edu").equals(null)){
		createAccount(email,phone,name,pass);
		}
		
		
		// create user 2
		if(findAccount("xxxx@jhu.edu").equals(null)){
		createAccount(email1,phone1,name1,pass1);
		}
		

		// find user 1
		AccountImpl update = findAccount("xxxx@jhu.edu");

		// update user 1's name
		update.getPrivatePerson().getSelf().setName("zzz");
		
		save(update);
		
		System.out.println("after:  "+findAccount("xxxx@jhu.edu").getPrivatePerson().getSelf().getName());

	}
*/
}
