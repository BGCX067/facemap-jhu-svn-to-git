package edu.jhu.cs.oose.fall2011.facemap.webservice;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import edu.jhu.cs.oose.fall2011.facemap.client.webservice.Command;
import edu.jhu.cs.oose.fall2011.facemap.client.webservice.Serializer;
import edu.jhu.cs.oose.fall2011.facemap.login.LoginSession;
import edu.jhu.cs.oose.fall2011.facemap.webservice.session.LoginSessionManager;

@Path("/LoginSession")
public class LoginSessionService {
	
	static final byte[] EMPTY = new byte[0];
	
	@POST
	@Path("/{id}")
	public byte[] invoke(@PathParam("id") String id, byte [] input) throws IOException, ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NamingException {
		LoginSessionManager loginSessionManager = InitialContext.doLookup("java:global/FacemapServerApp/FacemapEjb/LoginSessionManager");
		LoginSession loginSession = loginSessionManager.retrieve(id);
		Command command = Command.fromBytes(input);	
		System.out.println(command);

		Method m = this.getMethodByName(loginSession, command.getMethod());
		Object result = m.invoke(loginSession, command.getArgs());
		
		if(command.getMethod().equals("logout")) {
			// need to remove reference to loginSession
			loginSessionManager.remove(id);
		}
		
		if(result != null) {
			System.out.println(result);
			return Serializer.toBytes(result);
		} else {
			return EMPTY;
		}
		
	}
	
	private Method getMethodByName(Object o, String name)
	{
		for ( Method m : o.getClass().getMethods())
		{
			if (m.getName().equals(name))
			{
				return m;
			}
		}
		throw new RuntimeException("Cant find method");
	}
}
