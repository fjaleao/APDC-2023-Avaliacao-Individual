package pt.unl.fct.di.apdc.firstwebapp.util;

public class UserData {
	
	public String username;
	public String password;
	public String email;
	public String name;
	
	public UserData() {
		
	}

	public UserData(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}

	public boolean isValid() {
		return !(this.username == null || this.password == null || this.email == null);
	}
	
	

}
