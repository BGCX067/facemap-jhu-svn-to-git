package edu.jhu.cs.oose.fall2011.facemap.client.v3;

import java.util.Arrays;

import edu.jhu.cs.oose.fall2011.facemap.client.webservice.Command;
import edu.jhu.cs.oose.fall2011.facemap.login.LoginService;
import edu.jhu.cs.oose.fall2011.facemap.login.LoginSession;

public class RestLoginService implements LoginService {

	private String url, host, port;
	private ServerInvoker serverInvoker;
	
	public RestLoginService(String host, String port)
	{
		this.host = host;
		this.port = port;
		this.url = "http://" + host + ":" + port +"/FacemapWeb/rest/LoginService";
		serverInvoker = new ServerInvoker();
	}
	
	@Override
	public LoginSession login(String email, String password) {
		byte[] returnedBytes = null;
		
		try {
			returnedBytes = serverInvoker.postToServer(url, new Command("login", email, password).toBytes());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		
		String sessionId = new String(returnedBytes); // get session id from result
		return new RestLoginSession(sessionId, host, port);
	}

	@Override
	public void register(String email, String phoneNo, String name,
			String password) {
		try {
			serverInvoker.postToServer(url, new Command("register", email, phoneNo, name, password).toBytes());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		
	}

}
