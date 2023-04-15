package pt.unl.fct.di.apdc.firstwebapp.util;

public class AttributeChangeData extends UserData {

    private String name;
    private String email;
    private boolean visible;
	private String mobilePhoneNumber;
	private String phoneNumber;
	private String occupation;
	private String workAddress;
	private String address;
	private String secondAddress;
	private String postCode;
	private String nif;

    public AttributeChangeData() {
        
    }

    public AttributeChangeData(AuthToken token, String targetUsername, String name, String email, boolean visible, String mobilePhoneNumber,
            String phoneNumber, String occupation, String workAddress, String address, String secondAddress,
            String postCode, String nif) {
        super(token, targetUsername);
        this.name = name;
        this.email = email;
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
