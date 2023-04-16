package pt.unl.fct.di.apdc.firstwebapp.util;

import com.google.cloud.Timestamp;

public class CompleteQueryResultData {

    private String username;
    private String name;
    private String email;
    private String password;
    private Timestamp creationTime;
    private String type;
    private boolean state;
	private boolean visible;
	private String mobilePhoneNumber;
	private String phoneNumber;
	private String occupation;
	private String workAddress;
	private String address;
	private String secondAddress;
	private String postCode;
	private String nif;

    public CompleteQueryResultData() {

    }

    public CompleteQueryResultData(String username, String name, String email, String password, Timestamp creationTime,
            String type, boolean state, boolean visible, String mobilePhoneNumber, String phoneNumber,
            String occupation, String workAddress, String address, String secondAddress, String postCode, String nif) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.creationTime = creationTime;
        this.type = type;
        this.state = state;
        this.visible = visible;
        this.mobilePhoneNumber = mobilePhoneNumber;
        this.phoneNumber = phoneNumber;
        this.occupation = occupation;
        this.workAddress = workAddress;
        this.address = address;
        this.secondAddress = secondAddress;
        this.postCode = postCode;
        this.nif = nif;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the creationTime
     */
    public Timestamp getCreationTime() {
        return creationTime;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the state
     */
    public boolean isState() {
        return state;
    }

    /**
     * @return the visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @return the mobilePhoneNumber
     */
    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @return the occupation
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     * @return the workAddress
     */
    public String getWorkAddress() {
        return workAddress;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return the secondAddress
     */
    public String getSecondAddress() {
        return secondAddress;
    }

    /**
     * @return the postCode
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * @return the nif
     */
    public String getNif() {
        return nif;
    }
    
}
