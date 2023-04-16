package pt.unl.fct.di.apdc.firstwebapp.util;

public class TokenData {
	
	private String username;
	private String tokenID;
	private long creationDate;
	private long expirationDate;
	private String verification;
	
	public TokenData() {}

    public TokenData(String username, String tokenID, long creationDate, long expirationDate, String verification) {
        this.username = username;
        this.tokenID = tokenID;
        this.creationDate = creationDate;
        this.expirationDate = expirationDate;
        this.verification = verification;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the tokenID
     */
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
