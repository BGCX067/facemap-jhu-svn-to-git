package edu.jhu.cs.oose.fall2011.facemap.login;

public interface LoginService {
	LoginSession login(String email, String password);
	void register(String email, String phoneNo, String name, String password);
}
