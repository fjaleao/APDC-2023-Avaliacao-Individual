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
	private Timestamp creationDate;
	private Timestamp expirationDate;
	private String verification;
	
	public AuthToken() {
		
	}
	
	public AuthToken(String username, String verification) {
		
		this.username = username;
		this.tokenID = UUID.randomUUID().toString();
		this.creationDate = Timestamp.now();
		this.expirationDate = Timestamp.ofTimeSecondsAndNanos(this.creationDate.getSeconds() + TTL, this.creationDate.getNanos());
		this.verification = verification;
		
	}

	public boolean isExpired() {
		return this.expirationDate.compareTo(Timestamp.now()) < 0;
	}

	public void invalidate(long expirationData) {
		this.expirationDate = Timestamp.now();
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
	public Timestamp getCreationDate() {
		return creationDate;
	}

	/**
	 * @return the expirationDate
	 */
	public Timestamp getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @return the verification
	 */
	public String getVerification() {
		return verification;
	}

	

}
