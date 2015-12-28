package edu.jhu.cs.oose.fall2011.facemap.webservice.session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;

import edu.jhu.cs.oose.fall2011.facemap.login.LoginSession;


/**
 * Maintains references to LoginSession SFSB's for webservice
 * 
 * @author Daniel Cranford
 *
 */
@Singleton
@LocalBean
public class LoginSessionManager {
	private Map<String, LoginSession> loginSessionMap = new HashMap<String, LoginSession>();
	
	public String register(LoginSession loginSession) {
		String id = UUID.randomUUID().toString();
		loginSessionMap.put(id, loginSession);
		return id;
	}
	
	public void remove(String id) {
		loginSessionMap.remove(id);
	}
	
	public LoginSession retrieve(String id) {
		return loginSessionMap.get(id);
	}
}
