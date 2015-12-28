package ws;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import edu.jhu.cs.oose.fall2011.facemap.login.LoginService;
import edu.jhu.cs.oose.fall2011.facemap.login.LoginServiceImpl;
import edu.jhu.cs.oose.fall2011.facemap.login.LoginSession;


//@WebService
public class LoginServiceWebService /*implements LoginService*/ {
	@EJB LoginServiceImpl loginSerivce;
	@Resource WebServiceContext wsContext;
	
	//@Override
	public void login(String email, String password) {
		LoginSession loginSession = loginSerivce.login(email, password);
		HttpServletRequest request = (HttpServletRequest) (wsContext.getMessageContext().get(MessageContext.SERVLET_REQUEST));
		HttpSession session = request.getSession(true);
		session.setAttribute("loginSession", loginSession);
		
		//return loginSession;
	}

	//@Override
	public void register(String email, String phoneNo, String name,
			String password) {
		loginSerivce.register(email, phoneNo, name, password);
	}

}
