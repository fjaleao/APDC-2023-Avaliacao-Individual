package pt.unl.fct.di.apdc.firstwebapp.util;

import java.util.UUID;

public class AuthToken {
	
	public static final long EXPIRATION_TIME = 1000*60*60*2; //2h
	
	private String username;

	private String tokenID;
	private long creationDate;
	private long expirationDate;
	
	public AuthToken() {
		
	}
	
	public AuthToken(String username) {
		
		this.username = username;
		this.tokenID = UUID.randomUUID().toString();
		this.creationDate = System.currentTimeMillis();
		this.expirationDate = this.creationDate + AuthToken.EXPIRATION_TIME;
		
	}

	public boolean isValid() {
		return System.currentTimeMillis() < this.expirationDate;
	}

	public String getUsername() {
		return username;
	}

	public String getTokenID() {
		return tokenID;
	}

	public void invalidate(long expirationData) {
		this.expirationDate = System.currentTimeMillis();
	}

}
