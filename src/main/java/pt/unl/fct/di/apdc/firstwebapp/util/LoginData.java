package pt.unl.fct.di.apdc.firstwebapp.util;

public class LoginData {
	
	public String username;
	public String password;
	public String email;
	public String name;
	
	public LoginData() {
		
	}

	public LoginData(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}

	public boolean isValid() {
		return !(this.username == null || this.password == null || this.email == null);
	}
	
	

}
