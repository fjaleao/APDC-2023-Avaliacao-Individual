package pt.unl.fct.di.apdc.firstwebapp.util;

public class UserData {
	
	private String username;
	private String password;
	private String email;
	private String name;

	private AuthToken token;
	
	public UserData() {
		
	}

	public UserData(String username, String password, String email) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public boolean isValid() {
		return !(this.username == null || this.password == null || this.email == null);
	}

	public void login(AuthToken token) {
		this.token = token;
	}

	public AuthToken getToken() {
		return this.token;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	

}
