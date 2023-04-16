package pt.unl.fct.di.apdc.firstwebapp.util;

import java.util.UUID;

import com.google.cloud.Timestamp;

public class AuthToken {

	public static final String USERNAME = "token_user_id";
	public static final String ID = "token_id";
	public static final String CREATION_TIME = "token_creation_time";
	public static final String EXPIRATION_TIME = "token_expiration_time";
	public static final String VERIFICATION = "token_verification";
	
	public static final long TTL = 60*60*2; //2h in seconds
	
	private String username;
	private String tokenID;
	private long creationDate;
	private long expirationDate;
	private String verification;
	
	public AuthToken() {
		
	}
	
	public AuthToken(String username, String verification) {
		
		this.username = username;
		this.tokenID = UUID.randomUUID().toString();
		this.creationDate = Timestamp.now().getSeconds();
		this.expirationDate = this.creationDate + TTL;
		this.verification = verification;
		
	}

	public String getUsername() {
		return username;
	}

	public String getTokenID() {
		return tokenID;
	}

	/**
	 * @return the creationDate
	 */
	public long getCreationDate() {
		return creationDate;
	}

	/**
	 * @return the expirationDate
	 */
	public long getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @return the verification
	 */
	public String getVerification() {
		return verification;
	}

	

}
