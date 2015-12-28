package edu.jhu.cs.oose.fall2011.facemap.webservice;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import edu.jhu.cs.oose.fall2011.facemap.client.webservice.Command;
import edu.jhu.cs.oose.fall2011.facemap.login.LoginSession;
import edu.jhu.cs.oose.fall2011.facemap.webservice.session.LoginSessionManager;

@Path("/LoginService")
public class LoginService {
	
	
	static final byte[] EMPTY = new byte[0];

	
	@POST
	public byte[] invoke(byte[] input) throws IOException, ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NamingException {
		edu.jhu.cs.oose.fall2011.facemap.login.LoginService loginService = InitialContext.doLookup("java:global/FacemapServerApp/FacemapEjb/LoginServiceImpl!edu.jhu.cs.oose.fall2011.facemap.login.LoginService");
		LoginSessionManager loginSessionManager = InitialContext.doLookup("java:global/FacemapServerApp/FacemapEjb/LoginSessionManager");
		
		Command command = Command.fromBytes(input);
		System.out.println(command);
		
		Method m = loginService.getClass().getMethod(command.getMethod(), command.getArgTypes());
		Object result = m.invoke(loginService, command.getArgs());
		
		// need to store the loginSession instance returned
		if(command.getMethod().equals("login")) {
			String id = loginSessionManager.register((LoginSession) result);
			return id.getBytes();
		}
		
		
		return EMPTY;
	}

}
