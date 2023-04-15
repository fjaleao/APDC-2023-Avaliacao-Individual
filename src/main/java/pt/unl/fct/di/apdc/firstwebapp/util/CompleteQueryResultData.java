package pt.unl.fct.di.apdc.firstwebapp.util;

import com.google.cloud.Timestamp;

public class CompleteQueryResultData extends BaseQueryResultData {

    public String password;
    public Timestamp creationTime;
    public String type;
    public boolean state;
	public boolean visible;
	public String mobilePhoneNumber;
	public String phoneNumber;
	public String occupation;
	public String workAddress;
	public String address;
	public String secondAddress;
	public String postCode;
	public String nif;

    public CompleteQueryResultData() {

    }

    public CompleteQueryResultData(String username, String name, String email, String password, Timestamp creationTime,
            String type, boolean state, boolean visible, String mobilePhoneNumber, String phoneNumber,
            String occupation, String workAddress, String address, String secondAddress, String postCode, String nif) {
        super(username, name, email);
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
