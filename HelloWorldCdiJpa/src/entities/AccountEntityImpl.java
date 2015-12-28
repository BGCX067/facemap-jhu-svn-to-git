package entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AccountEntityImpl implements Account {
	@Id
	private String username;
	private String message;
	private String password;
	
	// required for JPA
	protected AccountEntityImpl() {}
	
	public AccountEntityImpl(String username, String message, String password) {
		this.username = username;
		this.message = message;
		this.password = password;
	}
	
	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
